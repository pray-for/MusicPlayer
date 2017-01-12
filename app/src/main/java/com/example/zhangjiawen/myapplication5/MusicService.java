package com.example.zhangjiawen.myapplication5;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.io.IOException;

/**
 * Created by zhangjiawen on 2016/10/4.
 */
public class MusicService extends Service {

    private MyBinder binder = new MyBinder();
    public MediaPlayer player = new MediaPlayer();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public class MyBinder extends Binder {
        public MusicService getMusicService() {
            return MusicService.this;
        }
    }

    //音乐播放
    public void play(String path) {
        player.reset();
        try {
            player.setDataSource(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            player.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //监听是否已经准备好
        player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                player.start();//开始播放音乐
            }
        });
    }

    //音乐暂停
    public void pause() {
        if (player != null) {
            player.pause();
        }
    }

    public void countineplay() {
        player.start();
    }

}


//
//import android.app.Service;
//import android.content.Intent;
//import android.media.MediaPlayer;
//import android.os.IBinder;
//import android.support.annotation.Nullable;
//
//import java.io.IOException;
//import java.util.List;
//
//
///**
// * Created by zhangjiawen on 2016/10/4.
// */
//public class MusicService extends Service {
//
//    private int status ;
//    private int current ;
//    MediaPlayer player;
//    private List<Song>list;
//
////    private  MyBinder binder = new MyBinder();
////    public class MyBinder extends Binder{
////        public int getCurrent(){
////            return current;
////        }
////    }
//
//    @Nullable
//    @Override
//    public IBinder onBind(Intent intent) {
////        return binder;
//        return null;
//    }
//
//
////    public void onCreate(){
////
////        super.onCreate();
////        list=MainActivity.list;
////        MyReceiver serviceReceiver = new MyReceiver();
////        IntentFilter filter = new IntentFilter();
////        filter.addAction("ASD");
////        registerReceiver(serviceReceiver,filter);
//
////        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
////            @Override
////            public void onCompletion(MediaPlayer mediaPlayer) {
////                if (current == MainActivity.list.size() - 1){
////                    current = 0;
////                }
////                else {
////                    current++;
////                }
////                Song song = MainActivity.list.get(current);
////                songName.setText(song.getSong());
////                singer.setText(song.getSinger());
////
////            }
////
////        });
////    }
//
//
//
//    public int onStartCommand(final Intent intent , int flag , int startId){
//        status = intent.getIntExtra("status" , status);
//        switch (status){
//            //播放
//            case MainActivity.PLAY:
//                current = intent.getIntExtra("current" , current);
//                if (player == null){
//                    player = new MediaPlayer();
//                }
//                player.reset();
//                try {
//                    player.setDataSource(list.get(current).path);
//                    player.prepare();
//                    player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//                        @Override
//                        public void onPrepared(MediaPlayer mediaPlayer) {
//                            mediaPlayer.start();
//                            int totalTime=mediaPlayer.getDuration();//得到总时间
//                            Intent intent=new Intent("ASD");
//                            intent.putExtra("current",MainActivity.TOTAL_TIME);
//                            intent.putExtra(MainActivity.TIME,totalTime);//将歌曲总时间传回去
//                            sendBroadcast(intent);
////                            new MusicplayerThread().start();
//                        }
//                    });
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                break;
//            //暂停
//            case MainActivity.PAUSE:
//                if (player.isPlaying()){
//                    player.pause();
//                }
//                else {
//                    player.start();
////                    new MusicplayerThread().start();
//                }
//                break;
////            case MainActivity.SEEK_TO://拖动SeekBar，设置播放进度
////                int progress=intent.getIntExtra(MainActivity.PROGRESS,0);//得到拖动位置
////                player.seekTo(progress);
////                break;
//            default:
//                break;
//
//        }
//
//        return super.onStartCommand(intent , flag , startId);
//    }
//
//
////    class MusicplayerThread extends Thread{
////        @Override
////        public void run() {
////            while (player.isPlaying()){
////                int currentTime=player.getCurrentPosition();//得到当前播放时间
////                Intent intent=new Intent("ASD");
////                intent.putExtra("current",MainActivity.CURRENT_TIME);
////                intent.putExtra(MainActivity.TIME,currentTime);//将当前播放时间传回去
////                sendBroadcast(intent);
////                try {
////                    Thread.sleep(1000);//休眠，不让消息发送太频繁
////                } catch (InterruptedException e) {
////                    e.printStackTrace();
////                }
////            }
////        }
////    }
//
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        if(player!=null){
//            player.stop();
//            player.release();//释放相关资源
//        }
//    }
//
//
//
//
//}
//
