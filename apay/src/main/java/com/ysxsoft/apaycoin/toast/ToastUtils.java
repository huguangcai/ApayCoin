package com.ysxsoft.apaycoin.toast;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ysxsoft.apaycoin.utils.AppUtil;

/**
 * 描述： Toast工具类
 * 日期： 2018/10/23 0023 16:18
 * 作者： 胡
 * 公司：郑州亿生信科技有限公司
 */
public class ToastUtils {
    private static IToastStyle sDefaultStyle;
    private static Toast sToast;

    public static void init(Context context){

        // 检查默认样式是否为空，如果是就创建一个默认样式
        if (sDefaultStyle == null) {
            sDefaultStyle = new ToastBlackStyle();
        }
        //如果context不是全局上下文  切换为全局上下文
        if (context!=context.getApplicationContext()){
            context=context.getApplicationContext();
        }
        GradientDrawable drawable=new GradientDrawable();
        drawable.setColor(sDefaultStyle.getBackgroundColor());//设置背景
        drawable.setCornerRadius(AppUtil.dip2px(context,sDefaultStyle.getCornerRadius()));

        TextView textView=new TextView(context);
        textView.setTextColor(sDefaultStyle.getTextColor());
        textView.setTextSize(sDefaultStyle.getTextSize());
        textView.setPadding(AppUtil.dip2px(context,sDefaultStyle.getPaddingLeft()),
                AppUtil.dip2px(context,sDefaultStyle.getPaddingTop()),
                AppUtil.dip2px(context,sDefaultStyle.getPaddingRight()),
                AppUtil.dip2px(context,sDefaultStyle.getPaddingBottom()));

        textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT));
        // setBackground API版本兼容
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.JELLY_BEAN){
            textView.setBackground(drawable);
        }else {
            textView.setBackgroundDrawable(drawable);
        }
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
            textView.setZ(sDefaultStyle.getZ());
        }
        sToast=new XToast(context);
        sToast.setGravity(sDefaultStyle.getGravity(), sDefaultStyle.getXOffset(), sDefaultStyle.getYOffset());
        sToast.setView(textView);

    }


    /**
     * 显示toast
     * @param text
     */
    public static void show(CharSequence text){
        checkToastState();
        if (text==null||text.equals("")){
            return;
        }
        // 如果显示的文字超过了10个就显示长吐司，否则显示短吐司
        if (text.length()>20){
            sToast.setDuration(Toast.LENGTH_LONG);
        }else {
            sToast.setDuration(Toast.LENGTH_SHORT);
        }
        sToast.setText(text);
        sToast.show();
    }
    /**
     * 显示一个吐司
     *
     * @param id      如果传入的是正确的string id就显示对应字符串
     *                如果不是则显示一个整数的string
     */
    public static void show(int id) {
        checkToastState();
        try {
            // 如果这是一个资源id
            show(sToast.getView().getContext().getResources().getText(id));
        } catch (Resources.NotFoundException ignored) {
            // 如果这是一个int类型
            show(String.valueOf(id));
        }
    }
    /**
     *核对toast的状态
     * 如果未初始化请先调用ToastUtils#init(Context)
     */
    private static void checkToastState() {
        if (sToast==null){
            throw new IllegalStateException("ToastUtils has not been initialized");
        }
    }
}
