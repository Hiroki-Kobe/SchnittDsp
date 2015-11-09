package jp.ac.kobe.stu.watanabe;

import info.pinlab.pinsound.WavClip;

public class Main {

	public static void main(String[] args) throws Exception{
		int  fftn = 512;
		int ch    = 26;
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
		
		int [] samples = wav.toIntArray();
		int offset = 0;
		int [] featureSamples = new int [fftn];
		
		for(int val: samples){
			
			System.arraycopy(samples, offset, featureSamples, 0, fftn);
			System.out.println(featureSamples.length);

			fe.writeSamples(samples);

			System.out.println(offset);
			
		    double [] features = fe.readFeatures();
			
		    System.out.println(features);
		    offset += stepLength;
		}
	}	
}
