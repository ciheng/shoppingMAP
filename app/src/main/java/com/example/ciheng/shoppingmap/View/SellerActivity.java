package com.example.ciheng.shoppingmap.View;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.ciheng.shoppingmap.R;
public class SellerActivity extends AppCompatActivity {
    public static final String ACTIVYTY_ADD_PRODUCT = "ADD_PRODUCT_ACTIVITY";
    private static final String TAG = "SellerActivity";
    private Button mButtonAdd;
    private Button mButtonProductList;
    private Button mButtonMessage;
    private int mUserId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller);
        Intent intent = getIntent();
        mUserId=intent.getIntExtra("user_id",-1);
        mButtonAdd=(Button) findViewById(R.id.sellButton);
        mButtonProductList=(Button) findViewById(R.id.selling_listButton);

        mButtonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startAddProductActivity();
            }
        });
        mButtonProductList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startProductList();
            }
        });

    }
    private void startAddProductActivity(){
        Intent intent = new Intent(this, addProductActivity.class);
        intent.putExtra("user_id",mUserId);
        startActivity(intent);
    }
    private void startProductList(){
        Intent intent = new Intent(this, productList.class);
        intent.putExtra("user_id",mUserId);
        startActivity(intent);
    }

}
