package com.axolotl.yanews.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.axolotl.yanews.fragment.PageFragment;
import com.axolotl.yanews.utils.Constants;

import java.util.List;

/**
 * Created by axolotl on 16/9/2.
 */
public class NewsListPagerAdapter extends FragmentPagerAdapter {
    final int PAGE_COUNT = Constants.CATEGORY_COUNT;
    private Context context;

    public NewsListPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {
        return PageFragment.newInstance(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return Constants.CATEGORYS[position];
    }
}