package com.ysxsoft.apaycoin.com;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.Stack;

/**
 * 描述： 清理activity缓存
 * 日期： 2018/10/23 0023 09:56
 * 作者： 胡
 * 公司：郑州亿生信科技有限公司
 */
public class ActivityPageManager {
    private static Stack<Activity> activityStack;
    private static ActivityPageManager instance;

    private ActivityPageManager() {

    }

    public static ActivityPageManager getInstance() {
        if (instance == null) {
            instance = new ActivityPageManager();
        }
        return instance;
    }

    /**
     * 添加Activity到堆栈
     */
    public void addActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack<Activity>();
        }
        activityStack.add(activity);
    }

    /**
     * 移除Activity
     */
    public void removeActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack<Activity>();
        }
        activityStack.remove(activity);
    }

    /**
     * 当前Actiivty
     */
    public Activity currentActivity() {
        Activity activity = activityStack.lastElement();
        return activity;
    }

    /**
     * 关闭当前Activity
     */
    public void finishActivity() {
        Activity activity = activityStack.lastElement();
        finishActivity(activity);
    }

    /**
     * 关闭某一个Actiivty
     */
    public void finishActivity(Activity activity) {
        if (activity != null) {
            activityStack.remove(activity);
            activity.finish();
            activity = null;
        }
    }

    /**
     * 关闭某一个Actiivty
     */
    public void finishActivity(Class<?> cls) {
        for (Activity activity : activityStack) {
            if (activity.getClass().equals(cls)) {
                finishActivity(activity);
            }
        }
    }

    /**
     * 清除所有Activity
     */
    public void finishAllActivity() {
        for (int i = 0, size = activityStack.size(); i < size; i++) {
            if (null != activityStack.get(i)) {
                activityStack.get(i).finish();
            }
        }
        activityStack.clear();
    }

    /**
     * 清除View资源引用
     */
    public static void unbindReferences(View view) {
        try {
            if (view != null) {
                view.destroyDrawingCache();
                unbindViewReferences(view);
                if (view instanceof ViewGroup){
                    unbindViewGroupReferences((ViewGroup) view);
                }
            }
        } catch (Throwable e) {
        }
    }

    /**
     * 清除View资源引用
     */
    private static void unbindViewGroupReferences(ViewGroup viewGroup) {
        int nrOfChildren = viewGroup.getChildCount();
        for (int i = 0; i < nrOfChildren; i++) {
            View view = viewGroup.getChildAt(i);
            unbindViewReferences(view);
            if (view instanceof ViewGroup)
                unbindViewGroupReferences((ViewGroup) view);
        }
        try {
            viewGroup.removeAllViews();
        } catch (Throwable mayHappen) {
        }
    }

    @SuppressWarnings("deprecation")
    private static void unbindViewReferences(View view) {
        try {
            view.setOnClickListener(null);
            view.setOnCreateContextMenuListener(null);
            view.setOnFocusChangeListener(null);
            view.setOnKeyListener(null);
            view.setOnLongClickListener(null);
            view.setOnClickListener(null);
        } catch (Throwable mayHappen) {
        }

        Drawable d = view.getBackground();
        if (d != null){
            d.setCallback(null);
        }

        if (view instanceof ImageView) {
            ImageView imageView = (ImageView) view;
            d = imageView.getDrawable();
            if (d != null){
                d.setCallback(null);
            }
            imageView.setImageDrawable(null);
            imageView.setBackgroundDrawable(null);
        }

        if (view instanceof WebView) {
            WebView webview = (WebView) view;
            webview.stopLoading();
            webview.clearFormData();
            webview.clearDisappearingChildren();
            webview.setWebChromeClient(null);
            webview.setWebViewClient(null);
            webview.destroyDrawingCache();
            webview.destroy();
            webview = null;
        }

        if (view instanceof ListView) {
            ListView listView = (ListView) view;
            try {
                listView.removeAllViewsInLayout();
            } catch (Throwable mayHappen) {
            }
            ((ListView) view).destroyDrawingCache();
        }
    }
    /**
     * 退出程序
     */
    @SuppressWarnings("deprecation")
    public void exit(Context context) {
        try {
            finishAllActivity();
            if(context != null){
                ActivityManager activityMgr = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
                activityMgr.restartPackage(context.getPackageName());
            }
            System.exit(0);
            android.os.Process.killProcess(android.os.Process.myPid());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
