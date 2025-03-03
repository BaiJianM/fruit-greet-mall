package com.liyuyouguo.common.service.log;

import com.liyuyouguo.common.config.operatelog.pojo.OperateLogDto;
import com.liyuyouguo.common.entity.SysOperateLog;
import com.liyuyouguo.common.mapper.SysOperateLogMapper;
import com.liyuyouguo.common.utils.ConvertUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 操作日志存储服务
 *
 * @author baijianmin
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OperateLogService {

    private final SysOperateLogMapper logMapper;

    /**
     * 日志存储
     *
     * @param operateLogDto 操作日志对象
     */
    public void createLog(OperateLogDto operateLogDto) {
        ConvertUtils.convert(operateLogDto, SysOperateLog::new).ifPresent(logMapper::insert);
    }
}
