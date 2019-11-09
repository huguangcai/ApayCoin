package com.ysxsoft.apaycoin.view;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.github.jdsjlzx.recyclerview.LuRecyclerView;
import com.github.jdsjlzx.recyclerview.LuRecyclerViewAdapter;
import com.ysxsoft.apaycoin.R;
import com.ysxsoft.apaycoin.adapte.ExchangeRecordAdapter;
import com.ysxsoft.apaycoin.com.BaseActivity;
import com.ysxsoft.apaycoin.utils.AppUtil;

/**
 * 描述： 兑换记录  没有用到  用了ChangeTicketRecordActivity
 * 日期： 2018/11/5 0005 15:38
 * 作者： 胡
 * 公司：郑州亿生信科技有限公司
 */
public class ExchangeRecordActivity extends BaseActivity implements View.OnClickListener,SwipeRefreshLayout.OnRefreshListener {

    private View img_back;
    private TextView tv_title;
    private int page=1;
    private RecyclerView rv_listview;
    private ExchangeRecordAdapter adapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private LuRecyclerView mRecyclerView = null;
    private LuRecyclerViewAdapter mLuRecyclerViewAdapter = null;
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
        rv_listview = getViewById(R.id.rv_listview);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        mRecyclerView = (LuRecyclerView) findViewById(R.id.list);
        //设置刷新时动画的颜色，可以设置4个
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setProgressViewOffset(false, 0, AppUtil.dip2px(this, 48));
            mSwipeRefreshLayout.setColorSchemeResources(R.color.btn_color);
            mSwipeRefreshLayout.setOnRefreshListener(this);
        }
//        ExchangeRecordAdapter mDataAdapter=new ExchangeRecordAdapter(this);

    }

    @SuppressLint("ResourceAsColor")
    private void initListView() {
//        adapter = new ExchangeRecordAdapter(mContext,new ArrayList<Object>());
    }

    private void initListener() {
        img_back.setOnClickListener(this);
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


    }
}
