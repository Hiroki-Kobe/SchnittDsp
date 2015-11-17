package test;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

import info.pinlab.pinsound.WavClip;
import jp.ac.kobe.stu.watanabe.AcousticFrontEnd;
import jp.ac.kobe.stu.watanabe.AcousticFrontEndFactory;
import jp.ac.kobe.stu.watanabe.AcousticFrontEndFactoryImp;

public class PreemphedSampleTest {
	static String CurrentPath;
	private static BufferedReader br;
	static String Snd = "src/test/testResource/snd/test1.wav";
	static int [] testSamples;
	
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
	public static void setUpBeforeClass() throws Exception {
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
	public void setUp() throws Exception {
		factory = new AcousticFrontEndFactoryImp(fftn, ch, windowType, stepLength);
		fe = factory
			.setFftN(fftn)
			.setMfccCh(ch)
			.setWindowType(windowType)
			.build()
			;
	
	String testWavPath = new File(CurrentPath, Snd).getPath();
	
	WavClip wav = new WavClip(testWavPath);
	testSamples = wav.toIntArray();
	
	}

	
	
}

