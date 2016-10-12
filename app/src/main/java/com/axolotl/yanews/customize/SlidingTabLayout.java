/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.axolotl.yanews.customize;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.axolotl.yanews.R;
import com.axolotl.yanews.utils.TextViewUtil;


/**
 * To be used with ViewPager to provide a tab indicator component which give constant feedback as to
 * the user's scroll progress.
 * <p>
 * To use the component, simply add it to your view hierarchy. Then in your
 * {@link android.app.Activity} or {@link android.support.v4.app.Fragment} call
 * {@link #setViewPager(ViewPager)} providing it the ViewPager this layout is being used for.
 * <p>
 * The colors can be customized in two ways. The first and simplest is to provide an array of colors
 * via {@link #setSelectedIndicatorColors(int...)} and {@link #setDividerColors(int...)}. The
 * alternative is via the {@link TabColorizer} interface which provides you complete control over
 * which color is used for any individual position.
 * <p>
 * The views used as tabs can be customized by calling {@link #setCustomTabView(int, int)},
 * providing the layout ID of your custom layout.
 */
public class SlidingTabLayout extends HorizontalScrollView {

    /**
     * Allows complete control over the colors drawn in the tab layout. Set with
     * {@link #setCustomTabColorizer(TabColorizer)}.
     */
    public interface TabColorizer {

        /**
         * @return return the color of the indicator used when {@code position} is selected.
         */
        int getIndicatorColor(int position);

        /**
         * @return return the color of the divider drawn to the right of {@code position}.
         */
        int getDividerColor(int position);

    }

    /**
     * 不知道是什么的数值dp
     */
    private static final int TITLE_OFFSET_DIPS = 24;
    private static final int TAB_VIEW_PADDING_DIPS = 10;
    /**
     * tab字体大小
     */
    public static int mTab_text_size_sp = 12;

    private int mTitleOffset;

    private int mTabViewLayoutId;
    private int mTabViewTextViewId;
    private View[] mTabViews;
    private boolean mDistributeEvenly;
    /**
     * 记录是否使用粗体
     */
    private boolean mBold;
    private boolean mChangeTextColor;
    private int mNormalColor;
    private int mHightColor;
    private int mBackgroundResource = R.drawable.btn_pressed_selector;
    private ViewPager mViewPager;
    private ViewPager.OnPageChangeListener mViewPagerPageChangeListener;

    private final SlidingTabStrip mTabStrip;

    private static int[] TAB_TEXT_COLORS = null;

    public SlidingTabLayout(Context context) {
        this(context, null);
    }

    public SlidingTabLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlidingTabLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        // Disable the Scroll Bar
        setHorizontalScrollBarEnabled(false);
        // Make sure that the Tab Strips fills this View
        setFillViewport(true);

        mTitleOffset = (int) (TITLE_OFFSET_DIPS * getResources().getDisplayMetrics().density);
        mTabStrip = new SlidingTabStrip(context);
        addView(mTabStrip, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    }

    /**
     * 设置字体大小
     * @param size sp
     */
    public void setTabTextSize(int size) {
        mTab_text_size_sp = size;
    }
    
    public void setTabTextBold(boolean bold) {
        mBold = bold;
    }
    
    public void setTabSelector(int s) {
        mBackgroundResource = s;
        mTabStrip.removeAllViews();

        if (mViewPager != null) {
            populateTabStrip();
        }
    }
    
    /**
     * 设置tab字体颜色变化
     * @param normal
     * @param highlight
     */
    public void setTabTextColor(int normal, int highlight) {
        mChangeTextColor = true;
        mNormalColor = normal;
        mHightColor = highlight;
        mTabStrip.removeAllViews();

        if (mViewPager != null) {
            populateTabStrip();
        }
    }
    
    /**
     * Set the custom {@link TabColorizer} to be used.
     *
     * If you only require simple custmisation then you can use
     * {@link #setSelectedIndicatorColors(int...)} and {@link #setDividerColors(int...)} to achieve
     * similar effects.
     */
    public void setCustomTabColorizer(TabColorizer tabColorizer) {
        mTabStrip.setCustomTabColorizer(tabColorizer);
    }

    /**
     * Sets the colors to be used for indicating the selected tab. These colors are treated as a
     * circular array. Providing one color will mean that all tabs are indicated with the same color.
     * @param colors 颜色循环数组
     */
    public void setSelectedIndicatorColors(int... colors) {
        mTabStrip.setSelectedIndicatorColors(colors);
    }

    /**
     * Sets the colors to be used for tab dividers. These colors are treated as a circular array.
     * Providing one color will mean that all tabs are indicated with the same color.
     * @param colors 颜色循环数组
     */
    public void setDividerColors(int... colors) {
        mTabStrip.setDividerColors(colors);
    }

    /**
     * 设置indicator的高度
     * @param height px值
     */
    public void setIndicatorHeight(int height) {
        mTabStrip.setSelectedIndicatorHeight(height);
    }
    
    /**
     * Set the {@link ViewPager.OnPageChangeListener}. 使用 {@link SlidingTabLayout} 必须
     * 通过这个方法设置 {@link ViewPager.OnPageChangeListener} . This is so
     * that the layout can update it's scroll position correctly.
     *
     * @see ViewPager#setOnPageChangeListener(ViewPager.OnPageChangeListener)
     */
    public void setOnPageChangeListener(ViewPager.OnPageChangeListener listener) {
        mViewPagerPageChangeListener = listener;
    }

    /**
     * 设置tab平均分布
     * @param distributeEvenly
     */
    public void setDistributeEvenly(boolean distributeEvenly) {
        mDistributeEvenly = distributeEvenly;
    }
    
    /**
     * Set the custom layout to be inflated for the tab views.
     *
     * @param layoutResId Layout id to be inflated
     * @param textViewId id of the {@link TextView} in the inflated view
     */
    public void setCustomTabView(int layoutResId, int textViewId) {
        mTabViewLayoutId = layoutResId;
        mTabViewTextViewId = textViewId;
    }

    /**
     * 设置viewpager, 务必在最后再调用
     * Sets the associated view pager. Note that the assumption here is that the pager content
     * (number of tabs and tab titles) does not change after this call has been made.
     */
    public void setViewPager(ViewPager viewPager) {
        mTabStrip.removeAllViews();

        mViewPager = viewPager;
        if (viewPager != null) {
            viewPager.setOnPageChangeListener(new InternalViewPagerListener());
            populateTabStrip();
        }
    }

    /**
     * 为tab创建默认view. 自定义tab view没有设置时会被调用
     * {@link #setCustomTabView(int, int)}.
     */
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    protected TextView createDefaultTabView(Context context) {
        TextView textView = new TextView(context, null, 0);
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, mTab_text_size_sp);
        if (mBold) {
            textView.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        } else {
            textView.setTypeface(Typeface.DEFAULT, Typeface.NORMAL);
        }
        if (mChangeTextColor) {
            textView.setTextColor(mNormalColor);
        }
        textView.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT));

        if (mBackgroundResource != 0) {
            textView.setBackgroundResource(mBackgroundResource);
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                // If we're running on Honeycomb or newer, then we can use the Theme's
                // selectableItemBackground to ensure that the View has a pressed state
                TypedValue outValue = new TypedValue();
                getContext().getTheme().resolveAttribute(android.R.attr.selectableItemBackground,
                        outValue, true);
                textView.setBackgroundResource(outValue.resourceId);
            }
        }

//wtf
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
//            // If we're running on ICS or newer, enable all-caps to match the Action Bar tab style
//            textView.setAllCaps(true);
//        }

        int padding = (int) (TAB_VIEW_PADDING_DIPS * getResources().getDisplayMetrics().density);
        textView.setPadding(padding, 0, padding, 0);
        return textView;
    }

    /**
     * 使用tab填充tabstrip
     */
    private void populateTabStrip() {
        final PagerAdapter adapter = mViewPager.getAdapter();
        final OnClickListener tabClickListener = new TabClickListener();
        mTabViews = null;
        mTabViews = new View[adapter.getCount()];
        for (int i = 0; i < adapter.getCount(); i++) {
            View tabView = null;
            TextView tabTitleView = null;

            if (mTabViewLayoutId != 0) {
                // If there is a custom tab view layout id set, try and inflate it
                tabView = LayoutInflater.from(getContext()).inflate(mTabViewLayoutId, mTabStrip,
                        false);
                tabTitleView = (TextView) tabView.findViewById(mTabViewTextViewId);
            }

            if (tabView == null) {
                tabView = createDefaultTabView(getContext());
            }

            if (tabTitleView == null && TextView.class.isInstance(tabView)) {
                tabTitleView = (TextView) tabView;
            }
            
            if (mDistributeEvenly) {
                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) tabView.getLayoutParams();
                lp.width = 0;
                lp.weight = 1;
            }

            tabTitleView.setText(adapter.getPageTitle(i));
            tabView.setOnClickListener(tabClickListener);
            mTabViews[i] = tabView;
            mTabStrip.addView(tabView);
        }
    }

    public View getTabAt(int index) {
        return mTabViews[index];
    }
    
    /**
     * 为了将viewpager设置成第一个
     */
    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        if (mViewPager != null) {
            scrollToTab(mViewPager.getCurrentItem(), 0);
        }
    }

    private int mLastScrollTo;

    private void scrollToTab(int tabIndex, int positionOffset) {
        final int tabStripChildCount = mTabStrip.getChildCount();
        if (tabStripChildCount == 0 || tabIndex < 0 || tabIndex >= tabStripChildCount) {
            return;
        }

        View selectedChild = mTabStrip.getChildAt(tabIndex);
        if (selectedChild != null && selectedChild.getMeasuredWidth() != 0) {

            int targetScrollX = ((positionOffset + selectedChild.getLeft()) - getWidth() / 2) + selectedChild.getWidth() / 2;

            if (targetScrollX != mLastScrollTo) {
                scrollTo(targetScrollX, 0);
                mLastScrollTo = targetScrollX;
            }
        }
    }


    /**
     * 设置是否显示bottom border 也就是 ________
     * @param enable
     */
    public void setBottomBorderEnbaled(boolean enable) {
        mTabStrip.setBottomBorderEnabled(enable);
    }

    /**
     * 设置是否显示divider 也就是 |
     * @param enable
     */
    public void setDividerEnabled(boolean enable) {
        mTabStrip.setDividerEnabled(enable);
    }
    
    private class InternalViewPagerListener implements ViewPager.OnPageChangeListener {
        private int mScrollState;
        
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            int tabStripChildCount = mTabStrip.getChildCount();
            if ((tabStripChildCount == 0) || (position < 0) || (position >= tabStripChildCount)) {
                return;
            }
            mTabStrip.onViewPagerPageChanged(position, positionOffset);

            View selectedTitle = mTabStrip.getChildAt(position);
            int selectedOffset = (selectedTitle == null) ? 0 : selectedTitle.getWidth();
            int nextTitlePosition = position + 1;
            View nextTitle = mTabStrip.getChildAt(nextTitlePosition);
            int nextOffset = (nextTitle == null) ? 0 : nextTitle.getWidth();
            int extraOffset = (int)(0.5F * (positionOffset * (float)(selectedOffset + nextOffset)));
            scrollToTab(position, extraOffset);

            if (mViewPagerPageChangeListener != null) {
                mViewPagerPageChangeListener.onPageScrolled(position, positionOffset,
                        positionOffsetPixels);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            mScrollState = state;
            if (mScrollState == ViewPager.SCROLL_STATE_IDLE) {
                int count = mTabStrip.getChildCount();
                int curItem = mViewPager.getCurrentItem();
                for (int i = 0; i<count; i++) {
                    //设置文字颜色和背景色的循环 by neo
                    View tabView = SlidingTabLayout.this.getTabAt(i);
                    if (i == curItem) {
                        TextViewUtil.setTextColor(tabView, mNormalColor);
                        tabView.setBackgroundColor(getColorByIndex(i));
                    } else {
                        TextViewUtil.setTextColor(tabView, getColorByIndex(i));
                        tabView.setBackgroundColor(Color.TRANSPARENT);
                    }
                }
            }
            if (mViewPagerPageChangeListener != null) {
                mViewPagerPageChangeListener.onPageScrollStateChanged(state);
            }
        }

        @Override
        public void onPageSelected(int position) {
            int count = mTabStrip.getChildCount();
            for (int i = 0; i<count; i++) {
                //设置文字颜色和背景色的循环 by neo
                View tabView = SlidingTabLayout.this.getTabAt(i);
                if (i == position) {
//                    Statistics.statsClickTab(TextViewUtil.getText(tabView));
                    TextViewUtil.setTextColor(tabView, mNormalColor);
                    tabView.setBackgroundColor(getColorByIndex(i));
                } else {
                    TextViewUtil.setTextColor(tabView, getColorByIndex(i));
                    tabView.setBackgroundColor(Color.TRANSPARENT);
                }
            }
            if (mScrollState == ViewPager.SCROLL_STATE_IDLE) {
                mTabStrip.onViewPagerPageChanged(position, 0f);
                scrollToTab(position, 0);
            }

            if (mViewPagerPageChangeListener != null) {
                mViewPagerPageChangeListener.onPageSelected(position);
            }
        }

    }

    private class TabClickListener implements OnClickListener {
        @Override
        public void onClick(View v) {
            for (int i = 0; i < mTabStrip.getChildCount(); i++) {
                if (v == mTabStrip.getChildAt(i)) {
                    mViewPager.setCurrentItem(i);
                    return;
                }
            }
        }
    }
    
    public void setCurrentItem(int index) {
        mViewPager.setCurrentItem(index, false);
        int count = mTabStrip.getChildCount();
        int curItem = mViewPager.getCurrentItem();
        for (int i = 0; i<count; i++) {
            //设置文字颜色和背景色的循环 by neo
            View tabView = SlidingTabLayout.this.getTabAt(i);
            if (i == curItem) {
                TextViewUtil.setTextColor(tabView, mNormalColor);
                tabView.setBackgroundColor(getColorByIndex(i));
            } else {
                TextViewUtil.setTextColor(tabView, getColorByIndex(i));
                tabView.setBackgroundColor(Color.TRANSPARENT);
            }
        }
    }

    /**
     * 支持颜色循环
     * @param index
     */
    private int getColorByIndex(int index) {
        if (index < 0) {
            index = 0;
        }
        if (null == TAB_TEXT_COLORS) {
            TAB_TEXT_COLORS = new int[]{
                    getResources().getColor(R.color.orangered),
                    getResources().getColor(R.color.orange),
                    getResources().getColor(R.color.limegreen),
                    getResources().getColor(R.color.deepskyblue),
                    getResources().getColor(R.color.blueviolet)
            };
        }
        int color = TAB_TEXT_COLORS[index % TAB_TEXT_COLORS.length];

        return color;
    }
}
