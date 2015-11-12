#! coding:utf_8

import wave
import numpy as np


def wav_read(filename):
    wf = wave.open(filename, "r")
    wf = getframerate()
    data = wf.readframes(wf.getnframes))
    wf = np.frombuffer(data, dtype="int16")
    wf.close()

   return data


