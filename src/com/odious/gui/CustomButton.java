package com.odious.gui;

import java.awt.Color;
import java.awt.Insets;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;

// refactor this, builder pattern
public class CustomButton extends JButton {
	
	public CustomButton(String fileName, String rolloverFileName, String pressedFileName) {
		setForeground(new Color(0, 0, 0));
		setContentAreaFilled(false);
		setRolloverEnabled(true);
		setBorderPainted(true);
		setMargin(new Insets(0, 0, 0, 0));
		setFocusPainted(false);
		
		setIcons(fileName, rolloverFileName, pressedFileName);
	}
	
//	private ImageIcon loadImageIcon(String fileName) throws IOException {
//		BufferedImage image = ImageIO.read(this.getClass().getClassLoader()
//							.getResourceAsStream("com/odious/images/" + fileName));
//
//		return new ImageIcon(image);
//	}
	
	private void setIcons(String fileName, String rolloverFileName, String pressedFileName) {
		if (fileName == null || rolloverFileName == null || pressedFileName == null) {
			throw new IllegalArgumentException("A file name cannot be null");
		}
		
		ImageHelper imageHelper = new ImageHelper();
		try {
			setIcon(imageHelper.loadImageIcon(fileName));
			setRolloverIcon(imageHelper.loadImageIcon(rolloverFileName));
			setPressedIcon(imageHelper.loadImageIcon(pressedFileName));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
