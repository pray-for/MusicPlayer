package com.example.zhangjiawen.myapplication5;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;


/**
 * Created by zhangjiawen on 2016/9/25.
 */


public class MusicAdapter extends BaseAdapter {

    private Context context;
    private List<Song> list;

    public MusicAdapter(MainActivity mainActivity, int item, List<Song> list) {
        this.context = mainActivity;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ViewHolder holder = null;
        Song s = (Song)  getItem(position);
        View view;
        if (convertView == null) {
            //convertView = LayoutInflater.from(context).inflate(resourceId,null);
            holder = new ViewHolder();
            view = LayoutInflater.from(context).inflate(R.layout.item,null ,false);
            //view = View.inflate(context, R.layout.item, null);
            holder.song = (TextView) view.findViewById(R.id.item_mymusic_song);
            holder.singer = (TextView) view.findViewById(R.id.item_mymusic_singer);
            holder.duration = (TextView) view.findViewById(R.id.item_mymusic_duration);
            holder.position = (TextView) view.findViewById(R.id.item_mymusic_position);
            view.setTag(holder);
        } else {
            view = convertView;
            holder = (ViewHolder) convertView.getTag();
        }
        holder.song.setText(String.valueOf(list.get(position).song));
        holder.singer.setText(String.valueOf(list.get(position).singer));
        //时间转换
        int duration = list.get(position).duration;
        String time = MusicUtils.formatTime(duration);
        holder.duration.setText(time);
        holder.position.setText(position + 1 + "");

        return view;
    }


    class ViewHolder {
        TextView song;//歌曲名
        TextView singer;//歌手
        TextView duration;//时长
        TextView position;//序号

    }

}





