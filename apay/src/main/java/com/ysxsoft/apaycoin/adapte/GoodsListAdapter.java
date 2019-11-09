package com.ysxsoft.apaycoin.adapte;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.jdsjlzx.interfaces.OnItemLongClickListener;
import com.github.jdsjlzx.recyclerview.LuRecyclerViewAdapter;
import com.ysxsoft.apaycoin.R;
import com.ysxsoft.apaycoin.com.ActivityPageManager;
import com.ysxsoft.apaycoin.com.ListBaseAdapter;
import com.ysxsoft.apaycoin.com.SuperViewHolder;
import com.ysxsoft.apaycoin.impservice.ImpDeleteGoodsService;
import com.ysxsoft.apaycoin.impservice.ImpOnlineUnderGoodsService;
import com.ysxsoft.apaycoin.modle.DeleteGoodsBean;
import com.ysxsoft.apaycoin.modle.GoodsListBean;
import com.ysxsoft.apaycoin.modle.OnlineUnderGoodsBean;
import com.ysxsoft.apaycoin.utils.AppUtil;
import com.ysxsoft.apaycoin.utils.ImageLoadUtil;
import com.ysxsoft.apaycoin.utils.NetWork;
import com.ysxsoft.apaycoin.view.LoginActivity;
import com.ysxsoft.apaycoin.view.MallGoodsDetialActivity;
import com.ysxsoft.apaycoin.widget.LongDialog;
import com.ysxsoft.apaycoin.widget.SwipeMenuView;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 描述： 我的店铺  商品列表适配器
 * 日期： 2018/11/26 0026 09:12
 * 作者： 胡
 * 公司：郑州亿生信科技有限公司
 */
public class GoodsListAdapter extends ListBaseAdapter<GoodsListBean.DataBean> {
    private int Is_flag=-1;
    private final LuRecyclerViewAdapter luRecyclerViewAdapter;
    private OnUnderOnClickListener onUnderOnClickListener;

    public GoodsListAdapter(Context context) {
        super(context);
        luRecyclerViewAdapter = new LuRecyclerViewAdapter(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.goods_list_item_layout;
    }

    @Override
    public void onBindItemHolder(final SuperViewHolder holder, final int position) {
        final GoodsListBean.DataBean dataBean = mDataList.get(position);
        ImageView img_head = holder.getView(R.id.img_head);
        TextView tv_content = holder.getView(R.id.tv_content);
        TextView tv_price = holder.getView(R.id.tv_price);
        TextView tv_stock_num = holder.getView(R.id.tv_stock_num);
        TextView tv_under_goods = holder.getView(R.id.tv_under_goods);
        ImageLoadUtil.NewGoodsGlideImageLoad(mContext, dataBean.getImgs(), img_head);
        tv_content.setText(AppUtil.stringReplace(dataBean.getTitle()));
        tv_price.setText(dataBean.getMoney());
        tv_stock_num.setText("库存："+dataBean.getStock());
        String flag = dataBean.getFlag();
        if ("0".equals(flag)) { //flag  1  上架  0  下架
            tv_under_goods.setText("下架");
            Is_flag=0;
        } else {
            tv_under_goods.setText("上架");
            Is_flag=1;
        }
        final String pid = dataBean.getPid();

        tv_under_goods.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                submit();
                if (onUnderOnClickListener!=null){
                    onUnderOnClickListener.UnderOnClick(position);
                }
            }

            private void submit() {
                NetWork.getRetrofit()
                        .create(ImpOnlineUnderGoodsService.class)
                        .getCall(NetWork.getToken(),pid,Is_flag+"")
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<OnlineUnderGoodsBean>() {
                            private OnlineUnderGoodsBean onlineUnderGoodsBean;

                            @Override
                            public void onCompleted() {
                                Toast.makeText(mContext,onlineUnderGoodsBean.getMsg(),Toast.LENGTH_SHORT).show();
                                if ("0".equals(onlineUnderGoodsBean.getCode())) {
                                    notifyDataSetChanged();
                                }else if ("2" .equals( onlineUnderGoodsBean.getCode())) {
                                    SharedPreferences.Editor sp = mContext.getSharedPreferences("save_name_pwd", Context.MODE_PRIVATE).edit();
                                    sp.clear();
                                    sp.commit();
                                    SharedPreferences.Editor is_first = mContext.getSharedPreferences("IS_FIRST", Context.MODE_PRIVATE).edit();
                                    is_first.clear();
                                    is_first.commit();
                                    ActivityPageManager instance = ActivityPageManager.getInstance();
                                    instance.finishAllActivity();
                                    mContext.startActivity(new Intent(mContext,LoginActivity.class));
                                }
                            }

                            @Override
                            public void onError(Throwable e) {
                                Toast.makeText(mContext,e.getMessage(),Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onNext(OnlineUnderGoodsBean onlineUnderGoodsBean) {
                                this.onlineUnderGoodsBean = onlineUnderGoodsBean;
                            }
                        });
            }
        });
    }
    public interface OnUnderOnClickListener{
        void UnderOnClick(int position);
    }
    public void setOnUnderOnClickListener(OnUnderOnClickListener onUnderOnClickListener){
        this.onUnderOnClickListener = onUnderOnClickListener;
    }
}
