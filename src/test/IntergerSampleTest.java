package test;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class IntergerSampleTest {
	static String path     = "/home/pon_chan/workspace/SchnittDsp/src/test/testResource/";
	static String intFile = "int_samples.txt";
    static boolean line;
	static ArrayList<Integer> IntAry;
    
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		try{
			String fileName = path + intFile;
			BufferedReader br = 
					new BufferedReader(new FileReader(fileName));
			
			IntAry = new ArrayList<Integer>(); 
			
			String value;
			
			while(line){

				value = br.readLine();
				int intValue = Integer.parseInt(value);
				IntAry.add(intValue);
				
				if(null==value){
					line = false;
				}
			}
			
		}catch(FileNotFoundException e){
			System.out.println("IOE ERROR");
		}
		
	}

	
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	
	@Test
	public void test() throws IOException {
		for (int i =0; i<IntAry.size();i++){
			int value = IntAry.get(i);
			System.out.println(value);
		}
		
		System.out.println("-------------Finish Here");
		
		
		
	}

}
