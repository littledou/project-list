package com.readface.cafe.utils;

/**
 * Created by mac on 15/10/15.
 */
public class AppUrl {

    private static final String APPURL = "http://121.42.141.249:8087/v1/";
    public static String postActivate(){
        return APPURL+"robots/activate";
    }

    public static String getNext(String voice,String emotion){
        return APPURL+"actions/next?voice="+voice+"&person_id=1&person_emotion="+emotion;
    }
}
