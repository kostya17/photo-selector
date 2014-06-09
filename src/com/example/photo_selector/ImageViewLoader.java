package com.example.photo_selector;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ImageViewLoader {

    private ExecutorService mExecutorService;
    private static final int SIZE = 100;
    private ImageCache mCache;
    private ImageView mImageView;

    public ImageViewLoader(ImageView imageView) {
        mExecutorService = Executors.newSingleThreadExecutor();
        mCache = ImageCache.init();
        mImageView = imageView;
    }

    public void loadImagesBitmap(final String path){
        mImageView.setTag(path);
        mImageView.setImageBitmap(null);
        Runnable task = new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap = mCache.getCachedBitmap(path);
                if (bitmap == null){
                    bitmap = decodeSampledBitmapFromUri(path, SIZE, SIZE);
                    mCache.addBitmapToCache(path, bitmap);
                }
                if (isImageViewVisible(path)){
                    setBitmapToView(bitmap);
                }
            }
        };
        mExecutorService.submit(task);
    }

    // Check is imageview still visible
    private boolean isImageViewVisible(String path){
        return path.equals(mImageView.getTag());
    }

    /*
     More info about this method and next one read at
     http://developer.android.com/training/displaying-bitmaps/load-bitmap.html
    */
    private Bitmap decodeSampledBitmapFromUri(String path, int reqWidth, int reqHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(path, options);
    }

    private int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    // Set bitmap to view in UI thread
    private void setBitmapToView(final Bitmap bitmap){
        mImageView.post(new Runnable() {
            @Override
            public void run() {
                mImageView.setImageBitmap(bitmap);
            }
        });
    }
}
