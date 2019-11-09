package com.ysxsoft.apaycoin.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.jdsjlzx.ItemDecoration.LuDividerDecoration;
import com.github.jdsjlzx.interfaces.OnNetWorkErrorListener;
import com.github.jdsjlzx.recyclerview.LuRecyclerView;
import com.github.jdsjlzx.recyclerview.LuRecyclerViewAdapter;
import com.ysxsoft.apaycoin.R;
import com.ysxsoft.apaycoin.adapte.NewAddGoodsEditorListAdapter;
import com.ysxsoft.apaycoin.banner.Banner;
import com.ysxsoft.apaycoin.banner.GlideImageLoader;
import com.ysxsoft.apaycoin.banner.OnBannerListener;
import com.ysxsoft.apaycoin.com.ActivityPageManager;
import com.ysxsoft.apaycoin.com.BaseActivity;
import com.ysxsoft.apaycoin.impservice.ImpMallGoodsDetialHeaderService;
import com.ysxsoft.apaycoin.impservice.ImpNewAddGoodsEditorListService;
import com.ysxsoft.apaycoin.modle.MallGoodsDetialHeaderBean;
import com.ysxsoft.apaycoin.modle.NewAddGoodsEditorListBean;
import com.ysxsoft.apaycoin.utils.AppUtil;
import com.ysxsoft.apaycoin.utils.NetWork;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class OnlineMallGoodsDetialActivity extends BaseActivity implements View.OnClickListener, OnBannerListener, SwipeRefreshLayout.OnRefreshListener {

    private View img_back;
    private TextView tv_title, tv_stock_num, tv_content_title, tv_price;
    private String pid, uid, money, stock;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private LuRecyclerView mRecyclerView = null;
    private LuRecyclerViewAdapter mLuRecyclerViewAdapter = null;
    private NewAddGoodsEditorListAdapter mDataAdapter;
    private Banner vp_banner;
    private ArrayList<String> urls = new ArrayList<>();
    private int page = 1;
    private PreviewHandler mHandler = new PreviewHandler(this);
    private NewAddGoodsEditorListBean newAddGoodsEditorListBean;
    private LinearLayout ll_mall;
    private LinearLayout ll_customer;
    private TextView tv_immediately_buy;
    private String kefyPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.googs_detail_layout);
        getIntentData();
        initView();
        getHeaderData();
        initListener();
    }

    /**
     * 获取头部信息
     */
    private void getHeaderData() {
        NetWork.getRetrofit()
                .create(ImpMallGoodsDetialHeaderService.class)
                .getCall(NetWork.getToken(), pid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<MallGoodsDetialHeaderBean>() {
                    private MallGoodsDetialHeaderBean mallGoodsDetialHeaderBean;

                    @Override
                    public void onCompleted() {
//                        showToastMessage(mallGoodsDetialHeaderBean.getMsg());
                        if ("0".equals(mallGoodsDetialHeaderBean.getCode())) {
                            tv_content_title.setText(AppUtil.stringReplace(mallGoodsDetialHeaderBean.getData().getTitle()));
                            tv_price.setText(mallGoodsDetialHeaderBean.getData().getMoney());
                            tv_stock_num.setText("库存：" + mallGoodsDetialHeaderBean.getData().getStock());
                            kefyPhone = mallGoodsDetialHeaderBean.getData().getKefy();
                            uid = mallGoodsDetialHeaderBean.getData().getUid();
                            ArrayList<MallGoodsDetialHeaderBean.DataBean.LunBo> lunbo = mallGoodsDetialHeaderBean.getData().getLunbo();
                            for (int i = 0; i < lunbo.size(); i++) {
                                MallGoodsDetialHeaderBean.DataBean.LunBo lunBo = lunbo.get(i);
                                String icon = lunBo.getIcon();
                                urls.add(icon);
                            }
                            vp_banner.setImages(urls)
                                    .setImageLoader(new GlideImageLoader())
                                    .setOnBannerListener(OnlineMallGoodsDetialActivity.this)
                                    .start();
                        }else if ("2" == mallGoodsDetialHeaderBean.getCode()) {
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
                    public void onNext(MallGoodsDetialHeaderBean mallGoodsDetialHeaderBean) {
                        this.mallGoodsDetialHeaderBean = mallGoodsDetialHeaderBean;
                    }
                });

    }

    private void getIntentData() {
        Intent intent = getIntent();
        pid = intent.getStringExtra("pid");
        uid = intent.getStringExtra("uid");
        money = intent.getStringExtra("money");
        stock = intent.getStringExtra("stock");
    }

    private void initView() {
        img_back = getViewById(R.id.img_back);
        tv_title = getViewById(R.id.tv_title);
        tv_content_title = getViewById(R.id.tv_content_title);
        tv_price = getViewById(R.id.tv_price);
        tv_stock_num = getViewById(R.id.tv_stock_num);
        tv_title.setText("商品详情");
        vp_banner = getViewById(R.id.vp_banner);
        ll_mall = getViewById(R.id.ll_mall);
        ll_customer = getViewById(R.id.ll_customer);
        tv_immediately_buy = getViewById(R.id.tv_immediately_buy);

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        mRecyclerView = (LuRecyclerView) findViewById(R.id.list);

        //设置刷新时动画的颜色，可以设置4个
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setProgressViewOffset(false, 0, AppUtil.dip2px(this, 48));
            mSwipeRefreshLayout.setColorSchemeResources(R.color.btn_color);
            mSwipeRefreshLayout.setOnRefreshListener(this);
        }
        mDataAdapter = new NewAddGoodsEditorListAdapter(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mLuRecyclerViewAdapter = new LuRecyclerViewAdapter(mDataAdapter);
        mRecyclerView.setAdapter(mLuRecyclerViewAdapter);
        LuDividerDecoration divider = new LuDividerDecoration.Builder(this, mLuRecyclerViewAdapter)
                .setHeight(R.dimen.default_divider_height)
                .setPadding(R.dimen.default_divider_padding)
                .setColorResource(R.color.white)
                .build();
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addItemDecoration(divider);
        mLuRecyclerViewAdapter.addHeaderView(new MallGoodsDetialHeader(this));

        //设置底部加载颜色
        mRecyclerView.setFooterViewColor(R.color.btn_color, R.color.black, android.R.color.white);
        //设置底部加载文字提示
        mRecyclerView.setFooterViewHint("拼命加载中", "没有更多数据了", "网络不给力啊，点击再试一次吧");
//        onRefresh();

    }

    private void initListener() {
        img_back.setOnClickListener(this);
        ll_mall.setOnClickListener(this);
        ll_customer.setOnClickListener(this);
        tv_immediately_buy.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.ll_mall:
                Intent intent2 = new Intent(mContext, OnlineMallLookShopActivity.class);
                intent2.putExtra("uid", uid);
                startActivity(intent2);
//                finish();
                break;
            case R.id.ll_customer:
                Intent intent1 = new Intent(Intent.ACTION_DIAL);
                Uri data = Uri.parse("tel:" + kefyPhone);
                intent1.setData(data);
                startActivity(intent1);
                break;
            case R.id.tv_immediately_buy:
                Intent intent = new Intent(mContext, OrderCheckActivity.class);
                intent.putExtra("pid", pid);
                intent.putExtra("uid", uid);
                intent.putExtra("money", money);
                intent.putExtra("stock", stock);
                startActivity(intent);
                break;

        }
    }

    @Override
    public void OnBannerClick(int position) {

    }

    @Override
    public void onRefresh() {
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
        private WeakReference<OnlineMallGoodsDetialActivity> ref;

        PreviewHandler(OnlineMallGoodsDetialActivity activity) {
            ref = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            final OnlineMallGoodsDetialActivity activity = ref.get();
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
                        activity.mRecyclerView.refreshComplete(newAddGoodsEditorListBean.getData().size());
                        activity.notifyDataSetChanged();
                    } else {
                        activity.mRecyclerView.setOnNetWorkErrorListener(new OnNetWorkErrorListener() {
                            @Override
                            public void reload() {
                                activity.mRecyclerView.refreshComplete(newAddGoodsEditorListBean.getData().size());
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
                .create(ImpNewAddGoodsEditorListService.class)
                .getCall(NetWork.getToken(), pid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<NewAddGoodsEditorListBean>() {
                    private NewAddGoodsEditorListBean newAddGoodsEditorListBean;

                    @Override
                    public void onCompleted() {
//                        showToastMessage(newAddGoodsEditorListBean.getMsg());
                        if ("0".equals(newAddGoodsEditorListBean.getCode())) {
                            showData(newAddGoodsEditorListBean);
                            ArrayList<NewAddGoodsEditorListBean.DataBean> data = newAddGoodsEditorListBean.getData();
                            mDataAdapter.addAll(data);
                            if (mSwipeRefreshLayout.isRefreshing()) {
                                mSwipeRefreshLayout.setRefreshing(false);
                            }
                            mRecyclerView.refreshComplete(data.size());
                            notifyDataSetChanged();
                        }else if ("2" .equals( newAddGoodsEditorListBean.getCode())) {
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
                    public void onNext(NewAddGoodsEditorListBean newAddGoodsEditorListBean) {
                        this.newAddGoodsEditorListBean = newAddGoodsEditorListBean;
                    }
                });
    }

    private void showData(NewAddGoodsEditorListBean newAddGoodsEditorListBean) {
        this.newAddGoodsEditorListBean = newAddGoodsEditorListBean;
    }

    private void notifyDataSetChanged() {
        mLuRecyclerViewAdapter.notifyDataSetChanged();
    }

    //如果你需要考虑更好的体验，可以这么操作
    @Override
    protected void onStart() {
        super.onStart();
        //开始轮播
        vp_banner.startAutoPlay();
    }

    @Override
    protected void onStop() {
        super.onStop();
        //结束轮播
        vp_banner.stopAutoPlay();
    }

    @Override
    protected void onResume() {
        super.onResume();
        onRefresh();
    }
}
