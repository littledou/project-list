package com.readface.cafe.utils;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

/**
 * Created by mac on 15/10/19.
 */
public class VolleyHelper {

    private static RequestQueue mQueue;

    public static RequestQueue initQueue(Context mContext) {
        if (mQueue == null) {
            mQueue = Volley.newRequestQueue(mContext);
        }
        return mQueue;
    }

    public static void doGet(String url,final HelpListener listener){

    }



    private void addRequest(Request<?> mRequest) {
        mQueue.add(mRequest);
    }

    public interface HelpListener {
        public void onResponse(String response);

        public void onError(VolleyError error);
    }
}
