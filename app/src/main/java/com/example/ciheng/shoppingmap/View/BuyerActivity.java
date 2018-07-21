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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyer);
        mButtonMap=(Button) findViewById(R.id.discoverButton);

        mButtonMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startMapsActivity();
            }
        });
    }


    private void startMapsActivity() {
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }}