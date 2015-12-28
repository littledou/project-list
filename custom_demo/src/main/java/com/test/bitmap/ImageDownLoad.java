package com.test.bitmap;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.test.cache.BitmapCache;

/**
 * Created by mac on 15/12/23.
 */
public class ImageDownLoad {


    public void loadBitmap(Context mContext, int resId, ImageView imageView) {

        final String imageKey = String.valueOf(resId);
        final Bitmap bitmap = BitmapCache.getBitmapFromMemoryCache(imageKey);
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
        } else {
            BitmapWorkerTask task = new BitmapWorkerTask(mContext, imageView);
            task.execute(resId);
        }
    }

    public boolean cancelPotentiaWork(int data, ImageView imageView) {

        final BitmapWorkerTask bitmapWorkerTask = BitmapUtils.getBitmapWorkerTask(imageView);
        if (bitmapWorkerTask != null) {
            final int bitmapData = bitmapWorkerTask.data;
            if (bitmapData == 0 || bitmapData != data) {
                bitmapWorkerTask.cancel(true);
            } else {
                return false;
            }
        }
        return true;

    }


}