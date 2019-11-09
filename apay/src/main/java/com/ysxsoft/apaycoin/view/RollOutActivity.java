package com.ysxsoft.apaycoin.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ysxsoft.apaycoin.R;
import com.ysxsoft.apaycoin.com.ActivityPageManager;
import com.ysxsoft.apaycoin.com.BaseActivity;
import com.ysxsoft.apaycoin.impservice.ImpPhoneSearchUserService;
import com.ysxsoft.apaycoin.modle.PhoneSearchUserBean;
import com.ysxsoft.apaycoin.utils.NetWork;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 描述： 转出界面
 * 日期： 2018/11/5 0005 10:46
 * 作者： 胡
 * 公司：郑州亿生信科技有限公司
 */
public class RollOutActivity extends BaseActivity implements View.OnClickListener {

    private TextView tv_title, tv_title_right;
    private View img_back;
    private EditText ed_phone_uid;
    private Button btn_convertibility, btn_next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.roll_out_layout);
        initView();
        initListener();
    }

    private void initView() {
        img_back = getViewById(R.id.img_back);
        tv_title = getViewById(R.id.tv_title);
        tv_title.setText("转出");
        tv_title_right = getViewById(R.id.tv_title_right);
        tv_title_right.setVisibility(View.VISIBLE);
        tv_title_right.setText("转出记录");
        ed_phone_uid = getViewById(R.id.ed_phone_uid);
        btn_next = getViewById(R.id.btn_next);
        btn_convertibility = getViewById(R.id.btn_convertibility);
    }

    private void initListener() {
        img_back.setOnClickListener(this);
        tv_title_right.setOnClickListener(this);
        btn_next.setOnClickListener(this);
        btn_convertibility.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.tv_title_right:
//                showToastMessage("转出记录");
                startActivity(RollOutRecordActivity.class);
                break;
            case R.id.btn_next:
                if (checkData()) return;
                CheckUserData("checkPhone");
//                startActivity(NextRollOutActivity.class);
                break;
            case R.id.btn_convertibility:
//                if (checkData()) return;
                startActivity(ConvertibilityActivity.class);
//                CheckUserData("Convertibility");
                break;
        }
    }

    private void CheckUserData(final String checkPhone) {
        NetWork.getRetrofit()
                .create(ImpPhoneSearchUserService.class)
                .getCall(NetWork.getToken(), ed_phone_uid.getText().toString().trim())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<PhoneSearchUserBean>() {
                    private PhoneSearchUserBean phoneSearchUserBean;

                    @Override
                    public void onCompleted() {
                        showToastMessage(phoneSearchUserBean.getMsg());
                        if ("0".equals(phoneSearchUserBean.getCode())) {
                            if ("checkPhone".equals(checkPhone)) {
                                Intent intent = new Intent(mContext, NextRollOutActivity.class);
                                intent.putExtra("对方账户", ed_phone_uid.getText().toString().trim());
                                intent.putExtra("name",phoneSearchUserBean.getData().getNickname());
                                intent.putExtra("headPath",phoneSearchUserBean.getData().getAvatar());
                                intent.putExtra("uid",phoneSearchUserBean.getData().getUid());
                                intent.putExtra("money",phoneSearchUserBean.getData().getMoney());
                                startActivity(intent);
                            }else if ("Convertibility".equals(checkPhone)){
                                Intent intent = new Intent(mContext, ConvertibilityActivity.class);
                                intent.putExtra("对方账户", ed_phone_uid.getText().toString().trim());
                                intent.putExtra("name",phoneSearchUserBean.getData().getNickname());
                                intent.putExtra("headPath",phoneSearchUserBean.getData().getAvatar());
                                intent.putExtra("uid",phoneSearchUserBean.getData().getUid());
                                intent.putExtra("money",phoneSearchUserBean.getData().getMoney());
                                startActivity(intent);
                            }
                        }else if ("2" .equals( phoneSearchUserBean.getCode())) {
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
                    public void onNext(PhoneSearchUserBean phoneSearchUserBean) {
                        this.phoneSearchUserBean = phoneSearchUserBean;
                    }
                });


    }

    /**
     * 判断对方账户是否为空
     *
     * @return
     */
    private boolean checkData() {
        if (TextUtils.isEmpty(ed_phone_uid.getText().toString().trim())) {
            showToastMessage("对方账户不能为空");
            return true;
        }
        return false;
    }
}
