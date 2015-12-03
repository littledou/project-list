package com.readface.cafe.anim;

import android.app.Activity;
import android.os.Bundle;

import com.umeng.analytics.MobclickAgent;

/**
 * Created by mac on 15/12/1.
 */
public class BaseActivity extends Activity {


    private final String mPageName = BaseActivity.class.getSimpleName();

    protected boolean isActivityRun = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(mPageName);
        MobclickAgent.onResume(this);
        isActivityRun = true;
    }

    @Override
    public void onPause() {
        super.onPause();
        isActivityRun = false;
        MobclickAgent.onPageEnd(mPageName);
        MobclickAgent.onPause(this);
    }

}
