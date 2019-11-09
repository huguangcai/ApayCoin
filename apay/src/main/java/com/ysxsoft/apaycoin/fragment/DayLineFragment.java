package com.ysxsoft.apaycoin.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.Utils;
import com.ysxsoft.apaycoin.R;
import com.ysxsoft.apaycoin.com.ActivityPageManager;
import com.ysxsoft.apaycoin.impservice.ImpBrokenLineSumService;
import com.ysxsoft.apaycoin.utils.DateUtil;
import com.ysxsoft.apaycoin.utils.NetWork;
import com.ysxsoft.apaycoin.view.LoginActivity;

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

public class DayLineFragment extends Fragment {
    private LineChart linechart;
    private ArrayList<String> times = new ArrayList<>();
    private ArrayList<String> datavalues = new ArrayList<String>();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.day_line_fragment_layout, null);
        initView(view);
//        getData("3");
        return view;
    }

    private void initView(View view) {
        linechart = view.findViewById(R.id.line_chart);
    }

    /**
     * 获取五分钟  五小时 日线图数据
     *
     * @param type
     */
    private void getData(String type) {
        NetWork.getRetrofit1()
                .create(ImpBrokenLineSumService.class)
                .getCall(NetWork.getToken(), type)
//                .getCall(NetWork.getFragmentToken(getActivity()), type)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        try {
                            String jsonStr = new String(response.body().bytes());//把原始数据转为字符串
                            JSONObject jsonObject = new JSONObject(jsonStr);
                            String code = (String) jsonObject.get("code");
                            String gg = (String) jsonObject.get("gg");
                            String max = (String) jsonObject.get("max");
                            String min = (String) jsonObject.get("min");
                            String msg = (String) jsonObject.get("msg");
                            JSONObject data = jsonObject.getJSONObject("data");
                            Iterator<String> keys = data.keys();
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
                            }else if ("2".equals(code)) {
                                SharedPreferences.Editor sp = getActivity().getSharedPreferences("save_name_pwd", Context.MODE_PRIVATE).edit();
                                sp.clear();
                                sp.commit();
                                SharedPreferences.Editor is_first = getActivity().getSharedPreferences("IS_FIRST", Context.MODE_PRIVATE).edit();
                                is_first.clear();
                                is_first.commit();
                                ActivityPageManager instance = ActivityPageManager.getInstance();
                                instance.finishAllActivity();
                                getActivity().startActivity(new Intent(getActivity(),LoginActivity.class));

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
                        String message = t.getMessage();
                    }
                });
    }

    private void initLineChart(LineChart linechart, String gg, String max, String min, final List<String> times, List<String> datavalues) {
        Collections.sort(times);
//        //没有数据时的标题
        linechart.setNoDataText("暂无数据");
//        //标题颜色
        linechart.setNoDataTextColor(getActivity().getResources().getColor(R.color.btn_color));
        {
            linechart.setBackgroundColor(Color.WHITE);
//            // disable description text
            linechart.getDescription().setEnabled(false);
//            // enable touch gestures
            linechart.setTouchEnabled(true);
            linechart.setDrawGridBackground(false);
            linechart.setDragEnabled(false);//是否可拖动
            linechart.setScaleEnabled(false);
            linechart.setPinchZoom(false);
        }
        XAxis xAxis;
        {
            xAxis = linechart.getXAxis();
            xAxis.setDrawGridLines(false);
            //xz轴间隔
            xAxis.setGranularity(1f);
            //设置X轴的位置（默认在上方)
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
//            xAxis.setLabelCount(6, true);
            xAxis.setAxisLineColor(Color.BLACK);
            xAxis.setAxisLineWidth(2f);
            xAxis.setAxisMinimum(0f);
//            xAxis.setSpaceMax(1f);
            //设置X轴值为字符串
            xAxis.setValueFormatter(new IAxisValueFormatter() {
                @Override
                public String getFormattedValue(float value, AxisBase axis) {
                    if (times.size()!=0){
                        String tradeDate = times.get((int) value /*% times.size()*/);
                        return DateUtil.formatDate(tradeDate);
                    }
                    return "";
                }
            });
        }
        YAxis yAxis;
        {
            yAxis = linechart.getAxisLeft();
            linechart.getAxisRight().setEnabled(false);
            yAxis.setDrawAxisLine(false);
            yAxis.setDrawGridLines(false);
            yAxis.setTextColor(Color.WHITE);
//            yAxis.setAxisMaximum(Float.valueOf(max));
            yAxis.setAxisMinimum(Float.parseFloat(min));
//            yAxis.setAxisLineColor(Color.BLACK);
//            yAxis.setAxisLineWidth(2f);
            yAxis.setGranularity(Float.parseFloat(gg));
            yAxis.setLabelCount(datavalues.size()+2,false);
        }
//        setData(6);
//        // draw points over time
        linechart.animateX(1500);
        linechart.getLegend().setEnabled(false);
        setData(times, datavalues);
    }

    /**
     * 获取数据
     *
     * @param times
     * @param datavalues
     */
    private void setData(List<String> times, List<String> datavalues) {
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
            // create a dataset and give it a type
            set1 = new LineDataSet(values, "");
            set1.setDrawCircles(false);
            set1.setMode(LineDataSet.Mode.LINEAR);
            set1.setDrawValues(true);
            set1.setDrawIcons(false);
            set1.setColor(getResources().getColor(R.color.btn_color));
            set1.setLineWidth(2f);
            set1.setFormLineWidth(1f);
            set1.setFormSize(15.f);
            set1.setValueTextSize(9f);
            // set the filled area
            set1.setDrawFilled(true);

            // set color of filled area
            if (Utils.getSDKInt() >= 18) {
                // drawables only supported on api level 18 and above
                Drawable drawable = ContextCompat.getDrawable(getActivity(), R.drawable.fade_shadow);
                set1.setFillDrawable(drawable);
            } else {
                set1.setFillColor(Color.BLACK);
            }
            ArrayList<ILineDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1); // add the data sets
            LineData data = new LineData(dataSets);
            linechart.setData(data);
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            //Fragment隐藏时调用
//            times.clear();
//            datavalues.clear();
        } else {
            //Fragment显示时调用
//            getData("3");
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        getData("3");
    }

    @Override
    public void onPause() {
        super.onPause();
        times.clear();
        datavalues.clear();
    }
}
