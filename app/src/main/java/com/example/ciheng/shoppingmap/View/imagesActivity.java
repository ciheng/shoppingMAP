package com.example.ciheng.shoppingmap.View;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.ciheng.shoppingmap.Adapter.imageAdapter;
import com.example.ciheng.shoppingmap.Data.uploadPic;
import com.example.ciheng.shoppingmap.R;

import java.util.ArrayList;
import java.util.List;

public class imagesActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private imageAdapter mAdapter;

    private List<uploadPic> mUploadPics;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_images);
        mRecyclerView =findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mUploadPics = new ArrayList<>();

    }
}
