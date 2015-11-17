package jp.ac.kobe.stu.watanabe;

public class FirFilter {
	final double [] coef;
	static int [] samples;
	
	public FirFilter(double [] coef) {
		this.coef = coef;
	}


	public double [] doFirFilter(int [] sample){
		samples = sample;
		
		double [] filteredAry = new double [samples.length];
		for(int n = 0; n<samples.length; n++){
			for(int i = 0; i<coef.length; i++){
				if(n - i >= 0){
					double value = coef[i] * samples[n-i]; 
					filteredAry[n] += value;
				}
			}
		}

		return filteredAry;
		
	}

}
