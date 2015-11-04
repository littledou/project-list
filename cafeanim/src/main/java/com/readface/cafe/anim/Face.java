package com.readface.cafe.anim;

import android.content.Context;
import android.os.Handler;
import android.widget.RelativeLayout;

/**
 * Face 包括腮红1，眉毛1，眼睛1，嘴巴1
 */
public class Face extends RelativeLayout {

    private Blush blushLeft, blushRight;
    private Brow brow;
    private Eye eye;
    private Mouth mouth;

    private float radio = 1f;

    private boolean isAnim0 = false;
    private boolean isAnim3 = false;
    private boolean isAnim4 = false;


    private int count_emo0 = 0;
    private int count_emo1 = 0;

    public Face(Context context, float radio) {
        super(context);
        this.radio = radio;
        initPart(context);
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
        cleanALlAction();
        eye.startSight();
        mouth.setMouthImage(R.drawable.mouth_smile2);
    }

    public void action2() {//说话开心：100
        mouth.startSpeak(100, new int[]{
                R.drawable.mouth0
                , R.drawable.mouth_smile1
                , R.drawable.mouth_smile2
                , R.drawable.mouth_smile1
        });
    }

    public void action3() {//说话正常
        mouth.startSpeak(100, new int[]{
                R.drawable.mouth_nomal_speak0
                , R.drawable.mouth_nomal_speak1
                , R.drawable.mouth_nomal_speak2
                , R.drawable.mouth_nomal_speak3
                , R.drawable.mouth_nomal_speak4
                , R.drawable.mouth_nomal_speak5
        });
    }

    public void action4() {//悲伤1：眼白微颤，加嘴巴变化
        cleanALlAction();
        eye.startEyeShock();
        mouth.startSpeak(3000, new int[]{
                R.drawable.mouth_sad1
                , R.drawable.mouth_sad0
        });
    }

    public void cleanALlAction() {//清除处理事件
        eye.stopSight();
        eye.stopEyeShock();
        brow.nomal();
        mouth.setMouthImage(R.drawable.mouth0);
        isAnim0 = false;
        isAnim3 = false;
        isAnim4 = false;
    }

    public void stopSpeak() {
        mouth.stopSpeak();
    }

    public void emo0() {//joy
        cleanALlAction();
        isAnim0 = true;

        postDelayed(mEmo0Runnable, 20);
    }

    Runnable mEmo0Runnable = new Runnable() {
        @Override
        public void run() {
            if (isAnim0) {//眉毛跳动，嘴巴跳动
                if (count_emo0 == 0) {
                    count_emo0 = 1;
                    brow.up();
                    mouth.setMouthImage(R.drawable.mouth_smile3);
                } else {
                    count_emo0 = 0;
                    brow.nomal();
                    mouth.setMouthImage(R.drawable.mouth_smile1);
                }
                postDelayed(mEmo0Runnable, 120);
            }
        }
    };

    public void emo1() {//sad
        cleanALlAction();
        eye.startEyeShock();
        mouth.setMouthImage(R.drawable.mouth_sad2);
        brow.sad();
    }

    public void emo2() {//fear

    }

    public void emo3() {//anger
        cleanALlAction();
        isAnim3 = true;
        mouth.setMouthImage(R.drawable.mouth_ang);
        postDelayed(mEmo3Runnable, 20);
    }

    Runnable mEmo3Runnable = new Runnable() {
        @Override
        public void run() {
            if (isAnim3) {
                if (count_emo1 == 0) {
                    count_emo1 = 1;
                    brow.ang1();
                } else {
                    count_emo1 = 0;
                    brow.ang2();
                }
                postDelayed(mEmo3Runnable, 120);
            }
        }
    };

    public void emo4() {//surp
        cleanALlAction();
        isAnim4 = true;
        eye.startEyeShock();
        postDelayed(mEmo4Runnable, 20);
    }

    Runnable mEmo4Runnable = new Runnable() {
        @Override
        public void run() {
            if (isAnim4) {//眉毛跳动，嘴巴跳动
                if (count_emo1 == 0) {
                    count_emo1 = 1;
                    brow.sub1();
                    mouth.setMouthImage(R.drawable.mouth_sub1);
                } else {
                    count_emo1 = 0;
                    brow.sub2();
                    mouth.setMouthImage(R.drawable.mouth_sub2);
                }
                postDelayed(mEmo4Runnable, 120);
            }
        }
    };

    public void emo5() {//disg

    }

    public void emo6() {//nomal
        cleanALlAction();

    }


}
