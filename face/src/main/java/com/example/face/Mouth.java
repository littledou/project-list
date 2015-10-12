package com.example.face;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

/**
 * 嘴巴的定义340-200
 * @author dou
 *
 */
public class Mouth extends BasePart {


	public Mouth(Context context) {
		super(context);
		
		mRect.left = 0;
		mRect.right = 148;
		mRect.top = 0;
		mRect.bottom = 111;
	}

}
