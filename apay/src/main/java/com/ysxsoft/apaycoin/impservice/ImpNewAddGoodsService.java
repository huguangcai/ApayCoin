package com.ysxsoft.apaycoin.impservice;

import com.ysxsoft.apaycoin.modle.NewAddGoodsBean;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import rx.Observable;

/**
 * 描述： 新增商品的接口
 * 日期： 2018/11/24 0024 17:36
 * 作者： 胡
 * 公司：郑州亿生信科技有限公司
 */
public interface ImpNewAddGoodsService {
    @Multipart
    @POST("shops/addPro")
    Observable<NewAddGoodsBean> getCall(@Header("token") String token,
                                        @Part("title") String title,
                                        @Part("money") String money,
                                        @Part("stock") String stock,
                                        @Part List<MultipartBody.Part> parts);

}
