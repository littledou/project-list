package com.readface.cafe.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

/**
 * Created by mac on 15/10/12.
 */
public class FaceUtil {
    public static int getScreenWidth(Context mContext) {
        WindowManager manager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }

    public static String getDeviceId(Context context) {
        return android.os.Build.SERIAL;
    }

}
