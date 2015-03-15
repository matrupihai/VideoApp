package com.odious.util;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class VideoFileManager {
	private static File mainDir, screenshotDir, videoFile;
	private static String filePath;
	
	public static void setParams(VideoParams params) {
		String settingsFilePath = params.getSettingsFilePath();
		String geoLocation = params.getGeoLocation();
		
		DateFormat date = new SimpleDateFormat("yyyy-MM-dd");
		DateFormat time = new SimpleDateFormat("hh_mm_ss");
		String timestamp = time.format(new Date());
		if (settingsFilePath.isEmpty()) {
			mainDir = new File(System.getProperty("user.dir"), geoLocation + " " + date.format(new Date()));
		} else {
			mainDir = new File(settingsFilePath, geoLocation + " " + date.format(new Date()));
		}
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
	
	public static void deleteScreenshotDir() {
		if (screenshotDir.exists() && screenshotDir.list().length == 0) {
			screenshotDir.delete();
		}
		System.out.println("SCREEENSHOT DELETED: " + screenshotDir.exists());
	}
	
	public static void deleteTempVideo() {
		if (videoFile != null && videoFile.length() < 1024) {
			videoFile.delete();
		}
		System.out.println("VIDEO DELETED: " + videoFile.getName());
	}
	
	public static File getVideoFile() {
		videoFile = new File(filePath);
		return videoFile;
	}
	
	public static File getScrenshotDir() {
		return screenshotDir;
	}
	
}
