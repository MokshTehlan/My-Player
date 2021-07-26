package com.moksh.myplayer.VideoFiles;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
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
import android.widget.Toast;

import com.moksh.myplayer.Adapter;
import com.moksh.myplayer.R;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.zip.Inflater;

public class VideoActivity extends AppCompatActivity {
    static ArrayList<ModelClassVideo> modelClassVideos = new ArrayList<>();
//    static ArrayList<ModelClassVideo>  newVideoChecker = new ArrayList<>();
    LinearLayout videoActivityToolbar;
    RecyclerView VideoRecycler;
    String myFolderName;
    private static final int  lastFiles = modelClassVideos.size();
    SwipeRefreshLayout video_refresher;
    static  VideoActivityAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        //        Recycler view activity
        VideoRecycler = findViewById(R.id.VideoRecycler);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        VideoRecycler.setLayoutManager(linearLayoutManager);
        myFolderName = getIntent().getStringExtra("ActivityName");
        if (myFolderName != null){
//            newVideoChecker = getAllVideo(myFolderName);
//            newChecker(myFolderName);
            modelClassVideos = getAllVideo(myFolderName);
        }

//        toolbar activity
        LayoutInflater inflater  =getLayoutInflater();
        View toolbar = inflater.inflate(R.layout.toolbar,null);
        ImageView backButton = toolbar.findViewById(R.id.backButton);
        backButton.setVisibility(View.VISIBLE);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VideoActivity.super.onBackPressed();
            }
        });
        ImageView folder  = toolbar.findViewById(R.id.folder);
        folder.setVisibility(View.INVISIBLE);
        TextView Activityname = toolbar.findViewById(R.id.name);
        String activityName = getIntent().getStringExtra("ActivityName");
        String mfoldername = activityName.substring(0,1).toUpperCase()+activityName.substring(1);
        Activityname.setText(mfoldername);
        videoActivityToolbar = findViewById(R.id.videoActivityToolbar);
        videoActivityToolbar.addView(toolbar);


//        Adapter for video activity

        if(modelClassVideos.size()>0) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            adapter = new VideoActivityAdapter(modelClassVideos, this,fragmentManager,myFolderName);
            VideoRecycler.setAdapter(adapter);
            adapter.notifyDataSetChanged();


            video_refresher = findViewById(R.id.video_refresher);
            video_refresher.setColorSchemeColors(Color.WHITE);
            video_refresher.setProgressBackgroundColorSchemeColor(Color.parseColor("#346dbb"));
            video_refresher.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    adapter.notifyDataSetChanged();
                    if (myFolderName != null){
//                        newVideoChecker = getAllVideo(myFolderName);
//                        newChecker(myFolderName);
                        modelClassVideos = getAllVideo(myFolderName);
                    }
                    Timer t = new Timer();
                    TimerTask tt = new TimerTask() {
                        @Override
                        public void run() {
                            video_refresher.setRefreshing(false);
                        }
                    };
                    t.schedule(tt, 2000);
                }
            });
        }
    }
    public ArrayList<ModelClassVideo> getAllVideo(String myFolderName){
        modelClassVideos.clear();
        ArrayList<ModelClassVideo> tempVideoFiles = new ArrayList<>();
        tempVideoFiles.clear();
        Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {
                        MediaStore.Video.Media._ID,
                        MediaStore.Video.Media.DATA,
                        MediaStore.Video.Media.TITLE,
                        MediaStore.Video.Media.SIZE,
                        MediaStore.Video.Media.DATE_ADDED,
                        MediaStore.Video.Media.DURATION,
                        MediaStore.Video.Media.DISPLAY_NAME,
                        MediaStore.Video.Media.BUCKET_DISPLAY_NAME,

        };
        String selection = MediaStore.Video.Media.DATA + " like?";
        String[] selectionArgs = new String[]{"%"+myFolderName+"%"};
        Cursor cursor = this.getContentResolver().query(uri,projection,selection,selectionArgs,null);
        if (cursor != null)
        {
            while (cursor.moveToNext())
            {
                String id = cursor.getString(0);
                String path = cursor.getString(1);
                String title = cursor.getString(2);
                String size = cursor.getString(3);
                String dateAdded = cursor.getString(4);
                String duration = cursor.getString(5);
                String fileName = cursor.getString(6);
                String bucketName = cursor.getString(7);

                ModelClassVideo modelClassVideo = new ModelClassVideo(id, path, title, fileName, size, dateAdded, duration);
                if (myFolderName.equals(bucketName) && duration != null)
                {
                    tempVideoFiles.add(modelClassVideo);
                }

            }
            cursor.close();
        }
        return tempVideoFiles;
    }

    public void newChecker(String myFolderName)
    {
        Log.e("message",String.valueOf(lastFiles));
//           if (modelClassVideos.isEmpty()) {
//               modelClassVideos = newVideoChecker;
//           }
//           else {
//                   for (int i = 0; i < newVideoChecker.size(); i++)
//                   {
//                            if (!newVideoChecker.get(i).getFileName().equals(modelClassVideos.get(i).getFileName()))
//                            {
//                                newVideoChecker.get(i).setNewVideo(true);
//                            } else if (newVideoChecker.get(i).getFileName().equals(modelClassVideos.get(i).getFileName()))
//                            {
//                                if (newVideoChecker.get(i).isNewVideo())
//                                {
//                                    newVideoChecker.get(i).setNewVideo(false);
//                                }
//                            }
//                   }
//                    modelClassVideos = newVideoChecker;
//                    newVideoChecker.clear();
//               }
//           return temp;
        for (int j = lastFiles; j<modelClassVideos.size(); j++)
        {
            modelClassVideos.get(j).setNewVideo(true);
        }
//        lastFiles = modelClassVideos.size();
           }

}