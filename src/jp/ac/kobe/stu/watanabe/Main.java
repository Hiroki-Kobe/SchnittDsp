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
		
		final int  fftn         = 512;
		final int  ch           = 26;
		final String windowType = "Hunning";
		final int stepLength    = 10;  /*ms*/
		final int windowLength  = 20; /*ms*/
		final boolean running = true;
		
		final int hz;

		
		/**
		 * Read wav file from a directory
		 */
		cwd = System.getProperty("user.dir");
		wavFileIn = new File(cwd, wavName).getPath();
		WavClip wav = new WavClip(wavFileIn) ;
		File fileIn = new File(wavFileIn);
		
		/** 
		 * Get sampling rate
		 **/
		AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(fileIn);
		hz = (int) audioInputStream.getFormat().getSampleRate();

		
		/*
		 * Set AcousticFrontEndFactory
		 */
		AcousticFrontEndFactory factory = new AcousticFrontEndFactoryImp(fftn, ch, windowType, windowLength, stepLength, hz);
		AcousticFrontEnd fe = factory
				.setFftN(fftn)
				.setMfccCh(ch)
				.setWindowType(windowType)
				.setWindowLength(windowLength)
				.setStepLength(stepLength)
				.build()
				;
		
		
		int [] samples = wav.toIntArray();
        fe.setSamples(samples);
		
        while(running){
        	
        	
        }
		for(int j =0; j<fftArr.length; j++){
			    System.out.println("FFT: " + fftArr[j]);
		    }
		    for(int j =0; j<mfcc.length; j++){
		    System.out.println("MFCC: " + mfcc[j]);		    	
		    }
		    
	}
}