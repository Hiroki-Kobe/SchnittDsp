package jp.ac.kobe.stu.watanabe;

public interface AcousticFrontEnd {
    public void writeSamples(int [] samples);
    public double [] readFeatures();
}
