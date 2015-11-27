package com.readface.cafe.utils;

import android.content.Context;
import android.util.Base64;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.readface.cafe.anim.BaseApplication;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

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

    /**
     * 获取token
     *
     * @param listener
     */
    public static void doPostForToken(final HelpListener listener) {
        addRequest(new StringRequest(Request.Method.POST,
                AppUrl.postActivate(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        listener.onResponse(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        listener.onError(error);
                    }
                }
        ) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("robot[uuid]", FaceUtil.getDeviceId(BaseApplication.getIntence()));
                return params;
            }
        });
    }


    /**
     * 请求下一步
     *
     * @param url
     * @param listener
     */
    public static void doGet(String url, final HelpListener listener) {
        addRequest(new StringRequest(
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        listener.onResponse(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        listener.onError(error);
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> heads = new HashMap<String, String>();
                heads.put("token", BaseApplication.getIntence().getToken());
                return heads;
            }
        });
    }


    /**
     * 确认身份
     *
     * @param image
     * @param listener
     */
    public static void postFaceVerify(String URL, final byte[] image, final HelpListener listener) {

        StringRequest request = new StringRequest(Request.Method.POST,
                URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        listener.onResponse(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        listener.onError(error);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("person[image]", "data:image/jpg;base64," + Base64.encodeToString(image, Base64.DEFAULT));
                return params;
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                return super.getBody();
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> heads = new HashMap<String, String>();
                heads.put("token", BaseApplication.getIntence().getToken());

                return heads;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        addRequest(request);
    }

    /**
     * 创建人物
     */
    public static void putUpdatePerson(String url, final String voice, final HelpListener listener) {
        addRequest(new StringRequest(Request.Method.PUT,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        listener.onResponse(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        listener.onError(error);
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("person[voice]", voice);

                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> heads = new HashMap<String, String>();
                heads.put("token", BaseApplication.getIntence().getToken());
                return heads;
            }
        });
    }

    private static void addRequest(Request<?> mRequest) {
        mQueue.add(mRequest);
    }

    public interface HelpListener {
        public void onResponse(String response);

        public void onError(VolleyError error);
    }
}
