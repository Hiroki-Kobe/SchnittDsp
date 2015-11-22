package jp.ac.kobe.stu.watanabe;

public class AcousticFrontEndFactoryImp implements AcousticFrontEndFactory{

	private int FFT_N;
	private int MFCC_CH;
	private int stepLength; /*ms*/
	private String windowType;


	public AcousticFrontEndFactoryImp(int fft_n, int mfcc_ch, String win, int step){
		this.FFT_N      = fft_n;
		this.MFCC_CH    = mfcc_ch;
		this.windowType = win;
        this.stepLength = step;
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
	public AcousticFrontEndFactory setStepLength(int s){
		this.stepLength = s;
		return this;
	}

	
	@Override
	public AcousticFrontEnd build(){
		return new AcousticFrontEndImp(this.FFT_N, this.MFCC_CH, this.windowType, this.stepLength);
	}
}
