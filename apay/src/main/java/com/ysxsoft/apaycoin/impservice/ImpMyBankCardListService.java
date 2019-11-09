package com.ysxsoft.apaycoin.impservice;

import android.database.Observable;

import com.ysxsoft.apaycoin.modle.MyBankCardListBean;

import retrofit2.http.Header;
import retrofit2.http.POST;

/**
 * 描述： 银行卡列表接口
 * 日期： 2018/11/8 0008 11:46
 * 作者： 胡
 * 公司：郑州亿生信科技有限公司
 */
@Deprecated
public interface ImpMyBankCardListService {
    @POST("user/getMyCard")
    Observable<MyBankCardListBean> getBankCardListInfo(@Header("token") String token);
}
