package com.ysxsoft.apaycoin.impservice;

import com.ysxsoft.apaycoin.modle.NumAssetApayBean;

import retrofit2.http.Header;
import retrofit2.http.POST;
import rx.Observable;

/**
 * 描述： 数字资产Apay界面的接口
 * 日期： 2018/11/16 0016 10:30
 * 作者： 胡
 * 公司：郑州亿生信科技有限公司
 */
public interface ImpNumAssetApayService {
    @POST("homes/mymoney")
    Observable<NumAssetApayBean> getCall(@Header("token") String token);
}
