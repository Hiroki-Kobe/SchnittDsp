#! coding:utf-8
##########
# retutn preemphasied data
# INPUT: [1] data file(Interger of wavfiles) [2] coefficients of preemp. filter 
# OUTPUT: preemphasized data file (double) 
##########

import scipy.signal
import sys


# def: calc preemph
def pre_emph_filter(data, p):
    return scipy.signal.lfilter([1.0, -p], 1, data)



# variable
argvs  = sys.argv
input_file = argvs[1]
coef   = float(argvs[2])
output = input_file[:-4] + "_pre.txt" 



# Read data
data = open(input_file, "r")
data_lines = data.readlines()
data_list = [0] * len(data_lines)
 
for i in range(0, len(data_lines)): 
    val = data_lines[i].rstrip()
    data_list[i] = int(val)

print "[INFO] length: ", len(data_list)



# Do preemph
preemph_data = pre_emph_filter(data_list, coef)



# Write Results
f = open(output, "w")

for i in preemph_data:
    f.write(str(i)+"\n")

f.close()

