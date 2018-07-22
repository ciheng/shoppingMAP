package com.example.ciheng.shoppingmap.View;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.ciheng.shoppingmap.Data.userData;
import com.example.ciheng.shoppingmap.R;

import org.json.JSONArray;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class addProductActivity extends AppCompatActivity {
    private static final String TAG = "addProductActivity";
    userData mUserData = (userData)getApplication();
    private ImageView imageView;
    private Uri uri;

    private String mFilePath;
    private String mFile;
    public static final int CAMERA_RESULT = 1;
    public static final int SELECT_PIC = 2;
    private final String serverURL = "http://api.a17-sd207.studev.groept.be";
    private String itemName;
    private String price;
    private String description;
    private int mUserId;
    //private StorageReference mStorage;
    @Override


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        Intent intent = getIntent();
        mUserId=intent.getIntExtra("user_id",-1);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        imageView=(ImageView)findViewById(R.id.picture);
        EditText item_name=findViewById(R.id.item_name);
        EditText price=findViewById(R.id.price);
        EditText introduction=findViewById(R.id.introduction);
        itemName=item_name.getText().toString();
        this.price =price.getText().toString();
        description =introduction.getText().toString();
        //mStorage= FirebaseStorage.getInstance().getReference();
    }


    public void takePhoto(View view)
    {

        File outputImage=new File(getExternalCacheDir(), "output_image.jpg");
        try{
            if(outputImage.exists())
            {
                outputImage.delete();
            }
            outputImage.createNewFile();
        }catch(IOException e)
        {
            e.printStackTrace();
        }
        if (Build.VERSION.SDK_INT >= 24) {

            uri = FileProvider.getUriForFile(addProductActivity.this, "com.example.ciheng.shoppingmap.fileprovider", outputImage);
        }else {
            uri = Uri.fromFile(outputImage); //7.0以下
        }
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        startActivityForResult(intent, CAMERA_RESULT);

    }


    public void fromGallery(View view)
    {
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                "image/*");
        startActivityForResult(intent, SELECT_PIC);


    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch(requestCode) {
            case CAMERA_RESULT:
                try {
                    Bitmap photo=BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
                    imageView.setImageBitmap(photo);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                Log.v(TAG,"CAMERA_RESULT执行完毕!");
                break;
            case SELECT_PIC:
                Log.v(TAG,"select picture开始跑了!!");
                Bitmap mBitmap = null;
                try {
                    mBitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(data.getData()));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                imageView.setImageBitmap(mBitmap);
                break;
        }
    }



    public void sell(View view)
    {
        RequestQueue data1 = Volley.newRequestQueue(this);

        String url=serverURL+"/add_product/"+itemName+"/"+ price +"/"+ description +"/"+ mUserData.getUserId();
        JsonArrayRequest request=new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Toast.makeText(addProductActivity.this, "successfully posted an item", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(addProductActivity.this, navigationActivity.class);
                        startActivity(intent);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        data1.add(request);

    }
}
