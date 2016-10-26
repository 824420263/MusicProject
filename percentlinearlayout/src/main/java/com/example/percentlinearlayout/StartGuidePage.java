package com.example.percentlinearlayout;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.percentlinearlayout.helper.BaseFragment;

public class StartGuidePage extends AppCompatActivity {
    private ViewPager m_ViewPager = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_guide_page);
        m_ViewPager = (ViewPager) findViewById(R.id.guide_viewpager);
//        m_ViewPager.setAdapter(new MyFragmentAdapter(BaseFragment.getInstance(R.layout.)));
    }

private class MyFragmentAdapter extends FragmentPagerAdapter{

    public MyFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return null;
    }

    @Override
    public int getCount() {
        return 0;
    }
}
}
