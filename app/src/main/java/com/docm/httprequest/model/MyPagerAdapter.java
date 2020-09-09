package com.docm.httprequest.model;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.docm.httprequest.fragment.FragmentImage;
import com.docm.httprequest.fragment.FragmentText;

/**
 * Created by Romain Mathieu on 14/12/2015.
 *
 * Parameter and value associate for HTTP Request
 */
public class MyPagerAdapter extends FragmentStatePagerAdapter {

    public MyPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int pos) {
        switch(pos) {
            case 0: return FragmentText.newInstance();
            case 1: return FragmentImage.newInstance();
            default: return FragmentText.newInstance();
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        //return "Tab " + (position + 1);
        switch(position) {
            case 0: return "Text";
            case 1: return "Image";
            default:  return "Text";
        }
    }
}