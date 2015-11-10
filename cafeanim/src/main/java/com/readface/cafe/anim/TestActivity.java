package com.readface.cafe.anim;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.readface.cafe.Iinteface.TTSListenerCallback;
import com.readface.cafe.Iinteface.TTSSpeakCallback;
import com.readface.cafe.utils.AppUrl;
import com.readface.cafe.utils.CameraHelper;
import com.readface.cafe.utils.EmotionStatus;
import com.readface.cafe.utils.FaceUtil;
import com.readface.cafe.utils.StatusType;
import com.readface.cafe.utils.StringUtil;
import com.readface.cafe.utils.TTSHelper;
import com.readface.cafe.utils.VolleyHelper;

import org.json.JSONException;
import org.json.JSONObject;

import mobile.AUDetection.YMDetector;
import mobile.AUDetection.YMFace;


public class TestActivity extends Activity implements YMDetector.DetectorListener, CameraHelper.ImageListener {

    private final String TAG = "TestActivity";

    private String currEmo = EmotionStatus.emotion[0];
    private Context mContext;

    private Face face;
    private TextView tv_desc;


    private CameraHelper mCameraHelper;
    private YMDetector ymDetector;
    private SurfaceView mSurface;

    private String hasPerson = "";

    private long time_count = 0l;

    private int count_speak_number = 0;

    private TTSHelper ttsHelper;

    private boolean isPlay = false;

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

        ListView lv = new ListView(mContext);
        lv.setSelector(new ColorDrawable(Color.TRANSPARENT));
        lv.setDivider(new ColorDrawable(Color.TRANSPARENT));
        parent.addView(lv);
        lv.setAdapter(new ArrayAdapter<String>(mContext, R.layout.grid_item
                , new String[]{"闭l眼", "睁l眼", "闭r眼", "睁r眼", "眼睛全部闭上", "眼睛全部睁开"
                , "眩晕", "喜悦", "悲伤", "愤怒", "惊讶", "正常", "眨眼", "连续眨眼", "安慰", "委屈"
                , "鬼脸", "开心初始", "悲伤1", "悲伤2", "大哭", "笑1", "笑2", "笑3", "笑4"}));
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        face.animCloseLeftEye();
                        break;
                    case 1:
                        face.animOpenLeftEye();
                        break;
                    case 2:
                        face.animCloseRightEye();
                        break;
                    case 3:
                        face.animOpenRightEye();
                        break;
                    case 4:
                        face.closeAllEye();
                        break;
                    case 5:
                        face.openAllEye();
                        break;
                    case 6:
                        face.animGosh();
                        break;
                    case 7:
                        face.emo0();
                        break;
                    case 8:
                        face.emo1();
                        break;
                    case 9:
                        face.emo3();
                        break;
                    case 10:
                        face.emo4();
                        break;
                    case 11:
                        face.emo6();
                        break;
                    case 12:
                        face.eyeSine();
                        break;
                    case 13:
                        face.blink1();
                        break;
                    case 14:
                        face.comfort();
                        break;
                    case 15:
                        face.grievance();
                        break;
                    case 16:
                        face.grimace();
                        break;
                    case 17:
                        face.happy_initial();
                        break;
                    case 18:
                        face.sad1();
                        break;
                    case 19:
                        face.sad2();
                        break;
                    case 20:
                        face.cry();
                        break;
                    case 21:
                        face.smile1();
                        break;
                    case 22:
                        face.smile2();
                        break;
                    case 23:
                        face.smile3();
                        break;
                    case 24:
                        face.smile4();
                        break;

                }
            }
        });
//        initActivate();
        BaseApplication.getIntence().setStatus(StatusType.Nomal);
    }


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
                    //启动开场白
//                    checkIsPerson();
                }
            }

            @Override
            public void onError(VolleyError error) {
                countDesc("获取token失败");
            }
        });
    }


    private void initListener() {
        if (ttsHelper.isListen()) return;
        ttsHelper.ImListener(new TTSListenerCallback() {
            @Override
            public void callback(String response) {

                countDesc("监听结果--" + response);
                StatusType MStatus = getStatus();
                switch (MStatus) {
                    case Nomal:
                        //TODO 处理将录入的语音 接收下一步动作的指示
                        getNext(response);
                        break;
                    case CreateNomal:
                        response = getResources().getString(R.string.create_name);
                    case CreatePerson://认识这个人，，确定名字

                        VolleyHelper.putUpdatePerson(AppUrl.putUpdatePerson(BaseApplication.getIntence().getId())
                                , response
                                , new VolleyHelper.HelpListener() {
                            @Override
                            public void onResponse(String s) {
                                if (s != null) {
                                    try {
                                        countDesc("put for renew name success --" + s);
                                        JSONObject result = new JSONObject(s);
                                        String voice = result.getJSONObject("action").getString("voice");
                                        initSpeak(voice);
                                        BaseApplication.getIntence().setStatus(StatusType.Nomal);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }

                            @Override
                            public void onError(VolleyError error) {
                                Log.d(TAG, "error log = " + error.getMessage());
                                countDesc("put error");
                                initSpeak("我没听懂，再说一次吧");
                            }
                        });
                        break;
                    case Play:
                        //TODO 处理将录入的语音 接收下一步动作的指示
                        getNext(response);
                        break;
                }
            }

            @Override
            public void error() {
                countDesc("listen error ");
                StatusType MStatus = getStatus();
                switch (MStatus) {
                    case CreatePerson:
                        if (count_speak_number >= 1) {
                            initSpeak(getResources().getString(R.string.create_nomal));
                            BaseApplication.getIntence().setStatus(StatusType.CreateNomal);
                            count_speak_number = 0;
                        } else {
                            initSpeak("我叫小白，你叫什么名字");
                            count_speak_number++;
                        }
                        break;
                    case CreateNomal:
                        if (count_speak_number >= 1) {
                            countDesc("关机");
                            //TODO 关机
                            initListener();
                        } else {
                            initSpeak("再没人说话我就关机了哦");
                            count_speak_number++;
                        }
                        break;
                    case Nomal:
                        initListener();
                        break;
                    case Play:
                        initListener();
                        break;
                    default:
                        initListener();
                        break;
                }
            }
        });
    }

    public void initSpeak(String voice) {
        if (ttsHelper.isSpeak()) return;
        ttsHelper.ImSpeak(voice, new TTSSpeakCallback() {
            @Override
            public void onStart() {
                face.mouthStartSpeakAnim();
                countDesc("speak start");
            }

            @Override
            public void onComplete() {
                face.mouthStopSpeakAnim();
                countDesc("speak complete");
                initListener();
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //退出时释放链接
        if (ttsHelper != null)
            ttsHelper.cancle();
        if (mCameraHelper != null) {
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
                    ttsHelper.stopAll();//关闭音频服务
                    VolleyHelper.postFaceVerify(bytes, new VolleyHelper.HelpListener() {
                        @Override
                        public void onResponse(String s) {
                            countDesc(s);
                            try {
                                JSONObject result = new JSONObject(s);
                                BaseApplication.getIntence().setId(result.getJSONObject("person").getString("id"));
                                String voice = result.getJSONObject("action").getString("voice");

//                                if (result.getJSONObject("person").getBoolean("recognized")) {
//                                    //1：熟悉的人
//                                    countDesc("检测结果:--认识");
//                                    face.eyeSine();
//                                    BaseApplication.getIntence().setStatus(StatusType.Nomal);
//                                    initSpeak("要不要玩？");
//                                } else {
                                //2: 陌生人 创建人物
                                countDesc("检测结果:--不认识");
                                face.eyeSine();
                                BaseApplication.getIntence().setStatus(StatusType.CreatePerson);
                                initSpeak("我是小白，你叫什么名字");
//                                }

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
            face_success = true;
            Log.d(TAG, "人脸检测失败");
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


    void getNext(String voice) {
        VolleyHelper.doGetNext(AppUrl.getNext(voice,
                        BaseApplication.getIntence().getUser_emotion()),
                new VolleyHelper.HelpListener() {
                    @Override
                    public void onResponse(String s) {
                        if (s != null) {
                            try {
                                countDesc("next success---" + s);
                                JSONObject result = new JSONObject(s).getJSONObject("action");
                                String voice = result.getString("voice");
                                String anim = result.getString("emotion");
                                String id = result.getString("behavior_id");

                                if (StringUtil.isNotEmpty(anim)) {//3秒动画，开始下一步

                                    Message msg = new Message();
                                    msg.obj = new String[]{id, voice};
                                    msg.what = 1;
                                    mHandler.sendMessageDelayed(msg, 3000);
                                } else {
                                    statusChange(id, voice);
                                }


                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onError(VolleyError error) {
                        Log.d(TAG, "error log = " + error.getMessage());
                        countDesc("next error");
                        initSpeak("我没听懂，再说一次吧");
                    }
                });
    }

    void statusChange(String id, String voice) {
        if (StringUtil.isEmpty(id)) {
            BaseApplication.getIntence().setStatus(StatusType.Nomal);
            initSpeak(voice);
            return;
        }
        switch (id) {
            case "3"://玩
                if (!isPlay) {
                    isPlay = true;
                    initSpeak(voice);
                    BaseApplication.getIntence().setStatus(StatusType.Play);
                }
                break;
            case "2"://不玩
                isPlay = false;
                BaseApplication.getIntence().setStatus(StatusType.Nomal);
            default:
                initSpeak(voice);
                break;
        }
    }

    private Handler mHandler = new Handler() {
        @Override
        public void dispatchMessage(Message msg) {
            String[] arr = (String[]) msg.obj;
            switch (msg.what) {
                case 1:
                    statusChange(arr[0], arr[1]);
                    break;
            }
        }
    };

    void speakAnim(String anim) {

    }

    void stopAnim() {

    }
}
