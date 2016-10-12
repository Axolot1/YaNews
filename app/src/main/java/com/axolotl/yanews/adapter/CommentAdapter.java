package com.axolotl.yanews.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.axolotl.yanews.R;
import com.axolotl.yanews.retrofit.entity.Comment;
import com.bumptech.glide.Glide;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import timber.log.Timber;

/**
 * Created by axolotl on 16/9/27.
 */

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {

    private Context mCtx;
    private List<Comment> mData;


    public CommentAdapter(List<Comment> mData, Context mCtx) {
        this.mData = mData;
        this.mCtx = mCtx;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_layout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Comment comment = mData.get(position);
        holder.tvComments.setText(comment.getBody());
        holder.tvTime.setText(changeFormat(comment.getTimestamp()));
        holder.tvUserName.setText(comment.getUserName());
        Glide.with(mCtx).load(comment.getPicture()).into(holder.cvAvatar);
    }

    private String changeFormat(String time) {
        Timber.d("time : %s", time);
        String OLD_FORMAT = "EE, dd MMM yyyy hh:mm:ss zzz";
        SimpleDateFormat inputFormat = new SimpleDateFormat(OLD_FORMAT, Locale.UK);
//        inputFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        String NEW_FORMAT = "yyyy-MM-dd hh:mm:ss";
        SimpleDateFormat outputFormat = new SimpleDateFormat(NEW_FORMAT, Locale.CHINA);
        Date date = null;
        String str = null;
        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;

    }

    @Override
    public int getItemCount() {
        if (mData == null)
            return 0;
        return mData.size();
    }

    public List<Comment> getData() {
        return mData;
    }

    public void setData(List<Comment> mData) {
        this.mData = mData;
        this.notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.cv_avatar)
        CircleImageView cvAvatar;
        @BindView(R.id.tv_user_name)
        TextView tvUserName;
        @BindView(R.id.tv_time)
        TextView tvTime;
        @BindView(R.id.tv_comments)
        TextView tvComments;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
