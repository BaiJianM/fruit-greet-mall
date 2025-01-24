package com.liyuyouguo.common.config.operatelog.aop;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONWriter;
import com.liyuyouguo.common.annotations.operatelog.OperationLog;
import com.liyuyouguo.common.beans.UserInfo;
import com.liyuyouguo.common.commons.FruitShopException;
import com.liyuyouguo.common.config.operatelog.context.OperateLogContext;
import com.liyuyouguo.common.config.operatelog.function.OperateLogFunctionRegistrar;
import com.liyuyouguo.common.config.operatelog.pojo.OperateLogDto;
import com.liyuyouguo.common.config.operatelog.properties.OperateLogProperties;
import com.liyuyouguo.common.service.log.OperateLogService;
import com.liyuyouguo.common.utils.FruitShopThreadPool;
import com.liyuyouguo.common.utils.FruitShopUtils;
import com.liyuyouguo.common.utils.IpUtils;
import com.liyuyouguo.common.utils.UserInfoUtils;
import eu.bitwalker.useragentutils.UserAgent;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.Executor;
import java.util.function.Consumer;


/**
 * 操作日志切面
 *
 * @author baijianmin
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class SystemOperateLogAspect {

    /**
     * 操作日志服务
     */
    private final OperateLogService operateLogService;

    /**
     * 操作日志记录配置
     */
    private final OperateLogProperties properties;

    /**
     * Spel解析器
     */
    private final SpelExpressionParser parser = new SpelExpressionParser();

    /**
     * 方法参数解析
     */
    private final DefaultParameterNameDiscoverer discoverer = new DefaultParameterNameDiscoverer();

    /**
     * 操作日志环绕通知
     *
     * @param pjp 切点信息
     * @return Object 原操作执行结果
     * @throws Throwable 异常对象
     */
    @Around("@annotation(com.liyuyouguo.common.annotations.operatelog.OperationLog) " +
            "|| @annotation(com.liyuyouguo.common.annotations.operatelog.OperationLogs)")
    public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
        // 客户端信息
        HttpServletRequest request =
                ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getRequest();
        UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
        // 定义返回结果
        Object result;
        // 定义@OperationLog注解数组
        OperationLog[] annotations;
        // 定义LogDto集合，用于存放注解解析后的LogDto对象
        List<OperateLogDto> operateLogDtoList = new ArrayList<>();
        // 定义有序的Map，用于存放注解解析后的LogDto对象
        Map<OperationLog, OperateLogDto> logDtoMap = new LinkedHashMap<>();
        // 耗时监听对象
        StopWatch stopWatch = null;
        // 执行耗时
        long executionTime = 0L;
        // 注解解析：若解析失败直接不执行日志切面逻辑
        try {
            // 获取切面方法
            Method method = FruitShopUtils.getMethod(pjp);
            // 获取方法上的@OperationLog或@OperationLogs注解信息
            annotations = method.getDeclaredAnnotationsByType(OperationLog.class);
        } catch (Exception e) {
            // 若发生异常，则直接返回切面方法执行结果
            return pjp.proceed();
        }
        // 定义配置了executeBeforeFunc == false 的执行方法后解析切面逻辑的注解集合
        List<OperationLog> afterExec = new ArrayList<>();
        // 下方是日志切面逻辑
        try {
            // 遍历操作日志注解，解析方法执行前后逻辑
            this.loadLogAnnotations(annotations, pjp, logDtoMap, afterExec);
            // 初始化耗时监听对象
            stopWatch = new StopWatch();
            // 开启计时
            stopWatch.start();
            // 原方法执行
            result = pjp.proceed();
            // 方法成功执行后日志切面
            executionTime = this.afterMethodInvoke(afterExec, stopWatch, result, pjp, logDtoMap);
            // 写入成功执行后日志
            operateLogDtoList = new ArrayList<>(logDtoMap.values());
            // 遍历logDtoMap
            logDtoMap.forEach((annotation, operateLogDto) -> {
                if (operateLogDto.getSuccess() == null) {
                    operateLogDto.setSuccess(true);
                }
                // 若需要记录返回值，则logDto.setReturnStr切面方法返回值的JSONString
                if (annotation.recordReturnValue()) {
                    operateLogDto.setReturnStr(JSON.toJSONString(result));
                }
            });
        }
        // 原方法执行异常
        catch (Exception e) {
            // 方法异常执行后日志切面
            this.afterMethodThrow(e, afterExec, pjp, logDtoMap);
            // 记录日志切面执行耗时
            if (stopWatch != null) {
                // 结束计时
                stopWatch.stop();
                // 获取执行耗时
                executionTime = stopWatch.getTotalTimeMillis();
            }
            // 抛出原方法异常
            throw e;
        } finally {
            // 操作日志切面最终执行逻辑
            this.finalExec(executionTime, userAgent, request, operateLogDtoList);
        }
        return result;
    }

    /**
     * 遍历操作日志注解，解析方法执行前后逻辑
     *
     * @param annotations 操作日志注解集合
     * @param pjp         目标切点
     * @param logDtoMap   操作日志注解对应的Spel解析结果映射
     * @param afterExec   目标切点方法执行后置逻辑列表
     */
    private void loadLogAnnotations(OperationLog[] annotations, ProceedingJoinPoint pjp,
                                    Map<OperationLog, OperateLogDto> logDtoMap, List<OperationLog> afterExec) {
        // 遍历注解集合
        for (OperationLog annotation : annotations) {
            // 执行方法前解析切面逻辑
            if (annotation.executeBeforeFunc()) {
                // 解析注解中定义的Spel表达式，返回LogDto对象
                OperateLogDto dto = resolveExpress(annotation, pjp);
                if (dto != null) {
                    // 若解析后的logDto数据不为空，则将logDto放入Map中
                    logDtoMap.put(annotation, dto);
                }
            } else {
                afterExec.add(annotation);
            }
        }
    }

    /**
     * 方法成功执行后日志切面逻辑
     *
     * @param afterExec 切点方法执行后逻辑集合
     * @param stopWatch 耗时监听对象
     * @param result    切点方法执行结果
     * @param pjp       目标切点
     * @param logDtoMap 操作日志注解对应的Spel解析结果映射
     * @return long 方法执行耗时
     */
    private long afterMethodInvoke(List<OperationLog> afterExec, StopWatch stopWatch,
                                   Object result, ProceedingJoinPoint pjp, Map<OperationLog, OperateLogDto> logDtoMap) {
        long executionTime = 0L;
        // 如果执行了前置解析切面逻辑的注解后，不存在后置解析的注解了就返回原方法执行结果
        if (afterExec.isEmpty()) {
            // 否则继续执行后置解析切面注解
            stopWatch.stop();
            executionTime = stopWatch.getTotalTimeMillis();
        } else {
            // 在LogContext中写入执行后信息，将切面方法执行的返回结果写入日志记录上下文中
            OperateLogContext.putVariables(OperateLogContext.CONTEXT_KEY_NAME_RETURN, result);
            // 遍历执行方法后解析切面逻辑的注解集合
            afterExec.forEach(annotation -> {
                // 解析注解中定义的Spel表达式，返回LogDto对象
                OperateLogDto operateLogDto = resolveExpress(annotation, pjp);
                if (operateLogDto != null) {
                    // 若解析后的logDto数据不为空，则将logDto放入Map中
                    logDtoMap.put(annotation, operateLogDto);
                }
            });
        }
        return executionTime;
    }

    /**
     * 方法异常执行后日志切面
     *
     * @param e         异常对象
     * @param afterExec 切点方法执行后逻辑集合
     * @param pjp       目标切点
     * @param logDtoMap 操作日志注解对应的Spel解析结果映射
     */
    private void afterMethodThrow(Exception e, List<OperationLog> afterExec,
                                  ProceedingJoinPoint pjp, Map<OperationLog, OperateLogDto> logDtoMap) {
        // 在LogContext中写入执行后信息，将切面方法执行的异常信息写入日志记录上下文中
        OperateLogContext.putVariables(OperateLogContext.CONTEXT_KEY_NAME_ERROR_MSG, e.getMessage());
        // 如果配置了方法执行异常后继续生成操作日志的策略则继续执行(如果配置了继续生成日志的策略，则异常仍必须为主动异常类型才执行)
        if (properties.isOnError() && e instanceof FruitShopException fruitShopException) {
            // 遍历执行方法后解析切面逻辑
            loadAfterMethodInvoke(afterExec, pjp, logDtoMap);
            // 写入异常执行后日志
            List<OperateLogDto> operateLogDtoList = new ArrayList<>(logDtoMap.values());
            // 遍历日志信息LogDto集合
            operateLogDtoList.forEach(operateLogDto -> {
                // 设置执行结果为false，
                operateLogDto.setSuccess(false);
                // 写入执行异常信息
                operateLogDto.setException(fruitShopException.getDescribe());
            });
        }
    }

    /**
     * 遍历执行方法后解析切面逻辑
     *
     * @param afterExec 切点方法执行后逻辑集合
     * @param pjp       目标切点
     * @param logDtoMap 操作日志注解对应的Spel解析结果映射
     */
    private void loadAfterMethodInvoke(List<OperationLog> afterExec, ProceedingJoinPoint pjp,
                                       Map<OperationLog, OperateLogDto> logDtoMap) {
        afterExec.forEach(annotation -> {
            // 解析注解中定义的Spel表达式，返回LogDto对象
            OperateLogDto operateLogDto = resolveExpress(annotation, pjp);
            if (operateLogDto != null) {
                // 若解析后的logDto数据不为空，则将logDto放入Map中
                logDtoMap.put(annotation, operateLogDto);
            }
        });
    }

    /**
     * 操作日志切面最终执行逻辑
     *
     * @param executionTime     方法执行耗时
     * @param userAgent         客户端信息
     * @param request           请求对象
     * @param operateLogDtoList 操作日志信息对象列表
     */
    private void finalExec(long executionTime, UserAgent userAgent,
                           HttpServletRequest request, List<OperateLogDto> operateLogDtoList) {
        try {
            // 提交日志至主线程或线程池
            Consumer<OperateLogDto> createLogFunction = operateLogDto -> {
                try {
                    // 记录日志切面执行时间
                    operateLogDto.setExecutionTime(executionTime);
                    operateLogDto.setIpAddress(IpUtils.getIpAddress(request));
                    operateLogDto.setOs(userAgent.getOperatingSystem().getName());
                    operateLogDto.setBrowser(userAgent.getBrowser().getName());
                    operateLogService.createLog(operateLogDto);
                } catch (Exception e) {
                    log.error("操作日志提交至线程执行存储时失败: ", e);
                }
            };
            // 使用线程池异步处理日志
            Executor executor = FruitShopThreadPool.init();
            // 发送日志本地监听
            operateLogDtoList.forEach(operateLogDto -> executor.execute(() -> createLogFunction.accept(operateLogDto)));
            // 清除Context：每次方法执行一次
            OperateLogContext.clearContext();
        } catch (Exception e) {
            log.error("操作日志最终执行失败: ", e);
        }
    }

    /**
     * 解析注解中定义的spel表达式
     *
     * @param annotation 操作日志注解
     * @param joinPoint  连接点
     * @return LogDto 操作日志对象
     */
    private OperateLogDto resolveExpress(OperationLog annotation, ProceedingJoinPoint joinPoint) {
        // 定义LogDto对象，Spel解析后的对象
        OperateLogDto operateLogDto = null;
        // 日志内容，SpEL表达式
        String msg = annotation.msg();
        // 额外信息，SpEL表达式
        String extra = annotation.extra();
        // 操作人ID，SpEL表达式
        String operatorId = annotation.operatorId();
        // 操作人名称，SpEL表达式
        String operatorName = annotation.operatorName();
        // 日志记录条件，SpEL表达式
        String condition = annotation.condition();
        // 执行是否成功，SpEL表达式
        String success = annotation.success();
        // 执行是否成功，SpEL解析结果，默认为null
        boolean functionExecuteSuccess;
        try {
            // 封装日志对象 logDto
            operateLogDto = new OperateLogDto();
            // 获取切面的方法入参
            Object[] arguments = joinPoint.getArgs();
            // 获取切面方法
            Method method = FruitShopUtils.getMethod(joinPoint);
            // 获取切面方法的参数名
            String[] params = discoverer.getParameterNames(method);
            // 获取操作日志记录上下文
            StandardEvaluationContext context = OperateLogContext.getContext();
            // 注册自定义函数
            OperateLogFunctionRegistrar.register(context);
            if (params != null) {
                // 编辑方法参数，将参数放入日志记录上下文中
                for (int len = 0; len < params.length; len++) {
                    context.setVariable(params[len], arguments[len]);
                }
            }
            // 条件及事件类型解析
            // condition解析
            boolean passed = this.resolveBoolSpel(condition, context);
            if (!passed) {
                return null;
            }
            // success解析
            functionExecuteSuccess = this.resolveBoolSpel(success, context);
            // 业务事件解析赋值
            operateLogDto.setEvent(this.resolveStrSpel(annotation.event(), context));
            // msg解析 若为实体则JSON序列化实体
            if (StringUtils.isNotBlank(msg)) {
                Expression msgExpression = parser.parseExpression(msg);
                Object msgObj = msgExpression.getValue(context, Object.class);
                if (msgObj instanceof String string) {
                    msg = string;
                } else {
                    msg = JSON.toJSONString(msgObj, JSONWriter.Feature.WriteMapNullValue);
                }
            }
            // extra解析 若为实体则JSON序列化实体
            if (StringUtils.isNotBlank(extra)) {
                Expression extraExpression = parser.parseExpression(extra);
                Object extraObj = extraExpression.getValue(context, Object.class);
                if (extraObj instanceof String string) {
                    extra = string;
                } else {
                    extra = JSON.toJSONString(extraObj, JSONWriter.Feature.WriteMapNullValue);
                }
            }
            UserInfo userInfo = UserInfoUtils.getUserInfo();
            // operatorId解析：优先级 注解传入 > 自定义接口实现
            if (StringUtils.isNotBlank(operatorId)) {
                Expression operatorIdExpression = parser.parseExpression(operatorId);
                operatorId = operatorIdExpression.getValue(context, String.class);
            } else {
                operatorId = userInfo.getUserId() == null ? "0" : String.valueOf(userInfo.getUserId());
            }
            // operatorName解析：优先级 注解传入 > 自定义接口实现
            if (StringUtils.isNotBlank(operatorName)) {
                Expression operatorNameExpression = parser.parseExpression(operatorName);
                operatorName = operatorNameExpression.getValue(context, String.class);
            } else {
                if (userInfo.getRealName() != null) {
                    operatorName = userInfo.getRealName();
                }
            }
            operateLogDto.setOperateDate(new Date());
            operateLogDto.setMsg(msg);
            operateLogDto.setExtra(extra);
            assert operatorId != null;
            operateLogDto.setOperatorId(Long.parseLong(operatorId));
            operateLogDto.setOperatorName(operatorName);
            operateLogDto.setSuccess(functionExecuteSuccess);
            operateLogDto.setDiffVoList(OperateLogContext.getDiffVOList());
        } catch (Exception e) {
            log.error("操作日志SPEL表达式解析失败: {}", e.getMessage());
        } finally {
            // 清除Diff实体列表：每次注解执行一次
            OperateLogContext.clearDiffVOList();
        }
        return operateLogDto;
    }

    /**
     * 解析字符型spel
     *
     * @param spelStr 字符型spel
     * @param context 操作日志上下文
     * @return String 字符解析结果
     */
    private String resolveStrSpel(String spelStr, StandardEvaluationContext context) {
        if (StringUtils.isNotBlank(spelStr)) {
            Expression expression = parser.parseExpression(spelStr);
            spelStr = expression.getValue(context, String.class);
        }
        return spelStr;
    }

    /**
     * 解析布尔型spel
     *
     * @param spelBool 布尔型spel
     * @param context  操作日志上下文
     * @return boolean 布尔值解析结果
     */
    private boolean resolveBoolSpel(String spelBool, StandardEvaluationContext context) {
        boolean result = false;
        if (StringUtils.isNotBlank(spelBool)) {
            Expression expression = parser.parseExpression(spelBool);
            result = Boolean.TRUE.equals(expression.getValue(context, Boolean.class));
        }
        return result;
    }
}
