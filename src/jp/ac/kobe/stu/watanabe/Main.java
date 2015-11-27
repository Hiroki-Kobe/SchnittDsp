package jp.ac.kobe.stu.watanabe;

import java.io.File;
import java.util.Arrays;

import info.pinlab.pinsound.WavClip;

public class Main{

	public static void main(String[] args) throws Exception{
		final  String cwd;
		final  String wavName = "res/test.wav";
		final  String wavFile;
		
		final int  fftn = 512;
		final int  ch   = 26;
		final String windowType = "Hunning";
		final int stepLength = 10;  /*ms*/
		final int windowLength = 20; /*ms*/
		

//		final int hz;
//		この値をwavファイルから取得するように！
		final int hz = 16000;

		cwd = System.getProperty("user.dir");
		wavFile = new File(cwd, wavName).getPath();
		WavClip wav = new WavClip(wavFile) ;
		
		AcousticFrontEndFactory factory = new AcousticFrontEndFactoryImp(fftn, ch, windowType, stepLength, hz);
		AcousticFrontEnd fe = factory
				.setFftN(fftn)
				.setMfccCh(ch)
				.setWindowType(windowType)
				.build()
				;

		//		Adjust the length of the intSample
		int [] rowSamples = wav.toIntArray();
		int rest = (rowSamples.length - fftn) % stepLength;
		int sampleLength = rowSamples.length + (stepLength - rest);
		int[] intSamples = new int [sampleLength];				
		Arrays.fill(intSamples, 0);
		System.arraycopy(rowSamples, 0, intSamples, 0, rowSamples.length);
		
		int offset  = 0;
		int [] temp = new int[fftn];
				
		for(int i = 0; i< intSamples.length; i++){
			System.arraycopy(intSamples, offset, temp, 0, fftn-1);
			assert(temp.length == fftn);
			fe.writeSamples(temp);

		    double [] fftArr = fe.readFft();
		    double [] mfcc = fe.readMfcc();

		    for(int j =0; j<fftArr.length; j++){
			    System.out.println("FFT: " + fftArr[j]);
		    }

		    for(int j =0; j<mfcc.length; j++){
		    System.out.println("MFCC: " + mfcc[j]);		    	
		    }
		    
		    offset += stepLength;
		}

		System.out.println("Finish FFT + MFCC");
	}
}