package com.ysxsoft.apaycoin.adapte;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ysxsoft.apaycoin.R;
import com.ysxsoft.apaycoin.com.ActivityPageManager;
import com.ysxsoft.apaycoin.com.ListBaseAdapter;
import com.ysxsoft.apaycoin.com.SuperViewHolder;
import com.ysxsoft.apaycoin.impservice.MerchantFaHuoService;
import com.ysxsoft.apaycoin.modle.MerchantFaHuoBean;
import com.ysxsoft.apaycoin.modle.MerchantOrderRunAndCompleteBean;
import com.ysxsoft.apaycoin.utils.ImageLoadUtil;
import com.ysxsoft.apaycoin.utils.NetWork;
import com.ysxsoft.apaycoin.view.LoginActivity;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MerchantOrderRunAndCompleteAdapter extends ListBaseAdapter<MerchantOrderRunAndCompleteBean.DataBean> {
    private OnCheckClickListener onCheckClickListener;

    public MerchantOrderRunAndCompleteAdapter(Context context) {
        super(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.my_order_running_item_layout;
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, final int position) {
        MerchantOrderRunAndCompleteBean.DataBean dataBean = mDataList.get(position);
        TextView tv_mall_name = holder.getView(R.id.tv_mall_name);
        TextView tv_time = holder.getView(R.id.tv_time);
        ImageView img_icon = holder.getView(R.id.img_icon);
        TextView tv_desc = holder.getView(R.id.tv_desc);
        TextView tv_money = holder.getView(R.id.tv_money);
        TextView tv_num = holder.getView(R.id.tv_num);
        TextView tv_is_fahuo = holder.getView(R.id.tv_is_fahuo);
        final TextView tv_check = holder.getView(R.id.tv_check);
        tv_mall_name.setVisibility(View.GONE);
        tv_time.setText(dataBean.getAdd_time());
        ImageLoadUtil.GlideImageLoad(mContext, dataBean.getIcon(), img_icon);
        tv_desc.setText(dataBean.getTitle());
        tv_money.setText(dataBean.getMoney());
        tv_num.setText(dataBean.getNum());
        final String oid = dataBean.getOid();
        String flag = dataBean.getFlag();
        switch (flag) {	//0待发货 1已发货 2已完成
            case "0":
                tv_is_fahuo.setText("用户已下单");
                tv_check.setVisibility(View.VISIBLE);
                tv_check.setText("发货");
                break;

            case "1":
                tv_is_fahuo.setText("已发货");
                tv_check.setVisibility(View.GONE);
                break;

            case "2":
                tv_is_fahuo.setText("已完成");
                tv_check.setVisibility(View.VISIBLE);
                tv_check.setPadding(20,10,20,10);
                tv_check.setBackgroundResource(R.drawable.btn_cancle_shape_stroke);
                tv_check.setTextColor(mContext.getResources().getColor(R.color.btn_cancle_bg));
                tv_check.setText("删除订单");
                tv_check.setVisibility(View.GONE);
                break;
        }
        tv_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                submitDate(oid);
                if (onCheckClickListener!=null){
                    onCheckClickListener.checkClick(tv_check,position);
                }
            }
        });

    }
    public interface OnCheckClickListener{
        void checkClick(View view,int Position);
    }
    public void setOnCheckClickListener(OnCheckClickListener onCheckClickListener){
        this.onCheckClickListener = onCheckClickListener;
    }

    private void submitDate(String oid) {
        NetWork.getRetrofit()
                .create(MerchantFaHuoService.class)
                .getCall(NetWork.getToken(),oid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<MerchantFaHuoBean>() {
                    private MerchantFaHuoBean merchantFaHuoBean;

                    @Override
                    public void onCompleted() {
                        Toast.makeText(mContext,merchantFaHuoBean.getMsg(),Toast.LENGTH_SHORT).show();
                        notifyDataSetChanged();
                      if ("2" .equals( merchantFaHuoBean.getCode())) {
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
                        Toast.makeText(mContext,e.getMessage(),Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(MerchantFaHuoBean merchantFaHuoBean) {

                        this.merchantFaHuoBean = merchantFaHuoBean;
                    }
                });
    }
}
