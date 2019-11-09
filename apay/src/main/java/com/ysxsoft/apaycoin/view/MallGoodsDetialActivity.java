package com.ysxsoft.apaycoin.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.github.jdsjlzx.ItemDecoration.LuDividerDecoration;
import com.github.jdsjlzx.interfaces.OnNetWorkErrorListener;
import com.github.jdsjlzx.recyclerview.LuRecyclerView;
import com.github.jdsjlzx.recyclerview.LuRecyclerViewAdapter;
import com.ysxsoft.apaycoin.R;
import com.ysxsoft.apaycoin.adapte.MallGoodsDetialAdapter;
import com.ysxsoft.apaycoin.adapte.NewAddGoodsEditorListAdapter;
import com.ysxsoft.apaycoin.banner.Banner;
import com.ysxsoft.apaycoin.banner.GlideImageLoader;
import com.ysxsoft.apaycoin.banner.OnBannerListener;
import com.ysxsoft.apaycoin.com.ActivityPageManager;
import com.ysxsoft.apaycoin.com.BaseActivity;
import com.ysxsoft.apaycoin.impservice.ImpMallGoodsDetialHeaderService;
import com.ysxsoft.apaycoin.impservice.ImpNewAddGoodsEditorListService;
import com.ysxsoft.apaycoin.impservice.ImpOnlineUnderGoodsService;
import com.ysxsoft.apaycoin.modle.MallGoodsDetialHeaderBean;
import com.ysxsoft.apaycoin.modle.NewAddGoodsEditorListBean;
import com.ysxsoft.apaycoin.modle.OnlineUnderGoodsBean;
import com.ysxsoft.apaycoin.utils.AppUtil;
import com.ysxsoft.apaycoin.utils.NetWork;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 描述： 商家的商品详情
 * 日期： 2018/11/26 0026 10:48
 * 作者： 胡
 * 公司：郑州亿生信科技有限公司
 */
public class MallGoodsDetialActivity extends BaseActivity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener, OnBannerListener {

    private String pid;
    private View img_back;
    private TextView tv_title, tv_title_right, tv_content_title, tv_price, tv_stock_num, tv_under_goods;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private LuRecyclerView mRecyclerView = null;
    private LuRecyclerViewAdapter mLuRecyclerViewAdapter = null;
    private int page = 1;
    //    private MallGoodsDetialAdapter mDataAdapter;
    private NewAddGoodsEditorListAdapter mDataAdapter;
    private Banner vp_banner;
    private ArrayList<String> urls = new ArrayList<>();
    private String flag;
    private int Is_flag = -1;
    private PreviewHandler mHandler = new PreviewHandler(this);
    private NewAddGoodsEditorListBean newAddGoodsEditorListBean;
    private String title;
    private String money;
    private String stock;
    private String newGoods;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mall_goods_detial_layout);
        Intent intent = getIntent();
        pid = intent.getStringExtra("pid");
        flag = intent.getStringExtra("flag");
        newGoods = intent.getStringExtra("newGoods");
        initView();
        getHeaderData();
        initListener();
    }

    /**
     * 获取头部数据
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
                        showToastMessage(mallGoodsDetialHeaderBean.getMsg());
                        if ("0".equals(mallGoodsDetialHeaderBean.getCode())) {
                            title = AppUtil.stringReplace(mallGoodsDetialHeaderBean.getData().getTitle());
                            money = mallGoodsDetialHeaderBean.getData().getMoney();
                            pid = mallGoodsDetialHeaderBean.getData().getPid();
                            tv_content_title.setText(AppUtil.stringReplace(mallGoodsDetialHeaderBean.getData().getTitle()));
                            tv_price.setText(mallGoodsDetialHeaderBean.getData().getMoney());
                            stock = mallGoodsDetialHeaderBean.getData().getStock();
                            tv_stock_num.setText("库存：" + mallGoodsDetialHeaderBean.getData().getStock());
                            ArrayList<MallGoodsDetialHeaderBean.DataBean.LunBo> lunbo = mallGoodsDetialHeaderBean.getData().getLunbo();
                            for (int i = 0; i < lunbo.size(); i++) {
                                MallGoodsDetialHeaderBean.DataBean.LunBo lunBo = lunbo.get(i);
                                String icon = lunBo.getIcon();
                                urls.add(icon);
                            }
                            vp_banner.setImages(urls)
                                    .setImageLoader(new GlideImageLoader())
                                    .setOnBannerListener(MallGoodsDetialActivity.this)
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

    private void initView() {
        img_back = getViewById(R.id.img_back);
        tv_title_right = getViewById(R.id.tv_title_right);
        tv_title_right.setVisibility(View.VISIBLE);
        tv_title_right.setText("编辑详情");
        tv_title = getViewById(R.id.tv_title);
        tv_title.setText("商品详情");
        vp_banner = getViewById(R.id.vp_banner);
        tv_content_title = getViewById(R.id.tv_content_title);
        tv_price = getViewById(R.id.tv_price);
        tv_stock_num = getViewById(R.id.tv_stock_num);
        tv_under_goods = getViewById(R.id.tv_under_goods);
        if ("newGoods".equals(newGoods)){
            tv_under_goods.setVisibility(View.GONE);
        }else {
            if ("0".equals(flag)) { //flag  1  上架  0  下架
                tv_under_goods.setText("下架");
                Is_flag = 1;
            } else {
                tv_under_goods.setText("上架");
                Is_flag = 0;
            }
        }
        tv_under_goods.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NetWork.getRetrofit()
                        .create(ImpOnlineUnderGoodsService.class)
                        .getCall(NetWork.getToken(), pid, Is_flag + "")
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<OnlineUnderGoodsBean>() {
                            private OnlineUnderGoodsBean onlineUnderGoodsBean;

                            @Override
                            public void onCompleted() {
                               showToastMessage(onlineUnderGoodsBean.getMsg());
                                if ("0".equals(onlineUnderGoodsBean.getCode())) {
                                    if (Is_flag == 0) {
                                        tv_under_goods.setText("下架");
                                    }else if (Is_flag==1){
                                        tv_under_goods.setText("上架");
                                    }
                                }else if ("2" .equals( onlineUnderGoodsBean.getCode())) {
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
                                Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onNext(OnlineUnderGoodsBean onlineUnderGoodsBean) {
                                this.onlineUnderGoodsBean = onlineUnderGoodsBean;
                            }
                        });
            }
        });

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        mRecyclerView = (LuRecyclerView) findViewById(R.id.list);
        //设置刷新时动画的颜色，可以设置4个
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setProgressViewOffset(false, 0, AppUtil.dip2px(this, 48));
            mSwipeRefreshLayout.setColorSchemeResources(R.color.btn_color);
            mSwipeRefreshLayout.setOnRefreshListener(this);
        }
//        mDataAdapter = new MallGoodsDetialAdapter(this);
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
        onRefresh();
    }

    private void initListener() {
        img_back.setOnClickListener(this);
        tv_title_right.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.tv_title_right:
                Intent intent = new Intent(mContext, EditorNewAddGoodsActivity.class);
                intent.putExtra("pid", pid);
                intent.putExtra("mallName",title);
                intent.putExtra("price",money);
                intent.putExtra("stock",stock);
                startActivity(intent);
                break;
        }
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
        private WeakReference<MallGoodsDetialActivity> ref;

        PreviewHandler(MallGoodsDetialActivity activity) {
            ref = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            final MallGoodsDetialActivity activity = ref.get();
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
                        showToastMessage(newAddGoodsEditorListBean.getMsg());
                        if ("0".equals(newAddGoodsEditorListBean.getCode())) {
                            showData(newAddGoodsEditorListBean);
                            ArrayList<NewAddGoodsEditorListBean.DataBean> data = newAddGoodsEditorListBean.getData();
                            mDataAdapter.addAll(data);
                            if (mSwipeRefreshLayout.isRefreshing()) {
                                mSwipeRefreshLayout.setRefreshing(false);
                            }
                            mRecyclerView.refreshComplete(data.size());
                            notifyDataSetChanged();
                        }else if ("2" .equals(newAddGoodsEditorListBean.getCode())) {
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

    private void addItems(ArrayList<NewAddGoodsEditorListBean.DataBean> data) {
        mDataAdapter.addAll(data);
    }


    @Override
    public void OnBannerClick(int position) {
//        showToastMessage("点击了===>" + position);
    }

    //如果你需要考虑更好的体验，可以这么操作
    @Override
    protected void onStart() {
        super.onStart();
        //开始轮播
        vp_banner.startAutoPlay();
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filter=new IntentFilter("ICON_PATH");
        registerReceiver(broadcastReceiver,filter);

        IntentFilter intentFilter=new IntentFilter("FINISH");
        registerReceiver(broadcastReceiver1,intentFilter);
    }
    private BroadcastReceiver broadcastReceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if ("ICON_PATH".equals(intent.getAction())){
                String icon = intent.getStringExtra("icon_path");
                if (!TextUtils.isEmpty(icon)){
                    urls.clear();
                    getHeaderData();
//                    urls.add(icon);
                }
            }
        }
    };
    @Override
    protected void onStop() {
        super.onStop();
        //结束轮播
        vp_banner.stopAutoPlay();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
        unregisterReceiver(broadcastReceiver1);
    }


    BroadcastReceiver broadcastReceiver1=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if ("FINISH".equals(intent.getAction())) {
                finish();
            }
        }
    };

}
