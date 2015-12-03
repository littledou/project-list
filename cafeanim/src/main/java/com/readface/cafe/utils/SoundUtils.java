package com.readface.cafe.utils;

import android.media.AudioManager;
import android.media.SoundPool;

import com.readface.cafe.anim.BaseApplication;
import com.readface.cafe.anim.R;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by mac on 15/11/30.
 */
public class SoundUtils {


    private static SoundPool mSoundPool;
    private static SoundUtils util;
    private static Map<Integer, Integer> spMap;

    private int curr = -1;
    private static float volumnRatio = -1;

    public static SoundUtils getIntense() {
        if (util == null || mSoundPool == null) {
            util = null;
            mSoundPool = null;
            util = new SoundUtils();
            mSoundPool = new SoundPool(4, AudioManager.STREAM_SYSTEM, 2);
            spMap = new HashMap<Integer, Integer>();
            spMap.put(0, mSoundPool.load(BaseApplication.getInstence(), R.raw.smile1, 1));
            spMap.put(1, mSoundPool.load(BaseApplication.getInstence(), R.raw.sad1, 1));
            spMap.put(2, mSoundPool.load(BaseApplication.getInstence(), R.raw.cry1, 1));
            spMap.put(3, mSoundPool.load(BaseApplication.getInstence(), R.raw.sub1, 1));
            spMap.put(4, mSoundPool.load(BaseApplication.getInstence(), R.raw.kiss, 1));
            spMap.put(5, mSoundPool.load(BaseApplication.getInstence(), R.raw.gosh, 1));
            spMap.put(6, mSoundPool.load(BaseApplication.getInstence(), R.raw.grimace, 1));
            spMap.put(7, mSoundPool.load(BaseApplication.getInstence(), R.raw.collect_one, 1));
            spMap.put(8, mSoundPool.load(BaseApplication.getInstence(), R.raw.angry1, 1));
            spMap.put(9, mSoundPool.load(BaseApplication.getInstence(), R.raw.angry2, 1));

            AudioManager am = (AudioManager) BaseApplication.getInstence().getSystemService(BaseApplication.getInstence().AUDIO_SERVICE);
            float audioMaxVolumn = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
            float audioCurrentVolumn = am.getStreamVolume(AudioManager.STREAM_MUSIC);
            volumnRatio = audioCurrentVolumn / audioMaxVolumn;
        }
        return util;
    }

    /**
     * 0:smile2
     * 1:sad1
     * 2:cry1
     * 3:sub1
     *
     * @param position
     */
    public void playSound(int position) {
        if (curr == position) {
            mSoundPool.pause(spMap.get(position));
        }
        curr = position;
        mSoundPool.play(spMap.get(position), volumnRatio, volumnRatio, 1, 1, 1);
    }

    public void pauseSound() {
        mSoundPool.pause(spMap.get(curr));
    }
}
