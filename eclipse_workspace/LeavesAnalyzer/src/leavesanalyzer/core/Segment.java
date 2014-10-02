package leavesanalyzer.core;

import java.util.LinkedHashMap;
import java.util.Map;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;

public class Segment {
	
	private MatOfPoint points;
	private Map<SegmentPropertyID, Object> properties;
	
	public Segment(MatOfPoint points) {
		this.points = points;
		properties = new LinkedHashMap<SegmentPropertyID, Object>();
	}
	
	public MatOfPoint getPoints() {
		return points;
	}
	
	public Object getProperty(SegmentPropertyID propertyID) {
		Object property = properties.get(propertyID);
		if (property != null)
			return property;
		
		property = Measurer.computeProperty(this, propertyID);
		properties.put(propertyID, property);
		return property;
	}
	
	public void release() {
		// Cleanup
		points.release();
		
		for (Object property : properties.values())
			if (property instanceof Mat)
				((Mat) property).release();
	}
	
}
