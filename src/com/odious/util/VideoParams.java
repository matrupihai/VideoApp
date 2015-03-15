package com.odious.util;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import com.odious.panel.SettingsDialog;


public class VideoParams implements PropertyChangeListener {
	private int framerate;
	private String geoLocation;
	private String settingsFilePath;
	private double fontSize;
	private String fontColor;
	private String fontPosition;
	private int baud;
	
	public VideoParams(SettingsDialog dialog) {
		this.settingsFilePath = dialog.getFilePath();
		this.fontSize = dialog.getFontSize();
		this.fontColor = dialog.getFontColor();
		this.fontPosition = dialog.getFontPosition();
		this.framerate = dialog.getFramerate();
		this.baud = dialog.getBaud();
		dialog.addChangeListener(this);
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		switch (evt.getPropertyName()) {
		case "FILEPATH":
			this.settingsFilePath = (String) evt.getNewValue();
			break;
		case "FONTSIZE":
			this.fontSize = (Double) evt.getNewValue();
			break;
		case "FONTCOLOR":
			this.fontColor = (String) evt.getNewValue();
			break;
		case "FONTPOSITION":
			this.fontPosition = (String) evt.getNewValue();
			break;
		case "FRAMERATE":
			this.framerate = (Integer) evt.getNewValue();
			break;
		case "BAUD":
			this.baud = (Integer) evt.getNewValue();
			break;
		}
	}
	
	public String getFontPosition() {
		return fontPosition;
	}

	public String getFontColor() {
		return fontColor;
	}

	public int getFramerate() {
		return framerate;
	}
	
	public String getSettingsFilePath() {
		return settingsFilePath;
	}
	
	public double getFontSize() {
		return fontSize;
	}

	public String getGeoLocation() {
		return geoLocation;
	}

	public int getBaud() {
		return baud;
	}
	
}	
