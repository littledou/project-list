package com.readface.cafe.robot;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.widget.ImageView;

import com.readface.cafe.anim.R;
import com.readface.cafe.utils.FaceUtil;


public class AnimDrawable extends Drawable implements Animatable {


    private Bitmap[] view;

    private Matrix mMatrix;
    private Context mContext;
    private float radio;
    private int screenW, screenH;

    private boolean isRefreshing = false;

    private Point[] start;
    private Point[] off;
    private Point[] offset;

    private float timeLine = 0;
    private static final float ANIMATION_DURATION = 480;

    public AnimDrawable(Context mContext) {
        super();
        this.mContext = mContext;
        initCaclu();
    }

    private void initCaclu() {
        screenW = FaceUtil.getScreenWidth(mContext);
        screenH = FaceUtil.getScreenHeight(mContext);
        radio = screenW / 640f;
        if (screenW > 1200) {
            radio = 1080 / 640f;
        }
        mMatrix = new Matrix();


        view = new Bitmap[9];
        start = new Point[9];
        offset = new Point[9];
        off = new Point[9];

        for (int i = 0; i < 9; i++) {
            start[i] = new Point(0, 0);
            offset[i] = new Point(0, 0);
            off[i] = new Point(0, 0);
        }
        start[0].x = screenW;
        off[0].x = -(int) (175 * radio);

        start[0].y = (int) (125 * radio);
        off[0].y = (int) (125 * radio);

        start[1].x = -(int) (101 * radio);
        off[1].x = screenW;
        start[1].y = (int) (51 * radio);
        off[1].y = (int) (51 * radio);


        start[2].x = screenW / 2;
        start[2].y = screenH * 3 / 4 + 100;
        off[2].x = screenW + 300;
        off[2].y = screenH * 3 / 4 - screenW / 2 - 200;

        start[3].x = screenW / 2;
        start[3].y = screenH * 3 / 4;
        off[3].x = screenW + 300;
        off[3].y = screenH * 3 / 4 - screenW / 2 - 300;

        start[4].x = screenW / 2;
        start[4].y = screenH * 3 / 4 + 300;
        off[4].x = screenW + 300;
        off[4].y = screenH * 3 / 4 - screenW / 2;


        start[5].x = (int) (-145 * radio);
        start[5].y = (int) (700 * radio);

        off[5].x = (int) (740 * radio);
        off[5].y = (int) (700 * radio);

        start[6].x = (int) (-80 * radio);
        start[6].y = (int) (750 * radio);

        off[6].x = screenW + 100;
        off[6].y = (int) (750 * radio);

        start[7].x = (int) (640 * radio);
        start[7].y = (int) (650 * radio);

        off[7].x = (int) (-180 * radio);
        off[7].y = (int) (650 * radio);

        start[8].x = (int) (640 * radio);
        start[8].y = (int) (740 * radio);

        off[8].x = (int) (-90 * radio);
        off[8].y = (int) (740 * radio);

        for (int i = 0; i < offset.length; i++) {
            offset[i].x = off[i].x - start[i].x;
            offset[i].y = off[i].y - start[i].y;
        }

        createBitmaps();

    }

    @Override
    public void draw(Canvas canvas) {
        if (screenW <= 0) return;
        final int saveCount = canvas.save();
        drawAll(canvas);
        canvas.restoreToCount(saveCount);
    }

    private void drawAll(Canvas canvas) {
        Matrix matrix = mMatrix;
        matrix.reset();
        float x = timeLine * offset[0].x / ANIMATION_DURATION + start[0].x;
        float y = timeLine * offset[0].y / ANIMATION_DURATION + start[0].y;
        matrix.postTranslate(x, y);
        matrix.postRotate(timeLine * 360 / ANIMATION_DURATION, x + (int) (87 * radio), y + (int) (87 * radio));
        canvas.drawBitmap(view[0], matrix, null);

        matrix.reset();
        x = timeLine * offset[1].x / ANIMATION_DURATION + start[1].x;
        y = timeLine * offset[1].y / ANIMATION_DURATION + start[1].y;
        matrix.postTranslate(x, y);
        matrix.postRotate(timeLine * 360 / ANIMATION_DURATION, x + (int) (51 * radio), y + (int) (51 * radio));
        canvas.drawBitmap(view[1], matrix, null);


        float rock_count = timeLine;
        rock_count *= 3;
        rock_count %= ANIMATION_DURATION;

        rock_count = rock_count / ANIMATION_DURATION;

        if (timeLine >= 160 && timeLine <= 320) {

            float rock1 = caclu(rock_count, 0.7f);

            matrix.reset();
            x = rock1 * offset[2].x + start[2].x;
            y = rock1 * offset[2].y + start[2].y;

            matrix.postTranslate(x, y);
            canvas.drawBitmap(view[2], matrix, null);

            float rock2 = caclu(rock_count, 0.65f);
            matrix.reset();
            x = rock2 * offset[3].x + start[3].x;
            y = rock2 * offset[3].y + start[3].y;
            matrix.postTranslate(x, y);
            canvas.drawBitmap(view[3], matrix, null);

            float rock3 = caclu(rock_count, 0.68f);
            matrix.reset();
            x = rock3 * offset[4].x + start[4].x;
            y = rock3 * offset[4].y + start[4].y;
            matrix.postTranslate(x, y);
            canvas.drawBitmap(view[4], matrix, null);
        }
        rock_count = timeLine / ANIMATION_DURATION;
        matrix.reset();
        x = rock_count * offset[5].x + start[5].x;
        y = rock_count * offset[5].y + start[5].y;
        matrix.postTranslate(x, y);
        matrix.postRotate(rock_count * 45, x + (int) (57 * radio), y + (int) (34 * radio));
        canvas.drawBitmap(view[5], matrix, null);

        matrix.reset();
        x = rock_count * offset[6].x + start[6].x;
        y = rock_count * offset[6].y + start[6].y;
        matrix.postTranslate(x, y);
        matrix.postRotate(rock_count * 45, x + (int) (44 * radio), y + (int) (30 * radio));
        canvas.drawBitmap(view[6], matrix, null);

        matrix.reset();
        x = rock_count * offset[7].x + start[7].x;
        y = rock_count * offset[7].y + start[7].y;
        matrix.postTranslate(x, y);
        matrix.postRotate(-rock_count * 45, x + (int) (45 * radio), y + (int) (26 * radio));
        canvas.drawBitmap(view[7], matrix, null);

        matrix.reset();
        x = rock_count * offset[8].x + start[8].x;
        y = rock_count * offset[8].y + start[8].y;
        matrix.postTranslate(x, y);
        matrix.postRotate(-rock_count * 45, x + (int) (49 * radio), y + (int) (34 * radio));
        canvas.drawBitmap(view[8], matrix, null);

    }

    private float caclu(float x, float a) {
        if (x < a) {
            return -(x * x - 2 * x * a + a * a) + a * a * a;
        } else {
            return x * x * x;
        }
    }

    private Timer timer;
    Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            invalidateSelf();
        }

        ;
    };
    private TimerTask task = new TimerTask() {

        @Override
        public void run() {
            System.out.println("timeLine = " + timeLine);
            timeLine += 1;
            if (timeLine >= ANIMATION_DURATION) {
                timeLine = 0;
            }
            mHandler.sendEmptyMessage(0);
        }
    };

    @Override
    public void start() {
        isRefreshing = true;
        if (timer == null)
            timer = new Timer();
        else {
            timer.cancel();
        }

        timer.schedule(task, 1000, 25);
    }

    @Override
    public void stop() {
        isRefreshing = false;
        if (timer != null) timer.cancel();
    }

    private void createBitmaps() {
        view[0] = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.star1);
        view[0] = Bitmap.createScaledBitmap(view[0], (int) (175 * radio), (int) (175 * radio), true);
        view[1] = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.star2);
        view[1] = Bitmap.createScaledBitmap(view[1], (int) (102 * radio), (int) (102 * radio), true);

        view[2] = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.rocket1);
        view[2] = Bitmap.createScaledBitmap(view[2], (int) (87 * radio), (int) (85 * radio), true);
        view[3] = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.rocket2);
        view[3] = Bitmap.createScaledBitmap(view[3], (int) (50 * radio), (int) (45 * radio), true);
        view[4] = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.rocket3);
        view[4] = Bitmap.createScaledBitmap(view[4], (int) (30 * radio), (int) (27 * radio), true);

        view[5] = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.cloud1);
        view[5] = Bitmap.createScaledBitmap(view[5], (int) (115 * radio), (int) (68 * radio), true);
        view[6] = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.cloud2);
        view[6] = Bitmap.createScaledBitmap(view[6], (int) (88 * radio), (int) (60 * radio), true);
        view[7] = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.cloud3);
        view[7] = Bitmap.createScaledBitmap(view[7], (int) (89 * radio), (int) (53 * radio), true);
        view[8] = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.cloud4);
        view[8] = Bitmap.createScaledBitmap(view[8], (int) (98 * radio), (int) (68 * radio), true);
    }

    @Override
    public void setAlpha(int alpha) {

    }

    @Override
    public void setColorFilter(ColorFilter cf) {

    }

    @Override
    public int getOpacity() {
        return 0;
    }


    @Override
    public boolean isRunning() {
        return isRefreshing;
    }

}
