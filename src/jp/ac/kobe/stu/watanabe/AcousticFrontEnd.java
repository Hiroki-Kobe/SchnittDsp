package jp.ac.kobe.stu.watanabe;
import java.util.Arrays;

import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.transform.FastFourierTransformer;

import info.pinlab.pinsound.WavClip;

public class AcousticFrontEnd {
	private WavClip wav    = null;
	private int FFT_N      = 512;
	private int MFCC_CH    =  26;

	private int step_len =  10 /*ms*/; 
	private int step_size;
	private int frame_len  =  20 /*ms*/;
	private int frame_size;         /*number*/
	private int totalFrame;      /*Total number of frames*/
	
	private int [] samples;      /*array*/
	private int samples_size;     /*number*/

	private int hz;
	
	private String windowType;
	//public AcousticFrontEnd(int FFT_N, int MFCC_CH, int offset_len, int frame_len){
		//this.FFT_N = FFT_N;
		//this.MFCC_CH = MFCC_CH;
		//this.offset_len = offset_len;
		//this.frame_len = frame_len;    
	//}
	
	public void setWavClip(WavClip wav){
		this.wav         = wav;
		this.samples     = wav.toIntArray();       // Translate wav to Integer  
        this.samples_size = samples.length;        // Get Sample sizes (Numbers)
		this.hz = (int) wav.getAudioFormat().getSampleRate();     // Get sampling rate

//		  this.frame_size  = frame_len  * hz / 1000;   // Get frame sizes (Numbers)
//        this.step_size = step_len * hz / 1000;   // Get offset sizes (Numbers)

		this.step_size = step_len * hz / 1000;   // Get offset sizes (Numbers)
        
		System.out.println("Samp size: " + this.samples_size);
        System.out.println("Hz: " + hz);
//        System.out.println("Frame_numbers: "+ frame_size);
		}


	private int[][] getFrames(int[] samples){
		this.totalFrame = (1 - FFT_N + step_size) / step_size; // Get Total Number of Frames
		int [][] frames = new int[totalFrame][FFT_N];              // Create Frame Array
		
		int start = 0;
		
	    for(int i=0; i<totalFrame; i++){
	        int [] eachFrame = Arrays.copyOfRange(this.samples, start, FFT_N);
	        
	        for(int j=0; j < FFT_N; j++){
	        	frames[i][j] = eachFrame[j]; 
	        }
	        
	        start = start + step_size;
	    }
	    
	    return frames;
	}


	private double[][] getWindowedFrames(int[][] frames, String windowType){
		double [][] windowedFrames = new double[totalFrame][FFT_N];
		
		if(this.windowType == "Hamming"){
	        for(int i=0; i<totalFrame; i++){
			    for(int j=0; j<FFT_N; j++){

				    windowedFrames[i][j] *= (0.54 - 0.46 * Math.cos(2 * Math.PI * j / (FFT_N - 1))); // HammignWin
		  	    }
		    }
		}
		else if(this.windowType == "Hanning"){
			for(int i=0; i<totalFrame; i++){
			    for(int j=0; j<frames[i].length; j++){

				    windowedFrames[i][j] *= (0.5 - 0.5 * Math.cos(2 * Math.PI * j / (FFT_N - 1)));  // HanningWin
				    }
			}
		}
		
		return windowedFrames;
	}

//Untile here! 201510/20
	
	private int[][] getFft(int[][] windowedFrames){
		def fft = new FastFourierTransformer();
		int [][] fftAray = new int[totalFrame][frame_n];
		
		for(int i; i < windowedFrames.length; i++){
             for (int j; j < windowedFrames[i].length; j++){
            	 Complex[] Fft = fft.transform(this.windowedFrames[i]);
                 fftAray[i] = Complex;  
             }
             
        return fftAray;
  	    }
	}
	
	private int[][] getMfcc(int [][] fftAray){
		return null;
	}

	public static void main(String[] args) throws Exception{
		WavClip wav = new WavClip("/home/snoopy/workspace/schnitt/audiocore-0.0.3/sample.wav");

		
		AcousticFrontEnd fe = new AcousticFrontEnd();
		
		fe.setWavClip(wav);
		int [] samples = wav.toIntArray();
		int[][] frames = fe.getFrames(samples);
		int [][] WindowedFrames = fe.getWindowedFrames(frames);
		int [][] fft = 
    }
}

