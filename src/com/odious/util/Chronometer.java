package com.odious.util;

public class Chronometer {
	private static long startTime = 0;
	private static long stopTime = 0;
	private static boolean running = false;
	public static int delayTime = 0;

	public static void start() {
		startTime = System.nanoTime();
		running = true;
	}

	public static void stop() {
		stopTime = System.nanoTime();
		running = false;
	}

	public static String getElapsedSeconds(int delay) {
		Double seconds;
		if (running) {
			seconds = (System.nanoTime() - startTime) / 1000000000.0;
		} else {
			seconds = (stopTime - startTime) / 1000000000.0;
		}
		int s = seconds.intValue() + delay;
		delayTime = s;
		return s / 24 / 60 + ":" + s / 60 % 24 + ":" + s % 60;
	}

}
