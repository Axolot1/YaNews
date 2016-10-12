package com.axolotl.yanews.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.axolotl.yanews.R;
import com.axolotl.yanews.customize.FontTextView;
import com.axolotl.yanews.retrofit.entity.ContentPic;
import com.axolotl.yanews.retrofit.entity.NewsContent;
import com.axolotl.yanews.utils.CustomFontsLoader;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.JsonElement;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmList;

/**
 * Created by axolotl on 16/9/14.
 */
public class NewsContentAdapter extends RecyclerView.Adapter<NewsContentAdapter.ViewHolder> {

    public static final int TYPE_TXT = 1;
    public static final int TYPE_PIC = 2;
    public static final int TYPE_TITLE = 3;

    private RealmList<NewsContent> mData;
    private Context mCtx;
    private String mTitle;

    public NewsContentAdapter(RealmList<NewsContent> mData, Context mCtx, String title) {
        this.mData = mData;
        this.mCtx = mCtx;
        this.mTitle = title;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0)
            return TYPE_TITLE;
        NewsContent n = mData.get(position - 1);
        return n.getType();
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case TYPE_TXT:
                v = inflater.inflate(R.layout.item_content_txt, parent, false);
                break;
            case TYPE_PIC:
                v = inflater.inflate(R.layout.item_content_pic, parent, false);
                break;
            case TYPE_TITLE:
                v = inflater.inflate(R.layout.item_content_title, parent, false);

        }
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (getItemViewType(position) == TYPE_TITLE) {
            holder.tvTitle.setText(mTitle);
            return;
        }
        NewsContent n = mData.get(position - 1);
        if (getItemViewType(position) == TYPE_PIC) {
            Glide.with(mCtx).load(n.getContent()).into(holder.ivPic);
        } else if (getItemViewType(position) == TYPE_TXT) {
            holder.tvContent.setText(n.getContent());

        }
    }


    @Override
    public int getItemCount() {
        if (mData == null)
            return 0;
        return mData.size() + 1;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvContent;
        private ImageView ivPic;
        private TextView tvTitle;

        ViewHolder(View itemView) {
            super(itemView);
            tvContent = (TextView) itemView.findViewById(R.id.tv_content);
            ivPic = (ImageView) itemView.findViewById(R.id.iv_pic);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
        }
    }

    public RealmList<NewsContent> getData() {
        return mData;
    }

    public void setData(RealmList<NewsContent> mData) {
        this.mData = mData;
    }
}
