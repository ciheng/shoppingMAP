package com.example.ciheng.shoppingmap.View;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.ciheng.shoppingmap.R;

public class navigation extends AppCompatActivity {

    public static final String ACTIVITY_SELLER = "ACTIVITY_SELLER";
    public static final String ACTIVITY_BUYER = "ACTIVITY_BUYER";
    private Button mButtonSeller;
    private Button mButtonBuyer;
    private Button mButtonMessage;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //remove title bar
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_navigation);
        mButtonSeller=(Button) findViewById(R.id.sellerButton);
        mButtonBuyer=(Button) findViewById(R.id.buyerButton);
        mButtonMessage=(Button) findViewById(R.id.seller_messageButton);

    }


}
