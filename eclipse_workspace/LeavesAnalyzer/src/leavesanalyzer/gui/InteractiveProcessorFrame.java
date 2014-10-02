package leavesanalyzer.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import leavesanalyzer.core.ClassificationMethod;
import leavesanalyzer.core.Config;
import leavesanalyzer.core.Console;
import leavesanalyzer.core.DataSet;
import leavesanalyzer.core.IO;
import leavesanalyzer.core.InteractiveProcessor;
import leavesanalyzer.core.SegmentationMethod;
import leavesanalyzer.core.Utility;
import org.opencv.core.Mat;

public class InteractiveProcessorFrame extends JFrame {
	
	private InteractiveProcessor interactiveProcessor;
	
	private JComboBox classificationMethodComboBox;
	private JButton clearButton;
	private JTextArea consoleTextArea;
	private JComboBox dataSetComboBox;
	private JFileChooser fileChooser;
	private JButton imageFileButton;
	private JTextField imageFileTextField;
	private ImagePanel imagePanel;
	private JButton loadImageButton;
	private JPanel mainPanel;
	private JButton menuButton;
	private JComboBox segmentationMethodComboBox;
	private JButton stepButton;
	private JList stepsList;
	private DefaultListModel stepsListModel;
	private JSpinner maxImageSizeSpinner;
	
	/**
	 * Create the frame.
	 */
	public InteractiveProcessorFrame() {
		setMinimumSize(new Dimension(600, 456));
		addWindowListener(new WindowAdapter() {
			
			public void windowClosed(WindowEvent e) {
				onClose();
			}
			
		});
		setTitle("Procesador interactivo - Leaves Analyzer");
		setIconImage(Toolkit.getDefaultToolkit().getImage(InteractiveProcessorFrame.class.getResource("/resources/img/leaf.png")));
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
			0.0,
			0.0,
			0.0,
			1.0,
			0.0,
			Double.MIN_VALUE
		};
		inputPanel.setLayout(gbl_inputPanel);
		
		JLabel segmentationMethodLabel = new JLabel("M\u00E9todo de segmentaci\u00F3n:");
		segmentationMethodLabel.setFont(new Font("Tahoma", Font.PLAIN, 13));
		segmentationMethodLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		GridBagConstraints gbc_segmentationMethodLabel = new GridBagConstraints();
		gbc_segmentationMethodLabel.insets = new Insets(0, 0, 5, 5);
		gbc_segmentationMethodLabel.fill = GridBagConstraints.BOTH;
		gbc_segmentationMethodLabel.gridx = 0;
		gbc_segmentationMethodLabel.gridy = 0;
		inputPanel.add(segmentationMethodLabel, gbc_segmentationMethodLabel);
		
		segmentationMethodComboBox = new JComboBox();
		segmentationMethodComboBox.setModel(new DefaultComboBoxModel(SegmentationMethod.values()));
		segmentationMethodComboBox.setFont(new Font("Tahoma", Font.PLAIN, 13));
		GridBagConstraints gbc_segmentationMethodComboBox = new GridBagConstraints();
		gbc_segmentationMethodComboBox.insets = new Insets(0, 0, 5, 0);
		gbc_segmentationMethodComboBox.fill = GridBagConstraints.BOTH;
		gbc_segmentationMethodComboBox.gridx = 1;
		gbc_segmentationMethodComboBox.gridy = 0;
		inputPanel.add(segmentationMethodComboBox, gbc_segmentationMethodComboBox);
		
		JLabel classificationMethodLabel = new JLabel("M\u00E9todo de clasificaci\u00F3n:");
		classificationMethodLabel.setFont(new Font("Tahoma", Font.PLAIN, 13));
		classificationMethodLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		GridBagConstraints gbc_classificationMethodLabel = new GridBagConstraints();
		gbc_classificationMethodLabel.fill = GridBagConstraints.BOTH;
		gbc_classificationMethodLabel.insets = new Insets(0, 0, 5, 5);
		gbc_classificationMethodLabel.gridx = 0;
		gbc_classificationMethodLabel.gridy = 1;
		inputPanel.add(classificationMethodLabel, gbc_classificationMethodLabel);
		
		classificationMethodComboBox = new JComboBox();
		classificationMethodComboBox.setModel(new DefaultComboBoxModel(ClassificationMethod.values()));
		classificationMethodComboBox.setFont(new Font("Tahoma", Font.PLAIN, 13));
		GridBagConstraints gbc_classificationMethodComboBox = new GridBagConstraints();
		gbc_classificationMethodComboBox.insets = new Insets(0, 0, 5, 0);
		gbc_classificationMethodComboBox.fill = GridBagConstraints.BOTH;
		gbc_classificationMethodComboBox.gridx = 1;
		gbc_classificationMethodComboBox.gridy = 1;
		inputPanel.add(classificationMethodComboBox, gbc_classificationMethodComboBox);
		
		JLabel dataSetLabel = new JLabel("Data set:");
		dataSetLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		dataSetLabel.setFont(new Font("Tahoma", Font.PLAIN, 13));
		GridBagConstraints gbc_dataSetLabel = new GridBagConstraints();
		gbc_dataSetLabel.fill = GridBagConstraints.BOTH;
		gbc_dataSetLabel.insets = new Insets(0, 0, 5, 5);
		gbc_dataSetLabel.gridx = 0;
		gbc_dataSetLabel.gridy = 2;
		inputPanel.add(dataSetLabel, gbc_dataSetLabel);
		
		dataSetComboBox = new JComboBox();
		dataSetComboBox.setEnabled(false);
		dataSetComboBox.setFont(new Font("Tahoma", Font.PLAIN, 13));
		GridBagConstraints gbc_dataSetComboBox = new GridBagConstraints();
		gbc_dataSetComboBox.insets = new Insets(0, 0, 5, 0);
		gbc_dataSetComboBox.fill = GridBagConstraints.BOTH;
		gbc_dataSetComboBox.gridx = 1;
		gbc_dataSetComboBox.gridy = 2;
		inputPanel.add(dataSetComboBox, gbc_dataSetComboBox);
		
		JLabel maxImageSizeLabel = new JLabel("M\u00E1xima dimensi\u00F3n de la imagen (pixels):");
		maxImageSizeLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		maxImageSizeLabel.setFont(new Font("Tahoma", Font.PLAIN, 13));
		GridBagConstraints gbc_maxImageSizeLabel = new GridBagConstraints();
		gbc_maxImageSizeLabel.fill = GridBagConstraints.BOTH;
		gbc_maxImageSizeLabel.insets = new Insets(0, 0, 5, 5);
		gbc_maxImageSizeLabel.gridx = 0;
		gbc_maxImageSizeLabel.gridy = 3;
		inputPanel.add(maxImageSizeLabel, gbc_maxImageSizeLabel);
		
		maxImageSizeSpinner = new JSpinner();
		maxImageSizeSpinner.setModel(new SpinnerNumberModel(800, 1, 2147483647, 10));
		maxImageSizeSpinner.setFont(new Font("Tahoma", Font.PLAIN, 13));
		GridBagConstraints gbc_maxImageSizeSpinner = new GridBagConstraints();
		gbc_maxImageSizeSpinner.fill = GridBagConstraints.BOTH;
		gbc_maxImageSizeSpinner.insets = new Insets(0, 0, 5, 0);
		gbc_maxImageSizeSpinner.gridx = 1;
		gbc_maxImageSizeSpinner.gridy = 3;
		inputPanel.add(maxImageSizeSpinner, gbc_maxImageSizeSpinner);
		
		JPanel loadImagePanel = new JPanel();
		GridBagConstraints gbc_loadImagePanel = new GridBagConstraints();
		gbc_loadImagePanel.fill = GridBagConstraints.BOTH;
		gbc_loadImagePanel.gridx = 1;
		gbc_loadImagePanel.gridy = 4;
		inputPanel.add(loadImagePanel, gbc_loadImagePanel);
		loadImagePanel.setLayout(new BorderLayout(5, 0));
		
		JPanel imageFilePanel = new JPanel();
		loadImagePanel.add(imageFilePanel, BorderLayout.CENTER);
		imageFilePanel.setLayout(new BorderLayout(5, 0));
		
		imageFileTextField = new JTextField();
		imageFileTextField.setFont(new Font("Tahoma", Font.PLAIN, 13));
		imageFilePanel.add(imageFileTextField, BorderLayout.CENTER);
		imageFileTextField.setColumns(10);
		
		imageFileButton = new JButton("Examinar...");
		imageFileButton.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				chooseFile(imageFileTextField);
			}
			
		});
		imageFileButton.setFont(new Font("Tahoma", Font.PLAIN, 13));
		imageFilePanel.add(imageFileButton, BorderLayout.EAST);
		
		loadImageButton = new JButton("Cargar imagen");
		loadImageButton.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				loadImage();
			}
			
		});
		loadImageButton.setEnabled(false);
		loadImageButton.setFont(new Font("Tahoma", Font.PLAIN, 13));
		loadImagePanel.add(loadImageButton, BorderLayout.EAST);
		
		JLabel loadImageLabel = new JLabel("Imagen:");
		loadImageLabel.setFont(new Font("Tahoma", Font.PLAIN, 13));
		loadImageLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		GridBagConstraints gbc_loadImageLabel = new GridBagConstraints();
		gbc_loadImageLabel.insets = new Insets(0, 0, 0, 5);
		gbc_loadImageLabel.fill = GridBagConstraints.BOTH;
		gbc_loadImageLabel.gridx = 0;
		gbc_loadImageLabel.gridy = 4;
		inputPanel.add(loadImageLabel, gbc_loadImageLabel);
		
		JPanel outputPanel = new JPanel();
		mainPanel.add(outputPanel, BorderLayout.CENTER);
		outputPanel.setLayout(new BorderLayout(16, 0));
		
		JScrollPane imageScrollPane = new JScrollPane();
		outputPanel.add(imageScrollPane, BorderLayout.CENTER);
		
		imagePanel = new ImagePanel();
		imagePanel.addMouseListener(new MouseAdapter() {
			
			public void mouseClicked(MouseEvent arg0) {
				showSegmentInformation(arg0.getX(), arg0.getY());
			}
			
		});
		imagePanel.setBackground(new Color(0, 0, 0));
		imageScrollPane.setViewportView(imagePanel);
		imagePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		JPanel processingPanel = new JPanel();
		outputPanel.add(processingPanel, BorderLayout.EAST);
		processingPanel.setLayout(new BorderLayout(0, 16));
		
		JScrollPane consoleScrollPane = new JScrollPane();
		processingPanel.add(consoleScrollPane, BorderLayout.CENTER);
		
		consoleTextArea = new JTextArea();
		consoleTextArea.setColumns(40);
		consoleTextArea.setFont(new Font("Consolas", Font.PLAIN, 12));
		consoleTextArea.setEditable(false);
		consoleScrollPane.setViewportView(consoleTextArea);
		
		JPanel stepsPanel = new JPanel();
		processingPanel.add(stepsPanel, BorderLayout.SOUTH);
		stepsPanel.setLayout(new BorderLayout(0, 16));
		
		stepsListModel = new DefaultListModel();
		stepsList = new JList(stepsListModel);
		stepsList.addListSelectionListener(new ListSelectionListener() {
			
			public void valueChanged(ListSelectionEvent arg0) {
				stepsListSelection();
			}
			
		});
		stepsList.setPreferredSize(new Dimension(0, 100));
		stepsList.setBorder(new CompoundBorder(new LineBorder(new Color(128, 128, 128)), new EmptyBorder(4, 4, 4, 4)));
		stepsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		stepsList.setEnabled(false);
		stepsList.setFont(new Font("Tahoma", Font.PLAIN, 13));
		stepsPanel.add(stepsList, BorderLayout.CENTER);
		
		stepButton = new JButton("Procesar");
		stepButton.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				step();
			}
			
		});
		stepButton.setEnabled(false);
		stepButton.setFont(new Font("Tahoma", Font.PLAIN, 13));
		stepsPanel.add(stepButton, BorderLayout.SOUTH);
		
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
		
		clearButton = new JButton("Reiniciar procesador");
		clearButton.setEnabled(false);
		clearButton.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent arg0) {
				clear();
			}
			
		});
		clearButton.setFont(new Font("Tahoma", Font.PLAIN, 13));
		buttonsPanel.add(clearButton);
		
		setLocationRelativeTo(null);
		
		configure();
	}
	
	protected JComboBox getClassificationMethodComboBox() {
		return classificationMethodComboBox;
	}
	
	protected JButton getClearButton() {
		return clearButton;
	}
	
	protected JTextArea getConsoleTextArea() {
		return consoleTextArea;
	}
	
	protected JComboBox getDataSetComboBox() {
		return dataSetComboBox;
	}
	
	protected ImagePanel getImagePanel() {
		return imagePanel;
	}
	
	protected JButton getImagePathButton() {
		return imageFileButton;
	}
	
	protected JTextField getImagePathTextField() {
		return imageFileTextField;
	}
	
	protected JButton getLoadImageButton() {
		return loadImageButton;
	}
	
	protected JSpinner getMaxImageSizeSpinner() {
		return maxImageSizeSpinner;
	}
	
	protected JButton getMenuButton() {
		return menuButton;
	}
	
	protected JComboBox getSegmentationMethodComboBox() {
		return segmentationMethodComboBox;
	}
	
	protected JButton getStepButton() {
		return stepButton;
	}
	
	protected JList getStepsList() {
		return stepsList;
	}
	
	private void chooseFile(JTextField textField) {
		int option = fileChooser.showOpenDialog(InteractiveProcessorFrame.this);
		
		if (option == JFileChooser.APPROVE_OPTION)
			textField.setText(fileChooser.getSelectedFile().toString());
	}
	
	private void clear() {
		// Cleanup
		if (interactiveProcessor != null)
			interactiveProcessor.release();
		
		segmentationMethodComboBox.setEnabled(true);
		classificationMethodComboBox.setEnabled(true);
		dataSetComboBox.setEnabled(true);
		maxImageSizeSpinner.setEnabled(true);
		imageFileTextField.setEnabled(true);
		imageFileButton.setEnabled(true);
		loadImageButton.setEnabled(true);
		imagePanel.setImage(null);
		((DefaultListModel) stepsList.getModel()).removeAllElements();
		stepButton.setEnabled(false);
		clearButton.setEnabled(false);
	}
	
	private void configure() {
		// Redirects the standard output to the text area
		Console.redirectStandardOutput(consoleTextArea);
		
		fileChooser = new JFileChooser();
		FileNameExtensionFilter imageFilter = new FileNameExtensionFilter("Archivos de imagen", "bmp", "dib", "jpeg", "jpg", "jpe", "jp2", "png", "pbm", "pgm", "ppm", "sr", "ras", "tiff", "tif");
		fileChooser.setFileFilter(imageFilter);
		
		try {
			// Reads the data set names file
			String[] dataSetNames = IO.readDataSetNamesFile();
			
			if (dataSetNames.length > 0) {
				dataSetComboBox.setEnabled(true);
				dataSetComboBox.setModel(new DefaultComboBoxModel(dataSetNames));
				
				imageFileTextField.getDocument().addDocumentListener(new DocumentListener() {
					
					public void changedUpdate(DocumentEvent arg0) {
						setEnabledLoadImageButton();
					}
					
					public void insertUpdate(DocumentEvent arg0) {
						setEnabledLoadImageButton();
					}
					
					public void removeUpdate(DocumentEvent arg0) {
						setEnabledLoadImageButton();
					}
					
				});
			}
		} catch (IOException exception) {
			Console.printErrorMessage(exception, "No es posible acceder al archivo " + exception.getMessage() + ".");
		} catch (Exception exception) {
			Console.printErrorMessage(exception, "Se produjo un error inesperado.");
		}
	}
	
	private void loadImage() {
		segmentationMethodComboBox.setEnabled(false);
		classificationMethodComboBox.setEnabled(false);
		dataSetComboBox.setEnabled(false);
		maxImageSizeSpinner.setEnabled(false);
		imageFileTextField.setEnabled(false);
		imageFileButton.setEnabled(false);
		loadImageButton.setEnabled(false);
		
		new SwingWorker<Boolean, Void>() {
			
			protected Boolean doInBackground() {
				Console.printProcessingMessage();
				
				// Gets the input
				SegmentationMethod segmentationMethod = SegmentationMethod.values()[segmentationMethodComboBox.getSelectedIndex()];
				ClassificationMethod classificationMethod = ClassificationMethod.values()[classificationMethodComboBox.getSelectedIndex()];
				String dataSetName = (String) dataSetComboBox.getSelectedItem();
				int maxImageSize = (int) maxImageSizeSpinner.getValue();
				String imagePath = imageFileTextField.getText().trim();
				
				// Applies the configurations
				Config.maxImageSize = maxImageSize;
				Config.segmentationMethod = segmentationMethod;
				Config.classificationMethod = classificationMethod;
				
				try {
					// Reads the input files
					DataSet dataSet = IO.readDataSet(dataSetName);
					
					// Initializes the processor and updates the components
					interactiveProcessor = new InteractiveProcessor(imagePath, dataSet);
					stepUpdate();
					Console.clear();
					
					return true;
				} catch (IOException exception) {
					Console.printErrorMessage(exception, "No es posible acceder al archivo " + exception.getMessage() + ".");
				} catch (Exception exception) {
					Console.printErrorMessage(exception, "Se produjo un error inesperado.");
				}
				
				return false;
			}
			
			protected void done() {
				try {
					stepsList.setEnabled(true);
					stepButton.setEnabled(true);
					clearButton.setEnabled(true);
					
					if (! get())
						clear();
				} catch (Exception exception) {
					Console.printErrorMessage(exception, "Se produjo un error inesperado.");
				}
			}
			
		}.execute();
	}
	
	private void onClose() {
		// Cleanup
		if (interactiveProcessor != null)
			interactiveProcessor.release();
		
		MenuFrame frame = new MenuFrame();
		frame.setVisible(true);
	}
	
	private void printSegmentInformation(String segmentInformation) {
		Console.clear();
		
		if (! segmentInformation.isEmpty()) {
			System.out.println("Información del segmento");
			System.out.println("--------------------------------");
			System.out.println();
			System.out.println(segmentInformation);
		}
	}
	
	private void setEnabledLoadImageButton() {
		if (imageFileTextField.getText().trim().isEmpty())
			loadImageButton.setEnabled(false);
		else
			loadImageButton.setEnabled(true);
	}
	
	private void showImage(Mat image) {
		BufferedImage bufferedImage = Utility.bufferedImage(image);
		imagePanel.setImage(bufferedImage);
	}
	
	private void showSegmentInformation(final int x, final int y) {
		new SwingWorker<Void, Void>() {
			
			protected Void doInBackground() {
				String segmentInformation = interactiveProcessor.getSegmentInformation(x, y);
				printSegmentInformation(segmentInformation);
				return null;
			}
			
		}.execute();
	}
	
	private void step() {
		stepButton.setEnabled(false);
		
		new SwingWorker<Void, Void>() {
			
			protected Void doInBackground() {
				Console.printProcessingMessage();
				interactiveProcessor.step();
				Console.clear();
				return null;
			}
			
			protected void done() {
				stepButton.setEnabled(true);
				
				stepUpdate();
			}
			
		}.execute();
	}
	
	private void stepsListSelection() {
		int selectedIndex = stepsList.getSelectedIndex();
		if (selectedIndex >= 0)
			showImage(interactiveProcessor.getResizedImage(selectedIndex));
	}
	
	private void stepUpdate() {
		int currentStep = interactiveProcessor.getCurrentStep();
		
		showImage(interactiveProcessor.getResizedImage(currentStep));
		stepsListModel.addElement(interactiveProcessor.getStepName(currentStep));
		stepsList.setSelectedIndex(currentStep);
		
		if (interactiveProcessor.isFinished())
			stepButton.setEnabled(false);
	}
	
}
