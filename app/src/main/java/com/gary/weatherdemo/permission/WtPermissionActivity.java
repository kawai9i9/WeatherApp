package com.gary.weatherdemo.permission;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v7.app.AppCompatActivity;

import com.gary.weatherdemo.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by GaryCao on 2019/01/13.
 * 动态申请权限
 */
public class WtPermissionActivity extends AppCompatActivity {
    public static int REQUEST_CODE_PERMISSION = 0x10001;

    /**
     * 需动态申请的权限列表
     */
    private static String[] mAllPermissions = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //发起权限申请
        List<String> needPermissions = getNeedGrantPermissions(mAllPermissions);
        ActivityCompat.requestPermissions(
                this,
                needPermissions.toArray(new String[needPermissions.size()]),
                REQUEST_CODE_PERMISSION);
    }

    /**
     * 获取待用户授权的权限列表
     *
     * @param permissions
     * @return
     */
    private List<String> getNeedGrantPermissions(String[] permissions) {
        List<String> needRequestPermissionList = new ArrayList<>();
        for (String permission : permissions) {
            if (PermissionChecker.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                needRequestPermissionList.add(permission);
            }
        }
        return needRequestPermissionList;
    }

    /**
     * 系统API: 系统请求权限结束时自动回调
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_PERMISSION) {
            if (checkPermissionsGrantResult(grantResults)) {
                LogUtils.d("onRequestPermissionsResult success");
            }

            finish();
        }
    }

    /**
     * 检测用户是否授权所有权限
     *
     * @param grantResults
     * @return
     */
    private boolean checkPermissionsGrantResult(int[] grantResults) {
        for (int grantResult : grantResults) {
            if (grantResult != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    /**
     * 唯一入口：请求权限
     *
     * @return true:启动授权界面 false:已授权，直接return走正常流程
     */
    public static boolean startRequestAllPermission(Context context) {
        if (!checkPermissionsGranted(context, mAllPermissions)) {
            Intent intent = new Intent();
            intent.setAction("android.wt.action.PERMISSION_REQUEST");
            context.startActivity(intent);
            return true;
        }

        return false;
    }


    /**
     * 查询应用是否有未授权项
     *
     * @param permissions
     * @return
     */
    private static boolean checkPermissionsGranted(Context context, String[] permissions) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }

        for (String permission : permissions) {
            if (PermissionChecker.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    //===================================================================================================
    //for test

    /**
     * 发起权限申请
     */
    public void test(Context context) {
        WtPermissionActivity.startRequestAllPermission(context);
    }
}
