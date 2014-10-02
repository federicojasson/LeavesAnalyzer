package leavesanalyzer;

import java.awt.EventQueue;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import leavesanalyzer.gui.MenuFrame;
import org.opencv.core.Core;

public class Main {
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			
			public void run() {
				try {
					configure();
					MenuFrame frame = new MenuFrame();
					frame.setVisible(true);
				} catch (Error | Exception e) {
					e.printStackTrace();
					
					// Abnormal termination
					System.exit(1);
				}
			}
			
		});
	}
	
	private static void configure() throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsatisfiedLinkError, UnsupportedLookAndFeelException {
		// Sets the LookAndFeel
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		
		try {
			// Loads the OpenCV library
			System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		} catch (UnsatisfiedLinkError error) {
			// Shows an error dialog
			JOptionPane.showMessageDialog(null, "La librería OpenCV no fue encontrada. Si ya ha sido instalada," + System.lineSeparator() + "puede que esté mal configurada o que no sea una versión compatible.", "No se encuentra OpenCV", JOptionPane.ERROR_MESSAGE);
			
			throw error;
		}
	}
	
}
