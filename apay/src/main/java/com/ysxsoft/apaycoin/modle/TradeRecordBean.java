package com.ysxsoft.apaycoin.modle;

import java.util.ArrayList;

/**
 * 描述： TODO
 * 日期： 2018/11/19 0019 17:33
 * 作者： 胡
 * 公司：郑州亿生信科技有限公司
 */
public class TradeRecordBean {
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
        private String gold;
        private String nickname;
        private String type;

        public String getAdd_time() {
            return add_time;
        }

        public void setAdd_time(String add_time) {
            this.add_time = add_time;
        }

        public String getGold() {
            return gold;
        }

        public void setGold(String gold) {
            this.gold = gold;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getVlas() {
            return vlas;
        }

        public void setVlas(String vlas) {
            this.vlas = vlas;
        }

        private String vlas;
    }

}
