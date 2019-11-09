package com.ysxsoft.apaycoin.modle;

import java.util.ArrayList;

/**
 * 描述： TODO
 * 日期： 2018/11/20 0020 08:57
 * 作者： 胡
 * 公司：郑州亿生信科技有限公司
 */
public class PersonInfoBean {
    private String code;
    private String msg;
    private String last_page;

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

    public String getLast_page() {
        return last_page;
    }

    public void setLast_page(String last_page) {
        this.last_page = last_page;
    }

    public ArrayList<DataBean> getData() {
        return data;
    }

    public void setData(ArrayList<DataBean> data) {
        this.data = data;
    }

    private ArrayList<DataBean> data;

    public class DataBean {
        private String add_time;
        private String vals;
        private String news;//0未读 1已读
        private String msg_id;

        public String getNews() {
            return news;
        }

        public void setNews(String news) {
            this.news = news;
        }

        public String getMsg_id() {
            return msg_id;
        }

        public void setMsg_id(String msg_id) {
            this.msg_id = msg_id;
        }

        public String getAdd_time() {
            return add_time;
        }

        public void setAdd_time(String add_time) {
            this.add_time = add_time;
        }

        public String getVals() {
            return vals;
        }

        public void setVals(String vals) {
            this.vals = vals;
        }


    }
}
