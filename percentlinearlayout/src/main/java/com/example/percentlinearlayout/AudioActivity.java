package com.example.percentlinearlayout;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.percentlinearlayout.helper.PopUpWindowHelper;
import com.example.percentlinearlayout.tool.AudioTool;
import com.example.percentlinearlayout.util.UIC_VoiceDialog;

import java.util.ArrayList;

public class AudioActivity extends AppCompatActivity{
    private static final String TAG = "AudioActivity";
    private TextView m_Song = null;
    private TextView m_Artist = null;
    private TextView m_Album = null;
    private ImageView m_ArtWork = null;
    private SeekBar m_SeekBar = null;
    private ImageView m_PlayPause = null;
    private ImageView m_Pre = null;
    private ImageView m_Next = null;
    private ImageView m_Setting = null;
    private ImageView m_ListMusic = null;
    private ArrayList<MusicMedia> m_list = null;
    private MusicPlayerService m_MusicPlayerService = null;
    private MediaPlayer mediaPlayer = null;
    private PopUpWindowHelper m_PopUpWindowHelper = null;
    private UIC_VoiceDialog m_Dialog = null;
    private Intent intent = null;
    private boolean isplay = true;
    private int index = 0;

    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            m_MusicPlayerService = ((MusicPlayerService.MusicBinder) service).getPlayInfo();
            if (null != m_MusicPlayerService){
                m_MusicPlayerService.setNextMusicListener(new MySongEndLinstener());
                mediaPlayer = m_MusicPlayerService.getMediaPlayer();
                if (null != mediaPlayer){
                   sendToMessage();
                }
            }

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            m_MusicPlayerService = null;
        }
    };

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (0 == msg.what){
                if (null == m_MusicPlayerService){
                    return;
                }
                m_SeekBar.setMax(mediaPlayer.getDuration());
                Log.i(TAG, TAG + "getCurrentPosition :" + mediaPlayer.getCurrentPosition());
                m_SeekBar.setProgress(mediaPlayer.getCurrentPosition());
                sendToMessage();
            }
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, TAG + "onCreate");
        setContentView(R.layout.activity_main);
        init();
        initData();
        initLoadPlayInfo();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, TAG + "onResume");
        setSeekBarListener();
    }

    private void init(){
        m_Song = (TextView) findViewById(R.id.Song);
        m_Artist = (TextView) findViewById(R.id.Artist);
        m_Album = (TextView) findViewById(R.id.Album);
        m_ArtWork = (ImageView) findViewById(R.id.artwork);
        m_SeekBar = (SeekBar) findViewById(R.id.seekbar);
        m_PlayPause = (ImageView) findViewById(R.id.playpause);
        m_Pre = (ImageView) findViewById(R.id.pre);
        m_Next = (ImageView) findViewById(R.id.next);
        m_Setting = (ImageView) findViewById(R.id.setting);
        m_ListMusic = (ImageView) findViewById(R.id.list_music);
        m_PopUpWindowHelper = PopUpWindowHelper.getInstance();

        MyOnClick onClick = new MyOnClick();
        m_Pre.setOnClickListener(onClick);
        m_Next.setOnClickListener(onClick);
        m_PlayPause.setOnClickListener(onClick);
        m_Setting.setOnClickListener(onClick);
        m_ListMusic.setOnClickListener(onClick);
        intent = new Intent(AudioActivity.this, MusicPlayerService.class);
    }

    private void initData() {
        //返回所有在外部存储卡上的音乐文件的信息：
        Log.i(TAG, "initData ");
        Cursor cursor = getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null, MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
        if (null == cursor || 0 == cursor.getCount()) {
            return;
        }

        m_list = new ArrayList<MusicMedia>();
            while (cursor.moveToNext()) {
                MusicMedia  m_MusicMedia = new MusicMedia();
                //歌曲编号
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID));
                //歌曲标题
                String song = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE));
                //歌曲的歌手名： MediaStore.Audio.Media.ARTIST
                String artist = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));
                //歌曲的专辑名：MediaStore.Audio.Media.ALBUM
                String album = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM));
                //歌曲的专辑ID：MediaStore.Audio.Media.ALBUM
                int albumID = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID));
                //歌曲文件的路径 ：MediaStore.Audio.Media.DATA
                String path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));

                //歌曲的总播放时长 ：MediaStore.Audio.Media.DURATION
                int duration = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION));
                //歌曲文件的大小 ：MediaStore.Audio.Media.SIZE
//            Long size = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE));
                int isMusic = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.IS_MUSIC)); // 是否为音乐
                if (0 != isMusic && !path.contains("voice")){
                    Log.i(TAG, "initData path :" + path);
                    m_MusicMedia.setId(id);
                    m_MusicMedia.setSong(song);
                    m_MusicMedia.setArtist(artist);
                    m_MusicMedia.setAlbum(album);
                    m_MusicMedia.setAlbumId(albumID);
                    m_MusicMedia.setUrl(path);
                    m_MusicMedia.setTime(duration);
                    m_list.add(m_MusicMedia);
                }
            }
        cursor.close();

    }


    private void initLoadPlayInfo(){
        if (null == m_list || 0 == m_list.size()){
            return;
        }

        m_Song.setText(m_list.get(0).getSong());
        m_Artist.setText(m_list.get(0).getArtist());
        m_Album.setText(m_list.get(0).getAlbum());
        String artPath = AudioTool.getInstance().getAlbumArt(this, m_list.get(0).getAlbumId());
        if (null == artPath){
            m_ArtWork.setImageResource(R.drawable.ic_avatar2);
        }else {
            m_ArtWork.setImageBitmap(AudioTool.getInstance().compressedBitmap(artPath, 200, 200));
        }
        playMusic(true);
    }

    private void playMusic(boolean... firstMusic){
        if (null != firstMusic && 0 < firstMusic.length){
            intent.putExtra("flag", true);
        }else{
            intent.putExtra("flag", false);
        }
        sendToMessage();
        intent.putExtra("url", m_list.get(index).getUrl());
        intent.putExtra("MSG", StaticDEF.PLAY_MUSIC);
        isplay = true;
        m_PlayPause.setBackgroundResource(R.drawable.pause);
        startService(intent);
        bindService(intent, conn, Context.BIND_AUTO_CREATE);
    }

    private void pauseMusic(){
         handler.removeMessages(0);
         intent.putExtra("MSG", StaticDEF.PAUSE_MUSIC);
         isplay = false;
         m_PlayPause.setBackgroundResource(R.drawable.play);
         startService(intent);

    }

    private void preMusic(){
        if (null == m_list || 0 == m_list.size()){
            return;
        }
        index --;
        if (index == -1){
                index = m_list.size() - 1;
        }
        m_Song.setText(m_list.get(index).getSong());
        m_Artist.setText(m_list.get(index).getArtist());
        m_Album.setText(m_list.get(index).getAlbum());
        String artPath = AudioTool.getInstance().getAlbumArt(this, m_list.get(index).getAlbumId());
        if (null == artPath){
            m_ArtWork.setImageResource(R.drawable.ic_avatar2);
        }else {
            m_ArtWork.setImageBitmap(AudioTool.getInstance().compressedBitmap(artPath, 200, 200));
        }
        handler.removeMessages(0);
        m_SeekBar.setProgress(0);
        playMusic(true);

    }

    private void nextMusic(){
        if (null == m_list || 0 == m_list.size()){
            return;
        }

        if (m_list.size() - 1 == index){
            index = -1;
        }
            index ++;
            m_Song.setText(m_list.get(index).getSong());
            m_Artist.setText(m_list.get(index).getArtist());
            m_Album.setText(m_list.get(index).getAlbum());
        String artPath = AudioTool.getInstance().getAlbumArt(this, m_list.get(index).getAlbumId());
        if (null == artPath){
            m_ArtWork.setImageResource(R.drawable.ic_avatar2);
        }else {
            m_ArtWork.setImageBitmap(AudioTool.getInstance().compressedBitmap(artPath, 200, 200));
        }
            handler.removeMessages(0);
            m_SeekBar.setProgress(0);
            playMusic(true);
    }

    private void sendToMessage(){
        Message msgs = new Message();
        msgs.what = 0;
        handler.sendMessageDelayed(msgs, 1000);
    }

    private class MyOnClick implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.next:
                    nextMusic();
                    break;
                case R.id.pre:
                    preMusic();
                    break;
                case R.id.playpause:
                    if (isplay){
                        pauseMusic();
                    }else {
                        playMusic();
                    }
                    break;
                case R.id.setting:
                    m_PopUpWindowHelper.setM_AudioListener(new MySongPlayMode());
                   if (m_PopUpWindowHelper.isShowing()){
                       m_PopUpWindowHelper.hide();
                   }else {
                       ArrayList<String> listData = new ArrayList<String>();
                       listData.add("单曲循环");
                       listData.add("全曲循环");
                       listData.add("音量调整");
                       listData.add("ssss2");
                       listData.add("ssss3");
                       listData.add("ssss4");
                       listData.add("ssss5");
                       listData.add("ssss6");
                       listData.add("ssss7");
                       listData.add("ssss8");
                       m_PopUpWindowHelper.init(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT, m_PopUpWindowHelper.setListView(AudioActivity.this, listData),
                               v);

                   }

                    break;
                case R.id.list_music:
                    m_PopUpWindowHelper.setM_AudioListener(new MySelectMusic());
                    if (m_PopUpWindowHelper.isShowing()){
                        m_PopUpWindowHelper.hide();
                    }else {
                        m_PopUpWindowHelper.init(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT, m_PopUpWindowHelper.setMuiscList(AudioActivity.this, m_list),
                                v);

                    }
                    break;
                default:
                    break;
            }
        }
    }

    private void setSeekBarListener(){
    m_SeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            Log.i(TAG, TAG + "setOnSeekBarChangeListener");
            if (null != mediaPlayer && fromUser){
                handler.removeMessages(0);
                m_PlayPause.setBackgroundResource(R.drawable.pause);
                mediaPlayer.seekTo(progress);
                sendToMessage();
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            if (null != mediaPlayer){
                mediaPlayer.pause();
            }
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            if (null != mediaPlayer){
                mediaPlayer.start();
            }
        }
    });
    }

    private class MySongEndLinstener implements MusicPlayerService.NextMusicLinstener{

        @Override
        public void nextToMusic() {
            Log.i(TAG, TAG + "MySongEndLinstener");
            nextMusic();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
//        handler.removeMessages(0);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, TAG + "onDestroy");
        handler.removeMessages(0);
        if(null != m_MusicPlayerService){
            if (null != mediaPlayer){
                mediaPlayer.stop();
            }
            unbindService(conn);
        }

        m_Pre.setOnClickListener(null);
        m_Next.setOnClickListener(null);
        m_PlayPause.setOnClickListener(null);
        m_Setting.setOnClickListener(null);
        m_ListMusic.setOnClickListener(null);
        m_Dialog.dismiss();
    }

    private class MySongPlayMode implements PopUpWindowHelper.AudioListener{

        @Override
        public void songPlayMode(int postion) {
            switch (postion){
                case 0:
                    mediaPlayer.setLooping(true);
                    break;
                case 1:
                    mediaPlayer.setLooping(false);
                    break;
                case 2:
                    UIC_VoiceDialog.Builder builder = new UIC_VoiceDialog.Builder(AudioActivity.this);
                    m_Dialog = builder.create();
                    m_Dialog.setCanceledOnTouchOutside(true);
                    m_Dialog.show();
                    break;
                default:
                    break;
            }
        }

        @Override
        public void selectMusic(int postion) {

        }
    }

    private class MySelectMusic implements PopUpWindowHelper.AudioListener{

        @Override
        public void songPlayMode(int postion) {
        }

        @Override
        public void selectMusic(int postion) {
            if (null == m_list || 0 == m_list.size()){
                return;
            }

            m_Song.setText(m_list.get(postion).getSong());
            m_Artist.setText(m_list.get(postion).getArtist());
            m_Album.setText(m_list.get(postion).getAlbum());
            String artPath = AudioTool.getInstance().getAlbumArt(AudioActivity.this, m_list.get(postion).getAlbumId());
            if (null == artPath){
                m_ArtWork.setImageResource(R.drawable.ic_avatar2);
            }else {
                m_ArtWork.setImageBitmap(AudioTool.getInstance().compressedBitmap(artPath, 200, 200));
            }
            index = postion;
            playMusic(true);
        }
    }
}
