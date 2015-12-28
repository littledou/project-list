package com.example.custom_demo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends Activity {

    private LinearLayout parent;

    private Class[] activityList = {
      StartActivity.class,TextToSpeechActivity.class
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        parent = (LinearLayout) findViewById(R.id.parent);
        for (int i = 0;i<activityList.length;i++){
            TextView tv = new TextView(this);
            tv.setPadding(0,10,0,10);
            tv.setText(activityList[i].getName());
            final int count = i;
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(MainActivity.this,activityList[count]));
                }
            });
            parent.addView(tv);
        }


        ImageView iv = new ImageView(this);
        parent.addView(iv, new ViewGroup.LayoutParams(1080, 1080));
        iv.setImageResource(R.mipmap.image_t);
    }
}
