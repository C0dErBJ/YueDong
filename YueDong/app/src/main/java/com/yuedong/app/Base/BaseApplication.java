package com.yuedong.app.Base;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.baidu.location.BDLocation;
import com.baidu.mapapi.SDKInitializer;
import com.yuedong.app.Model.User;
import com.yuedong.app.utils.CollectionExtension;
import com.yuedong.app.utils.LruBitmapCache;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by C0dEr on 16/1/11.
 */
public class BaseApplication extends Application {
    public static final String TAG = BaseApplication.class
            .getSimpleName();

    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;

    private static BaseApplication mInstance;

    public User CurrentUser;
    public String CurrentCity = "上海";

    public  BDLocation CurrentLocation;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        SDKInitializer.initialize(this);
    }

    public static synchronized BaseApplication getInstance() {
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    public ImageLoader getImageLoader() {
        getRequestQueue();
        if (mImageLoader == null) {
            mImageLoader = new ImageLoader(this.mRequestQueue,
                    new LruBitmapCache());
        }
        return this.mImageLoader;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

    public static List<Activity> activityList = new ArrayList<Activity>();

    /**
     * 退出应用程序
     */
    public static void exitAppWithDialog(Context mContext) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("提示");
        builder.setMessage("您确定要退出程序吗?");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (!CollectionExtension.IsNullOrEmpty(activityList)) {
                    for (Activity activity : activityList) {
                        activity.finish();
                    }

                }
                android.os.Process.killProcess(android.os.Process.myPid());
            }
        });
        builder.setNegativeButton("取消", null);
        builder.show();
    }

}
