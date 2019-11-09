package com.ysxsoft.apaycoin.view;

import android.Manifest;
import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.permissions.RxPermissions;
import com.luck.picture.lib.tools.PictureFileUtils;
import com.ysxsoft.apaycoin.R;
import com.ysxsoft.apaycoin.banner.GlideImageLoader;
import com.ysxsoft.apaycoin.com.ActivityPageManager;
import com.ysxsoft.apaycoin.com.BaseActivity;
import com.ysxsoft.apaycoin.impservice.ImpAddLunBoImageService;
import com.ysxsoft.apaycoin.impservice.ImpDeleteAndModifyLunBoService;
import com.ysxsoft.apaycoin.impservice.ImpDeleteLunBoImgService;
import com.ysxsoft.apaycoin.impservice.ImpGoodsDetialService;
import com.ysxsoft.apaycoin.impservice.ImpMallGoodsDetialHeaderService;
import com.ysxsoft.apaycoin.modle.AddLunBoImageBean;
import com.ysxsoft.apaycoin.modle.DeleteAndModifyLunBoBean;
import com.ysxsoft.apaycoin.modle.GoodsDetialBean;
import com.ysxsoft.apaycoin.modle.MallGoodsDetialHeaderBean;
import com.ysxsoft.apaycoin.utils.ImageLoadUtil;
import com.ysxsoft.apaycoin.utils.NetWork;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import io.reactivex.disposables.Disposable;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class EditorNewAddGoodsActivity extends BaseActivity implements View.OnClickListener {

    private View img_back;
    private TextView tv_title;
    private LinearLayout ll_1, ll_2, ll_3, ll_4, ll_5, ll_6;
    private ImageView img_1, img_2, img_3, img_4, img_5, img_6;
    private ImageView img_delete_1, img_delete_2, img_delete_3, img_delete_4, img_delete_5, img_delete_6;
    private TextView tv_1, tv_2, tv_3, tv_4, tv_5, tv_6;
    private EditText ed_goods_name, ed_goods_price, ed_stock_num;
    private Button btn_save_next;
    private List<LocalMedia> localMedia;
    private Map<String, File> map = new HashMap<>();
    private String path1, path2, path3, path4, path5, path6;
    private String pid;
    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    private String img_id1, img_id2, img_id3, img_id4, img_id5, img_id6;
    private String mallName, price, stock;
    private int size;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.business_new_add_goods_upload_layout);
        Intent intent = getIntent();
        pid = intent.getStringExtra("pid");
        mallName = intent.getStringExtra("mallName");
        price = intent.getStringExtra("price");
        stock = intent.getStringExtra("stock");
        initView();
        getCycleViewData();
        initListener();
    }

    private void getCycleViewData() {
        NetWork.getRetrofit()
                .create(ImpMallGoodsDetialHeaderService.class)
                .getCall(NetWork.getToken(), pid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<MallGoodsDetialHeaderBean>() {
                    private MallGoodsDetialHeaderBean mallGoodsDetialHeaderBean;

                    @Override
                    public void onCompleted() {
                        showToastMessage(mallGoodsDetialHeaderBean.getMsg());
                        if ("0".equals(mallGoodsDetialHeaderBean.getCode())) {
                            ArrayList<MallGoodsDetialHeaderBean.DataBean.LunBo> lunbo = mallGoodsDetialHeaderBean.getData().getLunbo();
                            size = lunbo.size();
                            if (size == 1) {
                                img_1.setScaleType(ImageView.ScaleType.FIT_XY);
                                tv_1.setVisibility(View.GONE);
                                img_1.setLayoutParams(params);
                                img_delete_1.setVisibility(View.VISIBLE);
                                img_id1 = lunbo.get(0).getImg_id();
                                ImageLoadUtil.GlideImageLoad(mContext, lunbo.get(0).getIcon(), img_1);
                                return;
                            }
                            if (size == 2) {
                                img_1.setScaleType(ImageView.ScaleType.FIT_XY);
                                img_2.setScaleType(ImageView.ScaleType.FIT_XY);
                                tv_1.setVisibility(View.GONE);
                                tv_2.setVisibility(View.GONE);
                                img_1.setLayoutParams(params);
                                img_2.setLayoutParams(params);
                                img_delete_1.setVisibility(View.VISIBLE);
                                img_delete_2.setVisibility(View.VISIBLE);
                                img_id1 = lunbo.get(0).getImg_id();
                                ImageLoadUtil.GlideImageLoad(mContext, lunbo.get(0).getIcon(), img_1);
                                img_id2 = lunbo.get(1).getImg_id();
                                ImageLoadUtil.GlideImageLoad(mContext, lunbo.get(1).getIcon(), img_2);
                                return;
                            }
                            if (size == 3) {
                                img_1.setScaleType(ImageView.ScaleType.FIT_XY);
                                img_2.setScaleType(ImageView.ScaleType.FIT_XY);
                                img_3.setScaleType(ImageView.ScaleType.FIT_XY);
                                tv_1.setVisibility(View.GONE);
                                tv_2.setVisibility(View.GONE);
                                tv_3.setVisibility(View.GONE);
                                img_1.setLayoutParams(params);
                                img_2.setLayoutParams(params);
                                img_3.setLayoutParams(params);
                                img_delete_1.setVisibility(View.VISIBLE);
                                img_delete_2.setVisibility(View.VISIBLE);
                                img_delete_3.setVisibility(View.VISIBLE);
                                img_id1 = lunbo.get(0).getImg_id();
                                ImageLoadUtil.GlideImageLoad(mContext, lunbo.get(0).getIcon(), img_1);
                                img_id2 = lunbo.get(1).getImg_id();
                                ImageLoadUtil.GlideImageLoad(mContext, lunbo.get(1).getIcon(), img_2);
                                img_id3 = lunbo.get(2).getImg_id();
                                ImageLoadUtil.GlideImageLoad(mContext, lunbo.get(2).getIcon(), img_3);
                                return;
                            }
                            if (size == 4) {
                                img_1.setScaleType(ImageView.ScaleType.FIT_XY);
                                img_2.setScaleType(ImageView.ScaleType.FIT_XY);
                                img_3.setScaleType(ImageView.ScaleType.FIT_XY);
                                img_4.setScaleType(ImageView.ScaleType.FIT_XY);
                                tv_1.setVisibility(View.GONE);
                                tv_2.setVisibility(View.GONE);
                                tv_3.setVisibility(View.GONE);
                                tv_4.setVisibility(View.GONE);
                                img_1.setLayoutParams(params);
                                img_2.setLayoutParams(params);
                                img_3.setLayoutParams(params);
                                img_4.setLayoutParams(params);
                                img_delete_1.setVisibility(View.VISIBLE);
                                img_delete_2.setVisibility(View.VISIBLE);
                                img_delete_3.setVisibility(View.VISIBLE);
                                img_delete_4.setVisibility(View.VISIBLE);
                                img_id1 = lunbo.get(0).getImg_id();
                                ImageLoadUtil.GlideImageLoad(mContext, lunbo.get(0).getIcon(), img_1);
                                img_id2 = lunbo.get(1).getImg_id();
                                ImageLoadUtil.GlideImageLoad(mContext, lunbo.get(1).getIcon(), img_2);
                                img_id3 = lunbo.get(2).getImg_id();
                                ImageLoadUtil.GlideImageLoad(mContext, lunbo.get(2).getIcon(), img_3);
                                img_id4 = lunbo.get(3).getImg_id();
                                ImageLoadUtil.GlideImageLoad(mContext, lunbo.get(3).getIcon(), img_4);
                                return;
                            }
                            if (size == 5) {
                                img_1.setScaleType(ImageView.ScaleType.FIT_XY);
                                img_2.setScaleType(ImageView.ScaleType.FIT_XY);
                                img_3.setScaleType(ImageView.ScaleType.FIT_XY);
                                img_4.setScaleType(ImageView.ScaleType.FIT_XY);
                                img_5.setScaleType(ImageView.ScaleType.FIT_XY);
                                tv_1.setVisibility(View.GONE);
                                tv_2.setVisibility(View.GONE);
                                tv_3.setVisibility(View.GONE);
                                tv_4.setVisibility(View.GONE);
                                tv_5.setVisibility(View.GONE);
                                img_1.setLayoutParams(params);
                                img_2.setLayoutParams(params);
                                img_3.setLayoutParams(params);
                                img_4.setLayoutParams(params);
                                img_5.setLayoutParams(params);
                                img_delete_1.setVisibility(View.VISIBLE);
                                img_delete_2.setVisibility(View.VISIBLE);
                                img_delete_3.setVisibility(View.VISIBLE);
                                img_delete_4.setVisibility(View.VISIBLE);
                                img_delete_5.setVisibility(View.VISIBLE);
                                img_id1 = lunbo.get(0).getImg_id();
                                ImageLoadUtil.GlideImageLoad(mContext, lunbo.get(0).getIcon(), img_1);
                                img_id2 = lunbo.get(1).getImg_id();
                                ImageLoadUtil.GlideImageLoad(mContext, lunbo.get(1).getIcon(), img_2);
                                img_id3 = lunbo.get(2).getImg_id();
                                ImageLoadUtil.GlideImageLoad(mContext, lunbo.get(2).getIcon(), img_3);
                                img_id4 = lunbo.get(3).getImg_id();
                                ImageLoadUtil.GlideImageLoad(mContext, lunbo.get(3).getIcon(), img_4);
                                img_id5 = lunbo.get(4).getImg_id();
                                ImageLoadUtil.GlideImageLoad(mContext, lunbo.get(4).getIcon(), img_5);
                                return;
                            }
                            if (size == 6) {
                                img_1.setScaleType(ImageView.ScaleType.FIT_XY);
                                img_2.setScaleType(ImageView.ScaleType.FIT_XY);
                                img_3.setScaleType(ImageView.ScaleType.FIT_XY);
                                img_4.setScaleType(ImageView.ScaleType.FIT_XY);
                                img_5.setScaleType(ImageView.ScaleType.FIT_XY);
                                img_6.setScaleType(ImageView.ScaleType.FIT_XY);
                                tv_1.setVisibility(View.GONE);
                                tv_2.setVisibility(View.GONE);
                                tv_3.setVisibility(View.GONE);
                                tv_4.setVisibility(View.GONE);
                                tv_5.setVisibility(View.GONE);
                                tv_6.setVisibility(View.GONE);
                                img_1.setLayoutParams(params);
                                img_2.setLayoutParams(params);
                                img_3.setLayoutParams(params);
                                img_4.setLayoutParams(params);
                                img_5.setLayoutParams(params);
                                img_6.setLayoutParams(params);
                                img_delete_1.setVisibility(View.VISIBLE);
                                img_delete_2.setVisibility(View.VISIBLE);
                                img_delete_3.setVisibility(View.VISIBLE);
                                img_delete_4.setVisibility(View.VISIBLE);
                                img_delete_5.setVisibility(View.VISIBLE);
                                img_delete_6.setVisibility(View.VISIBLE);
                                img_id1 = lunbo.get(0).getImg_id();
                                ImageLoadUtil.GlideImageLoad(mContext, lunbo.get(0).getIcon(), img_1);
                                img_id2 = lunbo.get(1).getImg_id();
                                ImageLoadUtil.GlideImageLoad(mContext, lunbo.get(1).getIcon(), img_2);
                                img_id3 = lunbo.get(2).getImg_id();
                                ImageLoadUtil.GlideImageLoad(mContext, lunbo.get(2).getIcon(), img_3);
                                img_id4 = lunbo.get(3).getImg_id();
                                ImageLoadUtil.GlideImageLoad(mContext, lunbo.get(3).getIcon(), img_4);
                                img_id5 = lunbo.get(4).getImg_id();
                                ImageLoadUtil.GlideImageLoad(mContext, lunbo.get(4).getIcon(), img_5);
                                img_id6 = lunbo.get(5).getImg_id();
                                ImageLoadUtil.GlideImageLoad(mContext, lunbo.get(5).getIcon(), img_6);
                                return;
                            }
                        }else if ("2" == mallGoodsDetialHeaderBean.getCode()) {
                            SharedPreferences.Editor sp = getSharedPreferences("save_name_pwd", Context.MODE_PRIVATE).edit();
                            sp.clear();
                            sp.commit();
                            SharedPreferences.Editor is_first = getSharedPreferences("IS_FIRST", Context.MODE_PRIVATE).edit();
                            is_first.clear();
                            is_first.commit();
                            ActivityPageManager   instance = ActivityPageManager.getInstance();
                            instance.finishAllActivity();
                            startActivity(LoginActivity.class);
                            finish();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        showToastMessage(e.getMessage());
                    }

                    @Override
                    public void onNext(MallGoodsDetialHeaderBean mallGoodsDetialHeaderBean) {
                        this.mallGoodsDetialHeaderBean = mallGoodsDetialHeaderBean;
                    }
                });
    }

    private void initView() {
        img_back = getViewById(R.id.img_back);
        tv_title = getViewById(R.id.tv_title);
        tv_title.setText("新增商品");
        ll_1 = getViewById(R.id.ll_1);
        ll_2 = getViewById(R.id.ll_2);
        ll_3 = getViewById(R.id.ll_3);
        ll_4 = getViewById(R.id.ll_4);
        ll_5 = getViewById(R.id.ll_5);
        ll_6 = getViewById(R.id.ll_6);
        img_1 = getViewById(R.id.img_1);
        img_2 = getViewById(R.id.img_2);
        img_3 = getViewById(R.id.img_3);
        img_4 = getViewById(R.id.img_4);
        img_5 = getViewById(R.id.img_5);
        img_6 = getViewById(R.id.img_6);
        img_delete_1 = getViewById(R.id.img_delete_1);
        img_delete_2 = getViewById(R.id.img_delete_2);
        img_delete_3 = getViewById(R.id.img_delete_3);
        img_delete_4 = getViewById(R.id.img_delete_4);
        img_delete_5 = getViewById(R.id.img_delete_5);
        img_delete_6 = getViewById(R.id.img_delete_6);
        tv_1 = getViewById(R.id.tv_1);
        tv_2 = getViewById(R.id.tv_2);
        tv_3 = getViewById(R.id.tv_3);
        tv_4 = getViewById(R.id.tv_4);
        tv_5 = getViewById(R.id.tv_5);
        tv_6 = getViewById(R.id.tv_6);
        ed_goods_name = getViewById(R.id.ed_goods_name);
        ed_goods_price = getViewById(R.id.ed_goods_price);
        ed_stock_num = getViewById(R.id.ed_stock_num);
        btn_save_next = getViewById(R.id.btn_save_next);
        if (!TextUtils.isEmpty(mallName)){
            ed_goods_name.setText(mallName);
            ed_goods_price.setText(price);
            ed_stock_num.setText(stock);
        }

    }

    private void initListener() {
        img_back.setOnClickListener(this);
        ll_1.setOnClickListener(this);
        ll_2.setOnClickListener(this);
        ll_3.setOnClickListener(this);
        ll_4.setOnClickListener(this);
        ll_5.setOnClickListener(this);
        ll_6.setOnClickListener(this);
        img_delete_1.setOnClickListener(this);
        img_delete_2.setOnClickListener(this);
        img_delete_3.setOnClickListener(this);
        img_delete_4.setOnClickListener(this);
        img_delete_5.setOnClickListener(this);
        img_delete_6.setOnClickListener(this);
        btn_save_next.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        switch (v.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.ll_1:
                openGallery(PictureConfig.CHOOSE_REQUEST);
                break;
            case R.id.ll_2:
                openGallery(PictureConfig.REQUEST_CAMERA);
                break;
            case R.id.ll_3:
                openGallery(PictureConfig.CAMERA);
                break;
            case R.id.ll_4:
                openGallery(PictureConfig.CLOSE_PREVIEW_FLAG);
                break;
            case R.id.ll_5:
                openGallery(PictureConfig.READ_EXTERNAL_STORAGE);
                break;
            case R.id.ll_6:
                openGallery(PictureConfig.UPDATE_FLAG);
                break;
            case R.id.img_delete_1:
                if (size<=1){//返回轮播图的集合数据集
                    showToastMessage("该图片不能删除");
                    return;
                }
                tv_1.setVisibility(View.VISIBLE);
                ImageLoadUtil.NewGoodsGlideImageLoad(mContext, "", img_1);
                img_1.setLayoutParams(params);
                img_delete_1.setVisibility(View.GONE);
                Iterator<String> iterator1 = map.keySet().iterator();
                DeleteData(img_id1);
                break;
            case R.id.img_delete_2:
                if (size<=1){//返回轮播图的集合数据集
                    showToastMessage("该图片不能删除");
                    return;
                }
                tv_2.setVisibility(View.VISIBLE);
                ImageLoadUtil.NewGoodsGlideImageLoad(mContext, "", img_2);
                img_2.setLayoutParams(params);
                img_delete_2.setVisibility(View.GONE);
                Iterator<String> iterator2 = map.keySet().iterator();
                DeleteData(img_id2);
                break;
            case R.id.img_delete_3:
                if (size<=1){//返回轮播图的集合数据集
                    showToastMessage("该图片不能删除");
                    return;
                }
                tv_3.setVisibility(View.VISIBLE);
                ImageLoadUtil.NewGoodsGlideImageLoad(mContext, "", img_3);
                img_3.setLayoutParams(params);
                img_delete_3.setVisibility(View.GONE);
                Iterator<String> iterator3 = map.keySet().iterator();
                DeleteData(img_id3);
                break;
            case R.id.img_delete_4:
                if (size<=1){//返回轮播图的集合数据集
                    showToastMessage("该图片不能删除");
                    return;
                }
                tv_4.setVisibility(View.VISIBLE);
                ImageLoadUtil.NewGoodsGlideImageLoad(mContext, "", img_4);
                img_4.setLayoutParams(params);
                img_delete_4.setVisibility(View.GONE);
                Iterator<String> iterator4 = map.keySet().iterator();
                DeleteData(img_id4);
                break;
            case R.id.img_delete_5:
                if (size<=1){//返回轮播图的集合数据集
                    showToastMessage("该图片不能删除");
                    return;
                }
                tv_5.setVisibility(View.VISIBLE);
                ImageLoadUtil.NewGoodsGlideImageLoad(mContext, "", img_5);
                img_5.setLayoutParams(params);
                img_delete_5.setVisibility(View.GONE);
                Iterator<String> iterator5 = map.keySet().iterator();
                DeleteData(img_id5);
                break;
            case R.id.img_delete_6:
                if (size<=1){//返回轮播图的集合数据集
                    showToastMessage("该图片不能删除");
                    return;
                }
                tv_6.setVisibility(View.VISIBLE);
                ImageLoadUtil.NewGoodsGlideImageLoad(mContext, "", img_6);
                img_6.setLayoutParams(params);
                img_delete_6.setVisibility(View.GONE);
                Iterator<String> iterator6 = map.keySet().iterator();
//                if (!("").equals(img_id6)) {
                DeleteData(img_id6);
//            /*    } else {
//                    while (iterator6.hasNext()) {
//                        String key = iterator6.next();
//                        if ("6".equals(key)) {
//                            iterator6.remove();
//                        }
//                    }
//                }*/
                break;
            case R.id.btn_save_next:
                modifyData();
                break;
        }
    }

    /**
     * 删除图片
     *
     * @param pid
     */
    private void DeleteData(String pid) {
        NetWork.getRetrofit()
                .create(ImpDeleteLunBoImgService.class)
                .getCall(NetWork.getToken(), pid, "1")//type		1删除2修改
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<DeleteAndModifyLunBoBean>() {
                    private DeleteAndModifyLunBoBean deleteAndModifyLunBoBean;

                    @Override
                    public void onCompleted() {
                        showToastMessage(deleteAndModifyLunBoBean.getMsg());
                        if ("0".equals(deleteAndModifyLunBoBean.getCode())){
                            Intent intent=new Intent("ICON_PATH");
                            intent.putExtra("icon_path","deleteImg");
                            sendBroadcast(intent);
                        }else  if ("2".equals(deleteAndModifyLunBoBean.getCode())) {
                            SharedPreferences.Editor sp =getSharedPreferences("save_name_pwd", Context.MODE_PRIVATE).edit();
                            sp.clear();
                            sp.commit();
                            SharedPreferences.Editor is_first =getSharedPreferences("IS_FIRST", Context.MODE_PRIVATE).edit();
                            is_first.clear();
                            is_first.commit();
                            ActivityPageManager instance = ActivityPageManager.getInstance();
                            instance.finishAllActivity();
                            startActivity(LoginActivity.class);
                            finish();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        showToastMessage(e.getMessage());
                    }

                    @Override
                    public void onNext(DeleteAndModifyLunBoBean deleteAndModifyLunBoBean) {
                        this.deleteAndModifyLunBoBean = deleteAndModifyLunBoBean;
                    }
                });
    }

    /**
     * 从店铺详情界面跳转过来  修改数据
     */
    private void modifyData() {
        NetWork.getRetrofit()
                .create(ImpGoodsDetialService.class)
                .getCall(NetWork.getToken(),
                        ed_goods_name.getText().toString().trim(),
                        ed_goods_price.getText().toString().trim(),
                        ed_stock_num.getText().toString().trim(), pid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<GoodsDetialBean>() {
                    private GoodsDetialBean goodsDetialBean;

                    @Override
                    public void onCompleted() {
                        showToastMessage(goodsDetialBean.getMsg());
                        if ("0".equals(goodsDetialBean.getCode())) {
                            Intent intent = new Intent(mContext, NewAddGoodsEditorActivity.class);
                            intent.putExtra("pid", pid);
                            startActivity(intent);
                        }else if ("2" == goodsDetialBean.getCode()) {
                            SharedPreferences.Editor sp = getSharedPreferences("save_name_pwd", Context.MODE_PRIVATE).edit();
                            sp.clear();
                            sp.commit();
                            SharedPreferences.Editor is_first = getSharedPreferences("IS_FIRST", Context.MODE_PRIVATE).edit();
                            is_first.clear();
                            is_first.commit();
                            ActivityPageManager   instance = ActivityPageManager.getInstance();
                            instance.finishAllActivity();
                            startActivity(LoginActivity.class);
                            finish();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        showToastMessage(e.getMessage());
                    }

                    @Override
                    public void onNext(GoodsDetialBean goodsDetialBean) {
                        this.goodsDetialBean = goodsDetialBean;
                    }
                });

    }

    private void openGallery(int chooseRequest) {
        PictureSelector.create(EditorNewAddGoodsActivity.this)
                .openGallery(PictureMimeType.ofAll())
                .maxSelectNum(1)// 最大图片选择数量
                .selectionMode(PictureConfig.SINGLE)
                .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                .imageFormat(PictureMimeType.PNG)// 拍照保存图片格式后缀,默认jpeg
                .enableCrop(false)// 是否裁剪
                .compress(true)// 是否压缩
                .synOrAsy(true)//同步true或异步false 压缩 默认同步
                .glideOverride(160, 160)// glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
                .withAspectRatio(16, 9)// 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                .freeStyleCropEnabled(true)// 裁剪框是否可拖拽
                .minimumCompressSize(100)// 小于100kb的图片不压缩
                .forResult(chooseRequest);//结果回调onActivityResult code
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    localMedia = PictureSelector.obtainMultipleResult(data);
                    path1 = localMedia.get(0).getPath();
                    File file1 = new File(localMedia.get(0).getPath());
                    if (!"".equals(path1) && path1 != null) {
                        ImageLoadUtil.GlideImageLoad(mContext, path1, img_1);
                        img_1.setLayoutParams(params);
                        img_1.setScaleType(ImageView.ScaleType.FIT_XY);
                        tv_1.setVisibility(View.GONE);
                        img_delete_1.setVisibility(View.VISIBLE);
                    }
                    ModifyImage(img_id1, file1);
                    break;
                case PictureConfig.REQUEST_CAMERA:
                    localMedia = PictureSelector.obtainMultipleResult(data);
                    File file2 = new File(localMedia.get(0).getPath());
                    path2 = localMedia.get(0).getPath();
                    if (!"".equals(path2) && path2 != null) {
                        ImageLoadUtil.GlideImageLoad(mContext, path2, img_2);
                        img_2.setLayoutParams(params);
                        img_2.setScaleType(ImageView.ScaleType.FIT_XY);
                        tv_2.setVisibility(View.GONE);
                        img_delete_2.setVisibility(View.VISIBLE);
                    }
                    ModifyImage(img_id2, file2);
                    break;
                case PictureConfig.CAMERA:
                    localMedia = PictureSelector.obtainMultipleResult(data);
                    File file3 = new File(localMedia.get(0).getPath());
                    path3 = localMedia.get(0).getPath();
                    if (!"".equals(path3) && path3 != null) {
                        ImageLoadUtil.GlideImageLoad(mContext, path3, img_3);
                        img_3.setLayoutParams(params);
                        img_3.setScaleType(ImageView.ScaleType.FIT_XY);
                        tv_3.setVisibility(View.GONE);
                        img_delete_3.setVisibility(View.VISIBLE);
                    }
                    ModifyImage(img_id3, file3);
                    break;
                case PictureConfig.CLOSE_PREVIEW_FLAG:
                    localMedia = PictureSelector.obtainMultipleResult(data);
                    File file4 = new File(localMedia.get(0).getPath());
                    path4 = localMedia.get(0).getPath();
                    if (!"".equals(path4) && path4 != null) {
                        ImageLoadUtil.GlideImageLoad(mContext, path4, img_4);
                        img_4.setLayoutParams(params);
                        img_4.setScaleType(ImageView.ScaleType.FIT_XY);
                        tv_4.setVisibility(View.GONE);
                        img_delete_4.setVisibility(View.VISIBLE);
                    }
                    ModifyImage(img_id4, file4);
                    break;
                case PictureConfig.READ_EXTERNAL_STORAGE:
                    localMedia = PictureSelector.obtainMultipleResult(data);
                    File file5 = new File(localMedia.get(0).getPath());
                    path5 = localMedia.get(0).getPath();
                    if (!"".equals(path5) && path5 != null) {
                        ImageLoadUtil.GlideImageLoad(mContext, path5, img_5);
                        img_5.setLayoutParams(params);
                        img_5.setScaleType(ImageView.ScaleType.FIT_XY);
                        tv_5.setVisibility(View.GONE);
                        img_delete_5.setVisibility(View.VISIBLE);
                    }
                    ModifyImage(img_id5, file5);
                    break;
                case PictureConfig.UPDATE_FLAG:
                    localMedia = PictureSelector.obtainMultipleResult(data);
                    File file6 = new File(localMedia.get(0).getPath());
                    path6 = localMedia.get(0).getPath();
                    if (!"".equals(path6) && path6 != null) {
                        ImageLoadUtil.GlideImageLoad(mContext, path6, img_6);
                        img_6.setLayoutParams(params);
                        img_6.setScaleType(ImageView.ScaleType.FIT_XY);
                        tv_6.setVisibility(View.GONE);
                        img_delete_6.setVisibility(View.VISIBLE);
                    }
                    ModifyImage(img_id6, file6);
                    break;
            }
        }
    }

    private void ModifyImage(String pid1, File file) {
        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("icon", file.getName(), requestBody);
        if (pid1 == null || TextUtils.isEmpty(pid1)) {//添加图片
            NetWork.getRetrofit()
                    .create(ImpAddLunBoImageService.class)
                    .getCal(NetWork.getToken(), pid, body)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<AddLunBoImageBean>() {
                        private AddLunBoImageBean addLunBoImageBean;

                        @Override
                        public void onCompleted() {
                            showToastMessage(addLunBoImageBean.getMsg());
                            if ("0".equals(addLunBoImageBean.getCode())) {
                                String icon = addLunBoImageBean.getIcon();
                                Intent intent=new Intent("ICON_PATH");
                                intent.putExtra("icon_path",icon);
                                sendBroadcast(intent);
                                ClearCacheData();
                            }else if ("2".equals(addLunBoImageBean.getCode())){
                                SharedPreferences.Editor sp = getSharedPreferences("save_name_pwd", Context.MODE_PRIVATE).edit();
                                sp.clear();
                                sp.commit();
                                SharedPreferences.Editor is_first = getSharedPreferences("IS_FIRST", Context.MODE_PRIVATE).edit();
                                is_first.clear();
                                is_first.commit();
                                ActivityPageManager instance = ActivityPageManager.getInstance();
                                instance.finishAllActivity();
                                startActivity(LoginActivity.class);
                                finish();
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            showToastMessage(e.getMessage());
                        }

                        @Override
                        public void onNext(AddLunBoImageBean addLunBoImageBean) {
                            this.addLunBoImageBean = addLunBoImageBean;
                        }
                    });
        } else {// 修改图片
//            RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
//            MultipartBody.Part body = MultipartBody.Part.createFormData("icon", file.getName(), requestBody);
            NetWork.getRetrofit()
                    .create(ImpDeleteAndModifyLunBoService.class)
                    .getCall(NetWork.getToken(), pid1, "2", body)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<DeleteAndModifyLunBoBean>() {
                        private DeleteAndModifyLunBoBean deleteAndModifyLunBoBean;

                        @Override
                        public void onCompleted() {
                            showToastMessage(deleteAndModifyLunBoBean.getMsg());
                            if ("0".equals(deleteAndModifyLunBoBean.getCode())) {
                                ClearCacheData();
                            }else  if ("2".equals(deleteAndModifyLunBoBean.getCode())) {
                                SharedPreferences.Editor sp =getSharedPreferences("save_name_pwd", Context.MODE_PRIVATE).edit();
                                sp.clear();
                                sp.commit();
                                SharedPreferences.Editor is_first =getSharedPreferences("IS_FIRST", Context.MODE_PRIVATE).edit();
                                is_first.clear();
                                is_first.commit();
                                ActivityPageManager instance = ActivityPageManager.getInstance();
                                instance.finishAllActivity();
                                startActivity(LoginActivity.class);
                                finish();
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            showToastMessage(e.getMessage());
                        }

                        @Override
                        public void onNext(DeleteAndModifyLunBoBean deleteAndModifyLunBoBean) {
                            this.deleteAndModifyLunBoBean = deleteAndModifyLunBoBean;
                        }
                    });
        }
    }

    private void ClearCacheData() {
        // 清空图片缓存，包括裁剪、压缩后的图片 注意:必须要在上传完成后调用 必须要获取权限
        RxPermissions permissions = new RxPermissions(this);
        permissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(new io.reactivex.Observer<Boolean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        if (aBoolean) {
                            PictureFileUtils.deleteCacheDirFile(mContext);
                        } else {
                            Toast.makeText(mContext, getString(R.string.picture_jurisdiction), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter intentFilter=new IntentFilter("FINISH");
        registerReceiver(broadcastReceiver,intentFilter);
    }

    BroadcastReceiver broadcastReceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if ("FINISH".equals(intent.getAction())) {
                finish();
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }
}
