package com.example.face;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

/**
 * 嘴巴的定义340-200
 *
 * @author dou
 */
public class Mouth extends BasePart {




    private int mouthImage;
    private int mouthcount = 0;



    private boolean start = false;

    public Mouth(Context context, float radio) {
        super(context);

        mRect.left = 0;
        mRect.right = (int) (340 * radio);
        mRect.top = 0;
        mRect.bottom = (int) (200 * radio);
        mouthImage = R.drawable.mouth0;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Bitmap bm = BitmapFactory.decodeResource(getResources(), mouthImage);
        canvas.drawBitmap(bm, null, mRect, mPaint);
    }

    /**
     * 任意设置嘴部表情接口
     * @param mouthImage
     */
    public void setMouthImage(int mouthImage) {
        this.mouthImage = mouthImage;
        postInvalidate();
    }

    /**
     * 返回正常值
     */
    public void stopSpeak() {
        if(!start)return;
        start = false;
        mouthcount = 0;
        mouthImage = R.drawable.mouth0;
        postInvalidate();
    }

    public void startSpeak(final int mill,final int[] mouth_nomal_speak) {
        if (start) return;
        start = true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (start) {
                    mouthcount++;
                    if (mouthcount == mouth_nomal_speak.length) {
                        mouthcount = 0;
                    }
                    mouthImage = mouth_nomal_speak[mouthcount];
                    postInvalidate();
                    try {
                        Thread.sleep(mill);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }


}
