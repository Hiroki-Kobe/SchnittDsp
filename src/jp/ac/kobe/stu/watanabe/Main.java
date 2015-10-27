package jp.ac.kobe.stu.watanabe;

public class Main {

	public static void main(String[] args) {

		int  n = 512;
		int ch = 26;
		
		AcousticFrontEndFactory factory = new AcousticFrontEndFactoryImp();
		AcousticFrontEnd fe = factory
				.setFttN(n)
				.setMfccCh(ch)
				.build()
				;
		
		
		int [] samples = new int [n];
		for(int i = 0; i< n;i++){
			samples[i] = i;
		}
		
		fe.writeSamples(samples);
		
		double [] features = fe.readFeatures();
		
		for(double d : features){
			System.out.println(d);
		}
		
	}

	
}
