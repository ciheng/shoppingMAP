package com.example.ciheng.shoppingmap.View;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.ciheng.shoppingmap.Adapter.urlAdapter;
import com.example.ciheng.shoppingmap.Data.product;
import com.example.ciheng.shoppingmap.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ProductDetail extends AppCompatActivity {
    private static final String TAG = "PRODUCT_DETAIL";
    private int mUserId;
    private int productID;
    private int sellerID;
    private String name;
    private TextView mItemName;
    private TextView mPrice;
    private TextView mSellerName;
    private TextView mAddress;
    private TextView mDescription;
    private product mProduct;
    private ImageView mProductPic;
    private FloatingActionButton fabMail;
    private FloatingActionButton fabLike;
    private urlAdapter mUrlAdapter = new urlAdapter();
    private boolean like;
    //private userData mUserData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        productID = extras.getInt("product_id", -1);
        mUserId = extras.getInt("user_id", -1);
        String message = "product Id " + productID + " user id " + mUserId;
        Log.v(TAG, message);
        mItemName = (TextView) findViewById(R.id.itemName);
        mPrice = (TextView) findViewById(R.id.price);
        mSellerName = (TextView) findViewById(R.id.sellerName);
        mAddress = (TextView) findViewById(R.id.address);
        mDescription = (TextView) findViewById(R.id.description);
        mProductPic = (ImageView) findViewById(R.id.productPic);
        fabMail = (FloatingActionButton) findViewById(R.id.mail);
        fabMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProductDetail.this, SendMessage.class);//还需要intent seller，productName，productID

                intent.putExtra("user_id", mUserId);
                intent.putExtra("seller_id", sellerID);
                intent.putExtra("product_name", name);
                startActivity(intent);
            }
        });
        fabLike = (FloatingActionButton) findViewById(R.id.like);
        fabLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                check_like();
                /*if (like) {
                    unlike();
                } else submit_like();*/
            }
        });
        getItem();


    }


    private void getItem() {
        RequestQueue data = Volley.newRequestQueue(this);
        String url = "http://api.a17-sd207.studev.groept.be/getProduct/" + productID;
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            JSONObject Event = response.getJSONObject(0);
                            name = Event.getString("name");
                            name = name.replaceAll("%20", " ");
                            mItemName.setText(name);
                            String seller = Event.getString("username");
                            mSellerName.setText(seller);
                            sellerID=Event.getInt("owner");
                            String description = Event.getString("description");
                            description = description.replaceAll("%20", " ");
                            mDescription.setText(description);
                            String price = Double.toString(Event.getDouble("price"));
                            mPrice.setText(price);
                            String download = Event.getString("download");
                            mProduct = new product(name, seller, price, description, productID);
                            mProduct.setOwner(Event.getInt("owner"));
                            String message = "download url = " + download;
                            Log.v(TAG, message);
                            mProduct.setDownloadUrl(download);

                            Glide.with(ProductDetail.this).load(mProduct.getDownloadUrl()).centerCrop().into(mProductPic);            //Glide是加载图片的方式


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

    private void submit_like() {
        String url = mUrlAdapter.wishlist_likeURL(mUserId, productID);
        RequestQueue data = Volley.newRequestQueue(this);
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Toast.makeText(ProductDetail.this, "LIKED", Toast.LENGTH_SHORT).show();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }

        });
        data.add(request);
        like = true;
    }


    private boolean check_like() {
        String url = mUrlAdapter.wishlist_checklikeURL(mUserId, productID);
        RequestQueue data = Volley.newRequestQueue(this);
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        if (response.length() != 0) {
                            like = true;
                            unlike();
                        } else {
                            like = false;
                            submit_like();
                        }
                        Log.v(TAG, "response length = " + response.length());
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }

        });
        data.add(request);
        return like;
    }

    private void unlike() {
        String url = mUrlAdapter.wishlist_unlikeURL(mUserId, productID);
        RequestQueue data = Volley.newRequestQueue(this);
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        Toast.makeText(ProductDetail.this, "UNLIKED", Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }

        });
        data.add(request);
        like = false;
    }
}
