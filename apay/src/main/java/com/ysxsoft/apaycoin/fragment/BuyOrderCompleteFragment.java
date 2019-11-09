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
import android.widget.Toast;

import com.github.jdsjlzx.ItemDecoration.LuDividerDecoration;
import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.interfaces.OnNetWorkErrorListener;
import com.github.jdsjlzx.recyclerview.LuRecyclerView;
import com.github.jdsjlzx.recyclerview.LuRecyclerViewAdapter;
import com.ysxsoft.apaycoin.R;
import com.ysxsoft.apaycoin.adapte.BuyOrderCompleteAdapter;
import com.ysxsoft.apaycoin.com.ActivityPageManager;
import com.ysxsoft.apaycoin.impservice.ImpDeleteOrderService;
import com.ysxsoft.apaycoin.impservice.ImpSaleBuyOrderService;
import com.ysxsoft.apaycoin.modle.DeleteOrderBean;
import com.ysxsoft.apaycoin.modle.SaleBuyOrderBean;
import com.ysxsoft.apaycoin.utils.AppUtil;
import com.ysxsoft.apaycoin.utils.NetWork;
import com.ysxsoft.apaycoin.view.LoginActivity;

import java.util.ArrayList;

import retrofit2.Retrofit;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 描述：
 * 日期： 2018/10/30 0030 11:08
 * 作者： 胡
 * 公司：郑州亿生信科技有限公司
 */
public class BuyOrderCompleteFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private LuRecyclerView mRecyclerView = null;
    private BuyOrderCompleteAdapter mDataAdapter;
    private LuRecyclerViewAdapter mLuRecyclerViewAdapter = null;
    private PreviewHandler mHandler = new PreviewHandler();
    private int page=1;
    private SaleBuyOrderBean saleBuyOrderBean;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sale_order_run_fragment, null);
        initView(view);
        return view;
    }

    private void initView(View view) {
        mSwipeRefreshLayout= view.findViewById(R.id.swipe_refresh_layout);
        mRecyclerView= view.findViewById(R.id.list);
        //设置刷新时动画的颜色，可以设置4个
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setProgressViewOffset(false, 0, AppUtil.dip2px(getActivity(), 48));
            mSwipeRefreshLayout.setColorSchemeResources(R.color.btn_color);
            mSwipeRefreshLayout.setOnRefreshListener(this);
        }
        mDataAdapter = new BuyOrderCompleteAdapter(getActivity());

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
        mDataAdapter.setOnDeleteClickListener(new BuyOrderCompleteAdapter.OnDeleteClickListener() {
            @Override
            public void onDeleteClick(int position) {
                SaleBuyOrderBean.DataBean dataBean = mDataAdapter.getDataList().get(position);
                String oid = dataBean.getOid();
                submitData(oid);
            }
        });
        mRecyclerView.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (saleBuyOrderBean != null) {
                    if (page < Integer.valueOf(saleBuyOrderBean.getLast_page())) {
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
                        Toast.makeText(getActivity(), deleteOrderBean.getMsg(), Toast.LENGTH_SHORT).show();
                        if ("0".equals(deleteOrderBean.getCode())) {
                            onRefresh();
                        }else  if ("2".equals(deleteOrderBean.getCode())) {
                            SharedPreferences.Editor sp =getActivity().getSharedPreferences("save_name_pwd", Context.MODE_PRIVATE).edit();
                            sp.clear();
                            sp.commit();
                            SharedPreferences.Editor is_first =getActivity().getSharedPreferences("IS_FIRST", Context.MODE_PRIVATE).edit();
                            is_first.clear();
                            is_first.commit();
                            ActivityPageManager instance = ActivityPageManager.getInstance();
                            instance.finishAllActivity();
                            getActivity().startActivity(new Intent(getContext(),LoginActivity.class));
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(DeleteOrderBean deleteOrderBean) {
                        this.deleteOrderBean = deleteOrderBean;
                    }
                });
    }

    private class PreviewHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case -1:
                    if(mSwipeRefreshLayout.isRefreshing()){
                        mDataAdapter.clear();
                    }
                    getData();
                    break;
                case -3:
                    if (mSwipeRefreshLayout.isRefreshing()) {
                        mSwipeRefreshLayout.setRefreshing(false);
                        mRecyclerView.refreshComplete(saleBuyOrderBean.getData().size());
                        notifyDataSetChanged();
                    } else {
                        mRecyclerView.setOnNetWorkErrorListener(new OnNetWorkErrorListener() {
                            @Override
                            public void reload() {
                                mRecyclerView.refreshComplete(saleBuyOrderBean.getData().size());
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
    private void getData() {
        Retrofit retrofit = NetWork.getRetrofit();
        retrofit.create(ImpSaleBuyOrderService.class)
                .getCall(NetWork.getToken(),1+"",2+"")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<SaleBuyOrderBean>() {
                    private SaleBuyOrderBean saleBuyOrderBean;

                    @Override
                    public void onCompleted() {
//                        Toast.makeText(getActivity(),saleBuyOrderBean.getMsg(),Toast.LENGTH_SHORT).show();
                        if ("0".equals(saleBuyOrderBean.getCode())) {
                            showData(saleBuyOrderBean);
                            ArrayList<SaleBuyOrderBean.DataBean> data = saleBuyOrderBean.getData();
                            addItems(saleBuyOrderBean.getData());
                            if (mSwipeRefreshLayout.isRefreshing()) {
                                mSwipeRefreshLayout.setRefreshing(false);
                            }
                            mRecyclerView.refreshComplete(data.size());
                            notifyDataSetChanged();
                        }else if ("2" .equals( saleBuyOrderBean.getCode())) {
                            SharedPreferences.Editor sp =getActivity().getSharedPreferences("save_name_pwd", Context.MODE_PRIVATE).edit();
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
                        Toast.makeText(getActivity(),e.getMessage(),Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(SaleBuyOrderBean saleBuyOrderBean) {

                        this.saleBuyOrderBean = saleBuyOrderBean;
                    }
                });
    }
    private void addItems(ArrayList<SaleBuyOrderBean.DataBean> data) {
        mDataAdapter.addAll(data);
    }
    private void notifyDataSetChanged() {
        mLuRecyclerViewAdapter.notifyDataSetChanged();
    }

    private void showData(SaleBuyOrderBean saleBuyOrderBean) {
        this.saleBuyOrderBean = saleBuyOrderBean;
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
        if (hidden){
            //Fragment隐藏时调用

        }else {
            //Fragment显示时调用
            onRefresh();
        }
    }
}
