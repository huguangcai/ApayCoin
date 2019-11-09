package com.ysxsoft.apaycoin.impservice;


import com.ysxsoft.apaycoin.modle.LinghongbaoBean;

import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import rx.Observable;

public interface ImpLinghongbaoService {
    @POST("pays/lingqu")
    Observable<LinghongbaoBean> getCall(@Header("token")String token);
}
