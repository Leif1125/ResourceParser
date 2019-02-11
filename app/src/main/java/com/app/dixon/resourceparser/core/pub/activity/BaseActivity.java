package com.app.dixon.resourceparser.core.pub.activity;

import android.app.Activity;
import android.os.Bundle;

import com.app.dixon.resourceparser.core.manager.AnalyticsManager;
import com.app.dixon.resourceparser.core.util.StatusBarUtil;

/**
 * Created by dixon.xu on 2019/2/1.
 * <p>
 * BaseActivity
 */

public class BaseActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StatusBarUtil.setColorForStatus(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        AnalyticsManager.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        AnalyticsManager.onPause(this);
    }
}
