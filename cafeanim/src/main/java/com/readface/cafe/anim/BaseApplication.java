package com.readface.cafe.anim;

import android.app.Application;

import com.iflytek.cloud.SpeechUtility;

/**
 * Created by mac on 15/10/14.
 */
public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        SpeechUtility.createUtility(BaseApplication.this, "appid=561e1035");
        super.onCreate();

    }
}
