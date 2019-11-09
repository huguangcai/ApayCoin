package com.ysxsoft.apaycoin.impservice;

import com.ysxsoft.apaycoin.modle.DeleteInfoBean;

import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

public interface ImpPersonInfoDeleteService {
    @POST("homes/delUserMsg")
//    Observer<DeleteInfoBean> getCall(@Header("token") String token,
//                                        @Query("msg_id") String msg_id);
    Observable<DeleteInfoBean> getCall(@Header("token") String token,
                                       @Query("msg_id")String msg_id);
}
