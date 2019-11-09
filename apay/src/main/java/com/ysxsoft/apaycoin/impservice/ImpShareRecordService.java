package com.ysxsoft.apaycoin.impservice;

import com.ysxsoft.apaycoin.modle.ShareRecordBean;

import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

public interface ImpShareRecordService {
    @POST("user/myyaoqing")
    Observable<ShareRecordBean> getCall(@Header("token")String token,
                                        @Query("page")String page);
}
