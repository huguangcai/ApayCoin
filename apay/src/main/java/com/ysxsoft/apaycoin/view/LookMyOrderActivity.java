package com.ysxsoft.apaycoin.view;

import android.Manifest;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.permissions.RxPermissions;
import com.luck.picture.lib.tools.PictureFileUtils;
import com.ysxsoft.apaycoin.R;
import com.ysxsoft.apaycoin.adapte.GridImageAdapter;
import com.ysxsoft.apaycoin.com.ActivityPageManager;
import com.ysxsoft.apaycoin.com.BaseActivity;
import com.ysxsoft.apaycoin.com.FullyGridLayoutManager;
import com.ysxsoft.apaycoin.impservice.ImpCheckGatheringMoneyservice;
import com.ysxsoft.apaycoin.impservice.ImpGoMakeMoneyService;
import com.ysxsoft.apaycoin.impservice.ImpLookOrderService;
import com.ysxsoft.apaycoin.modle.CheckGatheringMoneyBean;
import com.ysxsoft.apaycoin.modle.GoMakeMoneyBean;
import com.ysxsoft.apaycoin.modle.LookOrderBean;
import com.ysxsoft.apaycoin.utils.ImageLoadUtil;
import com.ysxsoft.apaycoin.utils.NetWork;
import com.ysxsoft.apaycoin.widget.paypwdpopuwindow.popwindow.SelectPopupWindow;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 描述： 去打款   查看界面共用
 * 日期： 2018/11/19 0019 14:57
 * 作者： 胡
 * 公司：郑州亿生信科技有限公司
 */
public class LookMyOrderActivity extends BaseActivity implements View.OnClickListener, SelectPopupWindow.OnPopWindowClickListener {

    private String type;
    private String oid;
    private View img_back;
    private TextView tv_title, tv_phone_num, tv_name, tv_khh, tv_bank_card_num, tv_zhifubao, tv_proof_tip, tv_upload_proof;
    private Button btn_submit;
    private ImageView img_copy, img_phone, img_upload_proof;
    private RecyclerView recyclerview;
    private List<LocalMedia> selectList = new ArrayList<>();
    private GridImageAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.go_make_money_layout);
        Intent intent = getIntent();
        type = intent.getStringExtra("type");
        oid = intent.getStringExtra("oid");
        initView();
        initListener();
        initData();
        requestData();
    }

    private void initData() {
        if ("look".equals(type)) {//销售中查看
            tv_title.setText("确认收款");
            btn_submit.setText("确认收款");
            img_copy.setVisibility(View.GONE);
            img_phone.setVisibility(View.GONE);
            tv_upload_proof.setText("对方打款凭证");
            tv_proof_tip.setVisibility(View.GONE);
            recyclerview.setVisibility(View.GONE);
            img_upload_proof.setVisibility(View.VISIBLE);
        } else if ("gomakemoney".equals(type)){//购买中的去打款
            tv_title.setText("打款");
            btn_submit.setText("确定提交");
            img_copy.setVisibility(View.VISIBLE);
            img_phone.setVisibility(View.VISIBLE);
            tv_upload_proof.setText("上传打款凭证");
            tv_proof_tip.setVisibility(View.VISIBLE);
            recyclerview.setVisibility(View.VISIBLE);
            img_upload_proof.setVisibility(View.GONE);
        }
    }

    /**
     * 确认收款  获取数据
     */
    private void requestData() {
        NetWork.getRetrofit()
                .create(ImpLookOrderService.class)
                .getCall(NetWork.getToken(), oid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<LookOrderBean>() {
                    private LookOrderBean lookOrderBean;

                    @Override
                    public void onCompleted() {
                        showToastMessage(lookOrderBean.getMsg());
                        if ("0".equals(lookOrderBean.getCode())) {
                            tv_name.setText(lookOrderBean.getData().getKhr());
                            tv_khh.setText(lookOrderBean.getData().getKhh());
                            tv_phone_num.setText(lookOrderBean.getData().getMobile());
                            tv_zhifubao.setText(lookOrderBean.getData().getAlipay());
                            tv_bank_card_num.setText(lookOrderBean.getData().getCarnum());
                            ImageLoadUtil.GlideImageLoad(mContext, lookOrderBean.getData().getPic(), img_upload_proof);
                        }else if ("2" == lookOrderBean.getCode()) {
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
                    public void onNext(LookOrderBean lookOrderBean) {
                        this.lookOrderBean = lookOrderBean;
                    }
                });

    }

    private void initView() {
        img_back = getViewById(R.id.img_back);
        tv_title = getViewById(R.id.tv_title);
        tv_name = getViewById(R.id.tv_name);
        tv_khh = getViewById(R.id.tv_khh);
        tv_zhifubao = getViewById(R.id.tv_zhifubao);
        tv_bank_card_num = getViewById(R.id.tv_bank_card_num);
        tv_phone_num = getViewById(R.id.tv_phone_num);
        tv_upload_proof = getViewById(R.id.tv_upload_proof);
        tv_proof_tip = getViewById(R.id.tv_proof_tip);
        btn_submit = getViewById(R.id.btn_submit);
        img_copy = getViewById(R.id.img_copy);
        img_phone = getViewById(R.id.img_phone);
        img_upload_proof = getViewById(R.id.img_upload_proof);
        recyclerview = getViewById(R.id.rv_recyclerview);
        FullyGridLayoutManager manager = new FullyGridLayoutManager(mContext, 1, GridLayoutManager.VERTICAL, false);
        recyclerview.setLayoutManager(manager);
        adapter = new GridImageAdapter(mContext, onAddPicClickListener);
        adapter.setSelectMax(1);
        recyclerview.setAdapter(adapter);
    }

    private GridImageAdapter.onAddPicClickListener onAddPicClickListener = new GridImageAdapter.onAddPicClickListener() {

        @Override
        public void onAddPicClick() {
            PictureSelector.create(LookMyOrderActivity.this)
                    .openGallery(PictureMimeType.ofAll())// 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                    .maxSelectNum(1)// 最大图片选择数量
                    .minSelectNum(1)// 最小选择数量
                    .imageSpanCount(3)// 每行显示个数
                    .selectionMode(PictureConfig.SINGLE)// 多选 or 单选
                    .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                    .imageFormat(PictureMimeType.PNG)// 拍照保存图片格式后缀,默认jpeg
                    .compress(true)// 是否压缩
                    .synOrAsy(true)//同步true或异步false 压缩 默认同步
                    .glideOverride(160, 160)// glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
                    .withAspectRatio(16, 9)// 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                    .freeStyleCropEnabled(true)// 裁剪框是否可拖拽
                    .selectionMedia(selectList)// 是否传入已选图片
                    .minimumCompressSize(100)// 小于100kb的图片不压缩
                    .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code
        }
    };

    private void initListener() {
        img_back.setOnClickListener(this);
        btn_submit.setOnClickListener(this);
        img_copy.setOnClickListener(this);
        img_phone.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.img_copy:
                ClipboardManager cmb = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
                cmb.setText(tv_bank_card_num.getText().toString());//将要复制的下载地址放入settext函数中
                showToastMessage("复制完成");
                break;
            case R.id.img_phone:
                showToastMessage("打电话");
                Intent intent = new Intent(Intent.ACTION_DIAL);
                Uri data = Uri.parse("tel:" + tv_phone_num.getText().toString());
                intent.setData(data);
                startActivity(intent);
                break;
            case R.id.btn_submit:
                if ("look".equals(type)) {//销售中查看
                    showPayDialog();
                } else if ("gomakemoney".equals(type)){//购买中的去打款
                    goMakeMoney();
                }
                break;
        }
    }

    //去打款
    private void goMakeMoney() {
        if (selectList.size()<=0){
            showToastMessage("打款凭证不能为空");
            return;
        }
        String path = selectList.get(0).getPath();
        File file = new File(path);
        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("pic", file.getName(), requestBody);
        NetWork.getRetrofit()
                .create(ImpGoMakeMoneyService.class)
                .getCall(NetWork.getToken(), oid, body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<GoMakeMoneyBean>() {
                    private GoMakeMoneyBean goMakeMoneyBean;

                    @Override
                    public void onCompleted() {
                        showToastMessage(goMakeMoneyBean.getMsg());
                        if ("0".equals(goMakeMoneyBean.getCode())) {
                            ClearCache();
                            finish();
                        }else if ("2" == goMakeMoneyBean.getCode()) {
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
                    public void onNext(GoMakeMoneyBean goMakeMoneyBean) {
                        this.goMakeMoneyBean = goMakeMoneyBean;
                    }
                });

    }

    private void ClearCache() {
//        清空图片缓存，包括裁剪、压缩后的图片 注意:必须要在上传完成后调用 必须要获取权限
        RxPermissions permissions = new RxPermissions(this);
        permissions.
                request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(new io.reactivex.Observer<Boolean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        if (aBoolean) {
                            PictureFileUtils.deleteCacheDirFile(mContext);
                        } else {
                            Toast.makeText(mContext, getString(R.string.picture_jurisdiction), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    private void showPayDialog() {
        SelectPopupWindow menuWindow = new SelectPopupWindow(this, this);
        Rect rect = new Rect();
        getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        int winHeight = getWindow().getDecorView().getHeight();
        menuWindow.showAtLocation(getWindow().getDecorView(), Gravity.BOTTOM, 0, winHeight - rect.bottom);
    }

    @Override
    public void onPopWindowClickListener(String psw, boolean complete) {
        if (complete) {
            submitdata(psw);
        }
    }

    private void submitdata(String psw) {
        NetWork.getRetrofit()
                .create(ImpCheckGatheringMoneyservice.class)
                .getCall(NetWork.getToken(), oid, psw)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CheckGatheringMoneyBean>() {
                    private CheckGatheringMoneyBean checkGatheringMoneyBean;

                    @Override
                    public void onCompleted() {
                        showToastMessage(checkGatheringMoneyBean.getMsg());
                        if ("0".equals(checkGatheringMoneyBean.getCode())){
                            finish();
                        }else if ("2".equals(checkGatheringMoneyBean.getCode())) {
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
                    public void onNext(CheckGatheringMoneyBean checkGatheringMoneyBean) {

                        this.checkGatheringMoneyBean = checkGatheringMoneyBean;
                    }
                });
    }

    @Override
    public void onForgetPayPwd() {
        startActivity(ForgetPayPwdActivity.class);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    // 图片选择结果回调
                    selectList = PictureSelector.obtainMultipleResult(data);
                    // 例如 LocalMedia 里面返回三种path
                    // 1.media.getPath(); 为原图path
                    // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true
                    // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true
                    // 如果裁剪并压缩了，已取压缩路径为准，因为是先裁剪后压缩的
                    for (LocalMedia media : selectList) {
                        Log.i("图片-----》", media.getPath());
                    }
                    adapter.setList(selectList);
                    adapter.notifyDataSetChanged();
                    break;
            }
        }
    }
}
