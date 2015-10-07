package com.example.stone.project63;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by stone on 2015/9/8.
 */
public class ViewPagerAdapter extends FragmentPagerAdapter {
    ArrayList<Fragment> Flist = new ArrayList<Fragment>();
    ArrayList<String> Tlist = new ArrayList<String>();

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }


    public void add(Fragment f,String t){
        Flist.add(f);
        Tlist.add(t);
    }
    @Override
    public Fragment getItem(int position) {
        return Flist.get(position);
    }

    @Override
    public CharSequence getPageTitle(int position){
        return Tlist.get(position);
    }

    @Override
    public int getCount() {
        return Flist.size();
    }
}
