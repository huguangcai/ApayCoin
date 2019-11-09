package com.ysxsoft.apaycoin.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ysxsoft.apaycoin.com.BaseApplication;
import com.ysxsoft.apaycoin.impservice.ImpDownloadApkService;
import com.ysxsoft.apaycoin.downloadapk.FileResponseBody;
import com.ysxsoft.apaycoin.downloadapk.GsonUtil;
import com.ysxsoft.apaycoin.downloadapk.NetConfig;
import com.ysxsoft.apaycoin.downloadapk.ResponseModel;
import com.ysxsoft.apaycoin.downloadapk.ServerException;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * 描述： 网络地址
 * 日期： 2018/11/6 0006 10:30
 * 作者： 胡
 * 公司：郑州亿生信科技有限公司
 */
public class NetWork {
    public static String BaseUrl = "http://ggapay.com/admin.php/api/";

    public static String getToken() {
        return BaseApplication.loginBean.getUserinfo().getSign();
    }

    public static String getFragmentToken(Context context) {
        SharedPreferences save_token = context.getSharedPreferences("save_token", Context.MODE_PRIVATE);
        String token = save_token.getString("token", "");
        return token;
    }

    public static String token;

    /**
     * 获取Retrofit对象
     *
     * @return
     */
    public static Retrofit getRetrofit() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(NetWork.BaseUrl)
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().create()))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        return retrofit;
    }

    //网络请求
    public static Retrofit getRetrofit1() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(NetWork.BaseUrl)
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().create()))
//                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        return retrofit;
    }

    private ImpDownloadApkService api;
    private volatile static NetWork INSTANCE;

    private NetWork() {
        if (api == null) {
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(NetConfig.CONNECT_TIME_OUT, TimeUnit.MILLISECONDS)
                    .build();
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
            Retrofit retrofit = new Retrofit.Builder()
                    .client(client)
                    .baseUrl(NetConfig.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .build();
            api = retrofit.create(ImpDownloadApkService.class);
        }
    }

    /**
     * 获取单例
     */
    public static NetWork getInstance() {
        if (INSTANCE == null) {
            synchronized (NetWork.class) {
                if (INSTANCE == null) {
                    INSTANCE = new NetWork();
                }
            }
        }
        return INSTANCE;
    }

    /**
     * 对结果进行预处理
     */
    private static <T> ObservableTransformer<ResponseModel, T> handleObject(final Class<T> cls) {
        return new ObservableTransformer<ResponseModel, T>() {
            @Override
            public ObservableSource<T> apply(Observable<ResponseModel> upstream) {
                return upstream
                        .flatMap(new Function<ResponseModel, ObservableSource<T>>() {
                            @Override
                            public ObservableSource<T> apply(ResponseModel responseModel) throws Exception {
                                if (responseModel.getError() == 0) {
                                    return createObject(responseModel.getData(), cls);
                                } else {
                                    return Observable.error(new ServerException(responseModel.getMsg()));
                                }
                            }
                        })
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    /**
     * 对结果进行预处理
     */
    private static <T> ObservableTransformer<ResponseModel, List<T>> handleList(final Class<T> cls) {
        return new ObservableTransformer<ResponseModel, List<T>>() {
            @Override
            public ObservableSource<List<T>> apply(Observable<ResponseModel> upstream) {
                return upstream
                        .flatMap(new Function<ResponseModel, ObservableSource<List<T>>>() {
                            @Override
                            public ObservableSource<List<T>> apply(ResponseModel responseModel) throws Exception {
                                if (responseModel.getError() == 0) {
                                    return createList(responseModel.getData(), cls);
                                } else {
                                    return Observable.error(new ServerException(responseModel.getMsg()));
                                }
                            }
                        })
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    /**
     * 创建成功的数据
     */
    private static <T> Observable<T> createObject(final String data, final Class<T> cls) {
        return Observable.create(new ObservableOnSubscribe<T>() {
            @Override
            public void subscribe(ObservableEmitter<T> emitter) throws Exception {
                try {
                    emitter.onNext(GsonUtil.GsonToBean(data, cls));
                    emitter.onComplete();
                } catch (Exception e) {
                    emitter.onError(e);
                }
            }
        });
    }

    /**
     * 创建成功的数据
     */
    private static <T> Observable<List<T>> createList(final String data, final Class<T> cls) {
        return Observable.create(new ObservableOnSubscribe<List<T>>() {
            @Override
            public void subscribe(ObservableEmitter<List<T>> emitter) throws Exception {
                try {
                    emitter.onNext(GsonUtil.GsonToList(data, cls));
                    emitter.onComplete();
                } catch (Exception e) {
                    emitter.onError(e);
                }
            }
        });
    }

    public Observable<ResponseBody> down(String url) {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(NetConfig.CONNECT_TIME_OUT, TimeUnit.MILLISECONDS)
                .addNetworkInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Response originalResponse = chain.proceed(chain.request());
                        return originalResponse
                                .newBuilder()
                                .body(new FileResponseBody(originalResponse))//将自定义的ResposeBody设置给它
                                .build();
                    }
                })
                .build();

        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

        Retrofit retrofit = new Retrofit.Builder()
                .client(client)
                .baseUrl(NetConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        ImpDownloadApkService api = retrofit.create(ImpDownloadApkService.class);
        return api.down(url);
    }


}
