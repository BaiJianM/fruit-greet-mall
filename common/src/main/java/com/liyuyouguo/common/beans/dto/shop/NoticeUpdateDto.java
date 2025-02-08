package com.liyuyouguo.common.beans.dto.shop;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author baijianmin
 */
@Data
public class NoticeUpdateDto {

    private Integer id;

    private String content;

    private LocalDateTime time;

}
