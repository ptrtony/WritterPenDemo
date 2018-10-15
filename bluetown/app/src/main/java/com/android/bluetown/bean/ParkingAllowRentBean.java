package com.android.bluetown.bean;

/**
 * Created by Dafen on 2018/7/6.
 */

public class ParkingAllowRentBean {

    /**
     * rep_code : 000000
     * rep_msg : 操作成功
     * body : {"allow":false,"tips":"功能尚未开放"}
     */

    public String rep_code;
    public String rep_msg;
    public BodyBean body;



    public static class BodyBean {
        /**
         * allow : false
         * tips : 功能尚未开放
         */

        public boolean allow;
        public String tips;

    }
}
