package leavesanalyzer.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import leavesanalyzer.core.BatchProcessor;
import leavesanalyzer.core.Config;
import leavesanalyzer.core.Console;
import leavesanalyzer.core.DataSet;
import leavesanalyzer.core.IO;
import leavesanalyzer.core.SegmentationException;
import leavesanalyzer.core.SegmentationMethod;
import leavesanalyzer.core.Utility;
import org.opencv.core.Mat;

public class DataSetGeneratorFrame extends JFrame {
	
	private JButton classificationsFileButton;
	private JButton classificationsFileHelpButton;
	private JTextField classificationsFileTextField;
	private JTextArea consoleTextArea;
	private JSpinner dataPrecisionSpinner;
	private JTextField dataSetNameTextField;
	private JFileChooser fileChooser;
	private JButton generateDataSetButton;
	private JButton imagePathsFileButton;
	private JButton imagePathsFileHelpButton;
	private JTextField imagePathsFileTextField;
	private JPanel mainPanel;
	private JButton menuButton;
	private JComboBox segmentationMethodComboBox;
	
	/**
	 * Create the frame.
	 */
	public DataSetGeneratorFrame() {
		addWindowListener(new WindowAdapter() {
			
			public void windowClosed(WindowEvent arg0) {
				onClose();
			}
			
		});
		setMinimumSize(new Dimension(480, 448));
		setIconImage(Toolkit.getDefaultToolkit().getImage(DataSetGeneratorFrame.class.getResource("/resources/img/leaf.png")));
		setTitle("Generador de data set - Leaves Analyzer");
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 640, 480);
		mainPanel = new JPanel();
		mainPanel.setBorder(new EmptyBorder(16, 16, 0, 16));
		mainPanel.setLayout(new BorderLayout(0, 16));
		setContentPane(mainPanel);
		
		JPanel inputPanel = new JPanel();
		inputPanel.setBorder(new CompoundBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Par\u00E1metros", TitledBorder.LEADING, TitledBorder.TOP, null, null), new EmptyBorder(4, 4, 4, 4)));
		mainPanel.add(inputPanel, BorderLayout.NORTH);
		inputPanel.setLayout(new BorderLayout(0, 16));
		
		JPanel configPanel = new JPanel();
		inputPanel.add(configPanel, BorderLayout.NORTH);
		GridBagLayout gbl_configPanel = new GridBagLayout();
		gbl_configPanel.columnWidths = new int[] {
			0,
			0,
			0
		};
		gbl_configPanel.rowHeights = new int[] {
			0,
			0,
			0,
			0
		};
		gbl_configPanel.columnWeights = new double[] {
			1.0,
			1.0,
			Double.MIN_VALUE
		};
		gbl_configPanel.rowWeights = new double[] {
			1.0,
			1.0,
			1.0,
			Double.MIN_VALUE
		};
		configPanel.setLayout(gbl_configPanel);
		
		JLabel dataSetNameLabel = new JLabel("Nombre del data set:");
		dataSetNameLabel.setFont(new Font("Tahoma", Font.PLAIN, 13));
		dataSetNameLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		GridBagConstraints gbc_dataSetNameLabel = new GridBagConstraints();
		gbc_dataSetNameLabel.fill = GridBagConstraints.BOTH;
		gbc_dataSetNameLabel.insets = new Insets(0, 0, 5, 5);
		gbc_dataSetNameLabel.gridx = 0;
		gbc_dataSetNameLabel.gridy = 0;
		configPanel.add(dataSetNameLabel, gbc_dataSetNameLabel);
		
		dataSetNameTextField = new JTextField();
		dataSetNameTextField.setFont(new Font("Tahoma", Font.PLAIN, 13));
		GridBagConstraints gbc_dataSetNameTextField = new GridBagConstraints();
		gbc_dataSetNameTextField.insets = new Insets(0, 0, 5, 0);
		gbc_dataSetNameTextField.fill = GridBagConstraints.BOTH;
		gbc_dataSetNameTextField.gridx = 1;
		gbc_dataSetNameTextField.gridy = 0;
		configPanel.add(dataSetNameTextField, gbc_dataSetNameTextField);
		dataSetNameTextField.setColumns(10);
		dataSetNameTextField.getDocument().addDocumentListener(new DocumentListener() {
			
			public void changedUpdate(DocumentEvent arg0) {
				setEnabledGenerateDataSetButton();
			}
			
			public void insertUpdate(DocumentEvent arg0) {
				setEnabledGenerateDataSetButton();
			}
			
			public void removeUpdate(DocumentEvent arg0) {
				setEnabledGenerateDataSetButton();
			}
			
		});
		
		JLabel segmentationMethodLabel = new JLabel("M\u00E9todo de segmentaci\u00F3n:");
		segmentationMethodLabel.setFont(new Font("Tahoma", Font.PLAIN, 13));
		segmentationMethodLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		GridBagConstraints gbc_segmentationMethodLabel = new GridBagConstraints();
		gbc_segmentationMethodLabel.fill = GridBagConstraints.BOTH;
		gbc_segmentationMethodLabel.insets = new Insets(0, 0, 5, 5);
		gbc_segmentationMethodLabel.gridx = 0;
		gbc_segmentationMethodLabel.gridy = 1;
		configPanel.add(segmentationMethodLabel, gbc_segmentationMethodLabel);
		
		segmentationMethodComboBox = new JComboBox();
		segmentationMethodComboBox.setFont(new Font("Tahoma", Font.PLAIN, 13));
		segmentationMethodComboBox.setModel(new DefaultComboBoxModel(SegmentationMethod.values()));
		GridBagConstraints gbc_segmentationMethodComboBox = new GridBagConstraints();
		gbc_segmentationMethodComboBox.insets = new Insets(0, 0, 5, 0);
		gbc_segmentationMethodComboBox.fill = GridBagConstraints.BOTH;
		gbc_segmentationMethodComboBox.gridx = 1;
		gbc_segmentationMethodComboBox.gridy = 1;
		configPanel.add(segmentationMethodComboBox, gbc_segmentationMethodComboBox);
		
		JLabel dataPrecisionLabel = new JLabel("Precisi\u00F3n de los datos (cantidad de decimales):");
		dataPrecisionLabel.setFont(new Font("Tahoma", Font.PLAIN, 13));
		dataPrecisionLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		GridBagConstraints gbc_dataPrecisionLabel = new GridBagConstraints();
		gbc_dataPrecisionLabel.fill = GridBagConstraints.BOTH;
		gbc_dataPrecisionLabel.insets = new Insets(0, 0, 0, 5);
		gbc_dataPrecisionLabel.gridx = 0;
		gbc_dataPrecisionLabel.gridy = 2;
		configPanel.add(dataPrecisionLabel, gbc_dataPrecisionLabel);
		
		dataPrecisionSpinner = new JSpinner();
		dataPrecisionSpinner.setFont(new Font("Tahoma", Font.PLAIN, 13));
		dataPrecisionSpinner.setModel(new SpinnerNumberModel(8, 1, 10, 1));
		GridBagConstraints gbc_dataPrecisionSpinner = new GridBagConstraints();
		gbc_dataPrecisionSpinner.fill = GridBagConstraints.BOTH;
		gbc_dataPrecisionSpinner.gridx = 1;
		gbc_dataPrecisionSpinner.gridy = 2;
		configPanel.add(dataPrecisionSpinner, gbc_dataPrecisionSpinner);
		
		JPanel filePathsPanel = new JPanel();
		inputPanel.add(filePathsPanel, BorderLayout.CENTER);
		filePathsPanel.setLayout(new GridLayout(2, 0, 0, 16));
		
		JPanel imagePathsFilePanel = new JPanel();
		filePathsPanel.add(imagePathsFilePanel);
		GridBagLayout gbl_imagePathsFilePanel = new GridBagLayout();
		gbl_imagePathsFilePanel.columnWidths = new int[] {
			0,
			0,
			0
		};
		gbl_imagePathsFilePanel.rowHeights = new int[] {
			0,
			0,
			0
		};
		gbl_imagePathsFilePanel.columnWeights = new double[] {
			0.0,
			1.0,
			Double.MIN_VALUE
		};
		gbl_imagePathsFilePanel.rowWeights = new double[] {
			1.0,
			1.0,
			Double.MIN_VALUE
		};
		imagePathsFilePanel.setLayout(gbl_imagePathsFilePanel);
		
		imagePathsFileHelpButton = new JButton("");
		imagePathsFileHelpButton.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent arg0) {
				openDialog(new ImagePathsFileHelpDialog(DataSetGeneratorFrame.this));
			}
			
		});
		imagePathsFileHelpButton.setIcon(new ImageIcon(DataSetGeneratorFrame.class.getResource("/resources/img/help.png")));
		GridBagConstraints gbc_imagePathsFileHelpButton = new GridBagConstraints();
		gbc_imagePathsFileHelpButton.gridheight = 2;
		gbc_imagePathsFileHelpButton.insets = new Insets(0, 0, 0, 5);
		gbc_imagePathsFileHelpButton.fill = GridBagConstraints.BOTH;
		gbc_imagePathsFileHelpButton.gridx = 0;
		gbc_imagePathsFileHelpButton.gridy = 0;
		imagePathsFilePanel.add(imagePathsFileHelpButton, gbc_imagePathsFileHelpButton);
		
		JLabel imagePathsFileLabel = new JLabel("Archivo con las rutas de las im\u00E1genes:");
		imagePathsFileLabel.setFont(new Font("Tahoma", Font.PLAIN, 13));
		GridBagConstraints gbc_imagePathsFileLabel = new GridBagConstraints();
		gbc_imagePathsFileLabel.insets = new Insets(0, 0, 5, 0);
		gbc_imagePathsFileLabel.gridwidth = 2;
		gbc_imagePathsFileLabel.fill = GridBagConstraints.BOTH;
		gbc_imagePathsFileLabel.gridx = 1;
		gbc_imagePathsFileLabel.gridy = 0;
		imagePathsFilePanel.add(imagePathsFileLabel, gbc_imagePathsFileLabel);
		
		imagePathsFileTextField = new JTextField();
		imagePathsFileTextField.setFont(new Font("Tahoma", Font.PLAIN, 13));
		GridBagConstraints gbc_imagePathsFileTextField = new GridBagConstraints();
		gbc_imagePathsFileTextField.insets = new Insets(0, 0, 0, 5);
		gbc_imagePathsFileTextField.fill = GridBagConstraints.BOTH;
		gbc_imagePathsFileTextField.gridx = 1;
		gbc_imagePathsFileTextField.gridy = 1;
		imagePathsFilePanel.add(imagePathsFileTextField, gbc_imagePathsFileTextField);
		imagePathsFileTextField.setColumns(10);
		imagePathsFileTextField.getDocument().addDocumentListener(new DocumentListener() {
			
			public void changedUpdate(DocumentEvent arg0) {
				setEnabledGenerateDataSetButton();
			}
			
			public void insertUpdate(DocumentEvent arg0) {
				setEnabledGenerateDataSetButton();
			}
			
			public void removeUpdate(DocumentEvent arg0) {
				setEnabledGenerateDataSetButton();
			}
			
		});
		
		imagePathsFileButton = new JButton("Examinar...");
		imagePathsFileButton.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				chooseFile(imagePathsFileTextField);
			}
			
		});
		imagePathsFileButton.setFont(new Font("Tahoma", Font.PLAIN, 13));
		GridBagConstraints gbc_imagePathsFileButton = new GridBagConstraints();
		gbc_imagePathsFileButton.fill = GridBagConstraints.BOTH;
		gbc_imagePathsFileButton.gridx = 2;
		gbc_imagePathsFileButton.gridy = 1;
		imagePathsFilePanel.add(imagePathsFileButton, gbc_imagePathsFileButton);
		
		JPanel classificationsFilePanel = new JPanel();
		filePathsPanel.add(classificationsFilePanel);
		GridBagLayout gbl_classificationsFilePanel = new GridBagLayout();
		gbl_classificationsFilePanel.columnWidths = new int[] {
			0,
			0,
			0
		};
		gbl_classificationsFilePanel.rowHeights = new int[] {
			0,
			0,
			0
		};
		gbl_classificationsFilePanel.columnWeights = new double[] {
			0.0,
			1.0,
			Double.MIN_VALUE
		};
		gbl_classificationsFilePanel.rowWeights = new double[] {
			1.0,
			1.0,
			Double.MIN_VALUE
		};
		classificationsFilePanel.setLayout(gbl_classificationsFilePanel);
		
		classificationsFileHelpButton = new JButton("");
		classificationsFileHelpButton.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				openDialog(new ClassificationsFileHelpDialog(DataSetGeneratorFrame.this));
			}
			
		});
		classificationsFileHelpButton.setIcon(new ImageIcon(DataSetGeneratorFrame.class.getResource("/resources/img/help.png")));
		GridBagConstraints gbc_classificationsFileHelpButton = new GridBagConstraints();
		gbc_classificationsFileHelpButton.gridheight = 2;
		gbc_classificationsFileHelpButton.insets = new Insets(0, 0, 0, 5);
		gbc_classificationsFileHelpButton.fill = GridBagConstraints.BOTH;
		gbc_classificationsFileHelpButton.gridx = 0;
		gbc_classificationsFileHelpButton.gridy = 0;
		classificationsFilePanel.add(classificationsFileHelpButton, gbc_classificationsFileHelpButton);
		
		JLabel classificationsFileLabel = new JLabel("Archivo con las clasificaciones:");
		classificationsFileLabel.setFont(new Font("Tahoma", Font.PLAIN, 13));
		GridBagConstraints gbc_classificationsFileLabel = new GridBagConstraints();
		gbc_classificationsFileLabel.insets = new Insets(0, 0, 5, 0);
		gbc_classificationsFileLabel.gridwidth = 2;
		gbc_classificationsFileLabel.fill = GridBagConstraints.BOTH;
		gbc_classificationsFileLabel.gridx = 1;
		gbc_classificationsFileLabel.gridy = 0;
		classificationsFilePanel.add(classificationsFileLabel, gbc_classificationsFileLabel);
		
		classificationsFileTextField = new JTextField();
		classificationsFileTextField.setFont(new Font("Tahoma", Font.PLAIN, 13));
		GridBagConstraints gbc_classificationsFileTextField = new GridBagConstraints();
		gbc_classificationsFileTextField.insets = new Insets(0, 0, 0, 5);
		gbc_classificationsFileTextField.fill = GridBagConstraints.BOTH;
		gbc_classificationsFileTextField.gridx = 1;
		gbc_classificationsFileTextField.gridy = 1;
		classificationsFilePanel.add(classificationsFileTextField, gbc_classificationsFileTextField);
		classificationsFileTextField.setColumns(10);
		classificationsFileTextField.getDocument().addDocumentListener(new DocumentListener() {
			
			public void changedUpdate(DocumentEvent arg0) {
				setEnabledGenerateDataSetButton();
			}
			
			public void insertUpdate(DocumentEvent arg0) {
				setEnabledGenerateDataSetButton();
			}
			
			public void removeUpdate(DocumentEvent arg0) {
				setEnabledGenerateDataSetButton();
			}
			
		});
		
		classificationsFileButton = new JButton("Examinar...");
		classificationsFileButton.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				chooseFile(classificationsFileTextField);
			}
			
		});
		classificationsFileButton.setFont(new Font("Tahoma", Font.PLAIN, 13));
		GridBagConstraints gbc_classificationsFileButton = new GridBagConstraints();
		gbc_classificationsFileButton.fill = GridBagConstraints.BOTH;
		gbc_classificationsFileButton.gridx = 2;
		gbc_classificationsFileButton.gridy = 1;
		classificationsFilePanel.add(classificationsFileButton, gbc_classificationsFileButton);
		
		JPanel outputPanel = new JPanel();
		mainPanel.add(outputPanel, BorderLayout.CENTER);
		outputPanel.setLayout(new BorderLayout(0, 0));
		
		JScrollPane consoleScrollPane = new JScrollPane();
		outputPanel.add(consoleScrollPane, BorderLayout.CENTER);
		
		consoleTextArea = new JTextArea();
		consoleTextArea.setFont(new Font("Consolas", Font.PLAIN, 12));
		consoleTextArea.setEditable(false);
		consoleScrollPane.setViewportView(consoleTextArea);
		
		JPanel buttonsPanel = new JPanel();
		FlowLayout flowLayout = (FlowLayout) buttonsPanel.getLayout();
		flowLayout.setAlignment(FlowLayout.RIGHT);
		mainPanel.add(buttonsPanel, BorderLayout.SOUTH);
		
		menuButton = new JButton("Volver al men\u00FA principal");
		menuButton.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
			
		});
		menuButton.setFont(new Font("Tahoma", Font.PLAIN, 13));
		buttonsPanel.add(menuButton);
		
		generateDataSetButton = new JButton("Generar data set");
		generateDataSetButton.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent arg0) {
				generateDataSet();
			}
			
		});
		generateDataSetButton.setEnabled(false);
		generateDataSetButton.setFont(new Font("Tahoma", Font.PLAIN, 13));
		buttonsPanel.add(generateDataSetButton);
		
		setLocationRelativeTo(null);
		
		configure();
	}
	
	protected JButton getClassificationsFileButton() {
		return classificationsFileButton;
	}
	
	protected JButton getClassificationsFileHelpButton() {
		return classificationsFileHelpButton;
	}
	
	protected JTextField getClassificationsFileTextField() {
		return classificationsFileTextField;
	}
	
	protected JTextArea getConsoleTextArea() {
		return consoleTextArea;
	}
	
	protected JSpinner getDataPrecisionSpinner() {
		return dataPrecisionSpinner;
	}
	
	protected JTextField getDataSetNameTextField() {
		return dataSetNameTextField;
	}
	
	protected JButton getGenerateDataSetButton() {
		return generateDataSetButton;
	}
	
	protected JButton getImagePathsFileButton() {
		return imagePathsFileButton;
	}
	
	protected JButton getImagePathsFileHelpButton() {
		return imagePathsFileHelpButton;
	}
	
	protected JTextField getImagePathsFileTextField() {
		return imagePathsFileTextField;
	}
	
	protected JButton getMenuButton() {
		return menuButton;
	}
	
	protected JComboBox getSegmentationMethodComboBox() {
		return segmentationMethodComboBox;
	}
	
	private void chooseFile(JTextField textField) {
		int option = fileChooser.showOpenDialog(DataSetGeneratorFrame.this);
		
		if (option == JFileChooser.APPROVE_OPTION)
			textField.setText(fileChooser.getSelectedFile().toString());
	}
	
	private void configure() {
		// Redirects the standard output to the text area
		Console.redirectStandardOutput(consoleTextArea);
		
		fileChooser = new JFileChooser();
		FileNameExtensionFilter textFileFilter = new FileNameExtensionFilter("Documentos de texto (*.txt)", "txt");
		fileChooser.setFileFilter(textFileFilter);
	}
	
	private void generateDataSet() {
		generateDataSetButton.setEnabled(false);
		
		new SwingWorker<Void, Void>() {
			
			protected Void doInBackground() {
				Console.printProcessingMessage();
				
				// Gets the input
				String dataSetName = dataSetNameTextField.getText().trim();
				SegmentationMethod segmentationMethod = SegmentationMethod.values()[segmentationMethodComboBox.getSelectedIndex()];
				int dataPrecision = (int) dataPrecisionSpinner.getValue();
				String imagePathsFilePath = imagePathsFileTextField.getText().trim();
				String classificationsFilePath = classificationsFileTextField.getText().trim();
				
				if (! Utility.isValidDataSetName(dataSetName)) {
					// Invalid data set name
					printInvalidDataSetNameMessage();
					return null;
				}
				
				try {
					if (IO.dataSetExists(dataSetName)) {
						// The data set name has already been used
						String title = "¿Sobrescribir data set?";
						String message = "Ya existe un data set llamado " + dataSetName + ". ¿Desea sobrescribirlo?";
						if (! openConfirmationDialog(title, message))
							return null;
					}
					
					// Applies the configurations
					Config.segmentationMethod = segmentationMethod;
					Config.dataPrecision = dataPrecision;
					
					// Reads the input files
					String[] imagePaths = IO.readImagePathsFile(imagePathsFilePath);
					String[] classifications = IO.readClassificationsFile(classificationsFilePath);
					
					// Processes the images
					Mat data = BatchProcessor.processImages(imagePaths);
					DataSet dataSet = new DataSet(data, classifications);
					printDataSetGeneratedMessage();
					
					// Writes the data set
					IO.writeDataSet(dataSetName, dataSet);
					
					// Cleanup
					dataSet.release();
				} catch (IOException exception) {
					Console.printErrorMessage(exception, "No es posible acceder al archivo " + exception.getMessage() + ".");
				} catch (SegmentationException exception) {
					Console.printErrorMessage(exception, "No se halló ningún segmento en la imagen " + exception.getMessage() + ".");
				} catch (Exception exception) {
					Console.printErrorMessage(exception, "Se produjo un error inesperado.");
				}
				
				return null;
			}
			
			protected void done() {
				generateDataSetButton.setEnabled(true);
			}
			
		}.execute();
	}
	
	private void onClose() {
		MenuFrame frame = new MenuFrame();
		frame.setVisible(true);
	}
	
	private boolean openConfirmationDialog(String title, String message) {
		int option = JOptionPane.showConfirmDialog(DataSetGeneratorFrame.this, message, title, JOptionPane.YES_NO_OPTION);
		return option == JOptionPane.YES_OPTION;
	}
	
	private void openDialog(JDialog dialog) {
		dialog.setVisible(true);
	}
	
	private void printDataSetGeneratedMessage() {
		System.out.println();
		System.out.println("Data set generado.");
	}
	
	private void printInvalidDataSetNameMessage() {
		Console.clear();
		System.out.println("El nombre del data set es inválido." + System.lineSeparator() + "Sólo se permiten letras, números y guiones bajos.");
	}
	
	private void setEnabledGenerateDataSetButton() {
		if (dataSetNameTextField.getText().trim().isEmpty() || imagePathsFileTextField.getText().trim().isEmpty() || classificationsFileTextField.getText().trim().isEmpty())
			generateDataSetButton.setEnabled(false);
		else
			generateDataSetButton.setEnabled(true);
	}
	
}
