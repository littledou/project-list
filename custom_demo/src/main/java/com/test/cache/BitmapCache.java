package com.test.cache;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v4.util.LruCache;

import java.io.File;
import java.io.IOException;

/**
 * Created by mac on 15/12/23.
 */
public class BitmapCache {

    private static DiskLruCache diskLruCache;
    private static final Object diskLruCacheLock = new Object();
    private static final int DISK_CACHE_SIZE = 1020 * 1024 * 10;
    private static final String DISK_CACHE_SUBDIR = "thumbnails";

    private static LruCache<String, Bitmap> mMemoryCache;

    private void initInstence(Context mContext) {
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        final int cacheSize = maxMemory / 8;

        mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getByteCount() / 1024;
            }
        };

        File cacheDir = getDiskCacheDir(mContext, DISK_CACHE_SUBDIR);

        new InitDiskLruCacheTask().execute(cacheDir);
    }

    public static void addBitmapToCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemoryCache(key) == null) {
            mMemoryCache.put(key, bitmap);
        }

        synchronized (diskLruCacheLock) {
            try {
                if (diskLruCache != null && diskLruCache.get(key) == null) {

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static Bitmap getBitmapFromMemoryCache(String key) {
        return mMemoryCache.get(key);
    }

    public static File getDiskCacheDir(Context context, String uniqueName) {
        String cachePath = "";
        //判断使用内部缓存还是外部缓存
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) || !Environment.isExternalStorageRemovable()) {
            cachePath = context.getExternalCacheDir().getPath();
        } else {
            cachePath = context.getCacheDir().getPath();
        }
        return new File(cachePath + File.separator + uniqueName);
    }


    class InitDiskLruCacheTask extends AsyncTask<File, Void, Void> {

        @Override
        protected Void doInBackground(File... params) {

            synchronized (diskLruCacheLock) {
                File cacheDir = params[0];
                try {
                    diskLruCache = DiskLruCache.open(cacheDir, 1, 100, DISK_CACHE_SIZE);
                    diskLruCacheLock.notifyAll();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

    }
}
