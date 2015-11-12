package jp.ac.kobe.stu.watanabe;

import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.transform.DctNormalization;
import org.apache.commons.math3.transform.DftNormalization;
import org.apache.commons.math3.transform.FastCosineTransformer;
import org.apache.commons.math3.transform.FastFourierTransformer;
import org.apache.commons.math3.transform.TransformType;

public class AcousticFrontEndImp implements AcousticFrontEnd {

	private final int    FFT_N;
	private final int    MFCC_CH;
	private final String windowType;
	private final int    stepLength;
	
	private double [] features     = null;
	private int    [] samples      = null;
	private double [] preEmphSamp  = null;
	private double [] windowedSamp = null;
	private double [] amplitudeSamples = null;
	
	public static final DftNormalization STANDARD = DftNormalization.STANDARD;
	public static final DctNormalization STANDARD_DCT_I = DctNormalization.STANDARD_DCT_I;
	public static final TransformType FORWARD = TransformType.FORWARD;
	
	
	public AcousticFrontEndImp(int fftN, int mfccCh,  String win, int step) {
		this.FFT_N      = fftN;
		this.MFCC_CH    = mfccCh;
		this.windowType = win;
		this.stepLength = step;
		}

	
	@Override
	public void writeSamples(int[] samples) {
		assert (samples.length == this.FFT_N);

//		if (samples.length != FFT_N) {
//			throw new IllegalArgumentException();
//		}
		
		this.samples = samples;
	}


	private double [] doPreEmph(int[] samples) {
		double [] coef = {1.0, 0.97};
		FirFilter fir = new FirFilter(coef);
		preEmphSamp = fir.doFirFilter(samples);
		
		return preEmphSamp;
	}

		
// Windowning: INPUT: preEmphasized samples
	private double[] windowFrames(double[] preEmphSamples) {
		
		preEmphSamp  = preEmphSamples;
		windowedSamp = new double [this.FFT_N];

		// DEBUG
		assert(samples.length == this.FFT_N);

		if(windowType == "Hunning"){		
		    for(int i=0; i < FFT_N; i++) {
			    windowedSamp[i] = this.preEmphSamp[i] * (0.5 - 0.5 * Math.cos(2 * Math.PI * i / (this.FFT_N - 1)));
			    }
		}
		
		return windowedSamp;
		
	}



	private double [] calcFft(double [] windowedSamp){
		FastFourierTransformer fft = new FastFourierTransformer(STANDARD);

		Complex [] fftAry = new Complex[this.FFT_N];
		amplitudeSamples = new double[this.FFT_N];
		
    	for(int i=0; i < FFT_N; i++){
    		fftAry = fft.transform(windowedSamp, FORWARD);
    	
    		// Calculating Amplitude
    		amplitudeSamples[i] = Math.sqrt(Math.pow(fftAry[i].getReal(), 2) + Math.pow(fftAry[i].getImaginary(), 2));
    	}
    	
    	return amplitudeSamples;
    }


    //	INPUT: Amplitude Spectrum
	private double [] doMfcc(double [] AmpSamples, int hz){
		double [] ampSamples = AmpSamples;

		MelFilterBank melFilter = new MelFilterBank(FFT_N, hz, MFCC_CH);
		double [][] filterBank = melFilter.calcMelFilterBank(AmpSamples);

		
//		Until Here: checking (including MelFilterBank)!! I have to do debug the rest of parts
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
		
        FastCosineTransformer fct = new FastCosineTransformer(STANDARD_DCT_I);
		double [] MFCC = fct.transform(MelFilteredSpec, null);
	
		return MFCC;
	}
	
	
	@Override
	public double[] readFeatures() {
		double [] preAmp = doPreEmph(samples);
		double [] windowedSamp = windowFrames(preAmp);
		
		features = calcFft(windowedSamp);

		return features;
		}
}
