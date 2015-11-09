package jp.ac.kobe.stu.watanabe;

public class MelFilterBank {

	public double[] ampSamples = null;
	
	int hz;
	int fftn;
	double nyq ;            // Nyquist
	double melNyq;          // Mel-Nyquist
	int    nmax;            // Maximum Number of Frequency Index
	double df;              // Frequency Resolution
	double dmel;            // Interval of center of FilterBank in MelScale
	int MFCC_CH;
	
	public MelFilterBank(double [] samples, int FFT_N, int hz, int MFCC_CH) {
		this.ampSamples = samples;
		this.hz = hz;
		this.MFCC_CH = MFCC_CH;
		
		this.nyq    =  hz / 2;                                  // Nyquist
		this.melNyq =  1127.01048 * Math.log(hz / 700.0 + 1.0); // Mel-Nyquist
		this.nmax   =  FFT_N /2;                                // Maximum Number of Frequency Index
		this.df     =  hz / FFT_N;                              // Frequency Resolution
		this.dmel   =  this.melNyq/ (MFCC_CH + 1);                   // Interval of center of FilterBank in MelScale

	}
	
	
	public double [][] calcMelFilterBank(){
		
//	Calculating Center of FilterBank in MelScale.
	double [] melCenter = new double [MFCC_CH];
	for(int i=1; i<MFCC_CH + 1; i++){
		melCenter[i-1] = i * dmel;	
	}
	
	
	
//	Translate Center of FilterBank in MelScale into Hz!
	double [] fcenter = new double [MFCC_CH];
	for(int i=0; i<MFCC_CH; i++){
		fcenter[i] = 700.0 * (Math.exp( melCenter[i] / 1127.01048) - 1.0);
	}
	
	
	
//	Translate Center of FilterBank in MelScale into Frequency Index.
	double [] indexCenter = new double [MFCC_CH];
	for(int i=0; i<MFCC_CH; i++){
		indexCenter[i] = Math.round(fcenter[i] / df);
	}
	
	
	double [] indexStart = new double [MFCC_CH];
	indexStart[0] = 0;
	for(int i=1; i<MFCC_CH-1; i++){
		indexStart[i] = indexCenter[i-1];
	}
	
	
	double [] indexStop = new double [MFCC_CH];
	indexStop[MFCC_CH]  = nmax;
	for(int i=1; i<MFCC_CH; i++){
		indexStop[i-1] = indexCenter[i];
	}

	
//	Creating mel-FilterBank
	double [][] filterBank = new double [MFCC_CH][nmax];

	for(int c = 0; c<MFCC_CH; c++){

		//		Calculating Slope of Left Side of FilterBank
		double increment = 1.0 / (indexCenter[c] - indexStart[c]);
		double incrementRange = indexStart[c] - indexCenter[c];
		
		for(int i=0; i<incrementRange; i++){
			filterBank[c][i] = (i - indexStart[c]) * increment;
		}
		
		double decrement = 1.0 / (indexStop[c] - indexCenter[c]); 
		double decrementRange = indexStop[c] - indexCenter[c];
		
		for(int i=0; i<decrementRange; i++){
			filterBank[c][i] = 1.0 - ((i - indexCenter[i]) * decrement);
		}
	}

	return filterBank;
	}
}