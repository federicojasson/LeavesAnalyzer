package leavesanalyzer.core;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;

public class IO {
	
	private static final String CLASSIFICATIONS_FILE_SUFIX = "classifications.txt";
	private static final String DATA_FILE_SUFIX = "data.txt";
	private static final String DATA_SET_FOLDER = "data_sets";
	private static final String DATA_SET_NAMES_FILE_PATH = DATA_SET_FOLDER + "/data_sets.txt";
	
	public static boolean dataSetExists(String dataSetName) throws IOException {
		String[] dataSetNames = readDataSetNamesFile();
		for (String existingDataSetName : dataSetNames)
			if (existingDataSetName.equalsIgnoreCase(dataSetName))
				return true;
		
		return false;
	}
	
	public static Mat readBGRImage(String imagePath) throws IOException {
		Mat image = Highgui.imread(imagePath, Highgui.CV_LOAD_IMAGE_COLOR);
		
		if (image.empty())
			// The image could not be loaded
			throw new IOException(imagePath);
		
		return image;
	}
	
	public static String[] readClassificationsFile(String filePath) throws IOException {
		return readStringsFile(filePath);
	}
	
	public static DataSet readDataSet(String dataSetName) throws IOException {
		Mat data = readFloatMatFile(dataFilePath(dataSetName));
		String[] classifications = readStringsFile(classificationsFilePath(dataSetName));
		return new DataSet(data, classifications);
	}
	
	public static String[] readDataSetNamesFile() throws IOException {
		String[] dataSetNames;
		
		String dataSetNamesFilePath = dataSetNamesFilePath();
		if (! fileExists(dataSetNamesFilePath)) {
			dataSetNames = new String[] {};
			writeStringsFile(dataSetNamesFilePath, dataSetNames);
		} else
			dataSetNames = readStringsFile(dataSetNamesFilePath);
		
		return dataSetNames;
	}
	
	public static String[] readImagePathsFile(String filePath) throws IOException {
		return readStringsFile(filePath);
	}
	
	public static void writeDataSet(String dataSetName, DataSet dataSet) throws IOException {
		writeFloatMatFile(dataFilePath(dataSetName), dataSet.getData());
		writeStringsFile(classificationsFilePath(dataSetName), dataSet.getClassifications());
		
		// Updates the data set names file
		String[] dataSetNames = readDataSetNamesFile();
		
		List<String> newDataSetNamesList = new ArrayList<String>(dataSetNames.length);
		boolean dataSetNameExists = false;
		for (String existingDataSetName : dataSetNames) {
			newDataSetNamesList.add(existingDataSetName);
			if (existingDataSetName.equalsIgnoreCase(dataSetName))
				dataSetNameExists = true;
		}
		
		if (! dataSetNameExists)
			// The data set name doesn't exist yet
			newDataSetNamesList.add(dataSetName);
		
		String[] newDataSetNames = new String[newDataSetNamesList.size()];
		newDataSetNames = newDataSetNamesList.toArray(newDataSetNames);
		
		writeStringsFile(dataSetNamesFilePath(), newDataSetNames);
	}
	
	private static String classificationsFilePath(String dataSetName) {
		return DATA_SET_FOLDER + "/" + dataSetName + "_" + CLASSIFICATIONS_FILE_SUFIX;
	}
	
	private static void createDirectories(String filePath) {
		File file = new File(filePath);
		File parentFile = file.getParentFile();
		
		if (parentFile != null)
			parentFile.mkdirs();
	}
	
	private static String dataFilePath(String dataSetName) {
		return DATA_SET_FOLDER + "/" + dataSetName + "_" + DATA_FILE_SUFIX;
	}
	
	private static String dataSetNamesFilePath() {
		return DATA_SET_NAMES_FILE_PATH;
	}
	
	private static boolean fileExists(String filePath) {
		File file = new File(filePath);
		return file.exists();
	}
	
	private static Mat readFloatMatFile(String filePath) throws IOException {
		Path path = Paths.get(filePath);
		Scanner scanner = new Scanner(path);
		scanner.useLocale(Locale.US);
		
		int rows = scanner.nextInt();
		int columns = scanner.nextInt();
		int total = rows * columns;
		float[] floatMatData = new float[total];
		
		for (int i = 0; i < total; ++i)
			floatMatData[i] = scanner.nextFloat();
		
		Mat floatMat = new Mat(rows, columns, CvType.CV_32F);
		floatMat.put(0, 0, floatMatData);
		
		return floatMat;
	}
	
	private static String[] readStringsFile(String filePath) throws IOException {
		Path path = Paths.get(filePath);
		Scanner scanner = new Scanner(path);
		scanner.useLocale(Locale.US);
		
		int count = scanner.nextInt();
		String[] strings = new String[count];
		
		scanner.nextLine(); // Consumes the first line
		for (int i = 0; i < count; ++i)
			strings[i] = scanner.nextLine();
		
		return strings;
	}
	
	private static void writeFloatMatFile(String filePath, Mat floatMat) throws IOException {
		createDirectories(filePath);
		BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
		
		int rows = floatMat.rows();
		int columns = floatMat.cols();
		
		float[] floatMatData = new float[rows * columns];
		floatMat.get(0, 0, floatMatData);
		
		writer.write(rows + " " + columns);
		writer.newLine();
		
		for (int i = 0; i < rows; ++i) {
			String separator = "";
			for (int j = 0; j < columns; ++j) {
				writer.write(separator + String.format(Locale.US, "%." + Config.dataPrecision + "f", floatMatData[i * columns + j]));
				separator = " ";
			}
			
			writer.newLine();
		}
		
		// Cleanup
		writer.close();
	}
	
	private static void writeStringsFile(String filePath, String[] strings) throws IOException {
		createDirectories(filePath);
		BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
		
		int count = strings.length;
		
		writer.write(Integer.toString(count));
		writer.newLine();
		
		for (String string : strings) {
			writer.write(string);
			writer.newLine();
		}
		
		// Cleanup
		writer.close();
	}
	
}
