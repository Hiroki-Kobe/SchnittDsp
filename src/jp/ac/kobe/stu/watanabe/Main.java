package jp.ac.kobe.stu.watanabe;

import java.io.File;
import java.util.Arrays;

import info.pinlab.pinsound.WavClip;

public class Main {

	public static void main(String[] args) throws Exception{
		String cwd;
		String wavName = "res/test.wav";
		String wavFile;
		
		int  fftn = 512;
		int  ch   = 26;
		String windowType = "Hunning";
		int stepLength = 10; /*10m*/
		
		cwd = System.getProperty("user.dir");
		wavFile = new File(cwd, wavName).getPath();
		WavClip wav = new WavClip(wavFile) ;
		
		AcousticFrontEndFactory factory = new AcousticFrontEndFactoryImp(fftn, ch, windowType, stepLength);
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

		
		int offset = 0;
		int [] temp = new int[fftn];
		int windowNum = (intSamples.length - fftn)/ stepLength;
		
		for(int i = 0; i< windowNum; i++){
			System.arraycopy(intSamples, offset, temp, 0, fftn-1);
			assert(temp.length == fftn);
			fe.writeSamples(temp);

		    double [] fftAry = fe.readFft();

		    // Test
		    for(int j =0; j<fftAry.length; j++){
			    System.out.println(fftAry[j]);		    	
		
		    }

		    offset += stepLength;

		}

		System.out.println("Finish FFT");
		
		for(int i = 0; i< windowNum; i++){
			System.arraycopy(intSamples, offset, temp, 0, fftn-1);
			assert(temp.length == fftn);
			fe.writeSamples(temp);

		    double [] mfccAry = fe.readMfcc();

		    // Test
		    for(int j =0; j<mfccAry.length; j++){
			    System.out.println(mfccAry[j]);		    	
		
		    }

		    offset += stepLength;

		}
	}	
}
