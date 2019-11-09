package com.ysxsoft.apaycoin.impservice;

import com.ysxsoft.apaycoin.modle.LoginBean;

import java.util.Map;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Query;
import rx.Observable;

/**
 * 描述： 登录的接口
 * 日期： 2018/11/6 0006 10:41
 * 作者： 胡
 * 公司：郑州亿生信科技有限公司
 */
public interface ImpLoginService {
    @GET("index/index")
    Observable<LoginBean> getLogin(@Query("username") String username,
                                   @Query("password") String password);


}
