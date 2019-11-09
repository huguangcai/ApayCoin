package com.ysxsoft.apaycoin.toast;

import android.view.Gravity;


/**
 * 描述： 默认黑色样式实现
 * 日期： 2018/10/23 0023 16:32
 * 作者： 胡
 * 公司：郑州亿生信科技有限公司
 */
public class ToastBlackStyle implements IToastStyle {
    @Override
    public int getGravity() {
        return Gravity.CENTER;
    }

    @Override
    public int getXOffset() {
        return 0;
    }

    @Override
    public int getYOffset() {
        return 0;
    }

    @Override
    public int getZ() {
        return 30;
    }

    @Override
    public int getCornerRadius() {
        return 6;
    }

    @Override
    public int getBackgroundColor() {
        return 0X88000000;
    }

    @Override
    public int getTextColor() {
        return 0XEEFFFFFF;
    }

    @Override
    public float getTextSize() {
        return 14;
    }

    @Override
    public int getMaxLines() {
        return 3;
    }

    @Override
    public int getPaddingLeft() {
        return 24;
    }

    @Override
    public int getPaddingRight() {
        return getPaddingLeft();
    }

    @Override
    public int getPaddingTop() {
        return 16;
    }

    @Override
    public int getPaddingBottom() {
        return getPaddingTop();
    }
}
