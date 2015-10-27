package jp.ac.kobe.stu.watanabe;

public class AcousticFrontEndImp implements AcousticFrontEnd{

	private final int FFT_N;
	private final int MFCC_CH;
	
    private double [] features = null;
    
    
    public AcousticFrontEndImp(int fft_n, int mfcc_ch){
    	this.FFT_N = fft_n;
    	this.MFCC_CH = mfcc_ch;
    }
        
    @Override
    public void writeSamples(int [] samples){
    	assert(samples.length == this.FFT_N);
    	
    	if(samples.length != FFT_N){
    		throw new IllegalArgumentException();
    	}
    	
    }
    
    private double [] windowFrames(int [] frames){
    	return null;
    	
    }
    
    private double [] calcFft(double [] samples){
    	return null;
    }
 
    @Override
    public double readFeatures(){
    	return features;
    }
    
   
	public static void main(String[] args) {
		
	}

}
