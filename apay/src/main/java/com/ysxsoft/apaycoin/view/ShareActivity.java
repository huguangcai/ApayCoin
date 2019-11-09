package com.ysxsoft.apaycoin.view;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.ysxsoft.apaycoin.R;
import com.ysxsoft.apaycoin.com.ActivityPageManager;
import com.ysxsoft.apaycoin.com.BaseActivity;
import com.ysxsoft.apaycoin.com.BaseApplication;
import com.ysxsoft.apaycoin.impservice.ImpShareQRCodeService;
import com.ysxsoft.apaycoin.modle.ShareQrCodeBean;
import com.ysxsoft.apaycoin.utils.FileUtils;
import com.ysxsoft.apaycoin.utils.ImageLoadUtil;
import com.ysxsoft.apaycoin.utils.NetWork;
import com.ysxsoft.zxing.util.QRCodeUtil;

import java.io.File;
import java.io.IOException;

import retrofit2.Retrofit;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 描述： 分享界面
 * 日期： 2018/11/6 0006 13:45
 * 作者： 胡
 * 公司：郑州亿生信科技有限公司
 */
public class ShareActivity extends BaseActivity implements View.OnClickListener {

    private View img_back;
    private TextView tv_title, tv_title_right, tv_save_qrCode, tv_copy_download_address,tv_yaoqing;
    private ImageView img_qrcode_bg, img_head_bg;
    private String qrData;
    private Bitmap bitmap;
    private int stateBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.share_layout);
        setHalfTransparent();
        setFitSystemWindow(false);
        stateBar = getStateBar();
        initView();
        initData();
        initListener();
    }

    private void initData() {
        Retrofit retrofit = NetWork.getRetrofit();
        ImpShareQRCodeService service = retrofit.create(ImpShareQRCodeService.class);
        Observable<ShareQrCodeBean> observable = service.getCall(NetWork.getToken());
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ShareQrCodeBean>() {
                    private ShareQrCodeBean sHareQrCodeBean;

                    @Override
                    public void onCompleted() {
                        if ("0".equals(sHareQrCodeBean.getCode())) {
                            qrData = sHareQrCodeBean.getLink_url();
                            String data = sHareQrCodeBean.getQrData();
                            bitmap = QRCodeUtil.CreateQrBitmap(ShareActivity.this.qrData, 500, 500);
                            img_qrcode_bg.setImageBitmap(bitmap);
//                            ImageLoadUtil.GlideImageLoad(mContext, qrData, img_qrcode_bg);
//                            bitmap = QRCodeUtil.returnBitMap(qrData);
                            tv_yaoqing.setText(sHareQrCodeBean.getInvitation());
                        }else if ("2" .equals( sHareQrCodeBean.getCode())) {
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
                        log(e.getMessage());
                    }

                    @Override
                    public void onNext(ShareQrCodeBean sHareQrCodeBean) {
                        this.sHareQrCodeBean = sHareQrCodeBean;
                    }
                });
    }

    private void initListener() {
        img_back.setOnClickListener(this);
        tv_title_right.setOnClickListener(this);
        tv_save_qrCode.setOnClickListener(this);
        tv_copy_download_address.setOnClickListener(this);
    }

    private void initView() {
        RelativeLayout ll_title = getViewById(R.id.ll_title);
        ll_title.setPadding(0,stateBar,0,0);
        img_back = getViewById(R.id.img_back);
        tv_title = getViewById(R.id.tv_title);
        tv_title.setText("分享");
        tv_title_right = getViewById(R.id.tv_title_right);
        tv_title_right.setVisibility(View.VISIBLE);
        tv_title_right.setText("分享记录");
        img_qrcode_bg = getViewById(R.id.img_qrcode_bg);
        img_head_bg = getViewById(R.id.img_head_bg);
        tv_save_qrCode = getViewById(R.id.tv_save_qrCode);
        tv_yaoqing = getViewById(R.id.tv_yaoqing);
        tv_copy_download_address = getViewById(R.id.tv_copy_download_address);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.tv_title_right:
                startActivity(ShareRecordActivity.class);
                break;
            case R.id.tv_save_qrCode:
                FileUtils.saveBitmap(mContext, bitmap, "qrcode");
                showToastMessage("保存成功");
                break;
            case R.id.tv_copy_download_address:
                ClipboardManager cmb = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
                cmb.setText(qrData);//TODO 将要复制的下载地址放入settext函数中
                showToastMessage("复制完成");
                break;
        }
    }

}
