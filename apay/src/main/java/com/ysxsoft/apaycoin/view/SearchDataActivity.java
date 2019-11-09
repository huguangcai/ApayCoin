package com.ysxsoft.apaycoin.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.LocaleList;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.github.jdsjlzx.ItemDecoration.LuDividerDecoration;
import com.github.jdsjlzx.interfaces.OnItemClickListener;
import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.interfaces.OnNetWorkErrorListener;
import com.github.jdsjlzx.recyclerview.LuRecyclerView;
import com.github.jdsjlzx.recyclerview.LuRecyclerViewAdapter;
import com.luck.picture.lib.tools.StringUtils;
import com.ysxsoft.apaycoin.R;
import com.ysxsoft.apaycoin.adapte.SearchDataAdapter;
import com.ysxsoft.apaycoin.com.ActivityPageManager;
import com.ysxsoft.apaycoin.com.BaseActivity;
import com.ysxsoft.apaycoin.impservice.ImpSearchGoodsService;
import com.ysxsoft.apaycoin.modle.NoticeListBean;
import com.ysxsoft.apaycoin.modle.OnlineMallGoodsListBean;
import com.ysxsoft.apaycoin.utils.AppUtil;
import com.ysxsoft.apaycoin.utils.CustomDialog;
import com.ysxsoft.apaycoin.utils.NetWork;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SearchDataActivity extends BaseActivity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    private View img_back;
    private EditText ed_title_search;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private LuRecyclerView mRecyclerView = null;
    private LuRecyclerViewAdapter mLuRecyclerViewAdapter = null;
    private SearchDataAdapter mDataAdapter;
    private int page = 1;
    private PreviewHandler mHandler = new PreviewHandler(this);
    private OnlineMallGoodsListBean onlineMallGoodsListBean;
    private TextView tv_title_right;
    private CustomDialog customDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_layout);
        customDialog = new CustomDialog(mContext, "正在搜索...");
        initView();
        initListener();
    }

    private void initListener() {
        img_back.setOnClickListener(this);
        tv_title_right.setOnClickListener(this);
    }

    private void initView() {
        img_back = getViewById(R.id.img_back);
        tv_title_right = getViewById(R.id.tv_title_right);
        tv_title_right.setText("搜索");
        tv_title_right.setVisibility(View.VISIBLE);
        ed_title_search = getViewById(R.id.ed_title_search);

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        mRecyclerView = (LuRecyclerView) findViewById(R.id.list);
        //设置刷新时动画的颜色，可以设置4个
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setProgressViewOffset(false, 0, AppUtil.dip2px(mContext, 48));
            mSwipeRefreshLayout.setColorSchemeResources(R.color.btn_color);
            mSwipeRefreshLayout.setOnRefreshListener(this);
        }
        mDataAdapter = new SearchDataAdapter(this);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mLuRecyclerViewAdapter = new LuRecyclerViewAdapter(mDataAdapter);
        mRecyclerView.setAdapter(mLuRecyclerViewAdapter);
        LuDividerDecoration divider = new LuDividerDecoration.Builder(this, mLuRecyclerViewAdapter)
                .setHeight(R.dimen.default_divider_height)
                .setPadding(R.dimen.default_divider_padding)
                .setColorResource(R.color.gray)
                .build();
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addItemDecoration(divider);
        mLuRecyclerViewAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                OnlineMallGoodsListBean.DataBean dataBean = mDataAdapter.getDataList().get(position);
                String pid = dataBean.getPid();
                String money = dataBean.getMoney();
                String uid = dataBean.getUid();
                String stock = dataBean.getStock();
                Intent intent = new Intent(mContext, OnlineMallGoodsDetialActivity.class);
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
//        onRefresh();

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
        if (AppUtil.isNetworkAvaiable(this)) {
            mHandler.sendEmptyMessage(-1);
        } else {
            mHandler.sendEmptyMessage(-3);
        }
    }

    private class PreviewHandler extends Handler {
        private WeakReference<SearchDataActivity> ref;
        PreviewHandler(SearchDataActivity activity) {
            ref = new WeakReference<>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            final SearchDataActivity activity = ref.get();
            if (activity == null || activity.isFinishing()) {
                return;
            }
            switch (msg.what) {
                case -1:
                    if (activity.mSwipeRefreshLayout.isRefreshing()) {
                        activity.mDataAdapter.clear();
                    }
                    getData();
                    break;
                case -3:
                    if (mSwipeRefreshLayout.isRefreshing()) {
                        activity.mSwipeRefreshLayout.setRefreshing(false);
                        activity.mRecyclerView.refreshComplete(onlineMallGoodsListBean.getData().size());
                        activity.notifyDataSetChanged();
                    } else {
                        activity.mRecyclerView.setOnNetWorkErrorListener(new OnNetWorkErrorListener() {
                            @Override
                            public void reload() {
                                activity.mRecyclerView.refreshComplete(onlineMallGoodsListBean.getData().size());
                                activity.notifyDataSetChanged();
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
                .create(ImpSearchGoodsService.class)
                .getCall(NetWork.getToken(), page + "", ed_title_search.getText().toString().trim())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<OnlineMallGoodsListBean>() {
                    private OnlineMallGoodsListBean onlineMallGoodsListBean;

                    @Override
                    public void onCompleted() {
//                        showToastMessage(onlineMallGoodsListBean.getMsg());
                        if ("0".equals(onlineMallGoodsListBean.getCode())) {
                            customDialog.dismiss();
                            showData(onlineMallGoodsListBean);
                            ArrayList<OnlineMallGoodsListBean.DataBean> data = onlineMallGoodsListBean.getData();
                            if (page == 1 && data.size() == 0) {
                                showToastMessage("没有搜索到商品");
                            }
                            addItems(onlineMallGoodsListBean.getData());
                            if (mSwipeRefreshLayout.isRefreshing()) {
                                mSwipeRefreshLayout.setRefreshing(false);
                            }
                            mRecyclerView.refreshComplete(data.size());
                            notifyDataSetChanged();
                        }else if ("2" .equals( onlineMallGoodsListBean.getCode())) {
                            SharedPreferences.Editor sp =getSharedPreferences("save_name_pwd", Context.MODE_PRIVATE).edit();
                            sp.clear();
                            sp.commit();
                            SharedPreferences.Editor is_first =getSharedPreferences("IS_FIRST", Context.MODE_PRIVATE).edit();
                            is_first.clear();
                            is_first.commit();
                            ActivityPageManager instance = ActivityPageManager.getInstance();
                            instance.finishAllActivity();
                            startActivity(LoginActivity.class);
                            finish();
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        showToastMessage(e.getMessage());
                    }

                    @Override
                    public void onNext(OnlineMallGoodsListBean onlineMallGoodsListBean) {
                        this.onlineMallGoodsListBean = onlineMallGoodsListBean;
                    }
                });


    }

    private void notifyDataSetChanged() {
        mLuRecyclerViewAdapter.notifyDataSetChanged();
    }

    private void addItems(ArrayList<OnlineMallGoodsListBean.DataBean> data) {
        mDataAdapter.addAll(data);
    }

    private void showData(OnlineMallGoodsListBean onlineMallGoodsListBean) {
        this.onlineMallGoodsListBean = onlineMallGoodsListBean;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.tv_title_right:
                page = 1;
                customDialog.show();
                getData();
                mDataAdapter.clear();
//                onRefresh();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //拿到InputMethodManager
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        //如果window上view获取焦点 && view不为空
        if(imm.isActive()&&getCurrentFocus()!=null) {
            //拿到view的token 不为空
            if (getCurrentFocus().getWindowToken() != null) {
                //表示软键盘窗口总是隐藏，除非开始时以SHOW_FORCED显示。
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }
}
