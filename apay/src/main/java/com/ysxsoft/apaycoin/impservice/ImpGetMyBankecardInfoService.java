package com.ysxsoft.apaycoin.impservice;

import com.ysxsoft.apaycoin.modle.GetMyBankecardInfoBean;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Header;
import retrofit2.http.POST;
import rx.Observable;

/**
 * 描述： 获取银行卡数据接口
 * 日期： 2018/11/19 0019 11:04
 * 作者： 胡
 * 公司：郑州亿生信科技有限公司
 */
public interface ImpGetMyBankecardInfoService {
    @POST("user/getMyCard")
    Observable<GetMyBankecardInfoBean> getCall(@Header("token") String token);
}
