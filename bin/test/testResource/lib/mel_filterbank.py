#! coding: utf-8
import numpy as np


class Mel_filterbank:
    """ Class for create Mel-filter Bank"""

    def __ini__(self):


    def hz2mel(self, f):
        """ translate Hz to mel"""
        return 1127.01048 * np.log(f/ 700 + 1)

    def mel2hz(self, mel):
        
""" translate mel to Hz"""
        return 700.0 * (np.exp(mel/ 1127.01048) - 1.0)

    def mel_filterbank(self):
        # Nyquist freq.
        fmax = fs / 2

        # Nyq freq(mel)
        melmax = 1127.01048 * np.log(fmax/ 700 + 1)

        # freq. res
        df = fs / fftn



