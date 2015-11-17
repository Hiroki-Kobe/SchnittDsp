package jp.ac.kobe.stu.watanabe;

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

		int [] rowSamples = wav.toIntArray();
		
		int offset = 0;
		int [] temp = new int[fftn];
		
		fe.writeSamples(rowSamples);


		for(int i = 0; i<rowSamples.length;i++){
			System.arraycopy(rowSamples, offset, temp, 0, fftn-1);
			assert(temp.length == fftn);
			
			fe.writeSamples(temp);

		    double [] features = fe.readFeatures();

		    // Test
		    for(int j =0; j<features.length; j++){
			    System.out.println(features[j]);		    	
		    }

		    offset += stepLength;
		    temp = null;

		}
	}	
}
