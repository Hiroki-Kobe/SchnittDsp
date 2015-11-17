#! coding: utf-8

import lib.dsp_py as dsp_py

##########################
# Parameter
##########################
filename     = "test1.wav"
FFTN         = 512
WINDOW       = "Hamming"
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
#  Check parameter
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
print "[int_samples]", int_samples
print "length", len(int_samples)


# print "[Int]: ", int_samples

### Preemph
preemphed_samples = dsp.do_preemph(int_samples)

### Windowning
windowed_samples = dsp.do_windowning(preemphed_samples[:FFTN])

### FFT
fft_spec = dsp.do_fft(windowed_samples[:FFTN], plotting=False, saving=False)
# print "[FFT_spec]: ", fft_spec


### MFCC
mfcc = dsp.mel_filterbank(CH, windowed_samples)
# print  mfcc



#################
#  write the values to text files
#################

## Integer
int_samples_file = "int_samples.txt" 
f = open(int_samples_file, "w")
for i in range(0, len(int_samples)):
    value = str(int_samples[i]) + "\n"
    f.write(value)

#f.close()

### Preemphed samples
preemphed_samples_file = "preemph_samples.txt"
f = open(preemphed_samples_file, "w")
for i in range(0, len(preemphed_samples)):
    value = str(preemphed_samples[i])  + "\n"
    f.write(value)

f.close()


### Windowed samples
windowed_samples_file = "windowed_samples.txt"
f = open(windowed_samples_file, "w")
for i in windowed_samples:
    value = str(windowed_samples[i]) + "\n"
    f.write(value)

f.close()



