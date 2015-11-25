package com.readface.cafe.robot;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;

import com.readface.cafe.anim.R;

/**
 * 眼睛的定义 294-784
 *
 * @author dou
 */
public class Eye extends BasePart {


    //眨眼动作开关

    private boolean isGosh = false;
    private boolean isLeft = false;
    private boolean isSign = false;
    private boolean noPoint = false;

    private boolean close;

    private Bitmap eyeBgBitmap;
    private int eyeBg = 0;

    private Bitmap eyeSignBitmap;
    private int eyeSign = 0;

    private int eyemove = 0;

    public Eye(Context context, float radio, boolean isLeft) {
        super(context, radio);
        this.isLeft = isLeft;
        mRect.left = 0;
        mRect.right = (int) (113 * radio);
        mRect.top = 0;
        mRect.bottom = (int) (113 * radio);

        mPaint.setColor(Color.WHITE);
        mPaint1.setColor(0x66ffffff);
        nomalSide();
    }

    public void nomalSide() {
        if (isLeft) {
            eyeBg = R.mipmap.new_nomal_eyel;
        } else {
            eyeBg = R.mipmap.new_nomal_eyer;
        }

        nomalContral();
    }

    private void nomalContral() {
        if (eyeBgBitmap != null && !eyeBgBitmap.isRecycled()) {
            eyeBgBitmap.recycle();
            eyeBgBitmap = null;
        }
        eyeBgBitmap = BitmapFactory.decodeResource(getResources(), eyeBg);
        postInvalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawBitmap(eyeBgBitmap, null, mRect, mPaint);//left sight
        if (!isGosh && !noPoint) {
            if (isLeft) {
                canvas.drawCircle(70 * radio - eyemove, 50 * radio, 10 * radio, mPaint);
                canvas.drawCircle(60 * radio - eyemove, 68 * radio, 5 * radio, mPaint1);
            } else {
                canvas.drawCircle(46 * radio + eyemove, 50 * radio, 10 * radio, mPaint);
                canvas.drawCircle(58 * radio + eyemove, 68 * radio, 5 * radio, mPaint1);
            }
        }

        if (isSign) {
            canvas.drawBitmap(eyeSignBitmap, null, mRect, mPaint);//left sight
        }
        isGosh = false;
        isSign = false;
        noPoint = false;
        eyemove = 0;
    }


    public void setEyeMove(int x) {
        eyemove = x;
        invalidate();
    }


    public void enableRes(int resId) {
        eyeBg = resId;
        nomalContral();
    }

    public void sign(int resId) {
        isSign = true;
        eyeSign = resId;

        if (eyeSignBitmap != null && !eyeSignBitmap.isRecycled()) {
            eyeSignBitmap.recycle();
            eyeSignBitmap = null;
        }
        eyeSignBitmap = BitmapFactory.decodeResource(getResources(), eyeSign);
        invalidate();
    }

    public void cry() {
        noPoint = true;
        if (isLeft) {
            eyeBg = R.mipmap.eye_cryl;
        } else {
            eyeBg = R.mipmap.eye_cry_r;
        }
        nomalContral();
    }

    public void enAng(int resId) {
        noPoint = true;
        eyeBg = resId;
        nomalContral();
    }

    public void playGosh() {
        if (isLeft) {
            eyeBg = R.mipmap.new_nomal_eyesidel;
        } else {
            eyeBg = R.mipmap.new_nomal_eyesider;
        }
        isGosh = true;
        nomalContral();
    }

    public void enableKiss(int resId) {

        postInvalidate();
    }

    public boolean isClose() {
        return close;
    }

    public void setClose(boolean close) {
        this.close = close;
    }


}
