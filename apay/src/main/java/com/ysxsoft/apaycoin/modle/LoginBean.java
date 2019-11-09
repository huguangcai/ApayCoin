package com.ysxsoft.apaycoin.modle;

/**
 * 描述： 登录的对象
 * 日期： 2018/11/6 0006 10:40
 * 作者： 胡
 * 公司：郑州亿生信科技有限公司
 */
public class LoginBean  {
    /**
     * code : 0
     * msg : 登陆成功！
     * userinfo : {"uid":8,"group":0,"role":2,"role_name":"普通用户","avatar":"http://ggapay.com/uploads/20181126/7fd8cfe434dca2f6cd3ba15899a9e0e3.jpg","username":"13253565026","mobile":"13253565026","nickname":"自由之战","last_login_time":1543380353,"money":"9007060.00","score":"499907.0000","quans":"6017940.00","star":5,"last_login_ip":"1933359502","uptime":"20181128","sign":"Mzc0NjgxNTQzMzgwMzU0LDZkNjNkMzgwODNiNjIxY2UwMzNhN2JlNWFlN2MxMTM3ZjM2ZDZiNDA=","flag":"0"}
     */

    private String code;
    private String msg;
    private UserinfoBean userinfo;

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

    public UserinfoBean getUserinfo() {
        return userinfo;
    }

    public void setUserinfo(UserinfoBean userinfo) {
        this.userinfo = userinfo;
    }

    public  class UserinfoBean {
        /**
         * uid : 8
         * group : 0
         * role : 2
         * role_name : 普通用户
         * avatar : http://ggapay.com/uploads/20181126/7fd8cfe434dca2f6cd3ba15899a9e0e3.jpg
         * username : 13253565026
         * mobile : 13253565026
         * nickname : 自由之战
         * last_login_time : 1543380353
         * money : 9007060.00
         * score : 499907.0000
         * quans : 6017940.00
         * star : 5
         * last_login_ip : 1933359502
         * uptime : 20181128
         * sign : Mzc0NjgxNTQzMzgwMzU0LDZkNjNkMzgwODNiNjIxY2UwMzNhN2JlNWFlN2MxMTM3ZjM2ZDZiNDA=
         * flag : 0
         */

        private String uid;
        private String group;
        private String role;
        private String role_name;
        private String avatar;
        private String username;
        private String mobile;
        private String nickname;
        private String last_login_time;
        private String money;
        private String score;
        private String quans;
        private String star;
        private String last_login_ip;
        private String uptime;
        private String sign;
        private String flag;
        private String ff;

        public String getFf() {
            return ff;
        }

        public void setFf(String ff) {
            this.ff = ff;
        }


        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getGroup() {
            return group;
        }

        public void setGroup(String group) {
            this.group = group;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }

        public String getLast_login_time() {
            return last_login_time;
        }

        public void setLast_login_time(String last_login_time) {
            this.last_login_time = last_login_time;
        }

        public String getStar() {
            return star;
        }

        public void setStar(String star) {
            this.star = star;
        }

        public String getRole_name() {
            return role_name;
        }

        public void setRole_name(String role_name) {
            this.role_name = role_name;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }

        public String getScore() {
            return score;
        }

        public void setScore(String score) {
            this.score = score;
        }

        public String getQuans() {
            return quans;
        }

        public void setQuans(String quans) {
            this.quans = quans;
        }

        public String getLast_login_ip() {
            return last_login_ip;
        }

        public void setLast_login_ip(String last_login_ip) {
            this.last_login_ip = last_login_ip;
        }

        public String getUptime() {
            return uptime;
        }

        public void setUptime(String uptime) {
            this.uptime = uptime;
        }

        public String getSign() {
            return sign;
        }

        public void setSign(String sign) {
            this.sign = sign;
        }

        public String getFlag() {
            return flag;
        }

        public void setFlag(String flag) {
            this.flag = flag;
        }
    }

//    /**
//     * code : 0
//     * msg : 登陆成功！
//     * userinfo : {"avatar":"http://ggapay.com/uploads/20181126/7fd8cfe434dca2f6cd3ba15899a9e0e3.jpg","flag":"12060","group":"0","last_login_ip":"2014946118","last_login_time":"1543212003","mobile":"13253565026","money":"8995000.00","nickname":"自由之战","quans":"6030000.00","role":"2","role_name":"普通用户","score":"499900.0000","sign":"MzA1MTgxNTQzMjEyMDAzLDdjOTY3MWIyYWIyODY1ZmE5ODYzZTE3NzgyMWZlYmM5NTFiYWQyMzU=","star":"5","uid":"8","uptime":{},"username":"13253565026"}
//     */
//
//    private String code;
//    private String msg;
//    private UserinfoBean userinfo;
//
//    public String getCode() {
//        return code;
//    }
//
//    public void setCode(String code) {
//        this.code = code;
//    }
//
//    public String getMsg() {
//        return msg;
//    }
//
//    public void setMsg(String msg) {
//        this.msg = msg;
//    }
//
//    public UserinfoBean getUserinfo() {
//        return userinfo;
//    }
//
//    public void setUserinfo(UserinfoBean userinfo) {
//        this.userinfo = userinfo;
//    }
//
//    public static class UserinfoBean {
//        /**
//         * avatar : http://ggapay.com/uploads/20181126/7fd8cfe434dca2f6cd3ba15899a9e0e3.jpg
//         * flag : 12060
//         * group : 0
//         * last_login_ip : 2014946118
//         * last_login_time : 1543212003
//         * mobile : 13253565026
//         * money : 8995000.00
//         * nickname : 自由之战
//         * quans : 6030000.00
//         * role : 2
//         * role_name : 普通用户
//         * score : 499900.0000
//         * sign : MzA1MTgxNTQzMjEyMDAzLDdjOTY3MWIyYWIyODY1ZmE5ODYzZTE3NzgyMWZlYmM5NTFiYWQyMzU=
//         * star : 5
//         * uid : 8
//         * uptime : {}
//         * username : 13253565026
//         */
//
//        private String avatar;
//        private String flag;
//        private String group;
//        private String last_login_ip;
//        private String last_login_time;
//        private String mobile;
//        private String money;
//        private String nickname;
//        private String quans;
//        private String role;
//        private String role_name;
//        private String score;
//        private String sign;
//        private String star;
//        private String uid;
//        private UptimeBean uptime;
//        private String username;
//
//        public String getAvatar() {
//            return avatar;
//        }
//
//        public void setAvatar(String avatar) {
//            this.avatar = avatar;
//        }
//
//        public String getFlag() {
//            return flag;
//        }
//
//        public void setFlag(String flag) {
//            this.flag = flag;
//        }
//
//        public String getGroup() {
//            return group;
//        }
//
//        public void setGroup(String group) {
//            this.group = group;
//        }
//
//        public String getLast_login_ip() {
//            return last_login_ip;
//        }
//
//        public void setLast_login_ip(String last_login_ip) {
//            this.last_login_ip = last_login_ip;
//        }
//
//        public String getLast_login_time() {
//            return last_login_time;
//        }
//
//        public void setLast_login_time(String last_login_time) {
//            this.last_login_time = last_login_time;
//        }
//
//        public String getMobile() {
//            return mobile;
//        }
//
//        public void setMobile(String mobile) {
//            this.mobile = mobile;
//        }
//
//        public String getMoney() {
//            return money;
//        }
//
//        public void setMoney(String money) {
//            this.money = money;
//        }
//
//        public String getNickname() {
//            return nickname;
//        }
//
//        public void setNickname(String nickname) {
//            this.nickname = nickname;
//        }
//
//        public String getQuans() {
//            return quans;
//        }
//
//        public void setQuans(String quans) {
//            this.quans = quans;
//        }
//
//        public String getRole() {
//            return role;
//        }
//
//        public void setRole(String role) {
//            this.role = role;
//        }
//
//        public String getRole_name() {
//            return role_name;
//        }
//
//        public void setRole_name(String role_name) {
//            this.role_name = role_name;
//        }
//
//        public String getScore() {
//            return score;
//        }
//
//        public void setScore(String score) {
//            this.score = score;
//        }
//
//        public String getSign() {
//            return sign;
//        }
//
//        public void setSign(String sign) {
//            this.sign = sign;
//        }
//
//        public String getStar() {
//            return star;
//        }
//
//        public void setStar(String star) {
//            this.star = star;
//        }
//
//        public String getUid() {
//            return uid;
//        }
//
//        public void setUid(String uid) {
//            this.uid = uid;
//        }
//
//        public UptimeBean getUptime() {
//            return uptime;
//        }
//
//        public void setUptime(UptimeBean uptime) {
//            this.uptime = uptime;
//        }
//
//        public String getUsername() {
//            return username;
//        }
//
//        public void setUsername(String username) {
//            this.username = username;
//        }
//
//        public static class UptimeBean {
//        }
//    }


//
//    /**
//     * code : 0
//     * msg : 登陆成功！
//     * userinfo : {"uid":"5","group":"0","role":"2","role_name":{},"avatar":"http://ggapay.com/uploads/images/20181108/85c18bf75f1ac27562bce4501b8764fd.png","username":"15838027619","mobile":"15838027619","nickname":"李铭阳","last_login_time":"1541667081","money":"1586.00","score":"0.00","quans":"6824","star":"5","last_login_ip":"2014946118","sign":"NTQxMDUxNTQxNjY3MDgxLDFkZmJjOTAyYzMyZTljZjBjNTUwNWY2MTQ1M2IxMzRmYmEwMGUwODc="}
//     */
//
//    private String code;
//    private String msg;
//    private UserinfoBean userinfo;
//
//    public String getCode() {
//        return code;
//    }
//
//    public void setCode(String code) {
//        this.code = code;
//    }
//
//    public String getMsg() {
//        return msg;
//    }
//
//    public void setMsg(String msg) {
//        this.msg = msg;
//    }
//
//    public UserinfoBean getUserinfo() {
//        return userinfo;
//    }
//
//    public void setUserinfo(UserinfoBean userinfo) {
//        this.userinfo = userinfo;
//    }
//
//    public static class UserinfoBean {
//        /**
//         * uid : 5
//         * group : 0
//         * role : 2
//         * role_name : {}
//         * avatar : http://ggapay.com/uploads/images/20181108/85c18bf75f1ac27562bce4501b8764fd.png
//         * username : 15838027619
//         * mobile : 15838027619
//         * nickname : 李铭阳
//         * last_login_time : 1541667081
//         * money : 1586.00
//         * score : 0.00
//         * quans : 6824
//         * star : 5
//         * last_login_ip : 2014946118
//         * sign : NTQxMDUxNTQxNjY3MDgxLDFkZmJjOTAyYzMyZTljZjBjNTUwNWY2MTQ1M2IxMzRmYmEwMGUwODc=
//         */
//
//        private String uid;
//        private String group;
//        private String role;
//        private RoleNameBean role_name;
//        private String avatar;
//        private String username;
//        private String mobile;
//        private String nickname;
//        private String last_login_time;
//        private String money;
//        private String score;
//        private String quans;
//        private String star;
//        private String last_login_ip;
//        private String sign;
//        private String flag;//多返回了个 flag  	为0不可领取 否则就是可以领取的数量
//
//        public String getFlag() {
//            return flag;
//        }
//
//        public void setFlag(String flag) {
//            this.flag = flag;
//        }
//
//        public String getUid() {
//            return uid;
//        }
//
//        public void setUid(String uid) {
//            this.uid = uid;
//        }
//
//        public String getGroup() {
//            return group;
//        }
//
//        public void setGroup(String group) {
//            this.group = group;
//        }
//
//        public String getRole() {
//            return role;
//        }
//
//        public void setRole(String role) {
//            this.role = role;
//        }
//
//        public RoleNameBean getRole_name() {
//            return role_name;
//        }
//
//        public void setRole_name(RoleNameBean role_name) {
//            this.role_name = role_name;
//        }
//
//        public String getAvatar() {
//            return avatar;
//        }
//
//        public void setAvatar(String avatar) {
//            this.avatar = avatar;
//        }
//
//        public String getUsername() {
//            return username;
//        }
//
//        public void setUsername(String username) {
//            this.username = username;
//        }
//
//        public String getMobile() {
//            return mobile;
//        }
//
//        public void setMobile(String mobile) {
//            this.mobile = mobile;
//        }
//
//        public String getNickname() {
//            return nickname;
//        }
//
//        public void setNickname(String nickname) {
//            this.nickname = nickname;
//        }
//
//        public String getLast_login_time() {
//            return last_login_time;
//        }
//
//        public void setLast_login_time(String last_login_time) {
//            this.last_login_time = last_login_time;
//        }
//
//        public String getMoney() {
//            return money;
//        }
//
//        public void setMoney(String money) {
//            this.money = money;
//        }
//
//        public String getScore() {
//            return score;
//        }
//
//        public void setScore(String score) {
//            this.score = score;
//        }
//
//        public String getQuans() {
//            return quans;
//        }
//
//        public void setQuans(String quans) {
//            this.quans = quans;
//        }
//
//        public String getStar() {
//            return star;
//        }
//
//        public void setStar(String star) {
//            this.star = star;
//        }
//
//        public String getLast_login_ip() {
//            return last_login_ip;
//        }
//
//        public void setLast_login_ip(String last_login_ip) {
//            this.last_login_ip = last_login_ip;
//        }
//
//        public String getSign() {
//            return sign;
//        }
//
//        public void setSign(String sign) {
//            this.sign = sign;
//        }
//
//        public static class RoleNameBean {
//        }
//    }
}
