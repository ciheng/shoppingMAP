package com.example.ciheng.shoppingmap.View;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
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
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.ciheng.shoppingmap.Adapter.urlAdapter;
import com.example.ciheng.shoppingmap.Data.uploadPic;
import com.example.ciheng.shoppingmap.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import org.json.JSONArray;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class addProductActivity extends AppCompatActivity {
    private static final String TAG = "addProductActivity";
    //userData mUserData = (userData)getApplication();
    private EditText item_name;
    private EditText price;
    private EditText introduction;
    private EditText address;
    private ImageView mImageView;
    private Uri mImageUri;
    private ProgressBar mProgressBar;
    //private String mFilePath;
    //private String mFile;
    public static final int CAMERA_RESULT = 1;
    public static final int SELECT_PIC = 2;
    private int mUserId;
    private StorageReference mStorageRef;
    private StorageReference mStorageRef_thumbnails;
    private DatabaseReference mDatabaseRef;
    private Bitmap mBitmap;
    private Bitmap mBitmapThunmbnail;
    private StorageTask mUploadTask;
    private uploadPic mUploadPic;
    private urlAdapter mUrlAdapter = new urlAdapter();
    //private ThumbnailUtils mThumbnailUtils = new ThumbnailUtils();

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
        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
        price = findViewById(R.id.price);
        address=findViewById(R.id.address);
        introduction = findViewById(R.id.introduction);
        mStorageRef = FirebaseStorage.getInstance(mUrlAdapter.getFirebaseURL()).getReference("uploads");//zip it into a folder called uploads
        mStorageRef_thumbnails = FirebaseStorage.getInstance(mUrlAdapter.getFirebaseURL()).getReference("thumbnails");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("uploads");
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
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                //Log.v(TAG, "CAMERA_RESULT执行完毕!");
                break;
            case SELECT_PIC:
                //Log.v(TAG, "select picture开始跑了!!");
                //Bitmap mBitmap = null;
                try {
                    mImageUri = data.getData();
                    mBitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(mImageUri));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                break;
        }
        mImageView.setImageBitmap(mBitmap);
    }


    public void sell(View view) {


        if (mUploadTask != null && mUploadTask.isInProgress()) {//to prevent multiple clicks
            Toast.makeText(addProductActivity.this, "Upload in progress...", Toast.LENGTH_SHORT);
        } else {
            uploadPicture();
            //String url = mUrlAdapter.genAddProductUrl(productName, productPrice, description, mUserId);
        }
    }

    private String getFileExtension(Uri uri) { //get the file extention from the image
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadPicture() {

        final RequestQueue requestQueue = Volley.newRequestQueue(this);

        if (mImageUri != null) {
            final String pictureName = "u" + mUserId + "_" + System.currentTimeMillis();

            mUploadPic = new uploadPic(pictureName);

            uploadToDatabase();
            final StorageReference fileReference = mStorageRef.child(pictureName + "." + getFileExtension(mImageUri));
            //Log.v(TAG,"url test1: "+ getPicRefUrl(fileReference));
            mUploadTask = fileReference.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mProgressBar.setProgress(0);
                        }
                    }, 500);
                    Toast.makeText(addProductActivity.this, "mUploadPic succesfull", Toast.LENGTH_LONG).show();
                    fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                           // mUploadPic = new uploadPic(pictureName, uri.toString());
                            mUploadPic.setImageUrl(uri.toString());
                            Log.v(TAG, "mUploadPic pic reference url:" + mUploadPic.getImageUrl());
                            String url = mUrlAdapter.genAddPicture(mUploadPic.getImageUrl(), mUploadPic.getName());
                            String message = "upload picture url to database: " + url;
                            Log.v(TAG, message);
                            JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                                    new Response.Listener<JSONArray>() {

                                        @Override
                                        public void onResponse(JSONArray response) {
                                            Log.v(TAG, "product picture successfully uploaded to database");
                                        }
                                    }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    error.printStackTrace();
                                }
                            });

                            requestQueue.add(request);
                            uploadThumbnail(pictureName);
                        }
                    });
                    //mUploadPic = new uploadPic(pictureName, fileReference.toString());

                    //String uploadId = mDatabaseRef.push().getKey();
                    //mDatabaseRef.child(uploadId).setValue(mUploadPic);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(addProductActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                    mProgressBar.setProgress((int) progress);
                }
            });
        } else {
            Toast.makeText(this, "no picture uploaded!", Toast.LENGTH_SHORT).show();
        }
    }

    private void uploadThumbnail(String pictureName) {
        final RequestQueue requestQueue = Volley.newRequestQueue(this);

        mBitmapThunmbnail = ThumbnailUtils.extractThumbnail(mBitmap, 100, 100);
        Uri thumbnail = getCompressedImageUri(mBitmapThunmbnail);
        final StorageReference thumbnailReference = mStorageRef_thumbnails.child(pictureName + "." + getFileExtension(thumbnail));
        StorageTask task = thumbnailReference.putFile(thumbnail).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                thumbnailReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        mUploadPic.setThumbnailUrl(uri.toString());
                        String url = mUrlAdapter.genAddThumbnail(mUploadPic.getThumbnailUrl(), mUploadPic.getName());
                        String message = "upload picture thumbnail url to database: " + url;
                        Log.v(TAG, message);
                        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                                new Response.Listener<JSONArray>() {

                                    @Override
                                    public void onResponse(JSONArray response) {
                                        Log.v(TAG, "product picture successfully uploaded to database");
                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                error.printStackTrace();
                            }
                        });

                        requestQueue.add(request);
                    }
                });
            }
        });
        //mBitmapThunmbnail= ThumbnailUtils.extractThumbnail(mBitmap,64,64);
        //mBitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(mImageUri));
    }

    public Uri getCompressedImageUri(Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String title="thumbnail_"+mUploadPic.getName();
        String path = MediaStore.Images.Media.insertImage(this.getContentResolver(), inImage, title, null);
        return Uri.parse(path);
    }
    private void uploadToDatabase(){
        //uploadAddress();

        String productAddress = address.getText().toString();
        String productName = item_name.getText().toString();
        String productPrice = price.getText().toString();
        String description = introduction.getText().toString();
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String geoAddr= mUrlAdapter.addressCoder(this,productAddress);
        String url = mUrlAdapter.genAddProductUrl(productName, productPrice, description, mUserId,mUploadPic.getName(),geoAddr);
        String message = "mUploadPic url to database: " + url;
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
        requestQueue.add(request);
        Toast.makeText(addProductActivity.this, "successfully posted an item", Toast.LENGTH_SHORT).show();
        addProductActivity.this.finish();

    }


    private void goBack(){
        Intent intent = new Intent(addProductActivity.this, navigationActivity.class);
        intent.putExtra("user_id", mUserId);
        startActivity(intent);
    }
}