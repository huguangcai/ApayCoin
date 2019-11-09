package com.ysxsoft.apaycoin.modle;

/**
 * 描述： TODO
 * 日期： 2018/11/9 0009 15:17
 * 作者： 胡
 * 公司：郑州亿生信科技有限公司
 */
public class RollInputBean {

    /**
     * code : 0
     * msg : 获取成功！
     * qrData : http://ggapay.com/uploads/images/qrcode_money/1541731270red.png
     */

    private String code;
    private String msg;
    private String qrData;

    public String getQrcode() {
        return qrcode;
    }

    public void setQrcode(String qrcode) {
        this.qrcode = qrcode;
    }

    private String qrcode;

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

    public String getQrData() {
        return qrData;
    }

    public void setQrData(String qrData) {
        this.qrData = qrData;
    }
}
