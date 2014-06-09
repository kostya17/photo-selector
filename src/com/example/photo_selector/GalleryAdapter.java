package com.example.photo_selector;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import java.util.ArrayList;

public class GalleryAdapter extends BaseAdapter {

    private ArrayList<Image> mGalleryImages;
    private Context mContext;
    private LayoutInflater mInflater;
    private ImageViewLoader loader;

    public GalleryAdapter(Context context, ArrayList<Image> images) {
        mContext = context;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mGalleryImages = images;
    }

    @Override
    public int getCount() {
        return mGalleryImages.size();
    }

    @Override
    public Object getItem(int i) {
        return mGalleryImages.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public boolean isSelectedItems(){
        for(Image img : mGalleryImages){
            if (img.mSelected){
                return true;
            }
        }
        return false;
    }

    public ArrayList<Image> getSelected(){
        ArrayList<Image> selected = new ArrayList<Image>();
        if (isSelectedItems()){
            for(Image img : mGalleryImages){
                if (img.mSelected)
                    selected.add(img);
            }
        }
        return selected;
    }

    public int getSelectedCount(){
        int result = 0;
        if (isSelectedItems()){
            for(Image img : mGalleryImages){
                if (img.mSelected)
                    result++;
            }
        }
        return result;
    }

    public void deselectAll(){
        for(Image img : mGalleryImages){
            img.mSelected = false;
        }
    }

    private void setSelectoin(ViewHolder viewHolder, int position){
        if (mGalleryImages.get(position).mSelected){
            mGalleryImages.get(position).mSelected = false;
            viewHolder.mImageView.setBackgroundColor(Color.WHITE);
            ((SelectionListener)mContext).onItemDeselected();
        } else{
            mGalleryImages.get(position).mSelected = true;
            viewHolder.mImageView.setBackgroundColor(Color.parseColor("#33b5e5"));
            ((SelectionListener)mContext).onItemSelected();
        }
        viewHolder.mSelection.setChecked(mGalleryImages.get(position).mSelected);
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        final ViewHolder holder;

        if (view == null){
            view = mInflater.inflate(R.layout.gallery_item, null);
            holder = new ViewHolder();
            holder.mImageView = (ImageView) view.findViewById(R.id.imageThumb);
            holder.mSelection = (CheckBox) view.findViewById(R.id.selectorThumb);
            view.setTag(holder);

        } else {
            holder = (ViewHolder) view.getTag();
        }

        loader = new ImageViewLoader(holder.mImageView);
        loader.loadImagesBitmap(mGalleryImages.get(i).mURIPath);

        holder.mSelection.setChecked(mGalleryImages.get(i).mSelected);
        holder.mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setSelectoin(holder, i);
            }
        });

        return view;
    }

    static class ViewHolder{
        ImageView mImageView;
        CheckBox mSelection;
    }
}