package com.example.ciheng.shoppingmap.View;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.ciheng.shoppingmap.Data.uploadPic;
import com.example.ciheng.shoppingmap.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONArray;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class addProductActivity extends AppCompatActivity {
    private static final String TAG = "addProductActivity";
    //userData mUserData = (userData)getApplication();
    private EditText item_name;
    private EditText price;
    private EditText introduction;
    private ImageView mImageView;
    private Uri mImageUri;
    private String mFilePath;
    private String mFile;
    public static final int CAMERA_RESULT = 1;
    public static final int SELECT_PIC = 2;
    private final String serverURL = "http://api.a17-sd207.studev.groept.be";
    private int mUserId;
    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;
    private Bitmap mBitmap;

    @Override


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        Intent intent = getIntent();
        mUserId = intent.getIntExtra("user_id", -1);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mImageView = (ImageView) findViewById(R.id.picture);
        item_name = findViewById(R.id.item_name);
        price = findViewById(R.id.price);
        introduction = findViewById(R.id.introduction);
        mStorageRef = FirebaseStorage.getInstance("gs://shoppingmap-209612").getReference("uploads");//zip it into a folder called uploads
        mDatabaseRef = FirebaseDatabase.getInstance("gs://shoppingmap-209612").getReference("uploads");
    }


    public void takePhoto(View view) {

        File outputImage = new File(getExternalCacheDir(), "output_image.jpg");
        try {
            if (outputImage.exists()) {
                outputImage.delete();
            }
            outputImage.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (Build.VERSION.SDK_INT >= 24) {

            mImageUri = FileProvider.getUriForFile(addProductActivity.this, "com.example.ciheng.shoppingmap.fileprovider", outputImage);
        } else {
            mImageUri = Uri.fromFile(outputImage); //7.0以下
        }
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
        startActivityForResult(intent, CAMERA_RESULT);

    }


    public void fromGallery(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                "image/*");
        startActivityForResult(intent, SELECT_PIC);


    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case CAMERA_RESULT:
                try {
                    //Bitmap photo
                    mBitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(mImageUri));
                    mImageView.setImageBitmap(mBitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                //Log.v(TAG, "CAMERA_RESULT执行完毕!");
                break;
            case SELECT_PIC:
                //Log.v(TAG, "select picture开始跑了!!");
                //Bitmap mBitmap = null;
                try {
                    mImageUri=data.getData();
                    mBitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(mImageUri));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                mImageView.setImageBitmap(mBitmap);
                break;
        }
    }


    public void sell(View view) {
        RequestQueue data1 = Volley.newRequestQueue(this);

        String productName = item_name.getText().toString();
        String productPrice = price.getText().toString();
        String description = introduction.getText().toString();
        uploadPicture();
        String url = serverURL + "/add_product/" + productName + "/" + productPrice + "/" + description + "/" + mUserId;
        String message = "product upload url: " + url;
        Log.v(TAG, message);
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        Log.v(TAG, "product successfully uploaded");
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        data1.add(request);
        Toast.makeText(addProductActivity.this, "successfully posted an item", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(addProductActivity.this, navigationActivity.class);
        intent.putExtra("user_id",mUserId);
        startActivity(intent);
    }

    private String getFileExtension(Uri uri) { //get the file extention from the image
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadPicture() {
        if (mImageUri != null) {
            StorageReference fileReference = mStorageRef.child(System.currentTimeMillis() + "." + getFileExtension(mImageUri));
            fileReference.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                        }
                    }, 5000);
                    Toast.makeText(addProductActivity.this,"upload succesfull",Toast.LENGTH_LONG).show();
                    String pictureName = "u"+mUserId+"_"+System.currentTimeMillis();
                    uploadPic upload=new uploadPic(pictureName,taskSnapshot.getStorage().getDownloadUrl().toString());
                    String uploadId=mDatabaseRef.push().getKey();
                    mDatabaseRef.child(uploadId).setValue(upload);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(addProductActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    //    double progress = (100 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                }
            });
        } else {
            Toast.makeText(this, "no picture uploaded!", Toast.LENGTH_SHORT).show();
        }
    }
        /*ByteArrayOutputStream baos = new ByteArrayOutputStream();
        mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = mountainsRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                // ...
            }
        });
    }*/
}