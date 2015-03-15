package com.odious.panel;

import gnu.io.CommPortIdentifier;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

import com.odious.gui.CustomButton;
import com.odious.gui.ImageHelper;
import com.odious.modbus.ModbusReader;
import com.odious.util.VideoParams;

public class SerialPanel extends JPanel {
	
	private static ImageIcon sensorOn, sensorOff, upActive, upInactive, downActive, downInactive, restActive, restInactive;
	private ModbusReader reader;
	private Timer timerModbus;
	private JLabel modbusLabel, restLabel, upLabel, downLabel;
	private VideoParams params;
	
	static {
		ImageHelper imgHelper = new ImageHelper();
		
		try {
			upActive = imgHelper.loadImageIcon("upActive.png");
			upInactive = imgHelper.loadImageIcon("upInactive.png");
			downActive = imgHelper.loadImageIcon("downActive.png");
			downInactive = imgHelper.loadImageIcon("downInactive.png");
			restActive = imgHelper.loadImageIcon("restActive.png");
			restInactive = imgHelper.loadImageIcon("restInactive.png");
			sensorOff = imgHelper.loadImageIcon("sensorOff.png");
			sensorOn = imgHelper.loadImageIcon("sensorOn.png");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private JComboBox<String> portCombo;
	private JLabel sensor;
	
	public SerialPanel() {
		JPanel containerPanel = new JPanel(new MigLayout());
		containerPanel.setBorder(BorderFactory.createLineBorder(MainPanel.BASE_COLOR));
		portCombo = new JComboBox<String>(getPortList());
		Dimension d = new Dimension(68, 30);
		portCombo.setMaximumSize(d);
		portCombo.setMinimumSize(d);
		sensor = new JLabel();
		sensor.setIcon(sensorOff);
		CustomButton serialStart = new CustomButton("play.png", "hoverPlay.png", "pressedPlay.png");
		serialStart.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				startSerial();
			}
		});
		CustomButton serialStop = new CustomButton("stop.png", "hoverStop.png", "pressedStop.png");
		serialStop.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				stopSerial();
			}
		});
		
		JPanel comboPanel = new JPanel(new MigLayout());
		comboPanel.add(serialStart, "west");
		comboPanel.add(serialStop, "west");
		comboPanel.add(portCombo, "west, gapright 10");
		comboPanel.add(sensor,"west, gapright 15, gapleft 14");
		
		containerPanel.add(comboPanel, "wrap");
		containerPanel.add(loadFeedPanel(), "wrap");
		
		add(containerPanel);
	}
	
	private JPanel loadFeedPanel() {
		JPanel panel = new JPanel(new MigLayout());
		JPanel container = new JPanel(new MigLayout());
		upLabel = new JLabel(upInactive);
		restLabel = new JLabel(restInactive);
		downLabel = new JLabel(downInactive);
		modbusLabel = new JLabel(ModbusReader.getValue() + "");
		modbusLabel.setFont(new Font("Arial", Font.BOLD, 35));
		modbusLabel.setForeground(MainPanel.BASE_COLOR);

		container.add(upLabel, "wrap");
		container.add(restLabel);
		container.add(modbusLabel, "wrap, gapleft 30");
		container.add(downLabel);
		panel.add(container, "gapright 10");
		return panel;
	}
	
	public Vector<String> getPortList() {
		Enumeration<CommPortIdentifier> portList;
		Vector<String> portVector = new Vector<String>();
		portList = CommPortIdentifier.getPortIdentifiers();

		CommPortIdentifier portId;
		while (portList.hasMoreElements()) {
			portId = portList.nextElement();
			if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
				portVector.add(portId.getName());
			}
		}

		return portVector;
	}
	
	private void startSerial() {
		if (reader == null && timerModbus == null) {
			timerModbus = new Timer();
			reader = new ModbusReader((String) portCombo.getSelectedItem(),
					params.getBaud(), 8, 1);
			reader.sendRequest();
			if (reader.getResponse() != null) {
				sendScheduledRequest(reader);
				sensor.setIcon(sensorOn);
			} else {
				System.out.println("Cannot connect!");
				JOptionPane.showMessageDialog(getRootPane(),
						"Error starting serial connection.", "Error",
						JOptionPane.ERROR_MESSAGE);
				timerModbus = null;
				reader = null;
			}
		}
	}
	
	private void stopSerial() {
		if (reader != null) {
			if (timerModbus != null) {
				timerModbus.cancel();
				timerModbus = null;
			}
			reader = null;
			sensor.setIcon(sensorOff);
			resetLabels();
		}
	}
	
	private void resetLabels() {
		upLabel.setIcon(upInactive);
		restLabel.setIcon(restInactive);
		downLabel.setIcon(downInactive);
	}
	
	public void sendScheduledRequest(final ModbusReader reader) {
		TimerTask task = new TimerTask() {

			@Override
			public void run() {
				reader.sendRequest();
				modbusLabel.setText(ModbusReader.getValue() + "");
				switch (reader.getDirection()) {
				case UP:
					resetLabels();
					upLabel.setIcon(upActive);
					upLabel.repaint();
					modbusLabel.setForeground(new Color(255, 9, 9));
					modbusLabel.repaint();
					break;
				case DOWN:
					resetLabels();
					downLabel.setIcon(downActive);
					downLabel.repaint();
					modbusLabel.setForeground(new Color(5, 194, 94));
					modbusLabel.repaint();
					break;
				case REST:
					resetLabels();
					restLabel.setIcon(restActive);
					restLabel.repaint();
					modbusLabel.setForeground(new Color(255, 162, 0));
					modbusLabel.repaint();
					break;
				}

			}
		};
		
		timerModbus.schedule(task, 0, 200);
	}

	public void setVideoParams(VideoParams params) {
		this.params = params;
	}	
	
}
