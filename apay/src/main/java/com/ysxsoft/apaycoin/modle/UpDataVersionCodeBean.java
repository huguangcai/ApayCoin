package com.ysxsoft.apaycoin.modle;

public class UpDataVersionCodeBean {

    /**
     * code : 0
     * msg : 获取成功！
     * data : {"id":"1","bbsm":"Android","verCode":"1","version":"1.0.0","fileAbsolutePath":"http://cafe.ysxapp.com/download/身份通.apk","content":"系统升级，优化界面效果、修复部分BU","device":"1","type":"1","updatetime":{}}
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
         * id : 1
         * bbsm : Android
         * verCode : 1
         * version : 1.0.0
         * fileAbsolutePath : http://cafe.ysxapp.com/download/身份通.apk
         * content : 系统升级，优化界面效果、修复部分BU
         * device : 1
         * type : 1
         * updatetime : {}
         */

        private String id;
        private String bbsm;
        private String verCode;
        private String version;
        private String fileAbsolutePath;
        private String content;
        private String device;
        private String type;
        private String updatetime;


        public String getUpdatetime() {
            return updatetime;
        }

        public void setUpdatetime(String updatetime) {
            this.updatetime = updatetime;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getBbsm() {
            return bbsm;
        }

        public void setBbsm(String bbsm) {
            this.bbsm = bbsm;
        }

        public String getVerCode() {
            return verCode;
        }

        public void setVerCode(String verCode) {
            this.verCode = verCode;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public String getFileAbsolutePath() {
            return fileAbsolutePath;
        }

        public void setFileAbsolutePath(String fileAbsolutePath) {
            this.fileAbsolutePath = fileAbsolutePath;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getDevice() {
            return device;
        }

        public void setDevice(String device) {
            this.device = device;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }


    }
}
