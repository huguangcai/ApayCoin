package com.ysxsoft.apaycoin.impservice;

import com.ysxsoft.apaycoin.modle.PhoneSearchUserBean;

import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

public interface ImpPhoneSearchUserService {
    @POST("pays/seachUser")
    Observable<PhoneSearchUserBean> getCall(@Header("token")String token,
                                            @Query("search")String search);

}
