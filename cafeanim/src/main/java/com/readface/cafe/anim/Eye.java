package com.readface.cafe.anim;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;

/**
 * 眼睛的定义 294-784
 *
 * @author dou
 */
public class Eye extends BasePart {


    //眨眼动作开关
    private int eyeMoveX = 0;
    private int eyeMoveY = 0;
    private int eyeCryMoveX = 0;
    private int eyeCryMoveY = 0;

    private int sightImage;
    private Bitmap sightImage_b;
    private boolean isGosh = false;
    private boolean isLeft = false;

    private boolean close;
    private boolean sad_cry = false;

    public Eye(Context context, float radio, boolean isLeft) {
        super(context);
        this.isLeft = isLeft;
        mRect.left = 0;
        mRect.right = (int) (294 * radio);
        mRect.top = 0;
        mRect.bottom = (int) (294 * radio);


        this.radio = radio;
        sightImage = R.drawable.eye_sight;

        enableBitmap();
    }


    private void enableBitmap() {
        if (sightImage_b != null && !sightImage_b.isRecycled()) {
            sightImage_b.recycle();
            sightImage_b = null;
        }
        sightImage_b = BitmapFactory.decodeResource(getResources(), sightImage);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //眼珠
        mPaint.setColor(Color.BLACK);
        canvas.drawCircle(147 * radio, 147 * radio, 147 * radio, mPaint);//left eye

        //眼白 初始状态
        mPaint.setColor(Color.WHITE);
        if (!isGosh) {
            if (sad_cry) {
                if (isLeft) {
                    canvas.drawCircle(177 * radio - eyeCryMoveX, 107 * radio, 60 * radio, mPaint);//left point
                    canvas.drawCircle(87 * radio - eyeCryMoveX, 177 * radio, 25 * radio, mPaint);
                    canvas.drawCircle(147 * radio, 197 * radio+eyeCryMoveX, 15 * radio, mPaint);

                } else {
                    canvas.drawCircle(147 * radio, 197 * radio+eyeCryMoveX, 15 * radio, mPaint);
                    canvas.drawCircle(207 * radio + eyeCryMoveX, 177 * radio, 25 * radio, mPaint);
                    canvas.drawCircle(117 * radio + eyeCryMoveX, 107 * radio, 60 * radio, mPaint);
                }
            } else {
                if (isLeft) {
                    canvas.drawCircle(177 * radio + eyeMoveX, 117 * radio + eyeMoveY, 36 * radio, mPaint);//left point
                    canvas.drawCircle(144 * radio + eyeMoveX, 162 * radio + eyeMoveY, 12 * radio, mPaint);
                } else {
                    canvas.drawCircle(117 * radio + eyeMoveX, 117 * radio + eyeMoveY, 36 * radio, mPaint);
                    canvas.drawCircle(150 * radio + eyeMoveX, 162 * radio + eyeMoveY, 12 * radio, mPaint);
                }
            }
        }

        //眼帘
        if (sightImage_b != null)
            canvas.drawBitmap(sightImage_b, null, mRect, mPaint);//left sight

        eyeMoveX = 0;
        eyeMoveY = 0;
        eyeCryMoveX = 0;
        eyeCryMoveY = 0;
        sad_cry = false;
    }

    public void setEyeCry(int x, int y) {
        sad_cry = true;
        eyeCryMoveX = x;
        eyeCryMoveY = y;
        invalidate();
    }

    public void setEyeMove(int x, int y) {
        eyeMoveX = x;
        eyeMoveY = y;
        invalidate();
    }


    public void enableRes(int resId) {
        isGosh = false;
        sightImage = resId;
        enableBitmap();
        postInvalidate();
    }


    public void playGosh() {
        isGosh = true;
        sightImage = R.drawable.eye_gosh;
        enableBitmap();

        invalidate();
    }

    public boolean isClose() {
        return close;
    }

    public void setClose(boolean close) {
        this.close = close;
    }
}
