package com.nisoft.photopicker;

import android.content.Context;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/7/31.
 */

public class PhotoPickerAdapter extends RecyclerView.Adapter<PhotoPickerAdapter.ViewHolder> {
    private ArrayList<Image> mImageUrlList;
    private OnImagePicked mOnImagePicked;
    private Context mContext;
    public interface OnImagePicked{
        void onPicked();
    }
    public PhotoPickerAdapter(ArrayList<String> imageUrlList, Context context,OnImagePicked imagePicked) {
        mImageUrlList = new ArrayList<>();
        for (String url :
                imageUrlList) {
            mImageUrlList.add(new Image(url, false));
        }
        mContext = context;
        mOnImagePicked = imagePicked;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(mContext, R.layout.photo_item, null);
        final ViewHolder holder = new ViewHolder(view);
        holder.mPhotoImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.mCheckRadioButton.setChecked(!holder.mCheckRadioButton.isChecked());
                mOnImagePicked.onPicked();
            }
        });
        holder.mCheckRadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.mCheckRadioButton.setChecked(!holder.mCheckRadioButton.isChecked());
                mOnImagePicked.onPicked();
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Log.e("adapter",mImageUrlList.get(position).getImageUrl());
        Glide.with(mContext).load(mImageUrlList.get(position).getImageUrl()).centerCrop().into(holder.mPhotoImageView);
        holder.mCheckRadioButton.setChecked(mImageUrlList.get(position).isChecked());
    }

    @Override
    public int getItemCount() {
        return mImageUrlList.size();
    }
    public ArrayList<String> getCheckedImageUrlList(){
        ArrayList<String> checkedImages = new ArrayList<>();
        for (Image image :
                mImageUrlList) {
            if (image.isChecked()){
                checkedImages.add(image.getImageUrl());
            }
        }
        return checkedImages;
    }
    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView mPhotoImageView;
        RadioButton mCheckRadioButton;

        public ViewHolder(View itemView) {
            super(itemView);
            mPhotoImageView = (ImageView) itemView.findViewById(R.id.iv_photo);
            mCheckRadioButton = (RadioButton) itemView.findViewById(R.id.rbtn_picker);
        }
    }
    class Image{
        String mImageUrl;
        boolean isChecked;

        public Image(String imageUrl, boolean isChecked) {
            mImageUrl = imageUrl;
            this.isChecked = isChecked;
        }

        public String getImageUrl() {
            return mImageUrl;
        }

        public void setImageUrl(String imageUrl) {
            mImageUrl = imageUrl;
        }

        public boolean isChecked() {
            return isChecked;
        }

        public void setChecked(boolean checked) {
            isChecked = checked;
        }
    }
}
