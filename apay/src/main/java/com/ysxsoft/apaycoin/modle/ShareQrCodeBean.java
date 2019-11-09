package com.ysxsoft.apaycoin.modle;

/**
 * 描述： TODO
 * 日期： 2018/11/8 0008 13:31
 * 作者： 胡
 * 公司：郑州亿生信科技有限公司
 */
public class ShareQrCodeBean {

    /**
     * code : 0
     * msg : 获取成功！
     * qrData : http://ggapay.com/uploads/images/qrcode/1541405829red.png
     */

    private String code;
    private String msg;
    private String qrData;
    private String invitation;

    public String getInvitation() {
        return invitation;
    }

    public void setInvitation(String invitation) {
        this.invitation = invitation;
    }

    public String getLink_url() {
        return link_url;
    }

    public void setLink_url(String link_url) {
        this.link_url = link_url;
    }

    private String link_url;

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
