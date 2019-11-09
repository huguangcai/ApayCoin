package com.ysxsoft.apaycoin.view;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.ysxsoft.apaycoin.adapte.GridImageAdapter;
import com.ysxsoft.apaycoin.com.ActivityPageManager;
import com.ysxsoft.apaycoin.com.BaseActivity;
import com.ysxsoft.apaycoin.com.FullyGridLayoutManager;
import com.ysxsoft.apaycoin.impservice.ImpGiveFeedBackService;
import com.ysxsoft.apaycoin.modle.GiveFeedBackBean;
import com.ysxsoft.apaycoin.utils.CustomDialog;
import com.ysxsoft.apaycoin.utils.NetWork;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.disposables.Disposable;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 描述： 意见反馈界面
 * 日期： 2018/11/6 0006 13:59
 * 作者： 胡
 * 公司：郑州亿生信科技有限公司
 */
public class GiveFeedBackActivity extends BaseActivity implements View.OnClickListener {

    private View img_back;
    private TextView tv_title, tv_num;
    private EditText ed_input_content;
    private LinearLayout ll_max_num;
    private RecyclerView rv_recyclerview;
    private Button btn_submit;
    private int maxSelectNum = 1;
    private List<LocalMedia> selectList = new ArrayList<>();
    private int chooseMode = PictureMimeType.ofAll();
    private GridImageAdapter adapter;
    private CustomDialog customDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.give_feedback_layout);
        customDialog = new CustomDialog(mContext,"正在提交数据...");
        initView();
        initData();
        initListener();
    }

    private void initView() {
        img_back = getViewById(R.id.img_back);
        tv_title = getViewById(R.id.tv_title);
        tv_title.setText("意见反馈");
        tv_num = getViewById(R.id.tv_num);
        ed_input_content = getViewById(R.id.ed_input_content);
        ll_max_num = getViewById(R.id.ll_max_num);
        rv_recyclerview = getViewById(R.id.rv_recyclerview);
        btn_submit = getViewById(R.id.btn_submit);
    }

    private void initData() {
        ed_input_content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tv_num.setText(String.valueOf(s.length()));
                if (200 == Integer.valueOf(s.length())) {
                    ll_max_num.setVisibility(View.GONE);
                } else {
                    ll_max_num.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        FullyGridLayoutManager manager = new FullyGridLayoutManager(mContext, 3, GridLayoutManager.VERTICAL, false);
        rv_recyclerview.setLayoutManager(manager);
        adapter = new GridImageAdapter(mContext, onAddPicClickListener);
//        adapter.setList(selectList);
        adapter.setSelectMax(maxSelectNum);
        rv_recyclerview.setAdapter(adapter);
    }

    private void initListener() {
        img_back.setOnClickListener(this);
        btn_submit.setOnClickListener(this);
    }

    private GridImageAdapter.onAddPicClickListener onAddPicClickListener = new GridImageAdapter.onAddPicClickListener() {

        @Override
        public void onAddPicClick() {
            PictureSelector.create(GiveFeedBackActivity.this)
                    .openGallery(chooseMode)// 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                    .maxSelectNum(maxSelectNum)// 最大图片选择数量
                    .minSelectNum(1)// 最小选择数量
                    .imageSpanCount(3)// 每行显示个数
//                    .selectionMode(PictureConfig.MULTIPLE)// 多选 or 单选
                    .selectionMode(PictureConfig.SINGLE)// 多选 or 单选
                    .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                    .imageFormat(PictureMimeType.PNG)// 拍照保存图片格式后缀,默认jpeg
                    .compress(true)// 是否压缩
                    .synOrAsy(true)//同步true或异步false 压缩 默认同步
                    .glideOverride(160, 160)// glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
                    .withAspectRatio(16, 9)// 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                    .freeStyleCropEnabled(true)// 裁剪框是否可拖拽
                    .selectionMedia(selectList)// 是否传入已选图片
                    .minimumCompressSize(100)// 小于100kb的图片不压缩
                    .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.btn_submit:
                if (TextUtils.isEmpty(ed_input_content.getText().toString().trim())) {
                    showToastMessage("内容描述不能为空");
                    return;
                }
                if (selectList.size() <= 0 || selectList == null) {
                    showToastMessage("截图内容不能为空");
                    return;
                }
                customDialog.show();
                submit();
                break;
        }
    }

    /**
     * 提交数据
     */
    private void submit() {
//        Map<String, RequestBody> bodyMap = new HashMap<String, RequestBody>();
//        for (int i = 0; i < selectList.size(); i++) {
//            String path = selectList.get(i).getPath();
//            File file = new File(path);
//            RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
//            bodyMap.put("file"+file.getName(), requestBody);
//        }
        File file = new File(selectList.get(0).getPath());
        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestBody);

        NetWork.getRetrofit()
                .create(ImpGiveFeedBackService.class)
                .getCall(NetWork.getToken(), ed_input_content.getText().toString().trim(), body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<GiveFeedBackBean>() {
                    private GiveFeedBackBean giveFeedBackBean;
                    @Override
                    public void onCompleted() {
                        showToastMessage(giveFeedBackBean.getMsg());
                        if ("0".equals(giveFeedBackBean.getCode())){
                            customDialog.dismiss();
                            ClearCache();
                            finish();
                        }else if ("2" == giveFeedBackBean.getCode()) {
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
                    public void onNext(GiveFeedBackBean giveFeedBackBean) {
                        this.giveFeedBackBean = giveFeedBackBean;
                    }
                });

    }

    private void ClearCache() {
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    // 图片选择结果回调
                    selectList = PictureSelector.obtainMultipleResult(data);
                    // 例如 LocalMedia 里面返回三种path
                    // 1.media.getPath(); 为原图path
                    // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true
                    // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true
                    // 如果裁剪并压缩了，已取压缩路径为准，因为是先裁剪后压缩的
                    for (LocalMedia media : selectList) {
                        Log.i("图片-----》", media.getPath());
                    }
                    adapter.setList(selectList);
                    adapter.notifyDataSetChanged();
                    break;
            }
        }
    }
}
