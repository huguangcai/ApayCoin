package com.ysxsoft.apaycoin.toast;

/**
 * 描述： 默认样式接口
 * 日期： 2018/10/23 0023 16:10
 * 作者： 胡
 * 公司：郑州亿生信科技有限公司
 */
public interface IToastStyle {
    int getGravity();//Toast的重心
    int getXOffset();//X轴偏移
    int getYOffset();//Y轴偏移
    int getZ();//Z轴偏移

    int getCornerRadius();//圆角大小
    int getBackgroundColor();//背景大小

    int getTextColor();//文本颜色
    float getTextSize();//字体大小
    int getMaxLines();//最大行数

    int getPaddingLeft();//距离左边内边距
    int getPaddingRight();//距离右边内边距
    int getPaddingTop();//距离上边内边距
    int getPaddingBottom();//距离底部内边距

}
