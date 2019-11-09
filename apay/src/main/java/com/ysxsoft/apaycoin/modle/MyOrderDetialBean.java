package com.ysxsoft.apaycoin.modle;

public class MyOrderDetialBean {

    /**
     * code : 0
     * msg : 获取成功
     * data : {"uid":"4","aid":"11","oid":"1","price":"523.0000","money":"1046","num":"2","add_time":"1542870224","add_times":"2018-11-22 15:03","fh_time":{},"c1":"备注","c2":{},"qr_time":{},"flag":"0","icon":"http://ggapay.com//uploads/feedback/20181126/9223788a1c4f6c8b1acf1437f13c8c4f.jpg","title":"吃了吗","address":"北京市 东城区  gbvghvbbbvhbhghdssdghhjjjjgyydffvbbbbnnnnnnn","phone":"15713823323","linkname":"ghghghbh","shop_phone":"15713823323","shopname":"3333","order_sn":"154287022441"}
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
         * uid : 4
         * aid : 11
         * oid : 1
         * price : 523.0000
         * money : 1046
         * num : 2
         * add_time : 1542870224
         * add_times : 2018-11-22 15:03
         * fh_time : {}
         * c1 : 备注
         * c2 : {}
         * qr_time : {}
         * flag : 0
         * icon : http://ggapay.com//uploads/feedback/20181126/9223788a1c4f6c8b1acf1437f13c8c4f.jpg
         * title : 吃了吗
         * address : 北京市 东城区  gbvghvbbbvhbhghdssdghhjjjjgyydffvbbbbnnnnnnn
         * phone : 15713823323
         * linkname : ghghghbh
         * shop_phone : 15713823323
         * shopname : 3333
         * order_sn : 154287022441
         */

        private String uid;
        private String aid;
        private String oid;
        private String price;
        private String money;
        private String num;
        private String add_time;
        private String add_times;
        private String fh_time;
        private String c1;
        private String c2;

        public String getFh_time() {
            return fh_time;
        }

        public void setFh_time(String fh_time) {
            this.fh_time = fh_time;
        }

        public String getC2() {
            return c2;
        }

        public void setC2(String c2) {
            this.c2 = c2;
        }

        public String getQr_time() {
            return qr_time;
        }

        public void setQr_time(String qr_time) {
            this.qr_time = qr_time;
        }

        private String qr_time;
        private String flag;
        private String icon;
        private String title;
        private String address;
        private String phone;
        private String linkname;
        private String shop_phone;
        private String shopname;
        private String order_sn;

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getAid() {
            return aid;
        }

        public void setAid(String aid) {
            this.aid = aid;
        }

        public String getOid() {
            return oid;
        }

        public void setOid(String oid) {
            this.oid = oid;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }

        public String getNum() {
            return num;
        }

        public void setNum(String num) {
            this.num = num;
        }

        public String getAdd_time() {
            return add_time;
        }

        public void setAdd_time(String add_time) {
            this.add_time = add_time;
        }

        public String getAdd_times() {
            return add_times;
        }

        public void setAdd_times(String add_times) {
            this.add_times = add_times;
        }



        public String getC1() {
            return c1;
        }

        public void setC1(String c1) {
            this.c1 = c1;
        }





        public String getFlag() {
            return flag;
        }

        public void setFlag(String flag) {
            this.flag = flag;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getLinkname() {
            return linkname;
        }

        public void setLinkname(String linkname) {
            this.linkname = linkname;
        }

        public String getShop_phone() {
            return shop_phone;
        }

        public void setShop_phone(String shop_phone) {
            this.shop_phone = shop_phone;
        }

        public String getShopname() {
            return shopname;
        }

        public void setShopname(String shopname) {
            this.shopname = shopname;
        }

        public String getOrder_sn() {
            return order_sn;
        }

        public void setOrder_sn(String order_sn) {
            this.order_sn = order_sn;
        }




    }
}
