package jp.ac.kobe.stu.watanabe;

public class Main {

	public static void main(String[] args) {

		int  n = 512;
		int ch = 26;
		int step = 10;
		double preEmph = 0.97;
		String windowType = "Hunning";
		
		
		AcousticFrontEndFactory factory = new AcousticFrontEndFactoryImp(n, ch, windowType, step, preEmph);
		AcousticFrontEnd fe = factory
				.setFftN(n)
				.setMfccCh(ch)
				.setWindowType(windowType)
				.setPreemphCof(preEmph)
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
