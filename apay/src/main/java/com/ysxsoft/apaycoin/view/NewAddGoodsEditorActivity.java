package com.ysxsoft.apaycoin.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.jdsjlzx.ItemDecoration.LuDividerDecoration;
import com.github.jdsjlzx.interfaces.OnItemLongClickListener;
import com.github.jdsjlzx.interfaces.OnNetWorkErrorListener;
import com.github.jdsjlzx.recyclerview.LuRecyclerView;
import com.github.jdsjlzx.recyclerview.LuRecyclerViewAdapter;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.ysxsoft.apaycoin.R;
import com.ysxsoft.apaycoin.adapte.NewAddGoodsEditorListAdapter;
import com.ysxsoft.apaycoin.com.ActivityPageManager;
import com.ysxsoft.apaycoin.com.BaseActivity;
import com.ysxsoft.apaycoin.impservice.ImpAddImageService;
import com.ysxsoft.apaycoin.impservice.ImpAddTextService;
import com.ysxsoft.apaycoin.impservice.ImpGoodsDetialDeleteService;
import com.ysxsoft.apaycoin.impservice.ImpNewAddGoodsEditorListService;
import com.ysxsoft.apaycoin.modle.AddTextOrImageBean;
import com.ysxsoft.apaycoin.modle.GoodsDetialDeleteBean;
import com.ysxsoft.apaycoin.modle.NewAddGoodsEditorListBean;
import com.ysxsoft.apaycoin.utils.AppUtil;
import com.ysxsoft.apaycoin.utils.CustomDialog;
import com.ysxsoft.apaycoin.utils.NetWork;
import com.ysxsoft.apaycoin.widget.LongDialog;
import com.ysxsoft.apaycoin.widget.TextEditorDialog;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Observer;
import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 描述：新增商品编辑界面
 * 日期： 2018/11/24 0024 16:19
 * 作者： 胡
 * 公司：郑州亿生信科技有限公司
 */
public class NewAddGoodsEditorActivity extends BaseActivity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    private View img_back;
    private TextView tv_title, tv_title_right;
    private LinearLayout ll_add_text, ll_add_img;
    private List<LocalMedia> localMedia;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private LuRecyclerView mRecyclerView = null;
    private LuRecyclerViewAdapter mLuRecyclerViewAdapter = null;
    private String pid;
    private NewAddGoodsEditorListAdapter mDataAdapter;
    private NewAddGoodsEditorListBean newAddGoodsEditorListBean;
    private PreviewHandler mHandler = new PreviewHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.business_new_add_goods_edittext_layout);
        Intent intent = getIntent();
        pid = intent.getStringExtra("pid");
        initView();
        initListener();
    }

    private void initView() {
        img_back = getViewById(R.id.img_back);
        tv_title = getViewById(R.id.tv_title);
        tv_title_right = getViewById(R.id.tv_title_right);
        tv_title.setText("新增商品");
        tv_title_right.setVisibility(View.VISIBLE);
        tv_title_right.setText("保存");
        ll_add_text = getViewById(R.id.ll_add_text);
        ll_add_img = getViewById(R.id.ll_add_img);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        mRecyclerView = (LuRecyclerView) findViewById(R.id.list);
        //设置刷新时动画的颜色，可以设置4个
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setProgressViewOffset(false, 0, AppUtil.dip2px(this, 48));
            mSwipeRefreshLayout.setColorSchemeResources(R.color.btn_color);
            mSwipeRefreshLayout.setOnRefreshListener(this);
        }
        mDataAdapter = new NewAddGoodsEditorListAdapter(this);
        //setLayoutManager放在setAdapter之前配置
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
        mLuRecyclerViewAdapter.setOnItemLongClickListener(new OnItemLongClickListener() {
            @Override
            public void onItemLongClick(View view, int position) {
                NewAddGoodsEditorListBean.DataBean dataBean = newAddGoodsEditorListBean.getData().get(position);
                final String iid = dataBean.getIid();
                final LongDialog dialog = new LongDialog(mContext);
                TextView tv_delete = dialog.findViewById(R.id.tv_delete);
                tv_delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DeleteData(iid, dialog);
                    }
                });
                dialog.show();
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
     * @param iid
     * @param dialog
     */
    private void DeleteData(String iid, final LongDialog dialog) {
        NetWork.getRetrofit()
                .create(ImpGoodsDetialDeleteService.class)
                .getCall(NetWork.getToken(), iid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<GoodsDetialDeleteBean>() {
                    private GoodsDetialDeleteBean goodsDetialDeleteBean;

                    @Override
                    public void onCompleted() {
                        showToastMessage(goodsDetialDeleteBean.getMsg());
                        if ("0".equals(goodsDetialDeleteBean.getCode())) {
                            onRefresh();
                            dialog.dismiss();
                        }else if ("2" == goodsDetialDeleteBean.getCode()) {
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
                    public void onNext(GoodsDetialDeleteBean goodsDetialDeleteBean) {
                        this.goodsDetialDeleteBean = goodsDetialDeleteBean;
                    }
                });
    }

    private void initListener() {
        img_back.setOnClickListener(this);
        tv_title_right.setOnClickListener(this);
        ll_add_text.setOnClickListener(this);
        ll_add_img.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;

            case R.id.tv_title_right:
                if (newAddGoodsEditorListBean.getData().size() > 1) {
                    Intent intent=new Intent(mContext,NewAddGoodsSaveActivity.class);
                    intent.putExtra("pid",pid);
                    startActivity(intent);
                } else {
                    showToastMessage("请添加商品描述");
                }
                break;

            case R.id.ll_add_text:
                addTextData();
                break;

            case R.id.ll_add_img:
                openGallery();
                break;

        }
    }

    /**
     * 添加文字
     */
    private void addTextData() {
        final TextEditorDialog dialog = new TextEditorDialog(mContext);
        TextView tv_save = dialog.findViewById(R.id.tv_save);
        final EditText ed_input_content = dialog.findViewById(R.id.ed_input_content);
        tv_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pid == null) {
                    return;
                }
                NetWork.getRetrofit()
                        .create(ImpAddTextService.class)
                        .getCall(NetWork.getToken(), pid, "2", ed_input_content.getText().toString().trim())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<AddTextOrImageBean>() {
                            private AddTextOrImageBean addTextOrImageBean;

                            @Override
                            public void onCompleted() {
                                showToastMessage(addTextOrImageBean.getMsg());
                                if ("0".equals(addTextOrImageBean.getCode())) {
                                    onRefresh();
                                    dialog.dismiss();
                                }else if ("2".equals(addTextOrImageBean.getCode())) {
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
                            public void onNext(AddTextOrImageBean addTextOrImageBean) {
                                this.addTextOrImageBean = addTextOrImageBean;
                            }
                        });
            }
        });
        dialog.show();
    }

    /**
     * 打开相册
     */
    private void openGallery() {
        PictureSelector.create(NewAddGoodsEditorActivity.this)
                .openGallery(PictureMimeType.ofAll())
                .maxSelectNum(1)// 最大图片选择数量
                .selectionMode(PictureConfig.SINGLE)
                .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                .imageFormat(PictureMimeType.PNG)// 拍照保存图片格式后缀,默认jpeg
                .enableCrop(false)// 是否裁剪
                .compress(false)// 是否压缩
//                .sizeMultiplier(0.5f)// glide 加载图片大小 0~1之间 如设置 .glideOverride()无效
                .synOrAsy(true)//同步true或异步false 压缩 默认同步
                .glideOverride(160, 160)// glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
                .withAspectRatio(3, 4)// 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                .freeStyleCropEnabled(true)// 裁剪框是否可拖拽
                .minimumCompressSize(100)// 小于100kb的图片不压缩
                .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    // 图片选择结果回调
                    localMedia = PictureSelector.obtainMultipleResult(data);
                    File file = new File(localMedia.get(0).getPath());
                    RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                    MultipartBody.Part body = MultipartBody.Part.createFormData("icon", file.getName(), requestBody);
                    final CustomDialog customDialog=new CustomDialog(mContext,"正在提交数据...");
                    customDialog.show();
                    NetWork.getRetrofit()
                            .create(ImpAddImageService.class)
                            .getCall(NetWork.getToken(), pid, "1", body)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Observer<AddTextOrImageBean>() {
                                private AddTextOrImageBean addTextOrImageBean;

                                @Override
                                public void onCompleted() {
                                    showToastMessage(addTextOrImageBean.getMsg());
                                    if ("0".equals(addTextOrImageBean.getCode())) {
                                        customDialog.dismiss();
                                        onRefresh();
                                    }else if ("2".equals(addTextOrImageBean.getCode())){
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
                                    customDialog.dismiss();
                                }

                                @Override
                                public void onNext(AddTextOrImageBean addTextOrImageBean) {
                                    this.addTextOrImageBean = addTextOrImageBean;
                                }
                            });

                    break;
            }
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
        private WeakReference<NewAddGoodsEditorActivity> ref;

        PreviewHandler(NewAddGoodsEditorActivity activity) {
            ref = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            final NewAddGoodsEditorActivity activity = ref.get();
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
                            addItems(data);
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

    private void addItems(ArrayList<NewAddGoodsEditorListBean.DataBean> data) {
        mDataAdapter.addAll(data);
    }
    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter intentFilter=new IntentFilter("FINISH");
        registerReceiver(broadcastReceiver,intentFilter);
    }

    BroadcastReceiver broadcastReceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if ("FINISH".equals(intent.getAction())) {
                finish();
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }
}
