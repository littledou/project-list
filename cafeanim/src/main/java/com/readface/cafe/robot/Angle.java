package com.readface.cafe.robot;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import com.readface.cafe.anim.R;

/**
 * Created by mac on 15/11/23.
 */
public class Angle extends BasePart {

    int res;
    private Bitmap bitmap;

    public Angle(Context context, float radio) {
        super(context, radio);

        mRect.left = 0;
        mRect.right = (int) (87 * radio);
        mRect.top = 0;
        mRect.bottom = (int) (160 * radio);
        res = R.mipmap.angle;
        enableBitmap();
    }


    private void enableBitmap() {

        if (bitmap != null && !bitmap.isRecycled()) {
            bitmap.recycle();
            bitmap = null;
        }
        bitmap = BitmapFactory.decodeResource(getResources(), res);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(bitmap, null, mRect, mPaint);
    }
}
