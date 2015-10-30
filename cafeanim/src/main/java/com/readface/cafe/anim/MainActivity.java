package com.readface.cafe.anim;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
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
import com.readface.cafe.utils.CameraHelper;
import com.readface.cafe.utils.EmotionStatus;
import com.readface.cafe.utils.FaceUtil;
import com.readface.cafe.utils.StringUtil;
import com.readface.cafe.utils.VolleyHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedHashMap;

import mobile.AUDetection.YMDetector;
import mobile.AUDetection.YMFace;


public class MainActivity extends Activity implements YMDetector.DetectorListener, CameraHelper.ImageListener {


    //引入机器状态码0：正常说话，1：创建人物
    public enum StatusType {
        NomalSpeak, CreatePerson, Play
    }

    private boolean closeTTS = true;

    private StatusType MStatus = StatusType.NomalSpeak;

    private final String TAG = "MainActivity";
    private Context mContext;
    private SpeechRecognizer mIat;
    private SpeechSynthesizer mTts;
    private HashMap<String, String> mIatResults = new LinkedHashMap<String, String>();
    private Face face;
    private TextView tv_desc;


    private CameraHelper mCameraHelper;
    private YMDetector ymDetector;
    private SurfaceView mSurface;

    private String hasPerson = "";

    private long time_count = 0l;

    private int count_speak_number = 0;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        RelativeLayout parent = new RelativeLayout(this);

        int screenW = FaceUtil.getScreenWidth(this);
        float radio = screenW / 1080f;
        face = new Face(this, radio);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(screenW, (int) (750 * radio));
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        face.setLayoutParams(layoutParams);
        face.setBackgroundColor(Color.WHITE);

        mSurface = new SurfaceView(this);

        parent.addView(mSurface);

        View v = new View(mContext);
        v.setLayoutParams(new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
        v.setBackgroundColor(Color.BLACK);
        tv_desc = new TextView(mContext);
        tv_desc.setTextSize(12);

        parent.addView(v);
        parent.addView(face);
        parent.addView(tv_desc);

        time_count = System.currentTimeMillis();
        countDesc("启动，眨眼，激活设备");

        setContentView(parent);
        face.action3();
        initActivate();
    }


    private void countDesc(final String desc) {//描述输出
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                tv_desc.setText(desc +
                        "--time--" + (System.currentTimeMillis() - time_count) + "status = " + MStatus + "\n" + tv_desc.getText().toString());
                time_count = System.currentTimeMillis();
            }
        });
    }

    private void initFaceDetector() {
        mCameraHelper = new CameraHelper(mContext, mSurface);
        mCameraHelper.startDetection(Camera.CameraInfo.CAMERA_FACING_FRONT);
        mCameraHelper.setImageListener(this);
        ymDetector = new YMDetector(mContext, this);
        ymDetector.setLicensePath("readface_android_demo.license");
    }

    /**
     * 激活设备 获取token
     */
    private void initActivate() {
        VolleyHelper.doPostForToken(new VolleyHelper.HelpListener() {
            @Override
            public void onResponse(String s) {
                Log.d(TAG, s);
                if (s != null) {//设备激活成功
                    countDesc("设备激活成功,开启语音服务,面部识别功能");
                    try {
                        BaseApplication.getIntence().token = new JSONObject(s).getString("token");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    initTTS();//开启音频服务
                    initFaceDetector();
                    startTTSService("我是小白!");
                    MStatus = StatusType.NomalSpeak;
                    //启动开场白
                }
            }

            @Override
            public void onError(VolleyError error) {
                countDesc("获取token失败");
                Log.d(TAG, "error log = " + error.getMessage());
            }
        });
    }


    //TODO 音频服务
    private void initTTS() {
        if (closeTTS) return;
        mIat = SpeechRecognizer.createRecognizer(mContext, mInitListener);//听
        mTts = SpeechSynthesizer.createSynthesizer(mContext, mInitListener);//说
    }

    /**
     * 听
     */
    private void startIATService() {//IAT 启动听服务位置，1：应用启动2：没听到3：说结束
        if (closeTTS) return;
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
        if (closeTTS) return;
        if (mTts.isSpeaking()) return;
        mTts.setParameter(SpeechConstant.PARAMS, null);
        mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
        mTts.setParameter(SpeechConstant.VOICE_NAME, "nannan");
        mTts.setParameter(SpeechConstant.SPEED, "80");//语速
        mTts.setParameter(SpeechConstant.SPEED, "60");//语调
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
            Log.d(TAG, "start speech");
            countDesc("start Listen");
        }

        @Override
        public void onEndOfSpeech() {
            Log.d(TAG, "end speech");

        }

        @Override
        public void onResult(RecognizerResult recognizerResult, boolean b) {

            printResult(recognizerResult);
            if (b) {
                StringBuffer resultBuffer = new StringBuffer();
                for (String key : mIatResults.keySet()) {
                    resultBuffer.append(mIatResults.get(key));
                }

                final String voice = resultBuffer.toString();
                countDesc("Listen success --" + voice);

                switch (MStatus) {
                    case NomalSpeak:
                        //TODO 处理将录入的语音 接收下一步动作的指示
                        VolleyHelper.doGetNext(AppUrl.getNext(resultBuffer.toString(),
                                        BaseApplication.getIntence().getUser_emotion()),
                                new VolleyHelper.HelpListener() {
                                    @Override
                                    public void onResponse(String s) {
                                        if (s != null) {
                                            try {
                                                countDesc("next success" + s);
                                                JSONObject result = new JSONObject(s);
                                                String voice = result.getJSONObject("action").getString("voice");
                                                startTTSService(voice);
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }

                                    @Override
                                    public void onError(VolleyError error) {
                                        Log.d(TAG, "error log = " + error.getMessage());
                                        countDesc("next error");
                                        startTTSService("我没听懂，再说一次吧");
                                    }
                                });
                        break;
                    case CreatePerson://认识这个人，，确定名字
                        MStatus = StatusType.NomalSpeak;
                        VolleyHelper.putUpdatePerson(AppUrl.putUpdatePerson(BaseApplication.getIntence().id)
                                , voice
                                , new VolleyHelper.HelpListener() {
                            @Override
                            public void onResponse(String s) {
                                if (s != null) {
                                    try {
                                        countDesc("put for renew name success --" + s);
                                        JSONObject result = new JSONObject(s);
                                        String voice = result.getJSONObject("action").getString("voice");
                                        startTTSService(voice);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }

                            @Override
                            public void onError(VolleyError error) {
                                countDesc("put error");
                            }
                        });

                        break;

                }
            }
        }

        @Override
        public void onError(SpeechError speechError) {
            countDesc("Listen error --" + speechError.getErrorDescription());
            if (!mTts.isSpeaking()) {
                if (MStatus == StatusType.CreatePerson) {//在询问名字的时候不回答

                    if (count_speak_number >= 1) {
                        startTTSService("呜呜，你都不告诉我你叫什么名字，那我以后就叫你小朋友了哦");
                        MStatus = StatusType.NomalSpeak;
                    } else {
                        startTTSService("你好啊，我叫小白，你叫什么名字");
                        count_speak_number++;
                    }
                } else {
                    startIATService();
                }
            }
        }

        @Override
        public void onEvent(int i, int i1, int i2, Bundle bundle) {

        }
    };

    private InitListener mInitListener = new InitListener() {
        @Override
        public void onInit(int code) {//初始化音频服务的监听
            if (code != ErrorCode.SUCCESS) {
                Log.d(TAG, "初始化失败：错误码" + code);
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

            face.action3();
            countDesc("speak start");
            Log.d(TAG, "开始");
        }

        @Override
        public void onBufferProgress(int i, int i1, int i2, String s) {
            Log.d(TAG, "合成进度" + i);

        }

        @Override
        public void onSpeakPaused() {
            Log.d(TAG, "暂停");
        }

        @Override
        public void onSpeakResumed() {
            Log.d(TAG, "继续播放");
        }

        @Override
        public void onSpeakProgress(int i, int i1, int i2) {
        }

        @Override
        public void onCompleted(SpeechError speechError) {
            Log.d(TAG, "播放完成");
            countDesc("speak over");
            face.action0();
            startIATService();
        }

        @Override
        public void onEvent(int i, int i1, int i2, Bundle bundle) {

        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //退出时释放链接
        if (mIat != null) {
            mIat.stopListening();
            mIat.cancel();
            mIat.destroy();
        }
        if (mTts != null) {
            mTts.stopSpeaking();
            mTts.destroy();
        }
        if (mCameraHelper != null && mCameraHelper.isDetecting()) {
            mCameraHelper.stopDetection();
        }
    }

    /**
     * Face Detector
     *
     * @param ymFace
     * @param v
     */

    private boolean face_success = true;

    @Override
    public void onSuccess(YMFace ymFace, byte[] data, float v) {

        hasPerson += "a";
        if (hasPerson.length() > 20) {
            hasPerson = hasPerson.substring(1, hasPerson.length());
            if (face_success && StringUtil.getCharCount(hasPerson, 'a') >= 18) {
                face_success = false;

                countDesc("获取到人脸信息，发起检测");
                hasPerson = "";
                VolleyHelper.postFaceVerify(data, new VolleyHelper.HelpListener() {
                    @Override
                    public void onResponse(String s) {
                        countDesc(s);
                        try {
                            JSONObject result = new JSONObject(s);
                            BaseApplication.getIntence().id = result.getJSONObject("person").getString("id");
                            String voice = result.getJSONObject("action").getString("voice");

                            if (result.getJSONObject("person").getBoolean("recognized")) {//1：熟悉的人
                                countDesc("检测结果:--认识");
                                MStatus = StatusType.NomalSpeak;
                                startTTSService(voice);
                            } else {
                                //2: 陌生人 创建人物 start voice:你好啊，我是你的新朋友，小白，请问你叫什么名字
                                countDesc("检测结果:--不认识");
                                MStatus = StatusType.CreatePerson;
                                startTTSService(voice);
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(VolleyError error) {
                        Log.d(TAG, "检测人脸error---" + error.getMessage());
                        countDesc("检测人脸失败");
                    }
                });

            }
        }
        //保存最近的几面部信息用于计算当前用户的情绪状态变化
        EmotionStatus.addFace(ymFace);

        countDesc("检测到人脸 嘴部离散度"+EmotionStatus.resultMouth());
    }

    @Override
    public void onError() {

        hasPerson += "b";
        if (hasPerson.length() > 20) {
            hasPerson = hasPerson.substring(1, hasPerson.length());
            if (StringUtil.getCharCount(hasPerson, 'b') >= 18) {
                hasPerson = "";
                face_success = true;
                countDesc("摄像头采集不到人脸信息 ");
            }
        }
    }

    private boolean isDetector = false;

    @Override
    public void onDataResults(final byte[] data, Camera camera) {
        if (!isDetector) {
            isDetector = true;
            final float iw = camera.getParameters().getPreviewSize().width;
            final float ih = camera.getParameters().getPreviewSize().height;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    ymDetector.onDetector(data, iw, ih, Camera.CameraInfo.CAMERA_FACING_FRONT);
                    isDetector = false;
                }
            }).start();
        }
    }


}
