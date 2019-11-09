package com.ysxsoft.apaycoin.adapte;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.ysxsoft.apaycoin.R;
import com.ysxsoft.apaycoin.com.ListBaseAdapter;
import com.ysxsoft.apaycoin.com.SuperViewHolder;
import com.ysxsoft.apaycoin.modle.NoticeListBean;

/**
 * 描述： 通知适配器
 * 日期： 2018/11/9 0009 09:32
 * 作者： 胡
 * 公司：郑州亿生信科技有限公司
 */
public class NoticeAdapter extends ListBaseAdapter<NoticeListBean.DataBean> {

    public NoticeAdapter(Context context) {
        super(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.notice_item_layout;
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, int position) {
        NoticeListBean.DataBean dataBean = mDataList.get(position);
        TextView tv_content_title = holder.getView(R.id.tv_content_title);
        TextView tv_red_cricle = holder.getView(R.id.tv_red_cricle);
        tv_red_cricle.setVisibility(View.GONE);
        TextView tv_content = holder.getView(R.id.tv_content);
        TextView tv_time = holder.getView(R.id.tv_time);
        tv_content_title.setText(dataBean.getTitle());
        tv_content.setText(dataBean.getDescr());
        tv_time.setText(dataBean.getAdd_time());
    }


}
