package com.readface.cafe.anim;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.android.volley.VolleyError;
import com.readface.cafe.Iinteface.TTSListenerCallback;
import com.readface.cafe.Iinteface.TTSSpeakCallback;
import com.readface.cafe.robot.Face;
import com.readface.cafe.robot.Robot;
import com.readface.cafe.utils.AnimHelper;
import com.readface.cafe.utils.AppUrl;
import com.readface.cafe.utils.CameraHelper;
import com.readface.cafe.utils.EmotionStatus;
import com.readface.cafe.utils.FaceUtil;
import com.readface.cafe.utils.StatusType;
import com.readface.cafe.utils.StringUtil;
import com.readface.cafe.utils.TTSHelper;
import com.readface.cafe.utils.VolleyHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import mobile.AUDetection.YMDetector;
import mobile.AUDetection.YMFace;


public class TestActivity extends Activity implements YMDetector.DetectorListener, CameraHelper.ImageListener {

    private final String TAG = "TestActivity";

    private String currEmo = EmotionStatus.emotion[0];
    private Context mContext;

    private Robot mRobot;
    private Face face;
    private TextView tv_desc;


    private CameraHelper mCameraHelper;
    private YMDetector ymDetector;
    private SurfaceView mSurface;

    private String hasPerson = "";

    private long time_count = 0l;

    private TTSHelper ttsHelper;

    private boolean isPlay = false;
    boolean isTouch = false;
    private int speakCount = 1;
    private String new_person = "";

    private JSONArray listForSpeak = null;
    private int speakForListCount = 0;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;

        RelativeLayout parent = new RelativeLayout(this);
        int screenW = FaceUtil.getScreenWidth(this);
        int screenH = FaceUtil.getScreenHeight(this);
        float radio = screenW / 640f;
        int frameH = (int) (810 * radio);
        mRobot = new Robot(this, radio);
        RelativeLayout.LayoutParams robotLa = new RelativeLayout.LayoutParams(screenW, frameH);
        robotLa.setMargins(0, (screenH - frameH) * 2 / 5, 0, 0);
        mRobot.setLayoutParams(robotLa);

        mSurface = new SurfaceView(this);
        parent.addView(mSurface);
        tv_desc = new TextView(mContext);
        tv_desc.setTextSize(12);

        VideoView v = new VideoView(mContext);
        v.setLayoutParams(new RelativeLayout.LayoutParams(screenW, screenH));
        String uri = "android.resource://" + getPackageName() + "/" + R.raw.bg_v;
        v.setVideoURI(Uri.parse(uri));
        v.start();
        View view = new View(mContext);
        view.setLayoutParams(new RelativeLayout.LayoutParams(screenW, screenH));
        view.setBackgroundResource(R.mipmap.main_bg);
        parent.addView(view);
        parent.addView(v);
        parent.addView(mRobot);
        parent.addView(tv_desc);

        time_count = System.currentTimeMillis();
        countDesc("启动，眨眼，激活设备");

//        ListView list = new ListView(mContext);
//        list.setAdapter(new ArrayAdapter<String>(this, R.layout.grid_item, new String[]{"眩晕", "安慰", "眨眼", "连续眨眼", "悲伤1",
//                "悲伤2", "哭泣", "委屈", "鬼脸", "亲吻", "困", "笑1", "笑2", "笑3", "笑4", "怒1", "怒2", "惊讶"}));
//        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//                switch (position) {
//                    case 0:
//                        face.animGosh();
//                        break;
//                    case 1:
//                        face.comfort();
//                        break;
//                    case 2:
//                        face.eyeSine();
//                        break;
//                    case 3:
//                        face.blink();
//                        break;
//                    case 4:
//                        face.sad1();
//                        break;
//                    case 5:
//                        face.sad2();
//                        break;
//                    case 6:
//                        face.cry();
//                        break;
//                    case 7:
//                        face.grievance();
//                        break;
//                    case 8:
//                        face.grimace();
//                        break;
//                    case 9:
//                        face.kiss();
//                        break;
//                    case 10:
//                        face.trapped();
//                        break;
//                    case 11:
//                        face.smile1();
//                        break;
//                    case 12:
//                        face.smile2();
//                        break;
//                    case 13:
//                        face.smile3();
//                        break;
//                    case 14:
//                        face.smile4();
//                        break;
//                    case 15:
//                        face.ang1();
//                        break;
//                    case 16:
//                        face.ang2();
//                        break;
//                    case 17:
//                        face.sub();
//                        break;
//                }
//            }
//        });
//
//        parent.addView(list);
        setContentView(parent);

        initActivate();
        BaseApplication.getIntence().setStatus(StatusType.Nomal);
        face = mRobot.getFace();
        mRobot.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if (!isTouch) {
                            isTouch = true;
                            if (listForSpeak != null && listForSpeak.length() == 3 && speakForListCount == 2) {
                                ttsHelper.stopSpeak();
                                JSONObject stopSpeak = new JSONObject();
                                try {
                                    stopSpeak.put("text", "哎呀，打断我干吗");
                                    stopSpeak.put("speed", "70");
                                    stopSpeak.put("touch", "0");
                                    listForSpeak.put(1, stopSpeak);
                                    speakForListCount = 1;
                                    initSpeak();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        isTouch = false;
                        break;
                }
                return true;
            }
        });


        mHandler.sendEmptyMessageDelayed(3, FaceUtil.getRandom() * 1000);


    }


    private Handler mHandler = new Handler() {
        @Override
        public void dispatchMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    statusChange((JSONObject) msg.obj);
                    break;
                case 2:
                    initActivate();
                    break;
                case 3:
                    if (getStatus() != StatusType.Play) {
                        face.eyeSine();
                    }
                    mHandler.sendEmptyMessageDelayed(3, FaceUtil.getRandom() * 1000);
                    break;
            }
        }
    };

    private void countDesc(final String desc) {//描述输出
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                tv_desc.setText(desc +
                        "--time--" + (System.currentTimeMillis() - time_count) + "status = "
                        + getStatus() + "\n" + tv_desc.getText().toString());
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
                if (s != null) {//设备激活成功
                    countDesc("设备激活成功,开启语音服务" + s);
                    try {
                        BaseApplication.getIntence().setToken(new JSONObject(s).getString("token"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    initFaceDetector();
                    ttsHelper = new TTSHelper(mContext);//开启音频服务
                }
            }

            @Override
            public void onError(VolleyError error) {
                countDesc("获取token失败,重新获取中……");
                Message msg = new Message();
                msg.what = 2;
                mHandler.sendMessageDelayed(msg, 3000);
            }
        });
    }


    private void initListener() {
        if (ttsHelper != null && ttsHelper.isListen()) return;
        if (ttsHelper == null) return;
        ttsHelper.ImListener(new TTSListenerCallback() {
            @Override
            public void callback(String response, String action) {

                countDesc("监听结果--" + response);

                StatusType MStatus = getStatus();
                switch (MStatus) {
                    case CreatePerson://认识这个人，，确定名字
                        upDateUse(response);
                        break;
                    case PreCreate:
                        getAskName(speakCount + "", response);
                        break;
                    default:
                        getNext(response, "0", action);
                        break;
                }
            }

            @Override
            public void error(String action) {
                countDesc("listen error ");

                if (getStatus() == StatusType.PreCreate) {
                    getAskName(speakCount + "");
                } else {
                    getNext("", speakCount + "", action);
                }
            }
        });
    }


    public void initSpeak() {
        if (ttsHelper != null && ttsHelper.isSpeak() && !isTouch) return;

        if (ttsHelper == null) return;

        String voice = null;
        String speed = null;
        String touch = null;
        try {
            if (speakForListCount >= listForSpeak.length()) {
                speakForListCount = 0;
                initListener();
                listForSpeak = null;
                return;
            }
            JSONObject current = listForSpeak.getJSONObject(speakForListCount);
            voice = current.getString("text");
            speed = current.getString("speed");
            touch = current.getString("touch");

            speakForListCount++;
        } catch (Exception e) {
            e.printStackTrace();
        }

        ttsHelper.ImSpeak(voice, speed, new TTSSpeakCallback() {
            @Override
            public void onStart() {
                face.mouthStartSpeakAnim();
                countDesc("speak start");
            }

            @Override
            public void onComplete() {
                face.mouthStopSpeakAnim();
                countDesc("speak complete");
                if (StringUtil.isNotEmpty(new_person)) {
                    speakForListCount = 0;
                    BaseApplication.getIntence().setStatus(StatusType.CreatePerson);
                    upDateUse(new_person);
                    new_person = null;
                } else {
                    initSpeak();
                }
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //退出时释放链接
        if (ttsHelper != null) {
            ttsHelper.cancle();
            ttsHelper = null;
        }
        if (mCameraHelper != null && mCameraHelper.isDetecting()) {
            mCameraHelper.stopDetection();
        }

    }


    @Override
    public void onSuccess(YMFace ymFace, byte[] bytes, float v) {
        EmotionStatus.addFace(ymFace);
        hasPerson += "b";
        switch (getStatus()) {
            case Nomal:
                if (hasPerson.length() > 20)
                    hasPerson = hasPerson.substring(1, hasPerson.length());
                if (face_success && StringUtil.getCharCount(hasPerson, 'b') >= 18) {
                    face_success = false;

                    countDesc("获取到人脸信息，发起检测");
                    hasPerson = "";
                    VolleyHelper.postFaceVerify(bytes, new VolleyHelper.HelpListener() {
                        @Override
                        public void onResponse(String s) {
                            countDesc(s);
                            try {
                                JSONObject result = new JSONObject(s);
                                BaseApplication.getIntence().setId(result.getJSONObject("person").getString("id"));
                                listForSpeak = result.getJSONObject("action").getJSONArray("voice");
                                if (result.getJSONObject("person").getBoolean("recognized")) {
                                    countDesc("检测结果:--认识");
                                    BaseApplication.getIntence().setStatus(StatusType.Nomal);
                                } else {
                                    countDesc("检测结果:--不认识");
                                    BaseApplication.getIntence().setStatus(StatusType.PreCreate);
                                }
                                initSpeak();

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onError(VolleyError error) {
                            countDesc("检测人脸失败");
                        }
                    });
                }
                break;
            case Play://开始玩 表情跟随

                if (face.animTag[16]) return;
                if (EmotionStatus.resultHeadY()) {
                    Log.d("TestActivity", "点头");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            face.kiss();
                        }
                    });
                    return;
                }
                if (face.animTag[8]) return;
                if (EmotionStatus.resultHeadN()) {
                    Log.d("TestActivity", "摇头");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            face.animGosh();
                        }
                    });
                    return;
                }

                if (EmotionStatus.resultEyeSine()) {
                    face.eyeSine();
                    Log.d("TestActivity", "眨眼");
                }
                String emo = EmotionStatus.resultEmotion();
                if (emo != null && !emo.equals(currEmo)) {
                    int position = EmotionStatus.qualEmo.get(emo);
                    Log.d(TAG, position + "--情绪 = " + emo);
                    currEmo = emo;
                    switch (position) {
                        case 0:
                            face.emo0();
                            break;
                        case 1:
                            face.emo1();
                            break;
                        case 2:
                            face.emo2();
                            break;
                        case 3:
                            face.emo3();
                            break;
                        case 4:
                            face.emo4();
                            break;
                        case 5:
                            face.emo5();
                            break;
                        case 6:
                            face.emo6();
                            break;
                        default://表情优先

                            break;
                    }
                }

                break;
            default:
                hasPerson = "";
                break;
        }
    }

    @Override
    public void onError() {
        hasPerson += "a";
        if (hasPerson.length() > 20)
            hasPerson = hasPerson.substring(1, hasPerson.length());
        if (StringUtil.getCharCount(hasPerson, 'a') >= 18) {
            hasPerson = "";
            EmotionStatus.cleanFace();
            countDesc("人脸检测失败");
        }
    }

    private boolean isDetector = false;
    private boolean face_success = true;

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

    private StatusType getStatus() {
        return BaseApplication.getIntence().getStatus();
    }


    void getNext(String voice, String count, String action) {
        VolleyHelper.doGet(AppUrl.getNext(voice, count,
                        BaseApplication.getIntence().getUser_emotion(), action),
                new VolleyHelper.HelpListener() {
                    @Override
                    public void onResponse(String s) {
                        if (s != null) {
                            try {
                                countDesc("next success---:" + s);
                                JSONObject result = new JSONObject(s).getJSONObject("action");
                                String anim = result.getString("emotion");

                                if (StringUtil.isNotEmpty(anim) && getStatus() != StatusType.Play) {//3秒动画，开始下一步
                                    speakAnim(anim);
                                    Message msg = new Message();
                                    msg.obj = result;
                                    msg.what = 1;
                                    mHandler.sendMessageDelayed(msg, 3000);
                                } else {
                                    statusChange(result);
                                }

                            } catch (Exception e) {
                                Log.d(TAG, e.getCause() + "");
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onError(VolleyError error) {
                        initListener();
                    }
                });
    }

    private void getAskName(String count) {
        getAskName(count, "");
    }

    private void getAskName(String count, String voice) {
        VolleyHelper.doGet(AppUrl.getAsk(BaseApplication.getIntence().getId(), count, voice), new VolleyHelper.HelpListener() {
            @Override
            public void onResponse(String s) {
                if (s != null) {
                    try {
                        countDesc("ask name success---:" + speakCount + ":" + s);
                        JSONObject result = new JSONObject(s).getJSONObject("action");
                        String respond_voice = null;
                        if (result.has("respond_voice"))
                            respond_voice = result.getString("respond_voice");

                        if (StringUtil.isNotEmpty(respond_voice)) {
                            speakCount = 1;
                            new_person = respond_voice;
                        } else {
                            speakCount++;
                        }
                        listForSpeak = result.getJSONArray("voice");
                        initSpeak();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onError(VolleyError error) {
                String errorCode = error.networkResponse.statusCode + "";
                countDesc("ask error---" + errorCode);
                initListener();
            }
        });
    }

    private void upDateUse(String response) {
        VolleyHelper.putUpdatePerson(AppUrl.putUpdatePerson(BaseApplication.getIntence().getId())
                , response
                , new VolleyHelper.HelpListener() {
            @Override
            public void onResponse(String s) {
                if (s != null) {
                    try {
                        BaseApplication.getIntence().setStatus(StatusType.Nomal);

                        countDesc("put for renew name success --" + s);
                        JSONObject result = new JSONObject(s);
                        listForSpeak = result.getJSONObject("action").getJSONArray("voice");
                        initSpeak();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onError(VolleyError error) {
                countDesc("put error---" + error.networkResponse.statusCode);
                initListener();
            }
        });
    }

    void statusChange(JSONObject result) {
        try {
            int id = result.getInt("behavior_id");
            switch (id) {
                case 3://玩
                    if (!isPlay) {
                        isPlay = true;
                        listForSpeak = result.getJSONArray("voice");
                        initSpeak();
                        BaseApplication.getIntence().setStatus(StatusType.Play);
                    } else {
                        initListener();
                    }
                    break;
                case 2://讲故事
                    isPlay = false;
                    face.emo6();
                    BaseApplication.getIntence().setStatus(StatusType.Nomal);
                    listForSpeak = result.getJSONArray("voice");
                    initSpeak();
                    break;
                case 0://正常
                default://其他
                    isPlay = false;
                    face.emo6();
                    BaseApplication.getIntence().setStatus(StatusType.Nomal);
                    listForSpeak = result.getJSONArray("voice");
                    initSpeak();
                    break;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    void speakAnim(String anim) {
        countDesc("表情--" + anim);
        int position = -1;
        try {
            position = AnimHelper.getAnimPosition(anim);
        } catch (Exception e) {

        }
        switch (position) {
            case 0:
                face.blink();
                break;
            case 1:
                face.comfort();
                break;
            case 2:
                face.grievance();
                break;
            case 3:
                face.grimace();
                break;
            case 4:
                face.happy_initial();
                break;
            case 5:
                face.sad1();
                break;
            case 6:
                face.sad2();
                break;
            case 7:
                face.cry();
                break;
            case 8:
                face.smile1();
                break;
            case 9:
                face.smile2();
                break;
            case 10:
                face.smile3();
                break;
            case 11:
                face.smile4();
                break;
            case 12:
                face.trapped();
                break;
            case 13:
                face.kiss();
                break;
            case 14:
                face.ang1();
                break;
            case 15:
                face.ang2();
                break;
            case 16:
                face.sub();
                break;
            default:
                break;
        }
    }
}
