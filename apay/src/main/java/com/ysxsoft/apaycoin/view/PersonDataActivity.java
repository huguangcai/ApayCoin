package com.ysxsoft.apaycoin.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.ysxsoft.apaycoin.R;
import com.ysxsoft.apaycoin.com.ActivityPageManager;
import com.ysxsoft.apaycoin.com.BaseActivity;
import com.ysxsoft.apaycoin.com.BaseApplication;
import com.ysxsoft.apaycoin.impservice.ImpModifyNikeNameService;
import com.ysxsoft.apaycoin.impservice.ImpModifyPersonDataService;
import com.ysxsoft.apaycoin.modle.LoginBean;
import com.ysxsoft.apaycoin.modle.ModifyLoginPwdBean;
import com.ysxsoft.apaycoin.utils.FileUtils;
import com.ysxsoft.apaycoin.utils.ImageLoadUtil;
import com.ysxsoft.apaycoin.utils.NetWork;
import com.ysxsoft.apaycoin.widget.CircleImageView;
import com.ysxsoft.apaycoin.widget.ModifyNikeNameDialog;

import java.io.File;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 描述： 个人资料界面
 * 日期： 2018/11/6 0006 11:57
 * 作者： 胡
 * 公司：郑州亿生信科技有限公司
 */
public class PersonDataActivity extends BaseActivity implements View.OnClickListener {

    private static final int REQUEST_ALBUM_OK = 1108;
    private CircleImageView img_head;
    private View img_back;
    private LinearLayout ll_nike_name, ll_credit,ll_no_credit;
    private TextView tv_nike_name, tv_uid;
    private Button btn_login_out;
    private List<LocalMedia> localMedia;
    private EditText ed_nike_name;
    private Button btn_ok;
    private String nikeName,star;
    private String headPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.person_data_layout);
        Intent intent = getIntent();
        nikeName = intent.getStringExtra("nikeName");
        headPath = intent.getStringExtra("headPath");
        star = intent.getStringExtra("heartNum");
        initView();
        initData();
        initListener();
    }

    private void initView() {
        img_back = getViewById(R.id.img_back);
        TextView tv_title = getViewById(R.id.tv_title);
        tv_title.setText("个人质料");
        img_head = getViewById(R.id.img_head);
        ll_nike_name = getViewById(R.id.ll_nike_name);
        ll_credit = getViewById(R.id.ll_credit);
        ll_no_credit = getViewById(R.id.ll_no_credit);
        tv_nike_name = getViewById(R.id.tv_nike_name);
        tv_uid = getViewById(R.id.tv_uid);
        btn_login_out = getViewById(R.id.btn_login_out);
    }

    private void initData() {
        LoginBean loginBean = BaseApplication.loginBean;
        LoginBean.UserinfoBean userinfo = loginBean.getUserinfo();
        if (!"".equals(headPath) && headPath != null) {
            ImageLoadUtil.GlideImageLoad(mContext, headPath, img_head);
        } else {
            ImageLoadUtil.GlideImageLoad(mContext, userinfo.getAvatar(), img_head);
        }
        tv_nike_name.setText(nikeName);
        tv_uid.setText(userinfo.getUid());
        for (int i = 0; i < Integer.valueOf(star); i++) {
            ImageView imageView = new ImageView(mContext);
            imageView.setBackgroundResource(R.mipmap.img_credit_heart);
            ll_credit.addView(imageView);
        }
        for (int i = 0; i < 5 - Integer.valueOf(star); i++) {
            ImageView imageView = new ImageView(mContext);
            imageView.setBackgroundResource(R.mipmap.img_gray_heart);
            ll_no_credit.addView(imageView);
        }
    }

    private void initListener() {
        img_back.setOnClickListener(this);
        img_head.setOnClickListener(this);
        ll_nike_name.setOnClickListener(this);
        btn_login_out.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.img_head:
                openGallery();
                break;
            case R.id.ll_nike_name:
                showModifyDialog();
                break;
            case R.id.btn_login_out:
                SharedPreferences.Editor sp = getSharedPreferences("save_name_pwd", Context.MODE_PRIVATE).edit();
                sp.clear();
                sp.commit();
                SharedPreferences.Editor is_first = getSharedPreferences("IS_FIRST", Context.MODE_PRIVATE).edit();
                is_first.clear();
                is_first.commit();
//                SharedPreferences.Editor headPath = getPreferences(Context.MODE_PRIVATE).edit();
//                headPath.clear();
//                headPath.commit();
                ActivityPageManager instance = ActivityPageManager.getInstance();
                instance.finishAllActivity();
                startActivity(LoginActivity.class);
                finish();
                break;
        }
    }

    private void showModifyDialog() {
        final ModifyNikeNameDialog dialog = new ModifyNikeNameDialog(mContext);
        ed_nike_name = dialog.findViewById(R.id.ed_nike_name);
        btn_ok = dialog.findViewById(R.id.btn_ok);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(ed_nike_name.getText().toString().trim())) {
                    showToastMessage("输入昵称不能为空");
                } else {
                    dialog.dismiss();
                    tv_nike_name.setText(ed_nike_name.getText().toString().trim());
                    requestData();
                }
            }
        });
        dialog.show();
    }

    /**
     * 修改昵称
     */
    private void requestData() {
        NetWork.getRetrofit().
                create(ImpModifyNikeNameService.class)
                .getCall(NetWork.getToken(), tv_nike_name.getText().toString().trim())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ModifyLoginPwdBean>() {
                    private ModifyLoginPwdBean modifyLoginPwdBean;

                    @Override
                    public void onCompleted() {
                        showToastMessage(modifyLoginPwdBean.getMsg());
                        if ("0".equals(modifyLoginPwdBean.getCode())) {
                            tv_nike_name.setText(ed_nike_name.getText().toString().trim());
                            Intent intent = new Intent("MODIFY_NIKENAME");
                            intent.putExtra("name", ed_nike_name.getText().toString().trim());
                            intent.putExtra("headPath", BaseApplication.loginBean.getUserinfo().getAvatar());
                            sendBroadcast(intent);
                        }else if ("2" .equals( modifyLoginPwdBean.getCode())) {
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
                    public void onNext(ModifyLoginPwdBean modifyLoginPwdBean) {
                        this.modifyLoginPwdBean = modifyLoginPwdBean;

                    }
                });
    }

    private void openGallery() {
        PictureSelector.create(PersonDataActivity.this)
                .openGallery(PictureMimeType.ofAll())
                .maxSelectNum(1)// 最大图片选择数量
                .selectionMode(PictureConfig.SINGLE)
                .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                .imageFormat(PictureMimeType.PNG)// 拍照保存图片格式后缀,默认jpeg
                .enableCrop(true)// 是否裁剪
                .compress(true)// 是否压缩
                .compressSavePath(FileUtils.getPath())//压缩图片保存地址
                .synOrAsy(true)//同步true或异步false 压缩 默认同步
                .glideOverride(160, 160)// glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
                .withAspectRatio(16, 9)// 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
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

                    NetWork.getRetrofit().
                            create(ImpModifyPersonDataService.class)
                            .getCall(NetWork.getToken(), body)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Observer<ModifyLoginPwdBean>() {
                                private ModifyLoginPwdBean modifyLoginPwdBean;

                                @Override
                                public void onCompleted() {
                                    showToastMessage(modifyLoginPwdBean.getMsg());
                                    if ("0".equals(modifyLoginPwdBean.getCode())) {
                                        ImageLoadUtil.GlideImageLoad(mContext, localMedia.get(0).getPath(), img_head);
                                        Intent intent = new Intent("MODIFY_NIKENAME");
                                        intent.putExtra("headPath", localMedia.get(0).getPath());
                                        intent.putExtra("name", tv_nike_name.getText().toString());
                                        sendBroadcast(intent);
                                    }else if ("2" .equals( modifyLoginPwdBean.getCode())) {
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
                                public void onNext(ModifyLoginPwdBean modifyLoginPwdBean) {
                                    this.modifyLoginPwdBean = modifyLoginPwdBean;

                                }
                            });
                    break;
            }
        }
    }

}
