package jp.ac.kobe.stu.watanabe;

public class hogehoge implements hogeinter{

	@Override
	public int add(int a, int b) {
		return a  + b;
	
	}

	@Override
	public int hiku(int a, int b) {
		return a - b;
	}
}
