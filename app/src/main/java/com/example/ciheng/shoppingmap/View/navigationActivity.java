package com.example.ciheng.shoppingmap.View;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.ciheng.shoppingmap.R;

public class navigationActivity extends AppCompatActivity {
    private static final String TAG = "NavigationActivity";
    public static final String ACTIVITY_SELLER = "ACTIVITY_SELLER";
    public static final String ACTIVITY_BUYER = "ACTIVITY_BUYER";
    private Button mButtonSeller;
    private Button mButtonBuyer;
    private Button mButtonMessage;
    private int mUserId;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //remove title bar
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_navigation);
        Intent intent = getIntent();
        mUserId=intent.getIntExtra("user_id",-1);
        mButtonSeller=(Button) findViewById(R.id.sellerButton);
        mButtonBuyer=(Button) findViewById(R.id.buyerButton);

        mButtonSeller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            startSellerActivity();
            }
        });
        mButtonBuyer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startBuyerActivity();
            }
        });
    }

    private void startSellerActivity() {
        Intent intent = new Intent(this, SellerActivity.class);
        intent.putExtra("user_id",mUserId);
        startActivity(intent);
    }
    private void startBuyerActivity() {
        Intent intent = new Intent(this, BuyerActivity.class);
        intent.putExtra("user_id",mUserId);
        startActivity(intent);
    }
}
