package com.android.bluetown.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Dafen on 2018/6/6.
 * 首页商家和活动的实体类
 */

public class BusinessAndEventBean {
    /**
     * LatestMessage : {"Id":"112910058159","Content":"成功支付一笔费用，请点击查看","GenerateTime":"9天前"}
     * Notice : {"Id":"109710000148","Title":"卡菲伦皮肤管理中心盛大开业","CreateDate":"2018-05-23"}
     * HotMerchants : [{"Id":"110710000160","ImageUrl":"https://xapi.smartparks.cn:9010/BLUECITY/SJYS_IMG/2017-08-14/index_02.jpg.jpg","Name":"一點點（江南一品店）"},{"Id":"110710000161","ImageUrl":"https://xapi.smartparks.cn:9010/BLUECITY/SJYS_IMG/2017-08-15/20170815085423.jpg.jpg","Name":"日月光(港隆店）"},{"Id":"110710000162","ImageUrl":"https://xapi.smartparks.cn:9010/BLUECITY/SJYS_IMG/2017-08-16/timg(1).jpg.jpg","Name":"星巴克（研发园店）"}]
     * HotActivities : [{"Id":"110410000127","ImageUrl":"https://xapi.smartparks.cn:9010/BLUECITY/SJYS_IMG/542926452013033118134008_6401487657477258.jpg","Name":"踏春","JoinNumber":"已有7参与","Status":3},{"Id":"110410000128","ImageUrl":"https://xapi.smartparks.cn:9010/BLUECITY/SJYS_IMG/111487752059897.gif","Name":"yige","JoinNumber":"已有5参与","Status":3},{"Id":"110410000129","ImageUrl":"https://xapi.smartparks.cn:9010/BLUECITY/SJYS_IMG/201703231312481490247672220.png","Name":"食料胃及·一元吃大餐","JoinNumber":"已有0参与","Status":3},{"Id":"110410000130","ImageUrl":"https://xapi.smartparks.cn:9010/BLUECITY/SJYS_IMG/201703231312481490255755992.png","Name":"食料胃及·一元吃大餐","JoinNumber":"已有10参与","Status":3},{"Id":"110410000131","ImageUrl":"https://xapi.smartparks.cn:9010/BLUECITY/SJYS_IMG/201703231312481490258206972.png","Name":"食料胃及·一元吃大餐","JoinNumber":"已有20参与","Status":3}]
     * Appointments : [{"Id":"111410000447","License":"浙B02B88","CreateTime":"02-21 08:37","EnterTime":"08:56","LeaveTime":"08:44","Telphone":"13957878221-","Status":2,"StatusName":"已离场"},{"Id":"111410000447","License":"浙B02B88","CreateTime":"02-21 08:37","EnterTime":"08:56","LeaveTime":"08:44","Telphone":"13957878221-","Status":2,"StatusName":"已离场"}]
     */
    public String rep_code;
    public String rep_msg;
    public LatestMessageBean LatestMessage;
    public NoticeBean Notice;
    public List<HotMerchantsBean> HotMerchants;
    public List<HotActivitiesBean> HotActivities;
    public List<AppointmentsBean> Appointments;

    public static class LatestMessageBean {
        /**
         * Id : 112910058159
         * Content : 成功支付一笔费用，请点击查看
         * GenerateTime : 9天前
         */

        public String Id;
        public String Content;
        public String GenerateTime;
    }

    public static class NoticeBean {
        /**
         * Id : 109710000148
         * Title : 卡菲伦皮肤管理中心盛大开业
         * CreateDate : 2018-05-23
         */

        public String Id;
        public String Title;
        public String CreateDate;
        public final int type =1;

    }

    public static class HotMerchantsBean implements Serializable{
        /**
         * Id : 110710000160
         * ImageUrl : https://xapi.smartparks.cn:9010/BLUECITY/SJYS_IMG/2017-08-14/index_02.jpg.jpg
         * Name : 一點點（江南一品店）
         */

        public String Id;
        public String ImageUrl;
        public String Name;
        public final int type =1;
    }

    public static class HotActivitiesBean implements Serializable{
        /**
         * Id : 110410000127
         * ImageUrl : https://xapi.smartparks.cn:9010/BLUECITY/SJYS_IMG/542926452013033118134008_6401487657477258.jpg
         * Name : 踏春
         * JoinNumber : 已有7参与
         * Status : 3
         */

        public String Id;
        public String ImageUrl;
        public String Name;
        public String JoinNumber;
        public int Status;
        public final int type =1;
    }

    public static class AppointmentsBean {
        /**
         * Id : 111410000447
         * License : 浙B02B88
         * CreateTime : 02-21 08:37
         * EnterTime : 08:56
         * LeaveTime : 08:44
         * Telphone : 13957878221-
         * Status : 2
         * StatusName : 已离场
         */

        public String Id;
        public String License;
        public String CreateTime;
        public String EnterTime;
        public String LeaveTime;
        public String Telphone;
        public int Status;
        public String StatusName;
        public String PlanTime;
        public final int type =2;
    }

}
