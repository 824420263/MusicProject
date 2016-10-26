package com.example.percentlinearlayout.tool;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;

/**
 * Created by wk on 2016/10/25.
 */

public class AudioTool {
    private static final String TAG = "AudioTool";
    private static AudioTool audioTool = null;
    private AudioTool(){

    }
    public static AudioTool getInstance(){
        if (null == audioTool){
            audioTool = new AudioTool();
        }
        return audioTool;
    }

    public Bitmap compressedBitmap(String path, int reqWidth, int reqHeight){
        Log.i(TAG, TAG + "path" + path);
        int width = -1;
        int height = -1;
        int sampleSize = 1;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        width = options.outWidth;
        height = options.outHeight;
        while (width / sampleSize > reqWidth || height / sampleSize > reqHeight) {
            sampleSize *= 2;
        }
        options.inSampleSize = sampleSize;
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(path, options);

    }

    public String getAlbumArt(Context context, int album_Id){
        String mUriAlbums = "content://media/external/audio/albums";
        String[] projection = new String[] { "album_art" };
        Cursor cur = context.getContentResolver().query(Uri.parse(mUriAlbums + "/" + Integer.toString(album_Id)), projection, null, null, null);
        String artPath = null;
        if (null != cur && cur.getCount() > 0){
            cur.moveToFirst();
            artPath = cur.getString(0);
            Log.i(TAG, TAG + "artpath :" + artPath);
        }
        cur.close();
        return artPath;
    }


}
