package test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

import info.pinlab.pinsound.WavClip;
import jp.ac.kobe.stu.watanabe.AcousticFrontEnd;
import jp.ac.kobe.stu.watanabe.AcousticFrontEndFactory;
import jp.ac.kobe.stu.watanabe.AcousticFrontEndFactoryImp;


public class IntergerSampleTest {
	static String CurrentPath;
	private static BufferedReader br;
	static String Snd = "src/test/testResource/snd/test1.wav";

	
	static String intFile = "src/test/testResource/int_samples.txt";
	static ArrayList<Integer> IntAry;
	static int [] intSamples;


	static String preemphedFile = "src/test/testResource/preemph_samples.txt";	
	static ArrayList<Double> PreemphedAry;
	static double [] preemphedTestSamples;

	
	
	static AcousticFrontEndFactory factory;
	static AcousticFrontEnd fe;
	static int fftn = 512;
	static int ch   = 26;
	static String windowType = "Hunning";
	static int stepLength = 10;
	
	
	
	@BeforeClass
	public static void readIntfile() throws Exception {
		try{
			CurrentPath = System.getProperty("user.dir");
					
			String fileName = new File(CurrentPath, intFile).getPath();
			br = new BufferedReader(new FileReader(fileName));
			
			IntAry = new ArrayList<Integer>(); 
			String value = br.readLine();
			while(value != null){
				int intValue = Integer.parseInt(value);
				IntAry.add(intValue);
				
				value = br.readLine();								
			}
			
			br.close();
			
		}catch(FileNotFoundException e){
			System.out.println("IOE ERROR");
		}
	}

	
	@BeforeClass
	public static void readPreemphedFile() throws Exception {
		try{			
			CurrentPath = System.getProperty("user.dir");
			String fileName = new File(CurrentPath, preemphedFile).getPath(); 
			br = new BufferedReader(new FileReader(fileName));
	
			PreemphedAry = new ArrayList<Double>();
			String value = br.readLine();
	
			while (value != null){
				double doubleValue = Double.parseDouble(value);
				PreemphedAry.add(doubleValue);
		
				value = br.readLine();
			}
		
			br.close();
		}catch(FileNotFoundException e){
			System.out.println("IOE ERROR");
		
		}		
	}
	
	
	@Before
	public void frontEndSetUp() throws Exception {
		factory = new AcousticFrontEndFactoryImp(fftn, ch, windowType, stepLength);
		fe = factory
				.setFftN(fftn)
				.setMfccCh(ch)
				.setWindowType(windowType)
				.build()
				;
		
		String testWavPath = new File(CurrentPath, Snd).getPath();
		
		WavClip wav = new WavClip(testWavPath);
		intSamples = wav.toIntArray();
		
		}


	@Test
	public void testIntergerSamples() throws IOException {
		// Check size;
		assertEquals(IntAry.size(), intSamples.length);

		System.out.println(intSamples[16]);
		System.out.println(IntAry.get(16));

		// Check each interger
		for(int i = 0; i<intSamples.length; i++){
		    int A = intSamples[i];
			int B = IntAry.get(i);
			System.out.println();
			assertEquals("different Integer", A, B);
		}
	}


//    
//	@Test
//	public void testPreemphedSamples() throws IOException{
//		int [] intSamplesTemp = new int [fftn]; 
//		System.arraycopy(intSamples, 0, intSamplesTemp, 0, fftn);
//			
//		fe.writeSamples(intSamplesTemp);
//			
//		preemphedTestSamples = fe.readFeatures();
//		assertEquals("Not Set FFTN", fftn, preemphedTestSamples.length);
//
//		for(int i =0; i<fftn; i++){
//			double exp = PreemphedAry.get(i);
//			double obs = preemphedTestSamples[i];
//				
//			assertEquals("Different number", exp, obs, 0.00001);
//			// Rounding Error = 0.00001
//		}
//	}
	
	
	@Test
	public void testWindonedSamples() throws IOException{
		int [] intSamplesTemp =  new int [fftn];
		System.arraycopy(intSamples, 0, intSamplesTemp, 0, fftn);
			
		fe.writeSamples(intSamplesTemp);
		double [] windowedTestSamples = fe.readFeatures();
		
		for(int i = 0; i<fftn; i++){
			double expected = 
		}
	}
	
}
