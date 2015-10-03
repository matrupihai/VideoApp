package com.odious.util;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public enum FileManager {
	INSTANCE;
	
	private File screenshotDir, videoFile;
	private String mainDirPath;
	private DateFormat dateFormat = new SimpleDateFormat("hh_mm_ss");
	private VideoParams params;
	
	private FileManager() {
		
	}
	
	public void setParams(VideoParams params) {
		this.params = params;
		File mainDir = createMainDir(params);
		if (mainDir == null) {
			throw new RuntimeException("");
		}
		
		mainDirPath = mainDir.getAbsolutePath();
	}
	
	private File createMainDir(VideoParams params) {
		String settingsFilePath = params.getSettingsFilePath();
		String geoLocation = params.getGeoLocation();
		String parentDirName = null;
		String mainDirName = geoLocation + " " + dateFormat.format(new Date());
		
		if (settingsFilePath != null && settingsFilePath.isEmpty()) {
			parentDirName = System.getProperty("user.dir");
		} else {
			parentDirName = settingsFilePath;
		}
		
		if (parentDirName == null) {
			return null;
		}
		
		return createDir(parentDirName, mainDirName);
	}
	
	public File createDir(String parentDirName, String mainDirName) {
		String currentMainDir = mainDirName;
		File mainDir = new File(parentDirName, currentMainDir);
		
		int version = 1;
		while (mainDir.exists()) {
			currentMainDir = mainDirName + " (" + version + ")";
			version++;
			mainDir = new File(parentDirName, currentMainDir);
		}
		
		mainDir.mkdir();
		
		return mainDir;
	}
	
	public void deleteScreenshotDir() {
		if (screenshotDir.exists() && screenshotDir.list().length == 0) {
			screenshotDir.delete();
		}
	}
	
	public void deleteTempVideo() {
		if (videoFile != null && videoFile.length() < 1024) {
			videoFile.delete();
		}
	}
	
	public File getVideoFile() {
		String geoLocation = params.getGeoLocation();
		String timestamp = dateFormat.format(new Date());
		String videoFilePath = mainDirPath + "\\" + geoLocation +  " " + timestamp + ".wmv";
		videoFile = new File(videoFilePath);
		
		screenshotDir = new File(mainDirPath, "foto " + geoLocation + " " + timestamp);
		screenshotDir.mkdir();
		
		System.out.println(String.format("video: %s screens: %s", videoFilePath, screenshotDir.getAbsolutePath()));
		
		return videoFile;
	}
	
	public File getScrenshotDir() {
		return screenshotDir;
	}
	
}
