package com.ysxsoft.apaycoin.adapte;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ysxsoft.apaycoin.R;
import com.ysxsoft.apaycoin.com.ListBaseAdapter;
import com.ysxsoft.apaycoin.com.SuperViewHolder;
import com.ysxsoft.apaycoin.modle.ShareRecordBean;
import com.ysxsoft.apaycoin.utils.ImageLoadUtil;
import com.ysxsoft.apaycoin.widget.CircleImageView;

/**
 * 描述： 分享记录适配器
 * 日期： 2018/11/7 0007 17:02
 * 作者： 胡
 * 公司：郑州亿生信科技有限公司
 */
public class ShareRecordAdapter extends ListBaseAdapter<ShareRecordBean.DataBean> {

    public ShareRecordAdapter(Context context) {
        super(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.share_record_item_layout;
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, int position) {
        ShareRecordBean.DataBean dataBean = mDataList.get(position);
        ImageView img_head = holder.getView(R.id.img_head);
        TextView tv_name = holder.getView(R.id.tv_name);
        TextView tv_uid = holder.getView(R.id.tv_uid);
        TextView tv_time = holder.getView(R.id.tv_time);

        ImageLoadUtil.GlideImageLoad(mContext,dataBean.getIcon(),img_head);
        tv_name.setText(dataBean.getNickname());
        tv_uid.setText(dataBean.getUid());
        tv_time.setText(dataBean.getCreate_time());

    }
}
