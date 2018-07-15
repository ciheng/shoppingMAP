package com.example.ciheng.shoppingmap.View;

import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.ciheng.shoppingmap.MyApplication;
import com.example.ciheng.shoppingmap.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static android.app.PendingIntent.getActivity;


public class sellingList extends AppCompatActivity {

    MyApplication user= (MyApplication)getApplication();

    private ListView mListView;
     ArrayList item_name= new ArrayList();
     ArrayList photo=new ArrayList();
     ArrayList item_intro=new ArrayList();
     ArrayList item_price=new ArrayList();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selling_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mListView = (ListView) findViewById(R.id.item_list);

        mListView.setAdapter(new MyBaseAdapter());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    RequestQueue data = Volley.newRequestQueue(this);

    String url="http://api.a17-sd606.studev.groept.be/selling_item";
    JsonArrayRequest request=new JsonArrayRequest(Request.Method.GET, url, null,
            new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    try {

                        for (int i=0;i< response.length();i++)
                        {
                            JSONObject Event =response.getJSONObject(i);
                            String UN=Event.getString("username");
                            if(UN.equals(user.getName()))
                            {
                                int PT=Event.getInt("item_photo");
                                String IN=Event.getString("item_name");
                                String IT=Event.getString("introduction");
                                String PZ=Event.getString("price");
                                item_name.add(IN);
                                item_intro.add(IT);
                                item_price.add(PZ);
                                photo.add(PT);
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





    private class MyBaseAdapter implements ListAdapter {
        @Override
        public void registerDataSetObserver(DataSetObserver dataSetObserver) {

        }

        @Override
        public void unregisterDataSetObserver(DataSetObserver dataSetObserver) {

        }

        @Override

        public int getCount() {
            return item_name.size();

        }



        @Override

        public Object getItem(int position) {
            return item_name.get(position);

        }



        @Override

        public long getItemId(int position) {
            return position;

        }

        @Override
        public boolean hasStableIds() {
            return false;
        }


        @Override

        public View getView(int position, View convertView, ViewGroup parent) {

            View view=View.inflate(sellingList.this,R.layout.content_lv_list,null);

            TextView mTextView1=(TextView) view.findViewById(R.id.item_list);
            ImageView imageView=(ImageView)view.findViewById(R.id.item_photo);
            TextView mTextView2=(TextView) view.findViewById(R.id.item_intro);
            TextView mTextView3=(TextView) view.findViewById(R.id.item_price);


            mTextView1.setText((CharSequence) item_name.get(position));
            imageView.setBackgroundResource((Integer) photo.get(position));
            mTextView2.setText((CharSequence) item_intro.get(position));
            mTextView3.setText((CharSequence) item_price.get(position));

            return view;

        }

        @Override
        public int getItemViewType(int i) {
            return 0;
        }

        @Override
        public int getViewTypeCount() {
            return 0;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public boolean areAllItemsEnabled() {
            return false;
        }

        @Override
        public boolean isEnabled(int i) {
            return false;
        }
    }
}
