package jp.ac.kobe.stu.watanabe;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

public class LinearRegression {
	final int coefNum;
	
	private double [] data;
	private double [] coef;
	private String basicFunc;
	private int M;
	private String basisFunction;
	
	public LinearRegression(int coefNum, double [] data, String basisFunc) {
		this.coefNum = coefNum;
		this.data = data;
		coef = new double[this.coefNum];
		this.basicFunc = basisFunc;
	}
	
	public void setBasisFunction(String bf){
		this.basisFunction = bf;
	}
	public void setM(int M){
		this.M = M;
	}
	
	
	/**
	 * cal. identity matrix
	 * @return identity matrix
	 */
	RealMatrix identityMatrix(){
		RealMatrix matrix = new Array2DRowRealMatrix(this.M, this.M);
		for(int i=0; i<this.M;i++){
			matrix.setEntry(i, i, 1);
		}
		return matrix;
	}

	RealVector phiVector(double x){
		RealVector vector = new ArrayRealVector(this.M);
		vector.setEntry(0, 1);
		for(int i=1;i<this.M;i++){
			vector.setEntry(i, Math.pow(x, i));
		}
		
//		System.err.println(" test: " + vector.getEntry(0) +" "+ 
//				vector.getEntry(1)+" "+
//				vector.getEntry(2)+" "+
//				vector.getEntry(3)
//				);
		return vector;
	}

	/**
	 * get phi-matrix
	 * @return
	 */
	RealMatrix phiMatrix(){
		RealMatrix matrix = new Array2DRowRealMatrix(this.data.length, this.M);
		for(int i = 0; i<this.data.length;i++){
			matrix.setRowVector(i, phiVector(this.data[i]));
		}
		
//		System.err.println(" test: " + 
//		matrix.getEntry(0, 0) +" "+ 
//		matrix.getEntry(0, 1)+" "+
//		matrix.getEntry(0, 2)+" "+
//		matrix.getEntry(0, 3)
//		);
		
		return matrix;
	}
	
	public void learn(){
		RealMatrix iMatrix = this.identityMatrix();
		RealMatrix  phiMatrix = this.phiMatrix();
		RealMatrix phi_t = phiMatrix.transpose();
		
	}
	
}

