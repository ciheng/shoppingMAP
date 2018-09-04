package com.example.ciheng.shoppingmap.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.ciheng.shoppingmap.Data.message;
import com.example.ciheng.shoppingmap.R;

import java.util.List;

/**
 * Created by 39112 on 2018/7/28.
 */

public class listAdapter extends RecyclerView.Adapter<listAdapter.ViewHolder> {           //This is the adapter used for the message list
    private Context mContext;
    private List<message> msgList;
    private OnItemClickListerner mOnItemClickListerner;

    static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView Productimage;
        TextView senderName;
        TextView message;


        public ViewHolder(View view) {                                                     //To connect the adapter to the item shows message by ViewHolder
            super(view);
            Productimage = (ImageView) view.findViewById(R.id.itemphoto);
            senderName = (TextView) view.findViewById(R.id.senderName);
            message = (TextView) view.findViewById(R.id.messages);
        }


    }

    public listAdapter(List<message> msgList) {
        this.msgList = msgList;
    }


    @Override
    public listAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_item, parent, false);    //add list item into this recyclerview
        return new listAdapter.ViewHolder(view);
    }

    public interface OnItemClickListerner {
        void onItemClick(View view, int position);

    }

    public void setOnItemClickLitener(OnItemClickListerner mOnItemClickListerner) {
        this.mOnItemClickListerner = mOnItemClickListerner;
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {             //Set what's on one item of the list

        if (mOnItemClickListerner != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = holder.getLayoutPosition();
                    mOnItemClickListerner.onItemClick(holder.itemView, pos);
                }
            });

        }


        message message = msgList.get(position);
        if (message.isSender()) {
            holder.senderName.setText("To: " + message.getReceiverName());
        } else{
            holder.senderName.setText("From: " + message.getSenderName());
        }
        holder.message.setText(message.getMessage());
        Glide.with(mContext).load(message.getProductUrl()).into(holder.Productimage);            //Glide is the way to download images
    }

    @Override
    public int getItemCount() {
        return msgList.size();
    }

}