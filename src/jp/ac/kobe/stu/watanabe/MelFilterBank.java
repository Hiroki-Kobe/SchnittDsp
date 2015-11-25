package jp.ac.kobe.stu.watanabe;


public class MelFilterBank {

	public double[] ampSamples;
	private final int FS;
	private final int FFT_N;
	private final int MFCC_CH;

	private double fmax;             // Nyquist
	private double melMax;          // Mel-Nyquist
	private int    nmax;            // Maximum Number of Frequency Index
	private double df;              // Frequency Resolution
	private double dmel;            // Interval of center of FilterBank in MelScale
	
	public MelFilterBank(int fft_n, int fs, int mfcc_ch) {
		this.FS         = fs;
		this.MFCC_CH    = mfcc_ch;
		this.FFT_N      = fft_n;		
	}
	
	public double [][] calcMelFilterBank(double [] samples){
		this.ampSamples = samples;
		this.fmax   =  FS / 2;                                  // Nyquist
		this.melMax =  1127.01048 * Math.log(fmax / 700.0 + 1.0); // Mel-Nyquist
		this.nmax   =  FFT_N /2;                                // Maximum Number of Frequency Index
		this.df     =  FS / FFT_N;                              // Frequency Resolution
		this.dmel   =  melMax/ (MFCC_CH + 1);                   // Interval of center of FilterBank in MelScale

		//	Calculating Center of FilterBank in MelScale.
		double [] melCenter = new double [MFCC_CH];
		for(int i=1; i<MFCC_CH+1; i++){
			melCenter[i-1] = i * dmel;
		}
	
		//	Translate Center of FilterBank in MelScale into Hz!
		double [] fcenter = new double [MFCC_CH];
		for(int i=0; i<MFCC_CH; i++){
			fcenter[i] = 700.0 * (Math.exp( melCenter[i] / 1127.01048) - 1.0);
		}
	
		//	Translate Center of FilterBank in MelScale into Frequency INDEX (BIN).
		double [] indexCenter = new double [MFCC_CH];
		for(int i=0; i<MFCC_CH; i++){
			indexCenter[i] = Math.round(fcenter[i] / df);
		}
	
		double [] indexStart = new double [MFCC_CH];
		indexStart[0] = 0;
		for(int i=0; i<MFCC_CH-1; i++){
			indexStart[i+1] = indexCenter[i];
		}
	
		double [] indexStop = new double [MFCC_CH];
		for(int i=0; i<MFCC_CH-1; i++){
			indexStop[i] = indexCenter[i+1];
		}
		indexStop[indexStop.length - 1]  = nmax;	
		
		//	Creating mel-FilterBank
		double [][] filterBank = new double [MFCC_CH][nmax];
		for(int c = 0; c<MFCC_CH; c++){			
			//	Calculating Slope of Left Side of FilterBank
			double increment = 1.0 / (indexCenter[c] - indexStart[c]);			
			double incrementRange = indexCenter[c] - indexStart[c];
			int startIn = (int) indexStart[c];
			
			for(int i=0; i<incrementRange; i++){
				filterBank[c][startIn] = i * increment;
				startIn++;
			}
		
			double decrement = 1.0 / (indexCenter[c] - indexStop[c]); 
			double decrementRange =  (indexStop[c])- indexCenter[c];
			int startDec = (int) indexCenter[c];
			for(int i=0; i<decrementRange; i++){
				filterBank[c][startDec] = i * decrement + 1;
				startDec++;
			}
		}

		return filterBank;
	}
}
