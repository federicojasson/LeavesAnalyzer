package leavesanalyzer.core;

import java.util.LinkedHashMap;
import java.util.Map;
import org.opencv.core.Mat;
import org.opencv.ml.CvKNearest;

public class KNNClassifier extends Classifier {
	
	private Map<String, Integer> classificationsMap;
	private Map<Integer, String> responsesMap;
	private CvKNearest classifier;
	
	public KNNClassifier(DataSet trainingDataSet) {
		classificationsMap = new LinkedHashMap<String, Integer>();
		responsesMap = new LinkedHashMap<Integer, String>();
		classifier = new CvKNearest();
		
		String[] classifications = trainingDataSet.getClassifications();
		Utility.fillClassificationMaps(classifications, classificationsMap, responsesMap);
		
		Mat responses = Utility.mapClassifications(classifications, classificationsMap);
		classifier.train(trainingDataSet.getData(), responses);
		
		// Cleanup
		responses.release();
	}
	
	public String[] classify(Mat testData) {
		Mat responses = new Mat();
		Mat neighborResponses = new Mat();
		Mat distances = new Mat();
		classifier.find_nearest(testData, 1, responses, neighborResponses, distances);
		String[] classifications = Utility.mapResponses(responses, responsesMap);
		
		// Cleanup
		responses.release();
		neighborResponses.release();
		distances.release();
		
		return classifications;
	}
	
}
