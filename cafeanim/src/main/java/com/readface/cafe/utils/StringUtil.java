package com.readface.cafe.utils;

/**
 * Created by mac on 15/10/21.
 */
public class StringUtil {
    public static int getCharCount(String str, char c) {
        int count = 0;
        for (int i = 0; i < str.length(); i++) {
            if (c == str.charAt(i)) {
                count++;
            }
        }
        return count;
    }

    public final static boolean isEmpty(String s){
        return s==null||s.trim().length()==0;
    }
    public final static boolean isNotEmpty(String s){
        return !isEmpty(s);
    }
}
