package com.example.ciheng.shoppingmap.View;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.ciheng.shoppingmap.R;

public class navigation extends AppCompatActivity {
    public static final String ACTIVITY_MAP = "ACTIVITY_MAPS";
    private Button mButtonMap;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //remove title bar
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_navigation);
        mButtonMap=(Button) findViewById(R.id.buyerButton);

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
    }
}
