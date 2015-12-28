package com.test.bitmap;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.test.cache.BitmapCache;

import java.lang.ref.WeakReference;

/**
 * Created by mac on 15/12/22.
 */
public class BitmapWorkerTask extends AsyncTask<Integer, Integer, Bitmap> {

    private final WeakReference imageViewReference;
    public int data = 0;

    private Context mContext;

    public BitmapWorkerTask(Context mContext, ImageView imageView) {
        this.imageViewReference = new WeakReference(imageView);
        this.mContext = mContext;
    }

    @Override
    protected Bitmap doInBackground(Integer... params) {
        data = params[0];
        final Bitmap bitmap = BitmapUtils.decodeSampledBitmapFromResource(mContext.getResources(), data, 1080, 1080);
        BitmapCache.addBitmapToCache(String.valueOf(data), bitmap);
        return bitmap;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if (isCancelled()) {
            bitmap = null;
        }
        if (imageViewReference != null && bitmap != null) {
            final ImageView imageView = (ImageView) imageViewReference.get();

            final BitmapWorkerTask bitmapWorkerTask = BitmapUtils.getBitmapWorkerTask(imageView);
            if (this == bitmapWorkerTask && imageView != null) {
                imageView.setImageBitmap(bitmap);
            }
        }
    }
}
