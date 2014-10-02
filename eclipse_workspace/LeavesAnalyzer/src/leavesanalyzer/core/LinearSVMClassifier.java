package leavesanalyzer.core;

import java.util.LinkedHashMap;
import java.util.Map;
import org.opencv.core.Mat;
import org.opencv.core.TermCriteria;
import org.opencv.ml.CvSVM;
import org.opencv.ml.CvSVMParams;

public class LinearSVMClassifier extends Classifier {
	
	private Map<String, Integer> classificationsMap;
	private Map<Integer, String> responsesMap;
	private CvSVM classifier;
	
	public LinearSVMClassifier(DataSet trainingDataSet) {
		classificationsMap = new LinkedHashMap<String, Integer>();
		responsesMap = new LinkedHashMap<Integer, String>();
		classifier = new CvSVM();
		
		String[] classifications = trainingDataSet.getClassifications();
		Utility.fillClassificationMaps(classifications, classificationsMap, responsesMap);
		
		Mat responses = Utility.mapClassifications(classifications, classificationsMap);
		Mat varIdx = new Mat();
		Mat sampleIdx = new Mat();
		CvSVMParams parameters = new CvSVMParams();
		parameters.set_kernel_type(CvSVM.LINEAR);
		TermCriteria terminationCriteria = new TermCriteria();
		terminationCriteria.type = TermCriteria.MAX_ITER | TermCriteria.EPS;
		terminationCriteria.maxCount = 10000;
		terminationCriteria.epsilon = 0.0000001;
		parameters.set_term_crit(terminationCriteria);
		classifier.train_auto(trainingDataSet.getData(), responses, varIdx, sampleIdx, parameters);
		
		// Cleanup
		responses.release();
		varIdx.release();
		sampleIdx.release();
	}
	
	public String[] classify(Mat testData) {
		Mat responses = new Mat();
		classifier.predict_all(testData, responses);
		String[] classifications = Utility.mapResponses(responses, responsesMap);
		
		// Cleanup
		responses.release();
		
		return classifications;
	}
	
}
