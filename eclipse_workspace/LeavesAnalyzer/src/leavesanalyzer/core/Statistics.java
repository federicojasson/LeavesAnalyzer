package leavesanalyzer.core;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

public class Statistics {
	
	private static final int HITS = 0;
	private static final int MISSES = 1;
	
	private Map<String, Integer[]> results;
	private int totalHits;
	private int totalMisses;
	
	private Statistics(Map<String, Integer[]> results, int totalHits, int totalMisses) {
		this.results = results;
		this.totalHits = totalHits;
		this.totalMisses = totalMisses;
	}
	
	public String toString() {
		String string = new String();
		
		for (Entry<String, Integer[]> entry : results.entrySet()) {
			String classification = entry.getKey();
			int hits = entry.getValue()[Statistics.HITS];
			int misses = entry.getValue()[Statistics.MISSES];
			
			string += String.format("%20s", classification) + "  ---  ";
			string += "Aciertos: " + String.format("%3d", hits) + "  ---  ";
			string += "Errores: " + String.format("%3d", misses) + "  ---  ";
			string += "Tasa de aciertos: " + String.format("%6.2f", 100 * hits / (float) (misses + hits)) + "%" + System.lineSeparator();
		}
		
		string += System.lineSeparator();
		string += " -------------------------------------------------------------------------------------------" + System.lineSeparator();
		string += System.lineSeparator();
		
		string += String.format("%20s", "Total") + "  ---  ";
		string += "Aciertos: " + String.format("%3d", totalHits) + "  ---  ";
		string += "Errores: " + String.format("%3d", totalMisses) + "  ---  ";
		string += "Tasa de aciertos: " + String.format("%6.2f", 100 * totalHits / (float) (totalMisses + totalHits)) + "%" + System.lineSeparator();
		
		return string;
	}
	
	public static Statistics calculateStatistics(String[] expectedClassifications, String[] classifications) {
		Map<String, Integer[]> results = new LinkedHashMap<String, Integer[]>();
		
		for (int i = 0; i < expectedClassifications.length; ++i) {
			String expectedClassification = expectedClassifications[i];
			if (! results.containsKey(expectedClassification)) {
				Integer[] values = new Integer[2];
				values[HITS] = 0;
				values[MISSES] = 0;
				results.put(expectedClassification, values);
			}
			
			if (expectedClassification.equals(classifications[i]))
				results.get(expectedClassification)[HITS]++;
			else
				results.get(expectedClassification)[MISSES]++;
		}
		
		int totalHits = 0;
		int totalMisses = 0;
		for (Integer[] values : results.values()) {
			totalHits += values[HITS];
			totalMisses += values[MISSES];
		}
		
		return new Statistics(results, totalHits, totalMisses);
	}
	
}
