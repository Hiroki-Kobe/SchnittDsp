package jp.ac.kobe.stu.watanabe;

import java.util.Arrays;

import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;

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

		WavClip wav = new WavClip("/home/pon_chan/workspace/SchnittDsp/res/test.wav") ;

//		Pudding 0 to Set Length of Samples to Multiple of FFT_N
		int [] rowSamples = wav.toIntArray();
		int pudding = fftn - (rowSamples.length % fftn);

		System.out.println("pudding: " + pudding);
		int [] samples = new int [rowSamples.length + pudding];
		Arrays.fill(samples, 0);

		assert(samples.length % fftn==0);
		
		System.out.println(samples);
		System.out.println("pudded samples: " +  samples.length);
		System.out.println("RowSamples: " + rowSamples.length);
		
		
		int offset = 0;
		int [] featureSamples = new int [fftn];
		

		
		for(int val: samples){

			System.arraycopy(samples, offset, featureSamples, 0, fftn);
			assert(featureSamples.length == fftn);

			fe.writeSamples(samples);

		    double [] features = fe.readFeatures();
			
		    System.out.println(features);
		    offset += stepLength;
		}
	}	
}

