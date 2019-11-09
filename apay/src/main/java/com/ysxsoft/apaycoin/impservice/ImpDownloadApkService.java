package com.ysxsoft.apaycoin.impservice;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * 下载apk接口
 */
public interface ImpDownloadApkService {
    @Streaming
    @GET
    Observable<ResponseBody> down(@Url String url);
}
