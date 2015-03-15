package com.odious.video;

import static com.googlecode.javacv.cpp.opencv_core.CV_FONT_HERSHEY_TRIPLEX;
import static com.googlecode.javacv.cpp.opencv_core.cvClearMemStorage;
import static com.googlecode.javacv.cpp.opencv_core.cvPoint;
import static com.googlecode.javacv.cpp.opencv_core.cvPutText;
import static com.googlecode.javacv.cpp.opencv_core.cvScale;
import static com.googlecode.javacv.cpp.opencv_core.cvAddS;
import static com.googlecode.javacv.cpp.opencv_core.cvNot;
import static com.googlecode.javacv.cpp.opencv_highgui.cvSaveImage;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingWorker;

import com.googlecode.javacpp.Loader;
import com.googlecode.javacv.FrameGrabber;
import com.googlecode.javacv.FrameRecorder;
import com.googlecode.javacv.OpenCVFrameRecorder;
import com.googlecode.javacv.cpp.opencv_core.CvFont;
import com.googlecode.javacv.cpp.opencv_core.CvMemStorage;
import com.googlecode.javacv.cpp.opencv_core.CvScalar;
import com.googlecode.javacv.cpp.opencv_core.IplImage;
import com.googlecode.javacv.cpp.opencv_highgui;
import com.googlecode.javacv.cpp.opencv_objdetect;
import com.odious.modbus.ModbusReader;
import com.odious.panel.SettingsDialog;
import com.odious.util.VideoFileManager;
import com.odious.util.VideoParams;

public class VideoPanel extends JPanel implements VideoActions {
	
	public static final int RESOLUTION_WIDTH = 1280;
	public static final int RESOLUTION_HEIGHT = 720;
	
	private FrameGrabber grabber;
	private FrameRecorder recorder;
	private IplImage grabbedImage;
	private int deviceId;
	
	private VideoParams params;

	public enum Status {
		STARTED,
		PAUSED,
		STOPPED,
		RECORDING
	}
	
	private Status status = Status.STOPPED;
	private File videoFile;
	private JLabel image;
	private VideoFileManager videoFileManager;
	private CvScalar color;
	private float displayedNumber;
	private int xPoint;
	private int yPoint;
	private double contrast;
	private double brightness;
	private boolean invert;
	
	public static VideoPanel newInstance() {
		return new VideoPanel();
	}
	
	private VideoPanel() {
		setLayout(new BorderLayout());
		image = new JLabel();
		add(image, BorderLayout.CENTER);
	}

	public void setParams(VideoParams params) {
		this.params = params;
		videoFileManager = new VideoFileManager(params.getSettingsFilePath(), params.getGeoLocation());
	}
	
	@Override
	public void pause() {
		this.status = Status.PAUSED;
	}

	@Override
	public void stop() {
		this.status = Status.STOPPED;
	}

	@Override
	public void record() {
		this.status = Status.RECORDING;
	}

	public void screenshot() {
		getScreenshot();
	}
	
	public void contrast(double value) {
		contrast = value;
	}
	
	public void brightness(double value) {
		brightness = value;
	}
	
	public void invert(boolean value) {
		invert = value;
	}
	
	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}
	
	@Override
	public void start(int deviceId) throws FrameGrabber.Exception, FrameRecorder.Exception {
		this.deviceId = deviceId;
		this.status = Status.STARTED;
		Loader.load(opencv_objdetect.class);
		try {
			grabber = FrameGrabber.createDefault(deviceId);
			grabber.setImageWidth(RESOLUTION_WIDTH);
			grabber.setImageHeight(RESOLUTION_HEIGHT);
			grabber.setFrameRate(params.getFramerate());
			grabber.start();
		} catch (FrameGrabber.Exception e) {
			e.printStackTrace();
		}
		
		CvMemStorage storage = CvMemStorage.create();
		grabbedImage = grabber.grab();
		
		videoFile = videoFileManager.getVideoFile();
		recorder = new OpenCVFrameRecorder(videoFile, RESOLUTION_WIDTH, RESOLUTION_HEIGHT); 
//		recorder = FrameRecorder.createDefault(videoFile, RESOLUTION_WIDTH, RESOLUTION_HEIGHT);
		recorder.setVideoCodec(opencv_highgui.CV_FOURCC('M','P','4','2'));
		recorder.setFrameRate(params.getFramerate());
		recorder.start();
		
		Dimension videoDimension = AppFrame.getVideoDimension();
		
		CvFont font = new CvFont(CV_FONT_HERSHEY_TRIPLEX, params.getFontSize(), 1);
		while (!status.equals(Status.STOPPED) && (grabbedImage = grabber.grab()) != null) {
			cvClearMemStorage(storage);
			
			displayedNumber = ModbusReader.getValue();
			setTextColor();
			setTextPosition();
			
			//text
			font.vscale((float) params.getFontSize());
			cvPutText(grabbedImage, "" + displayedNumber + "m", cvPoint(xPoint, yPoint), font, color);
			
			//contrast
			cvScale(grabbedImage, grabbedImage, contrast, 0);
			
			//brightness - CvScalar(blue, green, red, nothing?)
			cvAddS(grabbedImage, new CvScalar(brightness, brightness, brightness, 0), grabbedImage, null);
	
			if (invert) {
				cvNot(grabbedImage, grabbedImage);
			}
			
			BufferedImage bufImage = grabbedImage.getBufferedImage();
			bufImage = VideoHelper.resize(bufImage, videoDimension.width, videoDimension.height);
			image.setIcon(new ImageIcon(bufImage));
			image.repaint();
			if (Status.RECORDING.equals(status)) {
				recorder.record(grabbedImage);
			}
		}
		
		clearVideoResources();
//		EGB1.getChronometer().stop();
//		if(EGB1.getChronoTimer() != null){
//			EGB1.getChronoTimer().cancel();
//			EGB1.getChronoTimer().purge();
//		}
//		EGB1.getSensor().setIcon(sensorOff);
		
	}
	
	public void clearVideoResources() {
		if (recorder != null && grabber != null) {
				try {
					recorder.stop();
					grabber.stop();
					status = Status.STOPPED;
				} catch (com.googlecode.javacv.FrameRecorder.Exception
						| com.googlecode.javacv.FrameGrabber.Exception e) {
					e.printStackTrace();
				}
		}
		videoFileManager.deleteTempVideo();
		videoFileManager.deleteScreenshotDir();
	}
	
	private void getScreenshot() {
		if (grabbedImage != null) {
//			String modbusValue = ModbusReader.getValue();
			String modbusValue = "256";
			String fileLocation = "Saturn";
			File screenshotDir = videoFileManager.getScrenshotDir();
			DateFormat time = new SimpleDateFormat("hh_mm_ss");
			
			if (screenshotDir.exists()) {
				cvSaveImage(screenshotDir.getAbsolutePath() + "\\"
						+ fileLocation + " " + modbusValue + "m "
						+ "(ora " + time.format(new Date()) + ")" + ".jpg",
						grabbedImage);
			} else {
				cvSaveImage(System.getProperty("user.dir") + "\\"
						+ fileLocation + " " + modbusValue + "m "
						+ "(ora " + time.format(new Date()) + ")" + ".jpg",
						grabbedImage);
			}
			playSound("/snapshot.wav");
		}
	}
	
	public void playSound(String fileName) {
		AudioInputStream inputStream = null;
		try {
			Clip clip = AudioSystem.getClip();
			inputStream = AudioSystem.getAudioInputStream(getClass().getResource(fileName));
			clip.open(inputStream);
			clip.start();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public VideoParams getParams() {
		return params;
	}

	private void setTextColor() {
		switch (params.getFontColor()) {
		case "verde":
			color = CvScalar.GREEN;
			break;
		case "alb":
			color = CvScalar.WHITE;
			break;
		case "rosu":
			color = CvScalar.RED;
			break;
		case "magenta":
			color = CvScalar.MAGENTA;
			break;
		default:
			color = CvScalar.GREEN;
			break;
		}
	}

	private void setTextPosition() {
		switch (params.getFontPosition()) {
		case SettingsDialog.UP_LEFT:
			xPoint = (RESOLUTION_WIDTH / 8) - (RESOLUTION_WIDTH / 16);
			yPoint = (RESOLUTION_HEIGHT / 10);
			break;
		case SettingsDialog.UP_CENTER:
			xPoint = 4 * (RESOLUTION_WIDTH / 10);
			yPoint = (RESOLUTION_HEIGHT / 10);
			break;
		case SettingsDialog.UP_RIGHT:
			xPoint = 5 * (RESOLUTION_WIDTH / 8);
			yPoint = (RESOLUTION_HEIGHT / 10);
			break;
		case SettingsDialog.DOWN_LEFT:
			xPoint = (RESOLUTION_WIDTH / 8) - (RESOLUTION_WIDTH / 16);
			yPoint = 9 * (RESOLUTION_HEIGHT / 10);
			break;
		case SettingsDialog.DOWN_CENTER:
			xPoint = 4 * (RESOLUTION_WIDTH / 10);
			yPoint = 9 * (RESOLUTION_HEIGHT / 10);
			break;
		case SettingsDialog.DOWN_RIGHT:
			xPoint = 5 * (RESOLUTION_WIDTH / 8);
			yPoint = 9 * (RESOLUTION_HEIGHT / 10);
			break;
		default:
			xPoint = 4 * (RESOLUTION_WIDTH / 8);
			yPoint = 6 * (RESOLUTION_HEIGHT / 10);
			break;
		}
	}

	public class CameraSwingWorker extends SwingWorker<String, Object> {
		@Override
		protected String doInBackground() throws Exception {
				try {
					start(deviceId);
				} catch (com.googlecode.javacv.FrameGrabber.Exception
						| com.googlecode.javacv.FrameRecorder.Exception e) {
					e.printStackTrace();
					System.out.println("Error starting camera.");
				}
			return "Camera started";
		}
	}
	
}
