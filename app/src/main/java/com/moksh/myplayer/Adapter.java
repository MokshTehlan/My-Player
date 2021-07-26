package com.moksh.myplayer;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.moksh.myplayer.VideoFiles.VideoActivity;

import java.time.Duration;
import java.util.ArrayList;
public class Adapter extends RecyclerView.Adapter<Adapter.viewholder> {

    private ArrayList<String> folderNames;
    private Context context;

    public Adapter(ArrayList<String> folderNames,Context context) {
        this.folderNames = folderNames;
        this.context = context;
    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.folders_view,parent,false);
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder holder, int position) {
        String folderName = folderNames.get(position);
        String mfoldername = folderName.substring(0,1).toUpperCase()+folderName.substring(1);
        String files = "";
        if (noOfFiles(folderName)==1){
             files = ""+noOfFiles(folderName)+" video";
        }
        else{
             files = ""+noOfFiles(folderName)+" videos";
        }
        holder.setData(mfoldername,files);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, VideoActivity.class);
                intent.putExtra("ActivityName",folderName);
                context.startActivity(intent);
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(context, "working", Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return folderNames.size();
    }

    class viewholder extends RecyclerView.ViewHolder{
        private TextView folder_name,items_in_folder;
        public viewholder(@NonNull View itemView) {
            super(itemView);
            folder_name = itemView.findViewById(R.id.folder_name);
            items_in_folder = itemView.findViewById(R.id.items_in_folder);
        }
        private void setData(String folderName, String noOfFiles){
            folder_name.setText(folderName);
            items_in_folder.setText(noOfFiles);
        }
    }
    public int noOfFiles(String folderName){
        int count= 0;
        Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        String[] projection =  {MediaStore.Video.Media.DATA,MediaStore.Video.Media.DURATION,MediaStore.Video.Media.BUCKET_DISPLAY_NAME};
        String selection = MediaStore.Video.Media.DATA + " like?";
        String[] selectionArgs = new String[]{"%"+folderName+"%"};
        Cursor cursor = context.getContentResolver().query(uri,projection,selection,selectionArgs,null);
        if (cursor != null)
        {
            while (cursor.moveToNext())
            {
                if (folderName.equals(cursor.getString(2)) && cursor.getString(1)!= null) {
                    count++;
                }
            }
            cursor.close();
        }
        return count;
    }
}
