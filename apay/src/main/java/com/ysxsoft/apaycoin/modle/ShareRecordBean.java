package com.ysxsoft.apaycoin.modle;

import java.util.ArrayList;

public class ShareRecordBean {

    /**
     * code : 0
     * msg : 获取成功！
     * data : {"0":{"uid":"8","nickname":"自由之战","icon":"http://ggapay.com/uploads//20181128/b5abf30caff5738755f6b170a77f935f.jpg","create_time":"2018-11-08 10:28"}}
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
    public class  DataBean{
        private String uid;
        private String nickname;
        private String icon;

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }

        private String create_time;
    }
}
