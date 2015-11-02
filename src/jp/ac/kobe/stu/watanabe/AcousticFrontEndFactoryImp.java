package jp.ac.kobe.stu.watanabe;

public class AcousticFrontEndFactoryImp implements AcousticFrontEndFactory{

	private int FFT_N   = 256;
	private int MFCC_CH = 26;
	private int stepLength = 10; /*ms*/
	private String windowType = "Hunnig";


	public AcousticFrontEndFactoryImp(int fft_n, int mfcc_ch, String w, int s){
		this.FFT_N = fft_n;
		this.MFCC_CH = mfcc_ch;
		this.windowType = w;
        this.stepLength = s;
		
	}

	
	@Override
	public AcousticFrontEndFactory setFftN(int n){
		this.FFT_N = n;
		return this;
	}

	@Override
	public AcousticFrontEndFactory setMfccCh(int n){
		this.MFCC_CH = n;
		return this;
	}

	@Override
	public AcousticFrontEndFactory setWindowType(String w){
		this.windowType  = w;
		return this;

	}

	
	@Override
	public AcousticFrontEnd build(){
		return (AcousticFrontEnd) new AcousticFrontEndFactoryImp(this.FFT_N, this.MFCC_CH, this.windowType, this.stepLength);
	
	}
	
}
