package com.ysxsoft.apaycoin.impservice;

import com.ysxsoft.apaycoin.modle.NoticeDetailBean;

import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * 描述： 通知详情接口
 * 日期： 2018/11/8 0008 13:36
 * 作者： 胡
 * 公司：郑州亿生信科技有限公司
 */
public interface ImpNoticeDetailService {
    @POST("index/noticeDea")
    Observable<NoticeDetailBean> getCall(@Query("nid") String nid);
}
