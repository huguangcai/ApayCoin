package com.ysxsoft.apaycoin.modle;

import java.util.ArrayList;

/**
 * 描述： TODO
 * 日期： 2018/11/26 0026 11:14
 * 作者： 胡
 * 公司：郑州亿生信科技有限公司
 */
public class MallGoodsDetialHeaderBean {

    /**
     * code : 0
     * msg : 获取成功
     * data : {"title":"产品1333","money":"200.00","stock":"100","lunbo":{"0":{"img_id":"11","icon":"http://ggapay.com/uploads/feedback/20181124/25bd10069031b67bbd10dcaa3ccf63c0.jpg"},"1":{"img_id":"12","icon":"http://ggapay.com/uploads/feedback/20181124/bf982dbc2d3a4952022144cb34ad63b3.png"}}}
     */

    private String code;

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

    private String msg;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    private DataBean data;

    public class DataBean {
        private ArrayList<LunBo> lunbo;
        private String money;
        private String stock;

        public ArrayList<LunBo> getLunbo() {
            return lunbo;
        }

        public void setLunbo(ArrayList<LunBo> lunbo) {
            this.lunbo = lunbo;
        }

        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }

        public String getStock() {
            return stock;
        }

        public void setStock(String stock) {
            this.stock = stock;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        private String title;
        private String uid;
        private String pid;
        private String flag;
        private String pro_icon;

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getPid() {
            return pid;
        }

        public void setPid(String pid) {
            this.pid = pid;
        }

        public String getFlag() {
            return flag;
        }

        public void setFlag(String flag) {
            this.flag = flag;
        }

        public String getPro_icon() {
            return pro_icon;
        }

        public void setPro_icon(String pro_icon) {
            this.pro_icon = pro_icon;
        }

        public String getKefy() {
            return kefy;
        }

        public void setKefy(String kefy) {
            this.kefy = kefy;
        }

        private String kefy;


        public class LunBo {
            private String icon;

            public String getIcon() {
                return icon;
            }

            public void setIcon(String icon) {
                this.icon = icon;
            }

            public String getImg_id() {
                return img_id;
            }

            public void setImg_id(String img_id) {
                this.img_id = img_id;
            }

            private String img_id;
        }

    }
}
