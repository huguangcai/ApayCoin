package com.ysxsoft.apaycoin.view;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ysxsoft.apaycoin.R;
import com.ysxsoft.apaycoin.com.ActivityPageManager;
import com.ysxsoft.apaycoin.com.BaseActivity;
import com.ysxsoft.apaycoin.impservice.ImpMyWalletAddressService;
import com.ysxsoft.apaycoin.modle.MyWalletAddressBean;
import com.ysxsoft.apaycoin.utils.ImageLoadUtil;
import com.ysxsoft.apaycoin.utils.NetWork;
import com.ysxsoft.zxing.util.QRCodeUtil;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 描述： TODO
 * 日期： 2018/11/15 0015 17:12
 * 作者： 胡
 * 公司：郑州亿生信科技有限公司
 */
public class WalletAddressActivity extends BaseActivity implements View.OnClickListener {

    private View img_back;
    private TextView tv_title, tv_copy_wallet_address, tv_wallet_address;
    private ImageView img_qrCode;
    private String wallets;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wallet_address_layout);
        initView();
        initData();
        initListener();
    }

    private void initData() {
        NetWork.getRetrofit()
                .create(ImpMyWalletAddressService.class)
                .getCall(NetWork.getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<MyWalletAddressBean>() {
                    private MyWalletAddressBean myWalletAddressBean;

                    @Override
                    public void onCompleted() {
//                        showToastMessage(myWalletAddressBean.getMsg());
                        wallets = myWalletAddressBean.getWallets();
//                        Bitmap qrCodeBitmap = QRCodeUtil.createQRCodeBitmap(myWalletAddressBean.getQrData(), 500, 500);
//                        img_qrCode.setImageBitmap(qrCodeBitmap);
                        tv_wallet_address.setText(myWalletAddressBean.getWallets());
                        ImageLoadUtil.NewGoodsGlideImageLoad(mContext,myWalletAddressBean.getQrData(),img_qrCode);
//                        Bitmap bitmap = QRCodeUtil.returnBitMap(wallets);
                         if ("2" .equals( myWalletAddressBean.getCode())) {
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
                    public void onNext(MyWalletAddressBean myWalletAddressBean) {
                        this.myWalletAddressBean = myWalletAddressBean;
                    }
                });

    }

    private void initView() {
        img_back = getViewById(R.id.img_back);
        tv_title = getViewById(R.id.tv_title);
        tv_title.setText("钱包地址");
        img_qrCode = getViewById(R.id.img_qrCode);
        tv_copy_wallet_address = getViewById(R.id.tv_copy_wallet_address);
        tv_wallet_address = getViewById(R.id.tv_wallet_address);

    }

    private void initListener() {
        img_back.setOnClickListener(this);
        tv_copy_wallet_address.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.tv_copy_wallet_address:
                ClipboardManager cmb = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
                cmb.setText(wallets);
                showToastMessage("复制完成");
                break;


        }

    }
}
