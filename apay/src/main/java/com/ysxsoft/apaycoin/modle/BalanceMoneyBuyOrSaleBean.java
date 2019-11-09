package com.ysxsoft.apaycoin.modle;

import java.util.ArrayList;

/**
 * 描述： TODO
 * 日期： 2018/11/16 0016 15:01
 * 作者： 胡
 * 公司：郑州亿生信科技有限公司
 */
public class BalanceMoneyBuyOrSaleBean {

    private String code;
    private String last_page;
    private String msg;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getLast_page() {
        return last_page;
    }

    public void setLast_page(String last_page) {
        this.last_page = last_page;
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
    public class DataBean{
        private String add_time;
        private String avatar;
        private String gold;
        private String money;
        private String nickname;
        private String oid;

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

        public String getZf_type() {
            return zf_type;
        }

        public void setZf_type(String zf_type) {
            this.zf_type = zf_type;
        }

        private String zf_type;
    }
}
