package com.readface.cafe.robot;

import android.content.Context;
import android.view.ViewGroup;


/**
 * Created by mac on 15/11/23.
 * 肚子,灯和两只手
 */
public class Body extends ViewGroup {
    private float radio = 1f;

    private Arm armL, armR;
    private Tripe mTripe;
    private Light mLight;

    public Body(Context context, float radio) {
        super(context);

        this.radio = radio;

        armL = new Arm(context, true);
        armR = new Arm(context, false);

        addView(armL);
        addView(armR);

        mTripe = new Tripe(context);
        addView(mTripe);

        mLight = new Light(context);
        addView(mLight);

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        armL.layout((int) (180 * radio), 0, (int) (275 * radio), (int) (155 * radio));
        armR.layout((int) (380 * radio), 0, (int) (470 * radio), (int) (155 * radio));
        mTripe.layout((int) (224 * radio), 0, (int) (421 * radio), (int) (226 * radio));
        mLight.layout((int) (285 * radio), (int) (55 * radio), (int) (355 * radio), (int) (125 * radio));
    }
}
