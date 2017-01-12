package com.example.zhangjiawen.myapplication5;


import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {

    private List<Song> list = new ArrayList<Song>();
    private MusicAdapter adapter;
    private ListView mListView;
    private int index = -1;
    private Context context;
    //定义下底栏中的控件
    private TextView songName;
    private TextView singer;
    private ImageButton previous;
    private ImageButton next;
    private ImageButton pause;
    private SeekBar seekBar;

    //播放后台的定义
    private MusicService musicService;
    private MediaPlayer player;
    private MusicServiceConnection conn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;
        initView();
        MusicAdapter adapter = new MusicAdapter(MainActivity.this, R.layout.item, list);
        ListView mListView = (ListView) findViewById(R.id.main_listview);
        mListView.setAdapter(adapter);
        initEvent();
    }

    //初始化点击事件
    private void initEvent() {

        songName.setOnClickListener(this);
        singer.setOnClickListener(this);
        previous.setOnClickListener(this);
        next.setOnClickListener(this);
        pause.setOnClickListener(this);
        seekBar.setOnSeekBarChangeListener(this);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            //点击listView
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                index = position;
                //开启服务
                Intent intentser = new Intent(context, MusicService.class);
                context.startService(intentser);
                //绑定服务
                conn = new MusicServiceConnection();
                context.bindService(intentser, conn, Context.BIND_AUTO_CREATE);
            }
        });

    }

    //初始化控件
    private void initView() {
        songName = (TextView) findViewById(R.id.songName);
        singer = (TextView) findViewById(R.id.singer);
        previous = (ImageButton) findViewById(R.id.previous);
        next = (ImageButton) findViewById(R.id.next);
        pause = (ImageButton) findViewById(R.id.pause);
        mListView = (ListView) findViewById(R.id.main_listview);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
//        getList();
//        adapter=new MyMusicAdapter(this,list);
//        mListView.setAdapter(adapter);

        list = new ArrayList<Song>();
        //把扫描到的音乐赋值给list
        list = MusicUtils.getMusicData(this);
        adapter = new MusicAdapter(this, R.layout.item, list);
        mListView.setAdapter(adapter);
    }


        //按钮的监听事件
        @Override
        public void onClick (View v){
            switch (v.getId()) {
                case R.id.previous:
                    playMusicByStatus(2);
                    break;
                case R.id.next:
                    playMusicByStatus(1);
                    break;
                case R.id.pause:
                    if (player != null) {
                        if (player.isPlaying()) {
                            musicService.pause();
                            //改变图标
                            pause.setImageResource(R.drawable.icon_pause_normal);
                        } else {
                            musicService.countineplay();
                            //改变图标
                            pause.setImageResource(R.drawable.icon_play_normal);
                        }
                    }
                    break;
            }
        }


    //数值改变时调用
    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

    }
    //开始拖动时调用
    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }
    //停止拖动时调用
    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }


    public class MusicServiceConnection implements ServiceConnection {

            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                MusicService.MyBinder myBinder = (MusicService.MyBinder) service;
                musicService = myBinder.getMusicService();
                //开始播放
                player = musicService.player;
                //if (!player.isPlaying()) {
                    playMusicByStatus(0);
                    //改变图标
                    pause.setImageResource(R.drawable.icon_pause_normal);
                    player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            playMusicByStatus(1);
                        }
                    });
                }
//            else {
//                    //改变图标,无所谓  就看思维严谨不！
//                    pause.setImageResource(R.drawable.icon_play_normal);
//                }
//            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        }

    public void playMusicByStatus(int status) {
        switch (status) {
            case 0:
                //直接播放
                break;
            case 1:
                //下一首
                index++;
                if (index == list.size()) {
                    index = 0;
                }
                break;
            case 2:
                //上一首
                index--;
                if (index == -1) {
                    index = list.size() - 1;
                }
                break;
        }
        playMusic(index);
    }

    private void playMusic(int index) {
        if (list.size() > 0) {
            musicService.play(list.get(index).path);//调用Service播放音乐
            if (list.get(index).song.length() > 10) {   //在底部显示当前播放曲目信息
                songName.setText(list.get(index).song.substring(0, 9) + "...");
            } else {
                songName.setText(list.get(index).song);
            }
            if (list.get(index).singer.length() > 8) {
                singer.setText(list.get(index).singer.substring(0, 7) + "...");
            } else {
                singer.setText(list.get(index).singer);
            }
        }
    }

    //解绑服务
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (conn != null) {
            this.unbindService(conn);
        }
    }
}



//import android.app.Activity;
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.content.IntentFilter;
//import android.media.MediaPlayer;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.AdapterView;
//import android.widget.ImageButton;
//import android.widget.ListView;
//import android.widget.SeekBar;
//import android.widget.TextView;
//
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.List;
//
//
//public class MainActivity extends Activity {
//
//    private MusicAdapter adapter;
//    private ListView mListView;
//    public static List<Song> list;
//    MediaPlayer player = new MediaPlayer();
//    ImageButton pause , previous , next;
//    TextView songName , singer;
//    private int current ;
//    public static final int PLAY = 0;
//    public static final int PAUSE = 1;
//    public static final int TOTAL_TIME=0;
//    public static final int CURRENT_TIME=1;
//    public static final int SEEK_TO=2;
//    public static final String TYPE="type";
//    public static final String TIME="time";
//    public static final String PATH="musicpath";
//    public static final String PROGRESS="progress";
//    int position;
//    private View.OnClickListener listener;
//    private boolean isPlay;
//    private boolean isPause;
//    private boolean isFirstClick = true;
//    private MyMusicBroadcastReceicer myMusicBroadcastReceicer;
//    SeekBar seekBar;
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        pause = (ImageButton) findViewById(R.id.pause);
//        previous = (ImageButton) findViewById(R.id.previous);
//        next = (ImageButton) findViewById(R.id.next);
//        songName = (TextView) findViewById(R.id.songName);
//        singer = (TextView) findViewById(R.id.singer);
//
//        myMusicBroadcastReceicer = new MyMusicBroadcastReceicer();
//        IntentFilter intentFilter = new IntentFilter();
//        intentFilter.addAction("ASD");
//        registerReceiver(myMusicBroadcastReceicer, intentFilter);//动态注册广播
//
//        Intent intent = new Intent();
//        intent.putExtra("current" , current);
//        intent.setClass(MainActivity.this , MusicService.class);
//        startService(intent);
//
//
//        //获取音乐信息，并通过LIstView显示
//        initView();
//        MusicAdapter adapter = new MusicAdapter(MainActivity.this, R.layout.item, list);
//        ListView mListView = (ListView) findViewById(R.id.main_listview);
//        mListView.setAdapter(adapter);
//        //播放
//        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int current, long l) {
//                position = current;
//                isFirstClick=false;
//                isPlay=true;
//                isPause=false;
//
//                Intent intent = new Intent("ASD");
//                intent.putExtra("status", PLAY);
//                intent.putExtra("current" , current);
//                startService(intent);
//                if (list.get(position).song.length() > 10) {   //在底部显示当前播放曲目信息
//                    songName.setText(list.get(position).song.substring(0, 9) + "...");
//                } else {
//                    songName.setText(list.get(position).song);
//                }
//                if (list.get(position).singer.length() > 8) {
//                    singer.setText(list.get(position).singer.substring(0, 7) + "...");
//                } else {
//                    singer.setText(list.get(position).singer);
//                }
//
//            }
//        });
//
////        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
////            @Override
////            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
////
////            }
////
////            @Override
////            public void onStartTrackingTouch(SeekBar seekBar) {
////
////            }
////
////            @Override
////            public void onStopTrackingTouch(SeekBar seekBar) {
////                Intent intent = new Intent(getApplicationContext(), MusicService.class);
////                intent.putExtra("current", SEEK_TO);
////                intent.putExtra(PROGRESS, seekBar.getProgress());//传回拖动位置
////                startService(intent);
////            }
////        });
//
//
//        //暂停
//        pause.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                if (isFirstClick) {//第一次点击播放，默认播放第一首歌
//                    pause.setBackgroundResource(R.drawable.icon_pause_normal);
//                    Intent intent = new Intent(getApplicationContext(), MusicService.class);
//                    intent.putExtra("status", PLAY);
//                    intent.putExtra("current" , 0);
//                    startService(intent);
//                    isFirstClick = false;
//                    isPlay = true;
//                    isPause = false;
//                } else {
//                    if (isPlay) {
//                        pause.setBackgroundResource(R.drawable.icon_play_normal);
//                        Intent intent = new Intent(getApplicationContext(), MusicService.class);
//                        intent.putExtra("status", PAUSE);
//                        startService(intent);
//                        isPlay = false;
//                        isPause = true;
//                    } else if (isPause) {
//                        pause.setBackgroundResource(R.drawable.icon_pause_normal);
//                        Intent intent = new Intent(getApplicationContext(), MusicService.class);
//                        intent.putExtra("status", PAUSE);
//                        startService(intent);
//                        isPlay = true;
//                        isPause = false;
//                    }
//                }
//
////                Intent intent = new Intent("ASD");
////                intent.putExtra("current" , current);
////                sendBroadcast(intent);
//                if (list.get(position).song.length() > 10) {   //在底部显示当前播放曲目信息
//                    songName.setText(list.get(position).song.substring(0, 9) + "...");
//                } else {
//                    songName.setText(list.get(position).song);
//                }
//                if (list.get(position).singer.length() > 8) {
//                    singer.setText(list.get(position).singer.substring(0, 7) + "...");
//                } else {
//                    singer.setText(list.get(position).singer);
//                }
//            }
//        });
//        //上一首
//        previous.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View view) {
//
//                isFirstClick=false;
//                isPlay = true;
//                isPause = false;
//                if (position == 0){
//                    position = list.size() - 1;
//                }
//                else{
//                    position--;
//                }
//                Intent intent = new Intent("ASD");
//                intent.putExtra("status", PLAY);
//                intent.putExtra("current" , position);
//                startService(intent);
//                if (list.get(position).song.length() > 10) {   //在底部显示当前播放曲目信息
//                    songName.setText(list.get(position).song.substring(0, 9) + "...");
//                } else {
//                    songName.setText(list.get(position).song);
//                }
//                if (list.get(position).singer.length() > 8) {
//                    singer.setText(list.get(position).singer.substring(0, 7) + "...");
//                } else {
//                    singer.setText(list.get(position).singer);
//                }
//            }
//
//        });
//
//
//        //下一首
//        next.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                isFirstClick=false;
//                isPlay = true;
//                isPause = false;
//                if (position == list.size() - 1){
//                    position = 0;
//                }
//                else {
//                    position++;
//                }
//                Intent intent = new Intent("ASD");
//                intent.putExtra("status", PLAY);
//                intent.putExtra("current" , position);
//                startService(intent);
//                if (list.get(position).song.length() > 10) {   //在底部显示当前播放曲目信息
//                    songName.setText(list.get(position).song.substring(0, 9) + "...");
//                } else {
//                    songName.setText(list.get(position).song);
//                }
//                if (list.get(position).singer.length() > 8) {
//                    singer.setText(list.get(position).singer.substring(0, 7) + "...");
//                } else {
//                    singer.setText(list.get(position).singer);
//                }
//            }
//        });
//
//    }
//
//
//    class MyMusicBroadcastReceicer extends BroadcastReceiver {
//
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            int type = intent.getIntExtra("current", 0);
//            SimpleDateFormat format = new SimpleDateFormat("mm:ss");//设置歌曲显示时间格式
//            switch (type) {
//                case TOTAL_TIME:
//                    int totalTime = intent.getIntExtra(TIME, 0);
//                    seekBar.setMax(totalTime);
////                    textView_totaltime.setText(format.format(totalTime));
//                    break;
//                case CURRENT_TIME:
//                    int currentTime = intent.getIntExtra(TIME, 0);
//                    seekBar.setProgress(currentTime);
////                    textView_currenttime.setText(format.format(currentTime));
//                    break;
//                default:
//                    break;
//            }
//        }
//    }
//
//    //获取播放列表
//    private void initView() {
//        mListView = (ListView) findViewById(R.id.main_listview);
//        list = new ArrayList<Song>();
//        //把扫描到的音乐赋值给list
//        list = MusicUtils.getMusicData(this);
//        adapter = new MusicAdapter(this, R.layout.item, list);
//        mListView.setAdapter(adapter);
//    }
//
//
//
//}
