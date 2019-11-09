package com.ysxsoft.apaycoin.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ysxsoft.apaycoin.R;
import com.ysxsoft.apaycoin.com.ActivityPageManager;
import com.ysxsoft.apaycoin.com.BaseActivity;
import com.ysxsoft.apaycoin.com.BaseApplication;
import com.ysxsoft.apaycoin.impservice.ImpRollInputService;
import com.ysxsoft.apaycoin.modle.RollInputBean;
import com.ysxsoft.apaycoin.utils.FileUtils;
import com.ysxsoft.apaycoin.utils.ImageLoadUtil;
import com.ysxsoft.apaycoin.utils.NetWork;
import com.ysxsoft.zxing.util.QRCodeUtil;

import java.io.File;
import java.io.IOException;

import retrofit2.Retrofit;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 描述： 转入界面
 * 日期： 2018/11/5 0005 10:48
 * 作者： 胡
 * 公司：郑州亿生信科技有限公司
 */
public class RollIntputActivity extends BaseActivity implements View.OnClickListener {
    private View img_back;
    private TextView tv_title, tv_title_right;
    private TextView tv_save_qrCode;
    private ImageView img_qrCode;
    private Bitmap qrCodeBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
        setContentView(R.layout.roll_input_layout);
        initView();
        initListener();
    }

    private void initView() {
        img_back = getViewById(R.id.img_back);
        tv_title = getViewById(R.id.tv_title);
        tv_title.setText("转入");
        tv_title_right = getViewById(R.id.tv_title_right);
        tv_title_right.setVisibility(View.VISIBLE);
        tv_title_right.setText("转入记录");
        tv_save_qrCode = getViewById(R.id.tv_save_qrCode);
        img_qrCode = getViewById(R.id.img_qrCode);

    }

    private void initData() {
        Retrofit retrofit = NetWork.getRetrofit();
        ImpRollInputService service = retrofit.create(ImpRollInputService.class);
        service.getCall(NetWork.getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<RollInputBean>() {
                    private RollInputBean rollInputBean;

                    @Override
                    public void onCompleted() {
//                        showToastMessage(rollInputBean.getMsg());
                        if ("0".equals(rollInputBean.getCode())) {
                             qrCodeBitmap = QRCodeUtil.createQRCodeBitmap(rollInputBean.getQrcode(), 500, 500);
                             img_qrCode.setImageBitmap(qrCodeBitmap);
                        }else if ("2" .equals( rollInputBean.getCode())) {
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
                        log(e.getMessage());
                    }

                    @Override
                    public void onNext(RollInputBean rollInputBean) {
                        this.rollInputBean = rollInputBean;
                    }
                });
    }

    private void initListener() {
        img_back.setOnClickListener(this);
        tv_title_right.setOnClickListener(this);
        tv_save_qrCode.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.tv_title_right:
                startActivity(RollInputRecordActivity.class);
                break;
            case R.id.tv_save_qrCode:
                FileUtils.saveBitmap(mContext,qrCodeBitmap,"inputQr");
//                try {
//                    FileUtils.saveImg(qrCodeBitmap, "inputQr");
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                File file = new File(FileUtils.getSDCardPath(), "inputQr");
//                Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
//                Uri uri = Uri.fromFile(file);
//                intent.setData(uri);
//                mContext.sendBroadcast(intent);
                showToastMessage("保存成功");
                break;
        }

    }
}
