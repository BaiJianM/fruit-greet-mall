package com.liyuyouguo.common.beans.vo.express;

import lombok.Data;

import java.util.List;

/**
 * @author baijianmin
 */
@Data
public class AliExpressResponseVo {

    private String status;

    private String msg;

    private Result result;

    @Data
    public static class Result {

        private String number;

        private String type;

        private String deliverystatus;

        private String issign;

        private String expName;

        private String expSite;

        private String expPhone;

        private String courier;

        private String courierPhone;

        private String updateTime;

        private String takeTime;

        private String logo;

        private List<Item> list;

        @Data
        public static class Item {

            private String time;

            private String status;

        }

    }

}
