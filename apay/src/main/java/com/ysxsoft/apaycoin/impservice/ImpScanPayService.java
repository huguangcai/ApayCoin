package com.ysxsoft.apaycoin.impservice;

import com.ysxsoft.apaycoin.modle.ScanPayBean;

import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * 描述： 扫码支付接口
 * 日期： 2018/11/23 0023 17:34
 * 作者： 胡
 * 公司：郑州亿生信科技有限公司
 */
public interface ImpScanPayService {
    @POST("pays/scanOutMoney")
    Observable<ScanPayBean> getCall(@Header("token") String token,
                                    @Query("money") String money,
                                    @Query("qrcode") String qrcode,
                                    @Query("pwd") String pwd);

}
