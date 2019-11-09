package com.ysxsoft.apaycoin.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ysxsoft.apaycoin.R;
import com.ysxsoft.apaycoin.com.ActivityPageManager;
import com.ysxsoft.apaycoin.com.BaseActivity;
import com.ysxsoft.apaycoin.com.BaseApplication;
import com.ysxsoft.apaycoin.downloadapk.MyIntentService;
import com.ysxsoft.apaycoin.impservice.ImpGetCreditNumService;
import com.ysxsoft.apaycoin.impservice.ImpLoginService;
import com.ysxsoft.apaycoin.impservice.ImpNumAssetApayService;
import com.ysxsoft.apaycoin.impservice.ImpRecoverCreditService;
import com.ysxsoft.apaycoin.impservice.ImpUpDataVersionCodeService;
import com.ysxsoft.apaycoin.modle.GetCreditNumBean;
import com.ysxsoft.apaycoin.modle.LoginBean;
import com.ysxsoft.apaycoin.modle.NumAssetApayBean;
import com.ysxsoft.apaycoin.modle.RecoverCreditBean;
import com.ysxsoft.apaycoin.modle.UpDataVersionCodeBean;
import com.ysxsoft.apaycoin.utils.AppUtil;
import com.ysxsoft.apaycoin.utils.FileUtils;
import com.ysxsoft.apaycoin.utils.ImageLoadUtil;
import com.ysxsoft.apaycoin.utils.NetWork;
import com.ysxsoft.apaycoin.widget.CircleImageView;
import com.ysxsoft.apaycoin.widget.CreditRecoveryDialog;
import com.ysxsoft.apaycoin.widget.PayPwdEditText;
import com.ysxsoft.apaycoin.widget.VersionCodeDialog;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 描述： 个人中心
 * 日期： 2018/11/5 0005 14:01
 * 作者： 胡
 * 公司：郑州亿生信科技有限公司
 */
public class PersonCenterActivity extends BaseActivity implements View.OnClickListener {

    private View back;
    private TextView tv_title;
    private TextView tv_uid;
    private TextView tv_nike_name;
    private LinearLayout ll_credit, ll_no_credit;
    private TextView ll_become_business, ll_my_order, ll_address_manager, ll_my_bank;
    private TextView ll_my_asset, ll_share, ll_notice, ll_give_feedback, ll_about_my;
    private TextView ll_version_code, ll_setting, ll_person_info, tv_recover_credit;
    private CircleImageView img_head;
    private String shop_flag;
    private String headPath;
    private String star;
    private int stateBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.person_center_layout);
        Intent intent = getIntent();
        shop_flag = intent.getStringExtra("shop_flag");//-shop_flag	0没 1线上2线下
        setHalfTransparent();
        setFitSystemWindow(false);
        stateBar = getStateBar();
        initView();
        IntentFilter filter = new IntentFilter("MODIFY_NIKENAME");
        registerReceiver(broadcastReceiver, filter);
        initData();
        initListener();
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if ("MODIFY_NIKENAME".equals(intent.getAction())) {
                String name = intent.getStringExtra("name");
                tv_nike_name.setText(name);
                headPath = intent.getStringExtra("headPath");
                ImageLoadUtil.GlideImageLoad(mContext, headPath, img_head);
            }
        }
    };

    private void initView() {
        RelativeLayout ll_title = getViewById(R.id.ll_title);
        ll_title.setPadding(0,stateBar,0,0);
        back = getViewById(R.id.img_back);
        tv_title = getViewById(R.id.tv_title);
        tv_title.setText("我的");
        img_head = getViewById(R.id.img_head);
        tv_uid = getViewById(R.id.tv_uid);
        tv_nike_name = getViewById(R.id.tv_nike_name);
        tv_recover_credit = getViewById(R.id.tv_recover_credit);
        ll_credit = getViewById(R.id.ll_credit);
        ll_no_credit = getViewById(R.id.ll_no_credit);
        ll_become_business = getViewById(R.id.ll_become_business);
        ll_my_order = getViewById(R.id.ll_my_order);
        ll_address_manager = getViewById(R.id.ll_address_manager);
        ll_my_bank = getViewById(R.id.ll_my_bank);
        ll_my_asset = getViewById(R.id.ll_my_asset);
        ll_share = getViewById(R.id.ll_share);
        ll_notice = getViewById(R.id.ll_notice);
        ll_person_info = getViewById(R.id.ll_person_info);
        ll_give_feedback = getViewById(R.id.ll_give_feedback);
        ll_about_my = getViewById(R.id.ll_about_my);
        ll_version_code = getViewById(R.id.ll_version_code);
        ll_setting = getViewById(R.id.ll_setting);
        switch (shop_flag) {
            case "0":
                ll_become_business.setText("申请成为商家");
//                ll_become_business.setTextColor(mContext.getResources().getColor(R.color.black));
                break;
            case "1":
            case "2":
                ll_become_business.setText("我的店铺");
                break;
            case "3":
                ll_become_business.setText("店铺申请中");
//                ll_become_business.setTextColor(mContext.getResources().getColor(R.color.btn_cancle_bg));
                break;
        }
    }

    private void initData() {
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
                                String nickname = loginBean.getUserinfo().getNickname();
                                String uid = loginBean.getUserinfo().getUid();
                                star = loginBean.getUserinfo().getStar();
                                ImageLoadUtil.GlideImageLoad(mContext, avatar, img_head);
                                tv_uid.setText(uid);
                                tv_nike_name.setText(nickname);
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
                            }else if ("2" == loginBean.getCode()) {
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
                        public void onNext(LoginBean loginBean) {
                            this.loginBean = loginBean;
                        }
                    });
        }
    }

    private void initListener() {
        back.setOnClickListener(this);
        img_head.setOnClickListener(this);
        ll_become_business.setOnClickListener(this);
        ll_my_order.setOnClickListener(this);
        ll_address_manager.setOnClickListener(this);
        ll_my_bank.setOnClickListener(this);
        ll_my_asset.setOnClickListener(this);
        ll_share.setOnClickListener(this);
        ll_notice.setOnClickListener(this);
        ll_person_info.setOnClickListener(this);
        ll_give_feedback.setOnClickListener(this);
        ll_about_my.setOnClickListener(this);
        ll_version_code.setOnClickListener(this);
        ll_setting.setOnClickListener(this);
        tv_recover_credit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.img_head:
                Intent intent = new Intent(mContext, PersonDataActivity.class);
                intent.putExtra("nikeName", tv_nike_name.getText().toString());
                intent.putExtra("heartNum", star);
                if (!"".equals(headPath)) {
                    intent.putExtra("headPath", headPath);
                }
                startActivity(intent);
                break;
            case R.id.ll_become_business:
                switch (shop_flag) {
                    case "0":
                        startActivity(BecomeBusinessActivity.class);//申请成为商家
                        break;
                    case "1":
                        startActivity(LookMyMallActivity.class);//查看店铺
                        break;
                    case "2":
                        startActivity(UnderLineActivity.class);//线下商家
                        break;
                    case "3":
//                    showToastMessage("店铺申请中");
                        break;
                }
                break;
            case R.id.ll_my_order:
                startActivity(MyOrderActivity.class);
                break;
            case R.id.ll_address_manager:
                startActivity(AddressManagerActivity.class);
                break;
            case R.id.ll_my_bank:
                startActivity(MybankCardActivity.class);
                break;
            case R.id.ll_my_asset:
                startActivity(MyAssetActivity.class);
                break;
            case R.id.ll_share:
                startActivity(ShareActivity.class);
                break;
            case R.id.ll_notice:
                startActivity(NoticeActivity.class);
                break;
            case R.id.ll_person_info:
                startActivity(PersonInfoActivity.class);
                break;
            case R.id.ll_give_feedback:
                startActivity(GiveFeedBackActivity.class);
                break;
            case R.id.ll_about_my:
                startActivity(AboutMyActivity.class);
                break;
            case R.id.ll_version_code:
                getServiceData();
                break;
            case R.id.ll_setting:
                startActivity(SettingActivity.class);
                break;
            case R.id.tv_recover_credit:
                getCreditData();
//                showCreditDialog();
                break;
        }
    }

    /**
     * 获取一个信用需要多少币的数据请求
     */
    private void getCreditData() {
        NetWork.getRetrofit()
                .create(ImpGetCreditNumService.class)
                .getCall(NetWork.getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<GetCreditNumBean>() {
                    private GetCreditNumBean getCreditNumBean;

                    @Override
                    public void onCompleted() {
                        showToastMessage(getCreditNumBean.getMsg());
                        if ("0".equals(getCreditNumBean.getCode())) {
                            showCreditDialog(getCreditNumBean.getData());
                        }else if ("2" == getCreditNumBean.getCode()) {
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
                    public void onNext(GetCreditNumBean getCreditNumBean) {
                        this.getCreditNumBean = getCreditNumBean;
                    }
                });

    }

    /**
     * 信用恢复的dialog
     *
     * @param data
     */
    private void showCreditDialog(final String data){
        final CreditRecoveryDialog recoveryDialog = new CreditRecoveryDialog(mContext);
        LinearLayout ll_credit_no_good = recoveryDialog.findViewById(R.id.ll_credit_no_good);
        LinearLayout ll_credit_good = recoveryDialog.findViewById(R.id.ll_credit_good);
        TextView tv_credit_good = recoveryDialog.findViewById(R.id.tv_credit_good);
        TextView tv_apay_num = recoveryDialog.findViewById(R.id.tv_apay_num);
        if (Integer.valueOf(star)==5){
            ll_credit_good.setVisibility(View.VISIBLE);
            ll_credit_no_good.setVisibility(View.GONE);
        }else {
            ll_credit_good.setVisibility(View.GONE);
            ll_credit_no_good.setVisibility(View.VISIBLE);
        }
        tv_apay_num.setText(data);
        PayPwdEditText ed_ppet = recoveryDialog.findViewById(R.id.ed_ppet);
        final TextView tv_balance_money = recoveryDialog.findViewById(R.id.tv_balance_money);
        final EditText ed_apay = recoveryDialog.findViewById(R.id.ed_apay);
        Button btn_all = recoveryDialog.findViewById(R.id.btn_all);
        ed_apay.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(ed_apay.getText().toString().trim())) {
                    tv_balance_money.setText("");
                    return;
                }
                tv_balance_money.setText(String.valueOf(Integer.valueOf(data) * Integer.valueOf(ed_apay.getText().toString().trim())));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        btn_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ed_apay.setText(String.valueOf(5 - Integer.valueOf(star)));
                tv_balance_money.setText(String.valueOf((5 - Integer.valueOf(star)) * Integer.valueOf(data)));
            }
        });
        ed_ppet.setOnTextFinishListener(new PayPwdEditText.OnTextFinishListener() {
            @Override
            public void onFinish(String str) {
                NetWork.getRetrofit()
                        .create(ImpRecoverCreditService.class)
                        .getCall(NetWork.getToken(), ed_apay.getText().toString().trim(), str)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<RecoverCreditBean>() {
                            private RecoverCreditBean recoverCreditBean;

                            @Override
                            public void onCompleted() {
                                showToastMessage(recoverCreditBean.getMsg());
                                recoveryDialog.dismiss();
                                if ("0".equals(recoverCreditBean.getCode())) {
                                    ll_credit.removeAllViews();
                                    ll_no_credit.removeAllViews();
                                    star = recoverCreditBean.getStar();
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

                                    Intent intent = new Intent("CREDIT_NUM");
                                    intent.putExtra("credit_num", star);
                                    sendBroadcast(intent);
                                }else if ("2" .equals( recoverCreditBean.getCode())) {
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
                            public void onNext(RecoverCreditBean recoverCreditBean) {
                                this.recoverCreditBean = recoverCreditBean;
                            }
                        });
            }
        });
        recoveryDialog.show();
    }

    /**
     * 获取服务器数据
     */
    private void getServiceData() {
        NetWork.getRetrofit()
                .create(ImpUpDataVersionCodeService.class)
                .getCall("1")//type	1安卓2 ios
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<UpDataVersionCodeBean>() {
                    private UpDataVersionCodeBean upDataVersionCodeBean;

                    @Override
                    public void onCompleted() {
                        showToastMessage(upDataVersionCodeBean.getMsg());
                        if ("0".equals(upDataVersionCodeBean.getCode())) {
                            int versionCode = AppUtil.getVersionCode(mContext);//本地
                            String packageName = AppUtil.getVersionName(mContext);//本地
                            String verCode = upDataVersionCodeBean.getData().getVerCode();// 服务器verCode
                            String version = upDataVersionCodeBean.getData().getVersion();//版本号
                            String s1 = AppUtil.splitStrPoint(packageName);//本地的版本号
                            String s = AppUtil.splitStrPoint(version);//服务器的版本号
                            final VersionCodeDialog dialog = new VersionCodeDialog(mContext);
                            TextView tv_version_code = dialog.findViewById(R.id.tv_version_code);
                            TextView tv_version_code_tip = dialog.findViewById(R.id.tv_version_code_tip);
                            TextView tv_updata = dialog.findViewById(R.id.tv_updata);
                            View view_line = dialog.findViewById(R.id.view_line);
                            if (versionCode < Integer.valueOf(verCode) || Integer.valueOf(s1) < Integer.valueOf(s)) {
                                tv_version_code.setText(version);
                                tv_version_code_tip.setText(upDataVersionCodeBean.getData().getContent());
                                tv_updata.setVisibility(View.VISIBLE);
                                view_line.setVisibility(View.VISIBLE);
                                dialog.show();
                            } else if (versionCode == Integer.valueOf(verCode) && Integer.valueOf(s1) == Integer.valueOf(s)) {
                                tv_version_code.setText(packageName);
                                tv_version_code_tip.setText("当前已是最新版本号");
                                tv_updata.setVisibility(View.GONE);
                                view_line.setVisibility(View.GONE);
                                dialog.show();
                            }
                            tv_updata.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                    String url = upDataVersionCodeBean.getData().getFileAbsolutePath();
                                    String apkPath = FileUtils.getSDCardPath() + "/download";
                                    MyIntentService.startUpdateService(mContext, url, apkPath);
                                }
                            });
                        }else if ("2" .equals( upDataVersionCodeBean.getCode())) {
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
                    public void onNext(UpDataVersionCodeBean upDataVersionCodeBean) {
                        this.upDataVersionCodeBean = upDataVersionCodeBean;
                    }
                });

    }

    @Override
    protected void onResume() {
        super.onResume();
        NetWork.getRetrofit().create(ImpNumAssetApayService.class)
                .getCall(NetWork.getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<NumAssetApayBean>() {
                    private NumAssetApayBean numAssetApayBean;

                    @Override
                    public void onCompleted() {
                        if ("0".equals(numAssetApayBean.getCode())) {
                            shop_flag = numAssetApayBean.getData().getShop_flag();
                            switch (shop_flag) {
                                case "0":
                                    ll_become_business.setText("申请成为商家");
                                    break;
                                case "1":
                                case "2":
                                    ll_become_business.setText("我的店铺");
                                    break;
                                case "3":
                                    ll_become_business.setText("店铺申请中");
                                    break;
                            }
                        }else if ("2" .equals( numAssetApayBean.getCode())) {
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
                    public void onNext(NumAssetApayBean numAssetApayBean) {

                        this.numAssetApayBean = numAssetApayBean;
                    }
                });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }
}
