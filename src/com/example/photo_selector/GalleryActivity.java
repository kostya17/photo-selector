package com.example.photo_selector;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.GridView;

import java.util.ArrayList;

public class GalleryActivity extends Activity implements ActionMode.Callback, SelectionListener {

    private static final String IMAGES_RESTORING_KEY = "images";
    private static final String CAB_RESTORING_KEY = "cab";
    private static final String SELECTED = " selected";
    private GridView mGridView;
    private GalleryAdapter mAdapter;
    private ArrayList<Image> mImages;
    private ActionMode mActionMode;
    private boolean isInActionMode = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gallery);
        mGridView = (GridView) findViewById(R.id.galleryView);
        mGridView.setFastScrollEnabled(true);

        if (savedInstanceState != null && savedInstanceState.getSerializable(IMAGES_RESTORING_KEY) != null){
            mImages = (ArrayList<Image>) savedInstanceState.getSerializable(IMAGES_RESTORING_KEY);
        } else {
            mImages = getGalleryImages();
        }

        mAdapter = new GalleryAdapter(this, mImages);
        mGridView.setAdapter(mAdapter);

        if (savedInstanceState != null && savedInstanceState.getBoolean(CAB_RESTORING_KEY)){
            mActionMode = startActionMode(this);
            mActionMode.setTitle(mAdapter.getSelectedCount() + SELECTED);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if (mAdapter.isSelectedItems()){
            outState.putSerializable(IMAGES_RESTORING_KEY, mImages);
            outState.putBoolean(CAB_RESTORING_KEY, isInActionMode);
        }
    }

    @Override
    public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
        MenuInflater inflater = actionMode.getMenuInflater();
        inflater.inflate(R.menu.contextual_menu, menu);
        isInActionMode = true;
        actionMode.setTitle(mAdapter.getSelectedCount() + SELECTED);
        return true;
    }

    @Override
    public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.action_next){
            if(mAdapter.isSelectedItems()){
                ArrayList<Image> selectedImages = mAdapter.getSelected();
                Intent reseltIntent = new Intent();
                reseltIntent.putExtra("selected", selectedImages);
                setResult(RESULT_OK, reseltIntent);
                finish();
            } else {
                setResult(RESULT_CANCELED);
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
        return false;
    }

    @Override
    public void onDestroyActionMode(ActionMode actionMode) {
        mAdapter.deselectAll();
        mActionMode = null;
        isInActionMode = false;
    }

    @Override
    public void onItemSelected() {
        if (mActionMode == null){
            mActionMode = startActionMode(this);
        }
        mActionMode.setTitle(mAdapter.getSelectedCount() + SELECTED);
        VibratorUtil.init(this).vibrate(30);
    }

    @Override
    public void onItemDeselected() {
        if (mAdapter.getSelectedCount() == 0){
            mActionMode.finish();
        } else{
            mActionMode.setTitle(mAdapter.getSelectedCount() + SELECTED);
        }
    }

    private ArrayList<Image> getGalleryImages(){
        ArrayList<Image> images = null;
        Cursor externalImageCursor;
        Image img;
        String[] projection = {MediaStore.Images.Media.DATA};
        externalImageCursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, null, null, MediaStore.Images.Media.DEFAULT_SORT_ORDER);
        if (externalImageCursor.moveToFirst()){
            images = new ArrayList<Image>(externalImageCursor.getCount());
            int index = externalImageCursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            do{
                img = new Image();
                img.mURIPath = externalImageCursor.getString(index);
                images.add(img);
            } while (externalImageCursor.moveToNext());
        }

        return images;
    }
}
