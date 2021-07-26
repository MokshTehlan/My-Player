package com.moksh.myplayer.VideoFiles.PlayerFiles;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.text.CaptionStyleCompat;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.MappingTrackSelector;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Assertions;
import com.google.android.exoplayer2.util.Util;
import com.moksh.myplayer.R;

import java.util.ArrayList;
import java.util.List;

import static com.moksh.myplayer.VideoFiles.VideoActivityAdapter.modelClassVideos;

public class PlayerActivity extends AppCompatActivity {
    PlayerView playerView;
    SimpleExoPlayer simpleExoPlayer;
    ImageView backButton,next,prev,exit_fullscreen,enter_fullscreen;
    TextView videoName;
//    Matrix matrix = new Matrix();
//    Float scale = 1f;
    ScaleGestureDetector sgd;
    boolean isActivityRunning = false;
    int Position = -1;
    ArrayList<Pair<String,String>> audioLanguages = new ArrayList<Pair<String, String>>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setFullScreenMode();

        super.onCreate(savedInstanceState);



        Position = getIntent().getIntExtra("position",-1);
        String path = modelClassVideos.get(Position).getPath();
        rotateScreen(this,path);



        setContentView(R.layout.activity_player);
        playerView = findViewById(R.id.exoplayer);

        if (path != null){
            Uri uri = Uri.parse(path);
            simpleExoPlayer = new SimpleExoPlayer.Builder(this).build();
            DataSource.Factory factory = new DefaultDataSourceFactory(this,Util.getUserAgent(this,"My Player"));
            ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
            MediaSource mediaSource = new ProgressiveMediaSource.Factory(factory,extractorsFactory).createMediaSource(uri);
            playerView.setPlayer(simpleExoPlayer);
            playerView.setKeepScreenOn(true);
            simpleExoPlayer.prepare(mediaSource);
            isActivityRunning = true;
            simpleExoPlayer.setPlayWhenReady(isActivityRunning);
            try {
                setAudioTrack(simpleExoPlayer);
                Toast.makeText(this, audioLanguages.get(0).first, Toast.LENGTH_SHORT).show();
            }catch (Exception e){
                Toast.makeText(this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        }

        // back button on click listner
        backButton = findViewById(R.id.back_buttom);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlayerActivity.super.onBackPressed();
                isActivityRunning = false;
                simpleExoPlayer.setPlayWhenReady(false);
            }
        });

        sgd = new ScaleGestureDetector(this,new ScaleListener());

//            name of video
        videoName = findViewById(R.id.video_name);
        videoName.setText(modelClassVideos.get(Position).getFileName());

        prev = findViewById(R.id.prev);
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                onStop();
                if (Position == 0) {
                    Toast.makeText(PlayerActivity.this, "No previous video", Toast.LENGTH_SHORT).show();
                } else {
                    playerView.getPlayer().release();
                    Uri uri = Uri.parse(modelClassVideos.get(--Position).getPath());
                    simpleExoPlayer = new SimpleExoPlayer.Builder(PlayerActivity.this).build();
                    DataSource.Factory factory = new DefaultDataSourceFactory(PlayerActivity.this, Util.getUserAgent(PlayerActivity.this, "My Player"));
                    ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
                    MediaSource mediaSource = new ProgressiveMediaSource.Factory(factory, extractorsFactory).createMediaSource(uri);
                    rotateScreen(PlayerActivity.this,modelClassVideos.get(Position).getPath());
                    playerView.setPlayer(simpleExoPlayer);
                    playerView.setKeepScreenOn(true);
                    simpleExoPlayer.prepare(mediaSource);
                    isActivityRunning = true;
                    simpleExoPlayer.setPlayWhenReady(isActivityRunning);
                    onResume();
                    videoName.setText(modelClassVideos.get(Position).getFileName());
                }
            }
        });


        next = findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                onStop();
                if (Position == modelClassVideos.size() - 1) {
                    Toast.makeText(PlayerActivity.this, "No next video", Toast.LENGTH_SHORT).show();
                } else {
                    playerView.getPlayer().release();
                    Uri uri = Uri.parse(modelClassVideos.get(++Position).getPath());
                    simpleExoPlayer = new SimpleExoPlayer.Builder(PlayerActivity.this).build();
                    DataSource.Factory factory = new DefaultDataSourceFactory(PlayerActivity.this, Util.getUserAgent(PlayerActivity.this, "My Player"));
                    ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
                    MediaSource mediaSource = new ProgressiveMediaSource.Factory(factory, extractorsFactory).createMediaSource(uri);
                    rotateScreen(PlayerActivity.this,modelClassVideos.get(Position).getPath());
                    playerView.setPlayer(simpleExoPlayer);
                    playerView.setKeepScreenOn(true);
                    simpleExoPlayer.prepare(mediaSource);
                    isActivityRunning = true;
                    simpleExoPlayer.setPlayWhenReady(isActivityRunning);
                    onResume();
                    videoName.setText(modelClassVideos.get(Position).getFileName());
                }
            }
        });
        CaptionStyleCompat  csc= new CaptionStyleCompat(Color.WHITE,Color.TRANSPARENT,Color.TRANSPARENT,CaptionStyleCompat.EDGE_TYPE_DROP_SHADOW,Color.TRANSPARENT,null);
        playerView.getSubtitleView().setApplyEmbeddedStyles(false);
        playerView.getSubtitleView().setStyle(csc);


        enter_fullscreen = findViewById(R.id.enter_fullscreen);
        exit_fullscreen = findViewById(R.id.exit_fullscreen);
        if(this.getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE){
            enter_fullscreen.setVisibility(View.GONE);
            exit_fullscreen.setVisibility(View.VISIBLE);
        }
        else{
            exit_fullscreen.setVisibility(View.GONE);
            enter_fullscreen.setVisibility(View.VISIBLE);
        }
        exit_fullscreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPause();
                PlayerActivity.this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
                onResume();
                exit_fullscreen.setVisibility(View.GONE);
                enter_fullscreen.setVisibility(View.VISIBLE);
            }
        });

        enter_fullscreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPause();
                PlayerActivity.this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
                onResume();
                exit_fullscreen.setVisibility(View.VISIBLE);
                enter_fullscreen.setVisibility(View.GONE);
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releasePlayer();
    }

    @Override
    protected void onStop() {
        isActivityRunning = false;
        simpleExoPlayer.setPlayWhenReady(isActivityRunning);
        super.onStop();
    }

    private void releasePlayer(){
        if (simpleExoPlayer!=null){
            simpleExoPlayer.release();
            simpleExoPlayer.clearVideoSurface();
            playerView.getPlayer().release();
            simpleExoPlayer = null;
            playerView = null;
        }
    }



    private void setFullScreenMode(){
        requestWindowFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    private void rotateScreen(Context context, String path)
    {


        MediaMetadataRetriever retriever = new MediaMetadataRetriever();

        Bitmap bmp;

        retriever.setDataSource(context,Uri.parse(path));
        bmp = retriever.getFrameAtTime();

        int width = bmp.getWidth();
        int height = bmp.getHeight();

        if (width>height) {
            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);

        }

        else if (width < height || width == height){
            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        }

    }
    private class ScaleListener extends  ScaleGestureDetector.SimpleOnScaleGestureListener{

        Toast t;
        private float scaleFactor = 0f;
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            scaleFactor = detector.getScaleFactor();
            return true;
        }

        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {
            return  true;
        }

        @Override
        public void onScaleEnd(ScaleGestureDetector detector) {
            if (scaleFactor > 1)
            {
                playerView.hideController();
                if (t != null){
                    t.cancel();
                }
                t = Toast.makeText(PlayerActivity.this, "Zoomed to fill",Toast.LENGTH_SHORT);
                t.show();
                t.setGravity(Gravity.TOP,100,200);
                playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_ZOOM);
            }
            else{
                playerView.hideController();
                if (t != null){
                    t.cancel();
                }
                t = Toast.makeText(PlayerActivity.this, "Original",Toast.LENGTH_SHORT);
                t.show();
                t.setGravity(Gravity.TOP,100,200);
                playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIT);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        sgd.onTouchEvent(event);
        return true;
    }


    public void setAudioTrack(SimpleExoPlayer simpleExoPlayer) {
        for(int i = 0; i < simpleExoPlayer.getCurrentTrackGroups().length; i++){
            String format = simpleExoPlayer.getCurrentTrackGroups().get(i).getFormat(0).sampleMimeType;
            String lang = simpleExoPlayer.getCurrentTrackGroups().get(i).getFormat(0).language;
            String id = simpleExoPlayer.getCurrentTrackGroups().get(i).getFormat(0).id;

//            System.out.println(simpleExoPlayer.getCurrentTrackGroups().get(i).getFormat(0));
            if(format.contains("audio") && id != null && lang != null){
//                System.out.println(lang + " " + id);
                audioLanguages.add(new Pair<>(id, lang));
                Toast.makeText(this, format+" "+lang+" "+id, Toast.LENGTH_SHORT).show();
            }
            else {
                audioLanguages.add(new Pair<>("32", "moksh"));
            }
        }
    }
}
