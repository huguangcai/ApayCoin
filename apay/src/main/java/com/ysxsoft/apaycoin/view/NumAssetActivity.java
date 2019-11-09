package com.ysxsoft.apaycoin.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.gson.GsonBuilder;
import com.ysxsoft.apaycoin.R;
import com.ysxsoft.apaycoin.com.ActivityPageManager;
import com.ysxsoft.apaycoin.com.BaseActivity;
import com.ysxsoft.apaycoin.com.BaseApplication;
import com.ysxsoft.apaycoin.impservice.ImpNumAssetTuService;
import com.ysxsoft.apaycoin.utils.DateUtil;
import com.ysxsoft.apaycoin.utils.NetWork;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 描述： 数字资产
 * 日期： 2018/11/5 0005 10:49
 * 作者： 胡
 * 公司：郑州亿生信科技有限公司
 */
public class NumAssetActivity extends BaseActivity implements View.OnClickListener {

    private View img_back;
    private TextView tv_title, tv_price, tv_hight_price, tv_low_price, tv_num_asset, tv_current_price;
    private LinearLayout ll_apay, ll_roll_out, ll_crowd_funding, ll_wallet_address;
    private LineChart linechart;
    List<String> times = new ArrayList<>();
    List<String> datavalues = new ArrayList<String>();
    private String gg, gold, max, min, msg;
    private String jiaoyi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.num_asset_layout);
        initView();
        requestData();
        initListener();
    }

    private void initView() {
        img_back = getViewById(R.id.img_back);
        tv_title = getViewById(R.id.tv_title);
        tv_title.setText("数字资产");
        tv_price = getViewById(R.id.tv_price);
        tv_hight_price = getViewById(R.id.tv_hight_price);
        tv_low_price = getViewById(R.id.tv_low_price);
        tv_num_asset = getViewById(R.id.tv_num_asset);
        tv_current_price = getViewById(R.id.tv_current_price);
        ll_apay = getViewById(R.id.ll_apay);
        ll_roll_out = getViewById(R.id.ll_roll_out);
        ll_crowd_funding = getViewById(R.id.ll_crowd_funding);
        ll_wallet_address = getViewById(R.id.ll_wallet_address);
        linechart = getViewById(R.id.line_chart);
//        initLineChart(linechart);
    }

    private void requestData() {
        NetWork.getRetrofit1().create(ImpNumAssetTuService.class).getCall(NetWork.getToken())
//                .getCall(NetWork.getFragmentToken(this))
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        try {
                            String jsonStr = new String(response.body().bytes());//把原始数据转为字符串
                            JSONObject jsonObject = new JSONObject(jsonStr);
                            String code = (String) jsonObject.get("code");
                            gg = (String) jsonObject.get("gg");
                            gold = (String) jsonObject.get("gold");
                            Object jiao = jsonObject.get("jiaoyi");
                            jiaoyi = String.valueOf(jiao);
//                            jiaoyi = (String) jsonObject.get("jiaoyi");
                            max = (String) jsonObject.get("max");
                            min = (String) jsonObject.get("min");
                            msg = (String) jsonObject.get("msg");
                            tv_price.setText(String.valueOf(NumAssetActivity.this.jiaoyi));
                            tv_hight_price.setText(max);
                            tv_low_price.setText(min);
                            tv_num_asset.setText(gold);
                            tv_current_price.setText(String.valueOf(NumAssetActivity.this.jiaoyi));
                            JSONObject data = jsonObject.getJSONObject("data");
                            Iterator<String> keys = data.keys();
//                            showToastMessage(msg);
                            if ("0".equals(code)) {
                                //获取key
                                while (keys.hasNext()) {
                                    String key = keys.next();
                                    times.add(key);
                                }
                                //获取value
                                for (int i = 0; i < times.size(); i++) {
                                    String value = (String) data.get(times.get(i));
                                    datavalues.add(value);
                                }
                            }else if ("2" .equals(code)) {
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
                            initLineChart(linechart, gg, max, min, times, datavalues);
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        showToastMessage(t.getMessage());
                    }
                });
    }

    private void initLineChart(LineChart linechart, String gg, String max, String min, final List<String> times, List<String> values) {
        Collections.sort(times);
        //没有数据时的标题
        linechart.setNoDataText("暂无数据");
        //标题颜色
        linechart.setNoDataTextColor(mContext.getResources().getColor(R.color.black));
        //图表的背景
        linechart.setBackgroundColor(mContext.getResources().getColor(R.color.white));
        //是否可触摸
        linechart.setTouchEnabled(true);
        linechart.setDragEnabled(false);//是否可拖动
        linechart.getDescription().setEnabled(false);
        linechart.setDrawGridBackground(false);
        linechart.setScaleEnabled(false);
        linechart.setPinchZoom(false);
        //禁用图表  左下角的文本提示
        linechart.getLegend().setEnabled(false);
        //x轴
        XAxis xAxis;
        {
            xAxis = linechart.getXAxis();
            //x轴位置
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            //设置轴启用或禁用。如果false，该轴的任何部分都不会被绘制（不绘制坐标轴/便签等）
            xAxis.setEnabled(true);
            //设置为true，则绘制该行旁边的轴线
            xAxis.setDrawAxisLine(true);
            //设置网格线  true 绘制网格
            xAxis.setDrawGridLines(false);
            //X 轴的字体颜色
            xAxis.setTextColor(Color.BLACK);
            //x轴的字体大小
            xAxis.setTextSize(10f);
            //X轴的颜色
            xAxis.setAxisLineColor(Color.BLACK);
            xAxis.setAxisLineWidth(1.5f);
            xAxis.setGranularity(1f);//x轴上的间隔
            xAxis.setLabelCount(5, false);
            //标签倾斜45度
//            xAxis.setLabelRotationAngle(45);
            xAxis.setValueFormatter(new IAxisValueFormatter() {
                @Override
                public String getFormattedValue(float value, AxisBase axis) {
                    String tradeDate = times.get((int) value);
                    return DateUtil.formatDate(tradeDate);
                }
            });
        }
        //y轴
        YAxis leftAxis;
        {
            //y轴左边
            leftAxis = linechart.getAxisLeft();
            linechart.getAxisRight().setEnabled(false);
//            leftAxis.setAxisMaximum(Float.parseFloat(max));//Y轴最大值
            leftAxis.setAxisMinimum(Float.parseFloat(min));//Y轴最小值
            //设置y轴的标签数量   如果 if(force == true)，则确切绘制指定数量的标签，但这样可能导致轴线分布不均匀
            leftAxis.setLabelCount(datavalues.size() + 2, false);//TODO 根据集合的数量来绘制y轴标签数
            //设置，其中轴标签绘制的位置
            leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
            //y轴颜色
            leftAxis.setAxisLineColor(Color.BLACK);
            //y轴 线宽
            leftAxis.setAxisLineWidth(1.5f);
            //垂直y轴上的线的颜色
            leftAxis.setGridColor(mContext.getResources().getColor(R.color.gray));
            //y轴 间隔
            leftAxis.setGranularity(Float.parseFloat(gg));
            leftAxis.setTextSize(10f);
            leftAxis.setDrawGridLines(true);
            leftAxis.setGranularityEnabled(true);
//            leftAxis.setValueFormatter(new IAxisValueFormatter() {
//                @Override
//                public String getFormattedValue(float value, AxisBase axis) {
//                    int IValue = (int) value;
//                    return String.valueOf(IValue);
//                }
//            });
        }
        setData();
    }

    private void setData() {
        ArrayList<Entry> values = new ArrayList<>();
        for (int i = 0; i < times.size(); i++) {
            values.add(new Entry(i, Float.valueOf(datavalues.get(i))));
        }
        LineDataSet set1;

        if (linechart.getData() != null && linechart.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet) linechart.getData().getDataSetByIndex(0);
            set1.setValues(values);
            set1.notifyDataSetChanged();
            linechart.getData().notifyDataChanged();
            linechart.notifyDataSetChanged();
        } else {
            set1 = new LineDataSet(values, "");
            set1.setDrawCircles(false);
            set1.setMode(LineDataSet.Mode.CUBIC_BEZIER);
            //绘制的线上有值是否显示
            set1.setDrawValues(false);
            set1.setDrawIcons(false);
            set1.setColor(getResources().getColor(R.color.btn_color));
            set1.setLineWidth(1f);
            set1.setValueTextSize(9f);
            set1.setDrawFilled(false);
            ArrayList<ILineDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1);
            LineData data = new LineData(dataSets);
            linechart.setData(data);
            linechart.animateX(1500);//做动画
        }
    }


    private void initListener() {
        img_back.setOnClickListener(this);
        ll_apay.setOnClickListener(this);
        ll_roll_out.setOnClickListener(this);
        ll_crowd_funding.setOnClickListener(this);
        ll_wallet_address.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                finish();
                break;

            case R.id.ll_apay:
                Intent intent = new Intent(mContext, NumAssetApayActivity.class);
                intent.putExtra("max", max);
                intent.putExtra("min", min);
                startActivity(intent);
                break;

            case R.id.ll_roll_out:
                startActivity(NumAssetRollOutActivity.class);
                break;

            case R.id.ll_crowd_funding:
                startActivity(CrowdFundingActivity.class);
                break;

            case R.id.ll_wallet_address:
                startActivity(WalletAddressActivity.class);
                break;


        }
    }

}
