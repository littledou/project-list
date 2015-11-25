package com.readface.cafe.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by mac on 15/11/11.
 */
public class AnimHelper {

    private static final Map<String, Integer> animList = new HashMap<String, Integer>() {
        {
            put("blink1", 0);
            put("comfort", 1);
            put("grievance", 2);
            put("grimace", 3);
            put("happy-initial", 4);
            put("sad1", 5);
            put("sad2", 6);
            put("cry", 7);
            put("smile1", 8);
            put("smile2", 9);
            put("smile3", 10);
            put("smile4", 11);
            put("trapped", 12);
            put("kiss", 13);
            put("ang1", 14);
            put("ang2", 15);
            put("sub", 16);
        }
    };

    public static int getAnimPosition(String animStr) {
        return animList.get(animStr);
    }

}
