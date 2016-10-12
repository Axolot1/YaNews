package com.axolotl.yanews.fragment;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.axolotl.yanews.DetailActivity;
import com.axolotl.yanews.R;
import com.axolotl.yanews.YaNewApp;
import com.axolotl.yanews.adapter.NewsListAdapter;
import com.axolotl.yanews.customize.EmptyRecyclerView;
import com.axolotl.yanews.customize.MultiSwipeRefreshLayout;
import com.axolotl.yanews.dagger.DaggerPageComponent;
import com.axolotl.yanews.dagger.PageFraModule;
import com.axolotl.yanews.event.NetWorkError;
import com.axolotl.yanews.presenter.PageFragPresenter;
import com.axolotl.yanews.retrofit.entity.News;
import com.axolotl.yanews.utils.Constants;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.RealmResults;
import timber.log.Timber;

/**
 * Created by axolotl on 16/9/2.
 */
public class PageFragment extends Fragment implements NewsListAdapter.OnItemClickListener {


    public static final String ARG_POS = "ARG_POS";
    private static final String BUNDLE_RECYCLER_LAYOUT = "classname.recycler.layout";


    //第一次通知一定为0
    private boolean first = true;


    @BindView(R.id.rcv_news_list)
    EmptyRecyclerView mRcvNewsList;
    @BindView(R.id.refresh)
    MultiSwipeRefreshLayout refresh;
    @BindView(R.id.empty_view)
    View emptyView;

//    @BindView(R.id.progressBar)
//    ContentLoadingProgressBar progressBar;


    private int mPos;
    private String mCateID;
    private String mCateName;
    private int mColor;
    private NewsListAdapter mAdapter;
    private Parcelable mSavedRecyclerLayoutState;

    @Inject
    PageFragPresenter mPresenter;

    public static PageFragment newInstance(int position) {
        Bundle args = new Bundle();
        args.putInt(ARG_POS, position);
        PageFragment fragment = new PageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPos = getArguments().getInt(ARG_POS);
        mCateID = Constants.CATEGORY_IDS[mPos];
        mCateName = Constants.CATEGORYS[mPos];
        mColor = Constants.THEME_COLORS[mPos % Constants.THEME_COLORS.length];

        DaggerPageComponent.builder().mainComponent(YaNewApp.getNetComponent(getContext()))
                .pageFraModule(new PageFraModule(this, mCateID))
                .build().inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_page, container, false);
        ButterKnife.bind(this, view);
        mRcvNewsList.setLayoutManager(new LinearLayoutManager(getContext()));
        mRcvNewsList.setEmptyView(emptyView);
        mAdapter = new NewsListAdapter(getContext(), null, this);
        mRcvNewsList.setAdapter(mAdapter);
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.refreshList();
            }
        });
        refresh.setSwipeableChildren(R.id.rcv_news_list, R.id.empty_view);
        return view;
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mRcvNewsList != null) {
            outState.putParcelable(BUNDLE_RECYCLER_LAYOUT, mRcvNewsList.getLayoutManager().onSaveInstanceState());
        }
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            mSavedRecyclerLayoutState = savedInstanceState.getParcelable(BUNDLE_RECYCLER_LAYOUT);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPresenter.onViewCreated();

    }

    @Override
    public void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
        mPresenter.onPause();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void errorEvent(NetWorkError error) {
        Timber.d("%s errorEvent %s", mCateID, error.getChannel());
        if (mCateID.equals(error.getChannel())) {
            Toast.makeText(getContext(), "请求数据失败,请检查网络连接", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.onDestroy();
    }

    @Override
    public void onClick(News news) {
//        ActivityOptionsCompat options = ActivityOptionsCompat.
//                makeSceneTransitionAnimation(getActivity(), v, "title");
        DetailActivity.startDetailActivity(getActivity(), news, mColor, mCateName);
    }

    public void showData(RealmResults<News> newses) {
        if ((newses == null || newses.size() == 0) && first) {
            first = false;
            return;
        }
        mAdapter.setupData(newses);
        if (mSavedRecyclerLayoutState != null) {
            mRcvNewsList.getLayoutManager().onRestoreInstanceState(mSavedRecyclerLayoutState);
        }
    }


    public void finishRefresh() {
        refresh.setRefreshing(false);
    }
}
