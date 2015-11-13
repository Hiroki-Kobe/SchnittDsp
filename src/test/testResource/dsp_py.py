#! coding: utf-8


import scipy.signal
import sys
import wave
import os
import numpy as np


class Dsp:
    def __init__(self):
        self.filename = ""
        self.samples = []
        self.preemph_coef = 0.0
        self.window = ""
        self.wav_path = "./snd"

    """ Set Prameters"""
    def set_filename(self, filename):
        filename = os.path.join(self.wav_path, filename)

        if os.path.exists(filename):
            self.filename = filename
        else: print ("No file exists")
        
        # set samples
        try:
            self.wf = wave.open(self.filename, "r")
            self.samples = self.wf.readframes(self.wf.getnframes())
            self.samples = np.frombuffer(self.samples, dtype="int16")

            print "filename: ", self.filename
            print "finish setting Integer samples"

        except IOError:
            print "Cannot read the file!"



    def set_preemph_coef(self, preemph_coef):
       self.preepmph_coef = preemph_coef

    
    def set_windowtype(self, windowtype):
        if(windowtype == "Hanning" or "Hamming"):
            self.window = windowtype
        else: 
            print("[ERROR] window type = Hamming or Hanning")



    """ Method for DSP"""
    def getInteger(self):
        return self.samples
 
        
        
        
