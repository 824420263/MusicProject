package com.example.percentlinearlayout.util;

import android.app.Dialog;
import android.app.Service;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;

import com.example.percentlinearlayout.R;
import com.example.percentlinearlayout.StaticDEF;
import com.example.percentlinearlayout.tool.AudioTool;

/**
 * Created by wk on 2016/10/27.
 */

public class UIC_VoiceDialog extends Dialog {

    public UIC_VoiceDialog(Context context) {
        super(context);
    }

    public UIC_VoiceDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    public static class Builder{
        private Context context = null;
        private Window mWindow = null;
        private WindowManager.LayoutParams lp = null;
        private AudioManager mAudioManager  = null;
        private SeekBar mSeekBar = null;
        private FrameLayout mFrameLayout = null;
        private ImageView imageView = null;
        private Button mVoiceAdd = null;
        private Button mVoiceLess = null;
        private static final String TAG ="Builder";
        public Builder(Context context){
            this.context = context;
        }

        public UIC_VoiceDialog create(){
            Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),R.drawable.ic_avatar2);
            Bitmap dialogBitmap = AudioTool.getInstance().fillet(StaticDEF.ALL, bitmap, 45);
            UIC_VoiceDialog dialog = new UIC_VoiceDialog(context, R.style.VoiceDialog);
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = inflater.inflate(R.layout.voice_dialog, null);
            mFrameLayout = (FrameLayout) layout.findViewById(R.id.voice_dialog_frag);
            imageView = (ImageView) layout.findViewById(R.id.voice_dialog_background);
            imageView.setImageBitmap(dialogBitmap);
            imageView.setAlpha(0.3f);
            mVoiceLess= (Button) layout.findViewById(R.id.voice_dialog_less);
            MyOnClick onClick = new MyOnClick();
            mVoiceLess.setOnClickListener(onClick);
            mVoiceLess.setAlpha(0.3f);
            mVoiceAdd = (Button) layout.findViewById(R.id.voice_dialog_add);
            mVoiceAdd.setOnClickListener(onClick);
            mVoiceAdd.setAlpha(0.3f);
            mSeekBar = (SeekBar) layout.findViewById(R.id.voice_dialog_seekbar);
            mAudioManager = (AudioManager) context.getSystemService(Service.AUDIO_SERVICE);
            int maxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
            int currentVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
            mSeekBar.setMax(maxVolume);
            mSeekBar.setProgress(currentVolume);

            mWindow = dialog.getWindow();
            lp = mWindow.getAttributes();
            mWindow.setGravity(Gravity.LEFT | Gravity.TOP);
            lp.x = 0;
            lp.y = 0;
//          lp.alpha = 0.2f;
            mWindow.setAttributes(lp);
            imageView.setOnTouchListener(new MyTouchLintener());
            dialog.setContentView(layout);
            return dialog;
        }

        private class MyOnClick implements View.OnClickListener{

            @Override
            public void onClick(View v) {
             switch (v.getId()){
                 case R.id.voice_dialog_add:
                     mAudioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_RAISE, AudioManager.FLAG_SHOW_UI);
                    break;
                 case R.id.voice_dialog_less:
                     mAudioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_LOWER, AudioManager.FLAG_SHOW_UI);
                     break;
                 default:
                     break;
               }
                int currentVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                mSeekBar.setProgress(currentVolume);
            }
        }

       private class MyTouchLintener implements View.OnTouchListener{

           int startX = -1;
           int startY = -1;
           @Override
           public boolean onTouch(View v, MotionEvent event) {
               switch (event.getAction()){
                   case MotionEvent.ACTION_DOWN:
                       startX = (int) event.getRawX();
                       startY = (int) event.getRawY();
                       lp.x = startX;
                       lp.y = startY;
                       mWindow.setAttributes(lp);
                       break;
                   case MotionEvent.ACTION_MOVE:
                       int setX = (int) event.getRawX();
                       int setY = (int) event.getRawY();
                       lp.x += setX - startX;
                       lp.y += setY - startY;
                       mWindow.setAttributes(lp);

                       Log.i(TAG, TAG + "setX :" + setX + "setY :" + setY );
                       Log.i(TAG, TAG + "startX :" + startX + "startY :" + startY);
                       startX = setX;
                       startY = setY;
                       break;
                   case MotionEvent.ACTION_UP:
                       break;
                   default:
                       break;
               }
               return true;
           }
       }
    }

}
