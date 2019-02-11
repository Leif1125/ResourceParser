package com.app.dixon.resourceparser.func.home.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.dixon.resourceparser.R;
import com.app.dixon.resourceparser.core.pub.activity.BaseActivity;
import com.app.dixon.resourceparser.core.util.ToastUtils;
import com.app.dixon.resourceparser.func.home.present.MovieOutlinePresent;
import com.app.dixon.resourceparser.func.search.view.SearchActivity;
import com.app.dixon.resourceparser.func.set.EditActivity;
import com.app.dixon.resourceparser.func.torr.view.TorrActivity;
import com.app.dixon.resourceparser.model.MovieOutline;
import com.github.ybq.android.spinkit.SpinKitView;

import java.util.List;

/**
 * 电影梗概页面
 */

public class MovieOutlineActivity extends BaseActivity implements IMovieOutlineView {

    private MovieOutlinePresent mPresent;
    private GridView mGridView;
    private SpinKitView mLoadingView;
    private MovieOutlineAdapter mAdapter;
    private ImageView mSearch;
    private TextView mTorr, mEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPresent = new MovieOutlinePresent(this);
        mPresent.loadData();

        initView();
    }

    private void initView() {
        mSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SearchActivity.startSearchActivity(MovieOutlineActivity.this);
            }
        });
        mTorr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TorrActivity.startTorrActivity(MovieOutlineActivity.this);
            }
        });
        mEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditActivity.startEditActivity(MovieOutlineActivity.this);
            }
        });
    }

    public static void startMovieOutlineActivity(Context context) {
        context.startActivity(new Intent(context, MovieOutlineActivity.class));
    }

    @Override
    public void onContentChanged() {
        super.onContentChanged();
        mGridView = findViewById(R.id.gvMovies);
        mLoadingView = findViewById(R.id.svLoadingView);
        mSearch = findViewById(R.id.ivSearch);
        mTorr = findViewById(R.id.tvTorr);
        mEdit = findViewById(R.id.tvEdit);
    }

    @Override
    public void showMovieOutlines(List<MovieOutline> list) {
        if (mAdapter == null) {
            mAdapter = new MovieOutlineAdapter(this, list);
            mGridView.setAdapter(mAdapter);
            setShowAnim();
        } else {
            mAdapter.notifyData(list);
        }
    }

    private void setShowAnim() {
        LayoutAnimationController controller = new LayoutAnimationController(
                AnimationUtils.loadAnimation(this, R.anim.bottom_in));
        controller.setDelay(0.5f);
        controller.setOrder(LayoutAnimationController.ORDER_NORMAL);
        mGridView.setLayoutAnimation(controller);
    }

    @Override
    public void showFail(String err) {
        ToastUtils.toast(err);
    }

    @Override
    public void showLoading() {
        mLoadingView.setVisibility(View.VISIBLE);
    }

    @Override
    public void stopLoading() {
        mLoadingView.setVisibility(View.GONE);
    }
}
