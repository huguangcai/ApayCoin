package com.ysxsoft.apaycoin.modle;

import java.util.ArrayList;

public class OnlineMallGoodsListBean {

    /**
     * code : 0
     * msg : 获取成功
     * data : {"0":{"pid":"22","uid":"4","title":"吃了吗","money":"523.00","stock":"632","icon":"http://ggapay.com/uploads/feedback/20181126/06e33d56d9466b747ea540873c69df6b.jpg"},"1":{"pid":"22","uid":"4","title":"吃了吗","money":"523.00","stock":"632","icon":"http://ggapay.com/uploads/feedback/20181126/9223788a1c4f6c8b1acf1437f13c8c4f.jpg"},"2":{"pid":"22","uid":"4","title":"吃了吗","money":"523.00","stock":"632","icon":"http://ggapay.com/uploads/feedback/20181126/c366075f11a612b1423ce80bdf597e7b.jpg"},"3":{"pid":"21","uid":"4","title":"这是商品","money":"153.00","stock":"200","icon":"http://ggapay.com/uploads/feedback/20181126/4617bc5c68b5626c8ffe27f5b892a2e4.jpg"},"4":{"pid":"20","uid":"4","title":"nihao","money":"8546.00","stock":"6352","icon":"http://ggapay.com/uploads/feedback/20181126/05dc1a8b4128b694f8e61d4e7e61f426.jpg"},"5":{"pid":"19","uid":"4","title":"525","money":"525235.00","stock":"43","icon":"http://ggapay.com/uploads/feedback/20181126/c04314a23aca48f5aa34a275917f19ce.jpg"},"6":{"pid":"18","uid":"4","title":"520","money":"520.00","stock":"520","icon":"http://ggapay.com/uploads/feedback/20181126/6ba59e8348ca80a24554d29ac0ce25c4.jpg"},"7":{"pid":"17","uid":"4","title":"Dresden","money":"120.00","stock":"520","icon":"http://ggapay.com/uploads/feedback/20181126/ab85b582b739a513213a2d0adb410935.jpg"},"8":{"pid":"16","uid":"4","title":"ooooo","money":"120.00","stock":"520","icon":"http://ggapay.com/uploads/feedback/20181126/9f1494e7d0ad1a44fe11bc85141c6e51.jpg"},"9":{"pid":"15","uid":"4","title":"gdfgdf","money":"520.00","stock":"1265","icon":"http://ggapay.com/uploads/feedback/20181124/1010d407ed518a7af34f310ab00d6d2f.jpg"}}
     * last_page : 3
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

    public class DataBean{
        private String pid;
        private String uid;
        private String title;
        private String money;
        private String stock;

        public String getPid() {
            return pid;
        }

        public void setPid(String pid) {
            this.pid = pid;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
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

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        private String icon;
    }
}
