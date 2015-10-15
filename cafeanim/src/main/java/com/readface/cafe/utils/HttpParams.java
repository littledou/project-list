package com.readface.cafe.utils;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;

import java.io.UnsupportedEncodingException;

/**
 * Created by mac on 15/10/15.
 */
public class HttpParams {
    public static Header[] getHead() {
        Header[] mHeader = {
                new BasicHeader("Content-Type", "application/json")
        };
        return mHeader;
    }

    public static HttpEntity getEntity(String json) {

        HttpEntity mEntity = null;
        try {
            mEntity = new StringEntity(json);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return mEntity;
    }
}
