package com.example.ciheng.shoppingmap.View;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.ciheng.shoppingmap.R;

public class MessageList_byProduct extends AppCompatActivity {

    //最好用ListView代替cardView，做Gmail的样子

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_list_by_product);
    }
}
