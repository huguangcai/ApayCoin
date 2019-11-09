package com.ysxsoft.apaycoin.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.github.jdsjlzx.ItemDecoration.LuDividerDecoration;
import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.interfaces.OnNetWorkErrorListener;
import com.github.jdsjlzx.recyclerview.LuRecyclerView;
import com.github.jdsjlzx.recyclerview.LuRecyclerViewAdapter;
import com.ysxsoft.apaycoin.R;
import com.ysxsoft.apaycoin.adapte.BalanceMoneyBuyOrSaleAdapter;
import com.ysxsoft.apaycoin.adapte.BalanceMoneySaleAdapter;
import com.ysxsoft.apaycoin.com.ActivityPageManager;
import com.ysxsoft.apaycoin.impservice.ImpBalanceMoneyBuyOrSaleService;
import com.ysxsoft.apaycoin.impservice.ImpBuyOrSaleService;
import com.ysxsoft.apaycoin.modle.BalanceMoneyBuyOrSaleBean;
import com.ysxsoft.apaycoin.modle.BuyOrSaleBean;
import com.ysxsoft.apaycoin.utils.AppUtil;
import com.ysxsoft.apaycoin.utils.NetWork;
import com.ysxsoft.apaycoin.view.BuyOrderActivity;
import com.ysxsoft.apaycoin.view.LoginActivity;
import com.ysxsoft.apaycoin.view.SaleOrderActivity;
import com.ysxsoft.apaycoin.widget.BalanceMoneyBuyOrSaleDialog;
import com.ysxsoft.apaycoin.widget.IssueApayDialog;
import com.ysxsoft.apaycoin.widget.PayPwdEditText;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import retrofit2.Retrofit;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 描述： 余额销售的Fragment
 * 日期： 2018/11/16 0016 09:58
 * 作者： 胡
 * 公司：郑州亿生信科技有限公司
 */
public class BalanceMoneySaleFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private LuRecyclerView mRecyclerView = null;
    private LuRecyclerViewAdapter mLuRecyclerViewAdapter = null;
    private BalanceMoneySaleAdapter mDataAdapter;
    private PreviewHandler mHandler = new PreviewHandler(this);
    private int page = 1;
    private int type = -1;//支付方式  1余额 2现金
    private BalanceMoneyBuyOrSaleBean balanceMoneyBuyOrSaleBean;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.balance_money_buy_layout, null);
        initView(view);
        return view;
    }

    private void initView(View view) {
        mSwipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        mRecyclerView = view.findViewById(R.id.list);
        //设置刷新时动画的颜色，可以设置4个
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setProgressViewOffset(false, 0, AppUtil.dip2px(getActivity(), 48));
            mSwipeRefreshLayout.setColorSchemeResources(R.color.btn_color);
            mSwipeRefreshLayout.setOnRefreshListener(this);
        }
        mDataAdapter = new BalanceMoneySaleAdapter(getActivity());
        //setLayoutManager放在setAdapter之前配置
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mLuRecyclerViewAdapter = new LuRecyclerViewAdapter(mDataAdapter);
        mRecyclerView.setAdapter(mLuRecyclerViewAdapter);
        LuDividerDecoration divider = new LuDividerDecoration.Builder(getActivity(), mLuRecyclerViewAdapter)
                .setHeight(R.dimen.default_divider_height)
                .setPadding(R.dimen.default_divider_padding)
                .setColorResource(R.color.gray)
                .build();
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addItemDecoration(divider);
        mDataAdapter.setOnBalanceMoneyBuyAndSale(new BalanceMoneyBuyOrSaleAdapter.OnBalanceMoneyBuyAndSale() {
            @Override
            public void BalanceBuybtn(int position) {
                type = 1;
                BalanceMoneyBuyOrSaleBean.DataBean dataBean = mDataAdapter.getDataList().get(position);
                String oid = dataBean.getOid();
                String nickname = dataBean.getNickname();
                comData(dataBean, nickname, oid);
            }

            @Override
            public void BalanceSalebtn(int position) {
                type = 2;
                BalanceMoneyBuyOrSaleBean.DataBean dataBean = mDataAdapter.getDataList().get(position);
                String oid = dataBean.getOid();
                String nickname = dataBean.getNickname();
                comData(dataBean, nickname, oid);
            }
        });
        mRecyclerView.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (balanceMoneyBuyOrSaleBean != null) {
                    if (page < Integer.valueOf(balanceMoneyBuyOrSaleBean.getLast_page())) {
                        page++;
                        requestData();
                    } else {
                        //the end
                        mRecyclerView.setNoMore(true);
                    }
                }
            }
        });
        //设置底部加载颜色
        mRecyclerView.setFooterViewColor(R.color.btn_color, R.color.black, android.R.color.white);
        //设置底部加载文字提示
        mRecyclerView.setFooterViewHint("拼命加载中", "没有更多数据了", "网络不给力啊，点击再试一次吧");
        onRefresh();
    }

    private void comData(BalanceMoneyBuyOrSaleBean.DataBean dataBean, String nickname, final String oid) {
        final BalanceMoneyBuyOrSaleDialog dialog = new BalanceMoneyBuyOrSaleDialog(getActivity());
        TextView tv_limit_num = dialog.findViewById(R.id.tv_limit_num);
        TextView tv_nike_name = dialog.findViewById(R.id.tv_nike_name);
        TextView tv_current_price = dialog.findViewById(R.id.tv_current_price);
        TextView tv_balance_money = dialog.findViewById(R.id.tv_balance_money);
        TextView tv_buy_or_sale = dialog.findViewById(R.id.tv_buy_or_sale);
        EditText ed_apay_num = dialog.findViewById(R.id.ed_apay_num);
        String apay_num = ed_apay_num.getText().toString().trim();
        tv_limit_num.setText(dataBean.getGold());
        tv_nike_name.setText(nickname);
//        tv_current_price.setText(dataBean.getMoney());
        tv_balance_money.setText(dataBean.getMoney());
        tv_buy_or_sale.setText("卖出");
        PayPwdEditText ed_ppet = dialog.findViewById(R.id.ed_ppet);
        ed_ppet.setOnTextFinishListener(new PayPwdEditText.OnTextFinishListener() {
            @Override
            public void onFinish(String str) {
                subimitData(oid, str);
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void subimitData(String oid, String str) {
        NetWork.getRetrofit()
                .create(ImpBuyOrSaleService.class)
                .getCall(NetWork.getToken(), oid, type + "", str)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BuyOrSaleBean>() {
                    private BuyOrSaleBean buyOrSaleBean;

                    @Override
                    public void onCompleted() {
                        Toast.makeText(getActivity(), buyOrSaleBean.getMsg(), Toast.LENGTH_SHORT).show();
                        if ("0".equals(buyOrSaleBean.getCode())) {
                            getActivity().startActivity(new Intent(getActivity(), SaleOrderActivity.class));
                            onRefresh();
                        } else if ("3".equals(buyOrSaleBean.getCode())) {
                            IssueApayDialog dialog = new IssueApayDialog(getActivity());
                            dialog.show();
                        } if ("2".equals(buyOrSaleBean.getCode())) {
                            SharedPreferences.Editor sp = getActivity().getSharedPreferences("save_name_pwd", Context.MODE_PRIVATE).edit();
                            sp.clear();
                            sp.commit();
                            SharedPreferences.Editor is_first = getActivity().getSharedPreferences("IS_FIRST", Context.MODE_PRIVATE).edit();
                            is_first.clear();
                            is_first.commit();
                            ActivityPageManager instance = ActivityPageManager.getInstance();
                            instance.finishAllActivity();
                            getActivity().startActivity(new Intent(getActivity(),LoginActivity.class));

                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(BuyOrSaleBean buyOrSaleBean) {
                        this.buyOrSaleBean = buyOrSaleBean;
                    }
                });

    }

    private class PreviewHandler extends Handler {
        private WeakReference<BalanceMoneySaleFragment> ref;

        PreviewHandler(BalanceMoneySaleFragment activity) {
            ref = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case -1:
                    if (mSwipeRefreshLayout.isRefreshing()) {
                        mDataAdapter.clear();
                    }
                    getData();
                    break;
                case -3:
                    if (mSwipeRefreshLayout.isRefreshing()) {
                        mSwipeRefreshLayout.setRefreshing(false);
                        mRecyclerView.refreshComplete(balanceMoneyBuyOrSaleBean.getData().size());
                        notifyDataSetChanged();
                    } else {
                        mRecyclerView.setOnNetWorkErrorListener(new OnNetWorkErrorListener() {
                            @Override
                            public void reload() {
                                mRecyclerView.refreshComplete(balanceMoneyBuyOrSaleBean.getData().size());
                                notifyDataSetChanged();
                                requestData();
                            }
                        });
                    }
                    break;
                default:
                    break;
            }
        }
    }

    private void notifyDataSetChanged() {
        mLuRecyclerViewAdapter.notifyDataSetChanged();
    }

    private void getData() {
        Retrofit retrofit = NetWork.getRetrofit();
        retrofit.create(ImpBalanceMoneyBuyOrSaleService.class)
                .getCall(NetWork.getToken(), 2 + "", page + "")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BalanceMoneyBuyOrSaleBean>() {
                    private BalanceMoneyBuyOrSaleBean balanceMoneyBuyOrSaleBean;

                    @Override
                    public void onCompleted() {
//                        Toast.makeText(getActivity(), balanceMoneyBuyOrSaleBean.getMsg(), Toast.LENGTH_SHORT).show();
                        if ("0".equals(balanceMoneyBuyOrSaleBean.getCode())) {
                            showData(balanceMoneyBuyOrSaleBean);
                            ArrayList<BalanceMoneyBuyOrSaleBean.DataBean> data = balanceMoneyBuyOrSaleBean.getData();
                            addItems(balanceMoneyBuyOrSaleBean.getData());
                            if (mSwipeRefreshLayout.isRefreshing()) {
                                mSwipeRefreshLayout.setRefreshing(false);
                            }
                            mRecyclerView.refreshComplete(data.size());
                            notifyDataSetChanged();
                        }else if ("2".equals(balanceMoneyBuyOrSaleBean.getCode())) {
                            SharedPreferences.Editor sp = getActivity().getSharedPreferences("save_name_pwd", Context.MODE_PRIVATE).edit();
                            sp.clear();
                            sp.commit();
                            SharedPreferences.Editor is_first =getActivity().getSharedPreferences("IS_FIRST", Context.MODE_PRIVATE).edit();
                            is_first.clear();
                            is_first.commit();
                            ActivityPageManager instance = ActivityPageManager.getInstance();
                            instance.finishAllActivity();
                            getActivity().startActivity(new Intent(getActivity(),LoginActivity.class));
//                            finish();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(BalanceMoneyBuyOrSaleBean balanceMoneyBuyOrSaleBean) {
                        this.balanceMoneyBuyOrSaleBean = balanceMoneyBuyOrSaleBean;
                    }
                });
    }

    private void addItems(ArrayList<BalanceMoneyBuyOrSaleBean.DataBean> data) {
        mDataAdapter.addAll(data);
    }

    private void showData(BalanceMoneyBuyOrSaleBean balanceMoneyBuyOrSaleBean) {
        this.balanceMoneyBuyOrSaleBean = balanceMoneyBuyOrSaleBean;
    }

    @Override
    public void onRefresh() {
        page = 1;
        mSwipeRefreshLayout.setRefreshing(true);
        mRecyclerView.setRefreshing(true);//同时调用LuRecyclerView的setRefreshing方法
        requestData();
    }

    private void requestData() {
        //判断网络是否可用
        if (AppUtil.isNetworkAvaiable(getActivity())) {
            mHandler.sendEmptyMessage(-1);
        } else {
            mHandler.sendEmptyMessage(-3);
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            //Fragment隐藏时调用

        } else {
            //Fragment显示时调用
            onRefresh();
        }
    }
}
