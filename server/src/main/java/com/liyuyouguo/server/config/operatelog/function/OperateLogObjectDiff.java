package com.liyuyouguo.server.config.operatelog.function;

import com.alibaba.fastjson2.JSON;
import com.liyuyouguo.server.annotations.operatelog.LogDiff;
import com.liyuyouguo.server.annotations.operatelog.LogFunction;
import com.liyuyouguo.server.config.operatelog.context.OperateLogContext;
import com.liyuyouguo.server.config.operatelog.pojo.DiffFieldVo;
import com.liyuyouguo.server.config.operatelog.pojo.DiffVo;
import com.liyuyouguo.server.config.operatelog.properties.OperateLogProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringSubstitutor;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 日志对象差异分析类
 *
 * @author baijianmin
 */
@Slf4j
@Component
@LogFunction
public class OperateLogObjectDiff {

    public static final String DEFAULT_DIFF_MSG_FORMAT = "编辑前【${_beforeMsg}】, 编辑后【${_afterMsg}】";
    public static final String DEFAULT_ADD_MSG_FORMAT = "【${_afterMsg}】";
    public static final String DEFAULT_MSG_FORMAT = "%s: %s";
    public static final String DEFAULT_MSG_SEPARATOR = ", ";

    /**
     * 对象属性变更时的描述
     */
    private static String DIFF_MSG_FORMAT;
    /**
     * 新增对象属性时的描述
     */
    private static String ADD_MSG_FORMAT;
    /**
     * 对象属性键值对映射描述，第一个属性为键，第二个属性为值
     */
    private static String MSG_FORMAT;
    /**
     * 多个对象属性键值对转字符串的分隔符
     */
    private static String MSG_SEPARATOR;

    public OperateLogObjectDiff(OperateLogProperties properties) {
        DIFF_MSG_FORMAT = properties.getDiffMsgFormat().equals(DEFAULT_DIFF_MSG_FORMAT)
                ? DEFAULT_DIFF_MSG_FORMAT
                : new String(properties.getDiffMsgFormat().getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);

        ADD_MSG_FORMAT = properties.getAddMsgFormat().equals(DEFAULT_ADD_MSG_FORMAT)
                ? DEFAULT_ADD_MSG_FORMAT
                : new String(properties.getAddMsgFormat().getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);

        MSG_FORMAT = properties.getMsgFormat().equals(DEFAULT_MSG_FORMAT)
                ? DEFAULT_MSG_FORMAT
                : new String(properties.getMsgFormat().getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);

        MSG_SEPARATOR = properties.getMsgSeparator().equals(DEFAULT_MSG_SEPARATOR)
                ? DEFAULT_MSG_SEPARATOR
                : new String(properties.getMsgSeparator().getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
    }

    /**
     * 计算并记录对象变更的属性
     *
     * @param oldObject 原对象
     * @param newObject 新对象
     * @return String 差异描述
     */
    @LogFunction("_DIFF")
    public static String diff(Object oldObject, Object newObject) {
        StringBuilder msg = new StringBuilder();
        // 若包含null对象，直接返回
        if (oldObject == null || newObject == null) {
            log.warn("计算对象属性差异时检测到空对象类型，原对象：[{}]，新对象：[{}]", oldObject, newObject);
            return msg.toString();
        }
        DiffVo diffVO = getDiff(oldObject, newObject);
        List<String> beforeList = new ArrayList<>();
        List<String> afterList = new ArrayList<>();
        diffVO.getDiffFieldVoList().forEach(df -> {
            String fieldName = StringUtils.isBlank(df.getFieldAlias()) ? df.getFieldName() : df.getFieldAlias();
            beforeList.add(String.format(MSG_FORMAT, fieldName, df.getOldValue()));
            afterList.add(String.format(MSG_FORMAT, fieldName, df.getNewValue()));
        });
        if (!beforeList.isEmpty()) {
            Map<String, String> msgMap = new LinkedHashMap<>();
            msgMap.put("_beforeMsg", String.join(MSG_SEPARATOR, beforeList));
            msgMap.put("_afterMsg", String.join(MSG_SEPARATOR, afterList));
            StringSubstitutor sub = new StringSubstitutor(msgMap);
            msg.append(sub.replace(DIFF_MSG_FORMAT));
            OperateLogContext.addDiffVO(diffVO);
        } else {
            log.debug("操作日志: 编辑前后数据相同, 不处理");
        }
        return msg.toString();
    }

    /**
     * 计算并记录新增的属性
     *
     * @param newObject 新对象
     * @return String 对象新增描述
     * @throws InstantiationException    实例化异常
     * @throws IllegalAccessException    访问权限异常
     * @throws NoSuchMethodException     无参构造器异常
     * @throws InvocationTargetException 对象执行异常
     */
    @LogFunction("_ADD")
    public static String add(Object newObject) throws InstantiationException, IllegalAccessException,
            NoSuchMethodException, InvocationTargetException {
        StringBuilder msg = new StringBuilder();
        // 若包含null对象，直接返回
        if (newObject == null) {
            log.warn("计算对象属性新增时检测到空对象类型 null");
            return msg.toString();
        }
        Object oldObject = newObject.getClass().getDeclaredConstructor().newInstance();
        DiffVo diffVO = getDiff(oldObject, newObject);
        List<String> addMsgList = diffVO.getDiffFieldVoList().stream().map(item -> {
            String fieldName = StringUtils.isBlank(item.getFieldAlias()) ? item.getFieldName() : item.getFieldAlias();
            return String.format(MSG_FORMAT, fieldName, item.getNewValue());
        }).collect(Collectors.toList());
        Map<String, String> msgMap = new LinkedHashMap<>();
        if (!addMsgList.isEmpty()) {
            msgMap.put("_afterMsg", String.join(MSG_SEPARATOR, addMsgList));
            StringSubstitutor sub = new StringSubstitutor(msgMap);
            msg.append(sub.replace(ADD_MSG_FORMAT));
            OperateLogContext.addDiffVO(diffVO);
        } else {
            log.debug("操作日志: 编辑前后数据相同, 不处理");
        }
        return msg.toString();
    }

    /**
     * 记录删除的对象信息
     *
     * @param delObject 待删除对象
     * @return String 删除对象描述
     */
    @LogFunction("_DEL")
    public static String del(Object delObject) {
        // 若包含null对象，直接返回
        if (delObject == null) {
            log.warn("计算对象删除时检测到空对象类型 null");
            return "";
        }
        return JSON.toJSONString(delObject);
    }

    /**
     * 获取差异值对象
     *
     * @param oldObject 旧对象
     * @param newObject 新对象
     * @return DiffVo 差异值对象
     */
    public static DiffVo getDiff(Object oldObject, Object newObject) {
        Class<?> oClass = oldObject.getClass();
        Class<?> nClass = newObject.getClass();
        // 获取新旧对象类名
        String oldClassName = oClass.getName();
        String newClassName = nClass.getName();
        // 获取类上的注解
        LogDiff oldClassLogDiff = oClass.getDeclaredAnnotation(LogDiff.class);
        LogDiff newClassLogDiff = nClass.getDeclaredAnnotation(LogDiff.class);
        // 获取类上注解的别名
        String oldClassAlias = oldClassLogDiff != null && StringUtils.isNotBlank(oldClassLogDiff.alias())
                ? oldClassLogDiff.alias() : null;
        String newClassAlias = newClassLogDiff != null && StringUtils.isNotBlank(newClassLogDiff.alias())
                ? newClassLogDiff.alias() : null;
        log.debug("旧对象类名：[{}]，旧对象别名 [{}]，新对象类名：[{}]，新对象别名：[{}]",
                oldClassName, oldClassAlias, newClassName, newClassAlias);
        // 新旧对象变更的属性键值集合
        Map<String, Object> oldValueMap = new LinkedHashMap<>();
        Map<String, Object> newValueMap = new LinkedHashMap<>();
        // 初始化差异值VO
        DiffVo diffVO = new DiffVo();
        // 设置旧对象的class类名，别名
        diffVO.setOldClassName(oldClassName);
        diffVO.setOldClassAlias(oldClassAlias);
        // 设置新对象的class类名，别名
        diffVO.setNewClassName(newClassName);
        diffVO.setNewClassAlias(newClassAlias);
        // 定义对象属性变更列表
        List<DiffFieldVo> diffFieldVoList = new ArrayList<>();
        diffVO.setDiffFieldVoList(diffFieldVoList);

        // 获取类的字段
        Field[] fields = getAllFields(oClass);
        // 遍历字段加了@LogDiff注解的字段
        for (Field oldField : fields) {
            LogDiff oldObjectLogDiff = oldField.getDeclaredAnnotation(LogDiff.class);
            // 若没有@LogDiff注解，跳过
            if (oldObjectLogDiff == null) {
                continue;
            }
            try {
                String fieldName = oldField.getName();
                // 在新对象中寻找同名字段，若找不到则跳过本次循环
                Field newField = getFieldByName(nClass, oldField.getName());
                // 获取对象该字段的别名（旧对象和新对象属性名一致，一般不存在字段属性名变更但别名不变的情况）
                String fieldAlias = oldObjectLogDiff.alias();
                // 关闭字段安全检查
                oldField.setAccessible(true);
                newField.setAccessible(true);
                // 获取旧对象字段值
                Object oldValue = oldField.get(oldObject);
                // 获取新对象字段值
                Object newValue = newField.get(newObject);
                // 若新旧值不同，则将数据放入Map
                if (newValue != null && !newValue.equals(oldValue)) {
                    if (log.isDebugEnabled()) {
                        log.debug("对象字段属性 [{}] 发生变更，旧值：[{}]，新值：[{}]", oldField.getName(), oldValue, newValue);
                        oldValueMap.put(fieldName, oldValue);
                        newValueMap.put(fieldName, newValue);
                    }
                    // 初始化对象变更属性值
                    DiffFieldVo diffFieldVO = new DiffFieldVo();
                    diffFieldVO.setFieldName(fieldName);
                    diffFieldVO.setFieldAlias(fieldAlias);
                    diffFieldVO.setOldValue(oldValue);
                    diffFieldVO.setNewValue(newValue);
                    // 存入变更对象列表
                    diffFieldVoList.add(diffFieldVO);
                }
            } catch (IllegalAccessException e) {
                log.error("遍历@LogDiff注解字段出错，错误信息：{}", e.getMessage());
            }
        }
        if (log.isDebugEnabled()) {
            log.debug("旧对象字段属性键值映射 [{}]", oldValueMap);
            log.debug("新对象字段属性键值映射 [{}]", newValueMap);
        }
        return diffVO;
    }

    /**
     * 获取本类和父类所有字段
     *
     * @param clazz 父类
     * @return Field[] 父子类所有属性字段
     */
    public static Field[] getAllFields(Class<?> clazz) {
        List<Field> fieldList = new ArrayList<>();
        while (clazz != null) {
            fieldList.addAll(new ArrayList<>(Arrays.asList(clazz.getDeclaredFields())));
            clazz = clazz.getSuperclass();
        }
        Field[] fields = new Field[fieldList.size()];
        return fieldList.toArray(fields);
    }

    /**
     * 根据class和字段名称获取字段信息
     *
     * @param clazz     目标类
     * @param fieldName 字段名称
     * @return Field 属性字段
     */
    public static Field getFieldByName(Class<?> clazz, String fieldName) {
        List<Field> fieldList = new ArrayList<>();
        while (clazz != null) {
            fieldList.addAll(new ArrayList<>(Arrays.asList(clazz.getDeclaredFields())));
            clazz = clazz.getSuperclass();
        }
        Map<String, Field> fieldMap = fieldList.stream().collect(Collectors.toMap(Field::getName, Function.identity()));
        return fieldMap.get(fieldName);
    }
}
