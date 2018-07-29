package com.example.ciheng.shoppingmap.View;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.example.ciheng.shoppingmap.Data.userData;
import com.example.ciheng.shoppingmap.R;

public class ProductDetail extends AppCompatActivity {
    int mUserId;
    userData mUserData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent intent = getIntent();
        mUserId = intent.getIntExtra("user_id", -1);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.mail);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProductDetail.this, SendMessage.class);//还需要intent seller，productName，productID
                intent.putExtra("user_id", mUserData.getUserId());
                startActivity(intent);
            }
        });
    }

}
