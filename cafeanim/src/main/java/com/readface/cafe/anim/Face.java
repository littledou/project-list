package com.readface.cafe.anim;

import android.content.Context;
import android.widget.RelativeLayout;

/**
 * Face 包括腮红2，眉毛2，眼睛2，嘴巴1
 */
public class Face extends RelativeLayout {

    private Blush blushLeft, blushRight;
    private Brow brow;
    private Eye eye;
    private Mouth mouth;

    private float radio = 1f;

    public Face(Context context,float radio) {
        super(context);
        this.radio = radio;
        initPart(context);
    }

    private void initPart(Context context) {
        //添加腮红
        blushLeft = new Blush(context, radio,BasePart.LEFT);
        blushRight = new Blush(context, radio,BasePart.RIGHT);
        addView(blushLeft);
        addView(blushRight);

        eye = new Eye(context, radio);
        addView(eye);


        brow = new Brow(context, radio);
        addView(brow);

        mouth = new Mouth(context,radio);
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


    public void action0(){//正常表情值
        cleanAction();
    }

    public void action1(){//眨眼睛1：刚启动系统
        cleanAction();
        eye.startSight();
        mouth.setMouthImage(R.drawable.mouth_smile2);
    }
    public void action2(){//说话开心：100
        cleanAction();
        mouth.startSpeak(100, new int[]{
                R.drawable.mouth0
                , R.drawable.mouth_smile1
                , R.drawable.mouth_smile2
                , R.drawable.mouth_smile1
        });
    }
    public void action3(){//说话正常
        cleanAction();
        mouth.startSpeak(100, new int[]{
                R.drawable.mouth_nomal_speak0
                , R.drawable.mouth_nomal_speak1
                , R.drawable.mouth_nomal_speak2
                , R.drawable.mouth_nomal_speak3
                , R.drawable.mouth_nomal_speak4
                , R.drawable.mouth_nomal_speak5
        });
    }

    public void action4(){//悲伤1：眼白微颤，加嘴巴变化
        cleanAction();
        //TODO 开启绑定事件 嘴部口型变化时绑定眼睛颤动
        eye.startEyeShock();
        mouth.startSpeak(3000,new int[]{
                R.drawable.mouth_sad1
                ,R.drawable.mouth_sad0
        });
    }
    private void cleanAction(){//清除处理事件
        mouth.stopSpeak();
        eye.stopSight();
        eye.stopEyeShock();
    }


}
