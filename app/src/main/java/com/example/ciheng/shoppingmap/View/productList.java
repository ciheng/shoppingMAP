package com.example.ciheng.shoppingmap.View;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.ciheng.shoppingmap.Adapter.productAdapter;
import com.example.ciheng.shoppingmap.Data.product;
import com.example.ciheng.shoppingmap.Data.userData;
import com.example.ciheng.shoppingmap.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class productList extends AppCompatActivity {

    private DrawerLayout mDrawerlayout;
    private userData user = (userData) getApplication();
    private product Product;
    private int mUserId;
    private int product_id;
    private String name;
    private boolean flag=false;
    private RecyclerView recyclerView;

    private productAdapter adapter;
    private List<product> mProductList = new ArrayList<>();

    private SwipeRefreshLayout swipeRefresh;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent intent = getIntent();
        mUserId = intent.getIntExtra("user_id", -1);

        setContentView(R.layout.activity_product_list);
        getItem();

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 1);

        recyclerView.setLayoutManager(layoutManager);
        adapter = new productAdapter(mProductList);
        recyclerView.setAdapter(adapter);

        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);                  //下拉刷新
        swipeRefresh.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshProducts();
            }
        });


        adapter.setOnItemClickLitener(new productAdapter.OnItemClickListerner() {
            @Override
            public void onItemClick(View view, int position) {                        //总觉得还是不要跳转到详情页比较好...和地图点击公用物品详情页不大好又不想再做一个...
                /*
                Intent intent = new Intent(productList.this, ProductDetail.class);
                intent.putExtra("prouct_id",mProductList.get(position).getProductId());
                startActivity(intent);*/
            }

            @Override
            public void onItemLongClick(View view, final int position) {//长按删除

                AlertDialog.Builder builder = new AlertDialog.Builder(productList.this);
                builder.setMessage("Do you want to delete this item？");
                builder.setTitle("Alert");
                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        adapter.removeData(position);
                        deleteItem();
                    }

                });
                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.create().show();


            }
        });


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(productList.this, addProductActivity.class);
                startActivity(intent);
            }
        });
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
                        recyclerView.removeAllViews();            //刷新前需要把view和数据清空，还不知道怎么清....

                        getItem();
                        adapter.notifyDataSetChanged();
                        swipeRefresh.setRefreshing(false);
                    }
                });

            }
        }).start();
    }




    private void getItem()

    {
        RequestQueue data = Volley.newRequestQueue(this);

        String url = "http://api.a17-sd207.studev.groept.be/selling_list/" + mUserId;
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {

                            for (int i = 0; i < response.length(); i++) {
                                JSONObject Event = response.getJSONObject(i);
                                product_id=Event.getInt("id_product");

                                name = Event.getString("name");
                                name=name.replaceAll("%20"," ");
                                String description = Event.getString("description");
                                description=description.replaceAll("%20"," ");
                                String price = Event.getString("price");
                                String download = Event.getString("download");
                                String thumbnail= Event.getString("thumbnail");

                                Product = new product(name,mUserId,price,description,product_id);
                                Product.setDownloadUrl(download);
                                Product.setThumbnailUrl(thumbnail);

                                mProductList.add(Product);


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

    private void deleteItem()

    {
        RequestQueue data = Volley.newRequestQueue(this);

        String url = "http://api.a17-sd207.studev.groept.be/deleteProduct/" + name;
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        for (int i = 0; i < response.length(); i++) {


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


}
