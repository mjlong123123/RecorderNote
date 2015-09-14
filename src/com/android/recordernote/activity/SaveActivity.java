package com.android.recordernote.activity;

import java.io.File;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.android.recordernote.R;

public class SaveActivity extends BaseActivity implements OnClickListener {
	private static final String TAG = "EditorActivity";
	private Button mButtonOk;
	private Button mButtonCancle;
	public static final String KEY_FILE_DIR = "KEY_FILE_DIR";
	private String mFilePath;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.save_activity_layout);

		setFinishOnTouchOutside(false);
		initIntent();
		init();
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
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ok_button:
			Intent intent = new Intent();
			intent.setClass(SaveActivity.this, MainActivity.class);
			intent.putExtra(MainActivity.KEYGUARD_SERVICE, mFilePath);
			SaveActivity.this.startActivity(intent);

			finish();
			break;
		case R.id.cancel_button:
			File file = new File(mFilePath);
			File temp = new File(mFilePath + ".temp");
			file.renameTo(temp);
			temp.delete();

			finish();
			break;
		}
	}

	private void init() {
		mButtonOk = (Button) findViewById(R.id.ok_button);
		mButtonOk.setOnClickListener(this);
		mButtonCancle = (Button) findViewById(R.id.cancel_button);
		mButtonCancle.setOnClickListener(this);
	}

	private void initIntent() {
		mFilePath = getIntent().getStringExtra(KEY_FILE_DIR);
		if (mFilePath == null || !(new File(mFilePath).exists())) {
			Log.e(TAG, "initIntent mFilePath:" + mFilePath);
			finish();
			return;
		}
	}

}
