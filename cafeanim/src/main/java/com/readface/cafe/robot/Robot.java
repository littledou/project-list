package com.readface.cafe.robot;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;

/**
 * Created by mac on 15/11/23.
 */
public class Robot extends ViewGroup {

    Head mHead;
    Body mBody;
    private Context mContext;
    private float radio = 1f;

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
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        mHead.layout(0, 0, (int) (640 * radio), (int) (608 * radio));
        mBody.layout(0, (int) (585 * radio), (int) (640 * radio), (int) (809 * radio));
    }


    public Face getFace() {
        return mHead.getFace();
    }
}
