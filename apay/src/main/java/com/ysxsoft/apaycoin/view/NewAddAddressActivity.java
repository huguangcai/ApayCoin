package com.ysxsoft.apaycoin.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.google.gson.Gson;
import com.ysxsoft.apaycoin.R;
import com.ysxsoft.apaycoin.com.ActivityPageManager;
import com.ysxsoft.apaycoin.com.BaseActivity;
import com.ysxsoft.apaycoin.impservice.ImpAddMyAddressService;
import com.ysxsoft.apaycoin.impservice.ImpAddressListEditNormalService;
import com.ysxsoft.apaycoin.modle.AddMyAddressBean;
import com.ysxsoft.apaycoin.modle.JsonBean;
import com.ysxsoft.apaycoin.utils.AppUtil;
import com.ysxsoft.apaycoin.utils.GetJsonDataUtil;
import com.ysxsoft.apaycoin.utils.NetWork;

import org.json.JSONArray;

import java.util.ArrayList;

import retrofit2.Retrofit;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 描述： TODO
 * 日期： 2018/11/12 0012 17:56
 * 作者： 胡
 * 公司：郑州亿生信科技有限公司
 */
public class NewAddAddressActivity extends BaseActivity implements View.OnClickListener {

    private View img_back;
    private TextView tv_title, tv_area;
    private EditText ed_contact_person, ed_contact_phone, ed_detail_area;
    private LinearLayout ll_select_area;
    private Button btn_save;
    private ArrayList<JsonBean> options1Items = new ArrayList<>();
    private ArrayList<ArrayList<String>> options2Items = new ArrayList<>();
    private ArrayList<ArrayList<ArrayList<String>>> options3Items = new ArrayList<>();
    private String editor, aid, is_true, name, phone, intentprovice, intentcity, intentarea, intentadress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_add_address_layout);
        Intent intent = getIntent();
        editor = intent.getStringExtra("editor");
        aid = intent.getStringExtra("aid");
        is_true = intent.getStringExtra("is_true");
        name = intent.getStringExtra("name");
        phone = intent.getStringExtra("phone");
        intentprovice = intent.getStringExtra("provice");
        intentcity = intent.getStringExtra("city");
        intentarea = intent.getStringExtra("area");
        intentadress = intent.getStringExtra("adress");
        initView();
        initData();
        initListener();
    }

    private void initData() {
        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        initJsonData();
                    }
                }
        ).start();
    }

    /**
     * 初始化json数据
     */
    private void initJsonData() {
        /**
         * 注意：assets 目录下的Json文件仅供参考，实际使用可自行替换文件
         * 关键逻辑在于循环体
         *
         * */
        String JsonData = new GetJsonDataUtil().getJson(this, "province.json");//获取assets目录下的json文件数据
        ArrayList<JsonBean> jsonBean = parseData(JsonData);//用Gson 转成实体
        /**
         * 添加省份数据
         * 注意：如果是添加的JavaBean实体，则实体类需要实现 IPickerViewData 接口，
         * PickerView会通过getPickerViewText方法获取字符串显示出来。
         */
        options1Items = jsonBean;
        for (int i = 0; i < jsonBean.size(); i++) {//遍历省份
            ArrayList<String> CityList = new ArrayList<>();//该省的城市列表（第二级）
            ArrayList<ArrayList<String>> Province_AreaList = new ArrayList<>();//该省的所有地区列表（第三极）
            for (int c = 0; c < jsonBean.get(i).getCityList().size(); c++) {//遍历该省份的所有城市
                String CityName = jsonBean.get(i).getCityList().get(c).getName();
                CityList.add(CityName);//添加城市
                ArrayList<String> City_AreaList = new ArrayList<>();//该城市的所有地区列表
                //如果无地区数据，建议添加空字符串，防止数据为null 导致三个选项长度不匹配造成崩溃
                if (jsonBean.get(i).getCityList().get(c).getArea() == null || jsonBean.get(i).getCityList().get(c).getArea().size() == 0) {
                    City_AreaList.add("");
                } else {
                    City_AreaList.addAll(jsonBean.get(i).getCityList().get(c).getArea());
                }
                Province_AreaList.add(City_AreaList);//添加该省所有地区数据
            }
            /**
             * 添加城市数据
             */
            options2Items.add(CityList);

            /**
             * 添加地区数据
             */
            options3Items.add(Province_AreaList);
        }
    }

    /**
     * 解析数据
     *
     * @param result
     * @return
     */
    private ArrayList<JsonBean> parseData(String result) {
        ArrayList<JsonBean> detail = new ArrayList<>();
        try {
            JSONArray data = new JSONArray(result);
            Gson gson = new Gson();
            for (int i = 0; i < data.length(); i++) {
                JsonBean entity = gson.fromJson(data.optJSONObject(i).toString(), JsonBean.class);
                detail.add(entity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return detail;
    }

    private void initView() {
        img_back = getViewById(R.id.img_back);
        tv_title = getViewById(R.id.tv_title);
        if (editor != null) {
            tv_title.setText("修改地址");
        } else {
            tv_title.setText("新增地址");
        }
        tv_area = getViewById(R.id.tv_area);
        ed_contact_person = getViewById(R.id.ed_contact_person);
        ed_contact_phone = getViewById(R.id.ed_contact_phone);
        ed_detail_area = getViewById(R.id.ed_detail_area);
        ll_select_area = getViewById(R.id.ll_select_area);
        btn_save = getViewById(R.id.btn_save);

        if (!TextUtils.isEmpty(name)){
            ed_contact_person.setText(name);
            ed_contact_phone.setText(phone);
            ed_detail_area.setText(intentadress);
            tv_area.setText(intentprovice+intentcity+intentarea);
        }
    }

    private void initListener() {
        img_back.setOnClickListener(this);
        ll_select_area.setOnClickListener(this);
        btn_save.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.ll_select_area:
                AppUtil.colsePhoneKeyboard(this);
                showCity();
                break;
            case R.id.btn_save:
                if (editor != null) {
                    modifyData();
                } else {
                    submitData();
                }
                break;
        }
    }

    private void modifyData() {
        Retrofit retrofit = NetWork.getRetrofit();
        retrofit.create(ImpAddressListEditNormalService.class)
                .getCall(NetWork.getToken(),
                        aid, ed_detail_area.getText().toString().trim(),
                        is_true,
                        options1Items.get(provice).getPickerViewText(),
                        options2Items.get(provice).get(city),
                        options3Items.get(provice).get(city).get(area),
                        ed_contact_person.getText().toString().trim(),
                        ed_contact_phone.getText().toString().trim()
                )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<AddMyAddressBean>() {
                    private AddMyAddressBean addMyAddressBean;

                    @Override
                    public void onCompleted() {
                        showToastMessage(addMyAddressBean.getMsg());
                        finish();
                        if ("2".equals(addMyAddressBean.getCode())){
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
                    public void onNext(AddMyAddressBean addMyAddressBean) {
                        this.addMyAddressBean = addMyAddressBean;
                    }
                });

    }

    private int provice;
    private int city;
    private int area;

    private void showCity() {
        OptionsPickerView pvOptions = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                provice = options1;
                city = options2;
                area = options3;
                //返回的分别是三个级别的选中位置
                String tx = options1Items.get(options1).getPickerViewText() + options2Items.get(options1).get(options2) + options3Items.get(options1).get(options2).get(options3);
                tv_area.setText(tx);
            }
        })
                .setTitleText("城市选择")
                .setDividerColor(Color.BLACK)
                .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
                .setContentTextSize(20)
                .build();

        /*pvOptions.setPicker(options1Items);//一级选择器
        pvOptions.setPicker(options1Items, options2Items);//二级选择器*/
        pvOptions.setPicker(options1Items, options2Items, options3Items);//三级选择器
        pvOptions.show();
    }

    private void submitData() {
        if (TextUtils.isEmpty(ed_contact_person.getText().toString().trim())) {
            showToastMessage("联系人不能为空");
            return;
        }
        if (TextUtils.isEmpty(ed_contact_phone.getText().toString().trim())) {
            showToastMessage("联系电话不能为空");
            return;
        }
        if (!AppUtil.checkPhoneNum(ed_contact_phone.getText().toString().trim())) {
            showToastMessage("输入正确的手机号");
            return;
        }
        if (TextUtils.isEmpty(tv_area.getText().toString().trim())) {
            showToastMessage("地址不能为空");
            return;
        }
        if (TextUtils.isEmpty(ed_detail_area.getText().toString().trim())) {
            showToastMessage("详细地址不能为空");
            return;
        }
        if (!TextUtils.isEmpty(name) && TextUtils.isEmpty(options1Items.get(provice).getPickerViewText())) {
            SubmitData(intentprovice, intentcity, intentarea);
        } else {
            SubmitData(options1Items.get(provice).getPickerViewText(), options2Items.get(provice).get(city), options3Items.get(provice).get(city).get(area));
        }
    }

    private void SubmitData(String provice, String city, String area) {
        NetWork.getRetrofit()
                .create(ImpAddMyAddressService.class)
                .getCall(NetWork.getToken(),
                        provice,
                        city,
                        area,
                        ed_detail_area.getText().toString().trim(),
                        ed_contact_person.getText().toString().trim(),
                        ed_contact_phone.getText().toString().trim())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<AddMyAddressBean>() {
                    private AddMyAddressBean addMyAddressBean;

                    @Override
                    public void onCompleted() {
                        if ("0".equals(addMyAddressBean.getCode())) {
                            finish();
                        }else if ("2".equals(addMyAddressBean.getCode())){
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
                    public void onNext(AddMyAddressBean addMyAddressBean) {
                        this.addMyAddressBean = addMyAddressBean;
                    }
                });
    }
}
