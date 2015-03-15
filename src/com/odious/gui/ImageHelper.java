package com.odious.gui;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class ImageHelper {

	public ImageIcon loadImageIcon(String fileName) throws IOException {
		BufferedImage image = ImageIO.read(this.getClass().getClassLoader()
				.getResourceAsStream("com/odious/images/" + fileName));

		return new ImageIcon(image);
	}

}
