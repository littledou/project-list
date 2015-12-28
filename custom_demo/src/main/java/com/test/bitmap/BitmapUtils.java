package com.test.bitmap;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

/**
 * Created by mac on 15/12/23.
 */
public class BitmapUtils {

    //加载一张bitmap
    public static Bitmap decodeSampledBitmapFromResource(Resources resources, int res, int reqWidth, int reqHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;//解码时不分配内存
        BitmapFactory.decodeResource(resources, res, options);
        int width = options.outWidth;
        int height = options.outHeight;
        String imgType = options.outMimeType;

        int inSampleSize = 1;
        if (width > reqWidth || height > reqHeight) {
            width /= 2;
            height /= 2;
            while ((width / inSampleSize) > reqWidth || (height / inSampleSize > reqHeight)) {
                inSampleSize *= 2;
            }
        }
        options.inSampleSize = inSampleSize;
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(resources, res, options);
    }

    public static BitmapWorkerTask getBitmapWorkerTask(ImageView imageView) {
        if (imageView != null) {
            final Drawable drawable = imageView.getDrawable();
            if (drawable instanceof AsyncDrawable) {
                final AsyncDrawable asyncDrawable = (AsyncDrawable) drawable;
                return asyncDrawable.getBitmaoWorkerTask();
            }
        }
        return null;
    }
}
