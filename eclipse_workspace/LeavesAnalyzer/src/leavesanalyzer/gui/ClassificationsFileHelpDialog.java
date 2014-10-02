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

public class ClassificationsFileHelpDialog extends JDialog {
	
	private JButton closeButton;
	private JTextPane messageTextPane;
	
	/**
	 * Create the dialog.
	 */
	public ClassificationsFileHelpDialog(Component parent) {
		setModal(true);
		setTitle("Acerca del archivo de clasificaciones");
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
						messageTextPane.setText("Archivo de clasificaciones (requerimientos)\r\n-----------------------------------------------------------\r\n\r\nDebe ser un archivo de texto.\r\n\r\nLa primera l\u00EDnea debe contener un n\u00FAmero entero N correspondiente a la cantidad de im\u00E1genes que deben ser clasificadas. El n\u00FAmero N debe coincidir con la cantidad de rutas en el archivo de im\u00E1genes.\r\n\r\nLas siguientes N l\u00EDneas indican los nombres de las especies. La i-\u00E9sima l\u00EDnea debe contener el nombre de la especie correspondiente a la i-\u00E9sima imagen.\r\n\r\nEjemplo:\r\n\r\n5\r\nespecie 1\r\nespecie 1\r\nespecie 2\r\nespecie 2\r\nespecie 3\r\n\r\nLas primeras dos im\u00E1genes corresponden a \"especie 1\". La tercera y cuarta ser\u00E1n clasificadas como \"especie 2\". Por \u00FAltima, la quinta imagen pertenece a \"especie 3\".");
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
