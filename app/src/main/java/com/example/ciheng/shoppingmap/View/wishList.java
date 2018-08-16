package com.example.ciheng.shoppingmap.View;

import android.app.ListActivity;
import android.app.usage.UsageEvents;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.ciheng.shoppingmap.Adapter.productAdapter;
import com.example.ciheng.shoppingmap.Adapter.wishListAdapter;
import com.example.ciheng.shoppingmap.Data.product;
import com.example.ciheng.shoppingmap.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class wishList  extends AppCompatActivity {
    private int mUserId;
    private product Product;
    private productAdapter adapter;
    private RecyclerView recyclerView;
    private List<product> mWishList = new ArrayList<>();
    private List<product> newList = new ArrayList<>();
    private String name;
    private String description;
    private String price;
    private String download;
    private SwipeRefreshLayout swipeRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);
        Intent intent = getIntent();

        mUserId = intent.getIntExtra("user_id", -1);
        getWishList();

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 1);

        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);                  //下拉刷新
        swipeRefresh.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                refreshProducts();

            }
        });


        recyclerView.setLayoutManager(layoutManager);
        adapter = new productAdapter(mWishList);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickLitener(new productAdapter.OnItemClickListerner() {
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
                                int product_id = Event.getInt("product");
                                getWishProducts(product_id);

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

    public void getWishProducts(final int productID)
    {

        RequestQueue data = Volley.newRequestQueue(this);

        String url = "http://api.a17-sd207.studev.groept.be/getwishproduct/" + productID;
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {

                            for (int i = 0; i < response.length(); i++) {
                                JSONObject Event = response.getJSONObject(i);
                                name = Event.getString("name");
                                name=name.replaceAll("%20"," ");
                                description = Event.getString("description");
                                description=description.replaceAll("%20"," ");
                                price = Event.getString("price");
                                download = Event.getString("download");

                                Product = new product(name,mUserId,price,description,productID);
                                Product.setDownloadUrl(download);
                                int flag=0;
                                for(int count=0;count<newList.size();count++)
                                {
                                    if(newList.get(count).getProductId()==productID)
                                    {
                                        flag=1;
                                        break;
                                    }
                                    else
                                    {
                                        flag=0;
                                    }
                                }

                                if(flag==0) {
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


    private void refreshProducts() {                         //下拉刷新
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        recyclerView.removeAllViews();            //刷新前需要把view和数据清空，加这里没用
                        getWishList();


                        swipeRefresh.setRefreshing(false);
                    }
                });

            }
        }).start();
    }


    public void updateWishList(List<product> newlist) {
        mWishList.clear();
        mWishList.addAll(newlist);
        Collections.reverse(mWishList);        //倒叙显示
        adapter.notifyDataSetChanged();
    }

}
