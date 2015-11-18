#! coding: utf-8

import lib.dsp_py as dsp_py
import sys


##########################
# Input: wav file name for processing
#########################


##########################
# Parameter
##########################
argv = sys.argv

filename     = argv[1]
FFTN         = 512
WINDOW       = "Hanning"
PREEMPH_COEF = 0.97
CH           = 20


#########################
# Set parmeters
#########################
dsp = dsp_py.Dsp()
dsp.set_filename(filename)             \
   .set_preemph_coef(PREEMPH_COEF)     \
   .set_windowtype(WINDOW)             \
   .set_fftn(FFTN)                     \
   .set_mfcc_ch(CH)



#########################
#  Check the parameters
#########################
fftn = dsp.get_fftn()
print fftn

coef = dsp.get_preemph_coef()
print coef

window_type = dsp.get_windowtype()
print window_type


##################
# Do DSP
##################
### Integer
int_samples = dsp.do_wav2int()

### Preemph
preemphed_samples = dsp.do_preemph(int_samples)

### Windowning
windowed_samples = dsp.do_windowning(preemphed_samples[:FFTN])

### FFT
fft_spec = dsp.do_fft(windowed_samples[:FFTN], plotting=False, saving=False)

### MFCC
mfcc = dsp.mel_filterbank(CH, windowed_samples)



#################
#  write the values to text files
#################
## Get Integer
int_samples_file = "int_samples.txt" 
f = open(int_samples_file, "w")
for i in range(0, len(int_samples)):
    value = str(int_samples[i]) + "\n"
    f.write(value)

f.close()

### Preemphed samples
preemphed_samples_file = "preemph_samples.txt"
f = open(preemphed_samples_file, "w")
for i in range(0, FFTN):
    value = str(preemphed_samples[i])  + "\n"
    f.write(value)

f.close()


### Windowed samples
windowed_samples_file = "windowed_samples.txt"
f = open(windowed_samples_file, "w")
for i in range(0, FFTN):
    value = str(windowed_samples[i]) + "\n"
    f.write(value)

f.close()


### fft samples
fft_samples_file = "fft_samples.txt"
f = open(fft_samples_file, "w")
for i in range(0, FFTN/2):
    value = str(fft_spec[i]) + "\n"
    f.write(value)

f.close()
