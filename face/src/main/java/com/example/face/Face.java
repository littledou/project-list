package com.example.face;

import android.content.Context;
import android.widget.RelativeLayout;

/**
 * Face 包括腮红2，眉毛2，眼睛2，嘴巴1
 * 
 */
public class Face extends RelativeLayout{

	
	
	public Face(Context context)  {
		super(context);
		Eye eye = new Eye(context);
		eye.setLayoutParams(new LayoutParams(126,127));
		addView(eye);
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
		System.out.println("count = "+getChildCount());
	}
}
