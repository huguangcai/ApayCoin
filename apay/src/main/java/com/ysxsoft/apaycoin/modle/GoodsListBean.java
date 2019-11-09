package com.ysxsoft.apaycoin.modle;

import java.util.ArrayList;

/**
 * 描述： TODO
 * 日期： 2018/11/26 0026 09:15
 * 作者： 胡
 * 公司：郑州亿生信科技有限公司
 */
public class GoodsListBean {

    /**
     * code : 1
     * msg : 操作成功
     * data : {"0":{"add_time":"2018-11-24 14:00","title":"产品1333","stock":"100","flag":"0","pid":"13","imgs":"http://ggapay.com///uploads/feedback/20181124/25bd10069031b67bbd10dcaa3ccf63c0.jpg"},"1":{"add_time":"2018-11-24 14:00","title":"产品1","stock":"100","flag":"0","pid":"12","imgs":"http://ggapay.com///uploads/feedback/20181124/303c697a1dd6ff0331789e7f93633b6c.jpg"},"2":{"add_time":"2018-11-24 13:58","title":"产品1","stock":"100","flag":"0","pid":"11","imgs":"http://ggapay.com///uploads/feedback/20181124/5115a79eb6901f3993f7aebb607777ac.jpg"}}
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

    public class DataBean{
        private String add_time;
        private String title;
        private String stock;
        private String flag;
        private String pid;

        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }

        private String money;

        public String getAdd_time() {
            return add_time;
        }

        public void setAdd_time(String add_time) {
            this.add_time = add_time;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getStock() {
            return stock;
        }

        public void setStock(String stock) {
            this.stock = stock;
        }

        public String getFlag() {
            return flag;
        }

        public void setFlag(String flag) {
            this.flag = flag;
        }

        public String getPid() {
            return pid;
        }

        public void setPid(String pid) {
            this.pid = pid;
        }

        public String getImgs() {
            return imgs;
        }

        public void setImgs(String imgs) {
            this.imgs = imgs;
        }

        private String imgs;
    }
}
