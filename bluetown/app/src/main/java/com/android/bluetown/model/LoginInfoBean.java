package com.android.bluetown.model;

import java.util.List;

/**
 * Created by Dafen on 2018/7/13.
 */

public class LoginInfoBean {

    /**
     * data : {"memberUser":{"user":{"userId":"110810002862","telphone":"18858041077","latitude":"0.0","longitude":"0.0","headImg":"https://xapi.smartparks.cn:9010/BLUECITY/SJYS_IMG/7852_20180712_233424698_4485.jpg","createDate":"2018-05-02 22:08","nickName":"哈哈哈哈","isLocking":"0","payPassword":"96e79218965eb72c92a549dd5a330112","tellphone":"18858041077","pttId":"55910193c0b8faa387ffca92d8489c93","userCode":"FMSAFBJCAPCKJSUN","lockingExpireTime":"2018-05-18 23:46:43","smallMoney":100,"ppId":"https://xapi.smartparks.cn:9010/BLUECITY/SJYS_IMG/6961_20180709_141440191_57.jpg","ooId":"https://xapi.smartparks.cn:9010/BLUECITY/SJYS_IMG/5756_20180709_141440217_4766.jpg","stampImg":"https://xapi.smartparks.cn:9010/BLUECITY/SJYS_IMG/7241_20180709_141440320_5243.jpg","handPassword":null,"checkName":"adminhu","checkTime":"2018-07-10 18:00:39","checkState":"1","companyId":"111310000220","idCard":"511623199304176996","birthday":null,"sex":"1","umoney":null,"integral":null,"email":null,"cfmPwd":null,"smallNoPayPassword":"1","isDeleted":"0","llId":null,"idType":"0","garden":"113110000020","password":"96e79218965eb72c92a549dd5a330112","name":"程敬强","state":"1","type":"0"},"headImg":"https://xapi.smartparks.cn:9010/BLUECITY/SJYS_IMG/https://xapi.smartparks.cn:9010/BLUECITY/SJYS_IMG/7852_20180712_233424698_4485.jpg","memberId":"110810002862","nickName":"哈哈哈哈","companyName":"浙江鸿源建设工程有限公司","statu":"3","sex":"1","hotRegion":"宁波智慧园","menus":[{"code":"cwyy","createTime":"2016-05-04 10:16:26","isDelete":"0","createName":"超级管理员","authName":"车位预约","authId":"114210000002","updateName":"超级管理员","updateTime":"2016-06-13 15:04:32","description":"车位预约","type":"1"},{"code":"yqjk","createTime":"2016-06-13 15:05:20","isDelete":"0","createName":"超级管理员","authName":"园区监控","authId":"114210000004","updateName":"超级管理员","updateTime":"2016-06-13 15:06:00","description":"园区监控","type":"1"}],"gardenId":"113110000020","token":{"userId":"110810002862","createTime":"2018-07-13 18:19:04","expirationTime":"2018-08-12 18:19:04","isDeleted":0,"updateTime":"2018-07-13 18:19:04","isOverdue":null,"isForbidden":null,"ctid":"f56064f277c843d287c9a2a9a4bd37b6","cashierId":null,"token":"f56064f277c843d287c9a2a9a4bd37b6"},"roles":[{"code":"memberUser","createTime":"2016-05-03 15:03:36","isDelete":"0","createName":"超级管理员","roleId":"114110000000","roleName":"注册用户","updateName":"超级管理员","updateTime":"2016-07-11 00:00:00","type":"1"},{"code":"gardenEnterprise","createTime":"2016-05-05 09:52:48","isDelete":"0","createName":"超级管理员","roleId":"114110000001","roleName":"认证用户","updateName":"超级管理员","updateTime":"2016-06-26 00:00:00","type":"0"}],"address":null,"name":"程敬强"},"rong":{"token":"NiAl3Qsyz58OS5uNKOZi97bKQVnufmng5d4U+AFUNjLsnoR6n/Qa4vY2tX0eVdV1wOzwt3ZO7N4ejaTtj+k1wBHgI83YOqj3","code":"200","userId":"110810002862"}}
     * repCode : 000000
     * repMsg : 成功!
     */

    private DataBean data;
    private String repCode;
    private String repMsg;

    public static class DataBean {
        /**
         * memberUser : {"user":{"userId":"110810002862","telphone":"18858041077","latitude":"0.0","longitude":"0.0","headImg":"https://xapi.smartparks.cn:9010/BLUECITY/SJYS_IMG/7852_20180712_233424698_4485.jpg","createDate":"2018-05-02 22:08","nickName":"哈哈哈哈","isLocking":"0","payPassword":"96e79218965eb72c92a549dd5a330112","tellphone":"18858041077","pttId":"55910193c0b8faa387ffca92d8489c93","userCode":"FMSAFBJCAPCKJSUN","lockingExpireTime":"2018-05-18 23:46:43","smallMoney":100,"ppId":"https://xapi.smartparks.cn:9010/BLUECITY/SJYS_IMG/6961_20180709_141440191_57.jpg","ooId":"https://xapi.smartparks.cn:9010/BLUECITY/SJYS_IMG/5756_20180709_141440217_4766.jpg","stampImg":"https://xapi.smartparks.cn:9010/BLUECITY/SJYS_IMG/7241_20180709_141440320_5243.jpg","handPassword":null,"checkName":"adminhu","checkTime":"2018-07-10 18:00:39","checkState":"1","companyId":"111310000220","idCard":"511623199304176996","birthday":null,"sex":"1","umoney":null,"integral":null,"email":null,"cfmPwd":null,"smallNoPayPassword":"1","isDeleted":"0","llId":null,"idType":"0","garden":"113110000020","password":"96e79218965eb72c92a549dd5a330112","name":"程敬强","state":"1","type":"0"},"headImg":"https://xapi.smartparks.cn:9010/BLUECITY/SJYS_IMG/https://xapi.smartparks.cn:9010/BLUECITY/SJYS_IMG/7852_20180712_233424698_4485.jpg","memberId":"110810002862","nickName":"哈哈哈哈","companyName":"浙江鸿源建设工程有限公司","statu":"3","sex":"1","hotRegion":"宁波智慧园","menus":[{"code":"cwyy","createTime":"2016-05-04 10:16:26","isDelete":"0","createName":"超级管理员","authName":"车位预约","authId":"114210000002","updateName":"超级管理员","updateTime":"2016-06-13 15:04:32","description":"车位预约","type":"1"},{"code":"yqjk","createTime":"2016-06-13 15:05:20","isDelete":"0","createName":"超级管理员","authName":"园区监控","authId":"114210000004","updateName":"超级管理员","updateTime":"2016-06-13 15:06:00","description":"园区监控","type":"1"}],"gardenId":"113110000020","token":{"userId":"110810002862","createTime":"2018-07-13 18:19:04","expirationTime":"2018-08-12 18:19:04","isDeleted":0,"updateTime":"2018-07-13 18:19:04","isOverdue":null,"isForbidden":null,"ctid":"f56064f277c843d287c9a2a9a4bd37b6","cashierId":null,"token":"f56064f277c843d287c9a2a9a4bd37b6"},"roles":[{"code":"memberUser","createTime":"2016-05-03 15:03:36","isDelete":"0","createName":"超级管理员","roleId":"114110000000","roleName":"注册用户","updateName":"超级管理员","updateTime":"2016-07-11 00:00:00","type":"1"},{"code":"gardenEnterprise","createTime":"2016-05-05 09:52:48","isDelete":"0","createName":"超级管理员","roleId":"114110000001","roleName":"认证用户","updateName":"超级管理员","updateTime":"2016-06-26 00:00:00","type":"0"}],"address":null,"name":"程敬强"}
         * rong : {"token":"NiAl3Qsyz58OS5uNKOZi97bKQVnufmng5d4U+AFUNjLsnoR6n/Qa4vY2tX0eVdV1wOzwt3ZO7N4ejaTtj+k1wBHgI83YOqj3","code":"200","userId":"110810002862"}
         */

        private MemberUserBean memberUser;
        private RongBean rong;

        public static class MemberUserBean {
            /**
             * user : {"userId":"110810002862","telphone":"18858041077","latitude":"0.0","longitude":"0.0","headImg":"https://xapi.smartparks.cn:9010/BLUECITY/SJYS_IMG/7852_20180712_233424698_4485.jpg","createDate":"2018-05-02 22:08","nickName":"哈哈哈哈","isLocking":"0","payPassword":"96e79218965eb72c92a549dd5a330112","tellphone":"18858041077","pttId":"55910193c0b8faa387ffca92d8489c93","userCode":"FMSAFBJCAPCKJSUN","lockingExpireTime":"2018-05-18 23:46:43","smallMoney":100,"ppId":"https://xapi.smartparks.cn:9010/BLUECITY/SJYS_IMG/6961_20180709_141440191_57.jpg","ooId":"https://xapi.smartparks.cn:9010/BLUECITY/SJYS_IMG/5756_20180709_141440217_4766.jpg","stampImg":"https://xapi.smartparks.cn:9010/BLUECITY/SJYS_IMG/7241_20180709_141440320_5243.jpg","handPassword":null,"checkName":"adminhu","checkTime":"2018-07-10 18:00:39","checkState":"1","companyId":"111310000220","idCard":"511623199304176996","birthday":null,"sex":"1","umoney":null,"integral":null,"email":null,"cfmPwd":null,"smallNoPayPassword":"1","isDeleted":"0","llId":null,"idType":"0","garden":"113110000020","password":"96e79218965eb72c92a549dd5a330112","name":"程敬强","state":"1","type":"0"}
             * headImg : https://xapi.smartparks.cn:9010/BLUECITY/SJYS_IMG/https://xapi.smartparks.cn:9010/BLUECITY/SJYS_IMG/7852_20180712_233424698_4485.jpg
             * memberId : 110810002862
             * nickName : 哈哈哈哈
             * companyName : 浙江鸿源建设工程有限公司
             * statu : 3
             * sex : 1
             * hotRegion : 宁波智慧园
             * menus : [{"code":"cwyy","createTime":"2016-05-04 10:16:26","isDelete":"0","createName":"超级管理员","authName":"车位预约","authId":"114210000002","updateName":"超级管理员","updateTime":"2016-06-13 15:04:32","description":"车位预约","type":"1"},{"code":"yqjk","createTime":"2016-06-13 15:05:20","isDelete":"0","createName":"超级管理员","authName":"园区监控","authId":"114210000004","updateName":"超级管理员","updateTime":"2016-06-13 15:06:00","description":"园区监控","type":"1"}]
             * gardenId : 113110000020
             * token : {"userId":"110810002862","createTime":"2018-07-13 18:19:04","expirationTime":"2018-08-12 18:19:04","isDeleted":0,"updateTime":"2018-07-13 18:19:04","isOverdue":null,"isForbidden":null,"ctid":"f56064f277c843d287c9a2a9a4bd37b6","cashierId":null,"token":"f56064f277c843d287c9a2a9a4bd37b6"}
             * roles : [{"code":"memberUser","createTime":"2016-05-03 15:03:36","isDelete":"0","createName":"超级管理员","roleId":"114110000000","roleName":"注册用户","updateName":"超级管理员","updateTime":"2016-07-11 00:00:00","type":"1"},{"code":"gardenEnterprise","createTime":"2016-05-05 09:52:48","isDelete":"0","createName":"超级管理员","roleId":"114110000001","roleName":"认证用户","updateName":"超级管理员","updateTime":"2016-06-26 00:00:00","type":"0"}]
             * address : null
             * name : 程敬强
             */

            private UserBean user;
            private String headImg;
            private String memberId;
            private String nickName;
            private String companyName;
            private String statu;
            private String sex;
            private String hotRegion;
            private String gardenId;
            private TokenBean token;
            private Object address;
            private String name;
            private List<MenusBean> menus;
            private List<RolesBean> roles;


            public static class UserBean {
                /**
                 * userId : 110810002862
                 * telphone : 18858041077
                 * latitude : 0.0
                 * longitude : 0.0
                 * headImg : https://xapi.smartparks.cn:9010/BLUECITY/SJYS_IMG/7852_20180712_233424698_4485.jpg
                 * createDate : 2018-05-02 22:08
                 * nickName : 哈哈哈哈
                 * isLocking : 0
                 * payPassword : 96e79218965eb72c92a549dd5a330112
                 * tellphone : 18858041077
                 * pttId : 55910193c0b8faa387ffca92d8489c93
                 * userCode : FMSAFBJCAPCKJSUN
                 * lockingExpireTime : 2018-05-18 23:46:43
                 * smallMoney : 100.0
                 * ppId : https://xapi.smartparks.cn:9010/BLUECITY/SJYS_IMG/6961_20180709_141440191_57.jpg
                 * ooId : https://xapi.smartparks.cn:9010/BLUECITY/SJYS_IMG/5756_20180709_141440217_4766.jpg
                 * stampImg : https://xapi.smartparks.cn:9010/BLUECITY/SJYS_IMG/7241_20180709_141440320_5243.jpg
                 * handPassword : null
                 * checkName : adminhu
                 * checkTime : 2018-07-10 18:00:39
                 * checkState : 1
                 * companyId : 111310000220
                 * idCard : 511623199304176996
                 * birthday : null
                 * sex : 1
                 * umoney : null
                 * integral : null
                 * email : null
                 * cfmPwd : null
                 * smallNoPayPassword : 1
                 * isDeleted : 0
                 * llId : null
                 * idType : 0
                 * garden : 113110000020
                 * password : 96e79218965eb72c92a549dd5a330112
                 * name : 程敬强
                 * state : 1
                 * type : 0
                 */

                private String userId;
                private String telphone;
                private String latitude;
                private String longitude;
                private String headImg;
                private String createDate;
                private String nickName;
                private String isLocking;
                private String payPassword;
                private String tellphone;
                private String pttId;
                private String userCode;
                private String lockingExpireTime;
                private double smallMoney;
                private String ppId;
                private String ooId;
                private String stampImg;
                private Object handPassword;
                private String checkName;
                private String checkTime;
                private String checkState;
                private String companyId;
                private String idCard;
                private Object birthday;
                private String sex;
                private Object umoney;
                private Object integral;
                private Object email;
                private Object cfmPwd;
                private String smallNoPayPassword;
                private String isDeleted;
                private Object llId;
                private String idType;
                private String garden;
                private String password;
                private String name;
                private String state;
                private String type;

            }

            public static class TokenBean {
                /**
                 * userId : 110810002862
                 * createTime : 2018-07-13 18:19:04
                 * expirationTime : 2018-08-12 18:19:04
                 * isDeleted : 0
                 * updateTime : 2018-07-13 18:19:04
                 * isOverdue : null
                 * isForbidden : null
                 * ctid : f56064f277c843d287c9a2a9a4bd37b6
                 * cashierId : null
                 * token : f56064f277c843d287c9a2a9a4bd37b6
                 */

                private String userId;
                private String createTime;
                private String expirationTime;
                private int isDeleted;
                private String updateTime;
                private Object isOverdue;
                private Object isForbidden;
                private String ctid;
                private Object cashierId;
                private String token;

            }

            public static class MenusBean {
                /**
                 * code : cwyy
                 * createTime : 2016-05-04 10:16:26
                 * isDelete : 0
                 * createName : 超级管理员
                 * authName : 车位预约
                 * authId : 114210000002
                 * updateName : 超级管理员
                 * updateTime : 2016-06-13 15:04:32
                 * description : 车位预约
                 * type : 1
                 */

                private String code;
                private String createTime;
                private String isDelete;
                private String createName;
                private String authName;
                private String authId;
                private String updateName;
                private String updateTime;
                private String description;
                private String type;

            }

            public static class RolesBean {
                /**
                 * code : memberUser
                 * createTime : 2016-05-03 15:03:36
                 * isDelete : 0
                 * createName : 超级管理员
                 * roleId : 114110000000
                 * roleName : 注册用户
                 * updateName : 超级管理员
                 * updateTime : 2016-07-11 00:00:00
                 * type : 1
                 */

                private String code;
                private String createTime;
                private String isDelete;
                private String createName;
                private String roleId;
                private String roleName;
                private String updateName;
                private String updateTime;
                private String type;

            }
        }

        public static class RongBean {
            /**
             * token : NiAl3Qsyz58OS5uNKOZi97bKQVnufmng5d4U+AFUNjLsnoR6n/Qa4vY2tX0eVdV1wOzwt3ZO7N4ejaTtj+k1wBHgI83YOqj3
             * code : 200
             * userId : 110810002862
             */

            private String token;
            private String code;
            private String userId;

        }
    }
}
