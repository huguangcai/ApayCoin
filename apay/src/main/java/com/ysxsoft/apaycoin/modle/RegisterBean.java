package com.ysxsoft.apaycoin.modle;

/**
 * 描述： TODO
 * 日期： 2018/11/6 0006 10:44
 * 作者： 胡
 * 公司：郑州亿生信科技有限公司
 */
public class RegisterBean {

    /**
     * code : 1
     * msg : 验证码错误或已过期！
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
}
