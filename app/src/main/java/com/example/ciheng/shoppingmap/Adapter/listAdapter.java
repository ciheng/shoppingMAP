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

public class listAdapter extends BaseAdapter {
    private Context context;
    private List<message> msgList;


    public listAdapter(List<message> listValue, Context context) {
        super();
        this.context = context;
        this.msgList = listValue;
    }

    public productAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (context == null) {
            context = parent.getContext();
        }
        View view = LayoutInflater.from(context).inflate(R.layout.card_view, parent, false);
        return new productAdapter.ViewHolder(view);
    }


    @Override
    public int getCount() {
        return msgList.size();
    }

    @Override
    public Object getItem(int i) {
        return msgList.get(i);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            holder.Productimage = (ImageView) view.findViewById(R.id.photo);
            holder.senderName = (TextView) view.findViewById(R.id.senderName);
            holder.message=(TextView)view.findViewById(R.id.message);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        message mMsg = msgList.get(i);
        holder.message.setText(mMsg.getMessage());
        holder.senderName.setText(mMsg.getSenderName());
        Glide.with(context).load(mMsg.getThumbnailUrl()).into(holder.Productimage);

        return view;

    }

    class ViewHolder {
        ImageView Productimage;
        TextView senderName;
        TextView message;

    }
}