package com.odious.gui;

import java.awt.Color;

import javax.swing.UIManager;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;

public class CustomUI extends UIManager {
	
	public static void setCustomUI() {
		NimbusLookAndFeel laf = new NimbusLookAndFeel();
		laf.getDefaults().put("nimbusBase", new Color(140, 140, 140));
		laf.getDefaults().put("Panel.background", new Color(255, 255, 255));
		laf.getDefaults().put("OptionPane.background", new Color(255, 255, 255));
		laf.getDefaults().put("FileChooser.background", new Color(255, 255, 255));
		laf.getDefaults().put("nimbusBorder", new Color(220, 220, 220));
		try {
			UIManager.setLookAndFeel(laf);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
