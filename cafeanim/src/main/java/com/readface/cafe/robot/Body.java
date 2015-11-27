package com.readface.cafe.robot;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;


/**
 * Created by mac on 15/11/23.
 * 肚子,灯和两只手
 */
public class Body extends ViewGroup {
    private float radio = 1f;

    private Arm armL, armR;
    private Tripe mTripe;
    private Light mLight;
    boolean armLShock = false, armRShock = false;

    public Body(Context context, float radio) {
        super(context);

        this.radio = radio;

        armL = new Arm(context, true);
        armR = new Arm(context, false);

        addView(armL);
        addView(armR);

        mTripe = new Tripe(context);
        addView(mTripe);

        mLight = new Light(context, radio);
        addView(mLight);

        mLight.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mLight.startFly();
            }
        });

        armL.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                initArmL();


            }
        });
        armR.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                initArmR();
            }
        });

    }

    void initArmL() {
        if (armLShock) return;
        armLShock = true;
        Animation anim1 = new RotateAnimation(0, 20, Animation.RELATIVE_TO_SELF, 1f, Animation.RELATIVE_TO_SELF, 0f);
        final Animation anim2 = new RotateAnimation(20, 0, Animation.RELATIVE_TO_SELF, 1f, Animation.RELATIVE_TO_SELF, 0f);
        final Animation anim3 = new RotateAnimation(0, 20, Animation.RELATIVE_TO_SELF, 1f, Animation.RELATIVE_TO_SELF, 0f);
        final Animation anim4 = new RotateAnimation(20, 0, Animation.RELATIVE_TO_SELF, 1f, Animation.RELATIVE_TO_SELF, 0f);

        anim1.setDuration(300);
        anim2.setDuration(150);
        anim3.setDuration(150);
        anim4.setDuration(300);
        anim1.setFillAfter(true);
        armL.startAnimation(anim1);

        postDelayed(new Runnable() {
            @Override
            public void run() {
                armL.startAnimation(anim2);
            }
        }, 300);
        postDelayed(new Runnable() {
            @Override
            public void run() {
                armL.startAnimation(anim3);
            }
        }, 450);
        postDelayed(new Runnable() {
            @Override
            public void run() {
                armL.startAnimation(anim4);
                armLShock = false;
            }
        }, 600);
    }

    void initArmR() {
        if (armRShock) return;
        armRShock = true;
        Animation anim1 = new RotateAnimation(0, -20, Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f);
        final Animation anim2 = new RotateAnimation(-20, 0, Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f);
        final Animation anim3 = new RotateAnimation(0, -20, Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f);
        final Animation anim4 = new RotateAnimation(-20, 0, Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f);
        anim1.setDuration(300);
        anim2.setDuration(150);
        anim3.setDuration(150);
        anim4.setDuration(300);
        anim1.setFillAfter(true);
        armR.startAnimation(anim1);

        postDelayed(new Runnable() {
            @Override
            public void run() {
                armR.startAnimation(anim2);
            }
        }, 300);
        postDelayed(new Runnable() {
            @Override
            public void run() {
                armR.startAnimation(anim3);
            }
        }, 450);
        postDelayed(new Runnable() {
            @Override
            public void run() {
                armR.startAnimation(anim4);
                armRShock = false;
            }
        }, 600);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        armL.layout((int) (180 * radio), 0, (int) (275 * radio), (int) (155 * radio));
        armR.layout((int) (380 * radio), 0, (int) (470 * radio), (int) (155 * radio));
        mTripe.layout((int) (224 * radio), 0, (int) (421 * radio), (int) (226 * radio));
        mLight.layout((int) (285 * radio), (int) (55 * radio), (int) (355 * radio), (int) (125 * radio));
    }
}
