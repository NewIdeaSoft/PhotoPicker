package com.nisoft.photopicker;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;


import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private RecyclerView mPhotoPickerRecyclerView;
    private PhotoPickerAdapter mAdapter;
    private ArrayList<String> mImageUrlList;
    private ArrayList<String> mFolderPathList;
    private Button mMenuButton;
    private Button mDownButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (ContextCompat.checkSelfPermission(this
                , Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 11);
        }
        if (ContextCompat.checkSelfPermission(this
                , Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 11);
        }
        setImageUrlList();
        mAdapter = new PhotoPickerAdapter(mImageUrlList, this, new PhotoPickerAdapter.OnImagePicked() {
            @Override
            public void onPicked() {
                ArrayList<String> imageList = mAdapter.getCheckedImageUrlList();
                if (imageList.size()>0){
                    mDownButton.setText("完成（已选"+imageList.size()+"张）");
                }
            }
        });
        mPhotoPickerRecyclerView = (RecyclerView) findViewById(R.id.rv_photo_picker);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        mPhotoPickerRecyclerView.setLayoutManager(layoutManager);
        mPhotoPickerRecyclerView.setAdapter(mAdapter);
        mMenuButton = (Button) findViewById(R.id.btn_menu);
        mDownButton = (Button) findViewById(R.id.btn_down);
        mMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        mDownButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> imageList = mAdapter.getCheckedImageUrlList();
                Intent data = new Intent();
                data.putExtra("picked_images", imageList);
                setResult(RESULT_OK, data);
                finish();
            }
        });
    }

    private void setImageUrlList() {
        mImageUrlList = new ArrayList<>();
        if (mFolderPathList == null) {
            mFolderPathList = new ArrayList<>();
        }
        Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        ContentResolver mContentResolver = MainActivity.this
                .getContentResolver();
        // 只查询jpeg和png的图片
        Cursor mCursor = mContentResolver.query(mImageUri, null,
                MediaStore.Images.Media.MIME_TYPE + "=? or "
                        + MediaStore.Images.Media.MIME_TYPE + "=?",
                new String[]{"image/jpeg", "image/png"},
                MediaStore.Images.Media.DATE_MODIFIED);
        while (mCursor.moveToNext()) {
            // 获取图片的路径
            String path = mCursor.getString(mCursor
                    .getColumnIndex(MediaStore.Images.Media.DATA));
            mImageUrlList.add(path);
            File imageFile = new File(path);
            String dirPath = imageFile.getParent();
            if (dirPath != null && mFolderPathList.indexOf(dirPath) <= 0) {
                mFolderPathList.add(dirPath);
            }
            Log.e("TAG", path);
        }
    }

}
