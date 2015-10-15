package com.readface.cafe.anim;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedHashMap;


public class MainActivity extends Activity {

    private Context mContext;
    private SpeechRecognizer mIat;
    private SpeechSynthesizer mTts;

    private HashMap<String, String> mIatResults = new LinkedHashMap<String, String>();
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        RelativeLayout parent = new RelativeLayout(this);

        parent.setLayoutParams(new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        parent.setBackgroundColor(Color.BLACK);

        int screenW = FaceUtil.getScreenWidth(this);
        float radio = screenW / 1080f;
        final Face face = new Face(this, radio);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(screenW, (int) (750 * radio));
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        face.setLayoutParams(layoutParams);
        face.setBackgroundColor(Color.WHITE);
        parent.addView(face);

        setContentView(parent);


        LinearLayout testList = new LinearLayout(this);
        testList.setOrientation(LinearLayout.VERTICAL);
        Button startSpeak = new Button(this);
        startSpeak.setText("action1");
        startSpeak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                face.action1();
            }
        });

        testList.addView(startSpeak);
        Button stopSpeak = new Button(this);
        stopSpeak.setText("action2");
        stopSpeak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                face.action2();
            }
        });
        testList.addView(stopSpeak);

        Button startSight = new Button(this);
        startSight.setText("action3");
        startSight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                face.action3();
            }
        });
        testList.addView(startSight);

        Button startShock = new Button(this);
        startShock.setText("action4");
        startShock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                face.action4();
                startIATService();
            }
        });
        testList.addView(startShock);


        parent.addView(testList);

        mIat = SpeechRecognizer.createRecognizer(mContext, mInitListener);
        mTts = SpeechSynthesizer.createSynthesizer(mContext,mInitListener);
    }

    //TODO 音频服务

    public void startIATService(){//IAT
        mIat.setParameter(SpeechConstant.PARAMS, null);
        mIat.setParameter(SpeechConstant.DOMAIN, "iat");
        mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
        mIat.setParameter(SpeechConstant.ACCENT, "mandarin");
        mIat.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
        mIat.setParameter(SpeechConstant.RESULT_TYPE, "json");
        mIat.setParameter(SpeechConstant.VAD_BOS, "4000");
        mIat.setParameter(SpeechConstant.VAD_EOS, "1000");
        mIat.setParameter(SpeechConstant.ASR_PTT, "1");


        mIat.startListening(mRecoListener);
    }

    private RecognizerListener mRecoListener = new RecognizerListener() {
        @Override
        public void onVolumeChanged(int i, byte[] bytes) {

        }

        @Override
        public void onBeginOfSpeech() {

            Log.d("SpeechResult", "start speech");
        }

        @Override
        public void onEndOfSpeech() {
            Log.d("SpeechResult", "end speech");

        }

        @Override
        public void onResult(RecognizerResult recognizerResult, boolean b) {
            printResult(recognizerResult);
            if(b){
                StringBuffer resultBuffer = new StringBuffer();
                for (String key : mIatResults.keySet()) {
                    resultBuffer.append(mIatResults.get(key));
                }
                startTTSService(resultBuffer.toString());
                Log.d("SpeechResult",resultBuffer.toString());
            }
        }

        @Override
        public void onError(SpeechError speechError) {
            Log.d("SpeechResult", speechError.getErrorCode()+":"+speechError.getErrorDescription());
        }

        @Override
        public void onEvent(int i, int i1, int i2, Bundle bundle) {

        }
    };

    private InitListener mInitListener = new InitListener() {
        @Override
        public void onInit(int code) {//初始化音频服务的监听
            if(code != ErrorCode.SUCCESS){
                Log.d("SpeechResult","初始化失败：错误码"+code);
            }
        }
    };
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


    //TODO TTS

    private void startTTSService(String text){
        mTts.setParameter(SpeechConstant.PARAMS,null);
        mTts.setParameter(SpeechConstant.ENGINE_TYPE,SpeechConstant.TYPE_CLOUD);
        mTts.setParameter(SpeechConstant.VOICE_NAME,"xiaoyan");
        mTts.setParameter(SpeechConstant.SPEED,"50");//语速
        mTts.setParameter(SpeechConstant.SPEED,"50");//语调
        mTts.setParameter(SpeechConstant.VOLUME,"50");//音量
        mTts.setParameter(SpeechConstant.KEY_REQUEST_FOCUS,"true");
        mTts.startSpeaking(text,mTtsListener);

    }

    private SynthesizerListener mTtsListener = new SynthesizerListener() {
        @Override
        public void onSpeakBegin() {
            Log.d("SpeechResult","开始");
        }

        @Override
        public void onBufferProgress(int i, int i1, int i2, String s) {
            Log.d("SpeechResult","合成进度"+i);

        }

        @Override
        public void onSpeakPaused() {
            Log.d("SpeechResult","暂停");
        }

        @Override
        public void onSpeakResumed() {
            Log.d("SpeechResult","继续播放");
        }

        @Override
        public void onSpeakProgress(int i, int i1, int i2) {
            Log.d("SpeechResult","播放进度"+i);
        }

        @Override
        public void onCompleted(SpeechError speechError) {
            Log.d("SpeechResult","播放完成");

        }

        @Override
        public void onEvent(int i, int i1, int i2, Bundle bundle) {

        }
    };
}
