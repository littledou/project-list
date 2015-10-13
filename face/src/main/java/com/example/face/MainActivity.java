package com.example.face;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.util.FaceUtil;

public class MainActivity extends Activity {
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		RelativeLayout parent = new RelativeLayout(this);

		parent.setLayoutParams(new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
		parent.setBackgroundColor(Color.BLACK);

		int screenW = FaceUtil.getScreenWidth(this);
		float radio = screenW/ 1080f;
		final Face face = new Face(this,radio);
		RelativeLayout.LayoutParams layoutParams= new RelativeLayout.LayoutParams(screenW, (int)(750*radio));
		layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
		face.setLayoutParams(layoutParams);
		face.setBackgroundColor(Color.WHITE);
		parent.addView(face);

		setContentView(parent);


		LinearLayout testList = new LinearLayout(this);
		testList.setOrientation(LinearLayout.VERTICAL);
		Button startSpeak = new Button(this);
		startSpeak.setText("开始说话");
		startSpeak.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				face.startNomalSpeak();
			}
		});

		testList.addView(startSpeak);
		Button stopSpeak = new Button(this);
		stopSpeak.setText("停止说话");
		stopSpeak.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				face.stopNomalSpeak();
			}
		});
		testList.addView(stopSpeak);

		Button startSight = new Button(this);
		startSight.setText("开始眨眼");
		startSight.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				face.startSight();
			}
		});

		testList.addView(startSight);
		Button stopSight = new Button(this);
		stopSight.setText("停止眨眼");
		stopSight.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				face.stopSight();
			}
		});
		testList.addView(stopSight);

		parent.addView(testList);
	}

}
