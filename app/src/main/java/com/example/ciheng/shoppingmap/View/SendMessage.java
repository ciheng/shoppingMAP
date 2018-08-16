package com.example.ciheng.shoppingmap.View;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.ciheng.shoppingmap.Data.product;
import com.example.ciheng.shoppingmap.Data.userData;
import com.example.ciheng.shoppingmap.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SendMessage extends AppCompatActivity {
    private TextView Seller;
    private TextView product;
    private EditText message;
    private int sellerID;
    private String sellerName;
    private String productName;
    private String send_message;
    private Button sell;
    private int mUserId;
    private int productID;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        productName = extras.getString("product_name",null );
        mUserId = extras.getInt("user_id", -1);
        sellerID=extras.getInt("seller_id",-1);
        productID=extras.getInt("product_id",-1);

        setContentView(R.layout.activity_send_message);
        Seller=(TextView) findViewById(R.id.Seller);
        product=(TextView) findViewById(R.id.product);
        message=(EditText) findViewById(R.id.message);
        getSeller();
        Seller.setText(sellerName);
        product.setText(productName);
    }

    private void getSeller() {
        RequestQueue data = Volley.newRequestQueue(this);
        String url = "http://api.a17-sd207.studev.groept.be/findUser_byID/" + sellerID;
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject Event = response.getJSONObject(i);
                                sellerName = Event.getString("username");
                                sellerName = sellerName.replaceAll("%20", " ");
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


    private void send(View view)
    {

        send_message=message.getText().toString().trim();
        RequestQueue data = Volley.newRequestQueue(this);
        String url="http://api.a17-sd207.studev.groept.be/message"+"/"+mUserId+"/"+sellerName+"/"+send_message+"/"+productID;
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
        finish();
    }
}
