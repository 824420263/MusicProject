package com.example.percentlinearlayout.helper;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.percentlinearlayout.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wk on 2016/9/28.
 */

public abstract class ListBaseAdapter<T> extends BaseAdapter {
    private static final String TAG = "ListBaseAdapter";
    private ArrayList<T> m_Data = null;
    private Context context = null;

    public ListBaseAdapter(Context context, ArrayList<T> m_Data){
        this.context = context;
       this.m_Data = m_Data;
    }
    @Override
    public int getCount() {
        return m_Data.size();
    }

    @Override
    public Object getItem(int position) {
        return m_Data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

}
