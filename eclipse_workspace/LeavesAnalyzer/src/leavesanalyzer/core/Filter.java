package leavesanalyzer.core;

import java.util.ArrayList;
import java.util.List;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfInt;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

public class Filter {
	
	public static void averaging(Mat image, int kernelSize) {
		Imgproc.boxFilter(image, image, - 1, new Size(kernelSize, kernelSize));
	}
	
	public static Mat BGRToGrayscale(Mat BGRImage) {
		Mat grayscaleImage = new Mat();
		Imgproc.cvtColor(BGRImage, grayscaleImage, Imgproc.COLOR_BGR2GRAY);
		return grayscaleImage;
	}
	
	public static Mat BGRToHSV(Mat BGRImage) {
		Mat HSVImage = new Mat();
		Imgproc.cvtColor(BGRImage, HSVImage, Imgproc.COLOR_BGR2HSV);
		return HSVImage;
	}
	
	public static Mat channel(Mat image, int index) {
		Mat channel = new Mat(image.size(), CvType.CV_8U);
		
		List<Mat> sources = new ArrayList<Mat>(1);
		sources.add(image);
		
		List<Mat> destinations = new ArrayList<Mat>(1);
		destinations.add(channel);
		
		MatOfInt fromTo = new MatOfInt(index, 0);
		Core.mixChannels(sources, destinations, fromTo);
		
		// Cleanup
		for (Mat source : sources)
			source.release();
		
		fromTo.release();
		
		return channel;
	}
	
	public static void drawContour(Mat image, MatOfPoint points, Scalar color, int thickness) {
		List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
		contours.add(points);
		Imgproc.drawContours(image, contours, - 1, color, thickness);
	}
	
	public static void drawContour(Mat image, Point[] points, Scalar color, int thickness) {
		MatOfPoint pointsMat = new MatOfPoint(points);
		drawContour(image, pointsMat, color, thickness);
		
		// Cleanup
		pointsMat.release();
	}
	
	public static void drawLine(Mat image, Point[] line, Scalar color, int thickness) {
		Core.line(image, line[0], line[1], color, thickness);
	}
	
	public static Mat ExG(Mat BGRImage) {
		int rows = BGRImage.rows();
		int columns = BGRImage.cols();
		int total = rows * columns;
		
		byte[] BGRImageData = new byte[3 * total];
		BGRImage.get(0, 0, BGRImageData);
		
		byte[] ExGImageData = new byte[total];
		for (int i = 0; i < total; ++i) {
			int blue = BGRImageData[3 * i] & 255;
			int green = BGRImageData[3 * i + 1] & 255;
			int red = BGRImageData[3 * i + 2] & 255;
			
			ExGImageData[i] = (byte) (((2 * green - red - blue) + 510) * 0.25);
		}
		
		Mat ExGImage = new Mat(rows, columns, CvType.CV_8U);
		ExGImage.put(0, 0, ExGImageData);
		return ExGImage;
	}
	
	public static void fillContours(Mat binaryImage) {
		List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
		Mat hierarchy = new Mat();
		Imgproc.findContours(binaryImage, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);
		
		Scalar color = new Scalar(255);
		Imgproc.drawContours(binaryImage, contours, - 1, color, Core.FILLED);
		
		// Cleanup
		for (MatOfPoint contour : contours)
			contour.release();
		
		hierarchy.release();
	}
	
	public static Mat mask(Mat image, Mat mask) {
		Mat maskedImage = new Mat();
		image.copyTo(maskedImage, mask);
		return maskedImage;
	}
	
	public static void median(Mat image, int kernelSize) {
		Imgproc.medianBlur(image, image, kernelSize);
	}
	
	public static Mat NDI(Mat BGRImage) {
		int rows = BGRImage.rows();
		int columns = BGRImage.cols();
		int total = rows * columns;
		
		byte[] BGRImageData = new byte[3 * total];
		BGRImage.get(0, 0, BGRImageData);
		
		byte[] NDIImageData = new byte[total];
		for (int i = 0; i < total; ++i) {
			int green = BGRImageData[3 * i + 1] & 255;
			int red = BGRImageData[3 * i + 2] & 255;
			
			if (green + red != 0)
				NDIImageData[i] = (byte) (((green - red) / (double) (green + red) + 1) * 127.5);
			else
				NDIImageData[i] = 0;
		}
		
		Mat NDIImage = new Mat(rows, columns, CvType.CV_8U);
		NDIImage.put(0, 0, NDIImageData);
		return NDIImage;
	}
	
	public static void negative(Mat image) {
		Core.bitwise_not(image, image);
	}
	
	public static void opening(Mat binaryImage, int iterations) {
		Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(3, 3));
		Imgproc.morphologyEx(binaryImage, binaryImage, Imgproc.MORPH_OPEN, kernel, new Point(- 1, - 1), iterations);
		
		// Cleanup
		kernel.release();
	}
	
	public static Mat resize(Mat image, int maxSize) {
		int rows = image.rows();
		int columns = image.cols();
		
		int width;
		int height;
		if (columns > rows) {
			width = maxSize;
			height = width * rows / columns;
		} else {
			height = maxSize;
			width = height * columns / rows;
		}
		
		Mat resizedImage = new Mat();
		Imgproc.resize(image, resizedImage, new Size(width, height), 0, 0, Imgproc.INTER_AREA);
		return resizedImage;
	}
	
	public static Mat shadows(Mat BGRImage) {
		Mat HSVImage = Filter.BGRToHSV(BGRImage);
		Mat valueChannel = Filter.channel(HSVImage, 2);
		
		Mat shadows = Filter.threshold(valueChannel, 48); // 48 it's a value found experimentally
		Filter.negative(shadows);
		Filter.opening(shadows, 5);
		
		// Cleanup
		HSVImage.release();
		valueChannel.release();
		
		return shadows;
	}
	
	public static Mat threshold(Mat grayscaleImage) {
		Mat threshold = new Mat();
		Imgproc.threshold(grayscaleImage, threshold, 0, 255, Imgproc.THRESH_BINARY | Imgproc.THRESH_OTSU); // Otsu's method
		return threshold;
	}
	
	public static Mat threshold(Mat grayscaleImage, int thresholdValue) {
		Mat threshold = new Mat();
		Imgproc.threshold(grayscaleImage, threshold, thresholdValue, 255, Imgproc.THRESH_BINARY);
		return threshold;
	}
	
	public static Mat weightedSum(Mat[] grayscaleImages, int[] weights) {
		int count = grayscaleImages.length;
		int rows = grayscaleImages[0].rows();
		int columns = grayscaleImages[0].cols();
		int total = rows * columns;
		
		int minValue = 0;
		int maxValue = 0;
		for (int weight : weights)
			if (weight < 0)
				minValue += weight;
			else
				maxValue += weight;
		
		minValue *= 255;
		maxValue *= 255;
		
		byte[][] grayscaleImagesData = new byte[count][];
		for (int j = 0; j < count; ++j) {
			grayscaleImagesData[j] = new byte[total];
			grayscaleImages[j].get(0, 0, grayscaleImagesData[j]);
		}
		
		byte[] resultImageData = new byte[total];
		for (int i = 0; i < total; ++i) {
			int sum = 0;
			for (int j = 0; j < count; ++j)
				sum += weights[j] * (grayscaleImagesData[j][i] & 255);
			
			resultImageData[i] = (byte) ((sum - minValue) * (255.0 / (maxValue - minValue)));
		}
		
		Mat resultImage = new Mat(rows, columns, CvType.CV_8U);
		resultImage.put(0, 0, resultImageData);
		return resultImage;
	}
	
}
