package com.example.texttospeech;

import android.app.Application;

import com.iflytek.cloud.SpeechUtility;

/**
 * Created by mac on 15/9/9.
 */
public class BaseApplication extends Application
{
    @Override
    public void onCreate() {
        SpeechUtility.createUtility(BaseApplication.this, "appid=" + getString(R.string.app_id));
        super.onCreate();
    }
}
