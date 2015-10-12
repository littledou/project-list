package com.example.face;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

/**
 * 眼睛的定义 294-294
 * @author dou
 *
 */
public class Eye extends BasePart{


	private int currentPosition =0;
	public Eye(Context context) {
		super(context);

		mRect.left = 0;
		mRect.right = 126;
		mRect.top = 0;
		mRect.bottom = 127;
	}

}
