package com.ysxsoft.apaycoin.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.github.jdsjlzx.ItemDecoration.LuDividerDecoration;
import com.github.jdsjlzx.interfaces.OnItemClickListener;
import com.github.jdsjlzx.interfaces.OnNetWorkErrorListener;
import com.github.jdsjlzx.recyclerview.LuRecyclerView;
import com.github.jdsjlzx.recyclerview.LuRecyclerViewAdapter;
import com.ysxsoft.apaycoin.R;
import com.ysxsoft.apaycoin.adapte.AddressManagerAdapter;
import com.ysxsoft.apaycoin.com.ActivityPageManager;
import com.ysxsoft.apaycoin.com.BaseActivity;
import com.ysxsoft.apaycoin.impservice.ImpAddressDeleteService;
import com.ysxsoft.apaycoin.impservice.ImpAddressListEditNormalService;
import com.ysxsoft.apaycoin.impservice.ImpAddressManagerService;
import com.ysxsoft.apaycoin.modle.AddMyAddressBean;
import com.ysxsoft.apaycoin.modle.AddressDeleteBean;
import com.ysxsoft.apaycoin.modle.AddressManagerBean;
import com.ysxsoft.apaycoin.utils.AppUtil;
import com.ysxsoft.apaycoin.utils.NetWork;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import retrofit2.Retrofit;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 描述： 地址管理界面
 * 日期： 2018/11/6 0006 13:40
 * 作者： 胡
 * 公司：郑州亿生信科技有限公司
 */
public class AddressManagerActivity extends BaseActivity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    private View img_back;
    private TextView tv_title, tv_new_add_address;
    private int page = 1;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private LuRecyclerView mRecyclerView = null;
    private LuRecyclerViewAdapter mLuRecyclerViewAdapter = null;
    private AddressManagerAdapter mDataAdapter;
    private PreviewHandler mHandler = new PreviewHandler(this);
    private AddressManagerBean addressManagerBean;
    private int is_check = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.address_manager_layout);
        initView();
        initListener();
    }

    private void initListener() {
        img_back.setOnClickListener(this);
        tv_new_add_address.setOnClickListener(this);
    }

    private void initView() {
        img_back = getViewById(R.id.img_back);
        tv_title = getViewById(R.id.tv_title);
        tv_new_add_address = getViewById(R.id.tv_new_add_address);
        tv_title.setText("地址管理");

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        mRecyclerView = (LuRecyclerView) findViewById(R.id.list);
        //设置刷新时动画的颜色，可以设置4个
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setProgressViewOffset(false, 0, AppUtil.dip2px(this, 48));
            mSwipeRefreshLayout.setColorSchemeResources(R.color.btn_color);
            mSwipeRefreshLayout.setOnRefreshListener(this);
        }

        mDataAdapter = new AddressManagerAdapter(this);
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
        mDataAdapter.setOnDeleteClickListener(new AddressManagerAdapter.OnDeleteClickListener() {
            @Override
            public void deleteItem(int position) {
                AddressManagerBean.DataBean dataBean = mDataAdapter.getDataList().get(position);
                String aid = dataBean.getAid();
                String is_ture = dataBean.getIs_ture();
                if ("1".equals(is_ture)) {
                    showToastMessage("默认地址不能删除");
                } else {
                    deleteData(aid);
                }
            }
        });
        mLuRecyclerViewAdapter.setOnItemClickListener(new OnItemClickListener() {
            int currentNum = -1;

            @Override
            public void onItemClick(View view, int position) {
                if ("1".equals(mDataAdapter.getDataList().get(position).getIs_ture())) {
                    return;
                }
                for (AddressManagerBean.DataBean date : addressManagerBean.getData()) { //遍历list集合中的数据
                    date.setIs_ture("0");//全部设为未选中
                }
                if (currentNum == -1) { //选中
                    addressManagerBean.getData().get(position).setIs_ture("1");
                    currentNum = position;
                } else if (currentNum == position) { //同一个item选中变未选中
                    for (AddressManagerBean.DataBean date : addressManagerBean.getData()) { //遍历list集合中的数据
                        date.setIs_ture("0");//全部设为未选中
                    }
                    currentNum = -1;
                } else if (currentNum != position) { //不是同一个item选中当前的，去除上一个选中的
                    for (AddressManagerBean.DataBean date : addressManagerBean.getData()) { //遍历list集合中的数据
                        date.setIs_ture("0");//全部设为未选中
                    }
                    addressManagerBean.getData().get(position).setIs_ture("1");
                    currentNum = position;
                }
                isSettingNormal(position);
                mDataAdapter.notifyDataSetChanged();
                mLuRecyclerViewAdapter.notifyDataSetChanged();
            }
        });
        //设置底部加载颜色
        mRecyclerView.setFooterViewColor(R.color.btn_color, R.color.black, android.R.color.white);
        //设置底部加载文字提示
        mRecyclerView.setFooterViewHint("拼命加载中", "没有更多数据了", "网络不给力啊，点击再试一次吧");
//        onRefresh();
    }

    //删除
    private void deleteData(String aid) {
        NetWork.getRetrofit()
                .create(ImpAddressDeleteService.class)
                .getCall(NetWork.getToken(), aid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<AddressDeleteBean>() {
                    private AddressDeleteBean addressDeleteBean;

                    @Override
                    public void onCompleted() {
                        showToastMessage(addressDeleteBean.getMsg());
                        if ("0".equals(addressDeleteBean.getCode())) {
                            onRefresh();
                        } else if ("2".equals(addressDeleteBean.getCode())) {
                            SharedPreferences.Editor sp = mContext.getSharedPreferences("save_name_pwd", Context.MODE_PRIVATE).edit();
                            sp.clear();
                            sp.commit();
                            SharedPreferences.Editor is_first = mContext.getSharedPreferences("IS_FIRST", Context.MODE_PRIVATE).edit();
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
                    public void onNext(AddressDeleteBean addressDeleteBean) {
                        this.addressDeleteBean = addressDeleteBean;
                    }
                });

    }

    /**
     * 是否设置为默认
     *
     * @param position
     */
    private void isSettingNormal(int position) {
        AddressManagerBean.DataBean dataBean = addressManagerBean.getData().get(position);
        Retrofit retrofit = NetWork.getRetrofit();
        retrofit.create(ImpAddressListEditNormalService.class)
                .getCall(NetWork.getToken(),
                        dataBean.getAid(),
                        dataBean.getAddress(),
                        is_check + "",
                        dataBean.getProvice(),
                        dataBean.getCity(),
                        dataBean.getArea(),
                        "",
                        "")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<AddMyAddressBean>() {
                    private AddMyAddressBean addMyAddressBean;

                    @Override
                    public void onCompleted() {
                        showToastMessage(addMyAddressBean.getMsg());
                        if ("2".equals(addMyAddressBean.getCode())) {
                            SharedPreferences.Editor sp = getSharedPreferences("save_name_pwd", Context.MODE_PRIVATE).edit();
                            sp.clear();
                            sp.commit();
                            SharedPreferences.Editor is_first = getSharedPreferences("IS_FIRST", Context.MODE_PRIVATE).edit();
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
                    public void onNext(AddMyAddressBean addMyAddressBean) {
                        this.addMyAddressBean = addMyAddressBean;
                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.tv_new_add_address:
                startActivity(NewAddAddressActivity.class);
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

    private class PreviewHandler extends Handler {
        private WeakReference<AddressManagerActivity> ref;

        PreviewHandler(AddressManagerActivity activity) {
            ref = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            final AddressManagerActivity activity = ref.get();
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
                        activity.mRecyclerView.refreshComplete(addressManagerBean.getData().size());
                        activity.notifyDataSetChanged();
                    } else {
                        activity.mRecyclerView.setOnNetWorkErrorListener(new OnNetWorkErrorListener() {
                            @Override
                            public void reload() {
                                activity.mRecyclerView.refreshComplete(addressManagerBean.getData().size());
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
        Retrofit retrofit = NetWork.getRetrofit();
        retrofit.create(ImpAddressManagerService.class)
                .getCall(NetWork.getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<AddressManagerBean>() {
                    private AddressManagerBean addressManagerBean;

                    @Override
                    public void onCompleted() {
//                        showToastMessage(addressManagerBean.getMsg());
                        if ("0".equals(addressManagerBean.getCode())) {
                            showData(addressManagerBean);
                            ArrayList<AddressManagerBean.DataBean> data = addressManagerBean.getData();
                            addItems(data);
                            if (mSwipeRefreshLayout.isRefreshing()) {
                                mSwipeRefreshLayout.setRefreshing(false);
                            }
                            mRecyclerView.refreshComplete(data.size());
                            notifyDataSetChanged();

                        } else if ("2".equals(addressManagerBean.getCode())) {
                            SharedPreferences.Editor sp = getSharedPreferences("save_name_pwd", Context.MODE_PRIVATE).edit();
                            sp.clear();
                            sp.commit();
                            SharedPreferences.Editor is_first = getSharedPreferences("IS_FIRST", Context.MODE_PRIVATE).edit();
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
                        if (mSwipeRefreshLayout.isRefreshing()) {
                            mSwipeRefreshLayout.setRefreshing(false);
                        }
                        showToastMessage(e.getMessage());
                    }

                    @Override
                    public void onNext(AddressManagerBean addressManagerBean) {
                        this.addressManagerBean = addressManagerBean;
                    }
                });
    }

    private void addItems(ArrayList<AddressManagerBean.DataBean> data) {
        mDataAdapter.addAll(data);
    }

    private void notifyDataSetChanged() {
        mLuRecyclerViewAdapter.notifyDataSetChanged();
    }

    private void showData(AddressManagerBean addressManagerBean) {
        this.addressManagerBean = addressManagerBean;
    }

    @Override
    protected void onResume() {
        super.onResume();
        onRefresh();
    }
}
