package com.example.percentlinearlayout.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.TextView;

import com.example.percentlinearlayout.R;
import com.example.percentlinearlayout.helper.ListBaseAdapter;

import java.util.ArrayList;

/**
 * Created by wk on 2016/10/25.
 */

public class SettingAdapter<T> extends ListBaseAdapter{
    private ArrayList<String> m_Data = null;
    private Context context = null;

    public SettingAdapter(Context context, ArrayList m_Data) {
        super(context, m_Data);
        this.m_Data = m_Data;
        this.context = context;

    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder = null;
        if (null == convertView){
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);

            convertView.setLayoutParams(new AbsListView.LayoutParams( AbsListView.LayoutParams.MATCH_PARENT, 200));
            holder = new Holder();
            holder.tv = (TextView) convertView.findViewById(R.id.list_item_tv);
            convertView.setTag(holder);
        }else {
            holder = (Holder) convertView.getTag();
        }
        holder.tv.setText(m_Data.get(position));
        return convertView;
    }

    private class Holder{
        private TextView tv = null;

    }
}
