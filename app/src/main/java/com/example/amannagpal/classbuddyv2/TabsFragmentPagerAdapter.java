package com.example.amannagpal.classbuddyv2;

import android.content.Context;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;




// src = https://android.jlelse.eu/tablayout-and-viewpager-in-your-android-app-738b8840c38a


public class TabsFragmentPagerAdapter extends FragmentPagerAdapter {

    private Context mContext;

    public TabsFragmentPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    // This determines the fragment for each tab
    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new RecorderFragment();
        } else if (position == 1){
            return new FilesFragment();
        } else {
            return new RecorderFragment();
        }
    }

    // This determines the number of tabs
    @Override
    public int getCount() {
        return 2;
    }

    // This determines the title for each tab
    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        switch (position) {
            case 0:
                return mContext.getString(R.string.recorder);
            case 1:
                return mContext.getString(R.string.files);
            default:
                return null;
        }
    }



}
