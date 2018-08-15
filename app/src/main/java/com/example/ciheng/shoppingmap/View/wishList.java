package com.example.ciheng.shoppingmap.View;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.ciheng.shoppingmap.Adapter.wishListAdapter;
import com.example.ciheng.shoppingmap.Data.product;
import com.example.ciheng.shoppingmap.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class wishList extends ListActivity {
    private int mUserId;
    private product Product;
    private wishListAdapter adapter;
    private RecyclerView recyclerView;
    private List<product> mWishList = new ArrayList<>();
    private List<product> newList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wish_list);
        Intent intent = getIntent();
        mUserId = intent.getIntExtra("user_id", -1);
        getWishList();

        recyclerView = (RecyclerView) findViewById(R.id.wishlist_recyclerView);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 1);

        recyclerView.setLayoutManager(layoutManager);
        adapter = new wishListAdapter(mWishList);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickLitener(new wishListAdapter.OnItemClickListerner() {
            @Override
            public void onItemClick(View view, int position) {                        //点击card跳转

                Intent intent = new Intent(wishList.this, ProductDetail.class);
                Bundle extras = new Bundle();
                extras.putInt("user_id", mUserId);
                extras.putInt("product_id", mWishList.get(position).getProductId());
                intent.putExtras(extras);
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });
    }

    private void getWishList() {


        RequestQueue data = Volley.newRequestQueue(this);

        String url = "http://api.a17-sd207.studev.groept.be/getwishlist/" + mUserId;
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {

                            for (int i = 0; i < response.length(); i++) {
                                JSONObject Event = response.getJSONObject(i);
                                int product_id = Event.getInt("id_product");
                                String name = Event.getString("name");
                                name = name.replaceAll("%20", " ");
                                String description = Event.getString("description");
                                description = description.replaceAll("%20", " ");
                                String price = Event.getString("price");
                                String download = Event.getString("download");

                                Product = new product(name, mUserId, price, description, product_id);
                                Product.setDownloadUrl(download);
                                int flag = 0;
                                for (int count = 0; count < newList.size(); count++) {
                                    if (newList.get(count).getProductId() == product_id) {
                                        flag = 1;
                                        break;
                                    } else {
                                        flag = 0;
                                    }
                                }

                                if (flag == 0) {
                                    newList.add(Product);
                                    updateWishList(newList);
                                }

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }

        });
        data.add(request);

    }

    public void updateWishList(List<product> newlist) {
        mWishList.clear();
        mWishList.addAll(newlist);
        Collections.reverse(mWishList);        //倒叙显示
        adapter.notifyDataSetChanged();
    }

}
