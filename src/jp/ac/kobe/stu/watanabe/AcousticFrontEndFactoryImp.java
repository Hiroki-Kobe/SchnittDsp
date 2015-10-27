package jp.ac.kobe.stu.watanabe;

public class AcousticFrontEndFactoryImp implements AcousticFrontEndFactory{

	private int FFT_N = 256;
	private int MFCC_CH = 26;
	
	public AcousticFrontEndFactoryImp(){
		return null;
	}

	@Override
	public AcousticFrontEndFactory setFftN(int n){
		this.FFT_N = n;
		return this;
	}
	
	@Override
	public AcousticFrontEnd build(){
		return new AcousticFrontEndImp(this.FFT_N, this.MFCC_CH);
	}
	
	@Override
	public AcousticFrontEndFactory setMfccCh(int n){
		this.MFCC_CH = n;
		return this;
	}
}

	
	
	