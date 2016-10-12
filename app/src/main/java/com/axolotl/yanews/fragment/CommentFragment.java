package com.axolotl.yanews.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.axolotl.yanews.R;
import com.axolotl.yanews.YaNewApp;
import com.axolotl.yanews.adapter.CommentAdapter;
import com.axolotl.yanews.customize.EmptyRecyclerView;
import com.axolotl.yanews.customize.MultiSwipeRefreshLayout;
import com.axolotl.yanews.event.RefreshComment;
import com.axolotl.yanews.retrofit.OauthClient;
import com.axolotl.yanews.retrofit.entity.Comment;
import com.axolotl.yanews.retrofit.entity.CommentRes;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

/**
 * Created by axolotl on 16/9/26.
 */

public class CommentFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    public static final String ARG_LINK = "arg_link";

    @BindView(R.id.rcv_news_comments)
    EmptyRecyclerView rcvNewsComments;
    @BindView(R.id.refresh)
    MultiSwipeRefreshLayout refresh;
    @BindView(R.id.tv_empty)
    TextView tvEmpty;

    private CommentAdapter mAdapter;
    private String mLink;

    @Inject
    OauthClient.Endpoints mBackApi;

    public static CommentFragment newInstance(String link) {
        Bundle args = new Bundle();
        args.putString(ARG_LINK, link);
        CommentFragment fragment = new CommentFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        YaNewApp.getNetComponent(getContext()).inject(this);
        mLink = getArguments().getString(ARG_LINK);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_comments, container, false);
        ButterKnife.bind(this, v);
        refresh.setOnRefreshListener(this);
        mAdapter = new CommentAdapter(null, getContext());
        rcvNewsComments.setLayoutManager(new LinearLayoutManager(getContext()));
        rcvNewsComments.setAdapter(mAdapter);
        rcvNewsComments.setEmptyView(tvEmpty);
        refresh.setSwipeableChildren(R.id.rcv_news_comments, R.id.tv_empty);
        return v;
    }


    @Override
    public void onPause() {
        super.onPause();
        Timber.d("comment fra onPause");
    }

    @Override
    public void onResume() {
        super.onResume();
        requestComments();
        Timber.d("comment fra onResume");
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }


    @Override
    public void onRefresh() {
        requestComments();
    }

    private void requestComments() {
        mBackApi.getComments(mLink).enqueue(new Callback<CommentRes>() {
            @Override
            public void onResponse(Call<CommentRes> call, Response<CommentRes> response) {
                refresh.setRefreshing(false);
                Timber.d("GET Comments Success");
                List<Comment> comments = response.body().getComments();
                mAdapter.setData(comments);
            }

            @Override
            public void onFailure(Call<CommentRes> call, Throwable t) {
                Timber.d("GEt Comments Fail %s, %s", t.getLocalizedMessage(), call.request().url().toString());
                refresh.setRefreshing(false);
            }
        });
    }
}
