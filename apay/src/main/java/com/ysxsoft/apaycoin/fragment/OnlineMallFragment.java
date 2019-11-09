package com.ysxsoft.apaycoin.fragment;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.github.jdsjlzx.ItemDecoration.LuDividerDecoration;
import com.github.jdsjlzx.interfaces.OnItemClickListener;
import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.interfaces.OnNetWorkErrorListener;
import com.github.jdsjlzx.recyclerview.LuRecyclerView;
import com.github.jdsjlzx.recyclerview.LuRecyclerViewAdapter;
import com.ysxsoft.apaycoin.R;
import com.ysxsoft.apaycoin.adapte.OnLineMallAdapter;
import com.ysxsoft.apaycoin.adapte.OnLineMallFragmentAdapter;
import com.ysxsoft.apaycoin.com.ActivityPageManager;
import com.ysxsoft.apaycoin.impservice.OnlineMallGoodsListService;
import com.ysxsoft.apaycoin.modle.GoodsTypeBean;
import com.ysxsoft.apaycoin.modle.OnlineMallGoodsListBean;
import com.ysxsoft.apaycoin.utils.AppUtil;
import com.ysxsoft.apaycoin.utils.NetWork;
import com.ysxsoft.apaycoin.view.LoginActivity;
import com.ysxsoft.apaycoin.view.OnlineMallGoodsDetialActivity;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
@SuppressLint("ValidFragment")
public class OnlineMallFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private static String cid;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private LuRecyclerView mRecyclerView = null;
    private LuRecyclerViewAdapter mLuRecyclerViewAdapter = null;
    private OnLineMallAdapter mDataAdapter;
    private int page = 1;
    private PreviewHandler mHandler = new PreviewHandler();
    private OnlineMallGoodsListBean onlineMallGoodsListBean;

    public static Fragment getInstance(String cid) {
        OnlineMallFragment.cid = cid;
        OnlineMallFragment fragment = new OnlineMallFragment();
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.on_line_mall_fragment_layout, null);
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
        mDataAdapter = new OnLineMallAdapter(getActivity());
        //setLayoutManager放在setAdapter之前配置
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        mLuRecyclerViewAdapter = new LuRecyclerViewAdapter(mDataAdapter);
        mRecyclerView.setAdapter(mLuRecyclerViewAdapter);
        LuDividerDecoration divider = new LuDividerDecoration.Builder(getActivity(), mLuRecyclerViewAdapter)
                .setHeight(R.dimen.default_divider_height)
                .setPadding(R.dimen.default_divider_padding)
                .setColorResource(R.color.white)
                .build();
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addItemDecoration(divider);

        mLuRecyclerViewAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                List<OnlineMallGoodsListBean.DataBean> dataList = mDataAdapter.getDataList();
                OnlineMallGoodsListBean.DataBean dataBean = dataList.get(position);
                String pid = dataBean.getPid();
                String uid = dataBean.getUid();
                String money = dataBean.getMoney();
                String stock = dataBean.getStock();
                Intent intent = new Intent(getActivity(), OnlineMallGoodsDetialActivity.class);
                intent.putExtra("pid", pid);
                intent.putExtra("uid", uid);
                intent.putExtra("money", money);
                intent.putExtra("stock", stock);
                startActivity(intent);
            }
        });

        mRecyclerView.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (onlineMallGoodsListBean != null) {
                    if (page < Integer.valueOf(onlineMallGoodsListBean.getLast_page())) {
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
        if ("".equals(cid) || cid == null || "cid".equals(cid)) {
            onRefresh();
        }
    }

    /**
     * 获取内容
     */
    private void getData() {
        if ("".equals(cid) || cid == null || "cid".equals(cid)) {
            cid = null;
        }
        NetWork.getRetrofit()
                .create(OnlineMallGoodsListService.class)
                .getCall(NetWork.getToken(), page + "", cid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<OnlineMallGoodsListBean>() {
                    private OnlineMallGoodsListBean onlineMallGoodsListBean;

                    @Override
                    public void onCompleted() {
                        String last_page = onlineMallGoodsListBean.getLast_page();
                        if ("0".equals(onlineMallGoodsListBean.getCode())) {
                            ArrayList<OnlineMallGoodsListBean.DataBean> contentdata = onlineMallGoodsListBean.getData();
                            showContentData(onlineMallGoodsListBean);
                            mDataAdapter.addAll(contentdata);
                            if (mSwipeRefreshLayout.isRefreshing()) {
                                mSwipeRefreshLayout.setRefreshing(false);
                            }
                            mRecyclerView.refreshComplete(contentdata.size());
                            notifyDataSetChanged();
                        }else if ("2" .equals( onlineMallGoodsListBean.getCode())) {
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
                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(OnlineMallGoodsListBean onlineMallGoodsListBean) {
                        this.onlineMallGoodsListBean = onlineMallGoodsListBean;
                    }
                });
    }

    public void setPosition(String cid) {
        this.cid = cid;
        onRefresh();
    }

    private void showContentData(OnlineMallGoodsListBean onlineMallGoodsListBean) {
        this.onlineMallGoodsListBean = onlineMallGoodsListBean;
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
                        mRecyclerView.refreshComplete(onlineMallGoodsListBean.getData().size());
                        notifyDataSetChanged();
                    } else {
                        mRecyclerView.setOnNetWorkErrorListener(new OnNetWorkErrorListener() {
                            @Override
                            public void reload() {
                                mRecyclerView.refreshComplete(onlineMallGoodsListBean.getData().size());
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

}
