package jp.ac.kobe.stu.watanabe;

public class MelFilterBank {

	public double[] ampSamples;
	
	private final int HZ;
	private final int FFT_N;
	private final int MFCC_CH;

	private double nyq ;            // Nyquist
	private double melNyq;          // Mel-Nyquist
	private int    nmax;            // Maximum Number of Frequency Index
	private double df;              // Frequency Resolution
	private double dmel;            // Interval of center of FilterBank in MelScale
	
	public MelFilterBank(int fft_n, int hz, int mfcc_ch) {
		this.HZ         = hz;
		this.MFCC_CH    = mfcc_ch;
		this.FFT_N      = fft_n;		
	}
	
	
	public double [][] calcMelFilterBank(double [] samples){
		this.ampSamples = samples;

		this.nyq    =  HZ / 2;                                  // Nyquist
		this.melNyq =  1127.01048 * Math.log(HZ / 700.0 + 1.0); // Mel-Nyquist
		this.nmax   =  FFT_N /2;                                // Maximum Number of Frequency Index
		this.df     =  HZ / FFT_N;                              // Frequency Resolution
		this.dmel   =  melNyq/ (MFCC_CH + 1);                   // Interval of center of FilterBank in MelScale


		
//	Calculating Center of FilterBank in MelScale.
	double [] melCenter = new double [MFCC_CH];
	for(int i=0; i<MFCC_CH; i++){
		melCenter[i] = (i+1) * dmel;	
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
	indexStop[indexStop.length - 1]  = nmax;
	
	for(int i=1; i<MFCC_CH; i++){
		indexStop[i-1] = indexCenter[i];
	}

	
//	Creating mel-FilterBank
	double [][] filterBank = new double [MFCC_CH][nmax];

	for(int c = 0; c<MFCC_CH; c++){

		//		Calculating Slope of Left Side of FilterBank
		double increment = 1.0 / (indexCenter[c] - indexStart[c]);
		double incrementRange = (indexCenter[c] + 1) - indexStart[c];
		
		double incrementPoint = indexStart[c];

		for(int i=0; i<incrementRange; i++){
	
			filterBank[c][i] = (incrementPoint - indexStart[c]) * increment;
			incrementPoint   = incrementPoint + df;

		}
		
		double decrement = 1.0 / (indexStop[c] - indexCenter[c]); 
		double decrementRange =  (indexStop[c] + 1 ) - indexCenter[c];

		double decrementPoint = indexCenter[c];
				
		for(int i=0; i<decrementRange; i++){
	
			filterBank[c][i] = 1.0 - ((i - indexCenter[c]) * decrement);
			decrementPoint = decrementPoint + df;
		}
	}

	return filterBank;
	}
}