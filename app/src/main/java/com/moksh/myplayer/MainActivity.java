package com.moksh.myplayer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toolbar;

import com.moksh.myplayer.VideoFiles.ModelClassVideo;
import com.moksh.myplayer.VideoFiles.VideoActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity{
    LinearLayout mainPageToolbar;
    RecyclerView folderSection;
    static ArrayList<String> folderNameList = new ArrayList<>();
    SwipeRefreshLayout folder_refresher;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Section for toolbar
        View toolbar = getLayoutInflater().inflate(R.layout.toolbar,null);
        ImageView backButton = toolbar.findViewById(R.id.backButton);
        backButton.setVisibility(View.INVISIBLE);
        TextView name = toolbar.findViewById(R.id.name);
        name.setText("Folders");

        mainPageToolbar = findViewById(R.id.mainPageToolbar);
        mainPageToolbar.addView(toolbar);

        //Recycler view adapter
        folderSection = findViewById(R.id.folderSection);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        folderSection.setLayoutManager(linearLayoutManager);


        //to get folder name


        Adapter adapter = new Adapter(paths(),this);
        folderSection.setAdapter(adapter);
        folder_refresher = findViewById(R.id.folder_refresher);
        folder_refresher.setColorSchemeColors(Color.WHITE);
        folder_refresher.setProgressBackgroundColorSchemeColor(Color.parseColor("#346dbb"));
        folder_refresher.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                paths();
                adapter.notifyDataSetChanged();
                Timer t = new Timer();
                TimerTask tt = new TimerTask(){
                    @Override
                    public void run() {
                        folder_refresher.setRefreshing(false);
                    }
                };
                t.schedule(tt,2000);
            }
        });

    }
    public ArrayList<String> paths(){
        folderNameList.clear();
        Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        String[] projection =  {MediaStore.Video.Media.DATA};
        Cursor cursor = this.getContentResolver().query(uri,projection,null,null,null);
        if (cursor != null)
        {
            while (cursor.moveToNext())
            {
                String path = cursor.getString(0);
                int slashFirstIndex = path.lastIndexOf("/");
                String subString = path.substring(0,slashFirstIndex);
                int index = subString.lastIndexOf("/");
                String folder = subString.substring(index+1);
                if (!folderNameList.contains(folder)){
                    folderNameList.add(folder);
                }
            }
            cursor.close();
        }
        Collections.sort(folderNameList);
        return folderNameList;
    }
}