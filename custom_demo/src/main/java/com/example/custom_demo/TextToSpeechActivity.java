package com.example.custom_demo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import java.util.Locale;

/**
 * Created by mac on 15/9/7.
 */
public class TextToSpeechActivity extends Activity implements TextToSpeech.OnInitListener{

    private final static int MY_DATA_CHECK_CODE = 0;
    private TextToSpeech tts;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Intent checkIntent = new Intent();

        checkIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);

        startActivityForResult(checkIntent, MY_DATA_CHECK_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
// TODO Auto-generated method stub
        if (requestCode == MY_DATA_CHECK_CODE)
        {
            if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) // 如果TTS Engine有成功找到的話
            {
                tts = new TextToSpeech(this, this);
                // 宣告一個 TextToSpeech instance, 註冊android.speech.tts.TextToSpeech.OnInitListener
                // 當TTS Engine 初始完後會呼叫 onInit(int status)
            }
            else // 如果 TTS 沒有安裝的話 , 要求安裝
            {
                Intent installIntent = new Intent();
                installIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                startActivity(installIntent);
            }
        }
    }

    /**
     * Called to signal the completion of the TextToSpeech engine initialization.
     *
     * @param status {@link TextToSpeech#SUCCESS} or {@link TextToSpeech#ERROR}.
     */
    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS)
        {
            tts.setPitch(1.0f); // 音調
            tts.setSpeechRate(1); // 速度
            int result = tts.setLanguage(Locale.ENGLISH); // 語言

            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED)
            {
                Log.e("TTS", "This Language is not supported");
            }
            else
            {
                speakOut();
            }
        }
        else
        {
            Log.e("TTS", "Initilization Failed!");
        }
    }

    private void speakOut()
    {
        tts.speak("A TextToSpeech instance can only be used to synthesize text once it has completed its initialization. Implement the TextToSpeech.OnInitListener to be notified of the completion of the initialization.\n" +
                "When you are done using the TextToSpeech instance, call the shutdown() method to release the native resources used by the TextToSpeech engine.", TextToSpeech.QUEUE_FLUSH, null); //TextToSpeech.QUEUE_ADD 為目前的念完才念
    }
    @Override
    public void onDestroy()
    {
        if (tts != null)
        {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }
}
