package jp.ac.kobe.stu.watanabe;

import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.transform.DftNormalization;
import org.apache.commons.math3.transform.FastCosineTransformer;
import org.apache.commons.math3.transform.FastFourierTransformer;
import org.jtransforms.fft.DoubleFFT_1D;

public class AcousticFrontEndImp implements AcousticFrontEnd {

	private final int    FFT_N;
	private final int    MFCC_CH;
	private final String windowType;
	private final int    stepLength;
	
	private double [] features     = null;
	private int    [] samples      = null;
	private double [] preEmphSamp  = null;
	private double [] windowedSamp = null;
	private double [] ampAry       = null;
	public static final DftNormalization STANDARD = null;


	public AcousticFrontEndImp(int fftN, int mfccCh,  String win, int step) {
		this.FFT_N      = fftN;
		this.MFCC_CH    = mfccCh;
		this.windowType = win;
		this.stepLength = step;
		}

	
	@Override
	public void writeSamples(int[] samples) {
		assert (samples.length == this.FFT_N);

		if (samples.length != FFT_N) {
			throw new IllegalArgumentException();
		}
		
		this.samples = samples;
	}


	private double [] doPreEmph(int[] samples) {
		for (int i=0; i < this.FFT_N; i++) {
			preEmphSamp[i] = samples[i] - 0.97 * samples[i - 1];
		}

		return preEmphSamp;
	}

	
//Windowning: No preEmphasized samples
	private double[] windowFrames(int[] samples) {
		for (int i=0; i < FFT_N; i++) {
			windowedSamp[i] = samples[i] * (0.5-0.5 * Math.cos(2 * Math.PI * i /(this.FFT_N-1)));
		}
		return windowedSamp;
	}

	
//Windowning: preEmphasized samples
	private double[] windowFrames(double[] preEmphSamp) {
		for(int i=0; i < FFT_N; i++) {
			windowedSamp[i] = preEmphSamp[i] * (0.5-0.5 * Math.cos(2 * Math.PI * i /(this.FFT_N-1)));
		}
		return windowedSamp;
	}

	
	
	private double [] calcFft(double [] windowedSamp){
		FastFourierTransformer fft = new FastFourierTransformer(STANDARD);

		//		DoubleFFT_1D  fft = new DoubleFFT_1D(FFT_N);
		Complex [] fftAry = new Complex[this.FFT_N];
		double  [] ampAry = new double[this.FFT_N];
		
    	for(int i=0; i < FFT_N; i++){
    		fftAry    = fft.transform(windowedSamp, null);
    		ampAry[i] = Math.sqrt(Math.pow(fftAry[i].getReal(), 2) + Math.pow(fftAry[i].getImaginary(), 2));
    	}
    	
    	return ampAry;
    }

	
//	INPUT: Amplitude Spectrum
	private double [] doMfcc(double [] AmpSamples, int hz){
		double [] ampSamples = AmpSamples;

		MelFilterBank melFilter = new MelFilterBank(AmpSamples, FFT_N, hz, MFCC_CH);
		double [][] filterBank = melFilter.calcMelFilterBank();

		
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
		double [] preAmp = doPreEmph(samples);
		double [] windowedSamp = windowFrames(preAmp);
		double [] fftAry = calcFft(windowedSamp);

		return fftAry;
	}
}
