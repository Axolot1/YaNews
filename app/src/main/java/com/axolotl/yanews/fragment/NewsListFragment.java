package com.axolotl.yanews.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.axolotl.yanews.R;
import com.axolotl.yanews.adapter.NewsListPagerAdapter;
import com.axolotl.yanews.customize.SlidingTabLayout;
import com.axolotl.yanews.utils.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by axolotl on 16/9/2.
 */
public class NewsListFragment extends Fragment {


    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.slidingtab)
    SlidingTabLayout tabLayout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_list_news, container, false);
        ButterKnife.bind(this, v);
        setupTabAndPager();
        return v;
    }

    private void setupTabAndPager() {
        viewpager.setAdapter(new NewsListPagerAdapter(getFragmentManager(),
                getActivity()));
        tabLayout.setBottomBorderEnbaled(true);
        tabLayout.setDividerEnabled(false);
        tabLayout.setDistributeEvenly(false);
        tabLayout.setIndicatorHeight(2);
        tabLayout.setTabTextSize(15);
        tabLayout.setTabTextColor(
                getResources().getColor(R.color.white),
                getResources().getColor(R.color.red));
        tabLayout.setSelectedIndicatorColors(Constants.THEME_COLORS);
        tabLayout.setDividerColors(Constants.THEME_COLORS);
        tabLayout.setViewPager(viewpager);
        tabLayout.setCurrentItem(0);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}
