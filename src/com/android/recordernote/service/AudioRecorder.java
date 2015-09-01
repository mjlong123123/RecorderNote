package com.android.recordernote.service;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.util.Log;

public class AudioRecorder extends Thread {
	private static final String TAG = "AudioRecorder";
	private static final boolean debug = true;
	// 音频帧大小
	private static final int FRAME_SIZE_AUDIO = 160;
	// 录音用
	private AudioRecord mAudioRecord = null;
	// 录音数据回调
	private IRecorderDataCallBack mIRecorderDataCallBack = null;

	public AudioRecorder() {
	}

	/**
	 * 设置数据回调
	 * 
	 * @param callback
	 */
	public void setRecorderDataCallBack(IRecorderDataCallBack callback) {
		mIRecorderDataCallBack = callback;
	}

	/**
	 * 开始录音
	 */
	public void startAudioRecorder() {
		if (!isAlive()) {
			if (debug) {
				Log.v(TAG, "startAudioRecorder");
			}
			start();
		}
	}

	/**
	 * 停止录音
	 */
	public void stopAudioRecorder() {
		if (isAlive() && !interrupted()) {
			if (debug) {
				Log.v(TAG, "startAudioRecorder");
			}
			mIRecorderDataCallBack = null;
			interrupt();
		}
	}

	@Override
	public void run() {
		try {
			// 获得录音缓冲区大小
			int bufferSize = AudioRecord.getMinBufferSize(8000, AudioFormat.CHANNEL_CONFIGURATION_MONO, AudioFormat.ENCODING_PCM_16BIT);
			// 获得录音机对象
			mAudioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, 8000, AudioFormat.CHANNEL_CONFIGURATION_MONO, AudioFormat.ENCODING_PCM_16BIT, bufferSize);
			// 开始录音
			mAudioRecord.startRecording();
			// 数据buffer
			short[] readBuffer = new short[FRAME_SIZE_AUDIO];
			int count = 0;
			int index = 0;

			while (!isInterrupted()) {
				// 从mic读取音频数据
				count = mAudioRecord.read(readBuffer, index, FRAME_SIZE_AUDIO - index);
				if (debug) {
					Log.v(TAG, "run count:" + count);
				}
				if (count > 0) {
					index += count;
				}

				if (index == FRAME_SIZE_AUDIO) {
					index = 0;
					if (debug) {
						Log.v(TAG, "run send data:" + readBuffer[0]);
					}
					if (mIRecorderDataCallBack != null) {
						mIRecorderDataCallBack.RecorderDataCallBack(readBuffer);
					}
				}
			}
		} catch (Exception e) {
			if (debug) {
				Log.v(TAG, "run error:"+e);
			}
		} finally {
			if (debug) {
				Log.v(TAG, "run final");
			}
			if (mAudioRecord != null) {
				if (mAudioRecord.getRecordingState() == AudioRecord.RECORDSTATE_RECORDING) {
					mAudioRecord.stop();
				}
				mAudioRecord.release();
				mAudioRecord = null;
			}
		}

	}

	/**
	 * 录音数据返回
	 * 
	 * @author dragon
	 * 
	 */
	public static interface IRecorderDataCallBack {
		public void RecorderDataCallBack(short[] data);
	}
}
