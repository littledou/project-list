package com.readface.cafe.anim;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import com.readface.cafe.utils.ResUtils;

/**
 * Face 包括腮红1，眉毛1，眼睛1，嘴巴1
 */
public class Face extends RelativeLayout {

    private Blush blushLeft, blushRight;
    private Brow brow;
    private Eye eye, eyeRight;
    private Mouth mouth;
    private Context mContext;
    private float radio = 1f;

    /**
     * 0:喜悦表情
     * 1:愤怒表情
     * 2:惊讶表情
     * 3:眼白移动
     * 4:左眼睛闭合
     * 5:右眼睛闭合
     * 6:左眼睛睁开
     * 7:右眼睛睁开
     * 8:眩晕
     * 9:眨眼
     * 10:眼泪
     * 11:连续眨眼
     * 12:腮红颤动
     * 13:smile 1
     * 14: 困
     */
    private boolean[] animTag;//长度就代表几种动画
    private final int tag_count = 15;
    private int eye_shock_size = 2;
    private int blush_shock_size = 2;

    private int eye_cry_size = 6;

    private int count_emo = 0;
    private int countEye = 0;
    private int sadCount = 0;
    private int countEyeRight = 0;
    private int countMouthGosh = 0;
    private int countSmile = 0;
    private int countTrapped = 0;
    private int digress = 60;
    private boolean isCloseAllEye = false;

    public Face(Context context, float radio) {
        super(context);
        this.mContext = context;
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

        eye = new Eye(context, radio, true);
        addView(eye);
        eyeRight = new Eye(context, radio, false);
        addView(eyeRight);

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

        eye.layout((int) (150 * radio), (int) (175 * radio), (int) (444 * radio), (int) (469 * radio));
        eyeRight.layout((int) (640 * radio), (int) (175 * radio), (int) (934 * radio), (int) (469 * radio));

        brow.layout((int) (110 * radio), (int) (33 * radio), (int) (972 * radio), (int) (164 * radio));

        mouth.layout((int) (370 * radio), (int) (512 * radio), (int) (710 * radio), (int) (710 * radio));
    }


    public void action1() {//眨眼睛1：刚启动系统
        mouth.setMouthImage(R.drawable.mouth_smile2);
    }

    private void cleanEye() {
        eye.enableRes(R.drawable.eye_sight);
        eyeRight.enableRes(R.drawable.eye_sight);
        if (animTag[8]) {
            try {
                eye.animate().rotation(0).setDuration(10);
                eyeRight.animate().rotation(0).setDuration(10);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        digress = 0;
        eye.setClose(false);
        eyeRight.setClose(false);
    }

    public void animCloseLeftEye() {
        if (animTag[4] || eye.isClose()) return;

        if (!eyeRight.isClose())
            cleanALlAction();
        animTag[4] = true;
        countEye = 0;
        postDelayed(new Runnable() {
            @Override
            public void run() {
                if (animTag[4]) {
                    if (countEye < ResUtils.openEye.length) {
                        eye.enableRes(ResUtils.openEye[countEye]);
                        countEye++;
                        postDelayed(this, 50);
                    } else {
                        animTag[4] = false;
                        eye.setClose(true);
                    }
                }
            }
        }, 50);
    }


    public void animCloseRightEye() {
        if (animTag[5] || eyeRight.isClose()) return;
        if (!eye.isClose() && !isCloseAllEye) {
            cleanALlAction();
        }
        isCloseAllEye = false;
        animTag[5] = true;
        countEyeRight = 0;
        postDelayed(new Runnable() {
            @Override
            public void run() {
                if (animTag[5]) {

                    if (countEyeRight < ResUtils.openEye.length) {
                        eyeRight.enableRes(ResUtils.openEye[countEyeRight]);
                        countEyeRight++;
                        postDelayed(this, 50);
                    } else {
                        animTag[5] = false;
                        eyeRight.setClose(true);
                    }
                }
            }
        }, 50);
    }

    public void animOpenLeftEye() {
        if (animTag[6] || !eye.isClose()) return;
        animTag[6] = true;
        countEye = ResUtils.openEye.length - 1;
        postDelayed(new Runnable() {
            @Override
            public void run() {
                if (animTag[6]) {

                    if (countEye >= 0) {
                        eye.enableRes(ResUtils.openEye[countEye]);
                        countEye--;
                        postDelayed(this, 50);
                    } else {
                        animTag[6] = false;
                        eye.setClose(false);
                    }
                }
            }
        }, 50);
    }

    public void animOpenRightEye() {
        if (animTag[7] || !eyeRight.isClose()) return;
        animTag[7] = true;
        countEyeRight = ResUtils.openEye.length - 1;
        postDelayed(new Runnable() {
            @Override
            public void run() {
                if (animTag[7]) {

                    if (countEyeRight >= 0) {
                        eyeRight.enableRes(ResUtils.openEye[countEyeRight]);
                        countEyeRight--;
                        postDelayed(this, 50);
                    } else {
                        animTag[7] = false;
                        eyeRight.setClose(false);
                    }
                }
            }
        }, 50);
    }

    public void closeAllEye() {
        isCloseAllEye = true;
        animCloseLeftEye();
        animCloseRightEye();
    }

    public void openAllEye() {
        animOpenLeftEye();
        animOpenRightEye();
    }

    private void animShockEye() {//眼睛颤动，，可怜样
        if (animTag[3]) return;
        animTag[3] = true;

        post(new Runnable() {
            @Override
            public void run() {
                if (animTag[3]) {
                    eye.setEyeMove(eye_shock_size, 0);
                    eyeRight.setEyeMove(-eye_shock_size, 0);
                    eye_shock_size = -eye_shock_size;
                    postDelayed(this, 80);
                }
            }
        });
    }

    private void animShockBlush() {//腮红颤动

        if (animTag[12]) return;
        animTag[12] = true;

        post(new Runnable() {
            @Override
            public void run() {
                if (animTag[12]) {
                    blushLeft.animate().translationX(blush_shock_size).setDuration(80);
                    blushRight.animate().translationX(-blush_shock_size).setDuration(80);
                    blush_shock_size = -blush_shock_size;
                    postDelayed(this, 80);
                }
            }
        });
    }

    public void animGosh() {
        if (animTag[8]) return;
        cleanALlAction();
        animTag[8] = true;
        digress = 0;
        brow.ang1();
        eye.playGosh();
        eyeRight.playGosh();
        mouth.post(new Runnable() {
            @Override
            public void run() {
                if (animTag[8]) {
                    if (countMouthGosh >= 4) {
                        countMouthGosh = 0;
                    }
                    mouth.setMouthImage(ResUtils.goshMouth[countMouthGosh]);
                    countMouthGosh++;

                    eye.animate().rotation(digress).setDuration(120);
                    eyeRight.animate().rotation(digress).setDuration(120);
                    digress += 60;
                    mouth.postDelayed(this, 120);
                }
            }
        });

    }

    public void cleanALlAction() {//清除处理事件

        brow.nomal();//眉毛恢复正常
        mouth.setMouthImage(R.drawable.mouth0);//嘴巴恢复正常
        cleanEye();


        if (animTag[10]) {//悲伤眼泪去掉
            if (getChildCount() > 6) {
                post(new Runnable() {
                    @Override
                    public void run() {
                        removeViewAt(6);
                    }
                });
            }
            if (getChildCount() > 6) {
                post(new Runnable() {
                    @Override
                    public void run() {
                        removeViewAt(6);
                    }
                });
            }
        }
        for (int i = 0; i < animTag.length; i++) {
            animTag[i] = false;
        }
    }

    public void emo0() {//joy
        if (animTag[0]) return;
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

        animTag[10] = true;
        final View cryleft = new View(mContext);

        cryleft.setLayoutParams(new RelativeLayout.LayoutParams((int) (156 * radio), (int) (306 * radio)));
        cryleft.setX(187 * radio);
        cryleft.setY(446 * radio);
        final View cryright = new View(mContext);

        cryright.setLayoutParams(new RelativeLayout.LayoutParams((int) (156 * radio), (int) (306 * radio)));
        cryright.setX(742 * radio);
        cryright.setY(446 * radio);

        postDelayed(new Runnable() {
            @Override
            public void run() {

                if (animTag[10]) {
                    if (getChildCount() == 6) {
                        addView(cryleft);
                        addView(cryright);
                        mouth.setMouthImage(R.drawable.mouth_sad_cry);
                        animTag[3] = false;
                    }
                    eye_cry_size = -eye_cry_size;
                    eye.setEyeCry(eye_cry_size, eye_cry_size);
                    eyeRight.setEyeCry(eye_cry_size, eye_cry_size);

                    if (sadCount >= ResUtils.eyeSadLeft.length) sadCount = 0;
                    cryleft.setBackgroundResource(ResUtils.eyeSadLeft[sadCount]);
                    cryright.setBackgroundResource(ResUtils.eyeSadRight[sadCount]);
                    sadCount++;
                    postDelayed(this, 160);
                }
            }
        }, 3000);
    }

    public void emo2() {//fear

    }

    public void emo3() {//anger
        if (animTag[1]) return;
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
        if (animTag[2]) return;
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


    public void eyeSine() {
        //眩晕时不得眨眼
        if (animTag[9] || animTag[8]) return;
        animTag[9] = true;
        countEye = 0;
        post(new Runnable() {
            @Override
            public void run() {
                if (animTag[9]) {
                    if (countEye < ResUtils.openEyeSine.length) {
                        eye.enableRes(ResUtils.openEyeSine[countEye]);
                        eyeRight.enableRes(ResUtils.openEyeSine[countEye]);
                        countEye++;
                        postDelayed(this, 20);

                    } else {
                        animTag[9] = false;
                    }
                }
            }
        });
    }

    public void mouthStartSpeakAnim() {//愤怒 不能说话
        mouth.startSpeak(120, ResUtils.mouthSpeakNomal);
    }

    public void mouthStopSpeakAnim() {
        mouth.stopSpeak();
    }

    private int blinkCount = 0;

    public void blink1() {//连续眨眼
        if (animTag[11]) return;
        cleanALlAction();
        blinkCount = 0;
        animTag[11] = true;
        mouth.setMouthImage(R.drawable.mouth_smile1);
        post(new Runnable() {
            @Override
            public void run() {
                if (animTag[11]) {
                    eyeSine();
                    if (blinkCount < 1) {
                        postDelayed(this, 500);
                    } else if (blinkCount == 1) {
                        postDelayed(this, 1000);
                    } else {
                        animTag[11] = false;
                        mouth.setMouthImage(R.drawable.mouth0);
                    }
                    blinkCount++;
                }
            }
        });
    }


    public void comfort() {//安慰

        mouth.setMouthImage(R.drawable.mouth_smile4);
        brow.comfort();
        postDelayed(new Runnable() {
            @Override
            public void run() {
                mouth.setMouthImage(R.drawable.mouth0);
                brow.nomal();
            }
        }, 3000);
    }

    public void grievance() {//委屈

        mouth.setMouthImage(R.drawable.mouth_sad0);
        brow.sad();
        animShockEye();
        animShockBlush();
        postDelayed(new Runnable() {
            @Override
            public void run() {
                animTag[3] = false;
                animTag[12] = false;
                mouth.setMouthImage(R.drawable.mouth0);
                brow.nomal();
            }
        }, 3000);
    }

    public void grimace() {//鬼脸

    }

    public void happy_initial() {//开心的初始状态
        mouth.setMouthImage(R.drawable.mouth_smile2);
        postDelayed(new Runnable() {
            @Override
            public void run() {
                mouth.setMouthImage(R.drawable.mouth0);
            }
        }, 3000);
    }

    public void sad1() {
        mouth.setMouthImage(R.drawable.mouth_sad0);
        animShockEye();
        postDelayed(new Runnable() {
            @Override
            public void run() {
                animTag[3] = false;
                mouth.setMouthImage(R.drawable.mouth0);
            }
        }, 3000);
    }

    public void sad2() {
        mouth.setMouthImage(R.drawable.mouth_sad2);
        animShockEye();
        brow.sad();
        postDelayed(new Runnable() {
            @Override
            public void run() {
                animTag[3] = false;
                mouth.setMouthImage(R.drawable.mouth0);
                brow.nomal();
            }
        }, 3000);
    }

    public void cry() {

        mouth.setMouthImage(R.drawable.mouth_sad_cry);
        brow.sad();

        animTag[10] = true;
        final View cryleft = new View(mContext);
        cryleft.setLayoutParams(new RelativeLayout.LayoutParams((int) (156 * radio), (int) (306 * radio)));
        cryleft.setX(187 * radio);
        cryleft.setY(446 * radio);

        final View cryright = new View(mContext);
        cryright.setLayoutParams(new RelativeLayout.LayoutParams((int) (156 * radio), (int) (306 * radio)));
        cryright.setX(742 * radio);
        cryright.setY(446 * radio);

        if (getChildCount() == 6) {
            addView(cryleft);
            addView(cryright);
        }
        post(new Runnable() {
            @Override
            public void run() {
                if (animTag[10]) {
                    eye_cry_size = -eye_cry_size;
                    eye.setEyeCry(eye_cry_size, eye_cry_size);
                    eyeRight.setEyeCry(eye_cry_size, eye_cry_size);

                    if (sadCount >= ResUtils.eyeSadLeft.length) sadCount = 0;
                    cryleft.setBackgroundResource(ResUtils.eyeSadLeft[sadCount]);
                    cryright.setBackgroundResource(ResUtils.eyeSadRight[sadCount]);
                    sadCount++;
                    postDelayed(this, 200);
                }
            }
        });

        postDelayed(new Runnable() {
            @Override
            public void run() {
                animTag[10] = false;
                if (getChildCount() > 6) {
                    post(new Runnable() {
                        @Override
                        public void run() {
                            removeViewAt(6);
                        }
                    });
                }
                if (getChildCount() > 6) {
                    post(new Runnable() {
                        @Override
                        public void run() {
                            removeViewAt(6);
                        }
                    });
                }
                mouth.setMouthImage(R.drawable.mouth0);
                brow.nomal();
                cleanEye();
            }
        }, 3000);
    }

    public void smile1() {

        animTag[13] = true;
        post(new Runnable() {
            @Override
            public void run() {

                if (animTag[13]) {

                    if (countSmile < ResUtils.mouthSmail1.length) {
                        mouth.setMouthImage(ResUtils.mouthSmail1[countSmile]);
                        countSmile++;
                        postDelayed(this, 120);
                    } else {
                        animTag[13] = false;
                        countSmile = 0;
                    }

                }
            }
        });
        postDelayed(new Runnable() {
            @Override
            public void run() {
                animTag[13] = false;
                mouth.setMouthImage(R.drawable.mouth0);
            }
        }, 3000);

    }

    public void smile2() {

        emo0();
        postDelayed(new Runnable() {
            @Override
            public void run() {
                animTag[0] = false;
            }
        }, 3000);
    }

    public void smile3() {
        brow.smile3();
        mouth.setMouthImage(R.drawable.mouth_smile2);
        postDelayed(new Runnable() {
            @Override
            public void run() {
                brow.nomal();
                mouth.setMouthImage(R.drawable.mouth0);
            }
        }, 3000);
    }

    public void smile4() {
        brow.smile4();
        mouth.setMouthImage(R.drawable.mouth_smile2);
        postDelayed(new Runnable() {
            @Override
            public void run() {
                brow.nomal();
                mouth.setMouthImage(R.drawable.mouth0);
            }
        }, 3000);
    }

    public void trapped() {
        //TODO 困的
        mouth.setMouthImage(R.drawable.mouth_sad0);
        animTag[14] = true;
        post(new Runnable() {
            @Override
            public void run() {

                if (animTag[14]) {

                    if (countTrapped < ResUtils.eyeTrapped.length) {
                        mouth.setMouthImage(ResUtils.eyeTrapped[countTrapped]);
                        countTrapped++;
                        postDelayed(this, 120);
                    } else {
                        animTag[14] = false;
                        countTrapped = 0;
                    }

                }
            }
        });

        postDelayed(new Runnable() {
            @Override
            public void run() {
                mouth.setMouthImage(R.drawable.mouth0);
            }
        }, 3000);
    }
}
