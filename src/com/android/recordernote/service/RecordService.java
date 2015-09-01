package com.android.recordernote.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import android.app.Service;
import android.content.Intent;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;

import com.android.recordernote.jni.Speex;
import com.android.recordernote.service.AudioRecorder.IRecorderDataCallBack;

public class RecordService extends Service {
	private static final String TAG = "RecordService";
	private static final boolean debug = true;

	public static final String SERVICE_ACTION = "SERVICE_ACTION";

	public static final int ACTION_DEFAULT = 200;
	public static final int ACTION_START_RECORDING = ACTION_DEFAULT + 1;
	public static final int ACTION_STOP_RECORDING = ACTION_START_RECORDING + 1;
	public static final int ACTION_PLAY_AUDIO = ACTION_STOP_RECORDING + 1;
	public static final int ACTION_STOP_AUDIO = ACTION_PLAY_AUDIO + 1;

	private AudioRecorder mAudioRecorder;
	private AudioPlayer mAudioPlayer;
	private Thread mReadDataThread;
	private SpeexFileInputStream mSpeexFileInputStream;
	private SpeexFileOutputStream mSpeexFileOutputStream;

	@Override
	public void onCreate() {
		if (debug) {
			Log.v(TAG, "onCreate");
		}
		Speex.init();
		super.onCreate();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if (debug) {
			Log.v(TAG, "onStartCommand");
		}
		parcelIntent(intent);
		return START_NOT_STICKY;
	}

	@Override
	public void onDestroy() {
		if (debug) {
			Log.v(TAG, "onDestroy");
		}
		Speex.release();
		super.onDestroy();
	}

	@Override
	public IBinder onBind(Intent intent) {
		if (debug) {
			Log.v(TAG, "onBind");
		}
		return null;
	}

	@Override
	public boolean onUnbind(Intent intent) {
		if (debug) {
			Log.v(TAG, "onUnbind");
		}
		return super.onUnbind(intent);
	}

	@Override
	public void onRebind(Intent intent) {
		if (debug) {
			Log.v(TAG, "onRebind");
		}
		super.onRebind(intent);
	}

	private void parcelIntent(Intent intent) {

		int action = intent.getIntExtra(SERVICE_ACTION, ACTION_DEFAULT);
		switch (action) {
		case ACTION_START_RECORDING:
			startAudioRecording();
			break;
		case ACTION_STOP_RECORDING:
			stopAudioRecording();
			break;
		case ACTION_PLAY_AUDIO:
			playAudio();
			break;
		case ACTION_STOP_AUDIO:
			stopAudio();
			break;
		default:
			break;
		}
	}

	private void playAudio() {
		stopAudio();
		mAudioPlayer = new AudioPlayer();
		mAudioPlayer.startAudioPlayer();
		mReadDataThread = new Thread(new Runnable() {
			@Override
			public void run() {
				File file = new File(Environment.getExternalStorageDirectory(), "test.audio");
				if (!file.exists()) {
					Log.e("error", "file not exits");
					return;
				}
				try {
					mSpeexFileInputStream = new SpeexFileInputStream(file);
				} catch (FileNotFoundException e) {
					Log.e("error", "e:" + e);
				}
				short[] bufferout = new short[160];
				while (!mReadDataThread.interrupted()) {
					try {
						mSpeexFileInputStream.read(bufferout);
					} catch (IOException e) {
						Log.e("error", "e:" + e);
					}
					mAudioPlayer.writeDataToPlayer(bufferout, 160);
				}
				try {
					mSpeexFileInputStream.close();
				} catch (IOException e) {
					Log.e("error", "e:" + e);
				}
			}
		});
		mReadDataThread.start();
	}

	private void stopAudio() {
		if (mReadDataThread != null)
			mReadDataThread.interrupt();
		if (mAudioPlayer != null)
			mAudioPlayer.stopAudioPlayer();
	}

	private void startAudioRecording() {
		stopAudioRecording();
		mAudioRecorder = new AudioRecorder();
		File file = new File(Environment.getExternalStorageDirectory(), "test.audio");
		if (file.exists()) {
			file.delete();
		}
		try {
			mSpeexFileOutputStream = new SpeexFileOutputStream(file);
		} catch (FileNotFoundException e) {
			Log.e("error", "create stream e:" + e);
			return;
		}
		mAudioRecorder.setRecorderDataCallBack(new IRecorderDataCallBack() {

			@Override
			public void RecorderDataCallBack(short[] data) {
				Log.v(TAG, "data call back");
				try {
					mSpeexFileOutputStream.write(data);
				} catch (IOException e) {
					Log.e("error", "write e:" + e);
				}
			}
		});
		mAudioRecorder.startAudioRecorder();
	}

	private void stopAudioRecording() {
		if (mAudioRecorder != null && mAudioRecorder.isAlive()) {
			mAudioRecorder.stopAudioRecorder();
			mAudioRecorder = null;
		}
		try {
			if (mSpeexFileOutputStream != null)
				mSpeexFileOutputStream.close();
		} catch (IOException e) {
			Log.e("error", "close e:" + e);
		}
	}

}
