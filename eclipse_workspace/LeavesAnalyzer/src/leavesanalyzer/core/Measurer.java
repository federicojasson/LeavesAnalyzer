package leavesanalyzer.core;

import java.util.ArrayList;
import java.util.List;
import org.opencv.core.Mat;
import org.opencv.core.MatOfInt;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.imgproc.Imgproc;

public class Measurer {
	
	public static float[] computeFeatureVector(Segment segment) {
		return new float[] {
			((Double) segment.getProperty(SegmentPropertyID.ASPECT_RATIO)).floatValue(),
			((Double) segment.getProperty(SegmentPropertyID.FORM_FACTOR)).floatValue(),
			((Double) segment.getProperty(SegmentPropertyID.HU_MOMENT_1)).floatValue(),
			((Double) segment.getProperty(SegmentPropertyID.HU_MOMENT_2)).floatValue(),
			//((Double) segment.getProperty(SegmentPropertyID.HU_MOMENT_3)).floatValue(),
			//((Double) segment.getProperty(SegmentPropertyID.HU_MOMENT_4)).floatValue(),
			//((Double) segment.getProperty(SegmentPropertyID.HU_MOMENT_5)).floatValue(),
			//((Double) segment.getProperty(SegmentPropertyID.HU_MOMENT_6)).floatValue(),
			//((Double) segment.getProperty(SegmentPropertyID.HU_MOMENT_7)).floatValue(),
			((Double) segment.getProperty(SegmentPropertyID.RATIO_PERIMETER_BREADTH_LENGTH)).floatValue(),
			((Double) segment.getProperty(SegmentPropertyID.RATIO_PERIMETER_LENGTH)).floatValue(),
			((Double) segment.getProperty(SegmentPropertyID.RECTANGULARITY)).floatValue(),
			//((Double) segment.getProperty(SegmentPropertyID.ROUNDNESS)).floatValue(),
			((Double) segment.getProperty(SegmentPropertyID.SOLIDITY)).floatValue()
		};
	}
	
	public static Object computeProperty(Segment segment, SegmentPropertyID propertyID) {
		switch (propertyID) {
			case AREA :
				return computeArea(segment);
			case ASPECT_RATIO :
				return computeAspectRatio(segment);
			case BOUNDING_BOX :
				return computeBoundingBox(segment);
			case BREADTH :
				return computeBreadth(segment);
			case CONVEX_AREA :
				return computeConvexArea(segment);
			case CONVEX_HULL :
				return computeConvexHull(segment);
			case DIAMETER_LINE :
				return computeDiameterLine(segment);
			case FORM_FACTOR :
				return computeFormFactor(segment);
			case HU_MOMENT_1 :
				return computeHuMoment1(segment);
			case HU_MOMENT_2 :
				return computeHuMoment2(segment);
			case HU_MOMENT_3 :
				return computeHuMoment3(segment);
			case HU_MOMENT_4 :
				return computeHuMoment4(segment);
			case HU_MOMENT_5 :
				return computeHuMoment5(segment);
			case HU_MOMENT_6 :
				return computeHuMoment6(segment);
			case HU_MOMENT_7 :
				return computeHuMoment7(segment);
			case HU_MOMENTS :
				return computeHuMoments(segment);
			case LENGTH :
				return computeLength(segment);
			case PERIMETER :
				return computePerimeter(segment);
			case RATIO_PERIMETER_BREADTH_LENGTH :
				return computeRatioPerimeterBreadthLength(segment);
			case RATIO_PERIMETER_LENGTH :
				return computeRatioPerimeterLength(segment);
			case RECTANGULARITY :
				return computeRectangularity(segment);
			case ROUNDNESS :
				return computeRoundness(segment);
			case SOLIDITY :
				return computeSolidity(segment);
			default :
				throw new InvalidSegmentPropertyIDException();
		}
	}
	
	public static Segment[] computeSegments(Mat segmentsMask) {
		Mat segmentsMaskCopy = segmentsMask.clone(); // findContours modifies the input image
		List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
		Mat hierarchy = new Mat();
		Imgproc.findContours(segmentsMaskCopy, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);
		
		List<Segment> segmentList = new ArrayList<Segment>();
		for (int i = 0; i < contours.size(); ++i) {
			MatOfPoint points = contours.get(i);
			if (points.total() > 1)
				// The segment has at least 2 points
				segmentList.add(new Segment(points));
			else
				// Cleanup
				points.release();
		}
		
		Segment[] segments = new Segment[segmentList.size()];
		segments = segmentList.toArray(segments);
		
		// Cleanup
		segmentsMaskCopy.release();
		hierarchy.release();
		
		return segments;
	}
	
	private static double computeArea(Segment segment) {
		return Imgproc.contourArea(segment.getPoints());
	}
	
	private static double computeAspectRatio(Segment segment) {
		double breadth = (double) segment.getProperty(SegmentPropertyID.BREADTH);
		double length = (double) segment.getProperty(SegmentPropertyID.LENGTH);
		return length / breadth;
	}
	
	private static Point[] computeBoundingBox(Segment segment) {
		MatOfPoint convexHull = (MatOfPoint) segment.getProperty(SegmentPropertyID.CONVEX_HULL);
		Point[] diameterLine = (Point[]) segment.getProperty(SegmentPropertyID.DIAMETER_LINE);
		
		Point[] points = convexHull.toArray();
		Point meanPoint = Utility.meanPoint(points);
		double angle = Utility.lineAngle(diameterLine);
		
		// Rotates the convex hull points
		Point[] rotatedPoints = Utility.rotatedPoints(points, meanPoint, angle);
		
		// Gets the bounding box of the rotated convex hull
		Point[] rotatedBoundingBox = Utility.boundingBox(rotatedPoints);
		
		// Rotates back the points to get the actual bounding box
		Point[] boundingBox = Utility.rotatedPoints(rotatedBoundingBox, meanPoint, - angle);
		
		return boundingBox;
	}
	
	private static double computeBreadth(Segment segment) {
		Point[] boundingBox = (Point[]) segment.getProperty(SegmentPropertyID.BOUNDING_BOX);
		double distance1 = Utility.distance(boundingBox[1], boundingBox[0]);
		double distance2 = Utility.distance(boundingBox[1], boundingBox[2]);
		return Math.min(distance1, distance2);
	}
	
	private static double computeConvexArea(Segment segment) {
		MatOfPoint convexHull = (MatOfPoint) segment.getProperty(SegmentPropertyID.CONVEX_HULL);
		return Imgproc.contourArea(convexHull);
	}
	
	private static MatOfPoint computeConvexHull(Segment segment) {
		MatOfInt indices = new MatOfInt();
		Imgproc.convexHull(segment.getPoints(), indices);
		
		int[] indicesData = indices.toArray();
		Point[] segmentData = segment.getPoints().toArray();
		
		int count = indicesData.length;
		Point[] convexHullData = new Point[count];
		for (int i = 0; i < count; ++i) {
			int index = indicesData[i];
			convexHullData[i] = segmentData[index];
		}
		
		MatOfPoint convexHull = new MatOfPoint(convexHullData);
		
		// Cleanup
		indices.release();
		
		return convexHull;
	}
	
	private static Point[] computeDiameterLine(Segment segment) {
		Point[] diameterLine = new Point[2];
		
		MatOfPoint convexHull = (MatOfPoint) segment.getProperty(SegmentPropertyID.CONVEX_HULL);
		Point[] convexHullData = convexHull.toArray();
		
		// Searches the most separated pair of points
		int count = convexHullData.length;
		double maxSquareDistance = - 1;
		for (int i = 0; i < count; ++i)
			for (int j = i + 1; j < count; ++j) {
				Point p1 = convexHullData[i];
				Point p2 = convexHullData[j];
				
				double squareDistance = Utility.squareDistance(p1, p2);
				if (squareDistance > maxSquareDistance) {
					maxSquareDistance = squareDistance;
					diameterLine[0] = p1;
					diameterLine[1] = p2;
				}
			}
		
		return diameterLine;
	}
	
	private static double computeFormFactor(Segment segment) {
		double area = (double) segment.getProperty(SegmentPropertyID.AREA);
		double perimeter = (double) segment.getProperty(SegmentPropertyID.PERIMETER);
		return 4 * Math.PI * area / (perimeter * perimeter);
	}
	
	private static double computeHuMoment1(Segment segment) {
		double[] huMoments = (double[]) segment.getProperty(SegmentPropertyID.HU_MOMENTS);
		return huMoments[0];
	}
	
	private static double computeHuMoment2(Segment segment) {
		double[] huMoments = (double[]) segment.getProperty(SegmentPropertyID.HU_MOMENTS);
		return huMoments[1];
	}
	
	private static double computeHuMoment3(Segment segment) {
		double[] huMoments = (double[]) segment.getProperty(SegmentPropertyID.HU_MOMENTS);
		return huMoments[2];
	}
	
	private static double computeHuMoment4(Segment segment) {
		double[] huMoments = (double[]) segment.getProperty(SegmentPropertyID.HU_MOMENTS);
		return huMoments[3];
	}
	
	private static double computeHuMoment5(Segment segment) {
		double[] huMoments = (double[]) segment.getProperty(SegmentPropertyID.HU_MOMENTS);
		return huMoments[4];
	}
	
	private static double computeHuMoment6(Segment segment) {
		double[] huMoments = (double[]) segment.getProperty(SegmentPropertyID.HU_MOMENTS);
		return huMoments[5];
	}
	
	private static double computeHuMoment7(Segment segment) {
		double[] huMoments = (double[]) segment.getProperty(SegmentPropertyID.HU_MOMENTS);
		return huMoments[6];
	}
	
	private static double[] computeHuMoments(Segment segment) {
		Mat huMomentsMat = new Mat();
		Imgproc.HuMoments(Imgproc.moments(segment.getPoints()), huMomentsMat);
		int count = (int) huMomentsMat.total() * huMomentsMat.channels();
		
		double[] huMomentsMatData = new double[count];
		huMomentsMat.get(0, 0, huMomentsMatData);
		
		double[] huMoments = new double[count];
		for (int i = 0; i < count; ++i) {
			huMoments[i] = - Math.log10(Math.abs(huMomentsMatData[i]));
			if (Double.isInfinite(huMoments[i]))
				huMoments[i] = Double.MAX_VALUE;
		}
		
		// Cleanup
		huMomentsMat.release();
		
		return huMoments;
	}
	
	private static double computeLength(Segment segment) {
		Point[] boundingBox = (Point[]) segment.getProperty(SegmentPropertyID.BOUNDING_BOX);
		double distance1 = Utility.distance(boundingBox[1], boundingBox[0]);
		double distance2 = Utility.distance(boundingBox[1], boundingBox[2]);
		return Math.max(distance1, distance2);
	}
	
	private static double computePerimeter(Segment segment) {
		MatOfPoint2f points = new MatOfPoint2f(segment.getPoints().toArray());
		double perimeter = Imgproc.arcLength(points, true);
		
		// Cleanup
		points.release();
		
		return perimeter;
	}
	
	private static double computeRatioPerimeterBreadthLength(Segment segment) {
		double breadth = (double) segment.getProperty(SegmentPropertyID.BREADTH);
		double length = (double) segment.getProperty(SegmentPropertyID.LENGTH);
		double perimeter = (double) segment.getProperty(SegmentPropertyID.PERIMETER);
		return perimeter / (breadth + length);
	}
	
	private static double computeRatioPerimeterLength(Segment segment) {
		double length = (double) segment.getProperty(SegmentPropertyID.LENGTH);
		double perimeter = (double) segment.getProperty(SegmentPropertyID.PERIMETER);
		return perimeter / length;
	}
	
	private static double computeRectangularity(Segment segment) {
		double area = (double) segment.getProperty(SegmentPropertyID.AREA);
		double breadth = (double) segment.getProperty(SegmentPropertyID.BREADTH);
		double length = (double) segment.getProperty(SegmentPropertyID.LENGTH);
		return length * breadth / area;
	}
	
	private static double computeRoundness(Segment segment) {
		double area = (double) segment.getProperty(SegmentPropertyID.AREA);
		double length = (double) segment.getProperty(SegmentPropertyID.LENGTH);
		return 4 * area / (Math.PI * length * length);
	}
	
	private static double computeSolidity(Segment segment) {
		double area = (double) segment.getProperty(SegmentPropertyID.AREA);
		double convexArea = (double) segment.getProperty(SegmentPropertyID.CONVEX_AREA);
		return area / convexArea;
	}
	
}
