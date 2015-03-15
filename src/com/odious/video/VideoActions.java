package com.odious.video;

public interface VideoActions {
	void start(int deviceId) throws Exception;
	void pause();
	void stop();
	void record();
}
