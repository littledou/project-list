package com.readface.cafe.anim;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;

/**
 * 腮红的定义 300-200
 *
 * @author dou
 */
public class Blush extends BasePart {


    public Blush(Context context, float radio, int direc) {
        super(context);
        mRect.left = 0;
        mRect.right = (int) (300 * radio);
        mRect.top = 0;
        mRect.bottom = (int) (200 * radio);


        this.direc = direc;
        this.radio = radio;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.blush);

        if (direc == RIGHT) {
            Matrix matrix = new Matrix();
            matrix.postScale(-1, 1);
            bm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
        }
        canvas.drawBitmap(bm, null, mRect, mPaint);
    }
}
