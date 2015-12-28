package com.example.custom_demo;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by mac on 15/12/22.
 */
public class TestActivity extends AppCompatActivity {

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
    }

    private void test() {

        //test SharedPreferences
        SharedPreferences sp = mContext.getSharedPreferences("", Context.MODE_PRIVATE);

        sp.getString("", "");//获取数据

        SharedPreferences.Editor editor = sp.edit();

        editor.putString("", "");//保存某数据

        editor.apply();//推荐异步

        editor.commit();//通用的是同步


        /**
         * 保存到文件
         *Internal Storage：内部存储，不需要权限
         * External：外部存储 必须有sdcard 声明读写权限
         */

        //Internal Storage
        //mContext.getFilesDir();mContext.getCacheDir();
        File file = new File(mContext.getFilesDir(), "fileName");

        FileOutputStream fos;

        try {
            fos = mContext.openFileOutput("fileName", Context.MODE_PRIVATE);
            fos.write("save data".getBytes());
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //External Storage
        //判断是否可进行外部存储 声明有写的权限就默认有读得权限了
        String state = Environment.getExternalStorageState();
        //state ==Environment.MEDIA_MOUNTED（写）  MEDIA_MOUNTED_READ_ONLY（读）

        //


        //加载Bitmap
        int res = 0, reqWidth = 0, reqHeight = 0;

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;//解码时不分配内存
        BitmapFactory.decodeResource(getResources(), res, options);
        int width = options.outWidth;
        int height = options.outHeight;
        String imgType = options.outMimeType;

        int inSampleSize = 1;
        if (width > reqWidth || height > reqHeight) {
            width /= 2;
            height /= 2;
            while ((width / inSampleSize) > reqWidth && (height / inSampleSize > reqHeight)) {
                inSampleSize *= 2;
            }
        }

        options.inSampleSize = inSampleSize;
        options.inJustDecodeBounds = false;
        Bitmap myBmp = BitmapFactory.decodeResource(getResources(), res, options);

    }


}
