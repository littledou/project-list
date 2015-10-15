package com.readface.cafe.anim;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;

/**
 * 眉毛的定义 370-862
 * @author dou
 */
public class Brow extends BasePart{


	private Rect mRect1;
	public Brow(Context context,float radio) {
		super(context);

		mRect.left = 0;
		mRect.right = (int)(370*radio);
		mRect.top = 0;
		mRect.bottom = (int)(131*radio);
		mRect1 = new Rect();
		mRect1.left = (int) (492 * radio);
		mRect1.right = (int) (862 * radio);
		mRect1.top = 0;
		mRect1.bottom = (int) (131 * radio);
		this.radio = radio;
	}
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		Bitmap bm1 = BitmapFactory.decodeResource(getResources(), R.drawable.browleft0);
		Bitmap bm2 = BitmapFactory.decodeResource(getResources(), R.drawable.browright0);

		canvas.drawBitmap(bm1, null, mRect, mPaint);
		canvas.drawBitmap(bm2, null, mRect1, mPaint);

	}
}
