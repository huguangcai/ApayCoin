package com.ysxsoft.apaycoin.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.jdsjlzx.ItemDecoration.LuDividerDecoration;
import com.github.jdsjlzx.interfaces.OnItemClickListener;
import com.github.jdsjlzx.interfaces.OnItemLongClickListener;
import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.interfaces.OnNetWorkErrorListener;
import com.github.jdsjlzx.recyclerview.LuRecyclerView;
import com.github.jdsjlzx.recyclerview.LuRecyclerViewAdapter;
import com.ysxsoft.apaycoin.R;
import com.ysxsoft.apaycoin.adapte.PersonInfoAdapter;
import com.ysxsoft.apaycoin.com.ActivityPageManager;
import com.ysxsoft.apaycoin.com.BaseActivity;
import com.ysxsoft.apaycoin.impservice.ImpPersonInfoDeleteService;
import com.ysxsoft.apaycoin.impservice.ImpPersonInfoService;
import com.ysxsoft.apaycoin.modle.DeleteInfoBean;
import com.ysxsoft.apaycoin.modle.PersonInfoBean;
import com.ysxsoft.apaycoin.utils.AppUtil;
import com.ysxsoft.apaycoin.utils.NetWork;
import com.ysxsoft.apaycoin.widget.LongDialog;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 描述： 个人消息界面
 * 日期： 2018/11/6 0006 13:57
 * 作者： 胡
 * 公司：郑州亿生信科技有限公司
 */
public class PersonInfoActivity extends BaseActivity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    private View img_back;
    private TextView tv_title, tv_title_right, tv_delete;
    private LinearLayout ll_is_show, ll_all_select;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private LuRecyclerView mRecyclerView = null;
    private LuRecyclerViewAdapter mLuRecyclerViewAdapter = null;
    private int page = 1;
    private PersonInfoAdapter mDataAdapter;
    private PersonInfoBean personInfoBean;
    private PreviewHandler mHandler = new PreviewHandler(this);
    private boolean isBecome = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.person_info_layout);
        initView();
        initListener();
    }

    private void initView() {
        img_back = getViewById(R.id.img_back);
        tv_title = getViewById(R.id.tv_title);
        tv_title.setText("个人消息");
        tv_title_right = getViewById(R.id.tv_title_right);
        tv_title_right.setText("管理");
        tv_title_right.setVisibility(View.GONE);
        ll_is_show = getViewById(R.id.ll_is_show);
        ll_all_select = getViewById(R.id.ll_all_select);
        tv_delete = getViewById(R.id.tv_delete);
        mSwipeRefreshLayout = (SwipeRefreshLayout) getViewById(R.id.swipe_refresh_layout);
        mRecyclerView = (LuRecyclerView) getViewById(R.id.list);

        //设置刷新时动画的颜色，可以设置4个
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setProgressViewOffset(false, 0, AppUtil.dip2px(this, 48));
            mSwipeRefreshLayout.setColorSchemeResources(R.color.btn_color);
            mSwipeRefreshLayout.setOnRefreshListener(this);
        }
        mDataAdapter = new PersonInfoAdapter(this);
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
                CheckBox cb_box = view.findViewById(R.id.cb_box);

            }
        });
        mLuRecyclerViewAdapter.setOnItemLongClickListener(new OnItemLongClickListener() {
            @Override
            public void onItemLongClick(View view, int position) {
                PersonInfoBean.DataBean dataBean = mDataAdapter.getDataList().get(position);
                final String msg_id = dataBean.getMsg_id();
                final LongDialog dialog = new LongDialog(mContext);
                TextView tv_delete = dialog.findViewById(R.id.tv_delete);
                tv_delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deleteData(msg_id, dialog);
                    }
                });
                dialog.show();
            }
        });
        mRecyclerView.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (personInfoBean != null) {
                    if (page < Integer.valueOf(personInfoBean.getLast_page())) {
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

    /**
     * 删除
     *
     * @param msg_id
     * @param dialog
     */
    private void deleteData(String msg_id, final LongDialog dialog) {
        NetWork.getRetrofit()
                .create(ImpPersonInfoDeleteService.class)
                .getCall(NetWork.getToken(), msg_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<DeleteInfoBean>() {
                    private DeleteInfoBean deleteInfoBean;

                    @Override
                    public void onCompleted() {
                        showToastMessage(deleteInfoBean.getMsg());
                        if ("0".equals(deleteInfoBean.getCode())){
                            dialog.dismiss();
                            onRefresh();
                        }else if ("2" .equals( deleteInfoBean.getCode())) {
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
                    public void onNext(DeleteInfoBean deleteInfoBean) {
                        this.deleteInfoBean = deleteInfoBean;
                    }
                });


    }

    private void initListener() {
        img_back.setOnClickListener(this);
        tv_title_right.setOnClickListener(this);
        ll_all_select.setOnClickListener(this);
        tv_delete.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.tv_title_right:
                if (isBecome) {
                    ll_all_select.setVisibility(View.VISIBLE);
                    tv_title_right.setText("完成");
                    isBecome = false;
                    ll_is_show.setVisibility(View.VISIBLE);
                    mDataAdapter.isShow(true);
                } else {
                    ll_all_select.setVisibility(View.GONE);
                    ll_is_show.setVisibility(View.GONE);
                    tv_title_right.setText("管理");
                    isBecome = true;
                    mDataAdapter.isShow(false);
                }
                break;
            case R.id.ll_all_select:
                showToastMessage("全选");
                break;
            case R.id.tv_delete:
                showToastMessage("删除");
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

    private class PreviewHandler extends Handler {
        private WeakReference<PersonInfoActivity> ref;

        PreviewHandler(PersonInfoActivity activity) {
            ref = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            final PersonInfoActivity activity = ref.get();
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
                        activity.mRecyclerView.refreshComplete(personInfoBean.getData().size());
                        activity.notifyDataSetChanged();
                    } else {
                        activity.mRecyclerView.setOnNetWorkErrorListener(new OnNetWorkErrorListener() {
                            @Override
                            public void reload() {
                                activity.mRecyclerView.refreshComplete(personInfoBean.getData().size());
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
                .create(ImpPersonInfoService.class)
                .getCall(NetWork.getToken(), page + "")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<PersonInfoBean>() {
                    private PersonInfoBean personInfoBean;

                    @Override
                    public void onCompleted() {
//                        showToastMessage(personInfoBean.getMsg());
                        if ("0".equals(personInfoBean.getCode())) {
                            showData(personInfoBean);
                            ArrayList<PersonInfoBean.DataBean> data = personInfoBean.getData();
                            addItems(data);
                            if (mSwipeRefreshLayout.isRefreshing()) {
                                mSwipeRefreshLayout.setRefreshing(false);
                            }
                            mRecyclerView.refreshComplete(data.size());
                            notifyDataSetChanged();
                        }else if ("2" .equals( personInfoBean.getCode())) {
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
                    public void onNext(PersonInfoBean personInfoBean) {

                        this.personInfoBean = personInfoBean;
                    }
                });


    }

    private void notifyDataSetChanged() {
        mLuRecyclerViewAdapter.notifyDataSetChanged();
    }

    private void addItems(ArrayList<PersonInfoBean.DataBean> data) {
        mDataAdapter.addAll(data);
    }

    private void showData(PersonInfoBean personInfoBean) {
        this.personInfoBean = personInfoBean;
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
