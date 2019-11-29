package com.ethanchen.weatherapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PhotoItemAdapter extends RecyclerView.Adapter<PhotoItemAdapter.PhotoItemViewHolder> {

    private ArrayList<PhotoItem> mPhotoItemArrayList;
    private Context context;

    public static class PhotoItemViewHolder extends RecyclerView.ViewHolder {
        public ImageView mIamgeView;


        public PhotoItemViewHolder(View itemView) {
            super(itemView);
            mIamgeView = itemView.findViewById(R.id.photo_image);
        }
    }

    public PhotoItemAdapter(Context c, ArrayList<PhotoItem> photoItemArrayList) {
        context = c;
        mPhotoItemArrayList = photoItemArrayList;
    }

    @Override
    public PhotoItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_item, parent, false);
        PhotoItemViewHolder evh = new PhotoItemViewHolder(v);
        return evh;
    }

    @Override
    public void onBindViewHolder(PhotoItemViewHolder holder, int position) {
        PhotoItem currentItem = mPhotoItemArrayList.get(position);
        String text = currentItem.getText();

        //Picasso.with(context).load(text).into(holder.mIamgeView);
        PicassoClient.downloadImage(context, text, holder.mIamgeView);

        //holder.mIamgeView.setImageResource(currentItem.getImageResource());
    }

    @Override
    public int getItemCount() {
        return mPhotoItemArrayList.size();
    }

}
