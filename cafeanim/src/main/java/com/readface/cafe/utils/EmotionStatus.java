package com.readface.cafe.utils;

import java.util.ArrayList;
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
            "sad",
            "fear",
            "anger",
            "surp",
            "disg",
            "nomal"
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
    private static List<YMFace> faces = new ArrayList<>();

    public static void addFace(YMFace face) {
        faces.add(face);
        if (faces.size() > count)
            faces.remove(0);
    }

    public static String resultEmotion() {//6帧图像中哪个表情出现的最多
        if (faces.size() < count) {
            return null;
        }
        return emotion[countPosition()];
    }

    public static double resultMouth() {
        if (faces.size() < count) {
            return 0;
        }
        int[] tar = new int[faces.size()];
        for (int i = 0; i < faces.size(); i++) {
            tar[i] = (int) faces.get(i).getFacialActions()[2];
        }
        return isOffest(tar);
    }

    private static double isOffest(int[] target) {

        int nom = 0;
        for (int cur : target) {
            nom += cur;
        }
        nom = nom / target.length;//平均数
        int vari = 0;
        for (int cur : target) {
            vari += (cur - nom) * (cur - nom);
        }
        vari = vari / target.length;//方差
        double stand = Math.sqrt(vari);//标准差
        return stand;
    }


    //计算当前用户的情绪状态
    private static int countPosition() {
        //计算n帧图像中 每个的表情

        Map<Integer, Integer> map = new HashMap();
        for (int i = 0; i < faces.size(); i++) {
            int position = 0;
            float max = 0;
            float emo[] = faces.get(i).getEmotions();
            for (int j = 0; j < emo.length; j++) {//7个表情
                if (max <= emo[j]) {
                    max = emo[j];
                    position = j;
                }
            }
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


}
