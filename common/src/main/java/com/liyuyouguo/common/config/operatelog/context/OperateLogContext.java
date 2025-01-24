package com.liyuyouguo.common.config.operatelog.context;

import com.liyuyouguo.common.config.operatelog.pojo.DiffVo;
import org.springframework.core.NamedThreadLocal;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.util.ArrayList;
import java.util.List;

/**
 * 日志记录上下文
 *
 * @author baijianmin
 */
public class OperateLogContext {

    /**
     * 本地上下文线程
     */
    private static final ThreadLocal<StandardEvaluationContext> CONTEXT_THREAD_LOCAL =
            new NamedThreadLocal<>("ThreadLocal StandardEvaluationContext");

    /**
     * 发生变化的列表数据集合 本地线程
     */
    private static final ThreadLocal<List<DiffVo>> DIFF_DTO_LIST_THREAD_LOCAL =
            new NamedThreadLocal<>("ThreadLocal DiffVOList");

    /**
     * 上下文获取方法返回的key
     */
    public static final String CONTEXT_KEY_NAME_RETURN = "_return";

    /**
     * 上下文获取错误信息的key
     */
    public static final String CONTEXT_KEY_NAME_ERROR_MSG = "_errorMsg";

    private OperateLogContext() {}

    /**
     * 获取Spel操作日志上下文
     *
     * @return StandardEvaluationContext Spel操作日志上下文
     */
    public static StandardEvaluationContext getContext() {
        return CONTEXT_THREAD_LOCAL.get() == null ? new StandardEvaluationContext() : CONTEXT_THREAD_LOCAL.get();
    }

    /**
     * 设置操作日志上下文变量信息
     *
     * @param key   信息唯一标识
     * @param value 信息值
     */
    public static void putVariables(String key, Object value) {
        StandardEvaluationContext context = getContext();
        context.setVariable(key, value);
        CONTEXT_THREAD_LOCAL.set(context);
    }

    /**
     * 清除操作日志上下文信息
     */
    public static void clearContext() {
        CONTEXT_THREAD_LOCAL.remove();
    }

    /**
     * 获取发生变化的数据信息集合
     *
     * @return List<DiffVo> 操作日志变化信息集合
     */
    public static List<DiffVo> getDiffVOList() {
        return DIFF_DTO_LIST_THREAD_LOCAL.get() == null ? new ArrayList<>() : DIFF_DTO_LIST_THREAD_LOCAL.get();
    }

    /**
     * 添加发生变化的数据
     *
     * @param diffVO 操作日志发生变化的数据
     */
    public static void addDiffVO(DiffVo diffVO) {
        if (diffVO != null) {
            List<DiffVo> diffVoList = getDiffVOList();
            diffVoList.add(diffVO);
            DIFF_DTO_LIST_THREAD_LOCAL.set(diffVoList);
        }
    }

    /**
     * 清除操作日志发生变化的数据集合
     */
    public static void clearDiffVOList() {
        DIFF_DTO_LIST_THREAD_LOCAL.remove();
    }


}
