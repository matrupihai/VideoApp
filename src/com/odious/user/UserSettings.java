package com.odious.user;

import java.io.Serializable;

public class UserSettings implements Serializable {
	private static final long serialVersionUID = 7526472295622776147L;
	
	private int framerate, baudrate;
	private String  fontColor, fontPosition;
	private String filePath = null;
	private double fontSize;

	public UserSettings(String fontColor, int framerate, String filePath, int baudrate, double fontSize, String fontPosition){
		this.framerate = framerate;
		this.baudrate = baudrate;
		this.fontSize = fontSize;
		this.fontPosition = fontPosition;
		this.fontColor = fontColor;
		if(filePath != null){
			this.filePath = filePath;
		} else {
			this.filePath = null;
		}
	}
	
	public String getFontColor(){
		return fontColor;
	}
	
	public void setFontColor(String color){
		this.fontColor = color;
	}
	
	public double getFontSize(){
		return fontSize;
	}
	
	public void setFontSize(double fontSize){
		this.fontSize = fontSize;
	}
	
	public void setFontPosition(String fontPosition){
		this.fontPosition = fontPosition;
	}
	
	public String getFontPosition(){
		return fontPosition;
	}
	
	public String getFilePath(){
		return filePath;
	}
	
	public void setFilePath(String filePath){
		this.filePath = filePath;
	}
	
	public void setFramerate(int value){
		this.framerate = value;
	}
	
	public int getFramerate(){
		return framerate;
	}
	
	public int getBaudrate(){
		return baudrate;
	}
	
	public void setBaudrate(int value){
		this.baudrate = value;
	}
	
}
