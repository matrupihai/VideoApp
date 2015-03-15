package com.odious.panel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import net.miginfocom.swing.MigLayout;

import com.odious.gui.CustomButton;
import com.odious.gui.ImageHelper;
import com.odious.model.CustomComboBoxModel;
import com.odious.util.Chronometer;
import com.odious.video.VideoHelper;
import com.odious.video.VideoPanel;
import com.odious.video.VideoPanel.Status;

public class VideoControlPanel extends JPanel {
	private VideoPanel video;
	
	private static ImageIcon sensorOff, sensorOn, record, hoverPause, pressedPause, 
			hoverPauseRed, hoverRecord;
	private JLabel sensorLabel;
	private final JLabel chronoLabel = new JLabel("0:0:0");

	private CustomComboBoxModel comboModel;
	private CustomButton recordButton;
	private CustomButton playButton;
	private CustomButton stopButton;
	private CustomButton refreshButton;

	private JComboBox<String> comboDevices;

	private JSlider sliderContrast;

	private JSlider sliderBrightness;

	private JCheckBox invertCheck;
	private static Timer timerChronos;
	
	static {
		ImageHelper imgHelper = new ImageHelper();

		try {
			sensorOff = imgHelper.loadImageIcon("sensorOff.png");
			sensorOn = imgHelper.loadImageIcon("sensorOn.png");
			record = imgHelper.loadImageIcon("record.png");
			hoverPause = imgHelper.loadImageIcon("hoverPause.png");
			pressedPause = imgHelper.loadImageIcon("pressedPause.png");
			hoverPauseRed = imgHelper.loadImageIcon("hoverPauseRed.png");
			hoverRecord = imgHelper.loadImageIcon("hoverRecord.png");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public VideoControlPanel() {
		JPanel panelControls = new JPanel(new MigLayout());
		panelControls.setBorder(BorderFactory.createLineBorder(MainPanel.BASE_COLOR, 1));
		VideoHelper.detectVideoDevices();
		comboDevices = new JComboBox<String>();
		comboModel = new CustomComboBoxModel(
				VideoHelper.getDevices());
		comboDevices.setModel(comboModel);
		Dimension comboDimension = new Dimension(160, 30);
		comboDevices.setMinimumSize(comboDimension);
		comboDevices.setMaximumSize(comboDimension);
		comboDevices.setEditable(false);
		comboDevices.setSelectedIndex(0);
		sensorLabel = new JLabel(sensorOff);
		
		playButton = new CustomButton("play.png", "hoverPlay.png", "pressedPlay.png");
		playButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent a) {
				playAction();
			}
		});
		recordButton = new CustomButton("record.png",
				"hoverRecord.png", "pressedRecord.png");
		recordButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				recordAction();
			}
		});
		
		stopButton = new CustomButton("stop.png",
				"hoverStop.png", "pressedStop.png");
		stopButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				stopAction();
			}
		});

		refreshButton = new CustomButton("refresh.png",
				"hoverRefresh.png", "pressedRefresh.png");
		refreshButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				refreshAction();
			}
		});

		CustomButton screenshotButton = new CustomButton("screenshot.png",
				"hoverScreenshot.png", "pressedScreenshot.png");
		screenshotButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				screenshotAction();
			}
		});

		JPanel containerChronometer = new JPanel(new MigLayout());
		chronoLabel.setFont(new Font("Arial", Font.PLAIN, 30));
		chronoLabel.setForeground(MainPanel.BASE_COLOR);
		containerChronometer.add(chronoLabel);

		JPanel paramPanel = new JPanel(new MigLayout());
		paramPanel.add(refreshButton, "west");
		paramPanel.add(comboDevices, "west");
		paramPanel.add(sensorLabel, "west, gapleft 20");
		JPanel buttonPanel = new JPanel(new MigLayout());
		buttonPanel.add(playButton, "west");
		buttonPanel.add(recordButton, "west");
		buttonPanel.add(stopButton, "west");
		buttonPanel.add(screenshotButton, "west");
		panelControls.add(paramPanel, "wrap");
		panelControls.add(buttonPanel, "wrap");
		panelControls.add(containerChronometer, "top, center, wrap");
		panelControls.add(loadSlidersPanel(), "wrap");
		
		add(panelControls);
	}
	
	private JPanel loadSlidersPanel() {
		JPanel panel = new JPanel(new MigLayout());
		
		sliderContrast = new JSlider(SwingConstants.HORIZONTAL, 0, 500, 100);
		sliderContrast.setMinorTickSpacing(10);
		sliderContrast.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				video.contrast(getContrastValue());
			}
		});
		
		sliderBrightness = new JSlider(SwingConstants.HORIZONTAL, -1800, 1800, 0);
		sliderBrightness.setMinorTickSpacing(10);
		sliderBrightness.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				video.brightness(getBrightnessValue());
			}
		});
		
		invertCheck = new JCheckBox("Negativ");
		invertCheck.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				video.invert(isInvert());
			}
		});
		
		
		JButton defaultButton = new JButton("Default");
		defaultButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				setDefaults();
			}
		});
		
		panel.add(new JLabel("Contrast"), "wrap");
		panel.add(sliderContrast, "wrap");
		panel.add(new JLabel("Luminozitate"), "wrap");
		panel.add(sliderBrightness, "wrap");
		
		JPanel settings = new JPanel(new MigLayout());
		settings.add(invertCheck, "west, gapright 20");
		settings.add(defaultButton, "west");
		panel.add(settings);

		return panel;
	}
	
	private void setDefaults() {
		sliderContrast.setValue(100);
		sliderBrightness.setValue(0);
	}
	
	public double getContrastValue() {
		return ((double) sliderContrast.getValue() / 100);
	}

	public double getBrightnessValue() {
		return ((double) sliderBrightness.getValue() / 10);
	}
	
	public boolean isInvert() {
		return invertCheck.isSelected();
	}
	
	protected void refreshAction() {
		VideoHelper.detectVideoDevices();
		comboDevices.setModel(new CustomComboBoxModel(VideoHelper.getDevices()));
		comboDevices.setSelectedIndex(0);
	}

	protected void stopAction() {
		Status status = video.getStatus();
		if (Status.RECORDING.equals(status) || Status.PAUSED.equals(status)) {
			int option = JOptionPane.showConfirmDialog(getRootPane(),
					"Stop recording?");
			if (option == JOptionPane.YES_OPTION) {
				video.stop();
				sensorLabel.setIcon(sensorOff);
				recordButton.setIcon(record);
				recordButton.setRolloverIcon(hoverRecord);
				chronoLabel.setForeground(MainPanel.BASE_COLOR);
				chronoLabel.repaint();
				Chronometer.stop();
				Chronometer.delayTime = 0;
				if (timerChronos != null) {
					timerChronos.cancel();
					timerChronos.purge();
				}
			}
		}
	}
	
	protected void playAction() {
		if (!comboModel.getElementAt(0).equals(VideoHelper.NO_SOURCE)) {
			if (video != null && video.getStatus() == Status.STOPPED) {
				final VideoPanel.CameraSwingWorker camera = video.new CameraSwingWorker();
				camera.execute();
				System.out.println("Camera started!");
				// startSerial();
				chronoLabel.setText("0:0:0");
				KeyboardFocusManager manager = KeyboardFocusManager
						.getCurrentKeyboardFocusManager();
				// manager.addKeyEventDispatcher(video.new
				// CustomDispatcher());
			}
		} else {
			JOptionPane.showMessageDialog(getRootPane(),
					"No video source!", "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}
	
	protected void recordAction() {
		if (video != null) {
//			writeToFile(videoFrame.getVideoDestination() + "info.txt",
//					getFieldsData());
			Status status = video.getStatus();
			if (status.equals(Status.RECORDING)) {
				video.pause();
				sensorLabel.setIcon(sensorOff);
				recordButton.setIcon(hoverPauseRed);
				recordButton.setRolloverIcon(hoverPauseRed);
				chronoLabel.setForeground(MainPanel.BASE_COLOR);
				chronoLabel.repaint();
				Chronometer.stop();
				if (timerChronos != null) {
					timerChronos.cancel();
					timerChronos.purge();
				}
			} else {
				video.record();
				sensorLabel.setIcon(sensorOn);
				chronoLabel.setForeground(new Color(255, 9, 9));
				chronoLabel.repaint();
				Chronometer.start();
				getTime(chronoLabel, Chronometer.delayTime);
				recordButton.setIcon(record);
				recordButton.setRolloverIcon(hoverPause);
				recordButton.setPressedIcon(pressedPause);
			}
		}
	}
	
	public void screenshotAction() {
		if (video != null) {
			video.screenshot();
		}
	}
	
	public void getTime(final JLabel label, final int delay) {
		timerChronos = new Timer();
		TimerTask task = new TimerTask() {
			
			@Override
			public void run() {
				label.setText("" + Chronometer.getElapsedSeconds(delay));
			}
		};
		timerChronos.schedule(task, 0, 1000);
	}
	
	public void setVideo(VideoPanel video) {
		this.video = video;
		this.video.contrast(getContrastValue());
		this.video.brightness(getBrightnessValue());
	}
	
}
