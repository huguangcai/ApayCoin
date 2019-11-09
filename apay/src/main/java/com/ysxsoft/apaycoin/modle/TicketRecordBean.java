package com.ysxsoft.apaycoin.modle;

import java.util.List;

/**
 * 描述： TODO
 * 日期： 2018/11/14 0014 10:29
 * 作者： 胡
 * 公司：郑州亿生信科技有限公司
 */
public class TicketRecordBean {
    private String code;
    private String msg;
    private String last_page;
    private List<DataBean> data;

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

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }
    public class DataBean {
        private String money;
        private String add_time;

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

        private String flag;
    }
}
