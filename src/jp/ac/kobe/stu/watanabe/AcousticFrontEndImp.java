package jp.ac.kobe.stu.watanabe;

import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.transform.FastCosineTransformer;
import org.apache.commons.math3.transform.FastFourierTransformer;

public class AcousticFrontEndImp implements AcousticFrontEnd {

	private final int FFT_N;
	private final int MFCC_CH;
	private final int stepLength; /* ms */
	private final String windowType;
	
	private double[] features = null;
	private int[] samples = null;
	private double[] preEmphSamples = null;
	private double[] windowSamples = null;

	
	
	public AcousticFrontEndImp(int fft_n, int mfcc_ch,  String win, int steplen) {
		this.FFT_N = fft_n;
		this.MFCC_CH = mfcc_ch;
		this.stepLength = steplen;
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
			preEmphSamples[i] = frames[i] - 0.97 * frames[i - 1];
		}
		return preEmphSamples;
	}

	
	private double[] windowFrames(int[] frames) {
		for (int i=0; i < FFT_N; i++) {
			windowSamples[i] = frames[i] * (0.5 - 0.5 * Math.cos(2 * Math.PI * i / (this.FFT_N - 1)));
		}
		return windowSamples;
	}

	private double[] windowFrames(double[] frames) {
		for(int i=0; i < FFT_N; i++) {
			windowSamples[i] = frames[i] * (0.5 - 0.5 * Math.cos(2 * Math.PI * i / (this.FFT_N - 1)));
		}
		return windowSamples;
	}

	
	private double [] calcFft(double [] frames){
		FastFourierTransformer fft = FastFourierTransformer(null);
		Complex [] fftAry = new Complex[this.FFT_N];
		double [] ampAry = new double[this.FFT_N];


		for (int i=0; i < FFT_N; i++) {
			windowSamples[i] = frames[i] * (0.5 - 0.5 * Math.cos(2 * Math.PI * i / (this.FFT_N - 1)));
		}

    	for(int i=0; i < FFT_N; i++){
    		fftAry = fft.transform(windowSamples, null);
    	}
    	
    	for(int i=0; i<this.FFT_N; i++){
    		ampAry[i] = Math.sqrt(Math.pow(fftAry[i].getReal(), 2) + Math.pow(fftAry[i].getImaginary(), 2));
    	}
    	
    	return ampAry;
    }

	
	private double [] calcMfcc(int [] samples, int hz){
		double Nyq   = hz / 2;  //Nyquist
		double melNyq = 1127.01048 * Math.log(hz / 700.0 + 1.0); // mel-Nyquist
		int nmax = FFT_N /2;
		double bin = hz / FFT_N;

		
		double [] melCenters;
	    double [] freqCenters;
	    int[] indexCenter;
	    int[] indexStart;
	    int[] indexStop;

	    
	    double dmel = melmax/ (MFCC_CH + 1);
		for(int i=1; i<MFCC_CH+1; i++){
			double value = i * dmel;
			
			melCenters[i] = value;
			freqCenters[i] = 700 * (Math.exp(value / 1127.01048) - 1.0);
		    indexCenter[i] = (int) Math.round(freqCenters[i] / bin);
		}
		
		indexStart[0] = 0; 
		for(int i=1; i<MFCC_CH; i++){
			indexStart[i] = indexCenter[i-1];
		}

		indexStop[MFCC_CH] = indexCenter[nmax];
		for(int i=0; i<MFCC_CH-1; i++){
			indexStop[i] = indexCenter[i+1];
		}
		
		double [][] filterBank = new double [MFCC_CH][nmax];
		
		for(int i =0; i<MFCC_CH;i++){
			double increment = 1.0 / (indexCenter[i] - indexCenter[i]);
			
			for(int j = indexStart[i]; j<indexCenter[i]; j++){
				filterBank[i][j] = (j - indexStart[i]) * increment;
			}
			
			double decrement = 1.0 / (indexStop[i] - indexCenter[i]);
			
			for(int k =indexCenter[i]; k<indexCenter[i]; k++){
				filterBank[i][k] = 1.0 - ((k - indexCenter[i]) * decrement);
			}
		}

		FastCosineTransformer fct = new FastCosineTransformer(null);
		double [] melAry = new double [MFCC_CH];


		for(int i; i<totalFrame; i++){
			for(int j; j<MFCC_CH; j++){
				for(k; k<MFCC_CH;k++){
					double value;
					value += value * filterBank[k];
				}
				
				double logValue = Math.log10(value);
				melAry[i][j] = logValue;
			}
		}

		double mfccAry[] = new double[MFCC_CH];
		for(int i=0; i<totalFrame; i++){
			double[] fctResult = fct.transform(melAry, FORWARD);			
			mfccAry[i] = fctResult; 
		}

		return mfccAry;
		
	}
	
	
	
	@Override
	public double [] readFeatures() {
		return features;
	}

	public static void main(String[] args) {

	}

}
