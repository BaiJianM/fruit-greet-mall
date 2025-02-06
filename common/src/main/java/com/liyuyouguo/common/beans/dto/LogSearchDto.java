package com.liyuyouguo.common.beans.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.liyuyouguo.common.beans.FruitGreetPage;
import com.liyuyouguo.common.beans.enums.LogTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 系统操作日志搜索传参
 *
 * @author baijianmin
 */
@EqualsAndHashCode(callSuper = false)
@Data
@Schema(description = "系统操作日志搜索传参")
public class LogSearchDto extends FruitGreetPage {

    @Schema(description = "日志类型", hidden = true)
    private LogTypeEnum logType;

    @Schema(description = "勾选的操作记录id列表")
    private List<Long> logIds;

    @Schema(description = "事件")
    private String event;

    @Schema(description = "操作人")
    private String createUserCode;

    @Schema(description = "起始操作时间", example = "2023-01-01 12:00:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startCreateTime;

    @Schema(description = "截止操作时间", example = "2023-01-01 23:59:59")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endCreateTime;

}
