package com.readface.cafe.anim;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Rect;

import java.util.ArrayList;
import java.util.List;

/**
 * 眼睛的定义 294-784
 *
 * @author dou
 */
public class Eye extends BasePart {


    //眨眼动作开关
    private int eyeMoveX = 0;
    private int eyeMoveY = 0;

    private int sightImage1, sightImage2;
    private Bitmap sightImage1_b, sightImage2_b;
    private boolean goho_ = false;
    int digress = 10;
    private Rect mRect1;

    public Eye(Context context, float radio) {
        super(context);

        mRect.left = 0;
        mRect.right = (int) (294 * radio);
        mRect.top = 0;
        mRect.bottom = (int) (294 * radio);

        mRect1 = new Rect();
        mRect1.left = (int) (490 * radio);
        mRect1.right = (int) (784 * radio);
        mRect1.top = 0;
        mRect1.bottom = (int) (294 * radio);

        this.radio = radio;
        sightImage1 = R.drawable.eye_sight;
        sightImage2 = R.drawable.eye_sight;

        enableLeftBitmap();
        enableRightBitmap();
    }


    private void enableRightBitmap() {

        if (sightImage2_b != null && !sightImage2_b.isRecycled()) {
            sightImage2_b.recycle();
            sightImage2_b = null;
        }

        sightImage2_b = BitmapFactory.decodeResource(getResources(), sightImage2);
    }

    private void enableLeftBitmap() {
        if (sightImage1_b != null && !sightImage1_b.isRecycled()) {
            sightImage1_b.recycle();
            sightImage1_b = null;
        }
        sightImage1_b = BitmapFactory.decodeResource(getResources(), sightImage1);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //眼珠
        mPaint.setColor(Color.BLACK);
        canvas.drawCircle(147 * radio, 147 * radio, 147 * radio, mPaint);//left eye
        canvas.drawCircle(637 * radio, 147 * radio, 147 * radio, mPaint);

        if (goho_) {
            Matrix matrix = new Matrix();
            matrix.postRotate(digress);
            Bitmap _b = Bitmap.createBitmap(sightImage1_b, 0, 0, sightImage1_b.getWidth(), sightImage1_b.getHeight(), matrix, true);
            canvas.drawBitmap(_b, null, mRect, mPaint);//left sight
            canvas.drawBitmap(_b, null, mRect1, mPaint);
            digress += 10;

        } else {
            //眼白 初始状态
            mPaint.setColor(Color.WHITE);
            canvas.drawCircle(177 * radio + eyeMoveX, 117 * radio + eyeMoveY, 36 * radio, mPaint);//left point
            canvas.drawCircle(144 * radio + eyeMoveX, 162 * radio + eyeMoveY, 12 * radio, mPaint);

            canvas.drawCircle(607 * radio - eyeMoveX, 117 * radio - eyeMoveY, 36 * radio, mPaint);
            canvas.drawCircle(640 * radio - eyeMoveX, 162 * radio - eyeMoveY, 12 * radio, mPaint);
            //眼帘
            canvas.drawBitmap(sightImage1_b, null, mRect, mPaint);//left sight

            canvas.drawBitmap(sightImage2_b, null, mRect1, mPaint);
        }
        eyeMoveX = 0;
        eyeMoveY = 0;
        System.gc();

    }

    public void setEyeMove(int x, int y) {
        eyeMoveX = x;
        eyeMoveY = y;
        invalidate();
    }


    public void allEnable(int resId, boolean left, boolean right) {
        goho_ = false;
        digress = 0;
        if (left) {
            sightImage1 = resId;
            enableLeftBitmap();
        }
        if (right) {
            sightImage2 = resId;
            enableRightBitmap();
        }
        postInvalidate();
    }


    public void playGosh() {
        if (!goho_) {
            goho_ = true;
            sightImage1 = R.drawable.eye_gosh;
            enableLeftBitmap();

        }
        invalidate();
    }
}
