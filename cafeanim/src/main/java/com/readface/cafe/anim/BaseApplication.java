package com.readface.cafe.anim;

import android.app.Application;

import com.iflytek.cloud.SpeechUtility;
import com.readface.cafe.utils.EmotionStatus;
import com.readface.cafe.utils.StatusType;
import com.readface.cafe.utils.StringUtil;
import com.readface.cafe.utils.VolleyHelper;

/**
 * Created by mac on 15/10/14.
 */
public class BaseApplication extends Application {

    private String token;
    private String user_emotion = EmotionStatus.emotion[0];//用户的情绪
    private StatusType MStatus = StatusType.Nomal;
    private String id = "0";
    private static BaseApplication intence;

    @Override
    public void onCreate() {
        SpeechUtility.createUtility(BaseApplication.this, "appid=56404c83");
        super.onCreate();
        VolleyHelper.initQueue(this);
        intence = this;
    }

    public static BaseApplication getIntence() {
        return intence;
    }


    public String getUser_emotion() {
        String emo = EmotionStatus.resultEmotion();
        if (StringUtil.isNotEmpty(emo)) {
            user_emotion = emo;
            return emo;
        } else {
            return user_emotion;
        }
    }

    public StatusType getStatus() {
        return MStatus;
    }

    public void setStatus(StatusType MStatus) {
        this.MStatus = MStatus;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
