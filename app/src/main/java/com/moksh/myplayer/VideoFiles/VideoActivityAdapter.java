package com.moksh.myplayer.VideoFiles;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.moksh.myplayer.Adapter;
import com.moksh.myplayer.R;
import com.moksh.myplayer.VideoFiles.PlayerFiles.PlayerActivity;

import org.w3c.dom.Text;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class VideoActivityAdapter extends RecyclerView.Adapter<VideoActivityAdapter.viewholder> {
    public static List<ModelClassVideo> modelClassVideos;
    private Context context;
    public static FragmentManager fragmentManager;
    static String foldername;

    public VideoActivityAdapter(List<ModelClassVideo> modelClassVideos, Context context, FragmentManager fragmentManager,String foldername) {
        this.modelClassVideos = modelClassVideos;
        this.context = context;
        this.fragmentManager = fragmentManager;
        this.foldername = foldername;
    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.video_view,parent,false);
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder holder, int position) {
        int size =  modelClassVideos.size()-1;
//        if (modelClassVideos.get(size-position).isNewVideo()){
//            holder.new_video.setVisibility(View.VISIBLE);
//        }
        holder.video_name.setText(modelClassVideos.get(position).getFileName());
        holder.video_time.setText(modelClassVideos.get(position).getDuration());
        int radius = context.getResources().getDimensionPixelSize(R.dimen.corner_radius);
        RequestOptions myOptions =new RequestOptions().transform(new CenterCrop(), new RoundedCorners(radius));

        Glide.with(context)
                .asBitmap()
                .apply(myOptions)
                .encodeQuality(1)
                .load(new File(modelClassVideos.get(position).getPath()))
                .into(holder.video_thumbnail);

        holder.more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheet bottomSheet = new BottomSheet(context,position);
                bottomSheet.show(fragmentManager,"TAG");
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, PlayerActivity.class);
                intent.putExtra("position",position);
                context.startActivity(intent);
//                Timer t = new Timer();
//                TimerTask tt = new TimerTask() {
//                    @Override
//                    public void run() {
//                        if (modelClassVideos.get(size-position).isNewVideo()){
//                            modelClassVideos.get(size-position).setNewVideo(false);
//                            holder.new_video.setVisibility(View.INVISIBLE);
//                        }
//                    }
//                };
//                t.schedule(tt, 1000);
            }
        });

    }

    @Override
    public int getItemCount() {
        return modelClassVideos.size();
    }

    class viewholder extends RecyclerView.ViewHolder{
        ImageView video_thumbnail,more;
        TextView video_name, video_time, new_video;
        public viewholder(@NonNull View itemView) {
            super(itemView);
            video_thumbnail = itemView.findViewById(R.id.video_thumbnail);
            video_name = itemView.findViewById(R.id.video_name);
            video_time = itemView.findViewById(R.id.video_time);
            more = itemView.findViewById(R.id.more);
            new_video = itemView.findViewById(R.id.new_video);
        }
    }

}
