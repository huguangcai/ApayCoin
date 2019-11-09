package com.ysxsoft.apaycoin.adapte;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ysxsoft.apaycoin.R;
import com.ysxsoft.apaycoin.com.ListBaseAdapter;
import com.ysxsoft.apaycoin.com.SuperViewHolder;
import com.ysxsoft.apaycoin.modle.NewAddGoodsEditorListBean;
import com.ysxsoft.apaycoin.utils.ImageLoadUtil;

/**
 * 描述： TODO
 * 日期： 2018/11/26 0026 15:46
 * 作者： 胡
 * 公司：郑州亿生信科技有限公司
 */
public class NewAddGoodsEditorListAdapter extends ListBaseAdapter<NewAddGoodsEditorListBean.DataBean> {

    public NewAddGoodsEditorListAdapter(Context context) {
        super(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.goods_detail_item_layout;
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, int position) {
        NewAddGoodsEditorListBean.DataBean dataBean = mDataList.get(position);
        TextView tv_content = holder.getView(R.id.tv_content);
        ImageView img_goods = holder.getView(R.id.img_goods);
        if ("1".equals(dataBean.getType())) {//type 1 是图片  2文字
            img_goods.setVisibility(View.VISIBLE);
            tv_content.setVisibility(View.GONE);
            ImageLoadUtil.NewGoodsGlideImageLoad(mContext, dataBean.getIcon(), img_goods);
        } else {
            img_goods.setVisibility(View.GONE);
            tv_content.setVisibility(View.VISIBLE);
            tv_content.setText(dataBean.getVals());
        }
    }
}
