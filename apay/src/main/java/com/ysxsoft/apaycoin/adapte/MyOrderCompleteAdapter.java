package com.ysxsoft.apaycoin.adapte;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ysxsoft.apaycoin.R;
import com.ysxsoft.apaycoin.com.ListBaseAdapter;
import com.ysxsoft.apaycoin.com.SuperViewHolder;
import com.ysxsoft.apaycoin.modle.MyOrderRunCompleteBean;
import com.ysxsoft.apaycoin.utils.ImageLoadUtil;

import static com.ysxsoft.apaycoin.R.drawable.btn_cancle_shape_stroke;

public class MyOrderCompleteAdapter extends ListBaseAdapter<MyOrderRunCompleteBean.DataBean> {

    private OnDeleteListener onDeleteListener;

    public MyOrderCompleteAdapter(Context context) {
        super(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.my_order_running_item_layout;
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, final int position) {
        MyOrderRunCompleteBean.DataBean dataBean = mDataList.get(position);
        TextView tv_mall_name = holder.getView(R.id.tv_mall_name);
        TextView tv_time = holder.getView(R.id.tv_time);
        ImageView img_icon = holder.getView(R.id.img_icon);
        TextView tv_desc = holder.getView(R.id.tv_desc);
        TextView tv_money = holder.getView(R.id.tv_money);
        TextView tv_is_fahuo = holder.getView(R.id.tv_is_fahuo);
        TextView tv_check = holder.getView(R.id.tv_check);
        tv_mall_name.setText(dataBean.getShopname());
        tv_time.setText(dataBean.getAdd_time());
        ImageLoadUtil.GlideImageLoad(mContext, dataBean.getIcon(), img_icon);
        tv_desc.setText(dataBean.getTitle());
        tv_money.setText(dataBean.getMoney());
        switch (dataBean.getFlag()) {
            case "2":
                tv_is_fahuo.setText("已完成");
                tv_check.setText("删除订单");
                tv_check.setVisibility(View.GONE);
                tv_check.setBackgroundResource(R.drawable.btn_cancle_shape_stroke);
                tv_check.setTextColor(mContext.getResources().getColor(R.color.btn_cancle_bg));
                tv_check.setPadding(20,10,20,10);
                break;
        }
        tv_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onDeleteListener!=null){
                    onDeleteListener.DeleteClick(position);
                }
            }
        });

    }
    public interface OnDeleteListener{
        void DeleteClick(int position);
    }
    public void setOnDeleteListener(OnDeleteListener onDeleteListener){
        this.onDeleteListener = onDeleteListener;
    }
}
