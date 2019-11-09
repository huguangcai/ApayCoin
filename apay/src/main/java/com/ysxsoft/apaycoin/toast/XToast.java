package com.ysxsoft.apaycoin.toast;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


/**
 * 描述：
 * 日期： 2018/10/23 0023 16:49
 * 作者： 胡
 * 公司：郑州亿生信科技有限公司
 */
public class XToast extends Toast implements Runnable{
    private Handler mHandler = new Handler(Looper.getMainLooper()); // 吐司处理消息线程
    private TextView mTextView;//toast的消息
    private CharSequence mText; // 吐司显示的文本
    XToast(Context context) {
        super(context);
    }

    @Override
    public void setView(View view) {
        super.setView(view);
        mTextView = new TextView(view.getContext());
        if (mTextView != null) {
            return;
        } else if (view instanceof TextView) {
            mTextView = (TextView) view;
            return;
        } else if (view instanceof ViewGroup) {
            this.mTextView = getTextView((ViewGroup) view);
            if (this.mTextView != null) return;
        }
        // 如果设置的布局没有包含一个TextView则抛出异常，必须要包含一个TextView作为Message对象
        throw new IllegalArgumentException("The layout must contain a TextView");
    }

    /**
     * 获取TextView
     * @param group
     * @return
     */
    private TextView getTextView(ViewGroup group) {
        for (int i = 0; i < group.getChildCount(); i++) {
            View view = group.getChildAt(i);
            if ((view instanceof TextView)) {
                return (TextView) view;
            } else if (view instanceof ViewGroup) {
                TextView textView = getTextView((ViewGroup) view);
                if (textView != null) return textView;
            }
        }
        return null;
    }

    @Override
    public void setText(CharSequence s) {
        // 记录本次吐司欲显示的文本
        mText = s;
    }

    @Override
    public void show() {
        // 移除之前显示吐司的任务
        mHandler.removeCallbacks(this);
        // 添加一个显示吐司的任务
        mHandler.postDelayed(this, 300);
    }

    @Override
    public void cancel() {
        super.cancel();
        mHandler.removeCallbacks(this);
    }

    @Override
    public void run() {
        // 设置吐司文本
        mTextView.setText(mText);
        // 显示吐司
        super.show();
    }
}
