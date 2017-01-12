package com.example.zhangjiawen.myapplication5;

/**
 * Created by zhangjiawen on 2016/9/27.
 */
public class Song {
    public String singer;//歌手名
    public String song;//歌曲名
    public String path;//歌曲的地址
    public int duration;//歌曲长度
    public long size;//歌曲大小

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getSinger() {
        return singer;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getSong() {
        return song;
    }

    public void setSong(String song) {
        this.song = song;
    }


}
