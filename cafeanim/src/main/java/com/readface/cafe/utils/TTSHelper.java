package com.readface.cafe.utils;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;
import com.readface.cafe.anim.JsonParser;
import com.readface.cafe.Iinteface.*;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Created by mac on 15/11/3.
 */
public class TTSHelper {

    private static final String TAG = "TTSHelper";
    private SpeechRecognizer mIat;
    private SpeechSynthesizer mTts;
    private HashMap<String, String> mIatResults = new LinkedHashMap<String, String>();

    private Context mContext;

    public TTSHelper(Context mContext) {
        this.mContext = mContext;
        initTTS();
    }

    public void ImSpeak(String voice, String thread, final TTSSpeakCallback mCallback) {
        mTts.setParameter(SpeechConstant.SPEED, thread);//语速
        mTts.startSpeaking(voice, new SynthesizerListener() {
            @Override
            public void onSpeakBegin() {
                if (mCallback != null)
                    mCallback.onStart();
            }

            @Override
            public void onBufferProgress(int i, int i1, int i2, String s) {

            }

            @Override
            public void onSpeakPaused() {

            }

            @Override
            public void onSpeakResumed() {

            }

            @Override
            public void onSpeakProgress(int i, int i1, int i2) {

            }

            @Override
            public void onCompleted(SpeechError speechError) {
                if (mCallback != null)
                    mCallback.onComplete();
            }

            @Override
            public void onEvent(int i, int i1, int i2, Bundle bundle) {

            }
        });
    }

    private boolean shake = false;
    private boolean nod = false;

    public void ImListener(final TTSListenerCallback mCallback) {
        mIat.startListening(new RecognizerListener() {
            @Override
            public void onVolumeChanged(int i, byte[] bytes) {
                if (EmotionStatus.resultHeadY()) nod = true;
                if (EmotionStatus.resultHeadN()) shake = true;
            }

            @Override
            public void onBeginOfSpeech() {

            }

            @Override
            public void onEndOfSpeech() {

            }

            @Override
            public void onResult(RecognizerResult recognizerResult, boolean b) {
                printResult(recognizerResult);
                if (b) {
                    StringBuffer resultBuffer = new StringBuffer();
                    for (String key : mIatResults.keySet()) {
                        resultBuffer.append(mIatResults.get(key));
                    }
                    String action = "";
                    if (nod) action = "nod";
                    if (shake) action = "shake";
                    if (nod & shake) action = "";

                    String voice = resultBuffer.toString();
                    if (mCallback != null)
                        mCallback.callback(voice, action);
                }
            }

            @Override
            public void onError(SpeechError speechError) {
                Log.d(TAG, speechError.toString());
                if (!mTts.isSpeaking()) {
                    String action = "";
                    if (nod) action = "nod";
                    if (shake) action = "shake";
                    if (nod & shake) action = "";
                    if (mCallback != null)
                        mCallback.error(action);
                }
            }

            @Override
            public void onEvent(int i, int i1, int i2, Bundle bundle) {

                Log.d(TAG, "onEvent");
            }
        });
    }


    private void printResult(RecognizerResult results) {
        String text = JsonParser.parseIatResult(results.getResultString());

        String sn = null;
        // 读取json结果中的sn字段
        try {
            JSONObject resultJson = new JSONObject(results.getResultString());
            sn = resultJson.optString("sn");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mIatResults.put(sn, text);
    }

    /**
     * 初始化音频服务
     */
    private void initTTS() {
        mIat = SpeechRecognizer.createRecognizer(mContext, new InitListener() {
            @Override
            public void onInit(int code) {
                if (code == ErrorCode.SUCCESS) {
                    mIat.setParameter(SpeechConstant.PARAMS, null);
                    mIat.setParameter(SpeechConstant.DOMAIN, "iat");
                    mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
                    mIat.setParameter(SpeechConstant.ACCENT, "mandarin");
                    mIat.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
                    mIat.setParameter(SpeechConstant.RESULT_TYPE, "json");
                    mIat.setParameter(SpeechConstant.VAD_BOS, "6000");
                    mIat.setParameter(SpeechConstant.VAD_EOS, "1000");
                    mIat.setParameter(SpeechConstant.ASR_PTT, "1");
                    Log.d(TAG, "初始化听成功");
                } else {
                    Log.d(TAG, "初始化听失败：错误码" + code);
                }
            }
        });//听
        mTts = SpeechSynthesizer.createSynthesizer(mContext, new InitListener() {
            @Override
            public void onInit(int code) {
                if (code == ErrorCode.SUCCESS) {
                    mTts.setParameter(SpeechConstant.PARAMS, null);
                    mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
                    mTts.setParameter(SpeechConstant.VOICE_NAME, "nannan");
                    mTts.setParameter(SpeechConstant.SPEED, "80");//语速
                    mTts.setParameter(SpeechConstant.SPEED, "60");//语调
                    mTts.setParameter(SpeechConstant.VOLUME, "50");//音量
                    mTts.setParameter(SpeechConstant.KEY_REQUEST_FOCUS, "true");
                } else {
                    Log.d(TAG, "初始化说失败：错误码" + code);
                }
            }
        });//说
    }

    public void stopAll() {
        if (mIat != null) {
            mIat.stopListening();
        }
        if (mTts != null) {
            mTts.stopSpeaking();
        }
    }


    public void cancle() {
        stopAll();
        if (mIat != null) {
            mIat.destroy();
        }
        if (mTts != null) {
            mTts.destroy();
        }
    }

    public boolean isListen() {
        return mIat.isListening();
    }

    public boolean isSpeak() {
        return mTts.isSpeaking();
    }

    public void stopSpeak(){
        if(mTts.isSpeaking()){
            mTts.stopSpeaking();
        }
    }

}
