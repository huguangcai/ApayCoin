package com.ysxsoft.apaycoin.modle;

/**
 * 描述： TODO
 * 日期： 2018/11/24 0024 14:06
 * 作者： 胡
 * 公司：郑州亿生信科技有限公司
 */
public class MyMallHeaderInfoBean {

    /**
     * code : 0
     * msg : 获取成功
     * data : {"name":"我的店铺哦","phone":"110110","cid":"护肤彩妆","star":"5","icon":"http://ggapay.com//static/admin/img/none.png","money":"100","account":"2000"}
     */

    private String code;
    private String msg;
    private DataBean data;

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

    public static class DataBean {
        /**
         * name : 我的店铺哦
         * phone : 110110
         * cid : 护肤彩妆
         * star : 5
         * icon : http://ggapay.com//static/admin/img/none.png
         * money : 100
         * account : 2000
         */

        private String name;
        private String phone;
        private String cid;
        private String star;
        private String icon;
        private String money;
        private String account;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getCid() {
            return cid;
        }

        public void setCid(String cid) {
            this.cid = cid;
        }

        public String getStar() {
            return star;
        }

        public void setStar(String star) {
            this.star = star;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }

        public String getAccount() {
            return account;
        }

        public void setAccount(String account) {
            this.account = account;
        }
    }
}
