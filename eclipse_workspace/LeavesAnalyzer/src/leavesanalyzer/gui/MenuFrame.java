package leavesanalyzer.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import leavesanalyzer.core.Utility;

public class MenuFrame extends JFrame {
	
	private ImagePanel bannerPanel;
	private JButton classifierButton;
	private JButton classifierHelpButton;
	private JButton dataSetGeneratorButton;
	private JButton dataSetGeneratorHelpButton;
	private JButton interactiveProcessorButton;
	private JButton interactiveProcessorHelpButton;
	private JPanel mainPanel;
	
	/**
	 * Create the frame.
	 */
	public MenuFrame() {
		setResizable(false);
		setIconImage(Toolkit.getDefaultToolkit().getImage(MenuFrame.class.getResource("/resources/img/leaf.png")));
		setTitle("Leaves Analyzer");
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 512, 480);
		mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout(0, 16));
		setContentPane(mainPanel);
		
		JPanel buttonsPanel = new JPanel();
		buttonsPanel.setBorder(new EmptyBorder(16, 16, 0, 16));
		mainPanel.add(buttonsPanel, BorderLayout.CENTER);
		buttonsPanel.setLayout(new GridLayout(3, 0, 0, 16));
		
		JPanel dataSetGeneratorPanel = new JPanel();
		buttonsPanel.add(dataSetGeneratorPanel);
		dataSetGeneratorPanel.setLayout(new BorderLayout(16, 0));
		
		dataSetGeneratorHelpButton = new JButton("");
		dataSetGeneratorHelpButton.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent arg0) {
				openDialog(new DataSetGeneratorHelpDialog(MenuFrame.this));
			}
			
		});
		dataSetGeneratorHelpButton.setIcon(new ImageIcon(MenuFrame.class.getResource("/resources/img/help.png")));
		dataSetGeneratorPanel.add(dataSetGeneratorHelpButton, BorderLayout.WEST);
		
		dataSetGeneratorButton = new JButton("Generador de data set");
		dataSetGeneratorButton.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				openFrame(new DataSetGeneratorFrame());
			}
			
		});
		dataSetGeneratorButton.setFont(new Font("Tahoma", Font.PLAIN, 16));
		dataSetGeneratorPanel.add(dataSetGeneratorButton, BorderLayout.CENTER);
		
		JPanel classifierPanel = new JPanel();
		buttonsPanel.add(classifierPanel);
		classifierPanel.setLayout(new BorderLayout(16, 0));
		
		classifierHelpButton = new JButton("");
		classifierHelpButton.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				openDialog(new ClassifierHelpDialog(MenuFrame.this));
			}
			
		});
		classifierHelpButton.setIcon(new ImageIcon(MenuFrame.class.getResource("/resources/img/help.png")));
		classifierPanel.add(classifierHelpButton, BorderLayout.WEST);
		
		classifierButton = new JButton("Clasificador");
		classifierButton.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				openFrame(new ClassifierFrame());
			}
			
		});
		classifierButton.setFont(new Font("Tahoma", Font.PLAIN, 16));
		classifierPanel.add(classifierButton, BorderLayout.CENTER);
		
		JPanel interactiveProcessorPanel = new JPanel();
		buttonsPanel.add(interactiveProcessorPanel);
		interactiveProcessorPanel.setLayout(new BorderLayout(16, 0));
		
		interactiveProcessorHelpButton = new JButton("");
		interactiveProcessorHelpButton.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				openDialog(new InteractiveProcessorHelpDialog(MenuFrame.this));
			}
			
		});
		interactiveProcessorHelpButton.setIcon(new ImageIcon(MenuFrame.class.getResource("/resources/img/help.png")));
		interactiveProcessorPanel.add(interactiveProcessorHelpButton, BorderLayout.WEST);
		
		interactiveProcessorButton = new JButton("Procesador interactivo");
		interactiveProcessorButton.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				openFrame(new InteractiveProcessorFrame());
			}
			
		});
		interactiveProcessorButton.setFont(new Font("Tahoma", Font.PLAIN, 16));
		interactiveProcessorPanel.add(interactiveProcessorButton, BorderLayout.CENTER);
		
		bannerPanel = new ImagePanel();
		bannerPanel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		bannerPanel.addMouseListener(new MouseAdapter() {
			
			public void mouseClicked(MouseEvent arg0) {
				openVyGLabWebsite();
			}
			
		});
		bannerPanel.setBorder(new MatteBorder(1, 0, 0, 0, Color.GRAY));
		mainPanel.add(bannerPanel, BorderLayout.SOUTH);
		
		setLocationRelativeTo(null);
		
		configure();
	}
	
	protected ImagePanel getBannerPanel() {
		return bannerPanel;
	}
	
	protected JButton getClassifierButton() {
		return classifierButton;
	}
	
	protected JButton getClassifierHelpButton() {
		return classifierHelpButton;
	}
	
	protected JButton getDataSetGeneratorButton() {
		return dataSetGeneratorButton;
	}
	
	protected JButton getDataSetGeneratorHelpButton() {
		return dataSetGeneratorHelpButton;
	}
	
	protected JButton getInteractiveProcessorButton() {
		return interactiveProcessorButton;
	}
	
	protected JButton getInteractiveProcessorHelpButton() {
		return interactiveProcessorHelpButton;
	}
	
	private void configure() {
		try {
			bannerPanel.setImage(ImageIO.read(MenuFrame.class.getResource("/resources/img/banner.jpg")));
		} catch (IOException exception) {}
	}
	
	private void openDialog(JDialog dialog) {
		dialog.setVisible(true);
	}
	
	private void openFrame(JFrame frame) {
		dispose();
		frame.setVisible(true);
	}
	
	private void openVyGLabWebsite() {
		Utility.openWebsite("http://vyglab.cs.uns.edu.ar/");
	}
	
}
