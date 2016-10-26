package com.example.percentlinearlayout;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import java.io.IOException;

/**
 * Created by wk on 2016/9/21.
 */
public class MusicPlayerService extends Service {
    private MediaPlayer m_MediaPlayer = null;
    private static final String TAG = "MusicPlayerService";
    private String Path = null;
    private MusicBinder musicbinder = null;
    public MusicPlayerService() {
        Log.i(TAG, TAG + "MusicPlayerService......1");
        musicbinder = new MusicBinder();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, TAG + "onCreate.....");
        if (null != m_MediaPlayer){
            m_MediaPlayer.reset();
            m_MediaPlayer.release();
            m_MediaPlayer = null;
        }
        m_MediaPlayer = new MediaPlayer();
        m_MediaPlayer.setOnCompletionListener(m_CompletionListerner);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return musicbinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (null != intent){
            int Msg = intent.getIntExtra("MSG", StaticDEF.PLAY_MUSIC);
            boolean flag = intent.getBooleanExtra("flag", true);
            Path = intent.getStringExtra("url");
            switch (Msg){
                case StaticDEF.PAUSE_MUSIC:
                    pauseMusic();
                    break;
                case StaticDEF.PLAY_MUSIC:
                    if (flag){
                        playMusic();
                    }
                    m_MediaPlayer.start();
                    break;
                default:
                    break;
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private MediaPlayer.OnCompletionListener m_CompletionListerner = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
            nextMusicListener.nextToMusic();
        }
    };

    @Override
    public boolean onUnbind(Intent intent) {
        Log.i(TAG,"onUnbind......");
        return super.onUnbind(intent);

    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
        Log.i(TAG, "onRebind......");
    }

    @Override
    public void onDestroy() {
        Log.i(TAG,"onDestroy......");
        super.onDestroy();
        if (m_MediaPlayer != null) {
            m_MediaPlayer.stop();
            m_MediaPlayer.release();
            m_MediaPlayer = null;
        }
    }

    private void playMusic(){
       if (null == Path){
           return;
       }

        try {
            m_MediaPlayer.reset();
            m_MediaPlayer.setDataSource(Path);
//            m_MediaPlayer.setLooping(true);//设置循环播放
            m_MediaPlayer.prepare();
            m_MediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void pauseMusic(){
        m_MediaPlayer.pause();
    }

    public void stop() {
            if (m_MediaPlayer != null) {
                m_MediaPlayer.stop();
                m_MediaPlayer.release();
                m_MediaPlayer = null;
            }
        }

    public class MusicBinder extends Binder {
        public MusicPlayerService getPlayInfo(){
            return MusicPlayerService.this;
        }
    }

    public MediaPlayer getMediaPlayer(){
     return m_MediaPlayer;
    }


    private NextMusicLinstener nextMusicListener;
    public interface NextMusicLinstener{
        void nextToMusic();
    }

    public void setNextMusicListener(NextMusicLinstener nextMusicListener) {
        this.nextMusicListener = nextMusicListener;
    }
}
