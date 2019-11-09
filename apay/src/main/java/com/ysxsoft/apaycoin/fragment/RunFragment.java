package com.ysxsoft.apaycoin.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.LocaleList;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.github.jdsjlzx.ItemDecoration.LuDividerDecoration;
import com.github.jdsjlzx.interfaces.OnItemClickListener;
import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.interfaces.OnNetWorkErrorListener;
import com.github.jdsjlzx.recyclerview.LuRecyclerView;
import com.github.jdsjlzx.recyclerview.LuRecyclerViewAdapter;
import com.ysxsoft.apaycoin.R;
import com.ysxsoft.apaycoin.adapte.MyOrderRunAdapter;
import com.ysxsoft.apaycoin.com.ActivityPageManager;
import com.ysxsoft.apaycoin.impservice.ImpMyOrderRunCompleteService;
import com.ysxsoft.apaycoin.modle.BalanceMoneyBuyOrSaleBean;
import com.ysxsoft.apaycoin.modle.MyOrderRunCompleteBean;
import com.ysxsoft.apaycoin.modle.NewAddGoodsEditorListBean;
import com.ysxsoft.apaycoin.utils.AppUtil;
import com.ysxsoft.apaycoin.utils.NetWork;
import com.ysxsoft.apaycoin.view.LoginActivity;
import com.ysxsoft.apaycoin.view.MallGoodsDetialActivity;
import com.ysxsoft.apaycoin.view.OrderDetialActivity;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 描述： 运行中的fragment
 * 日期： 2018/10/26 0026 15:16
 * 作者： 胡
 * 公司：郑州亿生信科技有限公司
 */
public class RunFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private LuRecyclerView mRecyclerView = null;
    private LuRecyclerViewAdapter mLuRecyclerViewAdapter = null;
    private int page = 1;
    private MyOrderRunAdapter mDataAdapter;
    private PreviewHandler mHandler = new PreviewHandler();
    private MyOrderRunCompleteBean myOrderRunCompleteBean;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.my_order_run_fragment_layout, null);
        initView(view);
        return view;
    }

    private void initView(View view) {
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        mRecyclerView = (LuRecyclerView) view.findViewById(R.id.list);
        //设置刷新时动画的颜色，可以设置4个
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setProgressViewOffset(false, 0, AppUtil.dip2px(getActivity(), 48));
            mSwipeRefreshLayout.setColorSchemeResources(R.color.btn_color);
            mSwipeRefreshLayout.setOnRefreshListener(this);
        }
        mDataAdapter = new MyOrderRunAdapter(getActivity());
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
        mLuRecyclerViewAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                MyOrderRunCompleteBean.DataBean dataBean = mDataAdapter.getDataList().get(position);
                String oid = dataBean.getOid();
                String flag = dataBean.getFlag();
                Intent intent=new Intent(getActivity(), OrderDetialActivity.class);
                intent.putExtra("oid",oid);
                intent.putExtra("flag",flag);
                intent.putExtra("user","user");
                startActivity(intent);
            }
        });



        mRecyclerView.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (myOrderRunCompleteBean != null) {
                    if (page < Integer.valueOf(myOrderRunCompleteBean.getLast_page())) {
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

    @Override
    public void onRefresh() {
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

    private class PreviewHandler extends Handler {
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
                        mRecyclerView.refreshComplete(myOrderRunCompleteBean.getData().size());
                        notifyDataSetChanged();
                    } else {
                        mRecyclerView.setOnNetWorkErrorListener(new OnNetWorkErrorListener() {
                            @Override
                            public void reload() {
                                mRecyclerView.refreshComplete(myOrderRunCompleteBean.getData().size());
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
        NetWork.getRetrofit()
                .create(ImpMyOrderRunCompleteService.class)
                .getCall(NetWork.getToken(), "1")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<MyOrderRunCompleteBean>() {
                    private MyOrderRunCompleteBean myOrderRunCompleteBean;

                    @Override
                    public void onCompleted() {
//                        Toast.makeText(getActivity(), myOrderRunCompleteBean.getMsg(), Toast.LENGTH_SHORT).show();
                        if ("0".equals(myOrderRunCompleteBean.getCode())) {
                            showData(myOrderRunCompleteBean);
                            ArrayList<MyOrderRunCompleteBean.DataBean> data = myOrderRunCompleteBean.getData();
                            mDataAdapter.addAll(data);
                            if (mSwipeRefreshLayout.isRefreshing()) {
                                mSwipeRefreshLayout.setRefreshing(false);
                            }
                            mRecyclerView.refreshComplete(data.size());
                            notifyDataSetChanged();
                        }else if ("2" .equals( myOrderRunCompleteBean.getCode())) {
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
                    public void onNext(MyOrderRunCompleteBean myOrderRunCompleteBean) {

                        this.myOrderRunCompleteBean = myOrderRunCompleteBean;
                    }
                });
    }

    private void notifyDataSetChanged() {
        mLuRecyclerViewAdapter.notifyDataSetChanged();
    }

    private void showData(MyOrderRunCompleteBean myOrderRunCompleteBean) {
        this.myOrderRunCompleteBean = myOrderRunCompleteBean;
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
