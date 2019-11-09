package com.ysxsoft.apaycoin.utils;

import android.app.Activity;
import android.app.AppOpsManager;
import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 描述：
 * 日期： 2018/10/23 0023 10:18
 * 作者： 胡
 * 公司：郑州亿生信科技有限公司
 */
public class Toast {
    private static final String CHECK_OP_NO_THROW = "checkOpNoThrow";
    private static final String OP_POST_NOTIFICATION = "OP_POST_NOTIFICATION";
    private static int checkNotification = 0;
    private Object mToast;
    private Toast(Context context, String message, int duration) {
        if(context instanceof Application)
            checkNotification = 0;
        else
            checkNotification = isNotificationEnabled(context) ? 0 : 1;
        if (checkNotification == 1) {
            mToast = MToast.makeText(context, message, duration);
        } else {
            mToast = android.widget.Toast.makeText(context, message, duration);
        }
    }
    private Toast(Context context, int resId, int duration) {
        if(context instanceof Application)
            checkNotification = 0;
        else
            checkNotification = isNotificationEnabled(context) ? 0 : 1;
        if (checkNotification == 1 && context instanceof Activity) {
            mToast = MToast.makeText(context, resId, duration);
        } else {
            mToast = android.widget.Toast.makeText(context, resId, duration);
        }
    }

    public static Toast makeText(Context context, String message, int duration) {
        return new Toast(context,message,duration);
    }
    public static Toast makeText(Context context, int resId, int duration) {
        return new Toast(context,resId,duration);
    }

    public void show() {
        if(mToast instanceof MToast){
            ((MToast) mToast).show();
        }else if(mToast instanceof android.widget.Toast){
            ((android.widget.Toast) mToast).show();
        }
    }
    public void cancel(){
        if(mToast instanceof MToast){
            ((MToast) mToast).cancel();
        }else if(mToast instanceof android.widget.Toast){
            ((android.widget.Toast) mToast).cancel();
        }
    }
    public void setText(CharSequence s){
        if(mToast instanceof MToast){
            ((MToast) mToast).setText(s);
        }else if(mToast instanceof android.widget.Toast){
            ((android.widget.Toast) mToast).setText(s);
        }
    }
    /**
     * 用来判断是否开启通知权限
     * */
    private static boolean isNotificationEnabled(Context context){
        if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT){
            AppOpsManager mAppOps = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
            ApplicationInfo appInfo = context.getApplicationInfo();

            String pkg = context.getApplicationContext().getPackageName();

            int uid = appInfo.uid;

            Class appOpsClass = null; /* Context.APP_OPS_MANAGER */

            try {
                appOpsClass = Class.forName(AppOpsManager.class.getName());
                Method checkOpNoThrowMethod = appOpsClass.getMethod(CHECK_OP_NO_THROW, Integer.TYPE, Integer.TYPE, String.class);
                Field opPostNotificationValue = appOpsClass.getDeclaredField(OP_POST_NOTIFICATION);
                int value = (int)opPostNotificationValue.get(Integer.class);
                return ((int)checkOpNoThrowMethod.invoke(mAppOps,value, uid, pkg) == AppOpsManager.MODE_ALLOWED);
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }else{
            return false;
        }
    }
}
