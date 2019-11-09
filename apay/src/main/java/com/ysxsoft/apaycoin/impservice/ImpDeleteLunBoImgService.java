package com.ysxsoft.apaycoin.impservice;

import com.ysxsoft.apaycoin.modle.DeleteAndModifyLunBoBean;

import okhttp3.MultipartBody;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import rx.Observable;

public interface ImpDeleteLunBoImgService {
    @POST("shops/problaner")
    Observable<DeleteAndModifyLunBoBean> getCall(@Header("token")String token,
                                                 @Query("pid") String pid,
                                                 @Query("type") String type
                                                 );
}
