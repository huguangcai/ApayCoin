package com.ysxsoft.apaycoin.impservice;

import com.ysxsoft.apaycoin.modle.GoodsDetialDeleteBean;

import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

public interface ImpGoodsDetialDeleteService {
    @POST("shops/delpro")
    Observable<GoodsDetialDeleteBean> getCall(@Header("token")String token,
                                              @Query("pid")String pid);
}
