package com.ysxsoft.apaycoin.modle;

import java.util.ArrayList;

/**
 * 描述： TODO
 * 日期： 2018/11/26 0026 15:38
 * 作者： 胡
 * 公司：郑州亿生信科技有限公司
 */
public class NewAddGoodsEditorListBean {

    /**
     * code : 0
     * msg : 获取成功
     * data : {"0":{"iid":"9","vals":{},"type":"1","icon":"http://ggapay.com/uploads/feedback/20181124/303c697a1dd6ff0331789e7f93633b6c.jpg"},"1":{"iid":"20","vals":"第一行","type":"2","icon":{}},"2":{"iid":"21","vals":"第一行","type":"2","icon":{}},"3":{"iid":"22","vals":"第一行","type":"2","icon":{}},"4":{"iid":"23","vals":"第二行行","type":"2","icon":{}},"5":{"iid":"25","vals":"第三行行","type":"2","icon":{}},"6":{"iid":"10","vals":{},"type":"1","icon":"http://ggapay.com/uploads/feedback/20181124/b6e6f7f64f5c004726d622b8bcdb593c.png"}}
     */

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
        private String iid;
        private String vals;
        private String type;

        public String getIid() {
            return iid;
        }

        public void setIid(String iid) {
            this.iid = iid;
        }

        public String getVals() {
            return vals;
        }

        public void setVals(String vals) {
            this.vals = vals;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
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
