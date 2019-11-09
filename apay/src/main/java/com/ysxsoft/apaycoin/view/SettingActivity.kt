package com.ysxsoft.apaycoin.view

import android.os.Bundle
import android.view.View
import com.ysxsoft.apaycoin.R
import com.ysxsoft.apaycoin.com.BaseActivity
import kotlinx.android.synthetic.main.setting_layout.*
import kotlinx.android.synthetic.main.title_layout.*

/**
 * 描述： 设置界面
 * 日期： 2018/11/5 0005 10:45
 * 作者： 胡
 * 公司：郑州亿生信科技有限公司
 */
class SettingActivity : BaseActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.setting_layout)
        initView()
        initListener()
    }

    private fun initListener() {
        img_back.setOnClickListener(this)
        tv_modify_login_pwd.setOnClickListener(this)
        tv_modify_pay_pwd.setOnClickListener(this)

    }

    private fun initView() {
       tv_title.setText("设置")
    }

    override fun onClick(v: View) {
        when(v.id){
            R.id.img_back->{
                finish()
            }
            R.id.tv_modify_login_pwd->{
                startActivity(ModifyLoginPwdActivity::class.java)
                finish()
            }
            R.id.tv_modify_pay_pwd->{
                startActivity(ModifyPayPwdActivity::class.java)
                finish()
            }
        }
    }
}
