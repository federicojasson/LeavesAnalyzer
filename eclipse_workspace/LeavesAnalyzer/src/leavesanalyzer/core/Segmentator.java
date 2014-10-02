package leavesanalyzer.core;

import org.opencv.core.Mat;

public class Segmentator {
	
	public static Mat segment(Mat sampleImage) {
		switch (Config.segmentationMethod) {
			case GROUND :
				return segmentGround(sampleImage);
			case PLAIN :
				return segmentPlain(sampleImage);
			default :
				throw new InvalidSegmentationMethodException();
		}
	}
	
	private static Mat segmentGround(Mat sampleImage) {
		Mat cleanSampleImage = sampleImage.clone();
		Filter.median(cleanSampleImage, 9);
		Filter.averaging(cleanSampleImage, 3);
		
		Mat NDIImage = Filter.NDI(cleanSampleImage);
		Mat ExGImage = Filter.ExG(cleanSampleImage);
		Mat shadowMask = Filter.shadows(cleanSampleImage);
		
		Mat[] indexImages = new Mat[] {
			NDIImage,
			ExGImage,
			shadowMask
		};
		
		int[] weights = new int[] {
			// NDI weight
			10,
			// ExG weight
			10,
			// Shadows weight
			- 1
		};
		
		Mat indexImage = Filter.weightedSum(indexImages, weights);
		Filter.median(indexImage, 11);
		Filter.averaging(indexImage, 5);
		
		Mat segmentsMask = Filter.threshold(indexImage);
		Filter.fillContours(segmentsMask);
		
		// Cleanup
		cleanSampleImage.release();
		NDIImage.release();
		ExGImage.release();
		shadowMask.release();
		indexImage.release();
		
		return segmentsMask;
	}
	
	private static Mat segmentPlain(Mat sampleImage) {
		Mat cleanSampleImage = sampleImage.clone();
		Filter.median(cleanSampleImage, 9);
		Filter.averaging(cleanSampleImage, 3);
		
		Mat grayscaleImage = Filter.BGRToGrayscale(cleanSampleImage);
		Filter.negative(grayscaleImage);
		
		Mat segmentsMask = Filter.threshold(grayscaleImage);
		Filter.fillContours(segmentsMask);
		
		// Cleanup
		cleanSampleImage.release();
		grayscaleImage.release();
		
		return segmentsMask;
	}
	
}
