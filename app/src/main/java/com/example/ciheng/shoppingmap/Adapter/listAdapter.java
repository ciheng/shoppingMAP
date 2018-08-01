package com.example.ciheng.shoppingmap.Adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.ciheng.shoppingmap.Data.message;
import com.example.ciheng.shoppingmap.Data.product;
import com.example.ciheng.shoppingmap.R;
import com.example.ciheng.shoppingmap.View.MessageList_Buyer;

import java.util.List;

/**
 * Created by 39112 on 2018/7/28.
 */

public class listAdapter extends RecyclerView.Adapter<listAdapter.ViewHolder>{
    private Context context;

    private List<message> msgList;

    static class ViewHolder extends RecyclerView.ViewHolder {
        ListView listview;
        ImageView Productimage;
        TextView senderName;
        TextView message;


        public ViewHolder(View view) {
            super(view);
            listview = (ListView) view;
            Productimage = (ImageView) view.findViewById(R.id.imageView);
            senderName = (TextView) view.findViewById(R.id.senderName);
            message= (TextView) view.findViewById(R.id.message);


        }


    }



    public listAdapter(List<message> listValue, Context context)
    {
        this.context = context;
        this.msgList = listValue;
    }


    public listAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (context == null) {
            context = parent.getContext();
        }
        View view = LayoutInflater.from(context).inflate(R.layout.activity_message_list_buyer, parent, false);
        return new listAdapter.ViewHolder(view);
    }



    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public int getItemCount() {
        return msgList.size();
    }


    public void onBindViewHolder(final listAdapter.ViewHolder holder, int position) {

        message Message = msgList.get(position);
        holder.senderName.setText(Message.getSenderName());
        holder.message.setText(Message.getMessage());
        Glide.with(context).load(Message.getThumbnailUrl()).into(holder.Productimage);            //Glide是加载图片的方式
    }
}


class ViewItem
{
    ImageView imageView;
    TextView senderView;
    TextView msgView;
}

