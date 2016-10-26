package com.example.percentlinearlayout.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.percentlinearlayout.MusicMedia;
import com.example.percentlinearlayout.R;
import com.example.percentlinearlayout.helper.ListBaseAdapter;
import com.example.percentlinearlayout.tool.AudioTool;

import java.util.ArrayList;

/**
 * Created by wk on 2016/10/25.
 */

public class MusicAdapter<T> extends ListBaseAdapter{
    private ArrayList<MusicMedia> m_Data = null;
    private Context context = null;

    public MusicAdapter(Context context, ArrayList m_Data) {
        super(context, m_Data);
        this.m_Data = m_Data;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyHodler hodler = null;
        if (null == convertView){
            convertView = LayoutInflater.from(context).inflate(R.layout.music_listitem, parent, false);
            hodler = new MyHodler();
            hodler.imageView = (ImageView) convertView.findViewById(R.id.music_listitem_iv);
            hodler.textView = (TextView) convertView.findViewById(R.id.music_listitem_tv);
            convertView.setTag(hodler);
        }else {
            hodler = (MyHodler) convertView.getTag();
        }
        String artPath = AudioTool.getInstance().getAlbumArt(context, m_Data.get(position).getAlbumId());
        if (null == artPath){
            hodler.imageView.setImageResource(R.drawable.ic_avatar2);
        }else {
            hodler.imageView.setImageBitmap(AudioTool.getInstance().compressedBitmap(artPath, 30, 30));
        }

        hodler.textView.setText(m_Data.get(position).getSong());

        return convertView;
    }

    private class MyHodler{
        private TextView textView = null;
        private ImageView imageView = null;
    }
}
