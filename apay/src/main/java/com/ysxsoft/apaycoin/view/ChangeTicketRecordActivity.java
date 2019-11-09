package com.ysxsoft.apaycoin.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.TextView;

import com.github.jdsjlzx.ItemDecoration.LuDividerDecoration;
import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.interfaces.OnNetWorkErrorListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LuRecyclerView;
import com.github.jdsjlzx.recyclerview.LuRecyclerViewAdapter;
import com.ysxsoft.apaycoin.R;
import com.ysxsoft.apaycoin.adapte.ChangeTicketRecordAdapter;
import com.ysxsoft.apaycoin.com.ActivityPageManager;
import com.ysxsoft.apaycoin.com.BaseActivity;
import com.ysxsoft.apaycoin.impservice.ImpChangeTicketRecordService;
import com.ysxsoft.apaycoin.modle.ChangeTicketRecordBean;
import com.ysxsoft.apaycoin.utils.AppUtil;
import com.ysxsoft.apaycoin.utils.NetWork;

import java.lang.ref.WeakReference;
import java.util.List;

import retrofit2.Retrofit;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 描述： TODO
 * 日期： 2018/11/14 0014 11:23
 * 作者： 胡
 * 公司：郑州亿生信科技有限公司
 */
public class ChangeTicketRecordActivity extends BaseActivity implements View.OnClickListener,SwipeRefreshLayout.OnRefreshListener {

    private View img_back;
    private TextView tv_title;
    private int page=1;
    private LRecyclerView recyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private LuRecyclerView mRecyclerView = null;
    private LuRecyclerViewAdapter mLuRecyclerViewAdapter = null;
    private ChangeTicketRecordAdapter mDataAdapter;
    private PreviewHandler mHandler = new PreviewHandler(this);
    private ChangeTicketRecordBean changeTicketRecordBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exchange_record_layout);
        initView();
        initListener();
    }

    private void initView() {
        img_back = getViewById(R.id.img_back);
        tv_title = getViewById(R.id.tv_title);
        tv_title.setText("兑换记录");
        recyclerView = getViewById(R.id.recyclerView);

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        mRecyclerView = (LuRecyclerView) findViewById(R.id.list);
        //设置刷新时动画的颜色，可以设置4个
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setProgressViewOffset(false, 0, AppUtil.dip2px(this, 48));
            mSwipeRefreshLayout.setColorSchemeResources(R.color.btn_color);
            mSwipeRefreshLayout.setOnRefreshListener(this);
        }
        mDataAdapter = new ChangeTicketRecordAdapter(this);
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

        mRecyclerView.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (changeTicketRecordBean != null) {
                    if (page < Integer.valueOf(changeTicketRecordBean.getLast_page())) {
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
        private WeakReference<ChangeTicketRecordActivity> ref;
        PreviewHandler(ChangeTicketRecordActivity activity) {
            ref = new WeakReference<>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            final ChangeTicketRecordActivity activity = ref.get();
            if (activity == null || activity.isFinishing()) {
                return;
            }
            switch (msg.what) {
                case -1:
                    if(activity.mSwipeRefreshLayout.isRefreshing()){
                        activity.mDataAdapter.clear();
                    }
                    getData();
                    break;
                case -3:
                    if (mSwipeRefreshLayout.isRefreshing()) {
                        activity.mSwipeRefreshLayout.setRefreshing(false);
                        activity.mRecyclerView.refreshComplete(changeTicketRecordBean.getData().size());
                        activity.notifyDataSetChanged();
                    } else {
                        activity.mRecyclerView.setOnNetWorkErrorListener(new OnNetWorkErrorListener() {
                            @Override
                            public void reload() {
                                activity.mRecyclerView.refreshComplete(changeTicketRecordBean.getData().size());
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
    private void initListener() {
        img_back.setOnClickListener(this);
    }

    private void getData() {
        Retrofit retrofit = NetWork.getRetrofit();
        retrofit.create(ImpChangeTicketRecordService.class)
                .getCall(NetWork.getToken(),page+"")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ChangeTicketRecordBean>() {
                    private ChangeTicketRecordBean changeTicketRecordBean;

                    @Override
                    public void onCompleted() {
//                        showToastMessage(changeTicketRecordBean.getMsg());
//                        showToastMessage(changeTicketRecordBean.getMsg()+"===="+changeTicketRecordBean.getData().size());
                        if ("0".equals(changeTicketRecordBean.getCode())){
                            showData(changeTicketRecordBean);
                            List<ChangeTicketRecordBean.DataBean> beanList = changeTicketRecordBean.getData();
                            addItems(beanList);
                            if (mSwipeRefreshLayout.isRefreshing()) {
                                mSwipeRefreshLayout.setRefreshing(false);
                            }
                            mRecyclerView.refreshComplete(beanList.size());
                            notifyDataSetChanged();

                        }else if ("2".equals(changeTicketRecordBean.getCode())) {
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
                    public void onNext(ChangeTicketRecordBean changeTicketRecordBean) {

                        this.changeTicketRecordBean = changeTicketRecordBean;
                    }
                });
    }

    private void notifyDataSetChanged() {
        mLuRecyclerViewAdapter.notifyDataSetChanged();
    }

    private void addItems(List<ChangeTicketRecordBean.DataBean> beanList) {
        mDataAdapter.addAll(beanList);
    }

    private void showData(ChangeTicketRecordBean changeTicketRecordBean) {
        this.changeTicketRecordBean = changeTicketRecordBean;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_back:
                finish();
                break;
        }
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
        if (AppUtil.isNetworkAvaiable(mContext)) {
            mHandler.sendEmptyMessage(-1);
        } else {
            mHandler.sendEmptyMessage(-3);
        }
    }
}
