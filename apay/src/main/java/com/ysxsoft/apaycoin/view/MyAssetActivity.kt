package com.ysxsoft.apaycoin.view

import android.os.Bundle
import android.view.View
import com.ysxsoft.apaycoin.R
import com.ysxsoft.apaycoin.com.BaseActivity
import com.ysxsoft.apaycoin.com.BaseApplication
import kotlinx.android.synthetic.main.my_asset_layout.*
import kotlinx.android.synthetic.main.title_layout.*

/**
 * 描述： 我的资产
 * 日期： 2018/11/6 0006 13:43
 * 作者： 胡
 * 公司：郑州亿生信科技有限公司
 */
class MyAssetActivity : BaseActivity(),View.OnClickListener{

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.my_asset_layout)
        initView()
        initData()
        initListener()
    }

    private fun initData() {
        tv_balance_money.setText(BaseApplication.loginBean.userinfo.money)
        tv_apay_num.setText(BaseApplication.loginBean.userinfo.score)
        tv_RMB.setText(BaseApplication.loginBean.userinfo.money)
    }
    private fun initView() {
        tv_title.setText("我的资产")
    }
    private fun initListener() {
        img_back.setOnClickListener(this)
    }
    override fun onClick(v: View?) {
        if (v != null) {
            when(v.id){
                R.id.img_back->{
                    finish()
                }
            }
        }

    }
}
