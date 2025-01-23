package com.liyuyouguo.server.config.operatelog.properties;

import com.liyuyouguo.server.config.operatelog.function.OperateLogObjectDiff;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;


/**
 * 操作日志记录配置
 *
 * @author baijianmin
 */
@Data
@ConfigurationProperties(prefix = "answer.log.operate")
public class OperateLogProperties {

    /**
     * 对象属性变更时的描述
     */
    private String diffMsgFormat = OperateLogObjectDiff.DEFAULT_DIFF_MSG_FORMAT;

    /**
     * 新增对象属性时的描述
     */
    private String addMsgFormat = OperateLogObjectDiff.DEFAULT_ADD_MSG_FORMAT;

    /**
     * 对象属性键值对映射描述，第一个属性为键，第二个属性为值
     */
    private String msgFormat = OperateLogObjectDiff.DEFAULT_MSG_FORMAT;

    /**
     * 多个对象属性键值对转字符串的分隔符
     */
    private String msgSeparator = OperateLogObjectDiff.DEFAULT_MSG_SEPARATOR;

    /**
     * 在方法中主动抛出异常时的操作日志生成策略（默认不继续生成任何操作日志，注：未知异常（非AnswerException）的情况下，不进行日志生成）
     */
    private boolean onError = false;

}
