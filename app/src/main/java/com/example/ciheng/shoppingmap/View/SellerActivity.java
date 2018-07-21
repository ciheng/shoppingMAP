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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller);
        mButtonAdd=(Button) findViewById(R.id.sellButton);
        mButtonProductList=(Button) findViewById(R.id.selling_listButton);
        mButtonMessage=(Button) findViewById(R.id.seller_messageButton);

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
        mButtonMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
    private void startAddProductActivity(){
        Intent intent = new Intent(this, addProductActivity.class);
        //intent.putExtra(ACTIVITY_SELLER,带上xxx);
        startActivity(intent);
    }
    private void startProductList(){
        Intent intent = new Intent(this, productList.class);
        //intent.putExtra(ACTIVITY_SELLER,带上xxx);
        startActivity(intent);
    }

}
