package com.ysxsoft.apaycoin.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.widget.EditText;

/**
 * 描述： 限制只能输入汉字的输入框
 * 日期： 2018/11/7 0007 16:04
 * 作者： 胡
 * 公司：郑州亿生信科技有限公司
 */
@SuppressLint("AppCompatCustomView")
public class LimitEditText extends EditText {

    public LimitEditText(Context context) {
        super(context);
    }

    public LimitEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LimitEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 输入法
     *
     * @param outAttrs
     * @return
     */
    @Override
    public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
        return new mInputConnecttion(super.onCreateInputConnection(outAttrs),
                false);
    }
}

