package com.ysxsoft.apaycoin.impservice;

import com.ysxsoft.apaycoin.modle.OnlineMallGoodsListBean;

import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

public interface ImpSearchGoodsService {
    @POST("products/index")
    Observable<OnlineMallGoodsListBean> getCall(@Header("token")String token,
                                                @Query("page")String page,
                                                @Query("search")String search);
}
