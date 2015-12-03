package com.readface.cafe.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

import java.util.Random;

/**
 * Created by mac on 15/10/12.
 */
public class FaceUtil {
    private static boolean debug = true;

    public static int getScreenWidth(Context mContext) {
        WindowManager manager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }

    public static int getScreenHeight(Context mContext) {
        WindowManager manager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(dm);
        return dm.heightPixels;
    }

    public static String getDeviceId(Context context) {
        return android.os.Build.SERIAL;
    }

    public static int getRandom() {
        return new Random().nextInt(4) + 4;
    }

    public static void d(String obj) {
        if (debug) {

            Log.d("Anim", obj);
            System.out.println(obj);
        }
    }
}
