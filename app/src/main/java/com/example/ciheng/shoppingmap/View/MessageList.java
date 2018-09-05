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
import com.example.ciheng.shoppingmap.Data.message;
import com.example.ciheng.shoppingmap.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class MessageList extends AppCompatActivity {

    private static final String TAG = "MESSAGE_LIST";
    RecyclerView recyclerview;
    private listAdapter adapter;
    private int mUserId;
    private int senderId;
    private int receiverId;
    private int productId;
    private SwipeRefreshLayout swipeRefresh;
    private String download;
    private List<message> msgList = new ArrayList<message>();
    private message msg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_list_buyer);
        Intent intent = getIntent();
        mUserId = intent.getIntExtra("user_id", -1);
        recyclerview = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerview.setLayoutManager(new LinearLayoutManager(this));    //bind the recycler view and the adapter,the data got here would be transfered to the adapter to be build in a format which fits the view
        adapter = new listAdapter(msgList);
        recyclerview.setAdapter(adapter);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                getMsg();

            }
        });
        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);                  //refresh the data when swipe the screen
        swipeRefresh.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
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
                                getMsg();
                                adapter.notifyDataSetChanged();
                                swipeRefresh.setRefreshing(false);
                            }
                        });

                    }
                }).start();
            }
        });


        adapter.setOnItemClickLitener(new listAdapter.OnItemClickListerner() {         //why????????????????
            @Override
            public void onItemClick(View view, int position) {     //if the item is clicked, will go to writing back to the message sender
                message temp = msgList.get(position);

                int sender;
                String senderName;
                if (temp.isSender()) {
                    sender = temp.getReceiverID();
                    senderName=temp.getReceiverName();
                } else {
                    sender = temp.getSenderID();
                    senderName=temp.getSenderName();
                }
                Log.v("sender id is ", sender + "");
                int product = temp.getProductID();
                Intent intent = new Intent(MessageList.this, SendMessage.class);
                Bundle extras = new Bundle();
                extras.putInt("sender_id", sender);
                extras.putInt("user_id", mUserId);
                extras.putInt("product_id", product);
                extras.putString("sender", senderName);
                intent.putExtras(extras);
                startActivity(intent);
            }

        });
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
                                Log.v(TAG, "getMsg response length " + response.length());
                                JSONObject Event = response.getJSONObject(i);
                                senderId = Event.getInt("sender");
                                receiverId = Event.getInt("receiver");
                                productId = Event.getInt("productId");
                                String content = Event.getString("message");
                                int msgID = Event.getInt("id_messages");
                                download = Event.getString("download");
                                int ownerId = Event.getInt("owner");
                                msg = new message();
                                msg.setSenderID(senderId, mUserId);
                                msg.setReceiverID(receiverId, mUserId);
                                Log.v("msg info: rcv", msg.getReceiverID() + " sender" + msg.getSenderID());
                                msg.setMessage(content);
                                msg.setProductID(productId);
                                msg.setProductUrl(download);
                                msg.setMsgID(msgID);
                                msg.setOwnerId(ownerId);
                                if (msg.getOwnerId() == mUserId) {
                                    msg.setIsUserOwner(true);
                                } else {
                                    msg.setIsUserOwner(false);
                                }
                                int flag = 0;
                                for (int count = 0; count < msgList.size(); count++) {       //make sure one message can only have once in the list
                                    if (msgList.get(count).getMsgID() == msgID) {
                                        flag = 1;
                                        break;
                                    } else {
                                        flag = 0;
                                    }
                                }

                                if (flag == 0) {
                                    msgList.add(msg);

                                }

                                Sender(i);
                                Receiver(i);

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

    private void Sender(final int i) {
        RequestQueue data = Volley.newRequestQueue(this);
        String url = "http://api.a17-sd207.studev.groept.be/find_user_byID/";
        String query = url + msgList.get(i).getSenderID();
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, query, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            JSONObject Event = response.getJSONObject(0);
                            String username = Event.getString("username");
                            msgList.get(i).setSenderName(username);
                            Log.v("sender fuction", msg.getSenderID() + "");

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

    private void Receiver(final int i) {

        RequestQueue data = Volley.newRequestQueue(this);

        String url = "http://api.a17-sd207.studev.groept.be/find_user_byID/";
        String query = url + msgList.get(i).getReceiverID();
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, query, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            JSONObject Event = response.getJSONObject(0);
                            String username = Event.getString("username");
                            msgList.get(i).setReceiverName(username);
                            Log.v(TAG + "username", username);
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



