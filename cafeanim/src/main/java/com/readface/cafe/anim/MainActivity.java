package com.readface.cafe.anim;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
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
import com.readface.cafe.utils.AppUrl;
import com.readface.cafe.utils.AsyncTaskExecutor;
import com.readface.cafe.utils.FaceUtil;
import com.readface.cafe.utils.HttpParams;
import com.readface.cafe.utils.HttpUtil;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.util.HashMap;
import java.util.LinkedHashMap;


public class MainActivity extends Activity {


    private final String LOG = "MainActivity";
    private Context mContext;
    private SpeechRecognizer mIat;
    private SpeechSynthesizer mTts;

    private HashMap<String, String> mIatResults = new LinkedHashMap<String, String>();

    private Face face;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        RelativeLayout parent = new RelativeLayout(this);
        parent.setLayoutParams(new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        parent.setBackgroundColor(Color.BLACK);
        int screenW = FaceUtil.getScreenWidth(this);
        float radio = screenW / 1080f;
        face = new Face(this, radio);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(screenW, (int) (750 * radio));
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        face.setLayoutParams(layoutParams);
        face.setBackgroundColor(Color.WHITE);
        parent.addView(face);
        setContentView(parent);


        face.action1();
        initActivate();

    }

    /**
     * 激活设备 获取token
     */
    private void initActivate() {
        AsyncTaskExecutor.executeConcurrently(new ActiveTask());

    }


    //TODO 音频服务
    private void initTTS() {
        mIat = SpeechRecognizer.createRecognizer(mContext, mInitListener);//听
        mTts = SpeechSynthesizer.createSynthesizer(mContext, mInitListener);//说
    }

    /**
     * 听
     */
    private void startIATService() {//IAT 启动听服务位置，1：应用启动2：没听到3：说结束
        mIat.setParameter(SpeechConstant.PARAMS, null);
        mIat.setParameter(SpeechConstant.DOMAIN, "iat");
        mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
        mIat.setParameter(SpeechConstant.ACCENT, "mandarin");
        mIat.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
        mIat.setParameter(SpeechConstant.RESULT_TYPE, "json");
        mIat.setParameter(SpeechConstant.VAD_BOS, "6000");
        mIat.setParameter(SpeechConstant.VAD_EOS, "1000");
        mIat.setParameter(SpeechConstant.ASR_PTT, "1");
        mIat.startListening(mRecoListener);
    }

    /**
     * 说
     */
    private void startTTSService(String text) {
        mTts.setParameter(SpeechConstant.PARAMS, null);
        mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
        mTts.setParameter(SpeechConstant.VOICE_NAME, "xiaoyan");
        mTts.setParameter(SpeechConstant.SPEED, "50");//语速
        mTts.setParameter(SpeechConstant.SPEED, "50");//语调
        mTts.setParameter(SpeechConstant.VOLUME, "50");//音量
        mTts.setParameter(SpeechConstant.KEY_REQUEST_FOCUS, "true");
        mTts.startSpeaking(text, mTtsListener);
    }

    private RecognizerListener mRecoListener = new RecognizerListener() {
        @Override
        public void onVolumeChanged(int i, byte[] bytes) {

        }

        @Override
        public void onBeginOfSpeech() {

            Log.d(LOG, "start speech");
        }

        @Override
        public void onEndOfSpeech() {
            Log.d(LOG, "end speech");

        }

        @Override
        public void onResult(RecognizerResult recognizerResult, boolean b) {


            printResult(recognizerResult);
            if (b) {
                StringBuffer resultBuffer = new StringBuffer();
                for (String key : mIatResults.keySet()) {
                    resultBuffer.append(mIatResults.get(key));
                }
                //TODO 处理将录入的语音

                AsyncTaskExecutor.executeConcurrently(new NextTask(), resultBuffer.toString(),"joy");

                Log.d(LOG, "over = " + resultBuffer.toString());

            }
        }

        @Override
        public void onError(SpeechError speechError) {
            Log.d(LOG, speechError.getErrorCode() + ":" + speechError.getErrorDescription());
//            if (speechError.getErrorCode() == 10118) {

                startIATService();
//            }
        }

        @Override
        public void onEvent(int i, int i1, int i2, Bundle bundle) {

        }
    };

    private InitListener mInitListener = new InitListener() {
        @Override
        public void onInit(int code) {//初始化音频服务的监听
            if (code != ErrorCode.SUCCESS) {
                Log.d(LOG, "初始化失败：错误码" + code);
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

    private SynthesizerListener mTtsListener = new SynthesizerListener() {
        @Override
        public void onSpeakBegin() {
            Log.d(LOG, "开始");
            face.action3();
        }

        @Override
        public void onBufferProgress(int i, int i1, int i2, String s) {
            Log.d(LOG, "合成进度" + i);

        }

        @Override
        public void onSpeakPaused() {
            Log.d(LOG, "暂停");
        }

        @Override
        public void onSpeakResumed() {
            Log.d(LOG, "继续播放");
        }

        @Override
        public void onSpeakProgress(int i, int i1, int i2) {}

        @Override
        public void onCompleted(SpeechError speechError) {
            Log.d(LOG, "播放完成");
            startIATService();
            face.action0();
        }

        @Override
        public void onEvent(int i, int i1, int i2, Bundle bundle) {

        }
    };

    private class NextTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            String result = null;
            try {
                result = HttpUtil.doGet(AppUrl.getNext(params[0], params[1]));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            if(s!=null){
                try{
                    Log.d(LOG, "返回结果----" +s);

                    JSONObject result = new JSONObject(s);
                    String voice = result.getJSONObject("action").getString("voice");
                    startTTSService("解析成功");
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

    private class ActiveTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            String result = null;
            try {
                JSONStringer js = new JSONStringer();
                js.object();
                js.key("robot");
                js.object();
                js.key("uuid").value(FaceUtil.getDeviceId(mContext));
                js.endObject();
                js.endObject();
                result = HttpUtil.doPost(AppUrl.postActivate(), HttpParams.getEntity(js.toString()), HttpParams.getHead());
            } catch (Exception e) {
                e.printStackTrace();
            }

            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            Log.d(LOG, s);
            if (s != null) {//设备激活成功
                try {
                    BaseApplication.token = new JSONObject(s).getString("token");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                initTTS();//开启音频服务
                //启动开场白
                startTTSService("嗨，，我是你的好朋友 --小白!");


            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //退出时释放链接
        mIat.stopListening();
        mIat.cancel();
        mIat.destroy();
        mTts.stopSpeaking();
        mTts.destroy();
    }
}
