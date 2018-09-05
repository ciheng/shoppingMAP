package com.example.ciheng.shoppingmap.View;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.ciheng.shoppingmap.Adapter.pwdAdapter;
import com.example.ciheng.shoppingmap.Adapter.urlAdapter;
import com.example.ciheng.shoppingmap.R;

import org.json.JSONArray;

public class registerActivity extends AppCompatActivity {
    private static final String TAG = "registerActivity";
    private EditText email;
    private EditText password;
    private EditText username;
    private EditText address;

    private String getEmail;
    private String getPassword;
    private String getUsername;
    private String getAddress;
    //private final String serverURL = "http://api.a17-sd207.studev.groept.be";
    boolean flag = false;
    private pwdAdapter mPwdAdapter=new pwdAdapter();
    private urlAdapter mUrlAdapter=new urlAdapter();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        username = (EditText) findViewById(R.id.user_name);
        address = (EditText) findViewById(R.id.address);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

    }

    public void register(View view) {

        getEmail = email.getText().toString().trim();
        getPassword = password.getText().toString().trim();
        getUsername = username.getText().toString().trim();
        getAddress = address.getText().toString().trim();
        Log.v(TAG,getPassword);
        if (TextUtils.isEmpty(getEmail)) {
            Toast.makeText(registerActivity.this, "please input email", Toast.LENGTH_SHORT).show();
            return;
        } else if (TextUtils.isEmpty(getPassword)) {
            Toast.makeText(registerActivity.this, "please input password", Toast.LENGTH_SHORT).show();
            return;
        } else if (getPassword.length()<5) {
            Toast.makeText(registerActivity.this, "password should be at least 6 characters", Toast.LENGTH_LONG).show();
            return;
        }else if (TextUtils.isEmpty(getUsername)) {
            Toast.makeText(registerActivity.this, "please input username", Toast.LENGTH_SHORT).show();
            return;
        } else if (TextUtils.isEmpty(getAddress)) {
            Toast.makeText(registerActivity.this, "please input address", Toast.LENGTH_SHORT).show();
            return;
        } else {

            getuser();
            if (flag == false) {
                adduser();
                Toast.makeText(registerActivity.this, "successfully registered", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(registerActivity.this, LoginActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(registerActivity.this, "this account had already registered", Toast.LENGTH_SHORT).show();
                return;
            }
        }
    }

    private void adduser() {
        RequestQueue data1 = Volley.newRequestQueue(this);
        String email_tba = getEmail;
        String message="getPassword="+getPassword;
        Log.v(TAG,message);
        String pwd_tba = mPwdAdapter.md5(getPassword);    //use md5 to secure password
        String username_tba = getUsername;
        String addr_tba = getAddress;
        String url = mUrlAdapter.genRegister(email_tba,pwd_tba,username_tba,addr_tba);
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        //test.setText("successfully registed");
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        data1.add(request);
    }


    private void getuser() {
        RequestQueue data = Volley.newRequestQueue(this);

        String url = mUrlAdapter.genFindUser(getEmail);
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        if (response.length() != 0) {
                            flag = true;
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        data.add(request);
    }

}
