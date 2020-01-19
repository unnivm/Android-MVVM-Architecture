package com.petstore.view.util;

import android.graphics.Bitmap;
import android.util.LruCache;

public class BitmapCache extends LruCache<String, Bitmap> {

    private static BitmapCache cacheObj;

    public BitmapCache(int maxSize) {
        super(maxSize);
    }

    private BitmapCache()
    {
        this(getDefaultLruCacheSize());
    }

    /**
     * Create a static method to get instance.
     */
    public static BitmapCache getInstance()
    {
        if(cacheObj == null)
        {
            cacheObj = new BitmapCache();
        }
        return cacheObj;
    }

    /**
     * this method returns default cache size
     * @return cache size
     */
    public static int getDefaultLruCacheSize()
    {
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        final int cacheSize = maxMemory / 8;
        return cacheSize;
    }

    /**
     * return bitmap size
     * @param key
     * @param value
     * @return
     */
    @Override
    protected int sizeOf(String key, Bitmap value)
    {
        return value.getRowBytes() * value.getHeight() / 1024;
    }

    /**
     * this method used to get bitmap on cache
     * @param url unique identifier
     * @return bitmap
     */
    public Bitmap getBitmap(String url)
    {
        return get(url);
    }

    /**
     * this method used to put bitmap on cache
     * @param url url is unique identifier
     * @param bitmap bitmap to put
     */
    public void putBitmap(String url, Bitmap bitmap)
    {
        put(url, bitmap);
    }
}
