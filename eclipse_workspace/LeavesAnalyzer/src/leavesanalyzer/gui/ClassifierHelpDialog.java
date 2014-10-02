package leavesanalyzer.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EmptyBorder;

public class ClassifierHelpDialog extends JDialog {
	
	private JButton closeButton;
	private JTextPane messageTextPane;
	
	/**
	 * Create the dialog.
	 */
	public ClassifierHelpDialog(Component parent) {
		setModal(true);
		setTitle("Acerca del clasificador");
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setResizable(false);
		setIconImage(Toolkit.getDefaultToolkit().getImage(ClassificationsFileHelpDialog.class.getResource("/resources/img/help.png")));
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		{
			JPanel mainPanel = new JPanel();
			mainPanel.setBorder(new EmptyBorder(16, 16, 0, 16));
			getContentPane().add(mainPanel, BorderLayout.CENTER);
			mainPanel.setLayout(new BorderLayout(0, 0));
			{
				JPanel messagePanel = new JPanel();
				mainPanel.add(messagePanel, BorderLayout.CENTER);
				messagePanel.setLayout(new BorderLayout(0, 0));
				{
					JScrollPane messageScrollPane = new JScrollPane();
					messageScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
					messagePanel.add(messageScrollPane, BorderLayout.CENTER);
					{
						messageTextPane = new JTextPane();
						messageTextPane.setText("Clasificador\r\n-----------------------------------------------------------\r\n\r\nEl clasificador permite analizar la efectividad de un m\u00E9todo de clasificaci\u00F3n determinado.\r\n\r\nSe debe indicar un data set de entrenamiento y otro de prueba. Ambos deben haber sido creados previamente con el generador de data set.\r\n\r\nAutom\u00E1ticamente, el programa clasifica las im\u00E1genes correspondientes al data set de prueba a partir del conjunto de entrenamiento. Como se conocen las clasificaciones correctas (indicadas al generar el data set), es posible compararlas con las obtenidas por el clasificador y calcular la tasa de aciertos.");
						messageTextPane.setCaretPosition(0);
						messageTextPane.setFont(new Font("Tahoma", Font.PLAIN, 13));
						messageTextPane.setEditable(false);
						messageScrollPane.setViewportView(messageTextPane);
					}
				}
			}
			{
				JPanel buttonsPanel = new JPanel();
				FlowLayout flowLayout = (FlowLayout) buttonsPanel.getLayout();
				flowLayout.setAlignment(FlowLayout.RIGHT);
				mainPanel.add(buttonsPanel, BorderLayout.SOUTH);
				{
					closeButton = new JButton("Cerrar");
					closeButton.addActionListener(new ActionListener() {
						
						public void actionPerformed(ActionEvent arg0) {
							dispose();
						}
						
					});
					closeButton.setFont(new Font("Tahoma", Font.PLAIN, 13));
					buttonsPanel.add(closeButton);
				}
			}
		}
		
		setLocationRelativeTo(parent);
	}
	
	protected JButton getCloseButton() {
		return closeButton;
	}
	
	protected JTextPane getMessageTextPane() {
		return messageTextPane;
	}
	
}
