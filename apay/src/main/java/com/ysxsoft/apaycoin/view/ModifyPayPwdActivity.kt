package com.ysxsoft.apaycoin.view

import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import com.ysxsoft.apaycoin.R
import com.ysxsoft.apaycoin.com.ActivityPageManager
import com.ysxsoft.apaycoin.com.BaseActivity
import com.ysxsoft.apaycoin.com.BaseApplication
import com.ysxsoft.apaycoin.impservice.ImpModifyLoginPwdService
import com.ysxsoft.apaycoin.impservice.ImpModifyPayPasswordService
import com.ysxsoft.apaycoin.modle.ModifyLoginPwdBean
import com.ysxsoft.apaycoin.utils.NetWork
import kotlinx.android.synthetic.main.modify_pay_pwd_layout.*
import kotlinx.android.synthetic.main.title_layout.*
import rx.Observer
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

/**
 * 描述： 修改支付密码
 * 日期： 2018/11/7 0007 11:14
 * 作者： 胡
 * 公司：郑州亿生信科技有限公司
 */
class ModifyPayPwdActivity : BaseActivity(), View.OnClickListener {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.modify_pay_pwd_layout)
        initView()
        initListener()
    }

    private fun initView() {
        tv_title.setText("修改支付密码")
    }

    private fun initListener() {
        img_back.setOnClickListener(this)
        btn_submit.setOnClickListener(this)
        tv_forget_pay_pwd.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.img_back -> {
                finish()
            }
            R.id.btn_submit -> {
//                checkData()
                if (TextUtils.isEmpty(ed_old_pwd.text.toString().trim())) {
                    showToastMessage("旧密码不能为空")
                    return
                }
                if (TextUtils.isEmpty(ed_new_pwd.text.toString().trim())) {
                    showToastMessage("新密码不能为空")
                    return
                }
                if (TextUtils.isEmpty(ed_check_new_pwd.text.toString().trim())) {
                    showToastMessage("确认密码不能为空")
                    return
                }
                if (!ed_new_pwd.text.toString().trim().equals(ed_check_new_pwd.text.toString().trim())) {
                    showToastMessage("两次输入密码不一致")
                    return
                }
                submitData()
            }
            R.id.tv_forget_pay_pwd -> {
                startActivity(ForgetPayPwdActivity::class.java)
                finish()
            }
        }

    }

    private fun submitData() {
        NetWork.getRetrofit()
                .create(ImpModifyPayPasswordService::class.java)
                .getCall(NetWork.getToken(), ed_old_pwd.text.toString().trim(), ed_new_pwd.text.toString().trim())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<ModifyLoginPwdBean> {
                    var modifyLoginPwdBean: ModifyLoginPwdBean? = null
                    override fun onError(e: Throwable?) {
                        showToastMessage(e!!.message)
                    }

                    override fun onNext(t: ModifyLoginPwdBean?) {
                        modifyLoginPwdBean = t
                    }

                    override fun onCompleted() {
                        showToastMessage(modifyLoginPwdBean!!.msg)
                        if ("0".equals(modifyLoginPwdBean!!.code)) {
                            finish()
                        } else if ("2" == modifyLoginPwdBean!!.getCode()) {
                            val sp = getSharedPreferences("save_name_pwd", Context.MODE_PRIVATE).edit()
                            sp.clear()
                            sp.commit()
                            val is_first = getSharedPreferences("IS_FIRST", Context.MODE_PRIVATE).edit()
                            is_first.clear()
                            is_first.commit()
                            val instance = ActivityPageManager.getInstance()
                            instance.finishAllActivity()
                            startActivity(LoginActivity::class.java)
                            finish()
                        }
                    }
                })

    }

    private fun checkData() {
        if (TextUtils.isEmpty(ed_old_pwd.text.toString().trim())) {
            showToastMessage("旧密码不能为空")
            return
        }
        if (TextUtils.isEmpty(ed_new_pwd.text.toString().trim())) {
            showToastMessage("新密码不能为空")
            return
        }
        if (TextUtils.isEmpty(ed_check_new_pwd.text.toString().trim())) {
            showToastMessage("确认密码不能为空")
            return
        }
        if (!ed_new_pwd.text.toString().trim().equals(ed_check_new_pwd.text.toString().trim())) {
            showToastMessage("两次输入密码不一致")
            return
        }

    }
}
