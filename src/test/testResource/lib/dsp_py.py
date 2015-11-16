#! coding: utf-8


import scipy.signal
import sys
import wave
import os
import numpy as np
from pylab import *




class Dsp:
    """ Class for digital signal processing

        This HAVE TO set:
            - (1) wav file names
            - (2) preemphasized coefficients
            - (3) window function type
            - (4) fft size
        This can calculate:
            - (1) preemphsized samples
            - (2) windowed sampels
            - (3) amplitude spectrum
            - (4) mfcc
    """

    def __init__(self):
        # parameter
        self.filename     = ""
        self.samples      = []
        self.preemph_coef = 0.0
        self.windowtype   = ""
        self.fftn         = 0
        self.hz           = 0
        self.mfcc_ch      = 0

        # wav path
        self.wav_path     = "./snd"


    ### Set filename & Samples 
    def set_filename(self, filename):
        filename = os.path.join(self.wav_path, filename)

        if os.path.exists(filename):
            self.filename = filename
        else: print ("No file exists")
        
               
        return self


    ### Set fftN
    def set_fftn(self, fftn=512):
        if fftn > 0 and (fftn & (fftn - 1)) == 0:
            self.fftn = fftn
            print "[INFO] fftN: ", self.fftn
           

        else: print "[ERROR]: fftn has to be the power of 2"

        return self



    ### Set Preemph coef
    def set_preemph_coef(self, preemph_coef=0.97):
        self.preemph_coef = preemph_coef
        print "[INFO] preemph coef: ",  self.preemph_coef

        return self


    
    ### Set windowtype: Hanning or Hamming
    def set_windowtype(self, windowtype="Hanning"):
        if windowtype == "Hanning" or windowtype == "Hamming":
            self.windowtype = windowtype
            print self.windowtype
        
        else: 
            print("[ERROR] window type = Hamming or Hanning")

        return self

    ### Set MFCC ch
    def set_mfcc_ch(self, ch=20):
        if isinstance(ch, int): 
            self.mfcc_ch = ch
            return self
            

        else:
            raise Exception("Mfcc ch has to be integer")



    ### Get parameter
    def get_filename(self):
        return self.filename

    def get_fftn(self):
        return self.fftn

    def get_preemph_coef(self):
        return self.preemph_coef

    def get_windowtype(self):
        return self.windowtype



    ### Method fotr DSP
    def do_wav2int(self, plotting=False, saving=False):
        try:
            self.wf = wave.open(self.filename, "r")
            self.hz = self.wf.getframerate()

            self.samples = self.wf.readframes(self.wf.getnframes())
            self.samples = np.frombuffer(self.samples, dtype="int16")
         
            print "filename: ", self.filename
            print "finish setting Integer samples"

        except IOError:
            print "Cannot read the file!"


        if(plotting):
            self.t = np.arange(0.0, len(self.samples)/ float(self.hz), 1/float(self.hz))

            plot(self.t * 1000, self.samples)
            xlabel("time[ms]")
            ylabel("amplitude")

   
            if(saving):
                self.save_filename = self.filename[:-4] + "_waveform" + ".png"
                savefig(self.save_filename)
          
            show()

        return self.samples



    def do_preemph(self, signal):
        self.signal = signal
        return scipy.signal.lfilter([1.0, -self.preemph_coef], 1, self.signal)



    def do_preemph2(self, signal):
        self.signal = signal
        self.preemph_coef_list = [1.0, -self.preemph_coef]
        self.preemphed_samples = [0.0] * len(self.signal)

        for n in range(0, len(self.signal)):
            for i in range(0, len(self.preemph_coef_list)):
                if n - i >= 0:
                    self.preemphed_samples[n] += self.preemph_coef_list[i] * self.signal[n-i]

        return self.preemphed_samples



    def do_windowning(self, signal):
        try:
            self.signal = signal

            if  self.windowtype  == "Hamming":
                self.window = np.hamming(self.fftn)
            elif self.windowtype == "Hanning":
                self.window = np.hanning(self.fftn)

            return self.signal * self.window

        except:
            print "The length of signal have to match the fftN"


    def do_fft(self, signal, plotting=False, saving=False):
        if len(signal) == self.fftn:
            self.signal = signal
            self.nyq = self.fftn/2

            self.mag_spec = np.abs(np.fft.fft(self.signal, self.fftn))[:self.nyq]
            self.freq_scale = np.fft.fftfreq(self.fftn, d=1.0/self.hz)[:self.nyq]

           
            if(plotting):
                plot(self.freq_scale, self.mag_spec)
                xlabel("frequency [Hz]")
                ylabel("amplitude sapectrum")
                if(saving):
                    save_filename = self.filename[:-4] + "_spec" + ".png"
                    savefig(save_filename)


                show()

            return self.mag_spec

        else:
            print "[ERROR]: the length of signal has to mathch the fftn"

    def mel_filterbank(self, signal, plotting = False):
        self.signal = signal
        self.fmax = self.hz / 2
        self.melmax = 1127.01048 * np.log(self.fmax / 700 + 1)

        self.nmax = self.fftn/ 2
        self.df = self.hz / self.fftn

        self.dmel = self.melmax / (self.mfcc_ch + 1)
        self.melcenters = np.arange(1, self.mfcc_ch + 1) * self.dmel

        self.fcenters = 700.0 * (np.exp(self.melcenters / 1127.01048) - 1.0)
        self.indexcenter = np.round(self.fcenters / self.df)

        # 各フィルタの開始位置のインデックス
        self.indexstart = np.hstack(([0], self.indexcenter[0:self.mfcc_ch - 1]))
        # 各フィルタの終了位置のインデックス
        self.indexstop = np.hstack((self.indexcenter[1:self.mfcc_ch], [self.nmax]))

        self.filterbank = np.zeros((self.mfcc_ch, self.nmax))
        for c in np.arange(0, self.mfcc_ch):
            # 三角フィルタの左の直線の傾きから点を求める
            self.increment= 1.0 / (self.indexcenter[c] - self.indexstart[c])
            for i in np.arange(self.indexstart[c], self.indexcenter[c]):
                self.filterbank[c, i] = (i - self.indexstart[c]) * self.increment
            # 三角フィルタの右の直線の傾きから点を求める
            self.decrement = 1.0 / (self.indexstop[c] - self.indexcenter[c])
            for i in np.arange(self.indexcenter[c], self.indexstop[c]):
                self.filterbank[c, i] = 1.0 - ((i - self.indexcenter[c]) * self.decrement)


        for c in np.arange(0, self.mfcc_ch):
            plot(np.arange(0, self.fftn/2) * self.df, self.filterbank[c])
        show()

        self.mspec = []
        for c in np.arange(0, self.mfcc_ch):
            self.mspec.append(np.log10(sum(self.signal * self.filterbank[c])))
        self.mspec = np.array(self.mspec)


        plot(self.fcenters, self.mspec, "o-")
        xlabel("frequency")
        xlim(0, 8000)
        show()

        self.ceps = scipy.fftpack.realtransforms.dct(self.mspec, type=2, norm="ortho", axis=-1)

        return self.ceps[:12]
