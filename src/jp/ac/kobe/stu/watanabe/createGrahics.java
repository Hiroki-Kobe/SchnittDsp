package jp.ac.kobe.stu.watanabe;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;


public class createGrahics extends Canvas {
		/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
		Dimension dimension;
		double [] filterBank;
		Graphics g = this.getGraphics();
		
		public createGrahics(int width, int height) {
			setSize(width, height);
			dimension = getSize();
			setBackground(Color.lightGray);
		}
		
		public void setArray(double [] filterBankArray) {
			filterBank = filterBankArray;			
		}
		
		@Override
		public void paint(Graphics g){
			dimension = getSize();		
			 //軸の色は黒
			  g.setColor(Color.black);
			  //x軸
			  g.drawLine(0,dimension.height/2,dimension.width-1,dimension.height/2);
			  //y軸
			  g.drawLine(dimension.width/2,0,dimension.width/2,dimension.height-1);


			  //グラフ線の色は青に設定
			  g.setColor(Color.blue);

			for(int i=0;i<dimension.width - 2; i++){
				g.drawLine(i, (int) filterBank[i], i+1, (int) filterBank[i+1]);
			}
		}
}
