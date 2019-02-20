package com.gary.weatherdemo.ui.activity;

import android.os.Bundle;
import android.view.View;

import com.example.commonui.ActionBar;
import com.gary.weatherdemo.R;
import com.gary.weatherdemo.download.DownloadManager;
import com.gary.weatherdemo.download.IDownloadCallback;
import com.gary.weatherdemo.network.ApiContants;
import com.gary.weatherdemo.utils.LogUtils;

/**
 * Created by GaryCao on 2019/01/13.
 * 高德城市配置表文件下載管理
 */
public class WtDownloadActivity extends BaseActivity {
    private ActionBar mActionBar;

    @Override
    protected void onCreateNew(Bundle savedInstanceState) {
        //super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_download);
        initView();
    }

    private void initView() {
        findViewById(R.id.download_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DownloadManager.getInstance(WtDownloadActivity.this).
                        startDownload(ApiContants.AMAP_CITY_CONFIG_FILE_URL, mDownloadCallback);
            }
        });

        findViewById(R.id.pause_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DownloadManager.getInstance(WtDownloadActivity.this).pauseDownload();
            }
        });

        findViewById(R.id.cancel_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DownloadManager.getInstance(WtDownloadActivity.this).cancelDownload();
            }
        });

        initActionBar();
    }

    private void initActionBar() {
        mActionBar = findViewById(R.id.action_bar);
        mActionBar.setLeftOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LogUtils.d("onClickedLeftBtn()");
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /*GoF23 观察者模式: UI 通知刷新*/
    private IDownloadCallback mDownloadCallback = new IDownloadCallback() {
        @Override
        public void onStart() {

        }

        @Override
        public void onUpdate() {

        }

        @Override
        public void onSuccess() {

        }

        @Override
        public void onFail() {

        }

        @Override
        public void onCancel() {

        }
    };
}
