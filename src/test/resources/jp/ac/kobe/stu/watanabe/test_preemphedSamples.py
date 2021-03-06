#! coding: utf-8

import lib.dsp_py as dsp_py
import sys
from pylab import *

##########################
# Input: wav file name for processing
#########################

# Parameter
argv          = sys.argv
wav_filename  = argv[1]
FFTN          = 512
WINDOW_LEN    = 20 #ms
WINDOW        = "Hanning"
PREEMPH_COEF  = 0.97
CH            = 20
SMP_RATE      = 16000
SHIFT_LEN     = 10 #ms#


# Set parmeters
dsp = dsp_py.Dsp()
dsp.set_filename(wav_filename)         \
   .set_preemph_coef(PREEMPH_COEF)     \
   .set_windowtype(WINDOW)             \
   .set_fftn(FFTN)                     \
   .set_mfcc_ch(CH)


# sample number of window
win_samp_no = int((WINDOW_LEN / 1000.0) * SMP_RATE)
assert(win_samp_no == 320)

# get integer
int_samples = dsp.do_wav2int()
# get preemphed integer
tempSamples = [0] * (FFTN)
tempSamples[:win_samp_len] = int_samples[:win_samp _len]
preemphed_samples = dsp.do_preemph(tempSamples)

# write values
preemphed_samples_file = "preemph_samples.txt"
f = open(preemphed_samples_file, "w")
for i in range(0, FFTN):
    value = str(preemphed_samples[i])  + "\n"
    f.write(value)
f.close()

