package com.axolotl.yanews.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.axolotl.yanews.R;
import com.axolotl.yanews.adapter.NewsContentAdapter;
import com.axolotl.yanews.retrofit.entity.News;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by axolotl on 16/9/26.
 */

public class NewsContentFragment extends Fragment {

    public static String ARG_NEWS = "arg_news";
    @BindView(R.id.rec_content)
    RecyclerView recContent;
    private NewsContentAdapter mAdapter;
    private News mNews;

    public static NewsContentFragment newInstance(News news) {
        Bundle args = new Bundle();
        args.putParcelable(ARG_NEWS, Parcels.wrap(news));
        NewsContentFragment fragment = new NewsContentFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mNews = Parcels.unwrap(getArguments().getParcelable(ARG_NEWS));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_news_content, container, false);
        ButterKnife.bind(this, v);
        mAdapter = new NewsContentAdapter(mNews.getAllList(), getContext(), mNews.getTitle());
        recContent.setLayoutManager(new LinearLayoutManager(getContext()));
        recContent.setAdapter(mAdapter);
        return v;
    }
}
