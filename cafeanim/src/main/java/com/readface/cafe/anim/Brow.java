package com.readface.cafe.anim;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;

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
        super(context);

        mRect.left = 0;
        mRect.right = (int) (370 * radio);
        mRect.top = 0;
        mRect.bottom = (int) (131 * radio);
        mRect1 = new Rect();
        mRect1.left = (int) (492 * radio);
        mRect1.right = (int) (862 * radio);
        mRect1.top = 0;
        mRect1.bottom = (int) (131 * radio);
        this.radio = radio;
        left = R.drawable.browleft0;
        right = R.drawable.browright0;
        enableBitmap();
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

    public void nomal() {
        left = R.drawable.browleft0;
        right = R.drawable.browright0;
        enableBitmap();
        postInvalidate();
    }

    public void sad() {
        left = R.drawable.brow_sad_left;
        right = R.drawable.brow_sad_right;
        enableBitmap();
        postInvalidate();
    }

    public void up() {
        left = R.drawable.brow_up_left;
        right = R.drawable.brow_up_right;
        enableBitmap();
        postInvalidate();
    }

    public void sub1() {
        left = R.drawable.brow_sub_left1;
        right = R.drawable.brow_sub_right1;
        enableBitmap();
        postInvalidate();
    }

    public void sub2() {
        left = R.drawable.brow_sub_left2;
        right = R.drawable.brow_sub_right2;
        enableBitmap();
        postInvalidate();
    }

    public void comfort() {
//        right = R.drawable.brow_comfort_left;
//        left = R.drawable.brow_comfort_right;
        enableBitmap();
        postInvalidate();
    }

    public void smile3() {
//        right = R.drawable.brow_smile3_left;
//        left = R.drawable.brow_smile3_right;
        enableBitmap();
        postInvalidate();
    }

    public void ang1() {
        left = R.drawable.brow_ang_left1;
        right = R.drawable.brow_ang_right1;
        enableBitmap();
        postInvalidate();
    }

    public void ang2() {
        left = R.drawable.brow_ang_left2;
        right = R.drawable.brow_ang_right2;
        enableBitmap();
        postInvalidate();
    } public void smile4() {
        left = R.drawable.brow_ang_right2;
        right = R.drawable.brow_ang_left2;
        enableBitmap();
        postInvalidate();
    }

}
