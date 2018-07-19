package com.example.ciheng.shoppingmap.View;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ciheng.shoppingmap.MyApplication;
import com.example.ciheng.shoppingmap.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
@RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
public class LoginActivity extends AppCompatActivity implements LoaderCallbacks<Cursor> {

    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private Button loginButton;
    boolean checkfor=false;
    private String email1;
    private String password1;
    private String username;
    private String password;
    private boolean flag;

    MyApplication user= (MyApplication)getApplication();

    @SuppressLint("WrongViewCast")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        mPasswordView = (EditText) findViewById(R.id.password);
    }
    public void register(View view)
    {
        Intent intent = new Intent(LoginActivity.this,register.class);
        startActivity(intent);

    }




    public void sign_in(View view) {
        email1 = mEmailView.getText().toString();
        password1 =md5(mPasswordView.getText().toString());   //md5 type of password
        check();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    private void check()
    {

        RequestQueue data = Volley.newRequestQueue(this);

        String url="http://api.a17-sd606.studev.groept.be/selectuser";
        JsonArrayRequest request=new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {

                            for (int i=0;i< response.length();i++)
                            {
                                JSONObject Event =response.getJSONObject(i);
                                String UN=Event.getString("username");
                                String PS=Event.getString("password");
                                username=UN;
                                password=PS;
                                if(email1.equals(username))
                                {
                                    if(password1.equals(password))
                                    {
                                        checkfor=true;
                                        user.setName(username);
                                        Intent intent = new Intent(LoginActivity.this,navigation.class);
                                        startActivity(intent);

                                    }else{
                                        Toast.makeText(LoginActivity.this, "wrong password", Toast.LENGTH_SHORT).show();}
                                }else{Toast.makeText(LoginActivity.this, "you need to register first", Toast.LENGTH_SHORT).show();}
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
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

    public static String md5(String text) {                   //security type md5
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("md5");
            byte[] result = digest.digest(text.getBytes());
            StringBuffer sb = new StringBuffer();
            for (byte b : result){
                int number = b & 0xff;
                String hex = Integer.toHexString(number);
                if (hex.length() == 1){
                    sb.append("0"+hex);
                }else {
                    sb.append(hex);
                }
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }
    }

}

