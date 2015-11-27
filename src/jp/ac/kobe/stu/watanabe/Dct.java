package jp.ac.kobe.stu.watanabe;

public class Dct {
	private int N;
	private double [][] D;
	
	public Dct(int dataLength){
		this.N = dataLength;
		D = new double [N][N];
		
		for(int i=0; i<N;i++){
			double k = i==0 ? 1.0 / Math.sqrt(2.0) : 1.0;
			for (int j = 0; j<N; j++){
				D[i][j] = Math.sqrt(2.0/ N) * k * Math.cos(i*(j+0.5)* Math.PI/ (double) N);
			}
		}
	}
	
	public double [] transform(double [] data){
		double [] dctArR = new double [N];
		
		for(int i = 0; i<N; i++){
			double sumOf = 0;
			for(int j = 0; j<N; j++){
				double value = D[i][j] * data[j];
				sumOf += value;
			}
			
			dctArR[i] = sumOf;
		}
		
		return dctArR;
	}
}
