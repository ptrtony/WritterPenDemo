package com.android.bluetown.result;

import java.util.List;

/**
 * Created by Dafen on 2018/7/2.
 */

public class ParkingLotsData {
    /**
     * rep_code : 000000
     * rep_msg : 操作成功
     * body : {"parkinglots":[{"id":"4f64fd7e-1581-4ec8-811f-ddba87e16bcd","parkinglot_no":"113810000004","parkinglot_name":"A区地下停车场","supplied_count":200,"enabled":1,"available_count":200},{"id":"7d2d16ad-5c77-476c-8c4a-0a84a70ab8bc","parkinglot_no":"113810000000","parkinglot_name":"A区地面停车场","supplied_count":100,"enabled":1,"available_count":95}],"qualification":{"qua_code":"3","qua_msg":"有未支付预约费的预约记录"}}
     */

    public String rep_code;
    public String rep_msg;
    public BodyBean body;

    public static class BodyBean {
        /**
         * parkinglots : [{"id":"4f64fd7e-1581-4ec8-811f-ddba87e16bcd","parkinglot_no":"113810000004","parkinglot_name":"A区地下停车场","supplied_count":200,"enabled":1,"available_count":200},{"id":"7d2d16ad-5c77-476c-8c4a-0a84a70ab8bc","parkinglot_no":"113810000000","parkinglot_name":"A区地面停车场","supplied_count":100,"enabled":1,"available_count":95}]
         * qualification : {"qua_code":"3","qua_msg":"有未支付预约费的预约记录"}
         */

        public QualificationBean qualification;
        public List<ParkinglotsBean> parkinglots;

        public static class QualificationBean {
            /**
             * qua_code : 3
             * qua_msg : 有未支付预约费的预约记录
             */

            public String qua_code;
            public String qua_msg;
        }

        public static class ParkinglotsBean {
            /**
             * id : 4f64fd7e-1581-4ec8-811f-ddba87e16bcd
             * parkinglot_no : 113810000004
             * parkinglot_name : A区地下停车场
             * supplied_count : 200
             * enabled : 1
             * available_count : 200
             */
            public String id;
            public String parkinglot_no;
            public String parkinglot_name;
            public int supplied_count;
            public int enabled;
            public int available_count;
        }
    }

    /**
     * rep_code : 0000
     * rep_msg : 操作成功
     * result : [{"id":"4f64fd7e-1581-4ec8-811f-ddba87e16bcd","parkinglot_no":"113810000004","parkinglot_name":"A区地下停车场","supplied_count":200,"enabled":1,"available_count":200},{"id":"7d2d16ad-5c77-476c-8c4a-0a84a70ab8bc","parkinglot_no":"113810000000","parkinglot_name":"A区地面停车场","supplied_count":100,"enabled":1,"available_count":100}]
     */


}
