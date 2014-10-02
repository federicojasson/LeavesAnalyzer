package leavesanalyzer.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;
import javax.swing.JTextArea;

public class Console implements Runnable {
	
	private BufferedReader reader;
	private JTextArea textArea;
	
	private static PrintStream standardOutput = System.out;
	private static Console console;
	
	private Console(JTextArea textArea, PipedOutputStream pipedOutputStream) {
		this.textArea = textArea;
		
		try {
			PipedInputStream pipedInputStream = new PipedInputStream(pipedOutputStream);
			reader = new BufferedReader(new InputStreamReader(pipedInputStream));
		} catch (IOException exception) {
			// Resets the standard output
			resetStandardOutput();
		}
	}
	
	public void run() {
		try {
			String line = reader.readLine();
			while (line != null) {
				textArea.append(line + System.lineSeparator());
				textArea.setCaretPosition(textArea.getDocument().getLength());
				line = reader.readLine();
			}
		} catch (IOException exception) {
			// Resets the standard output
			resetStandardOutput();
		}
	}
	
	public static void clear() {
		if (console != null) {
			console.textArea.setText("");
			console.textArea.setCaretPosition(0);
		} else
			System.out.println();
	}
	
	public static void printErrorMessage(Exception exception, String message) {
		exception.printStackTrace();
		
		clear();
		System.out.println("ERROR:");
		System.out.println(message);
	}
	
	public static void printProcessingMessage() {
		clear();
		System.out.println("Procesando...");
		System.out.println();
	}
	
	public static void redirectStandardOutput(JTextArea textArea) {
		PipedOutputStream pipedOutputStream = new PipedOutputStream();
		System.setOut(new PrintStream(pipedOutputStream, true));
		
		console = new Console(textArea, pipedOutputStream);
		new Thread(console).start();
	}
	
	private static void resetStandardOutput() {
		System.setOut(standardOutput);
	}
	
}
