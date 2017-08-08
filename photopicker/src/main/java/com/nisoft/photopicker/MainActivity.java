package com.nisoft.photopicker;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;


import com.nisoft.photopicker.util.FileUtil;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private RecyclerView mPhotoPickerRecyclerView;
    private PhotoPickerAdapter mAdapter;
    private ArrayList<String> mImageUrlList;
    private ArrayList<String> mFolderPathList;
    private Button mMenuButton;
    private Button mDownButton;
    private DrawerLayout mDrawerLayout;
    private ListView mListView;
    private ArrayAdapter<String> mFolderAdapter;

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
                if (imageList.size() > 0) {
                    mDownButton.setText("完成（已选" + imageList.size() + "张）");
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
                if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
                    mDrawerLayout.closeDrawers();
                } else {
                    mDrawerLayout.openDrawer(GravityCompat.START);
                }
            }
        });
        mDownButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> imageList = mAdapter.getCheckedImageUrlList();
                Log.e("PhotoPicker:", imageList.size() + "");
                Intent data = new Intent();
                data.putExtra("picked_images", imageList);
                setResult(RESULT_OK, data);
                finish();
            }
        });
        mDrawerLayout = (DrawerLayout) findViewById(R.id.photo_picker_drawer_layout);
        mListView = (ListView) findViewById(R.id.photo_picker_menu);
        mFolderAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mFolderPathList) {
            @Override
            public int getCount() {
                return super.getCount()+1;
            }

            @Nullable
            @Override
            public String getItem(int position) {
                if (position == 0) {
                    return "所有图片";
                }
                String path = super.getItem(position-1);
                File file = new File(path);
                return file.getName();
            }
        };
        mListView.setAdapter(mFolderAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position==0){
                    setImageUrlList();
                }else{
                    String path = mFolderPathList.get(position-1);
                    mImageUrlList = FileUtil.getAllImagesName(path);
                }
                mDrawerLayout.closeDrawers();
                mAdapter.notifyDataSetChanged();
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
                MediaStore.Images.Media.DATE_MODIFIED + " desc");
        while (mCursor.moveToNext()) {
            // 获取图片的路径
            String path = mCursor.getString(mCursor
                    .getColumnIndex(MediaStore.Images.Media.DATA));
            mImageUrlList.add(path);
            File imageFile = new File(path);
            String dirPath = imageFile.getParent();
            if (dirPath != null && mFolderPathList.indexOf(dirPath) == -1) {
                mFolderPathList.add(dirPath);
            }
        }
    }
}
