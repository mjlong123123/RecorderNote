package com.android.recordernote.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class WaveView extends View {
	private Paint mPaint;
	private float mRadius;
	private float cx;
	private float cy;
	private int level = 0;

	private int[] mColors;

	public WaveView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	public WaveView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public WaveView(Context context) {
		super(context);
		init();
	}
	
	/**
	 * set color 
	 * @param colors
	 */
	public void setColors(int [] colors){
		mColors = colors;
	}

	/**
	 * update level
	 * 
	 * @param level
	 *            
	 */
	public void update(int level) {
		if (level >= mColors.length) {
			return;
		}
		this.level = mColors.length - 1 - level;
		invalidate();
	}

	private void init() {
		mColors = new int[3];
		mColors[0] = Color.parseColor("#e0e0e0");
		mColors[1] = Color.parseColor("#f5f5f5");
		mColors[2] = Color.parseColor("#fafafa");
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		mRadius = (float) (getWidth() / 2.0);
		cx = (float) (getWidth() / 2.0);
		cy = (float) (getHeight() / 2.0);
		super.onSizeChanged(w, h, oldw, oldh);
	}

	@Override
	public void draw(Canvas canvas) {
		canvas.save();
		canvas.drawColor(Color.TRANSPARENT);
		for (int i = level; i < mColors.length; i++) {
			mPaint.setColor(mColors[i]);
			canvas.drawCircle(cx, cy, mRadius * (mColors.length - i) / (mColors.length), mPaint);
		}
		canvas.restore();
	}
}
