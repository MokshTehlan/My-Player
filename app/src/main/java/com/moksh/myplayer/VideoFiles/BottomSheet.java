package com.moksh.myplayer.VideoFiles;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.moksh.myplayer.R;

import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.moksh.myplayer.VideoFiles.VideoActivity.adapter;
import static com.moksh.myplayer.VideoFiles.VideoActivityAdapter.foldername;
import static com.moksh.myplayer.VideoFiles.VideoActivityAdapter.modelClassVideos;

public class BottomSheet extends BottomSheetDialogFragment {
    Context context;
    int position;
    VideoActivity videoActivity;
    LinearLayout properties,delete,rename;
    public BottomSheet(Context context,int position) {
        this.context = context;
        this.position = position;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_video_bottom_sheet,container,false);
        TextView videoName = view.findViewById(R.id.video_name);
        videoName.setText(modelClassVideos.get(position).getFileName());

        properties = view.findViewById(R.id.properties);
        rename = view.findViewById(R.id.rename);
        delete = view.findViewById(R.id.delete);

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                File file = new File(path);
//                file.delete();
            }
        });


        properties.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                properties_dialog();
            }
        });

        rename.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                renmaeDialog();
            }
        });
        return view;
    }
    public void properties_dialog(){

        BottomSheet.this.dismiss();
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        View view = getLayoutInflater().inflate(R.layout.properties_dialog,null);
        TextView file_name  = (TextView)view.findViewById(R.id.file_name);
        file_name.setText(modelClassVideos.get(position).getFileName());

        TextView file_name_full  = (TextView)view.findViewById(R.id.file_name_full);
        file_name_full.setText(modelClassVideos.get(position).getFileName());

        TextView location  = (TextView)view.findViewById(R.id.location);
        String path = modelClassVideos.get(position).getPath();
        int slashFirstIndex = path.lastIndexOf("/");
        String subString = path.substring(0,slashFirstIndex);

        location.setText(subString);

        TextView date  = (TextView)view.findViewById(R.id.date);
        File file = new File(modelClassVideos.get(position).getPath());
        Date dateAdded = new Date(file.lastModified());
        SimpleDateFormat ft = new SimpleDateFormat("dd.MM.yyyy', ' hh:mm a");
        date.setText(""+ft.format(dateAdded));

        TextView size  = (TextView)view.findViewById(R.id.size);
        String sizeOfVideo = "";
        long  correctSyze = Long.parseLong(modelClassVideos.get(position).getSize());
        if (correctSyze >= 1000000 && correctSyze < 1000000000){
            correctSyze = correctSyze/1000000;
            sizeOfVideo = correctSyze+" MB";
        }
        else if (correctSyze > 1000000000){
            correctSyze = correctSyze/1000000000;
            sizeOfVideo = correctSyze+" GB";
        }

        size.setText(sizeOfVideo);

        TextView length  = (TextView)view.findViewById(R.id.length);
        length.setText(modelClassVideos.get(position).getDuration());

        TextView ok = view.findViewById(R.id.ok);

        alert.setView(view);

        AlertDialog alertDialog = alert.create();
        alertDialog.setCanceledOnTouchOutside(false);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        alertDialog.show();

    }

    public void renmaeDialog()
    {
        BottomSheet.this.dismiss();
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        View view = getLayoutInflater().inflate(R.layout.rename_dialog,null);

        EditText renameTo = view.findViewById(R.id.renameTo);
        String path = modelClassVideos.get(position).getPath();
        String tempPath = path;
        File file = new File(path);
        int begIndex = tempPath.lastIndexOf("/");
        int endIndex = tempPath.lastIndexOf(".");
        String nameOfFile = tempPath.substring(begIndex+1,endIndex);

        renameTo.setHint(nameOfFile);


        alert.setView(view);
        AlertDialog alertDialog = alert.create();
        alertDialog.setCanceledOnTouchOutside(false);

        TextView ok = view.findViewById(R.id.ok);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String renamedTo = renameTo.getText().toString();
                String newName = path.substring(0,begIndex)+"/"+renamedTo+path.substring(endIndex,path.length());
                File newFile = new File(newName);

                file.renameTo(newFile);
                modelClassVideos.get(position).setPath(newName.toString());
                modelClassVideos.get(position).setFileName(renamedTo+path.substring(endIndex,path.length()));
                Toast.makeText(context, "new name: "+newName.toString(), Toast.LENGTH_LONG).show();
                alertDialog.dismiss();
                adapter.notifyDataSetChanged();
            }
        });

        TextView cancel = view.findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }
}
