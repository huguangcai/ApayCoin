package com.ysxsoft.apaycoin.adapte;

import android.content.Context;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.ysxsoft.apaycoin.R;
import com.ysxsoft.apaycoin.com.ListBaseAdapter;
import com.ysxsoft.apaycoin.com.SuperViewHolder;
import com.ysxsoft.apaycoin.modle.PersonInfoBean;

/**
 * 描述： 个人消息适配器
 * 日期： 2018/11/20 0020 09:00
 * 作者： 胡
 * 公司：郑州亿生信科技有限公司
 */
public class PersonInfoAdapter extends ListBaseAdapter<PersonInfoBean.DataBean> {
    private boolean b;

    public PersonInfoAdapter(Context context) {
        super(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.person_info_item_layout;
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, int position) {
        PersonInfoBean.DataBean dataBean = mDataList.get(position);
        CheckBox cb_box = holder.getView(R.id.cb_box);
        TextView tv_time = holder.getView(R.id.tv_time);
        TextView tv_desc = holder.getView(R.id.tv_desc);
        TextView tv_red_cricle = holder.getView(R.id.tv_red_cricle);
        tv_time.setText(dataBean.getAdd_time());
        tv_desc.setText(dataBean.getVals());
        switch (dataBean.getNews()) {
            case "0":
                tv_red_cricle.setVisibility(View.VISIBLE);
                break;
            case "1":
                tv_red_cricle.setVisibility(View.GONE);
                break;
        }

        if (b) {
            cb_box.setVisibility(View.VISIBLE);
        } else {
            cb_box.setVisibility(View.GONE);
        }
    }

    public void isShow(boolean b) {
        this.b = b;
        notifyDataSetChanged();
    }
}
