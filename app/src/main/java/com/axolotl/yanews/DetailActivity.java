package com.axolotl.yanews;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;

import com.axolotl.yanews.adapter.DetailPageAdapter;
import com.axolotl.yanews.customize.FabTransform;
import com.axolotl.yanews.event.RefreshComment;
import com.axolotl.yanews.retrofit.entity.News;
import com.axolotl.yanews.utils.Prefs;

import org.greenrobot.eventbus.EventBus;
import org.parceler.Parcels;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by axolotl on 16/9/26.
 */

public class DetailActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {

    public static final String EXTRA_NEWS = "extra_news";
    public static final String EXTRA_COLOR = "extra_color";
    public static final String EXTRA_CATEGORY = "extra_category";
    public static final int REQUEST_LOGIN = 1001;
    public static final int REQUEST_COMMENT = 1002;

    @BindView(R.id.fab_add)
    FloatingActionButton fabAdd;

    private News mNews;
    private int mColor;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.sliding_tabs)
    TabLayout slidingTabs;
    @BindView(R.id.appbar)
    AppBarLayout appbar;
    @BindView(R.id.viewpager)
    ViewPager viewpager;

    @Inject
    Prefs mPrefer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        mNews = Parcels.unwrap(getIntent().getParcelableExtra(EXTRA_NEWS));
        mColor = getIntent().getIntExtra(EXTRA_COLOR, getResources().getColor(R.color.colorPrimaryDark));
        ButterKnife.bind(this);
        YaNewApp.getNetComponent(this).inject(this);
        setupView();

    }

    private void setupView() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getIntent().getStringExtra(EXTRA_CATEGORY));
        toolbar.setBackgroundColor(mColor);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DetailActivity.this.finish();
            }
        });
        viewpager.setAdapter(new DetailPageAdapter(getSupportFragmentManager(), mNews));
        viewpager.addOnPageChangeListener(this);
        slidingTabs.setupWithViewPager(viewpager);
        slidingTabs.setBackgroundColor(mColor);
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                launchLoginActivity();
                if (TextUtils.isEmpty(mPrefer.getAuthToken())) {
                    launchLoginActivity();
                } else {
                    launchCommentActivity();
                }
//                launchCommentActivity();

            }
        });
    }

    private void launchCommentActivity() {
        Intent intent = new Intent(this, AddCommentActivity.class);
        intent.putExtra(AddCommentActivity.EXTRA_LINK, mNews.getLink());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            FabTransform.addExtras(intent,
                    ContextCompat.getColor(this, R.color.colorAccent), R.drawable.fab_plus);
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this, fabAdd,
                    getString(R.string.transition_google_login));
            startActivityForResult(intent, REQUEST_COMMENT, options.toBundle());
        } else {
            startActivityForResult(intent, REQUEST_COMMENT);
        }

    }

    private void launchLoginActivity() {
        Intent intent = new Intent(DetailActivity.this, SignInActivity.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            FabTransform.addExtras(intent,
                    ContextCompat.getColor(this, R.color.colorAccent), R.drawable.fab_plus);
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this, fabAdd,
                    getString(R.string.transition_google_login));
            startActivityForResult(intent, REQUEST_LOGIN, options.toBundle());
        } else {
            startActivityForResult(intent, REQUEST_LOGIN);
        }
    }

    public static void startDetailActivity(Context context, News n, int color, String category) {
        Intent i = new Intent(context, DetailActivity.class);
        i.putExtra(EXTRA_NEWS, Parcels.wrap(n));
        i.putExtra(EXTRA_COLOR, color);
        i.putExtra(EXTRA_CATEGORY, category);
        context.startActivity(i);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_COMMENT && resultCode == RESULT_OK) {
            viewpager.setCurrentItem(1, true);
            EventBus.getDefault().post(new RefreshComment());
        } else if (requestCode == REQUEST_LOGIN && resultCode == RESULT_OK) {
            launchCommentActivity();
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        fabAdd.show();
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
