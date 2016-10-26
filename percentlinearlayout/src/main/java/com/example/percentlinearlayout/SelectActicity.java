package com.example.percentlinearlayout;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by wk on 2016/10/10.
 */

public class SelectActicity extends AppCompatActivity {
    private Button m_PlayAudio = null;
    private Button m_PlayVideo = null;
    private Button m_PlayImage = null;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_select);
        init();
    }

    private void init(){
        m_PlayAudio = (Button) findViewById(R.id.play_audio);
        m_PlayVideo = (Button) findViewById(R.id.play_video);
        m_PlayImage = (Button) findViewById(R.id.play_image);
        MyOnClickListener onclick =  new MyOnClickListener();
        m_PlayAudio.setOnClickListener(onclick);
        m_PlayVideo.setOnClickListener(onclick);
        m_PlayImage.setOnClickListener(onclick);
    }

    private class MyOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Intent intent = null;
            switch (v.getId()){
                case R.id.play_audio:
                    intent = new Intent(SelectActicity.this, AudioActivity.class);
                    startActivity(intent);
                    break;
                case R.id.play_video:
                    intent = new Intent(SelectActicity.this, VideoActivity.class);
                    startActivity(intent);
                    break;
                case R.id.play_image:
                    intent = new Intent(SelectActicity.this, ImageActivity.class);
                    startActivity(intent);
                    break;
                default:
                    break;
            }
        }
    }
}
