package com.app.dixon.resourceparser.func.home.view;

import android.content.ClipboardManager;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.dixon.resourceparser.R;
import com.app.dixon.resourceparser.core.manager.ParserManager;
import com.app.dixon.resourceparser.core.pub.view.AsyncImageView;
import com.app.dixon.resourceparser.core.util.ToastUtils;
import com.app.dixon.resourceparser.func.home.control.MovieDetailRequest;
import com.app.dixon.resourceparser.model.MovieDetail;
import com.app.dixon.resourceparser.model.MovieOutline;

import java.util.List;

/**
 * Created by dixon.xu on 2019/2/1.
 */

public class MovieOutlineAdapter extends BaseAdapter {

    private Context mContext;
    private List<MovieOutline> mList;

    public MovieOutlineAdapter(Context context, List<MovieOutline> list) {
        this.mContext = context;
        this.mList = list;
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_movie_outline, null);
            vh = new ViewHolder(convertView);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        loadView(vh, position);
        return convertView;
    }

    private void loadView(final ViewHolder vh, final int position) {
        final MovieOutline movie = mList.get(position);
        vh.title.setText(movie.getTitle());
        vh.image.setImageBitmap(null);
        vh.error.setVisibility(View.GONE);

        MovieDetail detail = movie.getDetailCache();
        if (detail != null) {
            //Cache机制
            //首页电影较多，一次性前往所有页面的详情页获取电影封面和链接不现实
            //而adapter动态获取又存在反复获取不合理因素
            //通过queue的缓存存在延迟问题
            //所以采用这种缓存机制
            vh.image.setUrl(detail.getCoverImg());
            vh.image.setOnLongClickListener(new ClipboardCopyListener(mContext, detail.getDownloadUrl()));
            mList.get(position).setDetailCache(detail);
        } else {
            ParserManager.queue().add(new MovieDetailRequest(movie.getTitle(), movie.getUrl(), new MovieDetailRequest.Listener() {
                @Override
                public void onSuccess(MovieDetail detail) {
                    vh.image.setUrl(detail.getCoverImg());
                    vh.image.setOnLongClickListener(new ClipboardCopyListener(mContext, detail.getDownloadUrl()));
                    mList.get(position).setDetailCache(detail);
                }

                @Override
                public void onFail(String msg) {
                    vh.error.setVisibility(View.VISIBLE);
                    vh.image.setOnLongClickListener(new ClipboardCopyListener(mContext, null));
                }
            }));
        }

        vh.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(movie.getUrl())) {
                    DownloadListActivity.startDownloadListActivity(mContext, movie.getUrl());
                }
            }
        });
    }

    public void notifyData(List<MovieOutline> list) {
        if (mList == null) {
            mList = list;
        } else {
            mList.clear();
            mList.addAll(list);
        }
        notifyDataSetChanged();
    }

    private static final class ViewHolder {

        TextView title;
        AsyncImageView image;
        ImageView error;

        public ViewHolder(View view) {
            title = view.findViewById(R.id.tvTitle);
            image = view.findViewById(R.id.ivCover);
            error = view.findViewById(R.id.ivError);
        }
    }

    private static class ClipboardCopyListener implements View.OnLongClickListener {

        private Context mContext;
        private String mUrl;

        public ClipboardCopyListener(Context context, String url) {
            this.mContext = context;
            this.mUrl = url;
        }

        @Override
        public boolean onLongClick(View v) {
            if (TextUtils.isEmpty(mUrl)) {
                ToastUtils.toast("当前页面失效 复制链接失败");
            } else {
                ToastUtils.toast("已复制首个下载链接");
                copy();
            }
            return true;
        }

        private void copy() {
            ClipboardManager cmb = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
            cmb.setText(mUrl);
        }
    }
}
