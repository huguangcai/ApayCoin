package com.ysxsoft.apaycoin.modle;

/**
 * 描述： TODO
 * 日期： 2018/11/8 0008 13:37
 * 作者： 胡
 * 公司：郑州亿生信科技有限公司
 */
public class NoticeDetailBean {
    /**
     * code : 0
     * msg : 获取成功！
     * data : {"nid":"2","title":"标题1标题1标题1标题1标题1","descr":"描述1描述1描述1描述1描述1描述1描述1 描述1描述1描述1描述1描述1描述1描述1","add_time":"2018-10-31 17:22:53","content":"<p>内容1内容1内容1内容1内容1内容1内容1内容1<\/p><p>内容1内容1内容1内容1内容1内容1内容1内容1内容1内容1<\/p><p>内容1内容1内容1内容1内容1内容1内容1内容1内容1内容1内容1<\/p><p>内容1内容1内容1内容1内容1内容1内容1内容1内容1内容1内容1<\/p><p>内容1内容1内容1内容1内容1内容1内容1内容1<\/p><p>内容1内容1内容1内容1内容1<\/p>"}
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
         * nid : 2
         * title : 标题1标题1标题1标题1标题1
         * descr : 描述1描述1描述1描述1描述1描述1描述1 描述1描述1描述1描述1描述1描述1描述1
         * add_time : 2018-10-31 17:22:53
         * content : <p>内容1内容1内容1内容1内容1内容1内容1内容1</p><p>内容1内容1内容1内容1内容1内容1内容1内容1内容1内容1</p><p>内容1内容1内容1内容1内容1内容1内容1内容1内容1内容1内容1</p><p>内容1内容1内容1内容1内容1内容1内容1内容1内容1内容1内容1</p><p>内容1内容1内容1内容1内容1内容1内容1内容1</p><p>内容1内容1内容1内容1内容1</p>
         */

        private String nid;
        private String title;
        private String descr;
        private String add_time;
        private String content;

        public String getNid() {
            return nid;
        }

        public void setNid(String nid) {
            this.nid = nid;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescr() {
            return descr;
        }

        public void setDescr(String descr) {
            this.descr = descr;
        }

        public String getAdd_time() {
            return add_time;
        }

        public void setAdd_time(String add_time) {
            this.add_time = add_time;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }
}
