package leavesanalyzer.core;

import org.opencv.core.Mat;

public class DataSet {
	
	private Mat data;
	private String[] classifications;
	
	public DataSet(Mat data, String[] classifications) {
		this.data = data;
		this.classifications = classifications;
	}
	
	public String[] getClassifications() {
		return classifications;
	}
	
	public Mat getData() {
		return data;
	}
	
	public void release() {
		// Cleanup
		data.release();
	}
	
}
