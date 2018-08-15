package com.example.ciheng.shoppingmap.View;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.ciheng.shoppingmap.Adapter.listAdapter;
import com.example.ciheng.shoppingmap.Data.message;
import com.example.ciheng.shoppingmap.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MessageList_Buyer extends AppCompatActivity {

    RecyclerView recyclerview;
    private listAdapter adapter;
    private int mUserId;
    private message message;
    private SwipeRefreshLayout swipeRefresh;
    private String download;
    private String thumbnail;
    private String senderName;
    private List<message> msgList = new ArrayList<message>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_list_buyer);
        Intent intent = getIntent();
        mUserId=intent.getIntExtra("user_id",-1);

        initViews();
        getMsg();

        swipeRefresh=(SwipeRefreshLayout)findViewById(R.id.swipe_refresh);                  //下拉刷新
        swipeRefresh.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            Thread.sleep(1000);
                        }catch (InterruptedException e){
                            e.printStackTrace();
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                Collections.reverse(msgList);        //倒叙显示
                                getMsg();
                                adapter.notifyDataSetChanged();
                                swipeRefresh.setRefreshing(false);
                            }
                        });

                    }
                }).start();
            }
        });

}

    private void initViews() {
        recyclerview =(RecyclerView)findViewById(R.id.recyclerview);
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        adapter = new listAdapter(msgList);
        recyclerview.setAdapter(adapter);
    }

    private void getMsg() {
        RequestQueue data = Volley.newRequestQueue(this);

        String url = "http://api.a17-sd207.studev.groept.be/find_message/" + mUserId;
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {

                            for (int i = 0; i < response.length(); i++) {
                                JSONObject Event = response.getJSONObject(i);
                                int senderId=Event.getInt("sender");
                                getSenderName(senderId);
                                int productId=Event.getInt("productID");
                                getProductPhoto(productId);
                                String msg=Event.getString("message");
                                getProductPhoto(productId);
                                int msgID=Event.getInt("msgID");

                                message = new message();
                                message.setSenderName(senderName);
                                message.setMessage(msg);
                                message.setProductID(productId);
                                message.setProductUrl(download);
                                message.setThumbnailUrl(thumbnail);
                                message.setMsgID(msgID);

                                int flag=0;
                                for(int count=0;count<msgList.size();count++)
                                {
                                    if(msgList.get(count).getMsgID()==msgID)
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
                                    msgList.add(message);

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


    private void getProductPhoto(int productID)

    {
        RequestQueue data = Volley.newRequestQueue(this);
        String url = "http://api.a17-sd207.studev.groept.be/getProduct/" + productID;
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {

                            for (int i = 0; i < response.length(); i++) {
                                JSONObject Event = response.getJSONObject(i);
                                download = Event.getString("download");
                                thumbnail = Event.getString("thumbnail");
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

    private void getSenderName(int senderID)

    {
        RequestQueue data = Volley.newRequestQueue(this);
        String url = "http://api.a17-sd207.studev.groept.be/find_user_byID/" + senderID;
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {

                            for (int i = 0; i < response.length(); i++) {
                                JSONObject Event = response.getJSONObject(i);
                                senderName = Event.getString("username");
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



