package jp.ac.kobe.stu.watanabe;

import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.transform.FastFourierTransformer;

public class AcousticFrontEndImp implements AcousticFrontEnd {

	private final int FFT_N;
	private final int MFCC_CH;
	private final int stepLength; /* ms */
	private final String windowType;
	private final double preEmphCof;

	private double[] features = null;
	private int[] samples = null;
	private double[] preEmphSamples = null;
	private double[] windowedSamples = null;

	public AcousticFrontEndImp(int fft_n, int mfcc_ch,  String win, int steplen, double p) {
		this.FFT_N = fft_n;
		this.MFCC_CH = mfcc_ch;
		this.stepLength = steplen;
		this.windowType = win;
		this.preEmphCof = p;

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

		for (int i = 0; i < this.FFT_N; i++) {
			preEmphSamples[i] = (double) frames[i] - this.preEmphCof * (double) frames[i - 1];
		}

		return preEmphSamples;

	}

	private double[] windowFrames(int[] frames) {
		for (int i=0; i < FFT_N; i++) {
			windowedSamples[i] = frames[i] * (0.5 - 0.5 * Math.cos(2 * Math.PI * i / (this.FFT_N - 1)));

		}

		return windowedSamples;
	}

	private double[] windowFrames(double[] frames) {
		for(int i=0; i < FFT_N; i++) {
			windowedSamples[i] = frames[i] * (0.5 - 0.5 * Math.cos(2 * Math.PI * i / (this.FFT_N - 1)));

		}

		return windowedSamples;
	}

	private double [] calcFft(double [] samples){
    	FastFourierTransformer fft = FastFourierTransformer(null);
    	Complex [] fftAry = new Complex[this.FFT_N];
    	double [] ampAry = new double[this.FFT_N];
    	
    	for(int i=0; i < FFT_N; i++){
    		fftAry = fft.transform(samples, null);
    		
    	}
    	
    	for(int i=0; i<this.FFT_N; i++){
    		ampAry[i] = Math.sqrt(Math.pow(fftAry[i].getReal(), 2) + Math.pow(fftAry[i].getImaginary(), 2));
    		
    	}
    	
    	return ampAry;
    }

	@Override
	public double [] readFeatures() {
		return features;
	}

	public static void main(String[] args) {

	}

}
