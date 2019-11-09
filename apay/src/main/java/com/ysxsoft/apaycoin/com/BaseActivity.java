package com.ysxsoft.apaycoin.com;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.ysxsoft.apaycoin.R;
import com.ysxsoft.apaycoin.utils.CustomDialog;
import com.ysxsoft.apaycoin.utils.MToast;


/**
 * 描述： activity基类
 * 日期： 2018/10/23 0023 09:55
 * 作者： 胡
 * 公司：郑州亿生信科技有限公司
 */
public class BaseActivity extends AppCompatActivity {

    private ActivityPageManager mActivityPageManager;
    public Context mContext;
    public int padding=4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityPageManager= ActivityPageManager.getInstance();
        mActivityPageManager.addActivity(this);
        mContext=this;
    }
    /**
     * 获取指定Id的View
     */
    protected <T extends View> T getViewById(int resId) {
        return (T) findViewById(resId);
    }

    /**
     * 弹出Toast信息
     */
    protected void  showToastMessage(String message) {
        Toast.makeText(getApplicationContext(),message, MToast.LENGTH_SHORT).show();
    }

    /**
     * 弹出Toast信息
     */
    protected void  showToastMessage(int resId) {
        showToastMessage(getResources().getString(resId));
    }

    /**
     * 打印Log，tag默认为类的名字
     */
    protected void log(String text){
        log(this.getClass().getName(),text);
    }

    /**
     * 打印Log
     */
    protected void log(String tag, String text){
        Log.i(tag, text);
    }

    protected void startActivity(Class clazz){
        startActivity(new Intent(BaseActivity.this,clazz));
    }

    /**
     * 全透状态栏
     */
    protected void setStatusBarFullTransparent() {
        if (Build.VERSION.SDK_INT >= 21) {//21表示5.0
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= 19) {//19表示4.4
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //虚拟键盘也透明
            //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }

    /**
     * 半透明状态栏
     */
    protected void setHalfTransparent() {
        if (Build.VERSION.SDK_INT >= 21) {//21表示5.0
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        } else if (Build.VERSION.SDK_INT >= 19) {//19表示4.4
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //虚拟键盘也透明
            // getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }

    /**
     * 如果需要内容紧贴着StatusBar
     * 应该在对应的xml布局文件中，设置根布局fitsSystemWindows=true。
     */
    private View contentViewGroup;

    protected void setFitSystemWindow(boolean fitSystemWindow) {
        if (contentViewGroup == null) {
            contentViewGroup = ((ViewGroup) findViewById(android.R.id.content)).getChildAt(0);
        }
        contentViewGroup.setFitsSystemWindows(fitSystemWindow);
    }

    /**
     * 获取状态栏高度
     * @return
     */
    protected int getStateBar(){
        int result = 0;
        int resourceId = this.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = this.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

}
