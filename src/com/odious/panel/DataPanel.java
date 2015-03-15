package com.odious.panel;

import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;

public class DataPanel extends JPanel {
	
	public DataPanel() {
		setBorder(BorderFactory.createLineBorder(MainPanel.BASE_COLOR, 1));
		setLayout(new MigLayout());
		
		JLabel labelDiameter = new JLabel("Diametru foraj");
		JLabel labelType = new JLabel("Tip coloana foraj");
		JTextField fieldDiameter = new JTextField(4);
		String[] diameter = { "mm", "toli" };
		JComboBox<String> comboDiameter = new JComboBox<String>(diameter);
		comboDiameter.setSelectedIndex(0);
		comboDiameter.setMinimumSize(new Dimension(62, 20));
		comboDiameter.setEditable(false);
		JRadioButton radio1 = new JRadioButton("metalica");
		JRadioButton radio2 = new JRadioButton("PVC");
		JRadioButton radio3 = new JRadioButton("inox");
		ButtonGroup groupType = new ButtonGroup();
		groupType.add(radio1);
		groupType.add(radio2);
		groupType.add(radio3);

		add(labelDiameter, "wrap");
		add(fieldDiameter);
		add(comboDiameter, "wrap");
		add(labelType, "wrap");
		add(radio1, "wrap");
		add(radio2, "wrap");
		add(radio3, "wrap");
	}
	
}
