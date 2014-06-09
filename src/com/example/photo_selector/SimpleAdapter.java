package com.example.photo_selector;

import android.app.Service;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.ArrayList;

public class SimpleAdapter extends BaseAdapter {

    private ArrayList<Image> mImages;
    private Context mContext;
    private LayoutInflater mInflater;
    private ImageViewLoader mLoader;

    public SimpleAdapter(ArrayList<Image> images, Context context) {
        mImages = images;
        mContext = context;
        mInflater = (LayoutInflater) mContext.getSystemService(Service.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mImages.size();
    }

    @Override
    public Object getItem(int i) {
        return mImages.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final ImageView imageView;
        if (view == null){
            view = mInflater.inflate(R.layout.result_item, null);
            imageView = (ImageView) view.findViewById(R.id.resultImageThumb);
            view.setTag(imageView);
        } else {
            imageView = (ImageView) view.getTag();
        }

        mLoader = new ImageViewLoader(imageView);
        mLoader.loadImagesBitmap(mImages.get(i).mURIPath);

        return view;
    }
}
