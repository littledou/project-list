package com.readface.cafe.anim;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.widget.RelativeLayout;

import com.readface.cafe.utils.ResUtils;

/**
 * Face 包括腮红1，眉毛1，眼睛1，嘴巴1
 */
public class Face extends RelativeLayout {

    private Blush blushLeft, blushRight;
    private Brow brow;
    private Eye eye;
    private Mouth mouth;

    private float radio = 1f;

    /**
     * 0:喜悦表情
     * 1:愤怒表情
     * 2:惊讶表情
     * 3:眼白移动
     * 4:眼睛闭合
     * 5:眼睛睁开
     * 6:眩晕
     */
    private boolean[] animTag;//长度就代表几种动画
    private final int tag_count = 7;
    private int eye_shock_size = 2;

    private int digress = 0;
    private int count_emo = 0;
    private int countEye = 0;
    private int countMouthGosh = 0;


    public Face(Context context, float radio) {
        super(context);
        this.radio = radio;
        initPart(context);

        animTag = new boolean[tag_count];
    }

    private void initPart(Context context) {
        //添加腮红
        blushLeft = new Blush(context, radio, BasePart.LEFT);
        blushRight = new Blush(context, radio, BasePart.RIGHT);
        addView(blushLeft);
        addView(blushRight);

        eye = new Eye(context, radio);
        addView(eye);


        brow = new Brow(context, radio);
        addView(brow);

        mouth = new Mouth(context, radio);
        addView(mouth);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        super.onLayout(changed, l, t, r, b);

        blushLeft.layout((int) (72 * radio), (int) (360 * radio), (int) (372 * radio), (int) (560 * radio));
        blushRight.layout((int) (708 * radio), (int) (360 * radio), (int) (1008 * radio), (int) (560 * radio));

        eye.layout((int) (150 * radio), (int) (175 * radio), (int) (934 * radio), (int) (469 * radio));

        brow.layout((int) (110 * radio), (int) (33 * radio), (int) (972 * radio), (int) (164 * radio));

        mouth.layout((int) (370 * radio), (int) (512 * radio), (int) (710 * radio), (int) (710 * radio));
    }


    public void action1() {//眨眼睛1：刚启动系统
        mouth.setMouthImage(R.drawable.mouth_smile2);
    }

    private void cleanEye() {
        eye.allEnable(R.drawable.eye_sight, true, true);
    }

    public void animCloseEye(final boolean left, final boolean right) {
        animTag[4] = true;
        countEye = 0;
        postDelayed(new Runnable() {
            @Override
            public void run() {
                if (animTag[4]) {

                    if (countEye < ResUtils.openEye.length) {
                        eye.allEnable(ResUtils.openEye[countEye], left, right);
                        countEye++;
                        postDelayed(this, 50);
                    }
                }
            }
        }, 50);
    }

    public void animOpenEye(final boolean left, final boolean right) {
        animTag[5] = true;
        countEye = ResUtils.openEye.length - 1;
        postDelayed(new Runnable() {
            @Override
            public void run() {
                if (animTag[5]) {

                    if (countEye >= 0) {
                        eye.allEnable(ResUtils.openEye[countEye], left, right);
                        countEye--;
                        postDelayed(this, 50);
                    }
                }
            }
        }, 50);
    }


    private void animShockEye() {//眼睛颤动，，可怜样
        animTag[3] = true;

        post(new Runnable() {
            @Override
            public void run() {
                if (animTag[3]) {
                    eye.setEyeMove(eye_shock_size, 0);
                    eye_shock_size = -eye_shock_size;
                    postDelayed(this, 40);
                }
            }
        });
    }

    public void animGosh() {
        cleanALlAction();
        animTag[6] = true;
        digress = 0;
        brow.ang1();
        mouth.post(new Runnable() {
            @Override
            public void run() {
                if (animTag[6]) {
                    if (countMouthGosh >= 4) {
                        countMouthGosh = 0;
                    }
                    mouth.setMouthImage(ResUtils.goshMouth[countMouthGosh]);
                    countMouthGosh++;
                    mouth.postDelayed(this, 200);
                }
            }
        });

        post(new Runnable() {
            @Override
            public void run() {
                if (animTag[6]) {
                    eye.playGosh();
                    postDelayed(this, 100);
                }
            }
        });
    }

    public void cleanALlAction() {//清除处理事件
        for (int i = 0; i < animTag.length; i++) {
            animTag[i] = false;
        }
        brow.nomal();//眉毛恢复正常
        mouth.setMouthImage(R.drawable.mouth0);//嘴巴恢复正常
        cleanEye();
    }

    public void emo0() {//joy
        cleanALlAction();
        animTag[0] = true;
        post(new Runnable() {
            @Override
            public void run() {
                if (animTag[0]) {//眉毛跳动，嘴巴跳动
                    if (count_emo == 0) {
                        count_emo = 1;
                        brow.up();
                        mouth.setMouthImage(R.drawable.mouth_smile3);
                    } else {
                        count_emo = 0;
                        brow.nomal();
                        mouth.setMouthImage(R.drawable.mouth_smile1);
                    }
                    postDelayed(this, 120);
                }
            }
        });
    }


    public void emo1() {//sad
        cleanALlAction();
        animShockEye();
        mouth.setMouthImage(R.drawable.mouth_sad2);
        brow.sad();
    }

    public void emo2() {//fear

    }

    public void emo3() {//anger
        cleanALlAction();
        animTag[1] = true;
        animShockEye();
        mouth.setMouthImage(R.drawable.mouth_ang);
        post(new Runnable() {
            @Override
            public void run() {
                if (animTag[1]) {
                    if (count_emo == 0) {
                        count_emo = 1;
                        brow.ang1();
                    } else {
                        count_emo = 0;
                        brow.ang2();
                    }
                    postDelayed(this, 120);
                }
            }
        });
    }


    public void emo4() {//surp
        cleanALlAction();
        animTag[2] = true;
        post(new Runnable() {
            @Override
            public void run() {
                if (animTag[2]) {//眉毛跳动，嘴巴跳动
                    if (count_emo == 0) {
                        count_emo = 1;
                        brow.sub1();
                        mouth.setMouthImage(R.drawable.mouth_sub1);
                    } else {
                        count_emo = 0;
                        brow.sub2();
                        mouth.setMouthImage(R.drawable.mouth_sub2);
                    }
                    postDelayed(this, 120);
                }
            }
        });
    }


    public void emo5() {//disg

    }

    public void emo6() {//nomal
        cleanALlAction();

    }


}
