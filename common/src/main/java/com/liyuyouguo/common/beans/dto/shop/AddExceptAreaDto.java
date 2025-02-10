package com.liyuyouguo.common.beans.dto.shop;

import lombok.Data;

import java.util.List;

/**
 * @author baijianmin
 */
@Data
public class AddExceptAreaDto {

    private List<ExceptAreaTableDto> table;

    private ExceptAreaInfoDto info;

    @Data
    public static class ExceptAreaTableDto {
        private String area;
    }

    @Data
    public static class ExceptAreaInfoDto {
        private String content;
    }

}
