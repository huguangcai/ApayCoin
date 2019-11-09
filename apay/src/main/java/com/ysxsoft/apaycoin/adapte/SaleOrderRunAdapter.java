package com.ysxsoft.apaycoin.adapte;

import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ysxsoft.apaycoin.R;
import com.ysxsoft.apaycoin.com.ListBaseAdapter;
import com.ysxsoft.apaycoin.com.SuperViewHolder;
import com.ysxsoft.apaycoin.impservice.ImpCancleOrderService;
import com.ysxsoft.apaycoin.modle.CancleOrderBean;
import com.ysxsoft.apaycoin.modle.SaleBuyOrderBean;
import com.ysxsoft.apaycoin.utils.AppUtil;
import com.ysxsoft.apaycoin.utils.CountDownTimeHelper;
import com.ysxsoft.apaycoin.utils.ImageLoadUtil;
import com.ysxsoft.apaycoin.utils.NetWork;
import com.ysxsoft.apaycoin.view.LookMyOrderActivity;
import com.ysxsoft.apaycoin.widget.CircleImageView;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 描述： 销售订单运行中适配器
 * 日期： 2018/11/16 0016 11:42
 * 作者： 胡
 * 公司：郑州亿生信科技有限公司
 */
public class SaleOrderRunAdapter extends ListBaseAdapter<SaleBuyOrderBean.DataBean> {
    private OnCancleClickListener onCancleClickListener;
    public CountDownTimer timer;

    public SaleOrderRunAdapter(Context context) {
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
        final TextView tv_sale_time = holder.getView(R.id.tv_sale_time);
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
        switch (dataBean.getFlag()) {
            case "0":
                tv_sale_run.setText("出售中");
                tv_sale_time.setVisibility(View.INVISIBLE);
                tv_cancle.setVisibility(View.VISIBLE);
                tv_look.setVisibility(View.GONE);
                tv_delete.setVisibility(View.GONE);
                tv_wait_check.setVisibility(View.GONE);
                break;

            case "1":
                break;

            case "2":
                break;

            case "3":
                tv_sale_run.setText("等待买家打款");
                tv_sale_time.setVisibility(View.INVISIBLE);
                tv_cancle.setVisibility(View.GONE);
                tv_look.setVisibility(View.GONE);
                tv_delete.setVisibility(View.GONE);
                tv_wait_check.setVisibility(View.GONE);

                break;
            case "4":
                tv_sale_run.setText("交易中");
                tv_sale_time.setVisibility(View.VISIBLE);
                final String dk_time = dataBean.getDk_time();
                int i = Integer.valueOf(dk_time) * 1000;
                final Long aLong = Long.valueOf(i);
                if ("0".equals(dk_time)) {
                    tv_sale_time.setText("(00:00:00)");
                } else {
                    timer = new CountDownTimer(aLong, 1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                            tv_sale_time.setText("(" + AppUtil.secToTime(Integer.valueOf(String.valueOf(millisUntilFinished)) / 1000) + ")");
                        }

                        @Override
                        public void onFinish() {
                            tv_sale_time.setText("(00:00:00)");

                        }
                    }.start();
                }
                tv_cancle.setVisibility(View.GONE);
                tv_look.setVisibility(View.VISIBLE);
                tv_look.setText("查看");
                tv_delete.setVisibility(View.GONE);
                tv_wait_check.setVisibility(View.GONE);
                break;
        }
        tv_look.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String oid = dataBean.getOid();
                Intent intent = new Intent(mContext, LookMyOrderActivity.class);
                intent.putExtra("type", "look");
                intent.putExtra("oid", oid);
                mContext.startActivity(intent);
            }
        });
        tv_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                String oid = dataBean.getOid();
//                submitData(oid);
                if (onCancleClickListener != null) {
                    onCancleClickListener.TvCacle(position);
                }
            }
        });
    }

    public interface OnCancleClickListener {
        void TvCacle(int position);
    }

    public void setOnCancleClickListener(OnCancleClickListener onCancleClickListener) {
        this.onCancleClickListener = onCancleClickListener;
    }

}
