package com.ysxsoft.apaycoin.view;

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

import com.google.gson.Gson;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.ysxsoft.apaycoin.R;
import com.ysxsoft.apaycoin.com.ActivityPageManager;
import com.ysxsoft.apaycoin.com.BaseActivity;
import com.ysxsoft.apaycoin.impservice.ImpGoodsDetialService;
import com.ysxsoft.apaycoin.impservice.ImpNewAddGoodsService;
import com.ysxsoft.apaycoin.modle.GoodsDetialBean;
import com.ysxsoft.apaycoin.modle.NewAddGoodsBean;
import com.ysxsoft.apaycoin.utils.CustomDialog;
import com.ysxsoft.apaycoin.utils.ImageLoadUtil;
import com.ysxsoft.apaycoin.utils.NetWork;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 描述： 新增商品界面
 * 日期： 2018/11/7 0007 14:52
 * 作者： 胡
 * 公司：郑州亿生信科技有限公司
 */
public class NewAddGoodsActivity extends BaseActivity implements View.OnClickListener {

    private View img_back;
    private TextView tv_title;
    private LinearLayout ll_1, ll_2, ll_3, ll_4, ll_5, ll_6;
    private ImageView img_1, img_2, img_3, img_4, img_5, img_6;
    private ImageView img_delete_1, img_delete_2, img_delete_3, img_delete_4, img_delete_5, img_delete_6;
    private TextView tv_1, tv_2, tv_3, tv_4, tv_5, tv_6;
    private EditText ed_goods_name, ed_goods_price, ed_stock_num;
    private Button btn_save_next;
    private List<LocalMedia> localMedia;
    private List<File> fileList = new ArrayList<>();
    private String path1, path2, path3, path4, path5, path6;
    private Map<String, File> map = new HashMap<>();
    private String pid;
    private int size=0;
    private CustomDialog customDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.business_new_add_goods_upload_layout);
        Intent intent = getIntent();
        pid = intent.getStringExtra("pid");
        initView();
        initListener();
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
    public void onClick(View view) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        switch (view.getId()) {
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
                tv_1.setVisibility(View.VISIBLE);
                ImageLoadUtil.NewGoodsGlideImageLoad(mContext, "", img_1);
                img_1.setLayoutParams(params);
                img_delete_1.setVisibility(View.GONE);
                Iterator<String> iterator1 = map.keySet().iterator();
                while (iterator1.hasNext()) {
                    String key = iterator1.next();
                    if ("1".equals(key)) {
                        iterator1.remove();
                    }
                }
                break;
            case R.id.img_delete_2:
                tv_2.setVisibility(View.VISIBLE);
                ImageLoadUtil.NewGoodsGlideImageLoad(mContext, "", img_2);
                img_2.setLayoutParams(params);
                img_delete_2.setVisibility(View.GONE);
                Iterator<String> iterator2 = map.keySet().iterator();
                while (iterator2.hasNext()) {
                    String key = iterator2.next();
                    if ("2".equals(key)) {
                        iterator2.remove();
                    }
                }
                break;
            case R.id.img_delete_3:
                tv_3.setVisibility(View.VISIBLE);
                ImageLoadUtil.NewGoodsGlideImageLoad(mContext, "", img_3);
                img_3.setLayoutParams(params);
                img_delete_3.setVisibility(View.GONE);
                Iterator<String> iterator3 = map.keySet().iterator();
                while (iterator3.hasNext()) {
                    String key = iterator3.next();
                    if ("3".equals(key)) {
                        iterator3.remove();
                    }
                }
                break;
            case R.id.img_delete_4:
                tv_4.setVisibility(View.VISIBLE);
                ImageLoadUtil.NewGoodsGlideImageLoad(mContext, "", img_4);
                img_4.setLayoutParams(params);
                img_delete_4.setVisibility(View.GONE);
                Iterator<String> iterator4 = map.keySet().iterator();
                while (iterator4.hasNext()) {
                    String key = iterator4.next();
                    if ("4".equals(key)) {
                        iterator4.remove();
                    }
                }
                break;
            case R.id.img_delete_5:
                tv_5.setVisibility(View.VISIBLE);
                ImageLoadUtil.NewGoodsGlideImageLoad(mContext, "", img_5);
                img_5.setLayoutParams(params);
                img_delete_5.setVisibility(View.GONE);
                Iterator<String> iterator5 = map.keySet().iterator();
                while (iterator5.hasNext()) {
                    String key = iterator5.next();
                    if ("5".equals(key)) {
                        iterator5.remove();
                    }
                }
                break;
            case R.id.img_delete_6:
                tv_6.setVisibility(View.VISIBLE);
                ImageLoadUtil.NewGoodsGlideImageLoad(mContext, "", img_6);
                img_6.setLayoutParams(params);
                img_delete_6.setVisibility(View.GONE);
                Iterator<String> iterator6 = map.keySet().iterator();
                while (iterator6.hasNext()) {
                    String key = iterator6.next();
                    if ("6".equals(key)) {
                        iterator6.remove();
                    }
                }
                break;
            case R.id.btn_save_next:
                if (map.size()<=0){
                    showToastMessage("图片不能为空");
                    return;
                }
                if (TextUtils.isEmpty(ed_goods_name.getText().toString().trim())) {
                    showToastMessage("店铺名称不能为空");
                    return;
                }
                if (TextUtils.isEmpty(ed_goods_price.getText().toString().trim())) {
                    showToastMessage("商品价格不能为空");
                    return;
                }
                if (TextUtils.isEmpty(ed_stock_num.getText().toString().trim())) {
                    showToastMessage("库存不能为空");
                    return;
                }
                customDialog = new CustomDialog(mContext,"正在提交数据...");
                customDialog.show();
//                if (!"".equals(pid) && pid != null) {
//                    modifyData();
//                } else {
                    submitData();
//                }
                break;
        }
    }
//    /**
//     * 从店铺详情界面跳转过来  修改数据
//     */
//    private void modifyData() {
//        NetWork.getRetrofit()
//                .create(ImpGoodsDetialService.class)
//                .getCall(NetWork.getToken(),
//                        ed_goods_name.getText().toString().trim(),
//                        ed_goods_price.getText().toString().trim(),
//                        ed_stock_num.getText().toString().trim(), pid)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Observer<GoodsDetialBean>() {
//                    private GoodsDetialBean goodsDetialBean;
//
//                    @Override
//                    public void onCompleted() {
//                        showToastMessage(goodsDetialBean.getMsg());
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        showToastMessage(e.getMessage());
//                    }
//
//                    @Override
//                    public void onNext(GoodsDetialBean goodsDetialBean) {
//                        this.goodsDetialBean = goodsDetialBean;
//                    }
//                });
//
//    }

    /**
     * 提交数据
     */
    private void submitData() {
        Map<String, RequestBody> bodyMap = new HashMap<String, RequestBody>();
        List<MultipartBody.Part> parts = new ArrayList<>();
        for (File value : map.values()) {
            size=size+1;
            RequestBody body = RequestBody.create(MediaType.parse("image/*"),value);   //说明该文件为图片类型
//"key"相当与<input type="file" name="key">中的name的值 (其实没有多大的用,在后台可以遍历取出文件,一般要和后台开发人员协商的) f.getName为文件名，这两个是不一样的。
            MultipartBody.Part part = MultipartBody.Part.createFormData("file"+(size+1), value.getName(),body);
            parts.add(part);
        }

        NetWork.getRetrofit()
                .create(ImpNewAddGoodsService.class)
                .getCall(NetWork.getToken(),
                        ed_goods_name.getText().toString().trim(),
                        ed_goods_price.getText().toString().trim(),
                        ed_stock_num.getText().toString().trim(),
                        parts)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<NewAddGoodsBean>() {
                    private NewAddGoodsBean newAddGoodsBean;
                    @Override
                    public void onCompleted() {
                        showToastMessage(newAddGoodsBean.getMsg());
                        if ("0".equals(newAddGoodsBean.getCode())) {
                            customDialog.dismiss();
                            Intent intent=new Intent(mContext,NewAddGoodsEditorActivity.class);
                            intent.putExtra("pid",newAddGoodsBean.getPid());
                            startActivity(intent);
                        }else if ("2" .equals( newAddGoodsBean.getCode())) {
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
                    public void onNext(NewAddGoodsBean newAddGoodsBean) {
                        this.newAddGoodsBean = newAddGoodsBean;
                    }
                });
    }

    private void openGallery(int chooseRequest) {
        PictureSelector.create(NewAddGoodsActivity.this)
                .openGallery(PictureMimeType.ofAll())
                .maxSelectNum(1)// 最大图片选择数量
                .selectionMode(PictureConfig.SINGLE)
                .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                .imageFormat(PictureMimeType.PNG)// 拍照保存图片格式后缀,默认jpeg
                .enableCrop(false)// 是否裁剪
                .compress(false)// 是否压缩
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
                    map.put("1", file1);
                    if (!"".equals(path1) && path1 != null) {
                        ImageLoadUtil.GlideImageLoad(mContext, path1, img_1);
                        img_1.setLayoutParams(params);
                        img_1.setScaleType(ImageView.ScaleType.FIT_XY);
                        tv_1.setVisibility(View.GONE);
                        img_delete_1.setVisibility(View.VISIBLE);
                    }
                    break;
                case PictureConfig.REQUEST_CAMERA:
                    localMedia = PictureSelector.obtainMultipleResult(data);
                    File file2 = new File(localMedia.get(0).getPath());
                    path2 = localMedia.get(0).getPath();
                    map.put("2", file2);
                    if (!"".equals(path2) && path2 != null) {
                        ImageLoadUtil.GlideImageLoad(mContext, path2, img_2);
                        img_2.setLayoutParams(params);
                        img_2.setScaleType(ImageView.ScaleType.FIT_XY);
                        tv_2.setVisibility(View.GONE);
                        img_delete_2.setVisibility(View.VISIBLE);
                    }
                    break;
                case PictureConfig.CAMERA:
                    localMedia = PictureSelector.obtainMultipleResult(data);
                    File file3 = new File(localMedia.get(0).getPath());
                    path3 = localMedia.get(0).getPath();
                    map.put("3", file3);
                    if (!"".equals(path3) && path3 != null) {
                        ImageLoadUtil.GlideImageLoad(mContext, path3, img_3);
                        img_3.setLayoutParams(params);
                        img_3.setScaleType(ImageView.ScaleType.FIT_XY);
                        tv_3.setVisibility(View.GONE);
                        img_delete_3.setVisibility(View.VISIBLE);
                    }

                    break;
                case PictureConfig.CLOSE_PREVIEW_FLAG:
                    localMedia = PictureSelector.obtainMultipleResult(data);
                    File file4 = new File(localMedia.get(0).getPath());
                    path4 = localMedia.get(0).getPath();
                    map.put("4", file4);

                    if (!"".equals(path4) && path4 != null) {
                        ImageLoadUtil.GlideImageLoad(mContext, path4, img_4);
                        img_4.setLayoutParams(params);
                        img_4.setScaleType(ImageView.ScaleType.FIT_XY);
                        tv_4.setVisibility(View.GONE);
                        img_delete_4.setVisibility(View.VISIBLE);
                    }

                    break;
                case PictureConfig.READ_EXTERNAL_STORAGE:
                    localMedia = PictureSelector.obtainMultipleResult(data);
                    File file5 = new File(localMedia.get(0).getPath());
                    path5 = localMedia.get(0).getPath();
                    map.put("5", file5);
                    if (!"".equals(path5) && path5 != null) {
                        ImageLoadUtil.GlideImageLoad(mContext, path5, img_5);
                        img_5.setLayoutParams(params);
                        img_5.setScaleType(ImageView.ScaleType.FIT_XY);
                        tv_5.setVisibility(View.GONE);
                        img_delete_5.setVisibility(View.VISIBLE);
                    }
                    break;
                case PictureConfig.UPDATE_FLAG:
                    localMedia = PictureSelector.obtainMultipleResult(data);
                    File file6 = new File(localMedia.get(0).getPath());
                    path6 = localMedia.get(0).getPath();
                    map.put("6", file6);

                    if (!"".equals(path6) && path6 != null) {
                        ImageLoadUtil.GlideImageLoad(mContext, path6, img_6);
                        img_6.setLayoutParams(params);
                        img_6.setScaleType(ImageView.ScaleType.FIT_XY);
                        tv_6.setVisibility(View.GONE);
                        img_delete_6.setVisibility(View.VISIBLE);
                    }
                    break;

            }
        }
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
