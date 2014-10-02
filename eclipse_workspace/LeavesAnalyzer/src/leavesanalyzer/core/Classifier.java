package leavesanalyzer.core;

import org.opencv.core.Mat;

public abstract class Classifier {
	
	public abstract String[] classify(Mat testData);
	
	public static String[] classify(DataSet trainingDataSet, Mat testData) {
		Classifier classifier = create(trainingDataSet);
		return classifier.classify(testData);
	}
	
	public static Classifier create(DataSet trainingDataSet) {
		switch (Config.classificationMethod) {
			case KNN :
				return new KNNClassifier(trainingDataSet);
			case LINEAR_SVM :
				return new LinearSVMClassifier(trainingDataSet);
			case NORMAL_BAYES :
				return new NormalBayesClassifier(trainingDataSet);
			default :
				throw new InvalidClassificationMethodException();
		}
	}
	
}
