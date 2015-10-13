package com.example.face;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;

/**
 * 眼睛的定义 294-784
 *
 * @author dou
 */
public class Eye extends BasePart {


    //眨眼动作开关
    private boolean isEyeSight = false;
    //眼睛颤颤的开关
    private boolean isEyeShock = false;
    private int eyeShockRange = 0;
    private int[] eyeSight = {//眨眼动作
            R.drawable.eye_sight
            , R.drawable.eye_sight0
            , R.drawable.eye_sight1
            , R.drawable.eye_sight2
            , R.drawable.eye_sight3
            , R.drawable.eye_sight2
            , R.drawable.eye_sight1
            , R.drawable.eye_sight0
    };
    private int sightImage;
    private int sightCount = 0;

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
        sightImage = eyeSight[0];
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //眼珠
        mPaint.setColor(Color.BLACK);
        canvas.drawCircle(147 * radio, 147 * radio, 147 * radio, mPaint);//left eye
        canvas.drawCircle(637 * radio, 147 * radio, 147 * radio, mPaint);

        //眼白 初始状态
        mPaint.setColor(Color.WHITE);
        canvas.drawCircle(177 * radio + eyeShockRange, 117 * radio, 36 * radio, mPaint);//left point
        canvas.drawCircle(144 * radio + eyeShockRange, 162 * radio, 12 * radio, mPaint);

        canvas.drawCircle(607 * radio - eyeShockRange, 117 * radio, 36 * radio, mPaint);
        canvas.drawCircle(640 * radio - eyeShockRange, 162 * radio, 12 * radio, mPaint);

        //		//眼帘
        if (isEyeSight) {
            Bitmap eye = BitmapFactory.decodeResource(getResources(), sightImage);
            canvas.drawBitmap(eye, null, mRect, mPaint);//left sight
            canvas.drawBitmap(eye, null, mRect1, mPaint);
        }
    }


    public void startEyeShock(int parmas){

    }
    /**
     * 眼睛颤颤
     */
    public void startEyeShock() {

        if (isEyeShock) return;
        isEyeShock = true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (isEyeShock) {

                    if (eyeShockRange == 0) {
                        eyeShockRange = 2;
                    }
                    eyeShockRange = -eyeShockRange;
                    postInvalidate();
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    public void stopEyeShock() {
        if(!isEyeShock)return;
        isEyeShock = false;
        eyeShockRange = 0;
        postInvalidate();
    }

    //一个眨眼动作的完整描述
    public void startSight() {//眨眼
        if (isEyeSight) return;
        isEyeSight = true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (isEyeSight) {
                    if (sightCount == eyeSight.length * 3) {
                        sightImage = eyeSight[0];
                        postInvalidate();
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        sightCount = 0;
                    }
                    sightImage = eyeSight[sightCount % eyeSight.length];
                    postInvalidate();
                    sightCount++;

                    try {
                        if (sightCount == eyeSight.length * 2 + 1) Thread.sleep(300);
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    public void stopSight() {//停止眨眼
        if(!isEyeSight)return;
        isEyeSight = false;
        sightCount = 0;
        sightImage = eyeSight[0];
        postInvalidate();
    }


}
