package com.example.ciheng.shoppingmap.View;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
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
import com.example.ciheng.shoppingmap.Data.userData;
import com.example.ciheng.shoppingmap.R;
import com.google.firebase.FirebaseApp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * A login screen that offers login via Username/password.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
@RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
public class LoginActivity extends AppCompatActivity implements LoaderCallbacks<Cursor> {

    private static final String TAG = "LoginActivity";
    private AutoCompleteTextView mUsernameView;
    private EditText mPasswordView;
    private Button mButtonLogin;
    private Button mButtonRegister;
    private boolean checkfor = false;
    private String username_tbc;
    private String password_tbc;
    private String username;
    private String password;
    private boolean flag;
    private userData mUserData;
    private pwdAdapter mPwdAdapter=new pwdAdapter();
    private urlAdapter mUrlAdapter=new urlAdapter();

    @SuppressLint("WrongViewCast")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        FirebaseApp.initializeApp(this);
       // mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        mUsernameView = (AutoCompleteTextView) findViewById(R.id.username);
        mPasswordView = (EditText) findViewById(R.id.password);
        mButtonLogin = (Button) findViewById(R.id.sign_in);
        mButtonRegister = (Button) findViewById(R.id.register);
        mButtonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startRegisterActivity();
            }
        });
    }

    public void startRegisterActivity() {
        Intent intent = new Intent(this, registerActivity.class);
        startActivity(intent);

    }


    public void sign_in(View view) {
        username_tbc = mUsernameView.getText().toString();
        password_tbc = mPwdAdapter.md5(mPasswordView.getText().toString());   //md5 type of password
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

    private void check() {

        RequestQueue data = Volley.newRequestQueue(this);

        //String url = serverURL + "/userLogin";
        String url =mUrlAdapter.genFindUser(username_tbc);
                //serverURL + "/findUser/"+ username_tbc;
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {

                            //for (int i = 0; i < response.length(); i++) {
                            if (response.length()==0) {

                                Log.v(TAG,"user doesn't exist");
                                Toast.makeText(LoginActivity.this, "User doesn't exist! jumping to register page...", Toast.LENGTH_SHORT).show();
                                startRegisterActivity();
                            } else {

                                JSONObject Event = response.getJSONObject(0);
                                String UN = Event.getString("username");
                                String PS = Event.getString("password");
                                int id_user = Event.getInt("id_user");
                                username = UN;
                                password = PS;

                                if (username_tbc.equals(username)) {
                                    if (password_tbc.equals(password)) {
                                        checkfor = true;
                                        mUserData = new userData();
                                        mUserData.setUserName(username);
                                        mUserData.setUserId(id_user);
                                        String message = "username:" + username + ",userId:" + id_user;
                                        Log.v(TAG, message);
                                        Intent intent = new Intent(LoginActivity.this, navigationActivity.class);
                                        intent.putExtra("user_id", mUserData.getUserId());
                                        startActivity(intent);

                                    } else {
                                        Toast.makeText(LoginActivity.this, "Wrong password", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                            // }
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

    /*public static String md5(String text) {                   //security type md5
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("md5");
            byte[] result = digest.digest(text.getBytes());
            StringBuffer sb = new StringBuffer();
            for (byte b : result) {
                int number = b & 0xff;
                String hex = Integer.toHexString(number);
                if (hex.length() == 1) {
                    sb.append("0" + hex);
                } else {
                    sb.append(hex);
                }
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }
    }*/

}

