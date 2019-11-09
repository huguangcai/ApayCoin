package com.ysxsoft.apaycoin.view

import android.content.Context
import android.os.Bundle
import android.view.View
import com.ysxsoft.apaycoin.R
import com.ysxsoft.apaycoin.com.BaseActivity
import kotlinx.android.synthetic.main.pay_sucessfull.*
import kotlinx.android.synthetic.main.title_layout.*

/**
 * 描述： 支付成功
 * 日期： 2018/11/6 0006 17:42
 * 作者： 胡
 * 公司：郑州亿生信科技有限公司
 */
class PaySuccessActivity : BaseActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pay_sucessfull)
        initView()
        initListener()
    }

    private fun initListener() {
        img_back.setOnClickListener(this)
        btn_look_my_mall.setOnClickListener(this)
    }

    private fun initView() {
        tv_title.setText("支付成功")
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.img_back -> {
                finish()
            }
            R.id.btn_look_my_mall -> {
            startActivity(LookMyMallActivity::class.java)
            }
        }
    }
}
