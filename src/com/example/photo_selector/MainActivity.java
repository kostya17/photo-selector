package com.example.photo_selector;

import android.app.Activity;
import android.app.Notification;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.LinearLayout;

import java.util.ArrayList;

public class MainActivity extends Activity{

    private static final int PICK_IMAGES_REQUEST = 1;
    private GridView mGridView;
    private SimpleAdapter mAdapter;
    private ArrayList<Image> mImages;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gallery);
        if(mImages != null){
            init();
        }

    }

    private void init() {
        mGridView = (GridView) findViewById(R.id.galleryView);
        mAdapter = new SimpleAdapter(mImages, this);
        mGridView.setAdapter(mAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_IMAGES_REQUEST){
            if (resultCode == RESULT_OK && data != null){
                if(mImages != null){
                    mImages.addAll((ArrayList<Image>) data.getSerializableExtra("selected"));
                } else{
                    mImages = (ArrayList<Image>) data.getSerializableExtra("selected");
                }
                init();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_select){
            Intent pickImage = new Intent(this, GalleryActivity.class);
            startActivityForResult(pickImage, PICK_IMAGES_REQUEST);
            return true;
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mainactivity_menu, menu);
        return true;
    }

}
