package com.example.ciheng.shoppingmap.View;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.ciheng.shoppingmap.R;

public class BuyerActivity extends AppCompatActivity {
    private static final String TAG = "BuyerActivity";
    public static final String ACTIVITY_MAP = "ACTIVITY_MAPS";
    private Button mButtonMap;
    private Button mButtonList;
    private int mUserId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyer);
        Intent intent = getIntent();
        mUserId=intent.getIntExtra("user_id",-1);
        mButtonMap=(Button) findViewById(R.id.discoverButton);
        mButtonList=(Button) findViewById(R.id.wish_listButton);

    }


    public void startMapsActivity(View view) {
        Intent intent = new Intent(this, MapsActivity.class);
        intent.putExtra("user_id",mUserId);
        startActivity(intent);
    }
    public void startListActivity(View view){

        Intent intent = new Intent(this, wishList.class);
        intent.putExtra("user_id",mUserId);
        startActivity(intent);
    }

    public void startMessageActivity(View view){
        Intent intent = new Intent(this, MessageList.class);
        intent.putExtra("user_id",mUserId);
        startActivity(intent);
    }

}