package jp.ac.kobe.stu.watanabe;
import java.util.Arrays;

import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.transform.FastCosineTransformer;
import org.apache.commons.math3.transform.FastFourierTransformer;

import info.pinlab.pinsound.WavClip;
public class AcousticFrontEndRow {
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
	private Object windowedFrames;
	
	
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
		this.totalFrame = (1 - FFT_N + step_size) / step_size; // Get Total Number of Frames
		System.out.println("Samp size: " + this.samples_size);
        System.out.println("Hz: " + hz);
//        System.out.println("Frame_numbers: "+ frame_size);
		}


	private int[][] getFrames(int[] samples){
		int [][] frames = new int[totalFrame][FFT_N];              // Create Frame Array
		
		int start = 0;
		
	    for(int i=0; i<totalFrame; i++){
	        int [] eachFrame = Arrays.copyOfRange(samples, start, FFT_N);
	        
	        for(int j=0; j < FFT_N; j++){
	        	frames[i][j] = eachFrame[j]; 
	        }
	        
	        start = start + step_size;
	    }
	    
	    return frames;
	}

	private double[][] preEmph(int[][] frames){
   	    double preEmphCof = 0.97;
	    double [][] preEmphArray = null;
	
//	formula: PreemphFilter: y[n] = x[n] - p*x[n-1]
	    for(int i=0; i<totalFrame; i++){
		    for(int j=1; j<FFT_N; j++){
			    preEmphArray[i][j] = frames[i][j] - preEmphCof * frames[i][j -1];
		    }
	    }
	    return(preEmphArray);
	}
	

	private double[][] getWindowedFrames(int[][] frames, String windowType){
		double [][] windowedFrames = new double[totalFrame][FFT_N];
			if(this.windowType == "Hamming"){
	            for(int i=0; i<totalFrame; i++){
			        for(int j=0; j<FFT_N; j++){

				        windowedFrames[i][j] = frames[i][j] * ((0.54 - 0.46 * Math.cos(2 * Math.PI * j / (FFT_N - 1)))); // HammignWin
		  	        }
		        }
		    }
		    else if(this.windowType == "Hanning"){
			    for(int i=0; i<totalFrame; i++){
			        for(int j=0; j<frames[i].length; j++){

				    windowedFrames[i][j] =frames[i][j] * (0.5 - 0.5 * Math.cos(2 * Math.PI * j / (FFT_N - 1)));  // HanningWin
				    }
		     	}
		   }
		return windowedFrames;
	}

	
	private Complex[][] getFft(double[][] windowedFrames){
		FastFourierTransformer fft = new FastFourierTransformer(null);
		Complex [][] fftAray = new Complex [totalFrame][frame_size];
		
		for(int i=0; i < totalFrame; i++){
             for (int j=0; j < FFT_N; j++){
            	 Complex[] fftFrame = fft.transform(windowedFrames[j], null);
                 fftAray[i] = fftFrame;  
             }
  	    }
		return fftAray;
	}

	
	private double[][] getAmp(Complex[][] fftAray){
		double [][] AmpAray = new double [totalFrame][frame_size];
		
		for(int i=0; i < totalFrame; i++){
            for (int j=0; j < FFT_N; j++){
            	double Amp = Math.sqrt(Math.pow(fftAray[i][j].getReal(), 2) + Math.pow(fftAray[i][j].getImaginary(), 2));
                AmpAray[i][j] = Amp;
             }
  	    }
		return AmpAray;
	}


	private int[][] getMfcc(double [][] AmpAray){
		double fmax = hz / 2;  //Nyquist
		double melmax = 1127 * Math.log(hz / 700.0 + 1.0); // mel-Nyquist
		int nmax = FFT_N /2;
		double bin = hz / FFT_N;
		
		double dmel = melmax/ (MFCC_CH + 1);
		double [] melCenters = null;
	    double [] fcenters = null;
	    int[] indexCenter;
	    int[] indexStart;
	    int[] indexStop;
	    
	    
		for(int i=0; i<MFCC_CH; i++){
			double x = i * dmel;
			melCenters[i] = x;
			fcenters[i] = 700 * (Math.exp(x / 1127) -1.0);
		    indexCenter = (int) Math.round(fcenters[i] / bin);
		}
		
		indexStart[0] = 0; 
		
		for(int i=1; i<MFCC_CH; i++){
			indexStart[i] = indexCenter[i-1];
		}
		for(int i =1; i<MFCC_CH-1; i++){
			indexStop[i] = indexCenter[i];
		}
		
		indexStop[MFCC_CH] = indexCenter[nmax];
		
		double [][] filterBank = new double [MFCC_CH][nmax];
		
		for(int i =0; i<MFCC_CH;i++){
			double increment = 1.0 / (indexCenter[i] - indexCenter[i]);
			
			for(int j = indexStart[i]; j<indexCenter[i]; j++){
				filterBank[i][j] = (j - indexStart[i]) * increment;
			}
			
			double decrement = 1.0 / (indexStop[i] - indexCenter[i]);
			for(int k =indexCenter[i]; k<indexCenter[i]; k++){
				filterBank[i][j] = 1.0 - ((k - indexCenter[i]) * decrement);
			}
		}

		FastCosineTransformer fct = new FastCosineTransformer(null);
		double [][] melArry = new double [totalFrame][MFCC_CH];


		for(int i; i<totalFrame; i++){
			for(int j; j<MFCC_CH; j++){
				for(k; k<MFCC_CH;k++){
					double value;
					value += value * filterBank[k];
				}
				
				double logValue = Math.log10(value);
				melArry[i][j] = logValue;
			}
		}

		double MfccArry[] = new double[MFCC_CH];
		for(int i=0; i<totalFrame; i++){
			double[] fctResult = fct.transform(melArry, FORWARD);			
			MfccArry[i] = fctResult; 
		}

		return MfccArry;
		
	}

	
	public static void main(String[] args) throws Exception{
		WavClip wav = new WavClip("/home/snoopy/workspace/schnitt/audiocore-0.0.3/sample.wav");

		
		AcousticFrontEnd fe = new AcousticFrontEnd();
		
		fe.setWavClip(wav);
		int []  samples = wav.toIntArray();
		int [][] frames = fe.getFrames(samples);
		int [][] windowedFrames = fe.getWindowedFrames(frames, "Hamming");
		int [][] fft = fe.getFft(windowedFrames);
		int [][] amp = fe.getAmp(fft);
		int [][] mfcc = fe.getMfcc(amp);
    }
}