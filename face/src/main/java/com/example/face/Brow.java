package com.example.face;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

/**
 * 眉毛的定义 370-131
 * @author dou
 */
public class Brow extends BasePart{

	public Brow(Context context,float radio , int direc) {
		super(context);

		mRect.left = 0;
		mRect.right = (int)(370*radio);
		mRect.top = 0;
		mRect.bottom = (int)(131*radio);
		this.direc = direc;
		this.radio = radio;
	}
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		Bitmap bm = null;
		if (direc == LEFT) {
			bm = BitmapFactory.decodeResource(getResources(), R.drawable.browleft0);
		}else if(direc == RIGHT){
			bm = BitmapFactory.decodeResource(getResources(), R.drawable.browright0);
		}

		canvas.drawBitmap(bm, null, mRect, mPaint);

	}
}
