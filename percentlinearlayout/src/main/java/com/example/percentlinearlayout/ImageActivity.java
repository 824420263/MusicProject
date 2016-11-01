package com.example.percentlinearlayout;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.percentlinearlayout.tool.AudioTool;

import java.util.ArrayList;

public class ImageActivity extends AppCompatActivity {
    private static final String TAG = "ImageActivity";
    private static final int SELECT_IMAGE_TIME = 7000;
    private ImageView mImageArtWork = null;
    private ImageView mImagePre = null;
    private ImageView mImageNext = null;
    private ImageView mImagePlayPause = null;
    private ArrayList<String> mPaths = new ArrayList<String> ();
    private int index = 0;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    nextImage();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        initView();
        initData();
    }

    private void initView(){
        mImageArtWork = (ImageView) findViewById(R.id.image_artwork);
        mImagePre = (ImageView) findViewById(R.id.image_pre);
        mImageNext = (ImageView) findViewById(R.id.image_next);
        mImagePlayPause = (ImageView) findViewById(R.id.image_playpause);
        MyOnClick myOnClick = new MyOnClick();
        mImageNext.setOnClickListener(myOnClick);
        mImagePre.setOnClickListener(myOnClick);
        mImagePlayPause.setOnClickListener(myOnClick);

    }

    private void initData(){
        String[] projection = {
                MediaStore.Images.Media.SIZE, MediaStore.Images.Media.DATA
        };

        Cursor cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, null, null, null);
        if (null != cursor && cursor.getCount() > 0){
            while (cursor.moveToNext()){
                String size = cursor.getString(0);
                String path = cursor.getString(1);
//                if (!path.contains("Camera") && Integer.parseInt(size) > 100000){
//                    mPaths.add(path);
//                }
                if (path.contains("Camera")){
                    mPaths.add(path);
                }
            }
            cursor.close();

            if (0 == mPaths.size()){
                return;
            }
            if (null == mPaths.get(0)){
                mImageArtWork.setImageResource(R.drawable.ic_avatar2);
            }
            Bitmap bitmap = AudioTool.getInstance().compressedBitmap(mPaths.get(0), 400, 300);
            mImageArtWork.setImageBitmap(bitmap);

        }
        handler.sendEmptyMessageDelayed(0, SELECT_IMAGE_TIME);
    }

    private class MyOnClick implements View.OnClickListener{

        @Override
        public void onClick(View v) {
             switch (v.getId()){
                 case R.id.image_pre:
                     preImage();
                     break;
                 case R.id.image_next:
                     nextImage();
                     break;
                 case R.id.image_playpause:
                     playpauseImage();
                     break;
                 default:
                     break;

            }
        }
    }

    private void preImage(){
        if (null == mPaths || 0 == mPaths.size()){
            return;
        }
        handler.removeMessages(0);
        index --;
        if (-1 == index){
            index = mPaths.size() - 1;
        }

        if (null == mPaths.get(index)){
            mImageArtWork.setImageResource(R.drawable.ic_avatar2);
        }
        Bitmap bitmap = AudioTool.getInstance().compressedBitmap(mPaths.get(index), 400, 300);
        mImageArtWork.setImageBitmap(bitmap);
        handler.sendEmptyMessageDelayed(0, SELECT_IMAGE_TIME);
    }

    private void nextImage(){
        if (null == mPaths || 0 == mPaths.size()){
            return;
        }
        handler.removeMessages(0);
        if (mPaths.size() - 1 == index){
            index = -1;
        }
        index ++;

        if (null == mPaths.get(index)){
            mImageArtWork.setImageResource(R.drawable.ic_avatar2);
        }
        Bitmap bitmap = AudioTool.getInstance().compressedBitmap(mPaths.get(index), 400, 300);
        mImageArtWork.setImageBitmap(bitmap);
        handler.sendEmptyMessageDelayed(0, SELECT_IMAGE_TIME);
    }

    private void playpauseImage(){

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeMessages(0);
    }
}
