package com.example.percentlinearlayout.helper;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by wk on 2016/9/30.
 */

public class BaseFragment extends Fragment {
    private static String Layout_Id = "layoutid";
    public static BaseFragment getInstance(int layoutId){
        BaseFragment fragment = new BaseFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(Layout_Id, layoutId);
        fragment.setArguments(bundle);
        return fragment;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getArguments().getInt(Layout_Id),container,false);
        return view;
    }
}
