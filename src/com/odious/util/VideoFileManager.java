package com.odious.util;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class VideoFileManager {
	private File mainDir, screenshotDir, videoFile;
	private String filePath;
	
	public VideoFileManager(String settingsFilePath, String geoLocation) {
		DateFormat date = new SimpleDateFormat("yyyy-MM-dd");
		DateFormat time = new SimpleDateFormat("hh_mm_ss");
		if (settingsFilePath.isEmpty()) {
			mainDir = new File(System.getProperty("user.dir"), geoLocation + " " + date.format(new Date()));
		} else {
			mainDir = new File(settingsFilePath, geoLocation + " " + date.format(new Date()));
		}
		String timestamp = time.format(new Date());
		if (!mainDir.exists()) {
			if (mainDir.mkdir()) {
				filePath = mainDir.getAbsolutePath() + "\\" + geoLocation +  " " + 
						timestamp + ".wmv";
				screenshotDir = new File(mainDir.getAbsolutePath(), "foto " + geoLocation + " " + timestamp);
				screenshotDir.mkdir();
			}
		} else {
			filePath = mainDir.getAbsolutePath() + "\\" + geoLocation +  " " + 
					timestamp + ".wmv";
			screenshotDir = new File(mainDir.getAbsolutePath(), "foto " + geoLocation + " " + timestamp);
			screenshotDir.mkdir();
		}	
	}
	
	public void deleteScreenshotDir() {
		System.out.println("SCREEENSHOT: " + screenshotDir.exists());
		if (screenshotDir.exists() && screenshotDir.list().length == 0) {
			screenshotDir.delete();
		}
	}
	
	public void deleteTempVideo() {
		System.out.println("VIDEO: " + videoFile);
		if (videoFile != null && videoFile.length() < 1024) {
			System.out.println("Temp file deleted: " + videoFile.delete());
		}
	}
	
	public File getVideoFile() {
		videoFile = new File(filePath);
		return videoFile;
	}
	
	public File getScrenshotDir() {
		return screenshotDir;
	}
	
}
