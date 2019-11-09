package com.ysxsoft.apaycoin.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnDismissListener;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.github.jdsjlzx.ItemDecoration.LuDividerDecoration;
import com.github.jdsjlzx.interfaces.OnItemClickListener;
import com.github.jdsjlzx.interfaces.OnItemLongClickListener;
import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.interfaces.OnNetWorkErrorListener;
import com.github.jdsjlzx.recyclerview.LuRecyclerView;
import com.github.jdsjlzx.recyclerview.LuRecyclerViewAdapter;
import com.ysxsoft.apaycoin.R;
import com.ysxsoft.apaycoin.adapte.GoodsListAdapter;
import com.ysxsoft.apaycoin.com.ActivityPageManager;
import com.ysxsoft.apaycoin.com.BaseActivity;
import com.ysxsoft.apaycoin.impservice.ImpDeleteGoodsService;
import com.ysxsoft.apaycoin.impservice.ImpGoodsListService;
import com.ysxsoft.apaycoin.impservice.ImpGoodsTypeService;
import com.ysxsoft.apaycoin.impservice.ImpModifyMallTypeService;
import com.ysxsoft.apaycoin.impservice.ImpMyMallHeaderInfoService;
import com.ysxsoft.apaycoin.impservice.ImpOnlineUnderGoodsService;
import com.ysxsoft.apaycoin.modle.DeleteGoodsBean;
import com.ysxsoft.apaycoin.modle.GoodsListBean;
import com.ysxsoft.apaycoin.modle.GoodsTypeBean;
import com.ysxsoft.apaycoin.modle.ModifyMallTypeBean;
import com.ysxsoft.apaycoin.modle.MyMallHeaderInfoBean;
import com.ysxsoft.apaycoin.modle.OnlineUnderGoodsBean;
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

/**
 * 描述： 我的店铺
 * 日期： 2018/11/6 0006 17:56
 * 作者： 胡
 * 公司：郑州亿生信科技有限公司
 */
public class LookMyMallActivity extends BaseActivity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    private View img_back;
    private TextView tv_title, tv_title_right, tv_shop_name, tv_phone_num, tv_balance_money, tv_ticket_num, tv_new_add_goods, tv_goods_type;
    private LinearLayout ll_credit_star, ll_no_credit_star,ll_income, ll_no_goods_tip,ll_new;
    private ImageView img_down_arrow;
    private CircleImageView img_head;
    private String star;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private LuRecyclerView mRecyclerView = null;
    private LuRecyclerViewAdapter mLuRecyclerViewAdapter = null;
    private GoodsListAdapter mDataAdapter;
    private int page = 1;
    private PreviewHandler mHandler = new PreviewHandler(this);
    private GoodsListBean goodsListBean;
    private int Is_flag = -1;
    ArrayList<String> options1Items = new ArrayList<>();
    private ArrayList<GoodsTypeBean.DataBean> data;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.business_my_shop_detail_layout);
        initView();
//        getData();
        getGoodsTypeData();
        requestHeaderData();
        initListener();
    }

    private void initView() {
        img_back = getViewById(R.id.img_back);
        tv_title = getViewById(R.id.tv_title);
        tv_title.setText("我的店铺");
        tv_title_right = getViewById(R.id.tv_title_right);
        tv_title_right.setVisibility(View.VISIBLE);
        tv_title_right.setText("商家订单");
        img_down_arrow = getViewById(R.id.img_down_arrow);
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

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        mRecyclerView = (LuRecyclerView) findViewById(R.id.list);
        //设置刷新时动画的颜色，可以设置4个
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setProgressViewOffset(false, 0, AppUtil.dip2px(this, 48));
            mSwipeRefreshLayout.setColorSchemeResources(R.color.btn_color);
            mSwipeRefreshLayout.setOnRefreshListener(this);
        }
        mDataAdapter = new GoodsListAdapter(this);
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
        mLuRecyclerViewAdapter.addHeaderView(new MallGoodsDetialHeader(this));
        mDataAdapter.setOnUnderOnClickListener(new GoodsListAdapter.OnUnderOnClickListener() {
            @Override
            public void UnderOnClick(int position) {
                GoodsListBean.DataBean dataBean = mDataAdapter.getDataList().get(position);
                String flag = dataBean.getFlag();
                String pid = dataBean.getPid();
                if ("0".equals(flag)) { //flag  1  上架  0  下架
                    Is_flag = 1;
                } else {
                    Is_flag = 0;
                }
                submitData(pid, Is_flag);
            }
        });
        mLuRecyclerViewAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                GoodsListBean.DataBean dataBean = mDataAdapter.getDataList().get(position);
                String pid = dataBean.getPid();
                String flag = dataBean.getFlag();
                Intent intent = new Intent(mContext, MallGoodsDetialActivity.class);
                intent.putExtra("pid", pid);
                intent.putExtra("flag", flag);
                startActivity(intent);
            }
        });
        mLuRecyclerViewAdapter.setOnItemLongClickListener(new OnItemLongClickListener() {
            @Override
            public void onItemLongClick(View view, int position) {
                List<GoodsListBean.DataBean> dataList = mDataAdapter.getDataList();
                final String pid = dataList.get(position).getPid();
                final LongDialog dialog = new LongDialog(mContext);
                TextView tv_delete = dialog.findViewById(R.id.tv_delete);
                tv_delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deleteData(pid, dialog);
                    }
                });
                dialog.show();
            }
        });
        mRecyclerView.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (goodsListBean != null) {
                    String last_page = goodsListBean.getLast_page();
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
     * shagnjia  huo xiajia
     *
     * @param pid
     * @param is_flag
     */
    private void submitData(String pid, int is_flag) {
        NetWork.getRetrofit()
                .create(ImpOnlineUnderGoodsService.class)
                .getCall(NetWork.getToken(), pid, is_flag + "")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<OnlineUnderGoodsBean>() {
                    private OnlineUnderGoodsBean onlineUnderGoodsBean;

                    @Override
                    public void onCompleted() {
                        showToastMessage(onlineUnderGoodsBean.getMsg());
                        if ("0".equals(onlineUnderGoodsBean.getCode())) {
                            onRefresh();
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

    /**
     * 删除商品
     *
     * @param pid
     * @param dialog
     */
    private void deleteData(String pid, final LongDialog dialog) {
        NetWork.getRetrofit()
                .create(ImpDeleteGoodsService.class)
                .getCall(NetWork.getToken(), pid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<DeleteGoodsBean>() {
                    private DeleteGoodsBean deleteGoodsBean;

                    @Override
                    public void onCompleted() {
                        showToastMessage(deleteGoodsBean.getMsg());
                        if ("0".equals(deleteGoodsBean.getCode())) {
                            onRefresh();
                            dialog.dismiss();
                        }else  if ("2".equals(deleteGoodsBean.getCode())) {
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
                    public void onNext(DeleteGoodsBean deleteGoodsBean) {
                        this.deleteGoodsBean = deleteGoodsBean;
                    }
                });

    }

    /**
     * 获取我的店铺头部信息
     */
    private void requestHeaderData() {
        NetWork.getRetrofit()
                .create(ImpMyMallHeaderInfoService.class)
                .getCall(NetWork.getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<MyMallHeaderInfoBean>() {
                    private MyMallHeaderInfoBean myMallHeaderInfoBean;
                    @Override
                    public void onCompleted() {
//                        showToastMessage(myMallHeaderInfoBean.getMsg());
                        if ("0".equals(myMallHeaderInfoBean.getCode())) {
                            MyMallHeaderInfoBean.DataBean data = myMallHeaderInfoBean.getData();
                            star = data.getStar();
                            ImageLoadUtil.GlideImageLoad(mContext, data.getIcon(), img_head);
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
                        }else if ("2" .equals( myMallHeaderInfoBean.getCode())) {
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
                    public void onNext(MyMallHeaderInfoBean myMallHeaderInfoBean) {
                        this.myMallHeaderInfoBean = myMallHeaderInfoBean;
                    }
                });

    }

    private void initListener() {
        img_back.setOnClickListener(this);
        img_down_arrow.setOnClickListener(this);
        tv_title_right.setOnClickListener(this);
        tv_new_add_goods.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.img_down_arrow:
                AppUtil.colsePhoneKeyboard(this);
                showGoodsType();
                break;
            case R.id.tv_title_right:
                startActivity(MerchantOrderActivity.class);
                break;
            case R.id.ll_new:
            case R.id.tv_new_add_goods:
                startActivity(NewAddGoodsActivity.class);
                break;
        }

    }

    /**
     * 展示商品分类的弹窗
     */
    private void showGoodsType() {
        OptionsPickerView pvOptions = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                String tx = data.get(options1).getTitle();
                id = data.get(options1).getId();
                tv_goods_type.setText(tx);
            }
        })
                .setTitleText("选择商品")
                .setDividerColor(Color.BLACK)
                .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
                .setContentTextSize(20)
                .build();
        /*pvOptions.setPicker(options1Items);//一级选择器
        pvOptions.setPicker(options1Items, options2Items);//二级选择器*/
        pvOptions.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(Object o) {
                if (TextUtils.isEmpty(id)||id==null){
                    return;
                }
                ModifyGoodsType(id);
            }
        });
        pvOptions.setPicker(options1Items);//三级选择器
        pvOptions.show();
    }

    /**
     * 修改店铺分类
     *
     * @param id
     */
    private void ModifyGoodsType(String id) {
        NetWork.getRetrofit()
                .create(ImpModifyMallTypeService.class)
                .getCall(NetWork.getToken(), id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ModifyMallTypeBean>() {
                    private ModifyMallTypeBean modifyMallTypeBean;

                    @Override
                    public void onCompleted() {
                        showToastMessage(modifyMallTypeBean.getMsg());
                        if ("2" .equals( modifyMallTypeBean.getCode())) {
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
                    public void onNext(ModifyMallTypeBean modifyMallTypeBean) {
                        this.modifyMallTypeBean = modifyMallTypeBean;
                    }
                });
    }

    private void getGoodsTypeData() {
        NetWork.getRetrofit()
                .create(ImpGoodsTypeService.class)
                .getCall(NetWork.getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<GoodsTypeBean>() {
                    private GoodsTypeBean goodsTypeBean;

                    @Override
                    public void onCompleted() {
//                        showToastMessage(goodsTypeBean.getMsg());
                        if ("0".equals(goodsTypeBean.getCode())) {
                            data = goodsTypeBean.getData();
                            for (int i = 0; i < data.size(); i++) {
                                options1Items.add(data.get(i).getTitle());
                            }
                        }else if ("2" == goodsTypeBean.getCode()) {
                            SharedPreferences.Editor sp = getSharedPreferences("save_name_pwd", Context.MODE_PRIVATE).edit();
                            sp.clear();
                            sp.commit();
                            SharedPreferences.Editor is_first = getSharedPreferences("IS_FIRST", Context.MODE_PRIVATE).edit();
                            is_first.clear();
                            is_first.commit();
                            ActivityPageManager   instance = ActivityPageManager.getInstance();
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
                    public void onNext(GoodsTypeBean goodsTypeBean) {
                        this.goodsTypeBean = goodsTypeBean;
                    }
                });

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
        private WeakReference<LookMyMallActivity> ref;
        PreviewHandler(LookMyMallActivity activity) {
            ref = new WeakReference<>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            final LookMyMallActivity activity = ref.get();
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
                .create(ImpGoodsListService.class)
                .getCall(NetWork.getToken(), page + "")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<GoodsListBean>() {
                    private GoodsListBean goodsListBean;
                    @Override
                    public void onCompleted() {
//                        showToastMessage(goodsListBean.getMsg());
                        if ("0".equals(goodsListBean.getCode())) {
                            showData(goodsListBean);
                            ArrayList<GoodsListBean.DataBean> data = goodsListBean.getData();
                            addItems(goodsListBean.getData());
                            if (data.size() <= 0) {
                                ll_income.setVisibility(View.GONE);
                                ll_no_goods_tip.setVisibility(View.VISIBLE);
                                mSwipeRefreshLayout.setVisibility(View.GONE);
                            } else {
                                ll_income.setVisibility(View.VISIBLE);
                                ll_no_goods_tip.setVisibility(View.GONE);
                                mSwipeRefreshLayout.setVisibility(View.VISIBLE);
                            }

                            if (mSwipeRefreshLayout.isRefreshing()) {
                                mSwipeRefreshLayout.setRefreshing(false);
                            }
                            mRecyclerView.refreshComplete(data.size());//一页加载的数量
                            notifyDataSetChanged();
                        }else if ("2" == goodsListBean.getCode()) {
                            SharedPreferences.Editor sp = getSharedPreferences("save_name_pwd", Context.MODE_PRIVATE).edit();
                            sp.clear();
                            sp.commit();
                            SharedPreferences.Editor is_first = getSharedPreferences("IS_FIRST", Context.MODE_PRIVATE).edit();
                            is_first.clear();
                            is_first.commit();
                            ActivityPageManager   instance = ActivityPageManager.getInstance();
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
                    public void onNext(GoodsListBean goodsListBean) {
                        this.goodsListBean = goodsListBean;
                    }
                });
    }

    private void showData(GoodsListBean goodsListBean) {
        this.goodsListBean = goodsListBean;
    }

    private void notifyDataSetChanged() {
        mLuRecyclerViewAdapter.notifyDataSetChanged();
    }

    private void addItems(ArrayList<GoodsListBean.DataBean> data) {
        mDataAdapter.addAll(data);
    }

    @Override
    protected void onResume() {
        super.onResume();
        onRefresh();
    }
}
