package jp.ac.kobe.stu.watanabe;

import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.transform.DftNormalization;
import org.apache.commons.math3.transform.FastFourierTransformer;
import org.apache.commons.math3.transform.TransformType;

public class AcousticFrontEndImp implements AcousticFrontEnd {
	private final int HZ          = 16000;
	private final int MFCC_CEPS_N = 12;
	
	private final int  FFT_N;
	private final int  MFCC_CH;
    private final int  stepLength;
	private final String windowType;
	
	private int    []  samples         = null;
	private double [] preEmphSamp      = null;
	private double [] windowedSamp     = null;
	private double [] amplitudeSamples = null;
	
	public static final DftNormalization STANDARD = DftNormalization.STANDARD;
	public static final TransformType FORWARD     = TransformType.FORWARD;
	
	
	public AcousticFrontEndImp(int fftN, int mfccCh, String win, int step){
		this.FFT_N      = fftN;
		this.MFCC_CH    = mfccCh;
		this.windowType = win;
		this.stepLength = step;
		}

	
	@Override
	public void writeSamples(int[] samples) {
		assert (samples.length == this.FFT_N);		
		this.samples = samples;
	}


	private double [] doPreEmph(int[] samples) {
		double [] coef = {1.0, -0.97};
		FirFilter fir   = new FirFilter(coef);
		preEmphSamp     = fir.doFirFilter(samples);
		
		return preEmphSamp;
	}

		
// Windowing: INPUT: preEmphasized samples
	private double[] windowFrames(double[] preEmphSamples) {
		preEmphSamp  = preEmphSamples;
		windowedSamp = new double [this.FFT_N];

		// DEBUG
		assert(samples.length == this.FFT_N);

		if(windowType == "Hunning"){		
		    for(int n=0; n<FFT_N; n++) {
			    windowedSamp[n] =  (0.5 - (0.5 * Math.cos(2 * Math.PI * n/ (FFT_N -1 )))) * preEmphSamp[n];
			    }
		}
		
		return windowedSamp;
	}


	private double [] doFft(double [] windowedSamp){
		FastFourierTransformer fft = new FastFourierTransformer(STANDARD);
		Complex [] fftAry = new Complex[this.FFT_N/2];
		amplitudeSamples = new double[this.FFT_N/2];
		
		fftAry = fft.transform(windowedSamp, FORWARD);
    	for(int i=0; i < FFT_N /2; i++){
    		// Calculating Amplitude
    		amplitudeSamples[i] = Math.sqrt(Math.pow(fftAry[i].getReal(), 2) + Math.pow(fftAry[i].getImaginary(), 2));
    	}
    	
    	return amplitudeSamples;
    }


    //	INPUT: Amplitude Spectrum
	private double [] doMfcc(double [] AmpSamples){
		double [] ampSamples = AmpSamples;

		MelFilterBank melFilter = new MelFilterBank(FFT_N, HZ, MFCC_CH);
		double [][] filterBank = melFilter.calcMelFilterBank(ampSamples);	
		
		// FilteredAmp = Amplitude array that is applied mel-FilterBank from CH1 = MFCC_CH to 
		double [] [] FilterBankedAmp = new double [MFCC_CH][FFT_N/2] ;
		for(int c = 0; c < MFCC_CH; c++){
			for(int i = 0; i < FFT_N/ 2; i++){
				FilterBankedAmp[c][i] = ampSamples[i] * filterBank[c][i];
			}
		}

//		Calculating Sum of FilteredAmp
		double [] SumOfFilteredAmp = new double [MFCC_CH];
		for(int c = 0; c < MFCC_CH; c++){
			// Calculating the sum of FilterBanked Amplitude; 			
			double SumValue = 0;
			for(int i = 0; i < FFT_N/2; i++){
				SumValue += FilterBankedAmp[c][i];
			}	
			SumOfFilteredAmp[c] = SumValue;	
		}

		for(int i = 0; i<MFCC_CH; i++){
			System.out.println("check: " + SumOfFilteredAmp[i]);
		}
		
		
		double [] melSpec = new double [MFCC_CH];
		for(int c = 0; c<MFCC_CH; c++){
			// Log-transfer Sum of FilteredBanked Amplitude			
			melSpec[c] = Math.log(SumOfFilteredAmp[c]);
		}
		        
		Dct dct = new Dct(MFCC_CH);
		double [] cepsAry = dct.transform(melSpec);
		double [] mfcc = new double [MFCC_CEPS_N];  	
		System.arraycopy(cepsAry, 0, mfcc, 0, MFCC_CEPS_N);

		return mfcc;
	}
	
	
	@Override
	public double[] readFft() {
		double [] preAmp = doPreEmph(samples);
		double [] windowedSamp = windowFrames(preAmp);
		double [] fftAry = doFft(windowedSamp);

		return fftAry;
		}
	
	@Override
	public double [] readMfcc(){
		double [] preAmp = doPreEmph(samples);
		double [] windowedSamp = windowFrames(preAmp);
		double [] fftAry = doFft(windowedSamp);
		double [] mfcc = doMfcc(fftAry);
				
		return mfcc;
	}	
}
