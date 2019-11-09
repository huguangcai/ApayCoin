package com.ysxsoft.apaycoin.view

import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.text.TextUtils
import android.view.Gravity
import android.view.View
import com.ysxsoft.apaycoin.R
import com.ysxsoft.apaycoin.com.ActivityPageManager
import com.ysxsoft.apaycoin.com.BaseActivity
import com.ysxsoft.apaycoin.impservice.ImpAddMyBankCardService
import com.ysxsoft.apaycoin.modle.AddMyBankCardBean
import com.ysxsoft.apaycoin.utils.NetWork
import com.ysxsoft.apaycoin.widget.paypwdpopuwindow.popwindow.SelectPopupWindow
import kotlinx.android.synthetic.main.add_bank_card.*
import kotlinx.android.synthetic.main.title_layout.*
import rx.Observer
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

/**
 *描述： 添加银行卡界面
 *日期： 2018/11/7 0007 15:22
 *作者： 胡
 *公司：郑州亿生信科技有限公司
 */
class AddBankCardActivity : BaseActivity(), View.OnClickListener, SelectPopupWindow.OnPopWindowClickListener {

    override fun onPopWindowClickListener(psw: String?, complete: Boolean) {
        if (complete) {
            submitData(psw)
        }
    }

    override fun onForgetPayPwd() {
        startActivity(ForgetPayPwdActivity::class.java)
    }

    var khh: String? = null
    var khr: String? = null
    var cardnum: String? = null
    var zhifubao: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_bank_card)
        khh = intent.getStringExtra("khh")
        khr = intent.getStringExtra("khr")
        cardnum = intent.getStringExtra("cardnum")
        zhifubao = intent.getStringExtra("zhifubao")

        initView()
        initListener()
    }

    private fun initListener() {
        img_back.setOnClickListener(this)
        btn_submit.setOnClickListener(this)
    }

    private fun initView() {
        tv_title.setText("添加银行卡")
        if (!"".equals(khh)){
            ed_belong_person.setText(khr)
            ed_card_num.setText(cardnum)
            ed_open_card_bank.setText(khh)
            ed_zhifubao.setText(zhifubao)
        }
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.img_back -> {
                finish()
            }
            R.id.btn_submit -> {
                if (TextUtils.isEmpty(ed_zhifubao.text.toString().trim())) {
                    showToastMessage("支付宝账号不能为空")
                    return
                }
                if (TextUtils.isEmpty(ed_open_card_bank.text.toString().trim())) {
                    showToastMessage("开户行不能为空")
                    return
                }
                if (TextUtils.isEmpty(ed_card_num.text.toString().trim())) {
                    showToastMessage("银行卡号不能为空")
                    return
                }
                showDialogPwd();
            }

        }
    }

    private fun showDialogPwd() {
        val menuWindow = SelectPopupWindow(this, this)
        val rect = Rect()
        window.decorView.getWindowVisibleDisplayFrame(rect)
        val winHeight = window.decorView.height
        menuWindow.showAtLocation(window.decorView, Gravity.BOTTOM, 0, winHeight - rect.bottom)
    }

    /**
     * 提交数据
     */
    private fun submitData(psw: String?) {
        val retrofit = NetWork.getRetrofit()
        val service = retrofit.create(ImpAddMyBankCardService::class.java)
        service.getMyCardInfo(NetWork.getToken(),
                ed_open_card_bank.text.toString().trim(),
                ed_belong_person.text.toString().trim(),
                ed_card_num.text.toString().trim(),
                ed_zhifubao.text.toString().trim(),
                psw)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<AddMyBankCardBean> {
                    var addMyBankCardBean: AddMyBankCardBean? = null;
                    override fun onError(e: Throwable?) {
                        log(e!!.message)
                    }

                    override fun onNext(t: AddMyBankCardBean?) {
                        addMyBankCardBean = t;

                    }

                    override fun onCompleted() {
                        showToastMessage(addMyBankCardBean!!.msg)
                        if ("0".equals(addMyBankCardBean!!.code)) {
//                            val intent = Intent(mContext, AddBankCardSucessActivity::class.java)
//                            intent.putExtra("name", ed_belong_person.text.toString().trim())
//                            intent.putExtra("bank", ed_open_card_bank.text.toString().trim())
//                            intent.putExtra("cardnum", ed_card_num.text.toString().trim())
//                            intent.putExtra("zhifubao",ed_zhifubao.text.toString().trim())
//                            startActivity(intent)
                            finish()
                        } else if ("2" == addMyBankCardBean!!.getCode()) {
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
}