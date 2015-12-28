package com.test.bitmap;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;

import java.lang.ref.WeakReference;

/**
 * Created by mac on 15/12/23.
 */
public class AsyncDrawable extends BitmapDrawable {
    private final WeakReference bitmapWorkerTaskReference;

    public AsyncDrawable(Resources res, Bitmap bitmap, BitmapWorkerTask bitmapWorkerTask) {
        super(res, bitmap);
        this.bitmapWorkerTaskReference = new WeakReference(bitmapWorkerTask);
    }

    public BitmapWorkerTask getBitmaoWorkerTask() {
        return (BitmapWorkerTask) bitmapWorkerTaskReference.get();
    }
}
