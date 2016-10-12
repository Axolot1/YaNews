package com.axolotl.yanews.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.axolotl.yanews.fragment.CommentFragment;
import com.axolotl.yanews.fragment.NewsContentFragment;
import com.axolotl.yanews.retrofit.entity.News;

/**
 * Created by axolotl on 16/9/26.
 */

public class DetailPageAdapter extends FragmentPagerAdapter {

    private String tabTitles[] = new String[]{"新闻", "评论"};
    private News mNews;

    public DetailPageAdapter(FragmentManager fm, News news) {
        super(fm);
        this.mNews = news;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0)
            return NewsContentFragment.newInstance(mNews);
        else if (position == 1)
            return CommentFragment.newInstance(mNews.getLink());
        return null;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }

    @Override
    public int getCount() {
        return tabTitles.length;
    }
}
