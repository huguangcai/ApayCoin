package com.ysxsoft.apaycoin.adapte;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ysxsoft.apaycoin.R;
import com.ysxsoft.apaycoin.com.ActivityPageManager;
import com.ysxsoft.apaycoin.com.ListBaseAdapter;
import com.ysxsoft.apaycoin.com.SuperViewHolder;
import com.ysxsoft.apaycoin.impservice.ImpCrowdfingBuyService;
import com.ysxsoft.apaycoin.modle.CrowdfingBuyBean;
import com.ysxsoft.apaycoin.modle.CrowdingFundingBean;
import com.ysxsoft.apaycoin.utils.NetWork;
import com.ysxsoft.apaycoin.view.LoginActivity;
import com.ysxsoft.apaycoin.widget.CrowFundingRunBuyDialog;
import com.ysxsoft.apaycoin.widget.PayPwdEditText;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 描述：众筹运行中适配器
 * 日期： 2018/11/21 0021 09:38
 * 作者： 胡
 * 公司：郑州亿生信科技有限公司
 */
public class CrowdfundingRunAdapter extends ListBaseAdapter<CrowdingFundingBean.DataBean> {
    public CrowdfundingRunAdapter(Context context) {
        super(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.crowd_funding_run_fragment_layout;
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, int position) {
        final CrowdingFundingBean.DataBean dataBean = mDataList.get(position);
        TextView tv_end_time = holder.getView(R.id.tv_end_time);
        TextView tv_balance_money = holder.getView(R.id.tv_balance_money);
        TextView tv_apay_price = holder.getView(R.id.tv_apay_price);
        TextView tv_num = holder.getView(R.id.tv_num);
        TextView tv_surplus_num = holder.getView(R.id.tv_surplus_num);
        TextView tv_limit_num = holder.getView(R.id.tv_limit_num);
        TextView tv_money_type = holder.getView(R.id.tv_money_type);
        Button btn_buy=holder.getView(R.id.btn_buy);
        tv_end_time.setText(dataBean.getEnd_date());
        tv_balance_money.setText(dataBean.getMoney());
        tv_apay_price.setText(dataBean.getMoney());
        tv_num.setText(dataBean.getFx_num());
        tv_surplus_num.setText(dataBean.getSy_num());
        tv_limit_num.setText(dataBean.getXiangou());

        btn_buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CrowFundingRunBuyDialog dialog=new CrowFundingRunBuyDialog(mContext);

                final TextView tv_limit_num = dialog.findViewById(R.id.tv_limit_num);
                TextView tv_current_price = dialog.findViewById(R.id.tv_current_price);
                final EditText ed_apay = dialog.findViewById(R.id.ed_apay);
                final TextView tv_balance_money = dialog.findViewById(R.id.tv_balance_money);
                Button btn_all = dialog.findViewById(R.id.btn_all);
                PayPwdEditText ed_ppet = dialog.findViewById(R.id.ed_ppet);
                tv_limit_num.setText(dataBean.getXiangou());
                tv_current_price.setText(dataBean.getMoney());
                final String cid = dataBean.getCid();
//                tv_balance_money.setText(BaseApplication.loginBean.getUserinfo().getMoney());
                btn_all.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ed_apay.setText(tv_limit_num.getText().toString());
                        tv_balance_money.setText(String.valueOf(Float.valueOf(dataBean.getMoney())*Float.valueOf(ed_apay.getText().toString().trim())));
                    }
                });
                ed_apay.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        if (TextUtils.isEmpty(ed_apay.getText().toString().trim())){
                            tv_balance_money.setText("");
                            return;
                        }
                        tv_balance_money.setText(String.valueOf(Float.valueOf(dataBean.getMoney())*Float.valueOf(ed_apay.getText().toString().trim())));
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });
                ed_ppet.setOnTextFinishListener(new PayPwdEditText.OnTextFinishListener() {
                    @Override
                    public void onFinish(String str) {
//                        Toast.makeText(mContext,str,Toast.LENGTH_SHORT).show();
                        submitData(str,ed_apay.getText().toString().trim(),cid);
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
    }

    private void submitData(String pwd, String num,String cid) {
        NetWork.getRetrofit()
                .create(ImpCrowdfingBuyService.class)
                .getCall(NetWork.getToken(),cid,num,pwd)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CrowdfingBuyBean>() {
                    private CrowdfingBuyBean crowdfingBuyBean;

                    @Override
                    public void onCompleted() {
                        Toast.makeText(mContext,crowdfingBuyBean.getMsg(),Toast.LENGTH_SHORT).show();
                       if ("2".equals(crowdfingBuyBean.getCode())) {
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
                    public void onNext(CrowdfingBuyBean crowdfingBuyBean) {
                        this.crowdfingBuyBean = crowdfingBuyBean;
                    }
                });


    }
}
