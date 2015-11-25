package com.readface.cafe.Iinteface;

/**
 * Created by mac on 15/11/9.
 */
public interface TTSListenerCallback {
   public void callback(String response,String action);

   public void error(String action);
}
