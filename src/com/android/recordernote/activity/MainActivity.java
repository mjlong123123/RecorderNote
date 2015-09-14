package com.android.recordernote.activity;

import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.recordernote.R;
import com.android.recordernote.service.RecordService;

public class MainActivity extends BaseActivity {

	private ExpandableListView mExpandableListView;
	private BaseExpandableListAdapter mBaseExpandableListAdapter;
	private TextView titleText;
	private ImageButton addImageButton;
	private String mNewFilePath;
	private final String KEY_NEW_FILE_PATH = "KEY_NEW_FILE_PATH";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.main_activity_layout);
		initIntent(getIntent());
		initView();
		super.onCreate(savedInstanceState);
	}

	private void initView() {
		titleText = (TextView)findViewById(R.id.title_text);
		addImageButton = (ImageButton)findViewById(R.id.title_add);
		addImageButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(MainActivity.this, RecordActivity.class);
				MainActivity.this.startActivity(intent);
			}
		});
		mExpandableListView = (ExpandableListView)findViewById(R.id.list_view);
		mBaseExpandableListAdapter = new CustomAdapter();
		mExpandableListView.setAdapter(mBaseExpandableListAdapter);
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
	protected void onNewIntent(Intent intent) {
		initIntent(intent);
		super.onNewIntent(intent);
	}
	
	private void initIntent(Intent intent){
		mNewFilePath = intent.getStringExtra(KEY_NEW_FILE_PATH);
	}
	
	private class CustomAdapter extends BaseExpandableListAdapter{

		@Override
		public int getGroupCount() {
			return 10;
		}

		@Override
		public int getChildrenCount(int groupPosition) {
			
			return 3;
		}

		@Override
		public Object getGroup(int groupPosition) {
			return null;
		}

		@Override
		public Object getChild(int groupPosition, int childPosition) {
			return null;
		}

		@Override
		public long getGroupId(int groupPosition) {
			return groupPosition;
		}

		@Override
		public long getChildId(int groupPosition, int childPosition) {
			return childPosition;
		}

		@Override
		public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
			if(convertView == null){
				convertView = LayoutInflater.from(MainActivity.this).inflate(R.layout.item_group_layout, parent, false);
			}
			TextView tv = (TextView)convertView.findViewById(R.id.group_item_text);
			tv.setText("group item "+groupPosition);
			return convertView;
		}

		@Override
		public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
			if(convertView == null){
				convertView = LayoutInflater.from(MainActivity.this).inflate(R.layout.item_layout, parent, false);
			}
			TextView tv = (TextView)convertView.findViewById(R.id.item_textview);
			tv.setText("item "+groupPosition);
			return convertView;
		}

		@Override
		public boolean hasStableIds() {
			return false;
		}

		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition) {
			return true;
		}
	}
}
