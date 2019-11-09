package com.ysxsoft.apaycoin.modle;

import com.google.gson.annotations.SerializedName;

/**
 * 描述： TODO
 * 日期： 2018/11/8 0008 11:47
 * 作者： 胡
 * 公司：郑州亿生信科技有限公司
 */
public class MyBankCardListBean {
    /**
     * code : 0
     * msg : 获取成功！
     * data : {"0":{"khh":"中国农业银行","card_num":"3243232423432","name":"李铭阳","cid":"3"},"1":{"khh":"中国农业银行","card_num":"32432432214","name":"李铭阳","cid":"4"}}
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
         * 0 : {"khh":"中国农业银行","card_num":"3243232423432","name":"李铭阳","cid":"3"}
         * 1 : {"khh":"中国农业银行","card_num":"32432432214","name":"李铭阳","cid":"4"}
         */

        @SerializedName("0")
        private _$0Bean _$0;
        @SerializedName("1")
        private _$1Bean _$1;

        public _$0Bean get_$0() {
            return _$0;
        }

        public void set_$0(_$0Bean _$0) {
            this._$0 = _$0;
        }

        public _$1Bean get_$1() {
            return _$1;
        }

        public void set_$1(_$1Bean _$1) {
            this._$1 = _$1;
        }

        public static class _$0Bean {
            /**
             * khh : 中国农业银行
             * card_num : 3243232423432
             * name : 李铭阳
             * cid : 3
             */

            private String khh;
            private String card_num;
            private String name;
            private String cid;

            public String getKhh() {
                return khh;
            }

            public void setKhh(String khh) {
                this.khh = khh;
            }

            public String getCard_num() {
                return card_num;
            }

            public void setCard_num(String card_num) {
                this.card_num = card_num;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getCid() {
                return cid;
            }

            public void setCid(String cid) {
                this.cid = cid;
            }
        }

        public static class _$1Bean {
            /**
             * khh : 中国农业银行
             * card_num : 32432432214
             * name : 李铭阳
             * cid : 4
             */

            private String khh;
            private String card_num;
            private String name;
            private String cid;

            public String getKhh() {
                return khh;
            }

            public void setKhh(String khh) {
                this.khh = khh;
            }

            public String getCard_num() {
                return card_num;
            }

            public void setCard_num(String card_num) {
                this.card_num = card_num;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getCid() {
                return cid;
            }

            public void setCid(String cid) {
                this.cid = cid;
            }
        }
    }
}
