package com.readface.cafe.anim;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.android.volley.VolleyError;
import com.readface.cafe.Iinteface.TTSListenerCallback;
import com.readface.cafe.Iinteface.TTSSpeakCallback;
import com.readface.cafe.robot.AnimDrawable;
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
import com.readface.cafe.utils.ToastUtils;
import com.readface.cafe.utils.VolleyHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import mobile.AUDetection.YMDetector;
import mobile.AUDetection.YMFace;


public class TestActivity extends BaseActivity implements YMDetector.DetectorListener, CameraHelper.ImageListener {

    private final String TAG = "TestActivity";

    private String currEmo = EmotionStatus.emotion[0];
    private Context mContext;

    private Robot mRobot;
    private Face face;

    private CameraHelper mCameraHelper;
    private YMDetector ymDetector;
    private SurfaceView mSurface;


    private TTSHelper ttsHelper;

    private boolean isPlay = false;
    private boolean isTTS = true;
    boolean isTouch = false;
    private int speakCount = 1;
    private String new_person = "";

    private JSONArray listForSpeak = null;
    private int speakForListCount = 0;

    private int isSendPicCount = 0;
    private boolean isSendPic = false;
    RelativeLayout parent;

    private int screenW, screenH;
    private float radio;
    View guide;

    private boolean hasToast = false;

    private long mBackTime = -1;
    private final static long DIFF_DEFAULT_BACKTIME = 2000;

    @Override
    public void onBackPressed() {
        long nowTime = System.currentTimeMillis();
        long diff = nowTime - mBackTime;
        if (diff > DIFF_DEFAULT_BACKTIME) {
            mBackTime = nowTime;
            ToastUtils.showShortToast("再按一次退出");
        } else {
            super.onBackPressed();
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        screenW = FaceUtil.getScreenWidth(this);
        screenH = FaceUtil.getScreenHeight(this);
        radio = screenW / 640f;
        if (screenW > 1200) {
            radio = 1080 / 640f;
        }
        parent = new RelativeLayout(this);
        setContentView(parent);
        initVideo();
    }

    void initVideo() {
        countDesc("init Video");
        final VideoView vv = new VideoView(mContext);
        vv.setLayoutParams(new RelativeLayout.LayoutParams(screenW, screenH));
        parent.addView(vv);
        vv.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.lanch));
        vv.start();
        vv.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mp) {

                AppPreferences appPreferences = BaseApplication.getInstence().getAppPreferences();
                appPreferences.readLocalProperties(mContext);

                if (StringUtil.isEmpty(appPreferences.isGuide)) {
                    countDesc("video complete then add yes");
                    hasToast = true;
                    appPreferences.isGuide = "yes";
                    appPreferences.saveInstance(mContext);
                    ImageView start_bg = new ImageView(mContext);
                    start_bg.setLayoutParams(new RelativeLayout.LayoutParams(screenW, screenH));
                    start_bg.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    start_bg.setImageResource(R.mipmap.lunch_bg);
                    View yes = new View(mContext);
                    RelativeLayout.LayoutParams yesl = new RelativeLayout.LayoutParams((int) (radio * 250), (int) (radio * 250));
                    yesl.addRule(RelativeLayout.CENTER_HORIZONTAL);
                    yesl.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                    yesl.bottomMargin = 120;
                    yes.setLayoutParams(yesl);
                    parent.addView(start_bg);
                    parent.addView(yes);
                    yes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            countDesc("video complete then add main");
                            initMainView();
                        }
                    });
                } else {
                    initMainView();
                }
            }
        });
    }

    void initMainView() {

        int robotW = (int) (radio * 640f);
        int robotH = (int) (radio * 810f);

        mRobot = new Robot(this, radio);
        RelativeLayout.LayoutParams robotLa = new RelativeLayout.LayoutParams(robotW, robotH);
        robotLa.addRule(RelativeLayout.CENTER_IN_PARENT);
        mRobot.setLayoutParams(robotLa);

        ImageView view = new ImageView(mContext);
        view.setLayoutParams(new RelativeLayout.LayoutParams(screenW, screenH));
        view.setBackgroundResource(R.drawable.anim_main_bg);
        AnimDrawable drawable = new AnimDrawable(this);
        view.setImageDrawable(drawable);

        ImageView view1 = new ImageView(mContext);
        view1.setScaleType(ImageView.ScaleType.CENTER_CROP);
        view1.setImageResource(R.drawable.anim_e);
        RelativeLayout.LayoutParams e_La = new RelativeLayout.LayoutParams(screenW, (int) (screenW * 327f / 640f));
        e_La.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        view1.setLayoutParams(e_La);

        mSurface = new SurfaceView(this);

        guide = new View(mContext);
        guide.setVisibility(View.GONE);
        RelativeLayout.LayoutParams guide_ = new RelativeLayout.LayoutParams((int) (156 * radio), (int) (48 * radio));
        guide_.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        guide_.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        guide_.rightMargin = (int) (25 * radio);
        guide_.bottomMargin = (int) (20 * radio);
        guide.setLayoutParams(guide_);
        guide.setBackgroundResource(R.mipmap.par_guide);

        parent.addView(mSurface);
        parent.addView(view);
        parent.addView(view1);
        parent.addView(mRobot);
        parent.addView(guide);


        drawable.start();
        BaseApplication.getInstence().setStatus(StatusType.Nomal);
        face = mRobot.getFace();

        guide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(mContext, GuideActivity.class), 1);
            }
        });

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


        if (!isNetworkConnected(mContext)) {
            mHandler.sendEmptyMessageDelayed(5, 2000);
        }
        mHandler.sendEmptyMessageDelayed(3, FaceUtil.getRandom() * 1000);
        initActivate();
    }


    private Handler mHandler = new Handler() {
        @Override
        public void dispatchMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    statusChange((JSONObject) msg.obj);
                    break;
                case 2:
                    if (isActivityRun)
                        initActivate();
                    break;
                case 3:
                    if (getStatus() != StatusType.Play) {
                        face.eyeSine();

                        int ran = FaceUtil.getRandom();
                        switch (ran) {
                            case 5:
                                mRobot.initHeadAnim();
                            case 6:
                        }
                    }
                    mHandler.sendEmptyMessageDelayed(3, FaceUtil.getRandom() * 1000);
                    break;
                case 4:
                    if (face_success) {
                        face_success = false;
                        postVerify(null);
                    }
                    break;
                case 5:
                    if (!isNetworkConnected(mContext)) {
                        countDesc("no net");
                        final Dialog dialog = new Dialog(mContext, R.style.Dialog);
                        ImageView dia = new ImageView(mContext);
                        dia.setImageResource(R.mipmap.no_wifi);
                        dia.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        dialog.setContentView(dia);
                        dialog.setCanceledOnTouchOutside(true);
                        dialog.show();
                        dia.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.cancel();
                            }
                        });
                    }
                    break;
            }
        }
    };

    private void countDesc(String desc) {//描述输出
        FaceUtil.d(desc + "---status: " + getStatus());
    }

    private void initFaceDetector() {
        mCameraHelper = new CameraHelper(mContext, mSurface);
        mCameraHelper.startDetection(Camera.CameraInfo.CAMERA_FACING_FRONT);
        mCameraHelper.setImageListener(this);
        ymDetector = new YMDetector(mContext, this);
        ymDetector.setLicensePath("robot_doudou.license");
    }

    public boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    /**
     * 激活设备 获取token
     */
    private void initActivate() {

        VolleyHelper.doPostForToken(new VolleyHelper.HelpListener() {
            @Override
            public void onResponse(String s) {
                if (s != null) {//设备激活成功
                    parent.removeViewAt(0);//remove video
                    if (hasToast) {//remove toast
                        parent.removeViewAt(0);
                        parent.removeViewAt(0);
                    }
                    countDesc("设备激活成功,开启语音服务" + s);
                    try {
                        BaseApplication.getInstence().setToken(new JSONObject(s).getString("token"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    initFaceDetector();
                    mHandler.sendEmptyMessageDelayed(4, 5000);
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
        if (!isTTS)
            return;
        mRobot.initLightAnim();
        ttsHelper.ImListener(new TTSListenerCallback() {
            @Override
            public void callback(String response, String action) {

                mRobot.stopLightAnim();
                EmotionStatus.cleanFace();
                countDesc("监听结果--" + response + "--" + action);
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
                EmotionStatus.cleanFace();
                mRobot.stopLightAnim();
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
        if (!isTTS)
            return;

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
            speakForListCount++;
            e.printStackTrace();
        }

        if (voice == null) voice = "";
        ttsHelper.ImSpeak(voice, speed, new TTSSpeakCallback() {
            @Override
            public void onStart() {
                mRobot.initArmAnim();
                face.mouthStartSpeakAnim();
                countDesc("speak start");
            }

            @Override
            public void onComplete() {
                face.mouthStopSpeakAnim();
                countDesc("speak complete");
                if (StringUtil.isNotEmpty(new_person)) {
                    speakForListCount = 0;
                    BaseApplication.getInstence().setStatus(StatusType.CreatePerson);
                    upDateUse(new_person);
                    new_person = null;
                } else {
                    initSpeak();
                }
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        countDesc("onPause");
        isTTS = false;
        if (ttsHelper != null)
            ttsHelper.stopAll();
        if (face != null) {
            face.cleanALlAction();
            face.mouthStopSpeakAnim();
        }
        if (mCameraHelper != null)
            mCameraHelper.stopDetection();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        VolleyHelper.cancleAll();
    }


    @Override
    public void onSuccess(YMFace ymFace, byte[] bytes) {
        if (isSendPic) {
            isSendPic = false;

            VolleyHelper.postFaceVerify(AppUrl.postForVerify(BaseApplication.getInstence().getId()), bytes, new VolleyHelper.HelpListener() {
                @Override
                public void onResponse(String response) {
                    countDesc("volley uccess" + isSendPicCount);
                    isSendPicCount++;
                    isSendPic = true;
                    if (isSendPicCount >= 5) {
                        isSendPic = false;
                        isSendPicCount = 0;
                    }
                }

                @Override
                public void onError(VolleyError error) {
                    isSendPic = false;
                    countDesc("volley error");
                }
            });


        }

        EmotionStatus.addFace(ymFace);
        switch (getStatus()) {
            case Nomal:
                if (face_success) {
                    face_success = false;
                    postVerify(bytes);
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
                Log.d(TAG, "--情绪 = " + emo);
                if (emo != null && !emo.equals(currEmo)) {
                    final int position = EmotionStatus.qualEmo.get(emo);
                    currEmo = emo;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
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
                    });
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onError() {

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
        return BaseApplication.getInstence().getStatus();
    }


    void postVerify(byte[] bytes) {

        countDesc("获取到人脸信息，发起检测");
        VolleyHelper.postFaceVerify(AppUrl.postFaceVerify(), bytes, new VolleyHelper.HelpListener() {
            @Override
            public void onResponse(String s) {
                guide.setVisibility(View.VISIBLE);
                countDesc(s);
                try {
                    JSONObject result = new JSONObject(s);
                    BaseApplication.getInstence().setId(result.getJSONObject("person").getString("id"));
                    listForSpeak = result.getJSONObject("action").getJSONArray("voice");
                    if (result.getJSONObject("person").getBoolean("recognized")) {
                        countDesc("检测结果:--认识");
                        BaseApplication.getInstence().setStatus(StatusType.Nomal);
                    } else {
                        countDesc("检测结果:--不认识");
                        BaseApplication.getInstence().setStatus(StatusType.PreCreate);
                        isSendPic = true;
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

    void getNext(String voice, String count, String action) {
        VolleyHelper.doGet(AppUrl.getNext(voice, count,
                        BaseApplication.getInstence().getUser_emotion(), action),
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
        VolleyHelper.doGet(AppUrl.getAsk(BaseApplication.getInstence().getId(), count, voice), new VolleyHelper.HelpListener() {
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
        VolleyHelper.putUpdatePerson(AppUrl.putUpdatePerson(BaseApplication.getInstence().getId())
                , response
                , new VolleyHelper.HelpListener() {
            @Override
            public void onResponse(String s) {
                if (s != null) {
                    try {
                        BaseApplication.getInstence().setStatus(StatusType.Nomal);

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
                        BaseApplication.getInstence().setStatus(StatusType.Play);
                    } else {
                        initListener();
                    }
                    break;
                case 2://讲故事
                    isPlay = false;
                    face.emo6();
                    BaseApplication.getInstence().setStatus(StatusType.Nomal);
                    listForSpeak = result.getJSONArray("voice");
                    initSpeak();
                    break;
                case 0://正常
                default://其他
                    isPlay = false;
                    face.emo6();
                    BaseApplication.getInstence().setStatus(StatusType.Nomal);
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
                face.cry(true);
                break;
            case 8:
                face.smile1();
                break;
            case 9:
                face.smile2(true);
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
                face.ang2(true);
                break;
            case 16:
                face.sub();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            countDesc("onRestart");
            isTTS = true;
            speakForListCount = 0;
            initListener();
            if (mCameraHelper != null)
                mCameraHelper.startDetection(Camera.CameraInfo.CAMERA_FACING_FRONT);
            BaseApplication.getInstence().setStatus(StatusType.Nomal);
        }
    }
}
