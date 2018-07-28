package com.example.ciheng.shoppingmap.View;

import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
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
    userData user = (userData) getApplication();
    private int mUserId;

    private productAdapter adapter;
    private List<product> mProductList=new ArrayList<>();

    private SwipeRefreshLayout swipeRefresh;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent intent = getIntent();
        mUserId = intent.getIntExtra("user_id", -1);

        setContentView(R.layout.activity_product_list);



        getItem();

        RecyclerView recyclerView=(RecyclerView)findViewById(R.id.recycler_view);
        GridLayoutManager layoutManager=new GridLayoutManager(this,2);
        recyclerView.setLayoutManager(layoutManager);
        adapter=new productAdapter(mProductList);
        recyclerView.setAdapter(adapter);

        swipeRefresh=(SwipeRefreshLayout)findViewById(R.id.swipe_refresh);                  //下拉刷新
        swipeRefresh.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshProducts();
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
                try{
                    Thread.sleep(2000);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
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

        String url = "http://api.a17-sd207.studev.groept.be/selling_list" + mUserId;
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {

                            for (int i = 0; i < response.length(); i++) {
                                JSONObject Event = response.getJSONObject(i);


                                    String IN = Event.getString("name");
                                    String IT = Event.getString("description");
                                    String PZ = Event.getString("price");
                                    int IM=Event.getInt("downloadUrl");
                                    product P=null;
                                    P.setDescreption(IT);
                                    P.setName(IN);
                                    P.setPrice(PZ);
                                    P.setImageId(IM);
                                    mProductList.add(P);


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


}
