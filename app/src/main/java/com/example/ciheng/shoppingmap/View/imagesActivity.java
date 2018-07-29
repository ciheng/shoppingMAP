package com.example.ciheng.shoppingmap.View;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.ciheng.shoppingmap.Adapter.imageAdapter;
import com.example.ciheng.shoppingmap.Adapter.urlAdapter;
import com.example.ciheng.shoppingmap.Data.product;
import com.example.ciheng.shoppingmap.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class imagesActivity extends AppCompatActivity {
    private static final String TAG="imagesActivity";
    //private RecyclerView mRecyclerView;

    private imageAdapter mAdapter;
    private ImageView image;
    private urlAdapter mUrlAdapter = new urlAdapter();
    private int mUserId;
    //private List<uploadPic> mUploadPics;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_images);
        /*mRecyclerView =findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));*/

        Intent intent = getIntent();
        mUserId = intent.getIntExtra("user_id", -1);
        //mUploadPics = new ArrayList<>();
        image = (ImageView) findViewById(R.id.image);
        loadPicture();
    }

    public void loadPicture() {
        RequestQueue data = Volley.newRequestQueue(this);
        String url=mUrlAdapter.getImage_test();
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    JSONObject Event = response.getJSONObject(0);


                    String name = Event.getString("name");
                    Log.v(TAG,name);
                    String description = Event.getString("description");
                    String price = Event.getString("price");
                    String download = Event.getString("download");
                    String thumbnail=Event.getString("thumbnail");
                    Log.v(TAG,download);
                    int id_product = Event.getInt("id_product");
                    product P = new product(name,mUserId,price,description,id_product);
                    P.setDownloadUrl(download);
                    Picasso.get().load(P.getDownloadUrl()).into(image);
                    String message = "image url:"+P.getDownloadUrl();
                    Log.v(TAG,message);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
Log.v(TAG,"error");
            }
        });

        data.add(request);
    }
}
