package com.ysxsoft.apaycoin.modle;

/**
 * 描述： TODO
 * 日期： 2018/11/24 0024 17:37
 * 作者： 胡
 * 公司：郑州亿生信科技有限公司
 */
public class NewAddGoodsBean {

    /**
     * code : 0
     * msg : 获取成功
     */

    private String code;
    private String msg;

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    private String pid;

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
