package com.android.recordernote.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.android.recordernote.R;
import com.android.recordernote.service.RecordService;

public class TestActivity extends BaseActivity {
	private Button button1;
	private Button button2;
	private Button button3;
	private Button button4;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.test_activity_layout);

		initView();
		super.onCreate(savedInstanceState);
	}

	private void initView() {
		button1 = (Button) findViewById(R.id.button1);
		button1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startRecording();
			}
		});
		button2 = (Button) findViewById(R.id.button2);
		button2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				stopRecording();
			}
		});
		button3 = (Button) findViewById(R.id.button3);
		button3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				play();
			}
		});
		button4 = (Button) findViewById(R.id.button4);
		button4.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				stop();
			}
		});
	}

	private void startRecording() {
		Intent intent = new Intent();
		intent.setClass(this, RecordService.class);
		intent.putExtra(RecordService.SERVICE_ACTION, RecordService.ACTION_START_RECORDING);
		startService(intent);
	}

	private void stopRecording() {
		Intent intent = new Intent();
		intent.setClass(this, RecordService.class);
		intent.putExtra(RecordService.SERVICE_ACTION, RecordService.ACTION_STOP_RECORDING);
		startService(intent);
	}

	private void play() {
		Intent intent = new Intent();
		intent.setClass(this, RecordService.class);
		intent.putExtra(RecordService.SERVICE_ACTION, RecordService.ACTION_PLAY_AUDIO);
		startService(intent);
	}

	private void stop() {
		Intent intent = new Intent();
		intent.setClass(this, RecordService.class);
		intent.putExtra(RecordService.SERVICE_ACTION, RecordService.ACTION_STOP_AUDIO);
		startService(intent);
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
}
