package com.ysxsoft.apaycoin.adapte;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.jdsjlzx.recyclerview.LuRecyclerViewAdapter;
import com.ysxsoft.apaycoin.R;
import com.ysxsoft.apaycoin.com.ListBaseAdapter;
import com.ysxsoft.apaycoin.com.SuperViewHolder;
import com.ysxsoft.apaycoin.impservice.ImpOnlineUnderGoodsService;
import com.ysxsoft.apaycoin.modle.GoodsListBean;
import com.ysxsoft.apaycoin.modle.OnlineMallGoodsListBean;
import com.ysxsoft.apaycoin.modle.OnlineUnderGoodsBean;
import com.ysxsoft.apaycoin.utils.AppUtil;
import com.ysxsoft.apaycoin.utils.ImageLoadUtil;
import com.ysxsoft.apaycoin.utils.NetWork;
import com.ysxsoft.apaycoin.view.MallGoodsDetialActivity;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 描述： 我的店铺  商品列表适配器
 * 日期： 2018/11/26 0026 09:12
 * 作者： 胡
 * 公司：郑州亿生信科技有限公司
 */
public class OnlineMallLookShopAdapter extends ListBaseAdapter<OnlineMallGoodsListBean.DataBean> {
    private int Is_flag = -1;
    private final LuRecyclerViewAdapter luRecyclerViewAdapter;

    public OnlineMallLookShopAdapter(Context context) {
        super(context);
        luRecyclerViewAdapter = new LuRecyclerViewAdapter(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.online_mall_fragment_item_layout;
    }

    @Override
    public void onBindItemHolder(final SuperViewHolder holder, final int position) {
        OnlineMallGoodsListBean.DataBean dataBean = mDataList.get(position);
        ImageView img_goods = holder.getView(R.id.img_goods);
        TextView tv_desc = holder.getView(R.id.tv_desc);
        TextView tv_price = holder.getView(R.id.tv_price);
        ImageLoadUtil.NewGoodsGlideImageLoad(mContext,dataBean.getIcon(),img_goods);
        tv_desc.setText(AppUtil.stringReplace(dataBean.getTitle()));
        tv_price.setText(dataBean.getMoney());
    }

}
