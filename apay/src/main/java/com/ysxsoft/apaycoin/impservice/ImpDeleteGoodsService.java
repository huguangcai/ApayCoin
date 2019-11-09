package com.ysxsoft.apaycoin.impservice;

import com.ysxsoft.apaycoin.modle.DeleteGoodsBean;

import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

public interface ImpDeleteGoodsService {
    @POST("shops/prodel")
    Observable<DeleteGoodsBean> getCall(@Header("token")String token,
                                        @Query("pid")String pid);
}
