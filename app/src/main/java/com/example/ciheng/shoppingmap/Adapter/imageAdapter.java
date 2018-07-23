package com.example.ciheng.shoppingmap.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ciheng.shoppingmap.Data.uploadPic;
import com.example.ciheng.shoppingmap.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class imageAdapter extends RecyclerView.Adapter<imageAdapter.imageViewHolder> {
    private Context mContext;
    private List<uploadPic> mUploadPics;

    public imageAdapter(Context context, List<uploadPic> uploadPics){
        mContext=context;
        mUploadPics=uploadPics;
    }

    @Override
    public imageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.image_item,parent,false);
        return new imageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(imageViewHolder holder, int position) {
        uploadPic uploadCurrent = mUploadPics.get(position);
        holder.mTextViewName.setText(uploadCurrent.getName());
        Picasso.get()
                .load(uploadCurrent.getImageUrl())
                .fit()
                .centerCrop()
                .into(holder.mImageView);
    }

    @Override
    public int getItemCount() {
        return mUploadPics.size();
    }

    //a recycler view adapter
    public class imageViewHolder extends RecyclerView.ViewHolder{
        public TextView mTextViewName;
        public ImageView mImageView;
        public imageViewHolder(View itemView){
            super(itemView);
            mTextViewName=itemView.findViewById(R.id.nameTextView);
            mImageView=itemView.findViewById(R.id.image_upload);
        }
    }
}
