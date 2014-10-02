package leavesanalyzer.gui;

import java.awt.BorderLayout;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import leavesanalyzer.core.ClassificationMethod;
import leavesanalyzer.core.Classifier;
import leavesanalyzer.core.Config;
import leavesanalyzer.core.Console;
import leavesanalyzer.core.DataSet;
import leavesanalyzer.core.IO;
import leavesanalyzer.core.Statistics;

public class ClassifierFrame extends JFrame {
	
	private JComboBox classificationMethodComboBox;
	private JButton classifyButton;
	private JTextArea consoleTextArea;
	private JPanel mainPanel;
	private JButton menuButton;
	private JComboBox testDataSetComboBox;
	private JComboBox trainingDataSetComboBox;
	
	/**
	 * Create the frame.
	 */
	public ClassifierFrame() {
		addWindowListener(new WindowAdapter() {
			
			public void windowClosed(WindowEvent arg0) {
				onClose();
			}
			
		});
		setMinimumSize(new Dimension(384, 384));
		setTitle("Clasificador - Leaves Analyzer");
		setIconImage(Toolkit.getDefaultToolkit().getImage(ClassifierFrame.class.getResource("/resources/img/leaf.png")));
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 720, 512);
		mainPanel = new JPanel();
		mainPanel.setBorder(new EmptyBorder(16, 16, 0, 16));
		mainPanel.setLayout(new BorderLayout(0, 16));
		setContentPane(mainPanel);
		
		JPanel inputPanel = new JPanel();
		inputPanel.setBorder(new CompoundBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Par\u00E1metros", TitledBorder.LEADING, TitledBorder.TOP, null, null), new EmptyBorder(4, 4, 4, 4)));
		mainPanel.add(inputPanel, BorderLayout.NORTH);
		GridBagLayout gbl_inputPanel = new GridBagLayout();
		gbl_inputPanel.columnWidths = new int[] {
			0,
			0,
			0
		};
		gbl_inputPanel.rowHeights = new int[] {
			0,
			0,
			0,
			0
		};
		gbl_inputPanel.columnWeights = new double[] {
			1.0,
			1.0,
			Double.MIN_VALUE
		};
		gbl_inputPanel.rowWeights = new double[] {
			1.0,
			1.0,
			1.0,
			Double.MIN_VALUE
		};
		inputPanel.setLayout(gbl_inputPanel);
		
		JLabel classificationMethodLabel = new JLabel("M\u00E9todo de clasificaci\u00F3n:");
		classificationMethodLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		classificationMethodLabel.setFont(new Font("Tahoma", Font.PLAIN, 13));
		GridBagConstraints gbc_classificationMethodLabel = new GridBagConstraints();
		gbc_classificationMethodLabel.insets = new Insets(0, 0, 5, 5);
		gbc_classificationMethodLabel.fill = GridBagConstraints.BOTH;
		gbc_classificationMethodLabel.gridx = 0;
		gbc_classificationMethodLabel.gridy = 0;
		inputPanel.add(classificationMethodLabel, gbc_classificationMethodLabel);
		
		classificationMethodComboBox = new JComboBox();
		classificationMethodComboBox.setFont(new Font("Tahoma", Font.PLAIN, 13));
		classificationMethodComboBox.setModel(new DefaultComboBoxModel(ClassificationMethod.values()));
		GridBagConstraints gbc_classificationMethodComboBox = new GridBagConstraints();
		gbc_classificationMethodComboBox.insets = new Insets(0, 0, 5, 0);
		gbc_classificationMethodComboBox.fill = GridBagConstraints.BOTH;
		gbc_classificationMethodComboBox.gridx = 1;
		gbc_classificationMethodComboBox.gridy = 0;
		inputPanel.add(classificationMethodComboBox, gbc_classificationMethodComboBox);
		
		JLabel trainingDataSetLabel = new JLabel("Data set de entrenamiento:");
		trainingDataSetLabel.setFont(new Font("Tahoma", Font.PLAIN, 13));
		trainingDataSetLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		GridBagConstraints gbc_trainingDataSetLabel = new GridBagConstraints();
		gbc_trainingDataSetLabel.anchor = GridBagConstraints.EAST;
		gbc_trainingDataSetLabel.fill = GridBagConstraints.VERTICAL;
		gbc_trainingDataSetLabel.insets = new Insets(0, 0, 5, 5);
		gbc_trainingDataSetLabel.gridx = 0;
		gbc_trainingDataSetLabel.gridy = 1;
		inputPanel.add(trainingDataSetLabel, gbc_trainingDataSetLabel);
		
		trainingDataSetComboBox = new JComboBox();
		trainingDataSetComboBox.setEnabled(false);
		trainingDataSetComboBox.setFont(new Font("Tahoma", Font.PLAIN, 13));
		GridBagConstraints gbc_trainingDataSetComboBox = new GridBagConstraints();
		gbc_trainingDataSetComboBox.insets = new Insets(0, 0, 5, 0);
		gbc_trainingDataSetComboBox.fill = GridBagConstraints.BOTH;
		gbc_trainingDataSetComboBox.gridx = 1;
		gbc_trainingDataSetComboBox.gridy = 1;
		inputPanel.add(trainingDataSetComboBox, gbc_trainingDataSetComboBox);
		
		JLabel testDataSetLabel = new JLabel("Data set de prueba:");
		testDataSetLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		testDataSetLabel.setFont(new Font("Tahoma", Font.PLAIN, 13));
		GridBagConstraints gbc_testDataSetLabel = new GridBagConstraints();
		gbc_testDataSetLabel.anchor = GridBagConstraints.EAST;
		gbc_testDataSetLabel.fill = GridBagConstraints.VERTICAL;
		gbc_testDataSetLabel.insets = new Insets(0, 0, 0, 5);
		gbc_testDataSetLabel.gridx = 0;
		gbc_testDataSetLabel.gridy = 2;
		inputPanel.add(testDataSetLabel, gbc_testDataSetLabel);
		
		testDataSetComboBox = new JComboBox();
		testDataSetComboBox.setEnabled(false);
		testDataSetComboBox.setFont(new Font("Tahoma", Font.PLAIN, 13));
		GridBagConstraints gbc_testDataSetComboBox = new GridBagConstraints();
		gbc_testDataSetComboBox.fill = GridBagConstraints.BOTH;
		gbc_testDataSetComboBox.gridx = 1;
		gbc_testDataSetComboBox.gridy = 2;
		inputPanel.add(testDataSetComboBox, gbc_testDataSetComboBox);
		
		JPanel outputPanel = new JPanel();
		mainPanel.add(outputPanel, BorderLayout.CENTER);
		outputPanel.setLayout(new BorderLayout(0, 0));
		
		JScrollPane consoleScrollPane = new JScrollPane();
		consoleScrollPane.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		outputPanel.add(consoleScrollPane);
		
		consoleTextArea = new JTextArea();
		consoleTextArea.setEditable(false);
		consoleTextArea.setFont(new Font("Consolas", Font.PLAIN, 12));
		consoleScrollPane.setViewportView(consoleTextArea);
		
		JPanel buttonsPanel = new JPanel();
		FlowLayout flowLayout = (FlowLayout) buttonsPanel.getLayout();
		flowLayout.setAlignment(FlowLayout.RIGHT);
		mainPanel.add(buttonsPanel, BorderLayout.SOUTH);
		
		menuButton = new JButton("Volver al men\u00FA principal");
		menuButton.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
			
		});
		menuButton.setFont(new Font("Tahoma", Font.PLAIN, 13));
		buttonsPanel.add(menuButton);
		
		classifyButton = new JButton("Clasificar");
		classifyButton.setEnabled(false);
		classifyButton.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				classify();
			}
			
		});
		classifyButton.setFont(new Font("Tahoma", Font.PLAIN, 13));
		buttonsPanel.add(classifyButton);
		
		setLocationRelativeTo(null);
		
		configure();
	}
	
	protected JComboBox getClassificationMethodComboBox() {
		return classificationMethodComboBox;
	}
	
	protected JButton getClassifyButton() {
		return classifyButton;
	}
	
	protected JTextArea getConsoleTextArea() {
		return consoleTextArea;
	}
	
	protected JButton getMenuButton() {
		return menuButton;
	}
	
	protected JComboBox getTestDataSetComboBox() {
		return testDataSetComboBox;
	}
	
	protected JComboBox getTrainingDataSetComboBox() {
		return trainingDataSetComboBox;
	}
	
	private void classify() {
		classifyButton.setEnabled(false);
		
		new SwingWorker<Void, Void>() {
			
			protected Void doInBackground() {
				Console.printProcessingMessage();
				
				// Gets the input
				ClassificationMethod classificationMethod = ClassificationMethod.values()[classificationMethodComboBox.getSelectedIndex()];
				String trainingDataSetName = (String) trainingDataSetComboBox.getSelectedItem();
				String testDataSetName = (String) testDataSetComboBox.getSelectedItem();
				
				// Applies the configurations
				Config.classificationMethod = classificationMethod;
				
				try {
					// Reads the input files
					DataSet trainingDataSet = IO.readDataSet(trainingDataSetName);
					DataSet testDataSet = IO.readDataSet(testDataSetName);
					
					// Classifies the test data and calculates statistics
					String[] classifications = Classifier.classify(trainingDataSet, testDataSet.getData());
					Statistics statistics = Statistics.calculateStatistics(testDataSet.getClassifications(), classifications);
					printStatistics(statistics);
					
					// Cleanup
					trainingDataSet.release();
					testDataSet.release();
				} catch (IOException exception) {
					Console.printErrorMessage(exception, "No es posible acceder al archivo " + exception.getMessage() + ".");
				} catch (Exception exception) {
					Console.printErrorMessage(exception, "Se produjo un error inesperado.");
				}
				
				return null;
			}
			
			protected void done() {
				classifyButton.setEnabled(true);
			}
			
		}.execute();
	}
	
	private void configure() {
		// Redirects the standard output to the text area
		Console.redirectStandardOutput(consoleTextArea);
		
		try {
			// Reads the data set names file
			String[] dataSetNames = IO.readDataSetNamesFile();
			
			if (dataSetNames.length > 0) {
				trainingDataSetComboBox.setEnabled(true);
				trainingDataSetComboBox.setModel(new DefaultComboBoxModel(dataSetNames));
				
				testDataSetComboBox.setEnabled(true);
				testDataSetComboBox.setModel(new DefaultComboBoxModel(dataSetNames));
				
				classifyButton.setEnabled(true);
			}
		} catch (IOException exception) {
			Console.printErrorMessage(exception, "No es posible acceder al archivo " + exception.getMessage() + ".");
		} catch (Exception exception) {
			Console.printErrorMessage(exception, "Se produjo un error inesperado.");
		}
	}
	
	private void onClose() {
		MenuFrame frame = new MenuFrame();
		frame.setVisible(true);
	}
	
	private void printStatistics(Statistics statistics) {
		Console.clear();
		System.out.println("Estadísticas");
		System.out.println("--------------------------------");
		System.out.println();
		System.out.println(statistics);
	}
	
}
