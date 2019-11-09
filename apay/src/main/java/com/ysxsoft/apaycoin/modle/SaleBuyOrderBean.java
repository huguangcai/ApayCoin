package com.ysxsoft.apaycoin.modle;

import java.util.ArrayList;

/**
 * 描述： TODO
 * 日期： 2018/11/16 0016 11:32
 * 作者： 胡
 * 公司：郑州亿生信科技有限公司
 */
public class SaleBuyOrderBean {
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

    public class DataBean{
        private String add_time;
        private String avatar;
        private String flag;
        private String gold;
        private String money;
        private String nickname;
        private String oid;
        private String qr_time;
        private String sy_time;

        public String getSy_time() {
            return sy_time;
        }

        public void setSy_time(String sy_time) {
            this.sy_time = sy_time;
        }

        public String getDk_time() {
            return dk_time;
        }

        public void setDk_time(String dk_time) {
            this.dk_time = dk_time;
        }

        private String dk_time;


        public String getQr_time() {
            return qr_time;
        }

        public void setQr_time(String qr_time) {
            this.qr_time = qr_time;
        }

        public String getAdd_time() {
            return add_time;
        }

        public void setAdd_time(String add_time) {
            this.add_time = add_time;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getFlag() {
            return flag;
        }

        public void setFlag(String flag) {
            this.flag = flag;
        }

        public String getGold() {
            return gold;
        }

        public void setGold(String gold) {
            this.gold = gold;
        }

        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getOid() {
            return oid;
        }

        public void setOid(String oid) {
            this.oid = oid;
        }

    }
}
