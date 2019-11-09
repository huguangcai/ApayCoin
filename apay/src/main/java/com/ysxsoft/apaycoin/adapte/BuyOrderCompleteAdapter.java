package com.ysxsoft.apaycoin.adapte;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ysxsoft.apaycoin.R;
import com.ysxsoft.apaycoin.com.ActivityPageManager;
import com.ysxsoft.apaycoin.com.ListBaseAdapter;
import com.ysxsoft.apaycoin.com.SuperViewHolder;
import com.ysxsoft.apaycoin.impservice.ImpDeleteOrderService;
import com.ysxsoft.apaycoin.modle.DeleteOrderBean;
import com.ysxsoft.apaycoin.modle.SaleBuyOrderBean;
import com.ysxsoft.apaycoin.utils.ImageLoadUtil;
import com.ysxsoft.apaycoin.utils.NetWork;
import com.ysxsoft.apaycoin.view.LoginActivity;
import com.ysxsoft.apaycoin.widget.CircleImageView;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 描述： 购买订单完成适配器
 * 日期： 2018/11/16 0016 11:42
 * 作者： 胡
 * 公司：郑州亿生信科技有限公司
 */
public class BuyOrderCompleteAdapter extends ListBaseAdapter<SaleBuyOrderBean.DataBean> {
    private OnDeleteClickListener onDeleteClickListener;

    public BuyOrderCompleteAdapter(Context context) {
        super(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.sale_order_run_fragment_item_layout;
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, final int position) {
        final SaleBuyOrderBean.DataBean dataBean = mDataList.get(position);
        CircleImageView img_head = holder.getView(R.id.img_head);
        TextView tv_trade_type = holder.getView(R.id.tv_trade_type);
        TextView tv_time = holder.getView(R.id.tv_time);
        TextView tv_nike_name = holder.getView(R.id.tv_nike_name);
        TextView tv_num = holder.getView(R.id.tv_num);
        TextView tv_money = holder.getView(R.id.tv_money);
        TextView tv_sale_run = holder.getView(R.id.tv_sale_run);
        TextView tv_sale_time = holder.getView(R.id.tv_sale_time);
        TextView tv_cancle = holder.getView(R.id.tv_cancle);
        TextView tv_look = holder.getView(R.id.tv_look);
        TextView tv_delete = holder.getView(R.id.tv_delete);
        TextView tv_wait_check = holder.getView(R.id.tv_wait_check);

//        tv_trade_type.setText(dataBean.);  //交易类型
        ImageLoadUtil.GlideImageLoad(mContext, dataBean.getAvatar(), img_head);
        tv_time.setText(dataBean.getAdd_time());//发起时间
        tv_nike_name.setText(dataBean.getNickname());
        tv_money.setText(dataBean.getMoney());
        tv_num.setText(dataBean.getGold());

        if ("1".equals(dataBean.getFlag())) {
            tv_sale_run.setText("已完成");
            tv_cancle.setVisibility(View.GONE);
            tv_wait_check.setVisibility(View.GONE);
            tv_look.setVisibility(View.GONE);
            tv_sale_time.setVisibility(View.INVISIBLE);
            tv_delete.setVisibility(View.VISIBLE);
        }
        tv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                String oid = dataBean.getOid();
//                submitData(oid);
                if (onDeleteClickListener!=null){
                    onDeleteClickListener.onDeleteClick(position);
                }

            }
        });
    }

    private void submitData(String oid) {
        NetWork.getRetrofit()
                .create(ImpDeleteOrderService.class)
                .getCall(NetWork.getToken(), oid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<DeleteOrderBean>() {
                    private DeleteOrderBean deleteOrderBean;

                    @Override
                    public void onCompleted() {
                        Toast.makeText(mContext, deleteOrderBean.getMsg(), Toast.LENGTH_SHORT).show();
                        if ("0".equals(deleteOrderBean.getCode())) {
                            notifyDataSetChanged();
                        }else  if ("2".equals(deleteOrderBean.getCode())) {
                            SharedPreferences.Editor sp =mContext.getSharedPreferences("save_name_pwd", Context.MODE_PRIVATE).edit();
                            sp.clear();
                            sp.commit();
                            SharedPreferences.Editor is_first =mContext.getSharedPreferences("IS_FIRST", Context.MODE_PRIVATE).edit();
                            is_first.clear();
                            is_first.commit();
                            ActivityPageManager instance = ActivityPageManager.getInstance();
                            instance.finishAllActivity();
                            mContext.startActivity(new Intent(mContext,LoginActivity.class));
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(DeleteOrderBean deleteOrderBean) {
                        this.deleteOrderBean = deleteOrderBean;
                    }
                });
    }
    public interface OnDeleteClickListener{
        void onDeleteClick(int position);
    }
    public void setOnDeleteClickListener(OnDeleteClickListener onDeleteClickListener){
        this.onDeleteClickListener = onDeleteClickListener;
    }
}
