package com.readface.cafe.robot;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.view.View;

/**
 * 基本部位
 *
 * @author dou
 */
public abstract class BasePart extends View {

    protected Paint mPaint;
    protected Paint mPaint1;
    protected Rect mRect;

    protected Handler handler = new Handler();
    protected float radio;

    public BasePart(Context context, float radio) {
        super(context);
        this.radio = radio;
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(1.0f);
        mPaint1 = new Paint();
        mPaint1.setAntiAlias(true);
        mPaint1.setStrokeWidth(1.0f);

        mRect = new Rect();
    }

}
