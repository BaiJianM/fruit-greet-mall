package com.liyuyouguo.server.controller;

import com.liyuyouguo.server.annotations.FruitShopController;
import com.liyuyouguo.server.commons.FruitShopResponse;
import com.liyuyouguo.server.config.FruitShopProperties;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件上传控制层
 *
 * @author baijianmin
 */
@Slf4j
@Tag(name = "文件上传相关接口")
@FruitShopController("/upload")
@RequiredArgsConstructor
public class UploadController {

    private final FruitShopProperties properties;

    @Operation(summary = "上传文件")
    @Parameter(name = "file", description = "文件", required = true)
    @ApiResponse(description = "文件上传成功后的文件路径", responseCode = "200")
    @PostMapping
    public FruitShopResponse<String> upload(@RequestParam("file") MultipartFile file) {
        return FruitShopResponse.success("");
    }

}
