package com.ysxsoft.apaycoin.modle;

/**
 * 描述： TODO
 * 日期： 2018/11/20 0020 14:56
 * 作者： 胡
 * 公司：郑州亿生信科技有限公司
 */
public class NumAssetTuBean {


    /**
     * code : 0
     * msg : 获取成功
     * data : {"20181109":"1.0000","20181110":"1.0000","20181111":"1.0000","20181112":"1.0343","20181113":"1.0800"}
     * min : 1.0
     * max : 1.1
     * gg : 0.03
     * jiaoyi : 1.0529
     * gold : 563.0000
     */

    private String code;
    private String msg;
    private DataBean data;
    private String min;
    private String max;
    private String gg;
    private String jiaoyi;
    private String gold;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getMin() {
        return min;
    }

    public void setMin(String min) {
        this.min = min;
    }

    public String getMax() {
        return max;
    }

    public void setMax(String max) {
        this.max = max;
    }

    public String getGg() {
        return gg;
    }

    public void setGg(String gg) {
        this.gg = gg;
    }

    public String getJiaoyi() {
        return jiaoyi;
    }

    public void setJiaoyi(String jiaoyi) {
        this.jiaoyi = jiaoyi;
    }

    public String getGold() {
        return gold;
    }

    public void setGold(String gold) {
        this.gold = gold;
    }

    public static class DataBean {
        private String json;

        public String getJson() {
            return json;
        }

        public void setJson(String json) {
            this.json = json;
        }
    }
}
