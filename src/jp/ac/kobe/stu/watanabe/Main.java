package jp.ac.kobe.stu.watanabe;

public class Main {

	public static void main(String[] args) {
		int  fftn = 512;
		int ch = 26;
		String windowType = "Hunning";
		
		
		AcousticFrontEndFactory factory = new AcousticFrontEndFactoryImp(fftn, ch, windowType);
		AcousticFrontEnd fe = factory
				.setFftN(fftn)
				.setMfccCh(ch)
				.setWindowType(windowType)
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
