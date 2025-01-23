package com.liyuyouguo.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.liyuyouguo.server.beans.dto.LogSearchDto;
import com.liyuyouguo.server.beans.vo.log.LogSearchResultVo;
import com.liyuyouguo.server.entity.SysOperateLog;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * 操作日志数据库操作Mapper接口
 *
 * @author baijianmin
 */
@Repository
public interface SysOperateLogMapper extends BaseMapper<SysOperateLog> {

    /**
     * 系统日志搜索
     *
     * @param page 分页参数
     * @param dto  查询条件
     * @return IPage<LogSearchResultVO> 日志分页列表
     */
    IPage<LogSearchResultVo> search(IPage<LogSearchResultVo> page, @Param("dto") LogSearchDto dto);

}




