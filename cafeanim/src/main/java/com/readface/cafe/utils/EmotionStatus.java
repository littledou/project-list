package com.readface.cafe.utils;

import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import mobile.AUDetection.YMFace;

/**
 * Created by mac on 15/10/22.
 */
public class EmotionStatus {

    private static final int count = 10;
    public static final String[] emotion = {
            "joy",
            "sadness",
            "fear",
            "anger",
            "surprise",
            "disgust",
            "netral"
    };
    public static Map<String, Integer> qualEmo = new HashMap() {
        {
            put(emotion[0], 0);
            put(emotion[1], 1);
            put(emotion[2], 2);
            put(emotion[3], 3);
            put(emotion[4], 4);
            put(emotion[5], 5);
            put(emotion[6], 6);
        }
    };
    private static List<YMFace> faces = new ArrayList<>();//储存面部的集合

    private static List<Integer> emo_list = new ArrayList<>();//储存每张面部表情的集合

    private static List<Integer> eye_left_slope = new ArrayList<>();//储存左眼斜率的集合
    private static List<Integer> eye_right_slope = new ArrayList<>();//储存右眼斜率的集合
    private static List<Integer> heady_slope = new ArrayList<>();//储存点头斜率的集合
    private static List<Integer> headn_slope = new ArrayList<>();//储存摇头斜率的集合
    private static boolean sineEye = false;
    private static boolean head_slopeY = false;
    private static boolean head_slopeN = false;

    public static void addFace(YMFace face) {

        faces.add(face);

        int position = 0;
        float max = 0;
        float emo[] = face.getEmotions();
        for (int j = 0; j < emo.length; j++) {
            if (max <= emo[j]) {
                max = emo[j];
                position = j;
            }
        }
        emo_list.add(position);

        int size = faces.size();
        if (size > 1) {
            int tar_eye_left = (int) (face.getFacialActions()[1] - faces.get(size - 2).getFacialActions()[1]);
            int tar_eye_right = (int) (face.getFacialActions()[0] - faces.get(size - 2).getFacialActions()[0]);
            if (Math.abs(tar_eye_right) >= 20 && Math.abs(tar_eye_left) >= 30) {
                sineEye = true;
                eye_right_slope.add(tar_eye_right);
                eye_left_slope.add(tar_eye_left);
            } else {
                sineEye = false;
            }

            int tar_headY = (int) (face.getHeadPose()[1] - faces.get(size - 2).getHeadPose()[1]);
            if (Math.abs(tar_headY) > 13) {
                head_slopeY = true;
                heady_slope.add(tar_headY);
            } else {
                head_slopeY = false;
            }
            int tar_head = (int) (face.getHeadPose()[2] - faces.get(size - 2).getHeadPose()[2]);
            if (Math.abs(tar_head) > 17) {
                head_slopeN = true;
                headn_slope.add(tar_head);
            } else {
                head_slopeN = false;
            }

        }
        if (faces.size() > count) {
            faces.remove(0);
            emo_list.remove(0);
            if (eye_left_slope.size() > 5)
                eye_left_slope.remove(0);
            if (eye_right_slope.size() > 5)
                eye_right_slope.remove(0);
            if (heady_slope.size() > 5)
                heady_slope.remove(0);
            if (headn_slope.size() > 5)
                headn_slope.remove(0);
        }

    }


    public static boolean resultEyeSine() {
        return sineEye;
    }

    public static boolean resultHeadN() {
        return head_slopeN;
    }

    public static boolean resultHeadY() {
        return head_slopeY;
    }

    public static String resultEmotion() {//6帧图像中哪个表情出现的最多
        if (faces.size() < count) {
            return null;
        }
        return emotion[countPosition()];
    }


    //计算当前用户的情绪状态
    private static int countPosition() {
        //计算n帧图像中 每个的表情

        Map<Integer, Integer> map = new HashMap();
        for (int i = 0; i < faces.size(); i++) {
            int position = emo_list.get(i);
            Integer count = map.get(position);
            map.put(position, (count == null) ? 1 : count + 1);
        }
        //计算哪个表情出现的次数最多

        int max = 0;
        int position = 0;

        Iterator<Integer> iter = map.keySet().iterator();

        while (iter.hasNext()) {
            int key = iter.next();
            int value = map.get(key);
            if (max <= value) {
                position = key;
                max = value;
            }
        }
        return position;
    }

    public static void cleanFace() {
        sineEye = false;
        head_slopeY = false;
        head_slopeN = false;
    }

}
