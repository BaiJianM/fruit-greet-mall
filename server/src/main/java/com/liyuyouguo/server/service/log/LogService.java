package com.liyuyouguo.server.service.log;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liyuyouguo.server.beans.PageResult;
import com.liyuyouguo.server.beans.dto.LogSearchDto;
import com.liyuyouguo.server.beans.enums.LogTypeEnum;
import com.liyuyouguo.server.beans.enums.LoginTypeEnum;
import com.liyuyouguo.server.beans.vo.log.LogSearchResultVo;
import com.liyuyouguo.server.beans.vo.log.LoginLogExportResultVo;
import com.liyuyouguo.server.beans.vo.log.OperateLogExportResultVo;
import com.liyuyouguo.server.entity.SysOperateLog;
import com.liyuyouguo.server.mapper.SysOperateLogMapper;
import com.liyuyouguo.server.utils.ConvertUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 日志服务类
 *
 * @author baijianmin
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class LogService extends ServiceImpl<SysOperateLogMapper, SysOperateLog> implements IService<SysOperateLog> {

    private final SysOperateLogMapper operateLogMapper;

    /**
     * 搜索日志
     *
     * @param dto     查询条件
     * @param logType 日志类型
     * @return PageResult<LogSearchResultVo> 日志分页列表
     */
    public PageResult<LogSearchResultVo> search(LogSearchDto dto, LogTypeEnum logType) {
        dto.setLogType(logType);
        IPage<LogSearchResultVo> search = operateLogMapper.search(new Page<>(dto.getCurrent(), dto.getSize()), dto);
        return ConvertUtils.convert(search, PageResult<LogSearchResultVo>::new).orElse(new PageResult<>());
    }

    /**
     * 删除日志
     *
     * @param logIds 日志id列表
     */
    public void delete(List<Long> logIds) {
        operateLogMapper.deleteBatchIds(logIds);
    }

    /**
     * 导出日志
     *
     * @param dto 查询条件
     */
    public void exportExcel(LogSearchDto dto, LogTypeEnum logType) {
        PageResult<LogSearchResultVo> page = this.search(dto, logType);
        // 序号
        AtomicInteger serialNo = new AtomicInteger(1);
        // 登陆日志导出
        if (LogTypeEnum.LOGIN_LOG == dto.getLogType()) {
            Collection<LoginLogExportResultVo> records = ConvertUtils.convertCollection(page.getRecords(), LoginLogExportResultVo::new, (s, t) -> {
                t.setEvent(LoginTypeEnum.getName(s.getEvent()));
                t.setSerialNo(serialNo.getAndIncrement());
            }).orElse(new ArrayList<>());
        }
        // 操作日志导出
        else if (LogTypeEnum.OPERATE_LOG == dto.getLogType()) {
            Collection<OperateLogExportResultVo> records = ConvertUtils.convertCollection(page.getRecords(), OperateLogExportResultVo::new, (s, t) -> {
                t.setEvent(LoginTypeEnum.getName(s.getEvent()));
                t.setSerialNo(serialNo.getAndIncrement());
            }).orElse(new ArrayList<>());
        }
        // TODO 待写导出
    }

    /**
     * 清空日志
     *
     * @param logType 日志类型
     */
    public void clean(LogTypeEnum logType) {
        operateLogMapper.delete(new LambdaQueryWrapper<SysOperateLog>().eq(SysOperateLog::getType, logType.getCode()));
    }

}
