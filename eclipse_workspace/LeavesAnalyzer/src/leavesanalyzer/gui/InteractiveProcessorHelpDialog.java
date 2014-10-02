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

public class InteractiveProcessorHelpDialog extends JDialog {
	
	private JButton closeButton;
	private JTextPane messageTextPane;
	
	/**
	 * Create the dialog.
	 */
	public InteractiveProcessorHelpDialog(Component parent) {
		setModal(true);
		setTitle("Acerca del procesador interactivo");
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
						messageTextPane.setEditable(false);
						messageTextPane.setText("Procesador interactivo\r\n-----------------------------------------------------------\r\n\r\nEl procesador interactivo permite cargar una imagen y ver paso a paso las etapas del procesamiento.\r\n\r\nUna vez segmentada la planta, es posible obtener informaci\u00F3n referente a cada segmento haciendo click en el mismo.\r\n\r\nSe debe indicar un data set previamente generado para utilizar en la clasificaci\u00F3n.");
						messageTextPane.setCaretPosition(0);
						messageTextPane.setFont(new Font("Tahoma", Font.PLAIN, 13));
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
