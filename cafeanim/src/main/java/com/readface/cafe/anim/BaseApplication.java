package com.readface.cafe.anim;

import android.app.Application;

import com.iflytek.cloud.SpeechUtility;
import com.readface.cafe.utils.EmotionStatus;
import com.readface.cafe.utils.StringUtil;
import com.readface.cafe.utils.VolleyHelper;

/**
 * Created by mac on 15/10/14.
 */
public class BaseApplication extends Application {

    public String token;
    private String user_emotion = EmotionStatus.emotion[0];//用户的情绪
    public String robot_emotion = EmotionStatus.emotion[0];//robot的情绪
    public String id = "";
    private static BaseApplication intence;

    @Override
    public void onCreate() {
        SpeechUtility.createUtility(BaseApplication.this, "appid=561e1035");
        super.onCreate();
        VolleyHelper.initQueue(this);
        intence = this;
    }

    public static BaseApplication getIntence() {
        return intence;
    }


    public String getUser_emotion() {
        String emo = EmotionStatus.resultEmotion();
        if(StringUtil.isNotEmpty(emo)){
            return emo;
        }else{
            return user_emotion;
        }
    }
}
