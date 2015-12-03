package com.readface.cafe.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by mac on 15/10/15.
 */
public class AppUrl {

    private static String APPURL = "http://121.42.141.249/v1/";

    //    private static String APPURL = "http://chatbot.readface.cn/v1/";
    public static String postActivate() {
        return APPURL + "robots/activate";
    }

    public static String getNext(String voice, String count, String emotion, String action) {
        try {
            voice = URLEncoder.encode(voice, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return APPURL + "actions/next?person_voice="
                + voice + "&person_id=1&person_emotion=" + emotion + "&empty_count=" + count + "&person_action=" + action;
    }

    public static String postFaceVerify() {
        return APPURL + "people/verify";
    }

    public static String postForVerify(String id) {
        return APPURL + "people/" + id + "/picture";
    }


    public static String putUpdatePerson(String id) {
        return APPURL + "people/" + id;
    }

    public static String getAsk(String id, String empty_count, String voice) {
        try {
            voice = URLEncoder.encode(voice, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return APPURL + "people/" + id + "/ask_name?empty_count=" + empty_count + "&person_voice=" + voice;

    }

}
