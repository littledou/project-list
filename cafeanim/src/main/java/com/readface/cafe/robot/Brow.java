package com.readface.cafe.robot;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;

import com.readface.cafe.anim.R;

/**
 * 眉毛的定义 370-862
 *
 * @author dou
 */
public class Brow extends BasePart {


    private Rect mRect1;
    private int left, right;
    private Bitmap left_b, right_b;

    public Brow(Context context, float radio) {
        super(context, radio);

        mRect.left = 0;
        mRect.right = (int) (113 * radio);
        mRect.top = 0;
        mRect.bottom = (int) (71 * radio);

        mRect1 = new Rect();
        mRect1.left = (int) (178 * radio);
        mRect1.top = 0;
        mRect1.right = (int) (291 * radio);
        mRect1.bottom = (int) (71 * radio);
        nomalSide();
    }

    public void nomalSide() {
        left = R.mipmap.new_nomal_browl;
        right = R.mipmap.new_nomal_browr;
        enableBitmap();
        postInvalidate();
    }

    private void enableBitmap() {

        if (left_b != null && !left_b.isRecycled()) {
            left_b.recycle();
            left_b = null;
        }
        if (right_b != null && !right_b.isRecycled()) {
            right_b.recycle();
            right_b = null;
        }
        left_b = BitmapFactory.decodeResource(getResources(), left);
        right_b = BitmapFactory.decodeResource(getResources(), right);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (left_b != null)
            canvas.drawBitmap(left_b, null, mRect, mPaint);
        if (right_b != null)
            canvas.drawBitmap(right_b, null, mRect1, mPaint);

    }


    public void sad() {
        left = R.mipmap.brow_sad_left;
        right = R.mipmap.brow_sad_right;
        enableBitmap();
        postInvalidate();
    }
    public void cry() {
        left = R.mipmap.brow_cryl;
        right = R.mipmap.brow_cryr;
        enableBitmap();
        postInvalidate();
    }

    public void up() {
        left = R.mipmap.brow_up_left;
        right = R.mipmap.brow_up_right;
        enableBitmap();
        postInvalidate();
    }

    public void sub() {
        left = R.mipmap.brow_sub_left;
        right = R.mipmap.brow_sub_right;
        enableBitmap();
        postInvalidate();
    }


    public void comfort() {
        right = R.mipmap.brow_comfort_left;
        left = R.mipmap.brow_comfort_right;
        enableBitmap();
        postInvalidate();
    }

    public void smile3_1() {
        right = R.mipmap.brow_smile31_right;
        left = R.mipmap.brow_smile31_left;
        enableBitmap();
        postInvalidate();
    }

    public void smile3_2() {
        right = R.mipmap.brow_smile32_right;
        left = R.mipmap.brow_smile32_left;
        enableBitmap();
        postInvalidate();
    }

    public void ang1() {
        left = R.mipmap.brow_ang_left1;
        right = R.mipmap.brow_ang_right1;
        enableBitmap();
        postInvalidate();
    }

    public void ang2() {
        left = R.mipmap.brow_ang_left2;
        right = R.mipmap.brow_ang_right2;
        enableBitmap();
        postInvalidate();
    }

    public void angFire() {

    }

    public void kiss(){
        left = R.mipmap.brow_kiss1;
        right = R.mipmap.brow_kiss2;
        enableBitmap();
        postInvalidate();
    }
    public void smile4() {
        left = R.mipmap.brow_ang_right2;
        right = R.mipmap.brow_ang_left2;
        enableBitmap();
        postInvalidate();
    }


}
