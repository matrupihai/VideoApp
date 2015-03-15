package com.odious.video;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import com.googlecode.javacv.cpp.videoInputLib.videoInput;

public class VideoHelper {
	public static final String NO_SOURCE = "NO_SOURCE";
	
	private static List<String> devices = new ArrayList<String>();

	public static void detectVideoDevices() {
		devices.clear();
		int count = videoInput.listDevices();
		if (count > 0) {
			for (int i = 0; i < count; i++) {
				StringBuilder deviceName = new StringBuilder(i).append(" ")
										.append(videoInput.getDeviceName(i));
				devices.add(deviceName.toString());
			}
		} else {
			devices.add(0, NO_SOURCE);
		}

		System.out.println("# of video devices: " + count);
	}
	
	public static List<String> getDevices() {
		return devices;
	}
	
	public static BufferedImage resize(BufferedImage image, int width, int height) {
		int currentWidth = image.getWidth();
		int currentHeight = image.getHeight();
		double ratio = (double) currentWidth/currentHeight;
		
	    BufferedImage bi = new BufferedImage(width, height, BufferedImage.TRANSLUCENT);
	    Graphics2D g2d = (Graphics2D) bi.createGraphics();
	    g2d.addRenderingHints(new RenderingHints(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY));
	    g2d.drawImage(image, 0, 0, width, (int) (width/ratio), null);
	    g2d.dispose();
	    return bi;
	}
	
}
