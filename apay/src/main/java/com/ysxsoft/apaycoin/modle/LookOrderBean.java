package com.ysxsoft.apaycoin.modle;

/**
 * 描述： TODO
 * 日期： 2018/11/19 0019 15:38
 * 作者： 胡
 * 公司：郑州亿生信科技有限公司
 */
public class LookOrderBean {

    /**
     * code : 0
     * msg : 获取成功
     * data : {"mobile":"15838027619","star":"5","alipay":"12321432","khh":"农业应用","khr":"李铭阳","carnum":"6228480018521554479","avatar":"http://ggapay.com/uploads/images/20181108/85c18bf75f1ac27562bce4501b8764fd.png","pic":"","flag":"1"}
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
         * mobile : 15838027619
         * star : 5
         * alipay : 12321432
         * khh : 农业应用
         * khr : 李铭阳
         * carnum : 6228480018521554479
         * avatar : http://ggapay.com/uploads/images/20181108/85c18bf75f1ac27562bce4501b8764fd.png
         * pic :
         * flag : 1
         */

        private String mobile;
        private String star;
        private String alipay;
        private String khh;
        private String khr;
        private String carnum;
        private String avatar;
        private String pic;
        private String flag;

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getStar() {
            return star;
        }

        public void setStar(String star) {
            this.star = star;
        }

        public String getAlipay() {
            return alipay;
        }

        public void setAlipay(String alipay) {
            this.alipay = alipay;
        }

        public String getKhh() {
            return khh;
        }

        public void setKhh(String khh) {
            this.khh = khh;
        }

        public String getKhr() {
            return khr;
        }

        public void setKhr(String khr) {
            this.khr = khr;
        }

        public String getCarnum() {
            return carnum;
        }

        public void setCarnum(String carnum) {
            this.carnum = carnum;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getPic() {
            return pic;
        }

        public void setPic(String pic) {
            this.pic = pic;
        }

        public String getFlag() {
            return flag;
        }

        public void setFlag(String flag) {
            this.flag = flag;
        }
    }
}
