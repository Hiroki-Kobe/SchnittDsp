#! coding:utf_8
##########
# retutn interger array of wave file
# INPUT: [1] wav file
# OUTPUT: preemphasized data file (int) 
##########
import wave
import numpy as np
import sys



def wav_read(filename):
    wf = wave.open(filename, "r")
    fs = wf.getframerate()
    data = wf.readframes(wf.getnframes())
    data = np.frombuffer(data, dtype="int16")
    wf.close()

    return data


# [1] snd 
argvs = sys.argv

filename = argvs[1]
output = filename[:-4] + "_int.txt"

filename = "./snd/" + filename
print "[INFO] Filename: " + filename
print "[INFO] Output: " + output


data = wav_read(filename)
f = open(output, 'w')
for i in data:

    value = str(i) + "\n"
    f.write(value)

f.close()
