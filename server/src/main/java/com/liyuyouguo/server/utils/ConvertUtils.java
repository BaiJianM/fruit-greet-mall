package com.liyuyouguo.server.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

/**
 * BeanUtils增强工具类(S，源对象类型；T，目标对象类型)
 *
 * @author baijianmin
 */
@Slf4j
public class ConvertUtils extends BeanUtils {

    /**
     * 将源对象按提供的目标对象函数表达式进行转换
     *
     * @param source         转换源对象
     * @param targetSupplier 目标对象函数表达式
     * @return Optional<T> 带有目标对象的容器
     */
    public static <S, T> Optional<T> convert(S source, Supplier<T> targetSupplier) {
        return convert(source, targetSupplier, null);
    }

    public static <S, T> Optional<T> convert(S source, Supplier<T> targetSupplier, BiConsumer<S, T> callBack) {
        // 如果提供的转换源对象为null或未提供目标对象函数表达式，则返回空Optional
        if (null == source || null == targetSupplier) {
            return Optional.empty();
        }
        // 调用表达式方法取出转换的目标对象
        T target = targetSupplier.get();
        // 使用Spring的BeanUtils工具类的内置属性copy方法进行拷贝
        copyProperties(source, target);
        // 如果存在指定特殊回调，则进行函数调用
        if (callBack != null) {
            callBack.accept(source, target);
        }
        return Optional.of(target);
    }

    /**
     * 将源对象列表按提供的目标对象函数表达式进行转换
     *
     * @param sources        转换源对象列表
     * @param targetSupplier 目标对象函数表达式
     * @return Optional<T> 带有目标对象列表的容器
     */
    public static <S, T> Optional<Collection<T>> convertCollection(Collection<S> sources, Supplier<T> targetSupplier) {
        return convertCollection(sources, targetSupplier, null);
    }

    public static <S, T> Optional<Collection<T>> convertCollection(Collection<S> sources,
                                                                   Supplier<T> targetSupplier,
                                                                   BiConsumer<S, T> callBack) {
        Collection<T> c = null;
        // 判断源对象列表类型
        if (sources instanceof List) {
            // 调用表达式方法取出转换的目标对象列表并转换ArrayList
            c = new ArrayList<>(sources.size());
        } else if (sources instanceof Set) {
            // 调用表达式方法取出转换的目标对象列表并转换HashSet
            c = new HashSet<>(sources.size());
        }
        // 如果提供的转换源对象列表为null或列表内无元素或提供目标对象函数表达式，则返回空Optional
        if (sources.isEmpty() || null == targetSupplier || null == c) {
            return Optional.empty();
        }
        // 遍历列表元素
        for (S source : sources) {
            // 取出列表对象元素
            T target = targetSupplier.get();
            // 使用Spring的BeanUtils工具类的内置属性copy方法进行拷贝
            copyProperties(source, target);
            // 如果存在指定特殊回调，则进行函数调用
            if (callBack != null) {
                callBack.accept(source, target);
            }
            c.add(target);
        }
        return Optional.of(c);
    }

    /**
     * 将驼峰命名法转为下划线命名法
     * @param str 驼峰命名的字符串
     * @return 下划线命名的字符串
     */
    public static String camelToSnake(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }

        StringBuilder result = new StringBuilder();
        char[] characters = str.toCharArray();

        for (char c : characters) {
            // 如果是大写字母, 转为小写并加上下划线
            if (Character.isUpperCase(c)) {
                // 在第一个字符或大写字母前加下划线
                if (!result.isEmpty()) {
                    result.append("_");
                }
                result.append(Character.toLowerCase(c));
            } else {
                result.append(c);
            }
        }
        return result.toString();
    }
}
