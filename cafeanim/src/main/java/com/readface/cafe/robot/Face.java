package com.readface.cafe.robot;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import com.readface.cafe.anim.R;
import com.readface.cafe.utils.ResUtils;

/**
 * Face 包括腮红1，眉毛1，眼睛1，嘴巴1
 */
public class Face extends RelativeLayout {

    private Brow brow;
    private Eye eye, eyeRight;
    private Mouth mouth;
    private Context mContext;
    private float radio = 1f;
    private View eye_cry, eye_goshl, eye_goshr;
    private final static int EMOTIME = 2000;
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
     * 15: 鬼脸
     * 16: 亲吻
     */
    public static boolean[] animTag;//长度就代表几种动画
    private final int tag_count = 17;
    private int eye_shock_size = 2;
    private int eye_cry_size = 6;

    private int count_emo = 0;
    private int countEye = 0;
    private int sadCount = 0;
    private int countEyeRight = 0;
    private int countMouthGosh = 0;
    private int countSmile = 0;
    private int countTrapped = 0;
    private int countGrimace = 0;
    private int countKiss = 0;
    private int digress = 60;
    private boolean isCloseAllEye = false;

    private boolean STOPALL = false;

    public Face(Context context, float radio) {
        super(context);
        this.mContext = context;
        this.radio = radio;

        animTag = new boolean[tag_count];
        setBackgroundResource(R.mipmap.head_bg);
        initPart(context);
    }

    private void initPart(Context context) {
        //添加腮红
        eye = new Eye(context, radio, true);
        addView(eye);
        eyeRight = new Eye(context, radio, false);
        addView(eyeRight);

        brow = new Brow(context, radio);
        addView(brow);

        mouth = new Mouth(context, radio);
        addView(mouth);

        eye_cry = new View(mContext);
        eye_goshl = new View(mContext);
        eye_goshr = new View(mContext);


        addView(eye_cry);
        addView(eye_goshl);
        addView(eye_goshr);
        eye_goshl.setBackgroundResource(R.mipmap.eye_gosh);
        eye_goshr.setBackgroundResource(R.mipmap.eye_gosh);
        eye_goshl.setVisibility(View.INVISIBLE);
        eye_goshr.setVisibility(View.INVISIBLE);
        eye_cry.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        super.onLayout(changed, l, t, r, b);

        eye.layout((int) (176 * radio), (int) (197 * radio), (int) (289 * radio), (int) (310 * radio));
        eye_goshl.layout((int) (176 * radio), (int) (197 * radio), (int) (289 * radio), (int) (310 * radio));
        eyeRight.layout((int) (354 * radio), (int) (197 * radio), (int) (467 * radio), (int) (310 * radio));
        eye_goshr.layout((int) (354 * radio), (int) (197 * radio), (int) (467 * radio), (int) (310 * radio));
        brow.layout((int) (176 * radio), (int) (120 * radio), (int) (467 * radio), (int) (191 * radio));
        mouth.layout((int) (228 * radio), (int) (310 * radio), (int) (413 * radio), (int) (423 * radio));
        eye_cry.layout(0, (int) (190 * radio), (int) (640 * radio), (int) (401 * radio));

    }


    private void cleanEye() {
        eye.nomalSide();
        eyeRight.nomalSide();
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
                    if (countEye < ResUtils.openEyeL.length) {
                        eye.enableRes(ResUtils.openEyeL[countEye]);
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

                    if (countEyeRight < ResUtils.openEyeR.length) {
                        eyeRight.enableRes(ResUtils.openEyeR[countEyeRight]);
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
        countEye = ResUtils.openEyeL.length - 1;
        postDelayed(new Runnable() {
            @Override
            public void run() {
                if (animTag[6]) {

                    if (countEye >= 0) {
                        eye.enableRes(ResUtils.openEyeL[countEye]);
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
        countEyeRight = ResUtils.openEyeR.length - 1;
        postDelayed(new Runnable() {
            @Override
            public void run() {
                if (animTag[7]) {

                    if (countEyeRight >= 0) {
                        eyeRight.enableRes(ResUtils.openEyeR[countEyeRight]);
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
        if (animTag[3] || STOPALL) return;
        animTag[3] = true;

        post(new Runnable() {
            @Override
            public void run() {
                if (animTag[3]) {
                    eye.setEyeMove(eye_shock_size);
                    eyeRight.setEyeMove(-eye_shock_size);
                    eye_shock_size = -eye_shock_size;
                    postDelayed(this, 80);
                }
            }
        });
    }


    public void animGosh() {
        if (animTag[8] || STOPALL) return;
        cleanALlAction();
        animTag[8] = true;
        digress = 0;
        countMouthGosh = 0;
        brow.ang1();
        eye.playGosh();
        eyeRight.playGosh();
        eye_goshl.setVisibility(View.VISIBLE);
        eye_goshr.setVisibility(View.VISIBLE);
        post(new Runnable() {
            @Override
            public void run() {
                if (animTag[8]) {
                    eye_goshl.animate().rotation(digress).setDuration(120);
                    eye_goshr.animate().rotation(digress).setDuration(120);
                    digress += 60;

                    if (countMouthGosh >= ResUtils.mouthGosh.length) countMouthGosh = 0;
                    mouth.setMouthImage(ResUtils.mouthGosh[countMouthGosh]);
                    countMouthGosh++;
                    postDelayed(this, 120);
                }
            }
        });

        postDelayed(new Runnable() {
            @Override
            public void run() {
                animTag[8] = false;
                cleanALlAction();
            }
        }, EMOTIME);

    }

    public void cleanALlAction() {//清除处理事件

        brow.nomalSide();//眉毛恢复正常
        mouth.nomalSide();//嘴巴恢复正常
        cleanEye();

        eye_goshl.setVisibility(View.INVISIBLE);
        eye_goshr.setVisibility(View.INVISIBLE);
        eye_cry.setVisibility(View.INVISIBLE);
        for (int i = 0; i < animTag.length; i++) {
            animTag[i] = false;
        }
    }

    public void emo0() {//joy
        if (animTag[0] || STOPALL) return;
        cleanALlAction();
        animTag[0] = true;
        count_emo = 0;
        post(new Runnable() {
            @Override
            public void run() {
                if (animTag[0]) {//眉毛跳动，嘴巴跳动
                    if (count_emo == 0) {
                        count_emo = 1;
                        brow.up();
                        mouth.setMouthImage(R.mipmap.mouth_smile3);
                    } else {
                        count_emo = 0;
                        brow.nomalSide();
                        mouth.setMouthImage(R.mipmap.mouth_smile1);
                    }
                    postDelayed(this, 120);
                }
            }
        });
    }


    public void emo1() {//sad
        if (STOPALL) return;
        cleanALlAction();
        animShockEye();
        mouth.setMouthImage(R.mipmap.mouth_sad2);
        brow.sad();
        sadCount = 0;
        animTag[10] = true;
        final View cry = new View(mContext);

        cry.setLayoutParams(new RelativeLayout.LayoutParams((int) (640 * radio), (int) (211 * radio)));
        cry.setY(0 * radio);

        postDelayed(new Runnable() {
            @Override
            public void run() {
                if (animTag[10]) {
                    if (getChildCount() == 4) {
                        addView(cry);
                        animTag[3] = false;
                    }
                    eye_cry_size = -eye_cry_size;
                    eye.setEyeMove(eye_cry_size);
                    eyeRight.setEyeMove(eye_cry_size);

                    if (sadCount >= ResUtils.eyeSadLeft.length) sadCount = 0;
                    cry.setBackgroundResource(ResUtils.eyeSadLeft[sadCount]);
                    mouth.setMouthImage(R.mipmap.mouth_sad_cry1);
                    if (sadCount % 2 == 1) mouth.setMouthImage(R.mipmap.mouth_sad_cry2);
                    sadCount++;
                    postDelayed(this, 160);
                }
            }
        }, EMOTIME);
    }

    public void emo2() {//fear

    }

    public void emo3() {//anger

    }


    public void emo4() {//surp
        if (animTag[2] || STOPALL) return;
        cleanALlAction();
        animTag[2] = true;
        count_emo = 0;
        brow.sub();
        post(new Runnable() {
            @Override
            public void run() {
                if (animTag[2]) {//眉毛跳动，嘴巴跳动
                    if (count_emo == 0) {
                        count_emo = 1;
                        eye.enAng(R.mipmap.eye_sub1);
                        eyeRight.enAng(R.mipmap.eye_sub1);
                        mouth.setMouthImage(R.mipmap.mouth_sub1);
                    } else {
                        eye.enAng(R.mipmap.eye_sub2);
                        eyeRight.enAng(R.mipmap.eye_sub2);
                        count_emo = 0;
                        mouth.setMouthImage(R.mipmap.mouth_sub2);
                    }
                    postDelayed(this, 120);
                }
            }
        });
    }


    public void emo5() {//disg

    }

    public void emo6() {//nomal

        if (STOPALL) return;
        cleanALlAction();
    }


    //TODO
    public void eyeSine() {
        //眩晕时不得眨眼
        if (animTag[9] || animTag[8]) return;
        animTag[9] = true;
        countEye = 0;
        post(new Runnable() {
            @Override
            public void run() {
                if (animTag[9]) {
                    if (countEye < ResUtils.openEyeSinel.length) {
                        eye.sign(ResUtils.openEyeSinel[countEye]);
                        eyeRight.sign(ResUtils.openEyeSiner[countEye]);
                        countEye++;
                        postDelayed(this, 20);
                    } else {
                        animTag[9] = false;
                        eye.nomalSide();
                    }
                }
            }
        });
    }

    public void mouthStartSpeakAnim() {//愤怒 不能说话
        if (STOPALL) return;
        mouth.startSpeak(120, ResUtils.mouthSpeakNomal);
    }

    public void mouthStopSpeakAnim() {
        mouth.stopSpeak();
    }


    private int blinkCount = 0;

    public void blink() {
        if (STOPALL) return;
        if (animTag[11]) return;
        cleanALlAction();
        blinkCount = 0;
        animTag[11] = true;

        post(new Runnable() {
            @Override
            public void run() {
                if (animTag[11]) {
                    eyeSine();

                    if (blinkCount == 0) {
                        postDelayed(this, 400);
                    } else if (blinkCount == 1) {
                        postDelayed(this, 800);
                    } else {
                        animTag[11] = false;
                    }
                    blinkCount++;
                }
            }
        });
    }

    public void comfort() {//安慰
        if (STOPALL) return;
        mouth.setMouthImage(R.mipmap.mouth_smile4);
        brow.comfort();
        postDelayed(new Runnable() {
            @Override
            public void run() {
                mouth.nomalSide();
                brow.nomalSide();
            }
        }, EMOTIME);
    }

    public void grievance() {//委屈
        if (STOPALL) return;
        mouth.setMouthImage(R.mipmap.mouth_sad1);
        brow.cry();
        animShockEye();
        postDelayed(new Runnable() {
            @Override
            public void run() {
                animTag[3] = false;
                mouth.nomalSide();
                brow.nomalSide();
            }
        }, EMOTIME);
    }

    public void grimace() {//鬼脸
        if (STOPALL) return;
        brow.up();
        animTag[15] = true;
        countGrimace = 0;
        post(new Runnable() {
            @Override
            public void run() {
                if (animTag[15]) {

                    if (countGrimace >= ResUtils.mouthGrimace.length) {
                        countGrimace = 0;
                    }
                    mouth.setMouthImage(ResUtils.mouthGrimace[countGrimace]);
                    countGrimace++;
                    postDelayed(this, 100);
                }
            }
        });

        postDelayed(new Runnable() {
            @Override
            public void run() {
                eye.setEyeMove(0);
                eyeRight.setEyeMove(0);
                brow.nomalSide();
                mouth.nomalSide();
                animTag[15] = false;
            }
        }, EMOTIME);

    }

    public void happy_initial() {//开心的初始状态
        if (STOPALL) return;
        mouth.setMouthImage(R.mipmap.mouth_smile2);
        postDelayed(new Runnable() {
            @Override
            public void run() {
                mouth.nomalSide();
            }
        }, EMOTIME);
    }

    public void sad1() {
        if (STOPALL) return;
        mouth.setMouthImage(R.mipmap.mouth_sad0);
        animShockEye();
        postDelayed(new Runnable() {
            @Override
            public void run() {
                animTag[3] = false;
                mouth.nomalSide();
            }
        }, EMOTIME);
    }

    public void sad2() {
        if (STOPALL) return;
        mouth.setMouthImage(R.mipmap.mouth_sad2);
        animShockEye();
        brow.sad();

        postDelayed(new Runnable() {
            @Override
            public void run() {
                animTag[3] = false;
                mouth.nomalSide();
                brow.nomalSide();
            }
        }, EMOTIME);
    }

    public void cry() {
        if (animTag[10]) return;
        cleanALlAction();
        mouth.setMouthImage(R.mipmap.mouth_sad_cry1);
        brow.cry();
        eye.cry();
        eyeRight.cry();

        sadCount = 0;
        animTag[10] = true;
        eye_cry.setVisibility(View.VISIBLE);

        post(new Runnable() {
            @Override
            public void run() {
                if (animTag[10]) {
                    if (sadCount >= ResUtils.eyeSadLeft.length) sadCount = 0;
                    eye_cry.setBackgroundResource(ResUtils.eyeSadLeft[sadCount]);
                    mouth.setMouthImage(R.mipmap.mouth_sad_cry1);
                    if (sadCount % 2 == 1) mouth.setMouthImage(R.mipmap.mouth_sad_cry2);
                    sadCount++;
                    postDelayed(this, 130);
                }
            }
        });

        postDelayed(new Runnable() {
            @Override
            public void run() {
                animTag[10] = false;
                mouth.nomalSide();
                brow.nomalSide();
                cleanEye();
                eye_cry.setVisibility(View.INVISIBLE);
            }
        }, EMOTIME);
    }

    public void smile1() {
        if (STOPALL) return;
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
                mouth.nomalSide();
            }
        }, EMOTIME);

    }

    public void smile2() {
        if (STOPALL) return;
        emo0();
        postDelayed(new Runnable() {
            @Override
            public void run() {
                cleanALlAction();
            }
        }, EMOTIME);
    }

    public void smile3() {
        if (STOPALL) return;
        brow.smile3_2();
        mouth.setMouthImage(R.mipmap.mouth_smile2);
        postDelayed(new Runnable() {
            @Override
            public void run() {
                brow.nomalSide();
                mouth.nomalSide();
            }
        }, EMOTIME);
    }

    public void smile4() {
        if (STOPALL) return;
        brow.cry();
        mouth.setMouthImage(R.mipmap.mouth_smile2);
        postDelayed(new Runnable() {
            @Override
            public void run() {
                brow.nomalSide();
                mouth.nomalSide();
            }
        }, EMOTIME);
    }

    public void trapped() {
        if (STOPALL) return;
        mouth.setMouthImage(R.mipmap.mouth_sad1);
        animTag[14] = true;
        post(new Runnable() {
            @Override
            public void run() {
                if (animTag[14]) {
                    if (countTrapped < ResUtils.eyeTrapped.length) {
                        eye.sign(ResUtils.eyeTrapped[countTrapped]);
                        eyeRight.sign(ResUtils.eyeTrapped[countTrapped]);
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
                cleanEye();
                mouth.nomalSide();
            }
        }, EMOTIME);
    }

    public void kiss() {
        if (STOPALL) return;
        if (animTag[16]) return;
        cleanALlAction();
        animTag[16] = true;
        countKiss = 0;
        brow.kiss();
        eye.cry();
        eyeRight.cry();
        post(new Runnable() {
            @Override
            public void run() {
                if (animTag[16]) {
                    if (countKiss >= 4) countKiss = 0;
                    mouth.setMouthImage(ResUtils.mouthKiss[countKiss % 2]);
                    countKiss++;
                    postDelayed(this, 100);
                }
            }
        });

        postDelayed(new Runnable() {
            @Override
            public void run() {
                cleanALlAction();
            }
        }, EMOTIME);
    }

    public void ang1() {
        if (STOPALL) return;
        if (animTag[1]) return;
        cleanALlAction();
        animTag[1] = true;
        animShockEye();
        count_emo = 0;
        mouth.setMouthImage(R.mipmap.mouth_ang);
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
        postDelayed(new Runnable() {
            @Override
            public void run() {
                cleanALlAction();
            }
        }, EMOTIME);
    }

    public void ang2() {
        if (STOPALL) return;
        if (animTag[1]) return;
        cleanALlAction();
        animTag[1] = true;
        count_emo = 0;
        brow.ang2();
        mouth.setMouthImage(R.mipmap.mouth_ang);

        post(new Runnable() {
            @Override
            public void run() {
                if (animTag[1]) {

                    if (count_emo >= ResUtils.eyeAngl.length) count_emo = 0;
                    eye.enAng(ResUtils.eyeAngl[count_emo]);
                    eyeRight.enAng(ResUtils.eyeAngr[count_emo]);
                    count_emo++;
                    postDelayed(this, 130);
                }
            }
        });

        postDelayed(new Runnable() {
            @Override
            public void run() {
                cleanALlAction();
            }
        }, EMOTIME);
    }

    public void sub() {
        if (STOPALL) return;
        emo4();
        postDelayed(new Runnable() {
            @Override
            public void run() {
                cleanALlAction();
            }
        }, EMOTIME);
    }

    public boolean isSTOPALL() {
        return STOPALL;
    }

    public void setSTOPALL(boolean STOPALL) {
        this.STOPALL = STOPALL;
    }
}
