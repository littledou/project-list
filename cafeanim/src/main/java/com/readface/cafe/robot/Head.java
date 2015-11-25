package com.readface.cafe.robot;

import android.content.Context;
import android.view.ViewGroup;

/**
 * Created by mac on 15/11/23.
 */
public class Head extends ViewGroup {

    //angle head
    private float radio = 1f;
    private Face mFace;
    private Angle mAngle;

    public Head(Context context, float radio) {
        super(context);
        this.radio = radio;

        mAngle = new Angle(context, radio);
        mFace = new Face(context, radio);
        addView(mAngle);
        addView(mFace);
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
