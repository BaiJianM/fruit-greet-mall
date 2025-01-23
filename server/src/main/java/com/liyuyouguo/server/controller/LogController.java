package com.liyuyouguo.server.controller;

import com.liyuyouguo.server.annotations.FruitShopController;
import com.liyuyouguo.server.beans.PageResult;
import com.liyuyouguo.server.beans.dto.LogSearchDto;
import com.liyuyouguo.server.beans.enums.LogTypeEnum;
import com.liyuyouguo.server.beans.vo.log.LogSearchResultVo;
import com.liyuyouguo.server.commons.FruitShopResponse;
import com.liyuyouguo.server.service.log.LogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 日志信息控制层
 *
 * @author baijianmin
 */
@FruitShopController("/log/{logType}")
@RequiredArgsConstructor
@Tag(name = "日志信息相关接口")
@Validated
@Slf4j
public class LogController {

    private final LogService logService;

    @Operation(summary = "日志搜索")
    @PostMapping("/search")
    public FruitShopResponse<PageResult<LogSearchResultVo>> search(@PathVariable("logType") LogTypeEnum logType,
                                                                   @Valid @RequestBody LogSearchDto dto) {
        return FruitShopResponse.success(logService.search(dto, logType));
    }

    @Operation(summary = "日志删除")
    @Parameter(name = "logIds", description = "日志id列表", required = true)
    @DeleteMapping
    public FruitShopResponse<Void> delete(
            @NotEmpty(message = "日志id列表，logIds不能为null且数组列表长度必须大于0")
            @RequestParam("logIds") List<Long> logIds) {
        logService.delete(logIds);
        return FruitShopResponse.success();
    }

    @Operation(summary = "日志导出")
    @PostMapping("/export")
    public void export(@PathVariable("logType") LogTypeEnum logType, @Valid @RequestBody LogSearchDto dto) {
        logService.exportExcel(dto, logType);
    }

    @Operation(summary = "清空日志")
    @DeleteMapping("/clean")
    public FruitShopResponse<Void> clean(@PathVariable("logType") LogTypeEnum logType) {
        logService.clean(logType);
        return FruitShopResponse.success();
    }

}
