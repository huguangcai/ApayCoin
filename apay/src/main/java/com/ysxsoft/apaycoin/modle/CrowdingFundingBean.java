package com.ysxsoft.apaycoin.modle;

import java.util.ArrayList;

/**
 * 描述： TODO
 * 日期： 2018/11/21 0021 09:31
 * 作者： 胡
 * 公司：郑州亿生信科技有限公司
 */
public class CrowdingFundingBean {


    /**
     * code : 0
     * msg : 获取成功!
     * data : {"0":{"begin_date":"2018-11-22 00:00","end_date":"2018-11-24 23:00","fx_num":"32432","money":"432234.0000","xiangou":"324234","sy_num":"32432"}}
     * ;last_page : 1
     */

    private String code;
    private String msg;
    private ArrayList<DataBean> data;
    private String last_page;

    public ArrayList<DataBean> getData() {
        return data;
    }

    public void setData(ArrayList<DataBean> data) {
        this.data = data;
    }

    public String getLast_page() {
        return last_page;
    }

    public void setLast_page(String last_page) {
        this.last_page = last_page;
    }

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

    public class DataBean {
        /**
         * begin_date : 2018-11-22 00:00
         * end_date : 2018-11-24 23:00
         * fx_num : 32432
         * money : 432234.0000
         * xiangou : 324234
         * sy_num : 32432
         */

        private String begin_date;
        private String end_date;
        private String fx_num;
        private String money;
        private String xiangou;
        private String sy_num;

        public String getCid() {
            return cid;
        }

        public void setCid(String cid) {
            this.cid = cid;
        }

        private String cid;

        public String getBegin_date() {
            return begin_date;
        }

        public void setBegin_date(String begin_date) {
            this.begin_date = begin_date;
        }

        public String getEnd_date() {
            return end_date;
        }

        public void setEnd_date(String end_date) {
            this.end_date = end_date;
        }

        public String getFx_num() {
            return fx_num;
        }

        public void setFx_num(String fx_num) {
            this.fx_num = fx_num;
        }

        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }

        public String getXiangou() {
            return xiangou;
        }

        public void setXiangou(String xiangou) {
            this.xiangou = xiangou;
        }

        public String getSy_num() {
            return sy_num;
        }

        public void setSy_num(String sy_num) {
            this.sy_num = sy_num;
        }

    }
}
