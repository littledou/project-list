package com.example.texttospeech;

import android.app.Activity;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class MainActivity extends Activity {

    private final static String TAG = "MainActivity";
    private SpeechRecognizer mIat;
    private RecognizerDialog mIatDialog;
    private Toast mToast;
    private TextView text_hello;

    // 用HashMap存储听写结果
    private HashMap<String, String> mIatResults = new LinkedHashMap<String, String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        text_hello = (TextView) findViewById(R.id.hello);
        mIat = SpeechRecognizer.createRecognizer(this, mInitListener);
        mIatDialog = new RecognizerDialog(this, mInitListener);
        mToast = Toast.makeText(this, "", Toast.LENGTH_SHORT);

        setParames();

        mIatDialog.setListener(mRecognizerDialogListener);
        mIatDialog.show();
    }

   private void setParames(){
       // 清空参数
       mIat.setParameter(SpeechConstant.PARAMS, null);

       // 设置听写引擎
       mIat.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);

       // 设置返回结果格式
       mIat.setParameter(SpeechConstant.RESULT_TYPE, "json");
       // 设置语言
       mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
       // 设置语言区域
       mIat.setParameter(SpeechConstant.ACCENT, "mandarin");
       // 设置语音前端点:静音超时时间，即用户多长时间不说话则当做超时处理
       mIat.setParameter(SpeechConstant.VAD_BOS, "4000");

       // 设置语音后端点:后端点静音检测时间，即用户停止说话多长时间内即认为不再输入， 自动停止录音
       mIat.setParameter(SpeechConstant.VAD_EOS, "1000");

       // 设置标点符号,设置为"0"返回结果无标点,设置为"1"返回结果有标点
       mIat.setParameter(SpeechConstant.ASR_PTT,  "1");

       // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
       // 注：AUDIO_FORMAT参数语记需要更新版本才能生效
       mIat.setParameter(SpeechConstant.AUDIO_FORMAT,"wav");
       mIat.setParameter(SpeechConstant.ASR_AUDIO_PATH, Environment.getExternalStorageDirectory()+"/msc/iat.wav");

    }
    private RecognizerDialogListener mRecognizerDialogListener = new RecognizerDialogListener() {
        public void onResult(RecognizerResult results, boolean isLast) {
            String text = JsonParser.parseIatResult(results.getResultString());
            showTip(results.getResultString());

            String sn = null;
            // 读取json结果中的sn字段
            try {
                JSONObject resultJson = new JSONObject(results.getResultString());
                sn = resultJson.optString("sn");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            mIatResults.put(sn, text);

            StringBuffer resultBuffer = new StringBuffer();
            for (String key : mIatResults.keySet()) {
                resultBuffer.append(mIatResults.get(key));
            }


            text_hello.setText(resultBuffer.toString());
        }

        /**
         * 识别回调错误.
         */
        public void onError(SpeechError error) {
            showTip(error.getPlainDescription(true));
        }

    };
    /**
     * 初始化监听器。
     */
    private InitListener mInitListener = new InitListener() {

        @Override
        public void onInit(int code) {
            Log.d(TAG, "SpeechRecognizer init() code = " + code);
            if (code != ErrorCode.SUCCESS) {
                showTip("初始化失败，错误码：" + code);
            }
        }
    };

    private void showTip(final String str) {
        mToast.setText(str);
        mToast.show();
    }


}
