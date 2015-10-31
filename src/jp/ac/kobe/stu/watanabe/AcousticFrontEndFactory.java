package jp.ac.kobe.stu.watanabe;

public interface AcousticFrontEndFactory {

	public AcousticFrontEndFactory setFftN(int n);
	public AcousticFrontEndFactory setMfccCh(int n);
	public AcousticFrontEndFactory setStepLength(int n);
	public AcousticFrontEndFactory setWindowType(String w);
	public AcousticFrontEnd build();
	
}
