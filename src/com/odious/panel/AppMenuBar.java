package com.odious.panel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import com.odious.video.AppFrame;

public class AppMenuBar {
	private JMenuBar menuBar;
	private AppFrame appFrame;

	public AppMenuBar() {
		menuBar = new JMenuBar();
		menuBar.setBorderPainted(false);

		JMenu fileMenu = new JMenu("File");
		fileMenu.setMnemonic(KeyEvent.VK_F);

		JMenuItem fileExitMenuItem = new JMenuItem("Exit", KeyEvent.VK_F4);
		fileExitMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				appFrame.clearResourcesOnExit();
			}
		});

		JMenu editMenu = new JMenu("Edit");
		editMenu.setMnemonic(KeyEvent.VK_E);

		JMenuItem settingsMenuItem = new JMenuItem("Preferences");
		settingsMenuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				SettingsDialog dialog = appFrame.getSettingsDialog();
				dialog.setLocationRelativeTo(null);
				dialog.setVisible(true);
			}
		});

		fileMenu.add(fileExitMenuItem);
		editMenu.add(settingsMenuItem);
		menuBar.add(fileMenu);
		menuBar.add(editMenu);
	}
	
	public JMenuBar getMenuBar() {
		return menuBar;
	}
	
	public void setAppFrame(AppFrame appFrame) {
		this.appFrame = appFrame;
	}
	
}
