package com.ysxsoft.apaycoin.modle;

import java.util.ArrayList;

public class MerchantOrderRunAndCompleteBean {

    /**
     * code : 0
     * msg : 获取成功
     * data : {"0":{"oid":"2","money":"523.0000","add_time":"1970-01-01 08:00","flag":"0","icon":"http://ggapay.com//uploads/feedback/20181126/9223788a1c4f6c8b1acf1437f13c8c4f.jpg","title":"吃了吗"},"1":{"oid":"1","money":"523.0000","add_time":"1970-01-01 08:00","flag":"0","icon":"http://ggapay.com//uploads/feedback/20181126/9223788a1c4f6c8b1acf1437f13c8c4f.jpg","title":"吃了吗"}}
     * last_page : 1
     */

    private String code;
    private String msg;
    private ArrayList<DataBean> data;

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

    public String getLast_page() {
        return last_page;
    }

    public void setLast_page(String last_page) {
        this.last_page = last_page;
    }

    private String last_page;

    public class DataBean{
        private String oid;
        private String money;
        private String add_time;
        private String flag;
        private String icon;

        public String getNum() {
            return num;
        }

        public void setNum(String num) {
            this.num = num;
        }

        private String num;

        public String getOid() {
            return oid;
        }

        public void setOid(String oid) {
            this.oid = oid;
        }

        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }

        public String getAdd_time() {
            return add_time;
        }

        public void setAdd_time(String add_time) {
            this.add_time = add_time;
        }

        public String getFlag() {
            return flag;
        }

        public void setFlag(String flag) {
            this.flag = flag;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        private String title;
    }
}
