package leavesanalyzer.core;

import java.io.IOException;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Scalar;

public class InteractiveProcessor {
	
	private int currentStep;
	private Mat[] images;
	private Mat[] resizedImages;
	private Segment[] segments;
	private DataSet dataSet;
	private Classifier classifier;
	
	private static final int STEPS = 5;
	
	private static final int SAMPLE_IMAGE = 0;
	private static final int SEGMENTS_MASK = 1;
	private static final int MASKED_SAMPLE_IMAGE = 2;
	private static final int SEGMENTS_IMAGE = 3;
	private static final int AUXILIAR_STRUCTURES_IMAGE = 4;
	
	private static final String[] STEP_NAMES = new String[] {
		"    Imagen original",
		" 1- Máscara de segmentos",
		" 2- Máscara de segmentos aplicada",
		" 3- Segmentos",
		" 4- Segmentos y estructuras auxiliares"
	};
	
	private static final Scalar SEGMENT_COLORS[] = {
		new Scalar(255, 167, 103),
		new Scalar(73, 164, 241),
		new Scalar(86, 214, 188),
		new Scalar(119, 119, 214),
		new Scalar(208, 115, 214),
		new Scalar(200, 166, 152),
		new Scalar(201, 214, 155),
		new Scalar(45, 228, 247),
		new Scalar(248, 229, 35),
		new Scalar(26, 195, 62)
	};
	private static final int SEGMENT_THICKNESS = 2;
	private static final Scalar BOUNDING_BOX_COLOR = new Scalar(0, 255, 0);
	private static final int BOUNDING_BOX_THICKNESS = 1;
	private static final Scalar CONVEX_HULL_COLOR = new Scalar(255, 0, 0);
	private static final int CONVEX_HULL_THICKNESS = 1;
	private static final Scalar DIAMETER_LINE_COLOR = new Scalar(0, 0, 255);
	private static final int DIAMETER_LINE_THICKNESS = 1;
	
	public InteractiveProcessor(String imagePath, DataSet dataSet) throws IOException {
		currentStep = - 1;
		images = new Mat[STEPS];
		resizedImages = new Mat[STEPS];
		segments = null;
		this.dataSet = dataSet;
		classifier = Classifier.create(dataSet);
		
		// Inserts the sample image
		insertImage(step0(imagePath));
	}
	
	public int getCurrentStep() {
		return currentStep;
	}
	
	public Mat getResizedImage(int step) {
		return resizedImages[step];
	}
	
	public String getSegmentInformation(int x, int y) {
		if (segments == null)
			// Segments haven't been computed yet
			return new String();
		
		// The coordinates x and y correspond to the resized image, so a
		// transformation must be done
		x = (int) (x * images[SAMPLE_IMAGE].cols() / (double) resizedImages[SAMPLE_IMAGE].cols());
		y = (int) (y * images[SAMPLE_IMAGE].rows() / (double) resizedImages[SAMPLE_IMAGE].rows());
		Point point = new Point(x, y);
		
		// Searches the segment
		Segment foundSegment = null;
		for (Segment segment : segments)
			if (Utility.segmentContainsPoint(segment, point)) {
				foundSegment = segment;
				break;
			}
		
		if (foundSegment == null)
			// No segment was found
			return new String();
		
		// A segment was found
		float[] featureVector = Measurer.computeFeatureVector(foundSegment);
		Mat testData = new Mat(1, featureVector.length, CvType.CV_32F);
		testData.put(0, 0, featureVector);
		String[] classifications = classifier.classify(testData);
		
		String information = new String();
		information += "Vector de características:" + System.lineSeparator();
		for (float feature : featureVector)
			information += "\t" + feature + System.lineSeparator();
		information += System.lineSeparator();
		information += "Especie: " + classifications[0];
		
		// Cleanup
		testData.release();
		
		return information;
	}
	
	public String getStepName(int step) {
		return STEP_NAMES[step];
	}
	
	public boolean isFinished() {
		return currentStep + 1 == STEPS;
	}
	
	public void release() {
		// Cleanup
		for (Mat image : images)
			if (image != null)
				image.release();
		
		for (Mat resizedImage : resizedImages)
			if (resizedImage != null)
				resizedImage.release();
		
		if (segments != null)
			for (Segment segment : segments)
				segment.release();
		
		dataSet.release();
	}
	
	public void step() {
		Mat image = null;
		
		switch (currentStep + 1) {
			case 1 : {
				image = step1();
				break;
			}
			
			case 2 : {
				image = step2();
				break;
			}
			
			case 3 : {
				image = step3();
				break;
			}
			
			case 4 : {
				image = step4();
				break;
			}
		}
		
		// Inserts the image
		insertImage(image);
	}
	
	private void insertImage(Mat image) {
		Mat resizedImage;
		if (image.cols() > Config.maxImageSize || image.rows() > Config.maxImageSize)
			resizedImage = Filter.resize(image, Config.maxImageSize);
		else
			// No resizing needed
			resizedImage = image;
		
		currentStep++;
		images[currentStep] = image;
		resizedImages[currentStep] = resizedImage;
	}
	
	private Mat step0(String imagePath) throws IOException {
		// Sample image
		Mat sampleImage = IO.readBGRImage(imagePath);
		return sampleImage;
	}
	
	private Mat step1() {
		// Segments mask
		Mat segmentsMask = Segmentator.segment(images[SAMPLE_IMAGE]);
		return segmentsMask;
	}
	
	private Mat step2() {
		// Masked sample image
		Mat maskedSampleImage = Filter.mask(images[SAMPLE_IMAGE], images[SEGMENTS_MASK]);
		return maskedSampleImage;
	}
	
	private Mat step3() {
		// Segments image
		segments = Measurer.computeSegments(images[SEGMENTS_MASK]);
		
		Mat segmentsImage = images[SAMPLE_IMAGE].clone();
		for (int i = 0; i < segments.length; ++i)
			Filter.drawContour(segmentsImage, segments[i].getPoints(), SEGMENT_COLORS[i % SEGMENT_COLORS.length], SEGMENT_THICKNESS);
		
		return segmentsImage;
	}
	
	private Mat step4() {
		// Auxiliar structures image
		Mat auxiliarStructuresImage = images[SEGMENTS_IMAGE].clone();
		
		for (Segment segment : segments) {
			Point[] boundingBox = (Point[]) segment.getProperty(SegmentPropertyID.BOUNDING_BOX);
			Filter.drawContour(auxiliarStructuresImage, boundingBox, BOUNDING_BOX_COLOR, BOUNDING_BOX_THICKNESS);
			
			MatOfPoint convexHull = (MatOfPoint) segment.getProperty(SegmentPropertyID.CONVEX_HULL);
			Filter.drawContour(auxiliarStructuresImage, convexHull, CONVEX_HULL_COLOR, CONVEX_HULL_THICKNESS);
			
			Point[] diameterLine = (Point[]) segment.getProperty(SegmentPropertyID.DIAMETER_LINE);
			Filter.drawLine(auxiliarStructuresImage, diameterLine, DIAMETER_LINE_COLOR, DIAMETER_LINE_THICKNESS);
		}
		
		return auxiliarStructuresImage;
	}
	
}
