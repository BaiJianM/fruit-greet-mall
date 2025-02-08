package com.liyuyouguo.common.beans.dto.shop;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author baijianmin
 */
@Data
public class NoticeAddDto {

    private String content;

    private LocalDateTime time;

}
