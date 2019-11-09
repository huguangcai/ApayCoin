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
import com.ysxsoft.apaycoin.adapte.CrowdfundingRunAdapter;
import com.ysxsoft.apaycoin.com.ActivityPageManager;
import com.ysxsoft.apaycoin.impservice.ImpCrowdingFundingService;
import com.ysxsoft.apaycoin.modle.CrowdingFundingBean;
import com.ysxsoft.apaycoin.utils.AppUtil;
import com.ysxsoft.apaycoin.utils.NetWork;
import com.ysxsoft.apaycoin.view.LoginActivity;

import java.util.ArrayList;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 描述： z众筹运行中的Fragment
 * 日期： 2018/10/29 0029 17:42
 * 作者： 胡
 * 公司：郑州亿生信科技有限公司
 */
public class CrowdfundingRunFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private int type = 2;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private LuRecyclerView mRecyclerView = null;
    private int page=1;
    private CrowdfundingRunAdapter mDataAdapter;
    private LuRecyclerViewAdapter mLuRecyclerViewAdapter = null;
    private PreviewHandler mHandler = new PreviewHandler();
    private CrowdingFundingBean crowdingFundingBean;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.crowd_funding_to_start_layout, null);
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
        mDataAdapter = new CrowdfundingRunAdapter(getActivity());
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
        mRecyclerView.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (crowdingFundingBean != null) {
                    if (page < Integer.valueOf(crowdingFundingBean.getLast_page())) {
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
                        mRecyclerView.refreshComplete(crowdingFundingBean.getData().size());
                        notifyDataSetChanged();
                    } else {
                        mRecyclerView.setOnNetWorkErrorListener(new OnNetWorkErrorListener() {
                            @Override
                            public void reload() {
                                mRecyclerView.refreshComplete(crowdingFundingBean.getData().size());
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
        NetWork.getRetrofit()
                .create(ImpCrowdingFundingService.class)
                .getCall(NetWork.getToken(), type + "",page+"")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CrowdingFundingBean>() {
                    private CrowdingFundingBean crowdingFundingBean;
                    @Override
                    public void onCompleted() {
//                        Toast.makeText(getActivity(),crowdingFundingBean.getMsg(),Toast.LENGTH_SHORT).show();
                        if ("0".equals(crowdingFundingBean.getCode())){
                            showData(crowdingFundingBean);
                            ArrayList<CrowdingFundingBean.DataBean> data = crowdingFundingBean.getData();
                            addItems(crowdingFundingBean.getData());
                            if (mSwipeRefreshLayout.isRefreshing()) {
                                mSwipeRefreshLayout.setRefreshing(false);
                            }
                            mRecyclerView.refreshComplete(data.size());
                            notifyDataSetChanged();
                        }else  if ("2".equals(crowdingFundingBean.getCode())) {
                            SharedPreferences.Editor sp =getActivity().getSharedPreferences("save_name_pwd", Context.MODE_PRIVATE).edit();
                            sp.clear();
                            sp.commit();
                            SharedPreferences.Editor is_first =getActivity().getSharedPreferences("IS_FIRST", Context.MODE_PRIVATE).edit();
                            is_first.clear();
                            is_first.commit();
                            ActivityPageManager instance = ActivityPageManager.getInstance();
                            instance.finishAllActivity();
                            getActivity().startActivity(new Intent(getActivity(),LoginActivity.class));
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (mSwipeRefreshLayout.isRefreshing()) {
                            mSwipeRefreshLayout.setRefreshing(false);
                        }
                        Toast.makeText(getActivity(),e.getMessage(),Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(CrowdingFundingBean crowdingFundingBean) {
                        this.crowdingFundingBean = crowdingFundingBean;
                    }
                });
    }

    private void addItems(ArrayList<CrowdingFundingBean.DataBean> data) {
        mDataAdapter.addAll(data);
    }

    private void showData(CrowdingFundingBean crowdingFundingBean) {
        this.crowdingFundingBean = crowdingFundingBean;
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
