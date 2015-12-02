package jp.ac.kobe.stu.watanabe;

import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

import info.pinlab.pinsound.WavClip;

public class Main{

	public static void main(String[] args) throws Exception{
		final  String cwd;
		final  String wavName = "res/test.wav";
		final  String wavFileIn;
		
		final int  fftn = 512;
		final int  ch   = 26;
		final String windowType = "Hunning";
		final int stepLength    = 10;  /*ms*/
		final int windowLength = 20; /*ms*/
		final int hz;
		final int stepTotalNum;

		
		/**
		 * Read wav file from a directory
		 */
		cwd = System.getProperty("user.dir");
		wavFileIn = new File(cwd, wavName).getPath();
		WavClip wav = new WavClip(wavFileIn) ;
		File fileIn = new File(wavFileIn);
		
		// Get sampling rate
		AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(fileIn);
		hz = (int) audioInputStream.getFormat().getSampleRate();

		
		/*
		 * Set AcousticFrontEndFactory
		 */
		AcousticFrontEndFactory factory = new AcousticFrontEndFactoryImp(fftn, ch, windowType, stepLength, hz);
		AcousticFrontEnd fe = factory
				.setFftN(fftn)
				.setMfccCh(ch)
				.setWindowType(windowType)
				.setStepLength(stepLength)
				.build()
				;
		int [] samples = wav.toIntArray();
		ShiftWavSamples shiftSamp = new ShiftWavSamples(samples, stepLength, windowLength, fftn, hz);


		/*
		 * Shifting the  window to wav samples, and return FFT value and MFCC value
		 */
		int [] temp = new int[fftn];
		int shiftIx = 0;

		for(int i = 0; i<shiftSamp.totalShiftnNum; i++){			
			temp = shiftSamp.returnValue(shiftIx);
			fe.writeSamples(temp);
		    
		    double [] fftArr = fe.readFft();
		    double [] mfcc = fe.readMfcc();

		    for(int j =0; j<fftArr.length; j++){
			    System.out.println("FFT: " + fftArr[j]);
		    }
		    for(int j =0; j<mfcc.length; j++){
		    System.out.println("MFCC: " + mfcc[j]);		    	
		    }
		    
		    shiftIx ++;
		}

		System.out.println("Finish FFT + MFCC");
	}
}