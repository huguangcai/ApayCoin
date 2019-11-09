package com.ysxsoft.apaycoin.modle;

import java.util.ArrayList;

/**
 * 描述： TODO
 * 日期： 2018/11/24 0024 11:57
 * 作者： 胡
 * 公司：郑州亿生信科技有限公司
 */
public class GoodsTypeBean {

    /**
     * code : 0
     * msg : 获取成功!
     * data : {"0":{"title":"服侍鞋包","id":"2"},"1":{"title":"护肤彩妆","id":"3"},"2":{"title":"数码电器","id":"4"},"3":{"title":"母婴用品","id":"5"}}
     */

    private String code;
    private String msg;

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

    public ArrayList<DataBean> getData() {
        return data;
    }

    public void setData(ArrayList<DataBean> data) {
        this.data = data;
    }

    private ArrayList<DataBean> data;

    public static class DataBean{
        private String title;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        private String id;
    }
}
