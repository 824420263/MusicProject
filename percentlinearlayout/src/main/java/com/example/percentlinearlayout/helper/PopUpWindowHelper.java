package com.example.percentlinearlayout.helper;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.percentlinearlayout.MusicMedia;
import com.example.percentlinearlayout.R;
import com.example.percentlinearlayout.adapter.MusicAdapter;
import com.example.percentlinearlayout.adapter.SettingAdapter;

import java.util.ArrayList;

/**
 * Created by wk on 2016/9/28.
 */

public class PopUpWindowHelper {
    private static PopUpWindowHelper helper = null;
    private PopupWindow m_PopupWindow = null;
    private static final String TAG = "PopUpWindowHelper";
    private PopUpWindowHelper(){

    }
    public static PopUpWindowHelper getInstance(){
        if (null == helper){
            helper = new PopUpWindowHelper();
        }
        return helper;
    }

    public void init(int width, int height, View view, View parent){
        m_PopupWindow = new PopupWindow();
        m_PopupWindow.setWidth(width);
        m_PopupWindow.setHeight(height);
        m_PopupWindow.setFocusable(true);//可以获取焦点
        m_PopupWindow.setOutsideTouchable(true);//设置外围可以点击
        //只能设置drawable(color --> drawable)
        m_PopupWindow.setBackgroundDrawable(new ColorDrawable(Color.GRAY));
        m_PopupWindow.setContentView(view);
        m_PopupWindow.showAtLocation(parent, Gravity.BOTTOM, 0, 0);

    }

    public View setListView(Context context, ArrayList<String> data){
        View view = LayoutInflater.from(context).inflate(R.layout.setting, null);
        ListView lv = (ListView) view.findViewById(R.id.listview);
        ListBaseAdapter adapter = new SettingAdapter<String>(context, data);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //显示以后隐藏
                m_AudioListener.songPlayMode(position);
                hide();
            }
        });
        return view;
    }

    public View setMuiscList(Context context, ArrayList<MusicMedia> data){
        View view = LayoutInflater.from(context).inflate(R.layout.setting, null);
        TextView tv = (TextView) view.findViewById(R.id.setting_tv);
        tv.setVisibility(View.GONE);
        ListView lv = (ListView) view.findViewById(R.id.listview);
        ListBaseAdapter adapter = new MusicAdapter<MusicMedia>(context, data);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //显示以后隐藏
                m_AudioListener.selectMusic(position);
                hide();
            }
        });
        return view;
    }

    /**
     * 隐藏popUpWindow
     */
    public void hide(){
        if(m_PopupWindow != null)
            m_PopupWindow.dismiss();
    }

    /**
     * 当前的PopUpWindow是否处于显示状态
     * @return   true 正在显示中
     */
    public  boolean isShowing(){
        return  m_PopupWindow != null && m_PopupWindow.isShowing() ?true:false;
    }

    private AudioListener m_AudioListener = null;
    public interface AudioListener{
        void songPlayMode(int postion);
        void selectMusic(int postion);
    }

    public void setM_AudioListener(AudioListener m_AudioListener) {
        this.m_AudioListener = m_AudioListener;
    }
}
