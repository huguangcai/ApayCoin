package com.ysxsoft.apaycoin.view

import android.os.Bundle
import android.view.View
import com.ysxsoft.apaycoin.R
import com.ysxsoft.apaycoin.com.BaseActivity
import kotlinx.android.synthetic.main.add_bank_card_sucessful.*
import kotlinx.android.synthetic.main.title_layout.*

/**
 *描述： 添加银行卡成功界面
 *日期： 2018/11/7 0007 15:42
 *作者： 胡
 *公司：郑州亿生信科技有限公司
 */
class AddBankCardSucessActivity : BaseActivity(), View.OnClickListener {

    var name: String? = null
    var bank: String? = null
    var cardnum: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_bank_card_sucessful)
        initIntent()
        initView()
        initListener()
    }

    private fun initListener() {
        img_back.setOnClickListener(this)
        tv_edit.setOnClickListener(this)
    }

    fun initIntent() {
        name = intent.getStringExtra("name")
        bank = intent.getStringExtra("bank")
        cardnum = intent.getStringExtra("cardnum")
    }

    fun initView() {
        tv_title.setText("我的银行开卡")
        tv_open_bank.setText(bank)
        tv_name.setText(name)
        tv_card_num.setText(cardnum)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.tv_edit -> {
                finish()
            }
            R.id.img_back -> {
                finish()
            }
        }

    }
}