package com.ysxsoft.apaycoin.view

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.webkit.WebViewClient
import com.ysxsoft.apaycoin.R
import com.ysxsoft.apaycoin.com.ActivityPageManager
import com.ysxsoft.apaycoin.com.BaseActivity
import com.ysxsoft.apaycoin.impservice.ImpNoticeDetailService
import com.ysxsoft.apaycoin.modle.NoticeDetailBean
import com.ysxsoft.apaycoin.utils.NetWork
import kotlinx.android.synthetic.main.notice_detail_layout.*
import kotlinx.android.synthetic.main.title_layout.*
import okhttp3.internal.Internal
import rx.Observer
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

/**
 * 描述：公告详情
 * 日期： 2018/11/9 0009 10:55
 * 作者： 胡
 * 公司：郑州亿生信科技有限公司
 */
class NoticeDetailActivity : BaseActivity(), View.OnClickListener {

    var nid: String? = null;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.notice_detail_layout)
        val intent = intent
        nid = intent.getStringExtra("nid")
        tv_title.setText("公告详情")
        initListener()
        initData()
    }

    private fun initData() {
        val retrofit = NetWork.getRetrofit()
        val service = retrofit.create(ImpNoticeDetailService::class.java)
        service.getCall(nid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<NoticeDetailBean> {
                    override fun onError(e: Throwable?) {
                        log(e!!.message)
                    }

                    override fun onNext(t: NoticeDetailBean?) {
                        if (t != null) {
                            if ("0".equals(t.code)) {
                                tv_content_title.text = t.data.title
                                tv_time.text = t.data.add_time
                                val webSettings = web_content.settings
                                webSettings.setJavaScriptEnabled(true)
                                webSettings.setUseWideViewPort(true)
                                webSettings.setLoadWithOverviewMode(true)
                                web_content.webViewClient = WebViewClient()
                                web_content.loadDataWithBaseURL(null, t.data.content, "text/html", "utf-8", null)
                            } else if ("2".equals(t!!.getCode())) {
                                var sp = getSharedPreferences("save_name_pwd", Context.MODE_PRIVATE).edit()
                                sp.clear();
                                sp.commit();
                                val is_first = getSharedPreferences("IS_FIRST", Context.MODE_PRIVATE).edit();
                                is_first.clear();
                                is_first.commit();
                                val instance = ActivityPageManager.getInstance();
                                instance.finishAllActivity();
                                startActivity(LoginActivity::class.java);
                                finish();
                            }
                        }
                    }

                    override fun onCompleted() {

                    }
                });
    }

    private fun initListener() {
        img_back.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.img_back -> {
                finish()
            }
        }

    }
}
