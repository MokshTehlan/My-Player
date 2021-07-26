package com.moksh.myplayer.VideoFiles;

import static java.sql.Types.NULL;

public class ModelClassVideo {
    private String id;
    private String path;
    private String title;
    private String fileName;
    private String size;
    private String dateAdded;
    private String duration;

    public String getTitle() {
        return title;
    }

    public String getSize() {
        return size;
    }

    public String getDateAdded() {
        return dateAdded;
    }

    private boolean newVideo = false;

    public String getId() {
        return id;
    }

    public String getPath() {
        return path;
    }

    public String getFileName() {
        return fileName;
    }
    public String getDuration() {
            int milliseconds = Integer.parseInt(duration);
            String finalTime = "";
            String minute = "", second = "";
            int hours = milliseconds / (1000 * 60 * 60);
            int minutes = (milliseconds % (1000 * 60 * 60)) / (1000 * 60);
            int seconds = ((milliseconds % (1000 * 60 * 60)) % (1000 * 60)) / 1000;
            if (hours > 0) {
                finalTime = hours + ":";
            }
            if (minutes < 10) {
                minute = "0" + minutes;
            } else {
                minute = "" + minutes;
            }
            if (seconds < 10) {
                second = "0" + seconds;
            } else {
                second = "" + seconds;
            }
            finalTime = finalTime + minute + ":" + second;
            return finalTime;
    }

    public void setNewVideo(boolean newVideo) {
        this.newVideo = newVideo;
    }

    public boolean isNewVideo(){
        return newVideo;
    }
    public ModelClassVideo(String id, String path, String title, String fileName, String size, String dateAdded, String duration) {
        this.id = id;
        this.path = path;
        this.title = title;
        this.fileName = fileName;
        this.size = size;
        this.dateAdded = dateAdded;
        this.duration = duration;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
