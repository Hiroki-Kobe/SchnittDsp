#! coding: utf-8

import lib.dsp_py as dsp_py
import sys
from pylab import *

##########################
# Input: wav file name for processing
#########################


##########################
# Parameter
##########################
argv = sys.argv

filename     = argv[1]
FFTN         = 512
WINDOW_LEN   = 20 #ms
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
coef = dsp.get_preemph_coef()
window_type = dsp.get_windowtype()



##################
# Do DSP
##################
### Integer
int_samples = dsp.do_wav2int()

### Preemph
tempSamples = [0] * (FFTN-1)
tempSamples[0:319] = int_samples[:320]

print "temp", tempSamples, len(tempSamples)



preemphed_samples = dsp.do_preemph(tempSamples)

### Windowning
windowed_samples = dsp.do_windowning(preemphed_samples)

### FFT
fft_spec = dsp.do_fft(windowed_samples, plotting=False, saving=False)

### MFCC
mfcc = dsp.do_mfcc(fft_spec)
print "sum banked amp: ", mfcc

plot(range(0, 12), mfcc[:12])
savefig("mfcc_dsp.png")

show()

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

### mfcc
#mfcc_samples_file  = "mfcc_samples.txt"
#f = open(mfcc_samples_file, "w")
#
#for i in range(0, len(mfcc)):
#    value = str(mfcc[i]) + "\n"
#    f.write(value)
#
# f.close()

