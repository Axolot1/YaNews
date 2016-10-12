package com.axolotl.yanews.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.axolotl.yanews.R;
import com.axolotl.yanews.retrofit.entity.News;
import com.bumptech.glide.Glide;

import java.util.List;

import io.realm.RealmList;
import io.realm.RealmRecyclerViewAdapter;
import io.realm.RealmResults;
import timber.log.Timber;

/**
 * Created by axolotl on 16/9/3.RealmRecyclerViewAdapter<News, NewsListAdapter.ViewHolder>
 */
public class NewsListAdapter extends RecyclerView.Adapter<NewsListAdapter.ViewHolder> {

    private Context mContext;
    private OnItemClickListener mListener;
    private RealmResults<News> mNews;

    public interface OnItemClickListener {
        void onClick(News news);
    }

    public NewsListAdapter(Context mContext, RealmResults<News> mNews, OnItemClickListener listener) {
//        super(mContext, mNews, true);
        this.mContext = mContext;
        this.mListener = listener;
        this.mNews = mNews;
    }

    public void setupData(RealmResults<News> newses) {
        this.mNews = newses;
        this.notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        News n = mNews.get(position);
        if (position == 0 && n.getType() == News.TYPE_IMAGE)
            return News.TYPE_HEAD;
        return n.getType();
    }

    @Override
    public int getItemCount() {
        if (mNews == null)
            return 0;
        return mNews.size();
    }

    @Override
    public NewsListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View v = null;
        switch (viewType) {
            case News.TYPE_HEAD:
                v = layoutInflater.inflate(R.layout.news_list_item_head, parent, false);
                break;
            case News.TYPE_TEXT:
                v = layoutInflater.inflate(R.layout.news_list_item_text, parent, false);
                break;
            default:
                v = layoutInflater.inflate(R.layout.news_list_item_image, parent, false);
        }
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(NewsListAdapter.ViewHolder holder, int position) {
        News news = mNews.get(position);
        holder.titleView.setText(news.getTitle());
        holder.subtitleView.setText(news.getSource());
        holder.dateView.setText(news.getPubDate());
        holder.descView.setText(news.getDesc());
        if (holder.imgView != null) {
            Glide.with(mContext).load(news.getImageurls().get(0).getUrl()).centerCrop().into(holder.imgView);
        }
    }


    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView titleView;
        private TextView subtitleView;
        private TextView dateView;
        private TextView descView;
        private ImageView imgView;

        public ViewHolder(View itemView) {
            super(itemView);

            this.titleView = (TextView) itemView.findViewById(R.id.news_item_title);
            this.subtitleView = (TextView) itemView.findViewById(R.id.news_item_subtitle);
            this.imgView = (ImageView) itemView.findViewById(R.id.news_item_img);
            this.dateView = (TextView) itemView.findViewById(R.id.tv_news_date);
            this.descView = (TextView) itemView.findViewById(R.id.tv_news_item_desc);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                News n = mNews.get(getAdapterPosition());
                mListener.onClick(n);
            }
        }
    }
}
