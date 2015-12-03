package com.readface.cafe.robot;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;

import com.readface.cafe.utils.SoundUtils;

/**
 * Created by mac on 15/11/23.
 */
public class Robot extends ViewGroup {

    Head mHead;
    Body mBody;
    private Context mContext;
    private float radio = 1f;
    private boolean isHead;

    public Robot(Context context, float radio) {
        super(context);
        this.mContext = context;
        this.radio = radio;
        initView(context);
    }

    private void initView(Context mContext) {
        mHead = new Head(mContext, radio);
        mBody = new Body(mContext, radio);
        addView(mBody);
        addView(mHead);

        mHead.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                SoundUtils.getIntense().playSound(7);
                initHeadAnim();
            }
        });
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        mHead.layout(0, 0, (int) (640 * radio), (int) (608 * radio));
        mBody.layout(0, (int) (585 * radio), (int) (640 * radio), (int) (809 * radio));
    }


    public Face getFace() {
        return mHead.getFace();
    }

    public void initLightAnim() {
        mBody.lightFly();
    }

    public void stopLightAnim() {
        mBody.stopLightFly();
    }

    public void initArmAnim() {
        mBody.initArmR();
        mBody.initArmL();
    }

    public void initAngleAnim() {
        mHead.angleAnim();
    }

    public void initHeadAnim() {
        if (isHead) return;
        isHead = true;
        Animation anim1 = new RotateAnimation(0, 8, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.8f);
        final Animation anim2 = new RotateAnimation(8, -8, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.8f);
        final Animation anim3 = new RotateAnimation(-8, 8, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.8f);
        final Animation anim4 = new RotateAnimation(8, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.8f);
        anim1.setDuration(200);
        anim2.setDuration(300);
        anim3.setDuration(300);
        anim4.setDuration(200);
        anim1.setFillAfter(true);

        mHead.startAnimation(anim1);
        postDelayed(new Runnable() {
            @Override
            public void run() {
                mHead.startAnimation(anim2);

            }
        }, 200);
        postDelayed(new Runnable() {
            @Override
            public void run() {
                mHead.startAnimation(anim3);

            }
        }, 500);
        postDelayed(new Runnable() {
            @Override
            public void run() {
                mHead.startAnimation(anim4);
                isHead = false;
            }
        }, 800);
    }


}
