package com.ysxsoft.apaycoin.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Application;
import android.app.PendingIntent;
import android.app.PendingIntent.CanceledException;
import android.content.ComponentCallbacks;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.location.LocationManager;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Vibrator;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.File;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AppUtil {
    private static float sNoncompatDensity;
    private static float sNoncompatScaledDensity;

    /**
     * 今日头条适配方案  以宽360dp  为基准
     *
     * @param activity
     * @param application
     */
    public static void setCustomDensity(Activity activity, final Application application) {
        DisplayMetrics appDisplayMetrics = application.getResources().getDisplayMetrics();
        if (sNoncompatDensity == 0) {
            sNoncompatDensity = appDisplayMetrics.density;
            sNoncompatScaledDensity = appDisplayMetrics.scaledDensity;
            application.registerComponentCallbacks(new ComponentCallbacks() {
                @Override
                public void onConfigurationChanged(Configuration newConfig) {
                    if (newConfig != null && newConfig.fontScale > 0) {
                        sNoncompatScaledDensity = application.getResources().getDisplayMetrics().scaledDensity;
                    }
                }

                @Override
                public void onLowMemory() {

                }
            });
            float targetDensity = appDisplayMetrics.widthPixels / 360;
            float targetScaleDensity = targetDensity * (sNoncompatScaledDensity / sNoncompatDensity);
            int targetDensityDpi = (int) (160 * targetDensity);

            appDisplayMetrics.density = targetDensity;
            appDisplayMetrics.scaledDensity = targetScaleDensity;
            appDisplayMetrics.densityDpi = targetDensityDpi;

            DisplayMetrics activityDisplayMetrics = activity.getResources().getDisplayMetrics();
            activityDisplayMetrics.density = targetDensity;
            activityDisplayMetrics.scaledDensity = targetScaleDensity;
            activityDisplayMetrics.densityDpi = targetDensityDpi;

        }
    }

    /**
     * 判断网络类型
     *
     * @param context
     * @return
     */
    public static String NetType(Context context) {
        try {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo info = cm.getActiveNetworkInfo();
            String typeName = info.getTypeName().toLowerCase(); // WIFI/MOBILE
            if (typeName.equalsIgnoreCase("wifi")) {
            } else {
                typeName = info.getExtraInfo().toLowerCase();
                // 3gnet/3gwap/uninet/uniwap/cmnet/cmwap/ctnet/ctwap
            }
            return typeName;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 网络是否可用
     *
     * @param mContext
     * @return
     */
    public static boolean isNetworkAvaiable(Context mContext) {
        ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        return (info != null && info.isConnected());
    }

    /**
     * 关闭软键盘
     *
     * @param mContext
     * @param view
     */
    public static void colseKeyboard(Context mContext, View view) {
        try {
            InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        } catch (Exception e) {
        }
    }

    /**
     * 关闭手机软件盘
     *
     * @param activity
     */
    public static void colsePhoneKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive() && activity.getCurrentFocus() != null) {
            if (activity.getCurrentFocus().getWindowToken() != null) {
                imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    /**
     * 打开软件盘
     *
     * @param mContext
     */
    public static void openKeyboard(Context mContext) {
        InputMethodManager inputMethodManager = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * 是否正在运行
     *
     * @param context
     * @param packageName
     * @return
     */
    public static boolean isRunning(Context context, String packageName) {
        boolean isAppRunning = false;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningTaskInfo> list = am.getRunningTasks(100);
        for (RunningTaskInfo info : list) {
            if (info.topActivity.getPackageName().equals(packageName)
                    && info.baseActivity.getPackageName().equals(packageName)) {
                isAppRunning = true;
                break;
            }
        }
        return isAppRunning;
    }

    /**
     * 启动app
     *
     * @param context
     * @param appName
     */
    public static void launchApp(Context context, String appName) {
        PackageManager packageManager = context.getPackageManager();
        // 获取手机里的应用列表
        List<PackageInfo> pInfo = packageManager.getInstalledPackages(0);
        for (int i = 0; i < pInfo.size(); i++) {
            PackageInfo p = pInfo.get(i);
            // 获取相关包的<application>中的label信息，也就是-->应用程序的名字
            String label = packageManager
                    .getApplicationLabel(p.applicationInfo).toString();
            System.out.println(label);
            if (label.equals(appName)) { // 比较label
                String pName = p.packageName; // 获取包名
                Intent intent = new Intent();
                // 获取intent
                intent = packageManager.getLaunchIntentForPackage(pName);
                context.startActivity(intent);

            }

        }
    }

    /**
     * 分享功能
     *
     * @param mContext      上下文
     * @param activityTitle Activity的名字
     * @param msgTitle      消息标题
     * @param msgText       消息内容
     * @param imgPath       图片路径，不分享图片则传null
     */
    public static void shareMsg(Context mContext, String activityTitle,
                                String msgTitle, String msgText, String imgPath) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        if (imgPath == null || imgPath.equals("")) {
            intent.setType("text/plain"); // 纯文本
        } else {
            File f = new File(imgPath);
            if (f != null && f.exists() && f.isFile()) {
                intent.setType("image/jpg");
                Uri u = Uri.fromFile(f);
                intent.putExtra(Intent.EXTRA_STREAM, u);
            }
        }
        intent.putExtra(Intent.EXTRA_SUBJECT, msgTitle);
        intent.putExtra(Intent.EXTRA_TEXT, msgText);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(Intent.createChooser(intent, activityTitle));
    }

    /**
     * GSON解析json数据
     *
     * @param json
     * @param cls
     */

    public static <T> T parse(String json, Type cls) {
        try {
            Gson gson = new Gson();
            return gson.fromJson(json, cls);
        } catch (Exception e) {
            Log.i("", "数据解析异常--" + e.getMessage());
        }
        return null;
    }

    /**
     * 拍照sd卡地址
     **/
    public static String getImageSDpath() {
        if (Build.MANUFACTURER.equalsIgnoreCase("meizu")) {
            return Environment.getExternalStorageDirectory().toString() + "/Camera/Nuctech";
        } else {
            return Environment.getExternalStorageDirectory().toString() + "/DCIM/Camera/Nuctech";
        }
    }

    /**
     * 创建图片名称
     *
     * @param dateTaken
     * @return
     */
    public static String createName(long dateTaken) {
        Date date = new Date(dateTaken);
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat dateFormat = new SimpleDateFormat("'IMG'_yyyyMMdd_HHmmss");
        return dateFormat.format(date);
    }

    /**
     * 获得软件版本名字
     */
    public static String getVersionName(Context context) {
        PackageManager manager = context.getPackageManager();
        try {
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            return info.versionName;
        } catch (NameNotFoundException e) {
        }
        return "";
    }

    /**
     * 获取版本号
     * 获得软件VersionCode
     */
    public static int getVersionCode(Context context) {
        PackageManager manager = context.getPackageManager();
        try {
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            return info.versionCode;
        } catch (NameNotFoundException e) {
        }
        return 0;
    }

    /**
     * 获得包名
     *
     * @param context
     * @return
     */
    public static String getPackageName(Context context) {
        PackageManager manager = context.getPackageManager();
        try {
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            return info.packageName;
        } catch (NameNotFoundException e) {
        }
        return "";
    }

    /**
     * 定时器
     *
     * @param handler
     * @param t
     * @return
     */
    public static Timer showCheckCodeTimer(Handler handler, int t) {
        Timer timer = new Timer();
        timer.schedule(new MyTimerTask(handler, t), 0, 1000);
        return timer;
    }

    static class MyTimerTask extends TimerTask {
        private Handler handler;
        private int t;

        public MyTimerTask(Handler handler, int t) {
            this.handler = handler;
            this.t = t;
        }

        @Override
        public void run() {
            if (t >= 0) {
                handler.sendEmptyMessage(t);
                t--;
            }
        }
    }

    /**
     * 检测手机号的格式是否正确
     *
     * @param phonenumber
     * @return
     */
    public static boolean checkPhoneNum(String phonenumber) {
        String regExp = "^13[0-9]{9}$|^14[0-9]{9}$|^15[0-9]{9}$|^18[0-9]{9}$|^17[0-9]{9}$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(phonenumber);
        return m.find();
    }

    /**
     * 检测身份证号格式是否正确
     *
     * @param idNum
     * @return
     */
    public static boolean checkIdNum(String idNum) {
        String regExp = "^(\\d{15}$|^\\d{18}$|^\\d{17}(\\d|X|x))$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(idNum);
        return m.find();
    }

    /**
     * 核对车牌号
     *
     * @param carNBum
     * @return
     */
    public static boolean checkCarNum(String carNBum) {
        String regExp = "[\u4e00-\u9fa5]{1}[a-zA-Z]{1}[a-zA-Z_0-9]{5}";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(carNBum);
        return m.find();
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 手机震动
     *
     * @param mContext
     */
    @SuppressLint("MissingPermission")
    public static void showRock(Context mContext) {
        Vibrator vibrator = (Vibrator) mContext.getSystemService(Context.VIBRATOR_SERVICE);
        long[] pattern = {100, 400, 100, 400}; // 停止 开启 停止 开启
        vibrator.vibrate(pattern, -1);// 重复两次上面的pattern 如果只想震动一次，index设为-1
        vibrator = null;
    }

    /**
     * 播放系统提示音
     *
     * @param mContext
     */
    public static void playSystemNotificationVoice(Context mContext) {
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Ringtone r = RingtoneManager.getRingtone(mContext, notification);
        r.play();
    }

    /**
     * 播放自定义提示音
     */
    public static void playCustomeVoice(Context mContext, int voiceId) {
        SoundPool soundPool = new SoundPool(5, AudioManager.STREAM_SYSTEM, 3);
        soundPool.load(mContext, voiceId, 1);
        soundPool.setOnLoadCompleteListener(new OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId,
                                       int status) {
                soundPool.play(1, 1, 1, 0, 0, 1);
                soundPool = null;
            }
        });

    }

    /**
     * 打电话
     *
     * @param context
     * @param number
     */
    public static void callPhone(Context context, String number) {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + number));
        context.startActivity(intent);
    }

    /**
     * 获取屏幕宽
     *
     * @param mContext
     * @return
     */
    public static int getScreenWidth(Context mContext) {
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }

    /**
     * 获取屏幕高
     *
     * @param mContext
     * @return
     */
    public static int getScreenHeight(Context mContext) {
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(dm);
        return dm.heightPixels;
    }

    /**
     * 根据逗号分割字符串
     *
     * @param str
     * @return
     */
    public static String splitStr(String str) {
        String[] as = str.split(",");
        for (int i = 0; i < as.length; i++) {
            return as[i];
        }
        return str;
    }

    /**
     * 根据点分割
     *
     * @param str
     * @return
     */
    public static String splitStrPoint(String str) {
        String[] as = str.split("\\.");
        for (int i = 0; i < as.length; i++) {
            return as[i];
        }
        return str;
    }

    /**
     * 去掉双引号
     *
     * @param string
     * @return
     */
    public static String stringReplace(String string) {
        //去掉" "号
        String str = string.replace("\"", "");
        return str;

    }

    /**
     * 下载完apk  通知系统安装  打开安装界面
     *
     * @param c
     * @param fileSavePath apk路径
     */
    private void openAPKInstall(Context c, String fileSavePath) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse("file://" + fileSavePath), "application/vnd.android.package-archive");
        c.startActivity(intent);
    }

    /**
     * 判断当前gps是否开启
     *
     * @param c
     * @return
     */
    public static boolean isGPSEnable(Context c) {
        /*
         * 用Setting.System来读取也可以，只是这是更旧的用法 String str =
         * Settings.System.getString(getContentResolver(),
         * Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
         */
        String str = Settings.Secure.getString(c.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
        Log.v("GPS", str);
        if (str != null) {
            return str.contains("gps");
        } else {
            return false;
        }
    }

    /**
     * 判断GPS是否开启，GPS
     *
     * @param context
     * @return true 表示开启
     */
    public static final boolean isOPen(final Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        // 通过GPS卫星定位，定位级别可以精确到街（通过24颗卫星定位，在室外和空旷的地方定位准确、速度快）
        boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (gps) {
            return true;
        }
        return false;

    }

    /**
     * 辅助gps 是否开启
     *
     * @param context
     * @return
     */
    public static boolean isAGpsOpen(final Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        // 通过WLAN或移动网络(3G/2G)确定的位置（也称作AGPS，辅助GPS定位。主要用于在室内或遮盖物（建筑群或茂密的深林等）密集的地方定位）
        boolean network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (network) {
            return true;
        }
        return false;
    }

    /**
     * 强制帮用户打开GPS
     *
     * @param context
     */
    public static final void openGPS(Context context) {
        Intent GPSIntent = new Intent();
        GPSIntent.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
        GPSIntent.addCategory("android.intent.category.ALTERNATIVE");
        GPSIntent.setData(Uri.parse("custom:3"));
        try {
            PendingIntent.getBroadcast(context, 0, GPSIntent, 0).send();
        } catch (CanceledException e) {
            e.printStackTrace();
        }
    }

    /**
     * 跳转到gps页面
     *
     * @param c
     */
    public static void initGpsSetttings(Context c) {
        LocationManager lm = (LocationManager) c.getSystemService(Context.LOCATION_SERVICE);
        // 判断GPS是否正常启动
        if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Toast.makeText(c, "请开启GPS导航...", MToast.LENGTH_SHORT).show();
            // 返回开启GPS导航设置界面
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            ((Activity) c).startActivityForResult(intent, 0);
            return;
        }
    }

    /**
     * 判断某个界面是否在前台
     *
     * @param context
     * @param className 某个界面名称
     */
    public static boolean isForeground(Context context, String className) {
        if (context == null || TextUtils.isEmpty(className)) {
            return false;
        }
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningTaskInfo> list = am.getRunningTasks(1);
        if (list != null && list.size() > 0) {
            ComponentName cpn = list.get(0).topActivity;
            if (className.equals(cpn.getClassName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * All, 年月日时分秒
     * Year,年
     * Year_Mouth,年月
     * Year_Mouth_Day 年月日
     */
    public enum AppTime {
        All,
        Year,
        Year_Mouth,
        Year_Mouth_Day,
        H_M_S
    }

    private static String format;

    /**
     * 格式化时间
     *
     * @param time
     * @return
     */
    public static String FormarTime(AppTime appTime, long time) {
        switch (appTime) {
            case All:
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                format = dateFormat.format(time);
                break;
            case Year:
                SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy");
                format = dateFormat1.format(time);
                break;
            case Year_Mouth:
                SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM");
                format = dateFormat2.format(time);
                break;
            case Year_Mouth_Day:
                SimpleDateFormat dateFormat3 = new SimpleDateFormat("yyyy-MM-dd");
                format = dateFormat3.format(time);
                break;
            case H_M_S:
                SimpleDateFormat dateFormat4 = new SimpleDateFormat("HH:mm:ss");
                format = dateFormat4.format(time);
                break;
        }
        return format;
    }

    /**
     * 将秒转化为时分秒
     * @param time
     * @return
     */
    public static String secToTime(int time) {
        String timeStr = null;
        int hour = 0;
        int minute = 0;
        int second = 0;
        if (time <= 0)
            return "00:00";
        else {
            minute = time / 60;
            if (minute < 60) {
                second = time % 60;
                timeStr = unitFormat(minute) + ":" + unitFormat(second);
            } else {
                hour = minute / 60;
                if (hour > 99)
                    return "99:59:59";
                minute = minute % 60;
                second = time - hour * 3600 - minute * 60;
                timeStr = unitFormat(hour) + ":" + unitFormat(minute) + ":" + unitFormat(second);
            }
        }
        return timeStr;
    }

    public static String unitFormat(int i) {
        String retStr = null;
        if (i >= 0 && i < 10)
            retStr = "0" + Integer.toString(i);
        else
            retStr = "" + i;
        return retStr;
    }


}
