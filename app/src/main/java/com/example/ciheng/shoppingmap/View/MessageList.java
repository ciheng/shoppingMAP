package com.example.ciheng.shoppingmap.View;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.ciheng.shoppingmap.Adapter.listAdapter;
import com.example.ciheng.shoppingmap.Adapter.productAdapter;
import com.example.ciheng.shoppingmap.Data.message;
import com.example.ciheng.shoppingmap.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class MessageList extends AppCompatActivity {

    private static final String TAG = "MESSAGE_LIST";
    RecyclerView recyclerview;
    private listAdapter adapter;
    private int mUserId;
    private int senderId;
    private message message;
    private int productId;
    private SwipeRefreshLayout swipeRefresh;
    private String senderName;
    private String download;
    private List<message> msgList = new ArrayList<message>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_list_buyer);
        Intent intent = getIntent();
        mUserId=intent.getIntExtra("user_id",-1);

        getMsg();
        recyclerview =(RecyclerView)findViewById(R.id.recyclerview);
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        adapter = new listAdapter(msgList);
        recyclerview.setAdapter(adapter);


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


        adapter.setOnItemClickLitener(new adapter.OnItemClickListerner() {         //why????????????????
            @Override
            public void onItemClick(View view, int position) {

                Intent intent = new Intent(MessageList.this, SendMessage.class);
                Bundle extras = new Bundle();
                extras.putInt("seller_id", senderId);
                extras.putInt("user_id", mUserId);
                extras.putInt("product_id", productId);

                intent.putExtras(extras);
                startActivity(intent);
            }

        });}



    private void getMsg() {
        RequestQueue data = Volley.newRequestQueue(this);

        String url = "http://api.a17-sd207.studev.groept.be/find_message/" + mUserId;
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {

                            for (int i = 0; i < response.length(); i++) {
                                Log.v(TAG,"getMsg response length "+ response.length());
                                JSONObject Event = response.getJSONObject(i);
                                senderId=Event.getInt("sender");
                                String senderName=Event.getString("username");
                                productId=Event.getInt("productId");
                                getProductPhoto(productId);
                                String msg=Event.getString("message");
                                int msgID=Event.getInt("id_messages");


                                message = new message();
                                message.setSenderID(senderId);
                                message.setSenderName(senderName);
                                message.setMessage(msg);
                                message.setProductID(productId);
                                message.setProductUrl(download);
                                message.setMsgID(msgID);
                                message.setReceiverID(mUserId);

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


