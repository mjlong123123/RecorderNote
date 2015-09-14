package com.android.recordernote.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.zip.DataFormatException;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.recordernote.R;
import com.android.recordernote.service.AudioRecorder;
import com.android.recordernote.service.SpeexFileOutputStream;
import com.android.recordernote.service.AudioRecorder.IRecorderDataCallBack;
import com.android.recordernote.view.WaveView;

public class RecordActivity extends BaseActivity implements OnClickListener {
	private static final String TAG = "RecordActivity";
	private TextView mTimeText;
	private WaveView mWaveView;
	private ImageButton mOkButton;
	
	private String mSaveFilePath;

	private static final String TIME_FORMAT = "%d%d:%d%d";
	private AudioRecorder mAudioRecorder;
	private SpeexFileOutputStream mSpeexFileOutputStream;
	private int[] colors = new int[6];
	private int mMax;
	private long mSecond;
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {

			switch (msg.what) {
			case 100: {
				int range = msg.arg1;
				range = Math.round(range);
				if (range > mMax) {
					mMax = range;
				}
				if (mMax != 0) {
					range = (colors.length - 1) * range / mMax;
					range++;
					mWaveView.update(range);
				}
			}
				break;
			case 101: {
				mSecond++;
				mTimeText.setText(String.format(TIME_FORMAT, mSecond / 60 / 10, mSecond / 60 % 10, mSecond % 60 / 10, mSecond % 60 % 10));
				sendMessageDelayed(obtainMessage(101), 100);
			}
				break;
			default:
				break;
			}
		};
	};

	private long distanceTime;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.record_activity_layout);

		setFinishOnTouchOutside(false);

		initView();

		startRecord();
		
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			if(event.getAction() == KeyEvent.ACTION_DOWN){
				gotoSave();}
		}
		return super.onKeyDown(keyCode, event);
	}

	private void initView() {
		mWaveView = (WaveView) findViewById(R.id.wave_view);
		colors[0] = Color.parseColor("#e0f2f1");
		colors[1] = Color.parseColor("#b2dfdb");
		colors[2] = Color.parseColor("#80cbc4");
		colors[3] = Color.parseColor("#4db6ac");
		colors[4] = Color.parseColor("#26a69a");
		mWaveView.setColors(colors);
		mWaveView.update(colors.length - 1);
		mWaveView.setOnClickListener(this);

		mTimeText = (TextView) findViewById(R.id.time_text);
		mTimeText.setText(String.format(TIME_FORMAT, 0, 0, 0, 0));
		
		mOkButton = (ImageButton)findViewById(R.id.ok_button);
		mOkButton.setOnClickListener(this);
	}

	private void startCountTime() {
		mHandler.sendMessageDelayed(mHandler.obtainMessage(101), 1000);
	}

	private void stopCountTime() {
		mHandler.removeMessages(101);
	}

	private void startRecord() {
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
				if (System.currentTimeMillis() - distanceTime > 50) {
					distanceTime = System.currentTimeMillis();
					mHandler.removeMessages(100);
					mHandler.sendMessage(mHandler.obtainMessage(100, data[0], 0));
				}
			}
		});
		mAudioRecorder.startAudioRecorder();
		startCountTime();
	}

	private void stopRecord() {

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
		stopCountTime();
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.ok_button:
			gotoSave();
			break;
		}
	}
	
	private File generateSaveFile(){
		File dir = new File(Environment.getExternalStorageDirectory(),"RecodeNote");
		if(!dir.exists()){
			dir.mkdirs();
		}
//		DateFormat df = 
		File file = new File(dir,System.currentTimeMillis()+".re");
		mSaveFilePath = file.getAbsolutePath();
		return file;
	}
	
	private void gotoSave(){
		stopRecord();
		finish();
		Intent intent = new Intent();
		intent.setClass(this, SaveActivity.class);
		intent.putExtra(SaveActivity.KEY_FILE_DIR, mSaveFilePath);
		startActivity(intent);
	}
}
