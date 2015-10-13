package com.example.face;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

/**
 * 眼睛的定义 294-294
 *
 * @author dou
 */
public class Eye extends BasePart {


    //眨眼动作开关
    private boolean isEyeSight = false;
    private int[] eyeSight = {//眨眼动作
            R.drawable.eye_sight0
            , R.drawable.eye_sight1
            , R.drawable.eye_sight2
            , R.drawable.eye_sight3
    };
    private int sightImage;
    private int sightCount = 0;

    public Eye(Context context, float radio, int direc) {
        super(context);

        mRect.left = 0;
        mRect.right = (int) (294 * radio);
        mRect.top = 0;
        mRect.bottom = (int) (294 * radio);
        this.direc = direc;
        this.radio = radio;
        sightImage = eyeSight[0];
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //眼珠
        mPaint.setColor(Color.BLACK);
        canvas.drawCircle(147 * radio, 147 * radio, 147 * radio, mPaint);

        //眼白 初始状态
        mPaint.setColor(Color.WHITE);
        if (direc == LEFT) {
            canvas.drawCircle(177 * radio, 117 * radio, 36 * radio, mPaint);
            canvas.drawCircle(144 * radio, 162 * radio, 12 * radio, mPaint);
        } else if (direc == RIGHT) {
            canvas.drawCircle(117 * radio, 117 * radio, 37 * radio, mPaint);
            canvas.drawCircle(150 * radio, 162 * radio, 12 * radio, mPaint);
        }

        //		//眼帘
        if (isEyeSight) {
            Bitmap eye = BitmapFactory.decodeResource(getResources(), sightImage);
            canvas.drawBitmap(eye, null, mRect, mPaint);
        }
    }

    //可怜
    public void startSight() {//
        if(isEyeSight)return;
        isEyeSight = true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (isEyeSight) {
                    sightCount++;
                    if(sightCount==eyeSight.length)sightCount=0;
                    sightImage = eyeSight[sightCount];

                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    postInvalidate();
                }
            }
        }).start();
    }

    public void stopSight() {//停止眨眼
        isEyeSight = false;
        sightCount = 0;
        sightImage= eyeSight[0];
        postInvalidate();
    }

}
