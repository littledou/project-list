package com.example.face;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

/**
 * 基本部位
 * @author dou
 */
public class BasePart extends View{
	protected int speed;

	protected Paint mPaint;
	protected Rect mRect;
	public static final int LEFT = 0;
	public static final int RIGHT = 1;
	protected int direc = 0;

	protected float radio;
	public BasePart(Context context) {
		super(context);
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setStrokeWidth(1.0f);
		mRect = new Rect();
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}
}
