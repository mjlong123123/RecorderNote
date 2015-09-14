package com.android.recordernote.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.android.recordernote.R;
import com.android.recordernote.service.RecordService;

public class PlayActivity extends BaseActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.play_activity_layout);

		initView();
		super.onCreate(savedInstanceState);
	}

	private void initView() {}


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
