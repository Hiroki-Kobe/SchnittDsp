package jp.ac.kobe.stu.watanabe;

public class FirFilter {
	final double [] coef;
	private int [] samples;
	
	public FirFilter(double [] coef) {
		this.coef = coef;
	}


	public double [] doFirFilter(int [] samples){
		this.samples = samples;
		
		double [] filteredAry = new double [samples.length];
		for(int n = 0; n<samples.length; n++){
			for(int i = 0; i<coef.length; i++){
				if(n - i > 0){
					double value = 0;
					value += coef[i] * samples[n-1];
					filteredAry[n] = value;
				}
			}
		}

		return filteredAry;
		
	}

}
