package com.odious.gui;

import java.awt.Color;
import java.awt.Insets;
import java.io.IOException;

import javax.swing.JButton;

// refactor this
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
	
	private void setIcons(String fileName, String rolloverFileName, String pressedFileName) {
		if (fileName == null || rolloverFileName == null || pressedFileName == null) {
			throw new IllegalArgumentException("A file name cannot be null");
		}
		
		ImageHelper imageHelper = new ImageHelper();
		try {
			setIcon(imageHelper.loadImage(fileName));
			setRolloverIcon(imageHelper.loadImage(rolloverFileName));
			setPressedIcon(imageHelper.loadImage(pressedFileName));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
