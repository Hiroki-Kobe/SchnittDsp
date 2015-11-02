package jp.ac.kobe.stu.watanabe;

import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.transform.FastCosineTransformer;
import org.apache.commons.math3.transform.FastFourierTransformer;

public class AcousticFrontEndImp implements AcousticFrontEnd {

	private final int    FFT_N;
	private final int    MFCC_CH;
	private final String windowType;
	
	private double[] features    = null;
	private int   [] samples     = null;
	private double[] preEmphSamp = null;
	private double[] windowSamp  = null;


	
	public AcousticFrontEndImp(int fftN, int mfccCh,  String win) {
		this.FFT_N      = fftN;
		this.MFCC_CH    = mfccCh;
		this.windowType = win;
		}

	
	@Override
	public void writeSamples(int[] samples) {
		assert (samples.length == this.FFT_N);

		if (samples.length != FFT_N) {
			throw new IllegalArgumentException();
		}
		
		this.samples = samples;
	}


	private double [] doPreEmph(int[] frames) {
		for (int i=0; i < this.FFT_N; i++) {
			preEmphSamp[i] = frames[i] - 0.97 * frames[i - 1];
		}
		return preEmphSamp;
	}

	
	private double[] windowFrames(int[] frames) {
		for (int i=0; i < FFT_N; i++) {
			windowSamp[i] = frames[i] * (0.5-0.5 * Math.cos(2 * Math.PI * i /(this.FFT_N-1)));
		}
		return windowSamp;
	}

	
	private double[] windowFrames(double[] frames) {
		for(int i=0; i < FFT_N; i++) {
			windowSamp[i] = frames[i] * (0.5-0.5 * Math.cos(2 * Math.PI * i /(this.FFT_N-1)));
		}
		return windowSamp;
	}

	
	private double [] calcFft(double [] frames){
		FastFourierTransformer fft = new FastFourierTransformer(null);
		Complex [] fftAry = new Complex[this.FFT_N];
		double  [] ampAry = new double[this.FFT_N];

		for (int i=0; i < FFT_N; i++) {
			windowSamp[i] = frames[i] * (0.5 - 0.5 * Math.cos(2 * Math.PI * i / (this.FFT_N - 1)));
		}

		
    	for(int i=0; i < FFT_N; i++){
    		fftAry    = fft.transform(windowSamp, null);
    		ampAry[i] = Math.sqrt(Math.pow(fftAry[i].getReal(), 2) + Math.pow(fftAry[i].getImaginary(), 2));
    	}
    	
    	return ampAry;
    }

	
//	INPUT: Amplitude Spectrum
	private double [] calcMfcc(double [] samples, int hz){
		double [] ampSamples = samples;
		double Nyq    =  hz / 2;                                  // Nyquist
		double melNyq =  1127.01048 * Math.log(hz / 700.0 + 1.0); // Mel-Nyquist
		int    nmax   =  FFT_N /2;                                // Maximum Number of Frequency Index
		double df     =  hz / FFT_N;                              // Frequency Resolution
		double dmel   =  melNyq/ (MFCC_CH + 1);                   // Interval of center of FilterBank in MelScale
		
		
//		Calculating Center of FilterBank in MelScale.
		double [] melCenter = new double [MFCC_CH];
		for(int i=1; i<MFCC_CH + 1; i++){
			melCenter[i-1] = i * dmel;	
		}
		
		
//		Translate Center of FilterBank in MelScale into Hz!
		double [] fcenter = new double [MFCC_CH];
		for(int i=0; i<MFCC_CH; i++){
			fcenter[i] = 700.0 * (Math.exp( melCenter[i] / 1127.01048) - 1.0);
		}
		
		
//		Translate Center of FilterBank in MelScale into Frequency Index.
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

//		Creating mel-FilterBank
		double [][] filterBank = new double [MFCC_CH][nmax];
		for(int c = 0; c<MFCC_CH; c++){
//			Calculating Slope of Left Side of FilterBank
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

		//FilteredAmp = Amplitude array that is applied mel-FilterBank from CH1 = MFCC_CH to 
		double [] [] FilterBankedAmp = new double [MFCC_CH] [ampSamples.length] ;
		for(int c = 0; c < MFCC_CH; c++){
		
			for(int i = 0; i < ampSamples.length; i++){
				FilterBankedAmp[c][i] = ampSamples[i] * filterBank[c][i];
				
			}
		}
			

//		Calculating Sum of FilteredAmp
		double [] MelFilteredSpec = new double [MFCC_CH];
		double SumTemp = 0.0;
		for(int c = 0; c < MFCC_CH; c++){

			// Calculating the sum of FilterBanked Amplitude; 				
		
			for(int i = 0; i < ampSamples.length; i++){
				
				SumTemp += FilterBankedAmp[c][i];
			}
				
				
			// Logtransfer Sum of FilteredBanked Amplitude			
		MelFilteredSpec[c] = Math.log10(SumTemp);
			
		}
		
        FastCosineTransformer fct = new FastCosineTransformer(null);
		double [] MFCC = fct.transform(MelFilteredSpec, null);
	
		return MFCC;
	}

	
	
	
	
	
					
		
	
	@Override
	public double [] readFeatures() {
		return features;
	}

	public static void main(String[] args) {

	}

}
