package jp.ac.kobe.stu.watanabe;

import java.lang.reflect.Array;
import java.util.Arrays;

import info.pinlab.pinsound.WavClip;

public class Main {

	public static void main(String[] args) throws Exception{
		int  fftn = 512;
		int  ch   = 26;
		String windowType = "Hunning";
		int stepLength = 10; /*10m*/
		
		
		AcousticFrontEndFactory factory = new AcousticFrontEndFactoryImp(fftn, ch, windowType, stepLength);
		AcousticFrontEnd fe = factory
				.setFftN(fftn)
				.setMfccCh(ch)
				.setWindowType(windowType)
				.build()
				;

		WavClip wav = new WavClip("/home/snoopy/workspace/SchnittDsp/res/test.wav") ;

//		Pudding 0 to Set Length of Samples to Multiple of FFT_N
		int [] rowSamples = wav.toIntArray();

		System.out.println(Arrays.toString(rowSamples));
		System.out.println();
		int pudding = fftn - (rowSamples.length % fftn);
		int [] samples = new int [rowSamples.length + pudding];
		Arrays.fill(samples, 0);

		assert(samples.length % fftn==0);
		
		
		int offset = 0;
		int [] featureSamples = new int [fftn];
		

		
		for(int val: samples){

			System.arraycopy(samples, offset, featureSamples, 0, fftn);
			assert(featureSamples.length == fftn);

			fe.writeSamples(samples);

		    double [] features = fe.readFeatures();

		    for(int i =0; i<features.length; i++){
			    System.out.println(features[i]);		    	
		    }

		    offset += stepLength;
		}
	}	
}

