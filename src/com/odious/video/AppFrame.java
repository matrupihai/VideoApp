package com.odious.video;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.odious.gui.CustomUI;
import com.odious.panel.AppMenuBar;
import com.odious.panel.MainPanel;
import com.odious.panel.SettingsDialog;
import com.odious.util.VideoParams;

public class AppFrame extends JFrame {
	
	public static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	public static int width = screenSize.width;
	public static int height = screenSize.height;
	
	private static Dimension videoDimension = new Dimension(2*(width/3), height);
	
	private VideoPanel video = VideoPanel.newInstance();
	private MainPanel panel;
	private SettingsDialog settingsDialog;
	
	public AppFrame() {
		initGui();
	}
	
	private void initGui() {
		CustomUI.setCustomUI();
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		settingsDialog = SettingsDialog.getInstance();
		initSettings();
		
		initVideo();
		initMenuBar();
		initMainPanel();
		
		setVisible(true);
	}
	
	private void initVideo() {
		video.setPreferredSize(videoDimension);
		add(video, BorderLayout.CENTER);
	}
	
	private void initMainPanel() {
		panel = new MainPanel();
		panel.setPreferredSize(new Dimension(width/3, height));
		add(panel, BorderLayout.EAST);
		panel.setVideo(video);
	}
	
	private void initMenuBar() {
		AppMenuBar bar = new AppMenuBar();
		bar.setAppFrame(this);
		setJMenuBar(bar.getMenuBar());
	}
	
	private void initSettings() {
		VideoParams settings = new VideoParams(settingsDialog);
		video.setParams(settings);
	}
	
	public static Dimension getVideoDimension() {
		return videoDimension;
	}
	
	public SettingsDialog getSettingsDialog() {
		return settingsDialog;
	}

	public void clearResourcesOnExit() {
		int exit = JOptionPane.showConfirmDialog(getRootPane(), "Exit?");
		if (exit == JOptionPane.YES_OPTION) {
			video.clearVideoResources();
			System.exit(0);
			dispose();
		} else {
			System.exit(0);
		}
	}
	
	@Override
	protected void processWindowEvent(WindowEvent e) {
		if (e.getID() == WindowEvent.WINDOW_CLOSING) {
			clearResourcesOnExit();
		}
	}
	
	public VideoPanel getVideo() {
		return video;
	}
	
}
