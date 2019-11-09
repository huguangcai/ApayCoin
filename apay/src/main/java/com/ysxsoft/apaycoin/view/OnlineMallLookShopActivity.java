package com.ysxsoft.apaycoin.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;
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
import com.ysxsoft.apaycoin.adapte.GoodsListAdapter;
import com.ysxsoft.apaycoin.adapte.OnlineMallLookShopAdapter;
import com.ysxsoft.apaycoin.com.ActivityPageManager;
import com.ysxsoft.apaycoin.com.BaseActivity;
import com.ysxsoft.apaycoin.impservice.ImpDeleteGoodsService;
import com.ysxsoft.apaycoin.impservice.ImpGoodsListService;
import com.ysxsoft.apaycoin.impservice.ImpMyMallHeaderInfoService;
import com.ysxsoft.apaycoin.impservice.ImpShopInfoService;
import com.ysxsoft.apaycoin.impservice.OnlineMallLookShopHeaderService;
import com.ysxsoft.apaycoin.modle.DeleteGoodsBean;
import com.ysxsoft.apaycoin.modle.GoodsListBean;
import com.ysxsoft.apaycoin.modle.MyMallHeaderInfoBean;
import com.ysxsoft.apaycoin.modle.OnlineMallGoodsListBean;
import com.ysxsoft.apaycoin.utils.AppUtil;
import com.ysxsoft.apaycoin.utils.ImageLoadUtil;
import com.ysxsoft.apaycoin.utils.NetWork;
import com.ysxsoft.apaycoin.widget.CircleImageView;
import com.ysxsoft.apaycoin.widget.LongDialog;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class OnlineMallLookShopActivity extends BaseActivity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    private View img_back;
    private TextView tv_title,  tv_shop_name, tv_phone_num, tv_balance_money, tv_ticket_num, tv_new_add_goods, tv_goods_type;
    private LinearLayout ll_credit_star, ll_no_credit_star,ll_income, ll_no_goods_tip,ll_new;
    private ImageView img_down_arrow;
    private CircleImageView img_head;
    private String star;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private LuRecyclerView mRecyclerView = null;
    private LuRecyclerViewAdapter mLuRecyclerViewAdapter = null;
    private OnlineMallLookShopAdapter mDataAdapter;
    private int page = 1;
    private PreviewHandler mHandler = new PreviewHandler(this);
    private String uid;
    private MyMallHeaderInfoBean myMallHeaderInfoBean;
    private OnlineMallGoodsListBean goodsListBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.business_my_shop_detail_layout);
        Intent intent = getIntent();
        uid = intent.getStringExtra("uid");
        requestHeaderData();
        initView();
//        getData();
        initListener();
    }

    private void initView() {
        img_back = getViewById(R.id.img_back);
        tv_title = getViewById(R.id.tv_title);

        img_down_arrow = getViewById(R.id.img_down_arrow);
        img_down_arrow.setVisibility(View.GONE);
        img_head = getViewById(R.id.img_head);
        tv_shop_name = getViewById(R.id.tv_shop_name);
        tv_phone_num = getViewById(R.id.tv_phone_num);
        ll_credit_star = getViewById(R.id.ll_credit_star);
        ll_no_credit_star = getViewById(R.id.ll_no_credit_star);
        ll_income = getViewById(R.id.ll_income);
        ll_new = getViewById(R.id.ll_new);
        tv_balance_money = getViewById(R.id.tv_balance_money);
        tv_ticket_num = getViewById(R.id.tv_ticket_num);
        tv_goods_type = getViewById(R.id.tv_goods_type);
        ll_no_goods_tip = getViewById(R.id.ll_no_goods_tip);
        tv_new_add_goods = getViewById(R.id.tv_new_add_goods);
        ll_income.setVisibility(View.GONE);
        ll_new.setVisibility(View.GONE);

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        mRecyclerView = (LuRecyclerView) findViewById(R.id.list);
        //设置刷新时动画的颜色，可以设置4个
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setProgressViewOffset(false, 0, AppUtil.dip2px(this, 48));
            mSwipeRefreshLayout.setColorSchemeResources(R.color.btn_color);
            mSwipeRefreshLayout.setOnRefreshListener(this);
        }
        mDataAdapter = new OnlineMallLookShopAdapter(this);

        mRecyclerView.setLayoutManager(new GridLayoutManager(this,2));
        mLuRecyclerViewAdapter = new LuRecyclerViewAdapter(mDataAdapter);
        mRecyclerView.setAdapter(mLuRecyclerViewAdapter);
        LuDividerDecoration divider = new LuDividerDecoration.Builder(this, mLuRecyclerViewAdapter)
                .setHeight(R.dimen.default_divider_height)
                .setPadding(R.dimen.default_divider_padding)
                .setColorResource(R.color.gray)
                .build();
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addItemDecoration(divider);
        mLuRecyclerViewAdapter.addHeaderView(new MallGoodsDetialHeader(this));

        mLuRecyclerViewAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                List<OnlineMallGoodsListBean.DataBean> dataList = mDataAdapter.getDataList();
                OnlineMallGoodsListBean.DataBean dataBean = dataList.get(position);
                String pid = dataBean.getPid();
                String stock = dataBean.getStock();
                String money = dataBean.getMoney();
                Intent intent=new Intent(mContext,OnlineMallGoodsDetialActivity.class);
                intent.putExtra("pid",pid);
                intent.putExtra("stock",stock);
                intent.putExtra("money",money);
                startActivity(intent);
//                finish();
            }
        });
        mRecyclerView.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (goodsListBean != null) {
                    if (page < Integer.valueOf(goodsListBean.getLast_page())) {
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

    /**
     * 获取我的店铺头部信息
     */
    private void requestHeaderData() {
        NetWork.getRetrofit()
                .create(OnlineMallLookShopHeaderService.class)
                .getCall(NetWork.getToken(),uid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<MyMallHeaderInfoBean>() {
                    private MyMallHeaderInfoBean myMallHeaderInfoBean;

                    @Override
                    public void onCompleted() {
//                        showToastMessage(myMallHeaderInfoBean.getMsg());
                        if ("0".equals(myMallHeaderInfoBean.getCode())) {
                            if ("0".equals(myMallHeaderInfoBean.getCode())) {
                                MyMallHeaderInfoBean.DataBean data = myMallHeaderInfoBean.getData();
                                star = data.getStar();
                                ImageLoadUtil.GlideImageLoad(mContext, data.getIcon(), img_head);
                                tv_title.setText(data.getName());
                                tv_shop_name.setText(data.getName());
                                tv_phone_num.setText(data.getPhone());
                                tv_balance_money.setText(data.getMoney());
                                tv_ticket_num.setText(data.getAccount());
                                tv_goods_type.setText(data.getCid());
                                for (int i = 0; i < Integer.valueOf(star); i++) {
                                    ImageView imageView = new ImageView(mContext);
                                    imageView.setBackgroundResource(R.mipmap.img_credit_star);
                                    ll_credit_star.addView(imageView);
                                }
                                for (int i = 0; i <5-Integer.valueOf(star); i++) {
                                    ImageView imageView = new ImageView(mContext);
                                    imageView.setBackgroundResource(R.mipmap.img_gray_star);
                                    ll_no_credit_star.addView(imageView);
                                }
                            }
                        }else if ("2" .equals( myMallHeaderInfoBean.getCode())) {
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
                    public void onNext(MyMallHeaderInfoBean myMallHeaderInfoBean) {
                        this.myMallHeaderInfoBean = myMallHeaderInfoBean;
                    }
                });

    }

    private void showhearderData(MyMallHeaderInfoBean myMallHeaderInfoBean) {
        this.myMallHeaderInfoBean = myMallHeaderInfoBean;
    }

    private void initListener() {
        img_back.setOnClickListener(this);
        img_down_arrow.setOnClickListener(this);
        tv_new_add_goods.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.img_down_arrow:
                showToastMessage("弹出popupwindow");
                break;
            case R.id.tv_new_add_goods:
                startActivity(NewAddGoodsActivity.class);
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
        private WeakReference<OnlineMallLookShopActivity> ref;
        PreviewHandler(OnlineMallLookShopActivity activity) {
            ref = new WeakReference<>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            final OnlineMallLookShopActivity activity = ref.get();
            if (activity == null || activity.isFinishing()) {
                return;
            }
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
                        mRecyclerView.refreshComplete(goodsListBean.getData().size());
                        notifyDataSetChanged();
                    } else {
                        mRecyclerView.setOnNetWorkErrorListener(new OnNetWorkErrorListener() {
                            @Override
                            public void reload() {
                                mRecyclerView.refreshComplete(goodsListBean.getData().size());
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

    /**
     * 获取数据
     */
    private void getData() {
        NetWork.getRetrofit()
                .create(ImpShopInfoService.class)
                .getCall(NetWork.getToken(), page + "",uid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<OnlineMallGoodsListBean>() {
                    private OnlineMallGoodsListBean goodsListBean;
                    @Override
                    public void onCompleted() {
//                        showToastMessage(goodsListBean.getMsg());
                        if ("0".equals(goodsListBean.getCode())) {
                            showData(goodsListBean);
                            ArrayList<OnlineMallGoodsListBean.DataBean> data = goodsListBean.getData();
                            addItems(goodsListBean.getData());
                            if (data.size() <= 0) {
                                ll_no_goods_tip.setVisibility(View.VISIBLE);
                                mSwipeRefreshLayout.setVisibility(View.GONE);
                            } else {
                                ll_no_goods_tip.setVisibility(View.GONE);
                                mSwipeRefreshLayout.setVisibility(View.VISIBLE);
                            }

                            if (mSwipeRefreshLayout.isRefreshing()) {
                                mSwipeRefreshLayout.setRefreshing(false);
                            }
                            mRecyclerView.refreshComplete(data.size());//一页加载的数量
                            notifyDataSetChanged();
                        }else if ("2" .equals( goodsListBean.getCode())) {
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
                    public void onNext(OnlineMallGoodsListBean goodsListBean) {
                        this.goodsListBean = goodsListBean;
                    }
                });
    }

    private void showData(OnlineMallGoodsListBean goodsListBean) {
        this.goodsListBean = goodsListBean;
    }

    private void notifyDataSetChanged() {
        mLuRecyclerViewAdapter.notifyDataSetChanged();
    }

    private void addItems(ArrayList<OnlineMallGoodsListBean.DataBean> data) {
        mDataAdapter.addAll(data);
    }

    @Override
    protected void onResume() {
        super.onResume();
        onRefresh();
    }
}
