package com.ysxsoft.apaycoin.impservice;

import com.ysxsoft.apaycoin.modle.AboutMyBean;

import retrofit2.http.GET;
import rx.Observable;

/**
 * 描述： 关于我们的接口
 * 日期： 2018/11/6 0006 10:51
 * 作者： 胡
 * 公司：郑州亿生信科技有限公司
 */
public interface ImpAboutMyService {
    @GET("index/abounts")
    Observable<AboutMyBean> getAboutMy();
}
