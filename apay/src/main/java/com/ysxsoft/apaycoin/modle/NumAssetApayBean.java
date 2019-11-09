package com.ysxsoft.apaycoin.modle;

/**
 * 描述： TODO
 * 日期： 2018/11/16 0016 10:30
 * 作者： 胡
 * 公司：郑州亿生信科技有限公司
 */
public class NumAssetApayBean {


    /**
     * code : 0
     * msg : 获取成功
     * data : {"gold":"563.0000","money":"682.80","jiaoyi":"1.0240"}
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
         * gold : 563.0000
         * money : 682.80
         * jiaoyi : 1.0240
         */

        private String gold;
        private String money;
        private String jiaoyi;

        public String getShops() {
            return shops;
        }

        public void setShops(String shops) {
            this.shops = shops;
        }

        private String shops;


        public String getShop_flag() {
            return shop_flag;
        }

        public void setShop_flag(String shop_flag) {
            this.shop_flag = shop_flag;
        }

        private String shop_flag;

        public String getQuans() {
            return quans;
        }

        public void setQuans(String quans) {
            this.quans = quans;
        }

        private String quans;

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

        public String getJiaoyi() {
            return jiaoyi;
        }

        public void setJiaoyi(String jiaoyi) {
            this.jiaoyi = jiaoyi;
        }
    }
}
