package com.ysxsoft.apaycoin.impservice;

import com.ysxsoft.apaycoin.modle.NoticeListBean;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * 描述： 通知列表接口
 * 日期： 2018/11/8 0008 13:34
 * 作者： 胡
 * 公司：郑州亿生信科技有限公司
 */
public interface ImpNoticeListService {
    @GET("index/noticeList")
    Observable<NoticeListBean> getCall(@Query("page") String page);
}
