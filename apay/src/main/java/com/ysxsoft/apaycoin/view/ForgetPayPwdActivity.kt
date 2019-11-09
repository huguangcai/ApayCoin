package com.ysxsoft.apaycoin.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import com.ysxsoft.apaycoin.R
import com.ysxsoft.apaycoin.com.ActivityPageManager
import com.ysxsoft.apaycoin.com.BaseActivity
import com.ysxsoft.apaycoin.impservice.ImpForgetLoginPayPwdService
import com.ysxsoft.apaycoin.impservice.ImpIdentifyingCodeService
import com.ysxsoft.apaycoin.modle.ForgetLoginPayPwdBean
import com.ysxsoft.apaycoin.modle.IdentifyingCodeBean
import com.ysxsoft.apaycoin.utils.AppUtil
import com.ysxsoft.apaycoin.utils.CountDownTimeHelper
import com.ysxsoft.apaycoin.utils.NetWork
import kotlinx.android.synthetic.main.forget_pay_pwd_layout.*
import kotlinx.android.synthetic.main.title_layout.*
import rx.Observer
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

/**
 *描述： 忘记支付密码
 *日期： 2018/11/7 0007 11:36
 *作者： 胡
 *公司：郑州亿生信科技有限公司
 */
class ForgetPayPwdActivity : BaseActivity(), View.OnClickListener {
    var type = 2
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.forget_pay_pwd_layout)
        initView()
        initListener()
    }

    private fun initView() {
        tv_title.setText("忘记支付密码")

    }

    private fun initListener() {
        img_back.setOnClickListener(this)
        get_identifying_code.setOnClickListener(this)
        btn_submit.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.img_back -> {
                AppUtil.colsePhoneKeyboard(this)
                finish()
            }
            R.id.get_identifying_code -> {
                if (checkPhoneNum()) return
                val helper = CountDownTimeHelper(60, get_identifying_code)
                sendMessage()
            }
            R.id.btn_submit -> {
                checkPhoneNum()
//                checkData()
                if (TextUtils.isEmpty(ed_identifying_code.text.toString().trim())) {
                    showToastMessage("验证码不能为空")
                    return
                }
                if (TextUtils.isEmpty(ed_setting_login_pwd.text.toString().trim())) {
                    showToastMessage("设置密码不能为空")
                    return
                }
                if (TextUtils.isEmpty(ed_second_login_pwd.text.toString().trim())) {
                    showToastMessage("确认密码不能为空")
                    return
                }
                if (!ed_setting_login_pwd.text.toString().trim().equals(ed_second_login_pwd.text.toString().trim())) {
                    showToastMessage("两次输入密码不一致")
                    return
                }
                submitData()
            }
        }
    }

    private fun submitData() {
        NetWork.getRetrofit()
                .create(ImpForgetLoginPayPwdService::class.java)
                .getCall(ed_phone_num.text.toString().trim(), ed_identifying_code.text.toString().trim(),
                        ed_second_login_pwd.text.toString().trim(), type.toString())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<ForgetLoginPayPwdBean> {
                    private var forgetLoginPayPwdBean: ForgetLoginPayPwdBean? = null

                    override fun onCompleted() {
                        showToastMessage(forgetLoginPayPwdBean!!.msg)
                        if ("0".equals(forgetLoginPayPwdBean!!.code)){
                            finish()
                        } else if ("2" == forgetLoginPayPwdBean!!.getCode()) {
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

                    override fun onError(e: Throwable) {
                        showToastMessage(e.message)
                    }

                    override fun onNext(forgetLoginPayPwdBean: ForgetLoginPayPwdBean) {
                        this.forgetLoginPayPwdBean = forgetLoginPayPwdBean
                    }
                })

    }

    fun checkPhoneNum(): Boolean {
        if (TextUtils.isEmpty(ed_phone_num.text.toString().trim())) {
            showToastMessage("手机号不能为空")
            return true
        }
        if (!AppUtil.checkPhoneNum(ed_phone_num.text.toString().trim())) {
            showToastMessage("请输入正确的手机号")
            return true
        }
        return false
    }

    /**
     * 发短信
     */
    private fun sendMessage() {
        NetWork.getRetrofit()
                .create(ImpIdentifyingCodeService::class.java)
                .getIdentifyingCode(ed_phone_num.text.toString().trim())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<IdentifyingCodeBean> {
                    var identifying: IdentifyingCodeBean? = null
                    override fun onError(e: Throwable?) {
                        showToastMessage(e!!.message)
                    }

                    override fun onNext(t: IdentifyingCodeBean?) {
                        identifying = t
                    }

                    override fun onCompleted() {
                        showToastMessage(identifying!!.msg)
                        if ("0" == identifying!!.getCode()) {
//                            val helper = CountDownTimeHelper(60, get_identifying_code)
                        } else if ("2" === identifying!!.getCode()) {
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
                });
    }

    /**
     * 核对信息
     */
    private fun checkData() {
        if (TextUtils.isEmpty(ed_identifying_code.text.toString().trim())) {
            showToastMessage("验证码不能为空")
            return
        }
        if (TextUtils.isEmpty(ed_setting_login_pwd.text.toString().trim())) {
            showToastMessage("设置密码不能为空")
            return
        }
        if (TextUtils.isEmpty(ed_second_login_pwd.text.toString().trim())) {
            showToastMessage("确认密码不能为空")
            return
        }
        if (!ed_setting_login_pwd.text.toString().trim().equals(ed_second_login_pwd.text.toString().trim())) {
            showToastMessage("两次输入密码不一致")
            return
        }

    }
}

