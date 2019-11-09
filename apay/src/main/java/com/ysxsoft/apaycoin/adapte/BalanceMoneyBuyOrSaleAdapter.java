package com.ysxsoft.apaycoin.adapte;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ysxsoft.apaycoin.R;
import com.ysxsoft.apaycoin.com.ActivityPageManager;
import com.ysxsoft.apaycoin.com.ListBaseAdapter;
import com.ysxsoft.apaycoin.com.SuperViewHolder;
import com.ysxsoft.apaycoin.impservice.ImpBuyOrSaleService;
import com.ysxsoft.apaycoin.modle.BalanceMoneyBuyOrSaleBean;
import com.ysxsoft.apaycoin.modle.BuyOrSaleBean;
import com.ysxsoft.apaycoin.utils.ImageLoadUtil;
import com.ysxsoft.apaycoin.utils.NetWork;
import com.ysxsoft.apaycoin.view.LoginActivity;
import com.ysxsoft.apaycoin.widget.BalanceMoneyBuyOrSaleDialog;
import com.ysxsoft.apaycoin.widget.CircleImageView;
import com.ysxsoft.apaycoin.widget.PayPwdEditText;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 描述： 余额购买适配器
 * 日期： 2018/11/16 0016 15:10
 * 作者： 胡
 * 公司：郑州亿生信科技有限公司
 */
public class BalanceMoneyBuyOrSaleAdapter extends ListBaseAdapter<BalanceMoneyBuyOrSaleBean.DataBean> {
    private int type = -1;
    private OnBalanceMoneyBuyAndSale onBalanceMoneyBuyAndSale;

    public BalanceMoneyBuyOrSaleAdapter(Context context) {
        super(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.balance_money_buy_item_layout;
    }

    @Override
    public void onBindItemHolder(final SuperViewHolder holder, final int position) {
        final BalanceMoneyBuyOrSaleBean.DataBean dataBean = mDataList.get(position);
        CircleImageView img_head = holder.getView(R.id.img_head);
        TextView tv_nike_name = holder.getView(R.id.tv_nike_name);
        TextView tv_limit_num = holder.getView(R.id.tv_limit_num);
        TextView tv_balance_money_buy = holder.getView(R.id.tv_balance_money_buy);
        TextView tv_money = holder.getView(R.id.tv_money);
        TextView tv_buy_sale = holder.getView(R.id.tv_buy_sale);
        switch (dataBean.getZf_type()) {
            case 0 + "":
                tv_balance_money_buy.setVisibility(View.VISIBLE);
                tv_buy_sale.setVisibility(View.VISIBLE);
                break;
            case 1 + "":
                tv_balance_money_buy.setVisibility(View.VISIBLE);
                tv_buy_sale.setVisibility(View.GONE);
                break;
            case 2 + "":
                tv_balance_money_buy.setVisibility(View.GONE);
                tv_buy_sale.setVisibility(View.VISIBLE);
                break;

        }

        ImageLoadUtil.GlideImageLoad(mContext, dataBean.getAvatar(), img_head);
        tv_nike_name.setText(dataBean.getNickname());
        tv_limit_num.setText(dataBean.getGold());
        tv_money.setText(dataBean.getMoney());
        final String oid = dataBean.getOid();
        final String nickname = dataBean.getNickname();
        tv_buy_sale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onBalanceMoneyBuyAndSale!=null){
                    onBalanceMoneyBuyAndSale.BalanceSalebtn(position);
                }
                type=2;
//                comData(dataBean, nickname, oid);
            }
        });
        tv_balance_money_buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onBalanceMoneyBuyAndSale!=null){
                    onBalanceMoneyBuyAndSale.BalanceBuybtn(position);
                }
                type=1;
//                comData(dataBean, nickname, oid);
            }
        });
    }

    private void comData(BalanceMoneyBuyOrSaleBean.DataBean dataBean, String nickname, final String oid) {
        final BalanceMoneyBuyOrSaleDialog dialog=new BalanceMoneyBuyOrSaleDialog(mContext);
        TextView tv_limit_num = dialog.findViewById(R.id.tv_limit_num);
        TextView tv_nike_name = dialog.findViewById(R.id.tv_nike_name);
        TextView tv_current_price = dialog.findViewById(R.id.tv_current_price);
        TextView tv_balance_money = dialog.findViewById(R.id.tv_balance_money);
        TextView tv_buy_or_sale = dialog.findViewById(R.id.tv_buy_or_sale);
        EditText ed_apay_num = dialog.findViewById(R.id.ed_apay_num);
        tv_limit_num.setText(dataBean.getGold());
        tv_nike_name.setText(nickname);
//        tv_current_price.setText(dataBean.getMoney());
        tv_balance_money.setText(dataBean.getMoney());
        tv_buy_or_sale.setText("购买");
        PayPwdEditText ed_ppet = dialog.findViewById(R.id.ed_ppet);
        ed_ppet.setOnTextFinishListener(new PayPwdEditText.OnTextFinishListener() {
            @Override
            public void onFinish(String str) {
                subimitData(oid,str);
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void subimitData(String oid, String str) {
        NetWork.getRetrofit().create(ImpBuyOrSaleService.class)
                .getCall(NetWork.getToken(),oid,type+"",str)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BuyOrSaleBean>() {
                    private BuyOrSaleBean buyOrSaleBean;

                    @Override
                    public void onCompleted() {
                        Toast.makeText(mContext,buyOrSaleBean.getMsg(),Toast.LENGTH_SHORT).show();
                        notifyDataSetChanged();
                         if ("2".equals(buyOrSaleBean.getCode())) {
                            SharedPreferences.Editor sp = mContext.getSharedPreferences("save_name_pwd", Context.MODE_PRIVATE).edit();
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
                        Toast.makeText(mContext,e.getMessage(),Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(BuyOrSaleBean buyOrSaleBean) {
                        this.buyOrSaleBean = buyOrSaleBean;
                    }
                });
    }

    public interface OnBalanceMoneyBuyAndSale{
        void BalanceBuybtn(int position);
        void BalanceSalebtn(int position);
    }
    public void setOnBalanceMoneyBuyAndSale(OnBalanceMoneyBuyAndSale onBalanceMoneyBuyAndSale){
        this.onBalanceMoneyBuyAndSale = onBalanceMoneyBuyAndSale;
    }
}
