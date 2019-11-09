package com.ysxsoft.apaycoin.modle;

/**
 * 描述： TODO
 * 日期： 2018/11/23 0023 15:45
 * 作者： 胡
 * 公司：郑州亿生信科技有限公司
 */
public class MyWalletAddressBean {


    /**
     * code : 0
     * msg : 获取成功！
     * wallets : bc043d8b24dea7034831c9023432ac51
     * qrData : http://ggapay.com/uploads/images/wallets/1541752061red.png
     */

    private String code;
    private String msg;
    private String wallets;
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

    public String getWallets() {
        return wallets;
    }

    public void setWallets(String wallets) {
        this.wallets = wallets;
    }

    public String getQrData() {
        return qrData;
    }

    public void setQrData(String qrData) {
        this.qrData = qrData;
    }
}
