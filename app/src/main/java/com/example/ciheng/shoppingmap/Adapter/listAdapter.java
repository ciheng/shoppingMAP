package com.example.ciheng.shoppingmap.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.ciheng.shoppingmap.Data.message;
import com.example.ciheng.shoppingmap.R;

import java.util.List;

/**
 * Created by 39112 on 2018/7/28.
 */

public class listAdapter extends BaseAdapter {
    private Context context;

    private List<message> TempSubjectList;

    public listAdapter(List<message> listValue, Context context)
    {
        this.context = context;
        this.TempSubjectList = listValue;
    }

    @Override
    public int getCount()
    {
        return this.TempSubjectList.size();
    }

    @Override
    public Object getItem(int position)
    {
        return this.TempSubjectList.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ViewItem viewItem = null;

        if(convertView == null)
        {
            viewItem = new ViewItem();

            LayoutInflater layoutInfiater = (LayoutInflater)this.context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

            convertView = layoutInfiater.inflate(R.layout.list_item, null);

            viewItem.IdTextView = (TextView)convertView.findViewById(R.id.BuyerName);

            viewItem.NameTextView = (TextView)convertView.findViewById(R.id.buyerMsg);

            convertView.setTag(viewItem);
        }
        else
        {
            viewItem = (ViewItem) convertView.getTag();
        }

        viewItem.IdTextView.setText(TempSubjectList.get(position).senderID);

        viewItem.NameTextView.setText(TempSubjectList.get(position).message);

        return convertView;
    }



}

class ViewItem
{
    TextView IdTextView;
    TextView NameTextView;

}
