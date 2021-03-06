
/*******************References:
 * https://developer.android.com/guide/topics/ui/layout/recyclerview
 * https://developer.android.com/reference/android/support/v7/widget/CardView
 **************/
package com.example.ciheng.shoppingmap.View;

import android.app.AlertDialog;
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
import android.util.Log;
import android.view.View;

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
import java.util.Collections;
import java.util.List;


public class productList extends AppCompatActivity {
    private DrawerLayout mDrawerlayout;
    private userData user = (userData) getApplication();
    private product Product;
    private int mUserId;
    private int product_id;
    private String name;
    private RecyclerView recyclerView;

    private productAdapter adapter;
    private List<product> mProductList = new ArrayList<>();
    private List<product> newList = new ArrayList<>();


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

        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);                  //refresh data when swipe the screen
        swipeRefresh.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                refreshProducts();

            }
        });


        adapter.setOnItemClickLitener(new productAdapter.OnItemClickListerner() {
            @Override
            public void onItemClick(View view, int position) {                        //go to the corresponding product detail when click card

                Intent intent = new Intent(productList.this, ProductDetail.class);
                Bundle extras = new Bundle();
                extras.putInt("user_id", mUserId);
                extras.putInt("product_id", mProductList.get(position).getProductId());
                intent.putExtras(extras);
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(View view, final int position) {            //delete the item when long clicking the card

                AlertDialog.Builder builder = new AlertDialog.Builder(productList.this);   // an alert to check if one really want to delete this item
                builder.setMessage("Do you want to delete this item？");
                builder.setTitle("Alert");
                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int id = mProductList.get(position).getProductId();
                        Log.v("delet product id=", id + "");
                        deleteItem(id);                             // delete from database
                        adapter.removeData(position);
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
                intent.putExtra("user_id", mUserId);
                startActivity(intent);
            }
        });
    }


    private void refreshProducts() {                         //updating the data
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
                        recyclerView.removeAllViews();            //clear the view before updating
                        getItem();


                        swipeRefresh.setRefreshing(false);
                    }
                });

            }
        }).start();
    }

    public void updateProductList(List<product> newlist) {
        mProductList.clear();
        mProductList.addAll(newlist);
        Collections.reverse(mProductList);
        adapter.notifyDataSetChanged();
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
                                product_id = Event.getInt("id_product");
                                name = Event.getString("name");
                                name = name.replaceAll("%20", " ");
                                String description = Event.getString("description");
                                description = description.replaceAll("%20", " ");
                                String price = Event.getString("price");
                                String download = Event.getString("download");

                                Product = new product(name, mUserId, price, description, product_id);

                                Product.setDownloadUrl(download);
                                int flag = 0;
                                for (int count = 0; count < newList.size(); count++) {
                                    if (newList.get(count).getProductId() == product_id)   //avoid duplicate item
                                    {
                                        flag = 1;
                                        break;
                                    } else {
                                        flag = 0;
                                    }
                                }

                                if (flag == 0) {
                                    newList.add(Product);
                                    updateProductList(newList);
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

    private void deleteItem(int id)

    {
        RequestQueue data = Volley.newRequestQueue(this);

        String url = "http://a17-sd207.studev.groept.be/peng/deleteproduct.php/?&id=" + id;   //we wrote another php file to delete all information related to this product
        Log.v("delete item", url);
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

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
