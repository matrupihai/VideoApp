package com.odious.main;

import javax.swing.SwingUtilities;

import com.odious.video.AppFrame;

public class VideoApp {
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				new AppFrame();
			}
		});

	}
}
