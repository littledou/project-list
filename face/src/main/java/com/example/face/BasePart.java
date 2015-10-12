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
	protected int mSpeed;

	protected Paint mPaint;
	protected Rect mRect;

	public BasePart(Context context) {
		super(context);
		mPaint = new Paint();
		mRect = new Rect();
	}

}
