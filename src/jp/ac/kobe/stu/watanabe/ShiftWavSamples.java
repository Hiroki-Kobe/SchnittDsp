package jp.ac.kobe.stu.watanabe;

import java.util.Arrays;

public class ShiftWavSamples {
	final int [] samples;
	final int shiftLen;   /*ms*/
	final int windowLen;  /*ms*/
	final int fftn;
	final int hz;
	final int windowSampNum;
	final int shiftSampNum;
	final int totalShiftnNum;
	final int [] paddedSamples;
	
	/**
	 * 
	 * @param samples     -> integer of wavfile
	 * @param shiftLen    -> ms
	 * @param windowLen   -> ms
	 * @param fftn       
	 * @param hz  
	 */
	
	public ShiftWavSamples(int[] samples, int shiftLen, int windowLen, int fftn, int hz){
		this.samples = samples;
		this.shiftLen = shiftLen;
		this.fftn = fftn;
		this.hz = hz;
		this.windowLen = windowLen;
		this.windowSampNum = (int) (hz * (windowLen/ (1.0 * 1000)));
		this.shiftSampNum =  (int) (hz * (shiftLen / (1.0 * 1000)));

//		System.err.println("winbdowSampnum: " + windowSampNum);
//		System.err.println("shiftSampUm" + shiftSampNum);

		
		/*
		 *  padding 0 into InteSamples for matching window length.
		 */
		int restOfSamples = (samples.length - windowLen) % shiftLen;
		int paddedSampleLen = samples.length + (shiftLen - restOfSamples);
		this.paddedSamples = new int [paddedSampleLen];				
		Arrays.fill(this.paddedSamples, 0);
		System.arraycopy(samples, 0, paddedSamples, 0, samples.length);
		
		/*
		 * Get the Number of Times of shifting the window.
		 */
		totalShiftnNum = (paddedSampleLen - windowSampNum) / shiftSampNum ;
	}
	
	
	/*
	 * return shifted samples:
	 * 
	 */
	public int [] returnValue(int shiftIx){
		int offset  = shiftSampNum * shiftIx;
		System.err.println("setted offset: " + offset);
		int [] cuttedSamples = new int[fftn];
		System.arraycopy(this.samples, offset, cuttedSamples, 0, this.windowSampNum);
		    
		return cuttedSamples;

	}
	
}
