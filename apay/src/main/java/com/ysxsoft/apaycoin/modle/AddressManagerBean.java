package com.ysxsoft.apaycoin.modle;

import java.util.ArrayList;

/**
 * 描述： TODO
 * 日期： 2018/11/15 0015 13:50
 * 作者： 胡
 * 公司：郑州亿生信科技有限公司
 */
public class AddressManagerBean {

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

    public class DataBean{
        private String is_ture;
        private String provice;
        private String city;
        private String area;
        private String address;
        private String linkname;
        private String phone;
        private String aid;


        public String getLinkname() {
            return linkname;
        }

        public void setLinkname(String linkname) {
            this.linkname = linkname;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getIs_ture() {
            return is_ture;
        }

        public void setIs_ture(String is_ture) {
            this.is_ture = is_ture;
        }

        public String getProvice() {
            return provice;
        }

        public void setProvice(String provice) {
            this.provice = provice;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getArea() {
            return area;
        }

        public void setArea(String area) {
            this.area = area;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getAid() {
            return aid;
        }

        public void setAid(String aid) {
            this.aid = aid;
        }

    }

}
