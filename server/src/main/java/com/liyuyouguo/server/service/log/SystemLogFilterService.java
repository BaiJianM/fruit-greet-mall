package com.liyuyouguo.server.service.log;

import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.filter.Filter;
import ch.qos.logback.core.spi.FilterReply;
import lombok.extern.slf4j.Slf4j;

/**
 * 系统日志(logback)过滤生成至数据库服务类
 *
 * @author baijianmin
 */
@Slf4j
public class SystemLogFilterService extends Filter<LoggingEvent> {

    @Override
    public FilterReply decide(LoggingEvent event) {
        String loggerName = event.getLoggerName();
        // 只记录业务的日志
        if (loggerName.startsWith("com.answer.com.liyuyouguo.server.service")) {
            // 如果要记日志到指定库或文件可在此写入
            // 注：因为logback的生命周期执行在SpringBean之前，所以不能直接注入Bean，可以通过getBean等方法注入
            return FilterReply.ACCEPT;
        } else {
            // 非业务的日志不会入库
            return FilterReply.DENY;
        }
    }
}
