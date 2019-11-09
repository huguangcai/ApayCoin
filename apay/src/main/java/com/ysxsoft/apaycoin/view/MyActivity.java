package com.ysxsoft.apaycoin.view;

import android.Manifest;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tbruyelle.rxpermissions.RxPermissions;
import com.ysxsoft.apaycoin.R;
import com.ysxsoft.apaycoin.com.ActivityPageManager;
import com.ysxsoft.apaycoin.com.BaseActivity;
import com.ysxsoft.apaycoin.com.BaseApplication;
import com.ysxsoft.apaycoin.fragment.DayDayCheckInDialog;
import com.ysxsoft.apaycoin.impservice.ImpLinghongbaoService;
import com.ysxsoft.apaycoin.impservice.ImpLoginService;
import com.ysxsoft.apaycoin.impservice.ImpNumAssetApayService;
import com.ysxsoft.apaycoin.modle.LinghongbaoBean;
import com.ysxsoft.apaycoin.modle.LoginBean;
import com.ysxsoft.apaycoin.modle.NumAssetApayBean;
import com.ysxsoft.apaycoin.utils.ImageLoadUtil;
import com.ysxsoft.apaycoin.utils.NetWork;
import com.ysxsoft.apaycoin.widget.CircleImageView;
import com.ysxsoft.apaycoin.widget.ReceverTicketDialog;
import com.ysxsoft.zxing.CaptureActivity;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * 描述： 我的界面
 * 日期： 2018/11/5 0005 10:08
 * 作者： 胡
 * 公司：郑州亿生信科技有限公司
 */
public class MyActivity extends BaseActivity implements View.OnClickListener {

    public static final int SCAN_CODE_CHANGE_BALANCE_MONEY = 2;
    private TextView tv_uid, tv_balance_money, tv_ticket_num;
    private CircleImageView img_head;
    private ImageView img_setting, img_scan_scan;
    private LinearLayout ll_credit, ll_no_credit, ll_roll_out, ll_roll_input, ll_num_asset, ll_online_mall, ll_near_business;
    private LinearLayout ll_balance_money, ll_ticket;
    private String flag;
    private String shop_flag;
    private String head, uid, credit, ff;
    private String headPath;
    private int stateBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_layout);
        setHalfTransparent();
        setFitSystemWindow(false);
        stateBar = getStateBar();
        RequestPersmission();
        Intent intent = getIntent();
        flag = intent.getStringExtra("flag");
        head = intent.getStringExtra("head");
        uid = intent.getStringExtra("uid");
        credit = intent.getStringExtra("credit");
        ff = intent.getStringExtra("ff");
        initView();
        if (credit != null) {
            ImageLoadUtil.GlideImageLoad(mContext, head, img_head);
            tv_uid.setText(uid);
            for (int i = 0; i < Integer.valueOf(credit); i++) {
                ImageView imageView = new ImageView(mContext);
                imageView.setBackgroundResource(R.mipmap.img_credit_heart);
                ll_credit.addView(imageView);
            }
            for (int i = 0; i < 5 - Integer.valueOf(credit); i++) {
                ImageView imageView = new ImageView(mContext);
                imageView.setBackgroundResource(R.mipmap.img_gray_heart);
                ll_no_credit.addView(imageView);
            }
        }

        IntentFilter filter = new IntentFilter("MODIFY_NIKENAME");
        registerReceiver(broadcastReceiver, filter);
        getData();
        initListener();
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if ("MODIFY_NIKENAME".equals(intent.getAction())) {
                String headPath = intent.getStringExtra("headPath");
                ImageLoadUtil.GlideImageLoad(mContext, headPath, img_head);
            }
        }
    };

    private void getData() {
        if (TextUtils.isEmpty(NetWork.getToken()) || NetWork.getToken() == null) {
            startActivity(LoginActivity.class);
            finish();
            return;
        }
        NetWork.getRetrofit()
                .create(ImpNumAssetApayService.class)
                .getCall(NetWork.getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<NumAssetApayBean>() {
                    private NumAssetApayBean numAssetApayBean;

                    @Override
                    public void onCompleted() {
                        if ("0".equals(numAssetApayBean.getCode())) {
                            shop_flag = numAssetApayBean.getData().getShop_flag();
                            tv_balance_money.setText(numAssetApayBean.getData().getMoney());
                            tv_ticket_num.setText(numAssetApayBean.getData().getQuans());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        showToastMessage(e.getMessage());
                    }

                    @Override
                    public void onNext(NumAssetApayBean numAssetApayBean) {
                        this.numAssetApayBean = numAssetApayBean;
                    }
                });


    }

    /**
     * 申请权限
     */
    private void RequestPersmission() {
        RxPermissions.getInstance(mContext)
                .request(Manifest.permission.CAMERA,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.SEND_SMS,
                        Manifest.permission.RECORD_AUDIO)
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean aBoolean) {
//                        showToastMessage("申请权限");
                    }
                });
    }

    private void initdata() {
        SharedPreferences sp = getSharedPreferences("save_name_pwd", Context.MODE_PRIVATE);
        String username = sp.getString("username", "");
        String pwd = sp.getString("pwd", "");
        if (!"".equals(username) && !"".equals(pwd)) {
            NetWork.getRetrofit().
                    create(ImpLoginService.class).getLogin(username, pwd)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<LoginBean>() {
                        private LoginBean loginBean;

                        @Override
                        public void onCompleted() {
                            if ("0".equals(loginBean.getCode())) {
                                BaseApplication.loginBean = loginBean;
                                String avatar = loginBean.getUserinfo().getAvatar();
                                String uid = loginBean.getUserinfo().getUid();
                                String star = loginBean.getUserinfo().getStar();
//                                flag = loginBean.getUserinfo().getFlag();
                                ImageLoadUtil.GlideImageLoad(mContext, avatar, img_head);
                                tv_uid.setText(uid);
                                ll_credit.removeAllViews();
                                ll_no_credit.removeAllViews();
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
                        }

                        @Override
                        public void onError(Throwable e) {
                            showToastMessage(e.getMessage());
                        }

                        @Override
                        public void onNext(LoginBean loginBean) {
                            this.loginBean = loginBean;
                        }
                    });
        }
    }

    private void initView() {
        LinearLayout ll_title = getViewById(R.id.ll_title);
        ll_title.setPadding(0, stateBar, 0, 0);
        img_head = getViewById(R.id.img_head);
        tv_uid = getViewById(R.id.tv_uid);
        ll_credit = getViewById(R.id.ll_credit);
        img_setting = getViewById(R.id.img_setting);
        img_scan_scan = getViewById(R.id.img_scan_scan);
        tv_balance_money = getViewById(R.id.tv_balance_money);
        tv_ticket_num = getViewById(R.id.tv_ticket_num);
        ll_roll_out = getViewById(R.id.ll_roll_out);
        ll_roll_input = getViewById(R.id.ll_roll_input);
        ll_num_asset = getViewById(R.id.ll_num_asset);
        ll_online_mall = getViewById(R.id.ll_online_mall);
        ll_near_business = getViewById(R.id.ll_near_business);
        ll_balance_money = getViewById(R.id.ll_balance_money);
        ll_ticket = getViewById(R.id.ll_ticket);
        ll_no_credit = getViewById(R.id.ll_no_credit);

        final DayDayCheckInDialog dialog = new DayDayCheckInDialog(mContext);
        dialog.setCanceledOnTouchOutside(false);//点击空白处不消失
        Button btn_deposit_balance = dialog.findViewById(R.id.btn_deposit_balance);
        final TextView tv_money = dialog.findViewById(R.id.tv_money);

        btn_deposit_balance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NetWork.getRetrofit()
                        .create(ImpLinghongbaoService.class)
                        .getCall(NetWork.getToken())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<LinghongbaoBean>() {
                            private LinghongbaoBean linghongbaoBean;

                            @Override
                            public void onCompleted() {
                                dialog.dismiss();
                                if ("0".equals(linghongbaoBean.getCode())) {
                                    dialog.dismiss();
                                } else if ("2".equals(linghongbaoBean.getCode())) {
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
                            public void onNext(LinghongbaoBean linghongbaoBean) {
                                this.linghongbaoBean = linghongbaoBean;
                            }
                        });
            }
        });

        final ReceverTicketDialog dialog1 = new ReceverTicketDialog(mContext);
        if ("0".equals(ff)) {//0显示 1 不显示
            ImageView img_recever_ticket = dialog1.findViewById(R.id.img_recever_ticket);
            img_recever_ticket.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog1.dismiss();
                    if (!"0".equals(flag) && !TextUtils.isEmpty(flag) && flag != null) {
                        tv_money.setText(flag);
                        dialog.show();
                    }
                }
            });

            dialog1.show();
        } else {
            if (!"0".equals(flag) && !TextUtils.isEmpty(flag) && flag != null) {
                tv_money.setText(flag);
                dialog.show();
            }
//            dialog1.dismiss();
        }
    }

    private void initListener() {
        img_head.setOnClickListener(this);
        img_setting.setOnClickListener(this);
        img_scan_scan.setOnClickListener(this);
        ll_roll_out.setOnClickListener(this);
        ll_roll_input.setOnClickListener(this);
        ll_num_asset.setOnClickListener(this);
        ll_online_mall.setOnClickListener(this);
        ll_near_business.setOnClickListener(this);
        ll_balance_money.setOnClickListener(this);
        ll_ticket.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_head:// 头像
                Intent intent = new Intent(mContext, PersonCenterActivity.class);
                intent.putExtra("shop_flag", shop_flag);
                startActivity(intent);
                break;
            case R.id.img_setting://设置
                startActivity(SettingActivity.class);
                break;
            case R.id.img_scan_scan://扫描
                RxPermissions.getInstance(mContext)
                        .request(Manifest.permission.CAMERA)//多个权限用","隔开
                        .subscribe(new Action1<Boolean>() {
                            @Override
                            public void call(Boolean aBoolean) {
                                if (aBoolean) {
                                    //当所有权限都允许之后，返回true
                                    Intent intent = new Intent(mContext, CaptureActivity.class);
                                    startActivityForResult(intent, SCAN_CODE_CHANGE_BALANCE_MONEY);
                                } else {
                                    //下一次申请只申请没通过申请的权限
                                    showToastMessage("请打开权限");
                                }
                            }
                        });
//                startActivity(CaptureActivity.class);
                break;
            case R.id.ll_roll_out://转出
                startActivity(RollOutActivity.class);
                break;
            case R.id.ll_roll_input://转入
                startActivity(RollIntputActivity.class);
                break;
            case R.id.ll_num_asset://数字资产
                startActivity(NumAssetActivity.class);
                break;
            case R.id.ll_online_mall://线上商城
                startActivity(OnLineMallActivity.class);
                break;
            case R.id.ll_near_business://周边商家
                startActivity(NearBusinessActivity.class);
                break;
            case R.id.ll_ticket://券纪录
                startActivity(TicketRecordActivity.class);
                break;
            case R.id.ll_balance_money://余额记录
                startActivity(BalanceMoneyRecordActivity.class);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case SCAN_CODE_CHANGE_BALANCE_MONEY:
                if (resultCode == RESULT_OK) {
                    String result = data.getStringExtra("scan_result");
                    if (result != null) {
                        Intent intent = new Intent(mContext, ScanPayActivity.class);
                        intent.putExtra("qrcode", result);
                        startActivity(intent);
                    }
                } else if (resultCode == RESULT_CANCELED) {
//                    showToastMessage("扫描出错");
                }
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        initdata();
        getData();

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }
}
