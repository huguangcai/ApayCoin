package com.ysxsoft.apaycoin.impservice;

import com.ysxsoft.apaycoin.modle.ModifyLoginPwdBean;

import okhttp3.MultipartBody;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import rx.Observable;

/**
 * 描述： 修改个人头像接口
 * 日期： 2018/11/10 0010 13:38
 * 作者： 胡
 * 公司：郑州亿生信科技有限公司
 */
public interface ImpModifyPersonDataService {
    @Multipart
    @POST("user/setUser")
    Observable<ModifyLoginPwdBean> getCall(@Header("token") String token,
                                           @Part MultipartBody.Part file);
}
