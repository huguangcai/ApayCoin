package com.ysxsoft.apaycoin.modle;

/**
 * 描述： TODO
 * 日期： 2018/11/6 0006 10:52
 * 作者： 胡
 * 公司：郑州亿生信科技有限公司
 */
public class AboutMyBean {

    /**
     * code : 0
     * msg : 获取成功！
     * data : {"content":"<p>关于我们<\/p>"}
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
         * content : <p>关于我们</p>
         */

        private String content;

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }
}
