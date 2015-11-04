package com.readface.cafe.anim;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
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
    int digress = -10;

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
        face.action1();
        initActivate();
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
                    checkIsPerson();
                }
            }

            @Override
            public void onError(VolleyError error) {
                countDesc("获取token失败");
            }
        });
    }


    private void initListener() {
        ttsHelper.ImListener(new TTSHelper.TTSListenerCallback() {
            @Override
            public void callback(String response) {

                StatusType MStatus = getStatus();
                switch (MStatus) {
                    case Nomal:
                        //TODO 处理将录入的语音 接收下一步动作的指示
                        VolleyHelper.doGetNext(AppUrl.getNext(response,
                                        BaseApplication.getIntence().getUser_emotion()),
                                new VolleyHelper.HelpListener() {
                                    @Override
                                    public void onResponse(String s) {
                                        if (s != null) {
                                            try {
                                                countDesc("next success" + s);
                                                JSONObject result = new JSONObject(s);
                                                String voice = result.getJSONObject("action").getString("voice");
                                                initSpeak(voice);
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
                        break;
                    case CreateNomal:
                        response = getResources().getString(R.string.create_nomal);
                    case CreatePerson://认识这个人，，确定名字
                        BaseApplication.getIntence().setStatus(StatusType.Nomal);
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

            @Override
            public void error() {
                StatusType MStatus = getStatus();
                switch (MStatus) {
                    case CreatePerson:
                        if (count_speak_number >= 1) {
                            initSpeak(getResources().getString(R.string.create_nomal));
                            BaseApplication.getIntence().setStatus(StatusType.CreateNomal);
                            face.action4();
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
                        } else {
                            initSpeak("再没人说话我就关机了哦");
                            count_speak_number++;
                        }
                        break;
                    default:
                        initListener();
                        break;
                }
            }
        });
    }

    public void initSpeak(String voice) {
        ttsHelper.ImSpeak(voice, new TTSHelper.TTSSpeakCallback() {
            @Override
            public void onStart() {
                face.action3();
                countDesc("speak start");
            }

            @Override
            public void onComplete() {
                countDesc("speak complete");
                face.stopSpeak();
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
        if(mCameraHelper!=null||mCameraHelper.isDetecting()){
            mCameraHelper.stopDetection();
        }
    }


    public void checkIsPerson() {

        new AlertDialog.Builder(this).setTitle("认识不认识你").setItems(new String[]{"认识", "不认识", "玩"},
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                countDesc("检测结果:--认识");
                                initSpeak("和我来玩吧");
                                break;
                            case 1:
                                countDesc("检测结果:--不认识，开始眨眼");
                                BaseApplication.getIntence().setStatus(StatusType.CreatePerson);
                                initSpeak("我叫小白，，你叫什么名字？");
                                face.action1();
                                break;
                            case 2:
                                BaseApplication.getIntence().setStatus(StatusType.Play);

                                break;
                        }
                    }
                }).show();
    }

    @Override
    public void onSuccess(YMFace ymFace, byte[] bytes, float v) {
        EmotionStatus.addFace(ymFace);
        hasPerson += "1";

        switch (getStatus()) {
            case Nomal:
                hasPerson = hasPerson.substring(1, hasPerson.length());
                if (face_success && StringUtil.getCharCount(hasPerson, '1') >= 18) {
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

                                if (result.getJSONObject("person").getBoolean("recognized")) {
                                    //1：熟悉的人
                                    countDesc("检测结果:--认识");
                                    BaseApplication.getIntence().setStatus(StatusType.Nomal);
                                    initSpeak("和我来玩吧");
                                } else {
                                    //2: 陌生人 创建人物 start voice:你好啊，我是你的新朋友，小白，请问你叫什么名字
                                    countDesc("检测结果:--不认识，开始眨眼");
                                    BaseApplication.getIntence().setStatus(StatusType.CreatePerson);
                                    initSpeak("我叫小白，，你叫什么名字？");
                                    face.action1();
                                }


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

                if (ymFace.getFacialActions()[0] > 20) {

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
                    }
                }

                break;
        }
    }

    @Override
    public void onError() {
        hasPerson += "0";
        hasPerson = hasPerson.substring(1, hasPerson.length());
        if (StringUtil.getCharCount(hasPerson, '0') >= 18) {
            hasPerson = "";
            face_success = true;
            countDesc("摄像头采集不到人脸信息 ");
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
}
