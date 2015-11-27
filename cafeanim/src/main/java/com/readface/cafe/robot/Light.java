package com.readface.cafe.robot;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

import com.readface.cafe.anim.R;

/**
 * Created by mac on 15/11/23.
 */
public class Light extends View {

    int defaultImage = R.mipmap.light;
    int lightFly[] = {
            R.drawable.light_fly1,
            R.drawable.light_fly2,
            R.drawable.light_fly3,
            R.drawable.light_fly4,
            R.drawable.light_fly5,
            R.drawable.light_fly6
    };
    private boolean isFly = false;
    private Rect mRect;
    protected Paint mPaint;

    private int count = 0;

    public Light(Context context, float radio) {
        super(context);
        setBackgroundResource(R.mipmap.light);
        mRect = new Rect();
        mRect.left = 0;
        mRect.top = 0;
        mRect.right = (int) (radio * 70);
        mRect.bottom = (int) (radio * 70);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(1.0f);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isFly) {
            Bitmap bm = BitmapFactory.decodeResource(getResources(), lightFly[count % lightFly.length]);
            canvas.drawBitmap(bm, null, mRect, mPaint);
        }
    }

    public void startFly() {
        if (isFly) return;
        isFly = true;
        post(new Runnable() {
            @Override
            public void run() {

                if (isFly) {
                    postInvalidate();
                    count++;
                    if (count >= 40) {
                        isFly = false;
                        count = 0;
                    }
                    postDelayed(this, 50);
                }
            }
        });
    }
}
