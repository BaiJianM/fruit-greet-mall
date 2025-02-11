package com.liyuyouguo.admin.controller;

import com.liyuyouguo.admin.service.NoticeService;
import com.liyuyouguo.common.annotations.FruitGreetController;
import com.liyuyouguo.common.beans.dto.shop.NoticeAddDto;
import com.liyuyouguo.common.beans.dto.shop.NoticeUpdateContentDto;
import com.liyuyouguo.common.beans.dto.shop.NoticeUpdateDto;
import com.liyuyouguo.common.commons.FruitGreetResponse;
import com.liyuyouguo.common.entity.shop.Notice;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author baijianmin
 */
@Slf4j
@FruitGreetController("/notice")
@RequiredArgsConstructor
@Validated
public class NoticeController {

    private final NoticeService noticeService;

    @GetMapping
    public FruitGreetResponse<List<Notice>> index() {
        return FruitGreetResponse.success(noticeService.getNoticeList());
    }

    @PostMapping("/updateContent")
    public FruitGreetResponse<Integer> updateContent(@RequestBody NoticeUpdateContentDto dto) {
        return FruitGreetResponse.success(noticeService.updateNoticeContent(dto));
    }

    @PostMapping("/add")
    public FruitGreetResponse<Integer> addNotice(@RequestBody NoticeAddDto dto) {
        return FruitGreetResponse.success(noticeService.addNotice(dto));
    }

    @PostMapping("/update")
    public FruitGreetResponse<Integer> updateNotice(@RequestBody NoticeUpdateDto dto) {
        return FruitGreetResponse.success(noticeService.updateNotice(dto));
    }

    @PostMapping("/destroy")
    public FruitGreetResponse<Void> destroyNotice(@RequestParam Integer id) {
        noticeService.destroyNotice(id);
        return FruitGreetResponse.success();
    }

}
