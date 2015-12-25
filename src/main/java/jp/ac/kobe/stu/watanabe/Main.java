package jp.ac.kobe.stu.watanabe;

import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

import info.pinlab.pinsound.WavClip;

/**
 * This program is set the wav files and return the fft and mfcc value
 * 
 * @author snoopy
 * @version 1.0
 * @since 2015
 */
public class Main {
	public static void main(String[] args) throws Exception {
		final String HOME;
		final String filePath = "workspace/SchnittDsp/src/main/resources/jp/ac/kobe/stu/watanabe/test.wav";
		final String wavFileIn;

		final int fftn = 512;
		final int ch = 26;
		final String windowType = "Hunning";
		final int stepLength = 10; /* ms */
		final int windowLength = 20; /* ms */
		final int hz;
		final int MFCC_CEPS_N = 12;
		boolean running = true;

		/*
		 * Read a wave file from a directory
		 */
		HOME = System.getProperty("user.home");
		wavFileIn = new File(HOME, filePath).getPath();
		WavClip wav = new WavClip(wavFileIn);
		File fileIn = new File(wavFileIn);

		System.err.println(wavFileIn);
		/*
		 * Get sampling rate
		 */
		AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(fileIn);
		hz = (int) audioInputStream
				.getFormat()
				.getSampleRate();

		/*
		 * Set AcousticFrontEndFactory
		 */
		AcousticFrontEndFactory factory = new AcousticFrontEndFactoryImp(fftn, ch, windowType, windowLength, stepLength, hz);
		AcousticFrontEnd fe = factory
				.setFftN(fftn)
				.setMfccCh(ch)
				.setWindowType(windowType)
				.setWindowLength(windowLength)
				.setStepLength(stepLength)
				.build();

		/*
		 * Set Integer Samples
		 */
		int[] intSamples = wav.toIntArray();
		fe.setSamples(intSamples);
		int ix = 1;
		int deltaFrameNo = 5;
		int deltaIx = 1;
		
		/*
		 * Loop for Returning Acoustic Values
		 */
		double [][] tempMfcc = new double [deltaFrameNo][MFCC_CEPS_N];
		double [][] deltaMfccArr = new double [deltaFrameNo][MFCC_CEPS_N];
		final int totalIx = fe.getShiftNum();
		final int penalutimate = totalIx - 1;
		final int last = totalIx;
		
		System.err.println("totalIx: " + totalIx);
		double [] val = new double [2];
		double [] test = new double[3];
		for(int i =0; i<3;i++){
			test[i] = 1;
		}
		LinearRegression lr = new LinearRegression(2, test, "multiplay");
		lr.setM(4);
		//lr.phiVector(3.0);
		lr.phiMatrix();
		
//		while (running) {
//			double[] fftArr = fe.getFft();
//			double[] mfccArr = fe.getMfcc();
//			running = fe.next();
//
//			switch (deltaIx){
//				case 1:
//					tempMfcc[deltaIx-1] = mfccArr;
//					break;
//				case 2:
//					tempMfcc[deltaIx-1] = mfccArr;
//					break;
//				case 3:
//					tempMfcc[deltaIx-1] = mfccArr;
//					break;
//				case 4:
//					tempMfcc[deltaIx-1] = mfccArr;
//					break;
//				case 5:
//					tempMfcc[deltaIx-1] = mfccArr;
//					break;
//			}
//
//			
//			switch (ix){
//				case 1:
//					// do nothing;
//					break;
//				case 2:
//					// do nothing;
//					break;
//				case 3:
//					deltaMfccArr[0] = tempMfcc[0];
//					deltaMfccArr[1] = tempMfcc[0];
//					deltaMfccArr[2] = tempMfcc[0];
//					deltaMfccArr[3] = tempMfcc[1];
//					deltaMfccArr[4] = tempMfcc[2];
//					break;
//				case 4:
//					deltaMfccArr[0] = tempMfcc[0];
//					deltaMfccArr[1] = tempMfcc[0];
//					deltaMfccArr[2] = tempMfcc[1];
//					deltaMfccArr[3] = tempMfcc[2];
//					deltaMfccArr[4] = tempMfcc[3];
//					break;
//				default:
//					deltaMfccArr[0] = tempMfcc[0];
//					deltaMfccArr[1] = tempMfcc[1];
//					deltaMfccArr[2] = tempMfcc[2];
//					deltaMfccArr[3] = tempMfcc[3];
//					deltaMfccArr[4] = tempMfcc[4];
//				break;
//					
//			}
//
//			if(ix == penalutimate){
//				deltaMfccArr[0] = tempMfcc[totalIx-4];
//				deltaMfccArr[1] = tempMfcc[totalIx-3];
//				deltaMfccArr[2] = tempMfcc[totalIx-2];
//				deltaMfccArr[3] = tempMfcc[totalIx-1];
//				deltaMfccArr[4] = tempMfcc[totalIx-1];
//			}else if(ix == last){
//				deltaMfccArr[0] = tempMfcc[totalIx-3];
//				deltaMfccArr[1] = tempMfcc[totalIx-2];
//				deltaMfccArr[2] = tempMfcc[totalIx-1];
//				deltaMfccArr[3] = tempMfcc[totalIx-1];
//				deltaMfccArr[4] = tempMfcc[totalIx-1];
//			}
//			
//
//			for(int j =0; j<fftArr.length; j++){
//			System.out.println("[ix:" + (ix + 1) + "]" + "FFT: " +
//			fftArr[j]);
//			}
//			
//			for(int j =0; j<mfccArr.length; j++){
//			System.out.println( "[ix:" + (ix + 1) + "]"+ "MFCC: " +
//			mfccArr[j]);
//			}
//			ix ++;
//			
//			// Increase delta IX
//			if(deltaIx == 4){
//				deltaIx = 0;
//			}else{
//				deltaIx++;
//			}
//		}
	}
}