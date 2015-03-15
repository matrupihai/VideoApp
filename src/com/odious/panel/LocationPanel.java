package com.odious.panel;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;

public class LocationPanel extends JPanel {
	
	private JTextField fieldTown;

	public LocationPanel() {
		setBorder(BorderFactory.createLineBorder(MainPanel.BASE_COLOR, 1));
		setLayout(new MigLayout());
		
		JLabel labelCoord = new JLabel("Coordonate");
		JLabel labelLatitude = new JLabel("Lat.");
		JLabel labelLongitude = new JLabel("Long.");
		JLabel labelTown = new JLabel("Localitate");
		JLabel labelCounty = new JLabel("Judet");
		JLabel labelBeneficiary = new JLabel("Beneficiar");
		JLabel labelDate = new JLabel("Data");
		
		JRadioButton radioManual = new JRadioButton("Manual");
		JRadioButton radioAuto = new JRadioButton("Auto");
		radioAuto.setSelected(true);
		ButtonGroup radioGroup = new ButtonGroup();
		radioGroup.add(radioManual);
		radioGroup.add(radioAuto);

		JTextField fieldLatitude = new JTextField(13);
		JTextField fieldLongitude = new JTextField(13);
		fieldTown = new JTextField(13);
		JTextField fieldCounty = new JTextField(13);
		JTextField fieldBeneficiary = new JTextField(13);

		add(labelCoord, "wrap");
		add(labelLatitude, "wrap");
		add(fieldLatitude, "wrap");
		add(labelLongitude, "wrap");
		add(fieldLongitude, "wrap");
		add(labelTown, "wrap");
		add(fieldTown, "wrap");
		add(labelCounty, "wrap");
		add(fieldCounty, "wrap");
		add(labelBeneficiary, "wrap");
		add(fieldBeneficiary, "wrap");
		add(labelDate, "wrap");
		add(radioManual, "wrap");
		add(radioAuto);
	}
	
	public String getGeoLocation() {
		return fieldTown.getText();
	}
	
}
