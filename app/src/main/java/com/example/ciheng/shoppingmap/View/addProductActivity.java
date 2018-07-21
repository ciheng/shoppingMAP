package com.example.ciheng.shoppingmap.View;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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
import com.example.ciheng.shoppingmap.Adapter.userAdapter;
import com.example.ciheng.shoppingmap.R;

import org.json.JSONArray;

import java.io.File;
import java.io.FileNotFoundException;

public class addProductActivity extends AppCompatActivity {

    userAdapter user= (userAdapter)getApplication();
    private ImageView imageView;
    private Uri uri;

    private String mFilePath;
    private File mFile;
    public final static int CAMERA_RESULT = 1;
    public static final int SELECT_PIC = 2;

    private String itemName;
    private String prc;
    private String intro;
    @Override


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sellan_item);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        imageView=(ImageView)findViewById(R.id.picture);
        EditText item_name=findViewById(R.id.item_name);
        EditText price=findViewById(R.id.price);
        EditText introduction=findViewById(R.id.introduction);
        itemName=item_name.getText().toString();
        prc=price.getText().toString();
        intro=introduction.getText().toString();
    }


    public void takePhoto(View view)
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.parse("file://" + mFilePath));
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
            Bitmap bitmap = BitmapFactory.decodeFile( mFilePath, null);
            imageView.setImageBitmap(bitmap);
            case SELECT_PIC:
                Bitmap mBitmap = null;
                try {
                    mBitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(data.getData()));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                imageView.setImageBitmap(mBitmap);
        }
    }



    public void sell(View view)
    {
        RequestQueue data1 = Volley.newRequestQueue(this);

        String url="http://api.a17-sd606.studev.groept.be/item_introduction/"+itemName+"/"+prc+"/"+intro+"/"+imageView+"/"+user.getName();
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
