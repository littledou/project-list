package com.example.face;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;

/**
 * 嘴巴的定义340-200
 *
 * @author dou
 */
public class Mouth extends BasePart {


    private int[] mouth_nomal_speak = {
            R.drawable.mouth0
            , R.drawable.mouth_nomal_speak0
            , R.drawable.mouth_nomal_speak1
            , R.drawable.mouth_nomal_speak2
            , R.drawable.mouth_nomal_speak3
            , R.drawable.mouth_nomal_speak4
            , R.drawable.mouth_nomal_speak5
    };

    private int targetImage;
    private int targetCount = 0;

    private boolean start = false;

    public Mouth(Context context, float radio) {
        super(context);

        mRect.left = 0;
        mRect.right = (int) (340 * radio);
        mRect.top = 0;
        mRect.bottom = (int) (200 * radio);
        targetImage = R.drawable.mouth0;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Bitmap bm = BitmapFactory.decodeResource(getResources(), targetImage);
        canvas.drawBitmap(bm, null, mRect, mPaint);
    }

    /**
     * 返回正常值
     */
    public void stopNomalSpeak() {
        start = false;
        targetCount = 0;
        targetImage = R.drawable.mouth0;
        postInvalidate();
    }

    public void startNomalSpeak() {
        if (start) return;
        start = true;

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (start) {
                    targetCount++;
                    if (targetCount == mouth_nomal_speak.length) {
                        targetCount = 0;
                    }
                    targetImage = mouth_nomal_speak[targetCount];
                    try {
                        Thread.sleep(80);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    postInvalidate();
                }
            }
        }).start();
    }


}
