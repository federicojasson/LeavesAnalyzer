package leavesanalyzer.core;

import java.awt.Desktop;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.imageio.ImageIO;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

public class Utility {
	
	public static Segment biggestSegment(Segment[] segments) {
		Segment biggestSegment = null;
		
		// Searches the biggest segment
		double maxArea = - 1;
		for (Segment segment : segments) {
			double area = (double) segment.getProperty(SegmentPropertyID.AREA);
			if (area > maxArea) {
				maxArea = area;
				biggestSegment = segment;
			}
		}
		
		return biggestSegment;
	}
	
	public static Point[] boundingBox(Point[] points) {
		double minX = Double.MAX_VALUE;
		double maxX = Double.MIN_VALUE;
		double minY = Double.MAX_VALUE;
		double maxY = Double.MIN_VALUE;
		
		for (Point point : points) {
			double x = point.x;
			double y = point.y;
			
			if (x < minX)
				minX = x;
			
			if (x > maxX)
				maxX = x;
			
			if (y < minY)
				minY = y;
			
			if (y > maxY)
				maxY = y;
		}
		
		if (minX == maxX)
			maxX += 1;
		
		if (minY == maxY)
			maxY += 1;
		
		Point[] boundingBox = new Point[4];
		boundingBox[0] = new Point(minX, minY);
		boundingBox[1] = new Point(maxX, minY);
		boundingBox[2] = new Point(maxX, maxY);
		boundingBox[3] = new Point(minX, maxY);
		
		return boundingBox;
	}
	
	public static BufferedImage bufferedImage(Mat image) {
		MatOfByte bytes = new MatOfByte();
		Highgui.imencode(".jpg", image, bytes);
		
		BufferedImage bufferedImage = null;
		
		try {
			InputStream inputStream = new ByteArrayInputStream(bytes.toArray());
			bufferedImage = ImageIO.read(inputStream);
		} catch (IOException exception) {}
		
		// Cleanup
		bytes.release();
		
		return bufferedImage;
	}
	
	public static double distance(Point p1, Point p2) {
		double differenceX = p1.x - p2.x;
		double differenceY = p1.y - p2.y;
		return Math.sqrt(differenceX * differenceX + differenceY * differenceY);
	}
	
	public static void fillClassificationMaps(String[] classifications, Map<String, Integer> stringToIntegerMap, Map<Integer, String> integerToStringMap) {
		int index = 0;
		for (String classification : classifications)
			if (! stringToIntegerMap.containsKey(classification)) {
				stringToIntegerMap.put(classification, index);
				integerToStringMap.put(index, classification);
				index++;
			}
	}
	
	public static float[] flattenMatrix(float[][] matrix) {
		int rows = matrix.length;
		int columns = matrix[0].length;
		float[] flatMatrix = new float[rows * columns];
		
		int index = 0;
		for (int i = 0; i < rows; ++i)
			for (int j = 0; j < columns; ++j)
				flatMatrix[index++] = matrix[i][j];
		
		return flatMatrix;
	}
	
	public static boolean isValidDataSetName(String dataSetName) {
		Pattern pattern = Pattern.compile("^[0-9A-Z_a-z]*$");
		Matcher matcher = pattern.matcher(dataSetName);
		return matcher.find();
	}
	
	public static double lineAngle(Point[] line) {
		double lineLength = Utility.distance(line[0], line[1]);
		
		double x;
		if (line[0].y > line[1].y)
			x = line[0].x - line[1].x;
		else
			x = line[1].x - line[0].x;
		
		return Math.acos(x / lineLength) * 180 / Math.PI;
	}
	
	public static Mat mapClassifications(String[] classifications, Map<String, Integer> classificationsMap) {
		float[] alias = new float[classifications.length];
		for (int i = 0; i < alias.length; ++i)
			alias[i] = classificationsMap.get(classifications[i]);
		
		Mat responses = new Mat(alias.length, 1, CvType.CV_32F);
		responses.put(0, 0, alias);
		
		return responses;
	}
	
	public static String[] mapResponses(Mat responses, Map<Integer, String> responsesMap) {
		float[] alias = new float[responses.rows()];
		responses.get(0, 0, alias);
		
		String[] classifications = new String[alias.length];
		for (int i = 0; i < classifications.length; ++i)
			classifications[i] = responsesMap.get((int) alias[i]);
		
		return classifications;
	}
	
	public static Point meanPoint(Point[] points) {
		double x = 0;
		double y = 0;
		
		// Adds the X and Y coordinates
		for (Point point : points) {
			x += point.x;
			y += point.y;
		}
		
		x = x / points.length;
		y = y / points.length;
		
		return new Point(x, y);
	}
	
	public static void openWebsite(String websiteAddress) {
		if (Desktop.isDesktopSupported()) {
			Desktop desktop = Desktop.getDesktop();
			
			if (desktop.isSupported(Desktop.Action.BROWSE))
				try {
					URI uri = new URI(websiteAddress);
					desktop.browse(uri);
				} catch (IOException | URISyntaxException exception) {}
		}
	}
	
	public static Point[] rotatedPoints(Point[] points, Point rotationPoint, double angle) {
		// Computes the rotation matrix
		Mat rotationMatrix = Imgproc.getRotationMatrix2D(rotationPoint, angle, 1);
		
		Point[] rotatedPoints = new Point[points.length];
		
		for (int i = 0; i < points.length; ++i) {
			Point point = points[i];
			Mat pointMat = new Mat(3, 1, CvType.CV_64F);
			pointMat.put(0, 0, new double[] {
				point.x,
				point.y,
				1
			});
			
			Mat resultMat = new Mat(2, 1, CvType.CV_64F);
			Mat delta = new Mat();
			
			// Multiplies the rotation matrix by the point
			Core.gemm(rotationMatrix, pointMat, 1, delta, 0, resultMat);
			double[] result = new double[2];
			resultMat.get(0, 0, result);
			
			rotatedPoints[i] = new Point(result[0], result[1]);
			
			// Cleanup
			delta.release();
		}
		
		// Cleanup
		rotationMatrix.release();
		
		return rotatedPoints;
	}
	
	public static boolean segmentContainsPoint(Segment segment, Point point) {
		MatOfPoint2f points = new MatOfPoint2f(segment.getPoints().toArray());
		boolean containsPoint = Imgproc.pointPolygonTest(points, point, false) >= 0;
		
		// Cleanup
		points.release();
		
		return containsPoint;
	}
	
	public static double squareDistance(Point p1, Point p2) {
		double differenceX = p1.x - p2.x;
		double differenceY = p1.y - p2.y;
		return differenceX * differenceX + differenceY * differenceY;
	}
	
}
