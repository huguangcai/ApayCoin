package com.ysxsoft.apaycoin.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.ysxsoft.apaycoin.R;

/**
 * 描述： TODO
 * 日期： 2018/11/26 0026 11:38
 * 作者： 胡
 * 公司：郑州亿生信科技有限公司
 */
public class MallGoodsDetialHeader extends RelativeLayout {

    public MallGoodsDetialHeader(Context context) {
        super(context);
        init(context);
    }

    public MallGoodsDetialHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MallGoodsDetialHeader(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void init(Context context) {
        inflate(context, R.layout.sample_header, this);
    }
}
