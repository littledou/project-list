package com.readface.cafe.robot;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

import com.readface.cafe.anim.R;

/**
 * Created by mac on 15/11/23.
 */
public class Arm extends View {


    public Arm(Context context) {
        super(context);
    }

    public Arm(Context context, boolean isLeft) {
        this(context);
        if (isLeft) {
            setBackgroundResource(R.mipmap.arm_l);
        } else {
            setBackgroundResource(R.mipmap.arm_r);

        }
    }
}
