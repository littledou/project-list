package com.readface.cafe.anim;

import android.app.Activity;
import android.content.Context;
import android.hardware.Camera;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.readface.cafe.robot.Robot;
import com.readface.cafe.utils.CameraHelper;
import com.readface.cafe.utils.FaceUtil;

import mobile.AUDetection.YMDetector;
import mobile.AUDetection.YMFace;

public class MainActivity extends Activity implements YMDetector.DetectorListener, CameraHelper.ImageListener {


    private Context mContext;
    private Robot mRobot;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        RelativeLayout parent = new RelativeLayout(this);
        parent.setBackgroundResource(R.mipmap.main_bg);
        int screenW = FaceUtil.getScreenWidth(this);
        int screenH = FaceUtil.getScreenHeight(this);

        float radio = screenW / 640f;
        int frameH = (int) (810 * radio);
        mRobot = new Robot(this, radio);

        RelativeLayout.LayoutParams robotLa = new RelativeLayout.LayoutParams(screenW, frameH);


        robotLa.setMargins(0, (screenH - frameH) * 2 / 5, 0, 0);
        mRobot.setLayoutParams(robotLa);
        parent.addView(mRobot);
        setContentView(parent);
    }

    @Override
    public void onSuccess(YMFace ymFace, byte[] bytes, float v) {

    }

    @Override
    public void onDataResults(byte[] data, Camera camera) {

    }

    @Override
    public void onError() {

    }
}
