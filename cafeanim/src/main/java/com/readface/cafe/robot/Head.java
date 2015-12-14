package com.readface.cafe.robot;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;

import com.readface.cafe.utils.SoundUtils;

/**
 * Created by mac on 15/11/23.
 */
public class Head extends ViewGroup {

    //angle head
    private float radio = 1f;
    private Face mFace;
    private Angle mAngle;
    boolean isAngle = false;

    private int count = 0;

    public Head(Context context, float radio) {
        super(context);
        this.radio = radio;

        mAngle = new Angle(context, radio);
        mFace = new Face(context, radio);
        addView(mAngle);
        addView(mFace);
        mAngle.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                count++;
                if (count >= 5) {
                    mFace.ang2(true);
                    count = 0;
                }
                SoundUtils.getIntense().playSound(7);
                angleAnim();
            }
        });
        post(new Runnable() {
            @Override
            public void run() {
                count = 0;
                postDelayed(this, 5000);
            }
        });
    }

    public void angleAnim() {
        if (isAngle) return;
        isAngle = true;
        Animation anim1 = new RotateAnimation(0, 30, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 1f);
        final Animation anim2 = new RotateAnimation(30, -30, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 1f);
        final Animation anim3 = new RotateAnimation(-30, 30, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 1f);
        final Animation anim4 = new RotateAnimation(30, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 1f);
        anim1.setDuration(150);
        anim2.setDuration(150);
        anim3.setDuration(150);
        anim4.setDuration(150);
        anim1.setFillAfter(true);

        mAngle.startAnimation(anim1);
        postDelayed(new Runnable() {
            @Override
            public void run() {
                mAngle.startAnimation(anim2);

            }
        }, 150);
        postDelayed(new Runnable() {
            @Override
            public void run() {
                mAngle.startAnimation(anim3);

            }
        }, 300);
        postDelayed(new Runnable() {
            @Override
            public void run() {
                mAngle.startAnimation(anim4);
                isAngle = false;

            }
        }, 450);

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        mAngle.layout((int) (277 * radio), 0, (int) (363 * radio), (int) (160 * radio));
        mFace.layout(0, (int) (135 * radio), (int) (640 * radio), (int) (608 * radio));
    }


    public Face getFace() {
        return mFace;
    }
}
