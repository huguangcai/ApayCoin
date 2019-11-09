package com.ysxsoft.apaycoin.modle;

public class PhoneSearchUserBean {

    /**
     * code : 0
     * msg : 获取成功！！！
     * data : {"uid":"5","nickname":"李铭阳","avatar":"http://ggapay.com//static/admin/img/avatar.jpg","money":"0.00"}
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
         * uid : 5
         * nickname : 李铭阳
         * avatar : http://ggapay.com//static/admin/img/avatar.jpg
         * money : 0.00
         */

        private String uid;
        private String nickname;
        private String avatar;
        private String money;

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

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }
    }
}
