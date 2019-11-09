package com.ysxsoft.apaycoin.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ysxsoft.apaycoin.R;
import com.ysxsoft.apaycoin.adapte.OnLineMallFragmentAdapter;
import com.ysxsoft.apaycoin.com.ActivityPageManager;
import com.ysxsoft.apaycoin.com.BaseActivity;
import com.ysxsoft.apaycoin.fragment.OnlineMallFragment;
import com.ysxsoft.apaycoin.impservice.ImpGoodsTypeService;
import com.ysxsoft.apaycoin.modle.GoodsTypeBean;
import com.ysxsoft.apaycoin.modle.OnlineMallGoodsListBean;
import com.ysxsoft.apaycoin.utils.NetWork;
import com.ysxsoft.apaycoin.widget.OnTabSelectListener;
import com.ysxsoft.apaycoin.widget.SlidingTabLayout;
import com.ysxsoft.apaycoin.widget.ViewFindUtils;
import com.ysxsoft.apaycoin.widget.ViewPagerSlide;

import java.util.ArrayList;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 描述： 线上商城
 * 日期： 2018/11/5 0005 10:50
 * 作者： 胡
 * 公司：郑州亿生信科技有限公司
 */
public class OnLineMallActivity extends BaseActivity implements View.OnClickListener {

    private View img_back;
    private TextView tv_title;
    private RelativeLayout rl_search;
    private SlidingTabLayout stl_tab;
    private View decorView;
    private SlidingTabLayout tabLayout_9;
    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private ViewPagerSlide vp_content;
    private ArrayList<GoodsTypeBean.DataBean> data;
    private ArrayList<OnlineMallGoodsListBean.DataBean> contentdata;
    private FrameLayout fl_content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.online_mall_layout);
        decorView = getWindow().getDecorView();
        initView();
        getHeaderData();
        initLiseterer();
    }

    private void initView() {
        img_back = getViewById(R.id.img_back);
        rl_search = getViewById(R.id.rl_search);
        stl_tab = getViewById(R.id.stl_tab);
        vp_content = ViewFindUtils.find(decorView, R.id.vp_content);
        tabLayout_9 = ViewFindUtils.find(decorView, R.id.stl_tab);
        fl_content = getViewById(R.id.fl_content);
        tabLayout_9.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                GoodsTypeBean.DataBean dataBean = data.get(position);
                String cid = dataBean.getId();
                OnlineMallFragment fragment = (OnlineMallFragment) mFragments.get(position);
                fragment.setPosition(cid);
            }

            @Override
            public void onTabReselect(int position) {

            }
        });
    }

    /**
     * 获取头部信息
     */
    private void getHeaderData() {
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
                            final ArrayList<GoodsTypeBean.DataBean> data = goodsTypeBean.getData();
                            GoodsTypeBean.DataBean dataBean = new GoodsTypeBean.DataBean();
                            dataBean.setTitle("全部");
                            dataBean.setId("");
                            data.add(0, dataBean);
                            showHeaderData(data);
                            for (int i = 0; i < data.size(); i++) {
                                mFragments.add(OnlineMallFragment.getInstance(dataBean.getId()));
                            }
                            OnLineMallFragmentAdapter adapter = new OnLineMallFragmentAdapter(getSupportFragmentManager(), data, mFragments);
                            vp_content.setAdapter(adapter);
                            tabLayout_9.setViewPager(vp_content);
                            tabLayout_9.setCurrentTab(0);
                        }else if ("2" == goodsTypeBean.getCode()) {
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
                    public void onNext(GoodsTypeBean goodsTypeBean) {
                        this.goodsTypeBean = goodsTypeBean;
                    }
                });


    }

    /**
     * 获取内容数据
     *
     * @param contentdata
     */
    private void showContentData(ArrayList<OnlineMallGoodsListBean.DataBean> contentdata) {
        this.contentdata = contentdata;
    }

    private void showHeaderData(ArrayList<GoodsTypeBean.DataBean> data) {
        this.data = data;
    }

    private void initLiseterer() {
        img_back.setOnClickListener(this);
        EditText ed_title_search = getViewById(R.id.ed_title_search);
        rl_search.setOnClickListener(this);
        ed_title_search.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.ed_title_search:
            case R.id.rl_search:
                startActivity(SearchDataActivity.class);
                break;
        }
    }

}
