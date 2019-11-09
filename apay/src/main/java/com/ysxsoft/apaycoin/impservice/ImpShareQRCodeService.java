package com.ysxsoft.apaycoin.impservice;

import com.ysxsoft.apaycoin.modle.ShareQrCodeBean;

import retrofit2.http.Header;
import retrofit2.http.POST;
import rx.Observable;

/**
 * 描述： 分享二维码界面
 * 日期： 2018/11/8 0008 12:01
 * 作者： 胡
 * 公司：郑州亿生信科技有限公司
 */
public interface ImpShareQRCodeService {
    @POST("user/getQrcode")
    Observable<ShareQrCodeBean> getCall(@Header("token") String token);

}
