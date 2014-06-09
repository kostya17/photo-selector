package com.example.photo_selector;

import android.graphics.Bitmap;
import android.util.LruCache;
import android.widget.ImageView;

public class ImageCache extends LruCache<String, Bitmap> {

    private static final int BYTE = 1024;
    private static final int PART = 2;

    private static ImageCache mImageCache;

    private ImageCache(int maxSize) {
        super(maxSize);
    }


    public static ImageCache init(int size){
        if (mImageCache == null){
            mImageCache = new ImageCache(size);
        }
        return mImageCache;
    }

    public static ImageCache init(){
        if (mImageCache == null){
            mImageCache = new ImageCache(defaultCacheSize());
        }
        return mImageCache;
    }

    @Override
    protected int sizeOf(String key, Bitmap value) {
        return value.getByteCount() / BYTE;
    }

    private static int defaultCacheSize(){
        return (int) (Runtime.getRuntime().maxMemory() / BYTE) /PART;
    }

    public Bitmap getCachedBitmap(String key){
        return get(key);
    }

    public void addBitmapToCache(String key, Bitmap value){
        if (getCachedBitmap(key) == null){
            put(key, value);
        }
    }
}
