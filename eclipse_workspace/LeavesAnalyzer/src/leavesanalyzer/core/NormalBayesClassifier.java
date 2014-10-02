package leavesanalyzer.core;

import java.util.LinkedHashMap;
import java.util.Map;
import org.opencv.core.Mat;
import org.opencv.ml.CvNormalBayesClassifier;

public class NormalBayesClassifier extends Classifier {
	
	private Map<String, Integer> classificationsMap;
	private Map<Integer, String> responsesMap;
	private CvNormalBayesClassifier classifier;
	
	public NormalBayesClassifier(DataSet trainingDataSet) {
		classificationsMap = new LinkedHashMap<String, Integer>();
		responsesMap = new LinkedHashMap<Integer, String>();
		classifier = new CvNormalBayesClassifier();
		
		String[] classifications = trainingDataSet.getClassifications();
		Utility.fillClassificationMaps(classifications, classificationsMap, responsesMap);
		
		Mat responses = Utility.mapClassifications(classifications, classificationsMap);
		classifier.train(trainingDataSet.getData(), responses);
		
		// Cleanup
		responses.release();
	}
	
	public String[] classify(Mat testData) {
		Mat responses = new Mat();
		classifier.predict(testData, responses);
		String[] classifications = Utility.mapResponses(responses, responsesMap);
		
		// Cleanup
		responses.release();
		
		return classifications;
	}
	
}
