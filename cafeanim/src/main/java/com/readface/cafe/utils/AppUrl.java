package com.readface.cafe.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by mac on 15/10/15.
 */
public class AppUrl {

    private static String APPURL = "http://121.42.141.249:8087/v1/";

    //    private static String APPURL = "http://chatbot.readface.cn/v1/";
    public static String postActivate() {
        return APPURL + "robots/activate";
    }

    public static String getNext(String voice, String emotion) {
        try {
            voice = URLEncoder.encode(voice, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return APPURL + "actions/next?person_voice=" + voice + "&person_id=1&person_emotion=" + emotion;
    }

    public static String postFaceVerify() {
        return APPURL + "people/verify";
    }


    public static String putUpdatePerson(String id) {
        return APPURL + "people/" + id;
    }
}
