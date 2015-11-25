package com.readface.cafe.robot;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import com.readface.cafe.anim.R;

/**
 * 嘴巴的定义340-200
 *
 * @author dou
 */
public class Mouth extends BasePart {


    private int mouthImage;
    private int mouthcount = 0;
    private boolean start = false;

    private int[] speak_arr;
    private int speed;

    public Mouth(Context context, float radio) {
        super(context, radio);

        mRect.left = 0;
        mRect.right = (int) (185 * radio);
        mRect.top = 0;
        mRect.bottom = (int) (113 * radio);
        nomalSide();
    }

    public void nomalSide() {
        mouthImage = R.mipmap.new_nomal_mouth;
        postInvalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Bitmap bm = BitmapFactory.decodeResource(getResources(), mouthImage);
        canvas.drawBitmap(bm, null, mRect, mPaint);
    }

    /**
     * 任意设置嘴部表情接口
     *
     * @param mouthImage
     */
    public void setMouthImage(int mouthImage) {
        this.mouthImage = mouthImage;
        postInvalidate();
    }

    /**
     * 返回正常
     */
    public void stopSpeak() {
        if (!start) return;
        handler.removeCallbacks(mRunnable);
        start = false;
        mouthcount = 0;
        nomalSide();
    }

    public void startSpeak(int mill, int[] speak_arr) {
        if (start) return;
        start = true;
        this.speak_arr = speak_arr;
        this.speed = mill;
        handler.removeCallbacks(mRunnable);
        handler.post(mRunnable);
    }

    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            mouthcount++;
            if (mouthcount == speak_arr.length) {
                mouthcount = 0;
            }
            mouthImage = speak_arr[mouthcount];
            postInvalidate();
            handler.postDelayed(mRunnable, speed);
        }
    };


}
