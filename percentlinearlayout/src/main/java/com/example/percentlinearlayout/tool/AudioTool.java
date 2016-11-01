package com.example.percentlinearlayout.tool;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.util.Log;

import com.example.percentlinearlayout.StaticDEF;

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
        int width = -1;
        int height = -1;
        int sampleSize = 1;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        width = options.outWidth;
        height = options.outHeight;
        Log.i(TAG, TAG + "size :" + width * height);
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

//Dialog切图
    public Bitmap fillet(int type,Bitmap bitmap,int roundPx) {
        try {
            // 其原理就是：先建立一个与图片大小相同的透明的Bitmap画板
            // 然后在画板上画出一个想要的形状的区域。
            // 最后把源图片帖上。
            final int width = bitmap.getWidth();
            final int height = bitmap.getHeight();

            Bitmap paintingBoard = Bitmap.createBitmap(width,height, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(paintingBoard);
            canvas.drawARGB(Color.TRANSPARENT, Color.TRANSPARENT, Color.TRANSPARENT, Color.TRANSPARENT);

            final Paint paint = new Paint();
            paint.setAntiAlias(true);
            paint.setColor(Color.BLACK);

            if( StaticDEF.TOP == type ){
                clipTop(canvas,paint,roundPx,width,height);
            }else if( StaticDEF.LEFT == type ){
                clipLeft(canvas,paint,roundPx,width,height);
            }else if( StaticDEF.RIGHT == type ){
                clipRight(canvas,paint,roundPx,width,height);
            }else if( StaticDEF.BOTTOM == type ){
                clipBottom(canvas,paint,roundPx,width,height);
            }else{
                clipAll(canvas,paint,roundPx,width,height);
            }

            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            //帖子图
            final Rect src = new Rect(0, 0, width, height);
            final Rect dst = src;
            canvas.drawBitmap(bitmap, src, dst, paint);
            return paintingBoard;
        } catch (Exception exp) {
            return bitmap;
        }
    }

    private static void clipLeft(final Canvas canvas,final Paint paint,int offset,int width,int height){
        final Rect block = new Rect(offset,0,width,height);
        canvas.drawRect(block, paint);
        final RectF rectF = new RectF(0, 0, offset * 2 , height);
        canvas.drawRoundRect(rectF, offset, offset, paint);
    }

    private static void clipRight(final Canvas canvas,final Paint paint,int offset,int width,int height){
        final Rect block = new Rect(0, 0, width-offset, height);
        canvas.drawRect(block, paint);
        final RectF rectF = new RectF(width - offset * 2, 0, width , height);
        canvas.drawRoundRect(rectF, offset, offset, paint);
    }

    private static void clipTop(final Canvas canvas,final Paint paint,int offset,int width,int height){
        final Rect block = new Rect(0, offset, width, height);
        canvas.drawRect(block, paint);
        final RectF rectF = new RectF(0, 0, width , offset * 2);
        canvas.drawRoundRect(rectF, offset, offset, paint);
    }

    private static void clipBottom(final Canvas canvas,final Paint paint,int offset,int width,int height){
        final Rect block = new Rect(0, 0, width, height - offset);
        canvas.drawRect(block, paint);
        final RectF rectF = new RectF(0, height - offset * 2 , width , height);
        canvas.drawRoundRect(rectF, offset, offset, paint);
    }

    private static void clipAll(final Canvas canvas,final Paint paint,int offset,int width,int height){
        final RectF rectF = new RectF(0, 0, width , height);
        canvas.drawRoundRect(rectF, offset, offset, paint);
    }
}
