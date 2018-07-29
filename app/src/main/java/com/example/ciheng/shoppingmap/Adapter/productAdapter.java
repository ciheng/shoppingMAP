package com.example.ciheng.shoppingmap.Adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.ciheng.shoppingmap.Data.product;
import com.example.ciheng.shoppingmap.R;

import java.util.List;




public class productAdapter extends RecyclerView.Adapter<productAdapter.ViewHolder>{

    private Context mContext;
    private List<product> mProductList;

    static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardview;
        ImageView Productimage;
        TextView ProductName;
        TextView ProductPrice;
        TextView ProductDescription;

        public ViewHolder(View view) {
            super(view);
            cardview = (CardView) view;
            Productimage = (ImageView) view.findViewById(R.id.photo);
            ProductName = (TextView) view.findViewById(R.id.item_name);
            ProductPrice= (TextView) view.findViewById(R.id.price);
            ProductDescription= (TextView) view.findViewById(R.id.description);

        }
    }

    public productAdapter(List<product> mProductList) {
        this.mProductList = mProductList;
    }

    @Override
    public productAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null) {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.card_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(productAdapter.ViewHolder holder, int position) {
        product Product = mProductList.get(position);
        holder.ProductName.setText(Product.getName());
        holder.ProductPrice.setText(Product.getPrice());
        holder.ProductDescription.setText(Product.getDescription());
        Glide.with(mContext).load(Product.getProductId()).into(holder.Productimage);            //Glide是加载图片的方式
    }

    @Override
    public int getItemCount() {
        return mProductList.size();
    }



}
