package com.example.face;

import android.content.Context;
import android.widget.RelativeLayout;

import com.example.util.FaceUtil;

/**
 * Face 包括腮红2，眉毛2，眼睛2，嘴巴1
 */
public class Face extends RelativeLayout {

    private Blush blushLeft, blushRight;
    private Brow browLeft, browRight;
    private Eye eyeLeft, eyeRight;
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

        eyeLeft = new Eye(context, radio,BasePart.LEFT);
        eyeRight = new Eye(context, radio,BasePart.RIGHT);
        addView(eyeLeft);
        addView(eyeRight);


        browLeft = new Brow(context, radio,BasePart.LEFT);
        browRight = new Brow(context, radio,BasePart.RIGHT);
        addView(browLeft);
        addView(browRight);

        mouth = new Mouth(context,radio);
        addView(mouth);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        super.onLayout(changed, l, t, r, b);

        blushLeft.layout((int) (72 * radio), (int) (360 * radio), (int) (372 * radio), (int) (560 * radio));
        blushRight.layout((int) (708 * radio), (int) (360 * radio), (int) (1008 * radio), (int) (560 * radio));
        eyeLeft.layout((int)(150*radio), (int)(175*radio), (int)(444*radio), (int)(469*radio));
        eyeRight.layout((int) (640 * radio), (int) (175 * radio), (int) (934 * radio), (int) (469 * radio));

        browLeft.layout((int) (110 * radio), (int) (33 * radio), (int) (480 * radio), (int) (164 * radio));
        browRight.layout((int)(602*radio), (int)(33*radio), (int)(972*radio), (int)(164*radio));

        mouth.layout((int) (370 * radio), (int) (512 * radio), (int) (710 * radio), (int) (710 * radio));
    }


    public void startNomalSpeak(){
        mouth.startNomalSpeak();
    }

    public void stopNomalSpeak(){
        mouth.stopNomalSpeak();
    }

    public void startSight(){
        eyeRight.startSight();
        eyeLeft.startSight();
    }
    public void stopSight(){
        eyeRight.stopSight();
        eyeLeft.stopSight();
    }

}
