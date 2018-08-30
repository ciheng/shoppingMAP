package com.example.ciheng.shoppingmap.View;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
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
    private static final String TAG = "sendMsg";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        productName = extras.getString("product_name", "null");
        mUserId = extras.getInt("user_id", -1);
        Log.v("TAG","muserId"+mUserId);
        sellerID = extras.getInt("seller_id", -1);

        Log.v("TAG","sellerId"+sellerID);
        productID = extras.getInt("product_id", -1);

        Log.v("TAG","productIDd"+productID);
        setContentView(R.layout.activity_send_message);
        Seller = (TextView) findViewById(R.id.Seller);
        product = (TextView) findViewById(R.id.product);
        message = (EditText) findViewById(R.id.message);
        getProduct();

    }


    public void getProduct() {
        RequestQueue data = Volley.newRequestQueue(this);
        String url = "http://api.a17-sd207.studev.groept.be/getProduct/" + productID;
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {

                                JSONObject Event = response.getJSONObject(0);
                                productName = Event.getString("name");
                                product.setText(productName.replaceAll("%20"," "));
                                sellerName = Event.getString("username");
                                Seller.setText(sellerName);
                                String msg = "product name is" + productName;
                                Log.v(TAG, msg);

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


    public void send(View view) {

        send_message = message.getText().toString().replaceAll(" ", "%20").trim();
        RequestQueue data = Volley.newRequestQueue(this);
        String url = "http://api.a17-sd207.studev.groept.be/message/" + mUserId + "/" + sellerID + "/" + send_message + "/" + productID;
        String msg = "upload message url " + url;
        Log.v(TAG, msg);
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
