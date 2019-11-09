package com.ysxsoft.apaycoin.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.ysxsoft.apaycoin.R;


/**
 * 描述： 加载提示框
 * 日期： 2018/10/23 0023 10:28
 * 作者： 胡
 * 公司：郑州亿生信科技有限公司
 */
public class CustomDialog extends Dialog {
    private String message;
    private Context context;
    private static CustomDialog customdialog;

    public CustomDialog(Context context, String message) {
        super(context);
        this.context = context;
        getContext().setTheme(android.R.style.Theme_DeviceDefault_Dialog_NoActionBar_MinWidth);
        this.message = message;
        setContentView(R.layout.updata_dialog);
        initParms();
        initView();
    }

    private void initView() {
        TextView tv_dialog_message = (TextView) findViewById(R.id.tv_dialog_message);
        if (!TextUtils.isEmpty(message)) {
            tv_dialog_message.setVisibility(View.VISIBLE);
            tv_dialog_message.setText(message);
        }
    }

    private void initParms() {
        /**
         * setCancelable(true) 弹出后点击或返回键不消失
         * setCanceledOnTouchOutside(false)弹出后点击不消失  按返回键消失
         */
        setCancelable(false);
        setCanceledOnTouchOutside(true);
        WindowManager windowManager = getWindow().getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams wm = getWindow().getAttributes();
        wm.width = (int) (display.getWidth() * 0.7);
        Window window = getWindow();
        window.setAttributes(wm);
        window.getDecorView().getBackground().setAlpha(0);
    }

    public static void show(Context context) {
        show(context, true, null);
    }

    public static void show(Context context, String message) {
        show(context, true, null);
    }

    public static void show(Context context, int resid) {
        show(context, true, null);
    }

    private static void show(Context context, boolean cancle, String message) {
        if (context instanceof Activity) {
            if (((Activity) context).isFinishing()) {
                return;
            }
        }
        if (customdialog != null && customdialog.isShowing()) {
            return;
        }
        customdialog = new CustomDialog(context, message);
        customdialog.show();
    }

    public static void dismiss(Context context){
        if (context instanceof Activity){
            if (((Activity) context).isFinishing()){
                customdialog=null;
                return;
            }
        }
    }
}
