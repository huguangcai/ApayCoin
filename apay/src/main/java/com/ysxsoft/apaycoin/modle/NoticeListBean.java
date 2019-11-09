package com.ysxsoft.apaycoin.modle;

import java.util.ArrayList;

/**
 * 描述： TODO
 * 日期： 2018/11/8 0008 13:35
 * 作者： 胡
 * 公司：郑州亿生信科技有限公司
 */
public class NoticeListBean {

    /**
     * code : 0
     * msg : 获取成功！
     * data : {"0":{"nid":"2","title":"标题1标题1标题1标题1标题1","descr":"描述1描述1描述1描述1描述1描述1描述1 描述1描述1描述1描述1描述1描述1描述1","add_time":"2018-10-31 17:22:53"},"1":{"nid":"1","title":"标题标题标题标题标题标题","descr":"描述描述描述描述描述描述描述描述 描述描述描述描述描述描述描述描述 描述描述描述描述描述描述描述描述","add_time":"2018-10-31 17:20:49"}}
     * last_page : 1
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

    public static class DataBean {
        /**
         * nid : 2
         * title : 标题1标题1标题1标题1标题1
         * descr : 描述1描述1描述1描述1描述1描述1描述1 描述1描述1描述1描述1描述1描述1描述1
         * add_time : 2018-10-31 17:22:53
         */

        private String nid;
        private String title;
        private String descr;
        private String add_time;

        public String getNid() {
            return nid;
        }

        public void setNid(String nid) {
            this.nid = nid;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescr() {
            return descr;
        }

        public void setDescr(String descr) {
            this.descr = descr;
        }

        public String getAdd_time() {
            return add_time;
        }

        public void setAdd_time(String add_time) {
            this.add_time = add_time;
        }

    }
}
