package leavesanalyzer.core;

import java.io.IOException;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

public class BatchProcessor {
	
	public static Mat processImages(String[] imagePaths) throws IOException, SegmentationException {
		float[][] featureVectors = new float[imagePaths.length][];
		
		for (int i = 0; i < imagePaths.length; ++i)
			featureVectors[i] = processImage(imagePaths[i]);
		
		int rows = featureVectors.length;
		int columns = featureVectors[0].length;
		Mat data = new Mat(rows, columns, CvType.CV_32F);
		data.put(0, 0, Utility.flattenMatrix(featureVectors));
		
		return data;
	}
	
	private static float[] processImage(String imagePath) throws IOException, SegmentationException {
		System.out.println("Procesando: " + imagePath);
		
		// Sample image
		Mat sampleImage = IO.readBGRImage(imagePath);
		
		// Segments mask
		Mat segmentsMask = Segmentator.segment(sampleImage);
		
		// Computes the segments using the mask
		Segment[] segments = Measurer.computeSegments(segmentsMask);
		
		// Cleanup
		sampleImage.release();
		segmentsMask.release();
		
		if (segments.length == 0)
			throw new SegmentationException(imagePath);
		
		// Searches the biggest computed segment
		Segment biggestSegment = Utility.biggestSegment(segments);
		
		// Computes the feature vector
		float[] featureVector = Measurer.computeFeatureVector(biggestSegment);
		
		// Cleanup
		for (Segment segment : segments)
			segment.release();
		
		return featureVector;
	}
	
}
