package com.ysxsoft.apaycoin.widget;

import android.view.KeyEvent;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputConnectionWrapper;

/**
 * 描述： 限制输入框为汉字
 * 日期： 2018/11/7 0007 16:19
 * 作者： 胡
 * 公司：郑州亿生信科技有限公司
 */
class mInputConnecttion extends InputConnectionWrapper implements InputConnection {

    public mInputConnecttion(InputConnection target, boolean mutable) {
        super(target, mutable);
    }

    /**
     * 对输入的内容进行拦截
     *
     * @param text
     * @param newCursorPosition
     * @return
     */
    @Override
    public boolean commitText(CharSequence text, int newCursorPosition) {
        // 只能输入汉字
        if (!text.toString().matches("[\u4e00-\u9fa5]+")) {
            return false;
        }
        return super.commitText(text, newCursorPosition);
    }

    @Override
    public boolean sendKeyEvent(KeyEvent event) {
        return super.sendKeyEvent(event);
    }

    @Override
    public boolean setSelection(int start, int end) {
        return super.setSelection(start, end);
    }

}
