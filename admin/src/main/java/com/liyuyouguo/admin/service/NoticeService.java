package com.liyuyouguo.admin.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.liyuyouguo.common.beans.dto.shop.NoticeAddDto;
import com.liyuyouguo.common.beans.dto.shop.NoticeDeleteDto;
import com.liyuyouguo.common.beans.dto.shop.NoticeUpdateContentDto;
import com.liyuyouguo.common.beans.dto.shop.NoticeUpdateDto;
import com.liyuyouguo.common.entity.shop.Notice;
import com.liyuyouguo.common.mapper.NoticeMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author baijianmin
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class NoticeService {

    private final NoticeMapper noticeMapper;

    public List<Notice> getNoticeList() {
        return noticeMapper.selectList(null);
    }

    public Integer updateNoticeContent(NoticeUpdateContentDto dto) {
        return noticeMapper.update(Wrappers.lambdaUpdate(Notice.class)
                .set(Notice::getContent, dto.getContent())
                .eq(Notice::getId, dto.getId()));
    }

    public Integer addNotice(NoticeAddDto dto) {
        Notice notice = new Notice();
        notice.setContent(dto.getContent());
        notice.setEndTime(dto.getTime());
        noticeMapper.insert(notice);
        return notice.getId();
    }

    public Integer updateNotice(NoticeUpdateDto dto) {
        Notice notice = new Notice();
        notice.setContent(dto.getContent());
        notice.setEndTime(dto.getTime());
        notice.setIsDelete(dto.getTime().isAfter(LocalDateTime.now()) ? 0 : 1);
        return noticeMapper.update(notice, Wrappers.lambdaUpdate(Notice.class)
                .eq(Notice::getId, dto.getId()));
    }

    public void destroyNotice(NoticeDeleteDto dto) {
        noticeMapper.delete(Wrappers.lambdaQuery(Notice.class)
                .eq(Notice::getId, dto.getId()));
    }

}
