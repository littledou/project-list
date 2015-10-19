package com.readface.cafe.anim;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.os.Handler;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class Myface extends SurfaceView implements SurfaceHolder.Callback {

    private Rect rect_blush_left, rect_blush_right, rect_brow_left, rect_brow_right, rect_eye_left, rect_eye_right, rect_mouth;
    private Paint paint;
    private float radio = 1.0f;

    private SurfaceHolder sfh;

    //眨眼动作开关
    private boolean isEyeSight = false;
    //眼睛颤颤的开关
    private boolean isEyeShock = false;
    private int eyeShockRange = 0;
    private int sightImage;
    private int sightCount = 0;
    private int[] eyeSight = {//眨眼动作

            R.drawable.eye_sight
            , R.drawable.eye_sight0
            , R.drawable.eye_sight1
            , R.drawable.eye_sight2
            , R.drawable.eye_sight3
            , R.drawable.eye_sight2
            , R.drawable.eye_sight1
            , R.drawable.eye_sight0
    };

    Handler handler = new Handler();

    public Myface(Context context, float radio) {
        super(context);
        this.radio = radio;
        init();
    }

    private void init() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        rect_blush_left = new Rect();//腮红
        rect_blush_left.left = (int) (radio * 72);
        rect_blush_left.top = (int) (radio * 360);
        rect_blush_left.right = (int) (radio * 372);
        rect_blush_left.bottom = (int) (radio * 560);

        rect_blush_right = new Rect();
        rect_blush_right.left = (int) (radio * 708);
        rect_blush_right.top = (int) (radio * 360);
        rect_blush_right.right = (int) (radio * 1008);
        rect_blush_right.bottom = (int) (radio * 560);

        rect_brow_left = new Rect();//眉毛
        rect_brow_left.left = (int) (radio * 110);
        rect_brow_left.top = (int) (radio * 33);
        rect_brow_left.right = (int) (radio * 480);
        rect_brow_left.bottom = (int) (radio * 164);

        rect_brow_right = new Rect();
        rect_brow_right.left = (int) (radio * 602);
        rect_brow_right.top = (int) (radio * 33);
        rect_brow_right.right = (int) (radio * 972);
        rect_brow_right.bottom = (int) (radio * 164);

        rect_eye_left = new Rect();//眼睛
        rect_eye_left.left = (int) (radio * 150);
        rect_eye_left.top = (int) (radio * 175);
        rect_eye_left.right = (int) (radio * 444);
        rect_eye_left.bottom = (int) (radio * 469);

        rect_eye_right = new Rect();
        rect_eye_right.left = (int) (radio * 640);
        rect_eye_right.top = (int) (radio * 175);
        rect_eye_right.right = (int) (radio * 934);
        rect_eye_right.bottom = (int) (radio * 469);

        rect_mouth = new Rect(
                (int) (radio * 370)
                , (int) (radio * 512)
                , (int) (radio * 710)
                , (int) (radio * 712));//嘴巴

        sfh = this.getHolder();
        this.setZOrderOnTop(true);
        sfh.setFormat(PixelFormat.TRANSPARENT);
        sfh.addCallback(this);

    }

    private void myOnDraw() {
        Canvas canvas = sfh.lockCanvas();

        //绘制腮红
        Bitmap blush_left = BitmapFactory.decodeResource(getResources(), R.drawable.blush);
        Matrix matrix = new Matrix();
        matrix.postScale(-1, 1);
        Bitmap blush_right = Bitmap.createBitmap(blush_left, 0, 0, blush_left.getWidth(), blush_left.getHeight(), matrix, true);
        canvas.drawBitmap(blush_left, null, rect_blush_left, paint);
        canvas.drawBitmap(blush_right, null, rect_blush_right, paint);

        //绘制眉毛
        Bitmap brow_left = BitmapFactory.decodeResource(getResources(), R.drawable.browleft0);
        Bitmap brow_right = BitmapFactory.decodeResource(getResources(), R.drawable.browright0);

        canvas.drawBitmap(brow_left, null, rect_brow_left, paint);
        canvas.drawBitmap(brow_right, null, rect_brow_right, paint);

        //绘制眼睛
        //眼珠
        paint.setColor(Color.BLACK);
        canvas.drawCircle(rect_eye_left.left + 147 * radio, rect_eye_left.top + 147 * radio, 147 * radio, paint);//left eye
        canvas.drawCircle(rect_eye_right.left + 147 * radio, rect_eye_right.top + 147 * radio, 147 * radio, paint);
        //眼白 初始状态
        paint.setColor(Color.WHITE);
        canvas.drawCircle(rect_eye_left.left + 177 * radio, rect_eye_left.top + 117 * radio, 36 * radio, paint);//left point
        canvas.drawCircle(rect_eye_left.left + 144 * radio, rect_eye_left.top + 162 * radio, 12 * radio, paint);

        canvas.drawCircle(rect_eye_right.left + 117 * radio, rect_eye_right.top + 117 * radio, 36 * radio, paint);
        canvas.drawCircle(rect_eye_right.left + 150 * radio, rect_eye_right.top + 162 * radio, 12 * radio, paint);
        //绘制眼帘
        if (isEyeSight) {
            Bitmap sight = BitmapFactory.decodeResource(getResources(), sightImage);
            canvas.drawBitmap(sight, null, rect_eye_left, paint);//left sight
            canvas.drawBitmap(sight, null, rect_eye_right, paint);
        }

        //绘制嘴巴
        Bitmap mouth = BitmapFactory.decodeResource(getResources(), R.drawable.mouth0);
        canvas.drawBitmap(mouth, null, rect_mouth, paint);
        sfh.unlockCanvasAndPost(canvas);
    }

    private void startInit() {//进入启动场景
        myOnDraw();

        Canvas canvas = sfh.lockCanvas(rect_mouth);
        Bitmap mouth = BitmapFactory.decodeResource(getResources(), R.drawable.mouth_smile2);
        canvas.drawBitmap(mouth, null, rect_mouth, paint);
        sfh.unlockCanvasAndPost(canvas);
        isEyeSight = true;
        handler.post(eye);

    }

    private Runnable eye = new Runnable() {
        @Override
        public void run() {
            if (isEyeShock) {
                if (eyeShockRange == 0) {
                    eyeShockRange = 2;
                }
                eyeShockRange = -eyeShockRange;
                myOnDraw();
                handler.postDelayed(eye, 100);
            } else if (isEyeSight) {
                if (sightCount == eyeSight.length * 3) {
                    sightImage = eyeSight[0];
                    postInvalidate();
                    sightCount = 0;
                    handler.postDelayed(eye, 2000);
                } else {
                    sightImage = eyeSight[sightCount % eyeSight.length];
                    myOnDraw();
                    sightCount++;

                    if (sightCount == eyeSight.length * 2 + 1) {
                        handler.postDelayed(eye, 100);
                    } else {
                        handler.postDelayed(eye, 50);
                    }
                }
            }
        }
    };

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        startInit();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }
}
