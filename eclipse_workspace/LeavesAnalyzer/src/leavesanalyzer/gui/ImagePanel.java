package leavesanalyzer.gui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

public class ImagePanel extends JPanel {
	
	private BufferedImage image;
	
	public Dimension getPreferredSize() {
		if (image == null)
			return super.getPreferredSize();
		
		// The preferred size is set according to image dimensions
		return new Dimension(image.getWidth(), image.getHeight());
	}
	
	public void paintComponent(Graphics graphics) {
		super.paintComponent(graphics);
		graphics.drawImage(image, 0, 0, null);
	}
	
	public void setImage(BufferedImage image) {
		this.image = image;
		revalidate();
		repaint();
	}
	
}
