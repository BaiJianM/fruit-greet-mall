package com.liyuyouguo.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.ZSetOperations.TypedTuple;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @description: Redis工具类
 * @author: baijianmin
 * @dateTime: 2022/7/8 08:46
 */
@Slf4j
@Component
public class RedisUtils {

    /**
     * 处理分布式锁的可重入
     */
    ThreadLocal<Map<String, Integer>> threadLocal = ThreadLocal.withInitial(HashMap::new);

    /**
     * 定时任务线程池
     */
    private ScheduledThreadPoolExecutor scheduledExecutorService = new ScheduledThreadPoolExecutor(1);

    private final RedisTemplate<String, Object> redisTemplate;

    public RedisUtils(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

// -------------------key 相关操作---------------------

    /**
     * 删除 key
     *
     * @param key 要删除的 键
     */
    public void delete(String key) {
        redisTemplate.delete(key);
    }

    /**
     * 批量删除 key
     *
     * @param keys 要删除的 键 的集合
     */
    public void delete(Collection<String> keys) {
        redisTemplate.delete(keys);
    }

    /**
     * 是否存在 key
     *
     * @param key 判断该 键 是否存在
     * @return 存在返回 true, 不存在返回 false
     */
    public Optional<Boolean> hasKey(String key) {
        return Optional.ofNullable(redisTemplate.hasKey(key));
    }

    /**
     * 设置过期时间
     *
     * @param key     要设置的 键
     * @param timeout 过期时间
     * @param unit    时间单位
     */
    public void expire(String key, long timeout, TimeUnit unit) {
        redisTemplate.expire(key, timeout, unit);
    }

    /**
     * 设置在什么时间过期
     *
     * @param key  要设置的 键
     * @param date 过期时间
     * @return 设置成功返回 true, 设置失败返回 false
     */
    public Optional<Boolean> expireAt(String key, Date date) {
        return Optional.ofNullable(redisTemplate.expireAt(key, date));
    }

    /**
     * 查找匹配的 key
     *
     * @param pattern 匹配字符串
     * @return 满足匹配条件的 键 的 Set 集合
     */
    public Optional<Set<String>> keys(String pattern) {
        return Optional.ofNullable(redisTemplate.keys(pattern));
    }

    /**
     * @param key      查找匹配的key
     * @param isSingle 是否单通配符查找
     * @description: 带通配符的key值查找
     * @author: baijianmin
     * @date: 2022-08-02 19:56:07
     * @return: java.util.Optional<java.util.Set < java.lang.String>>
     * @version: 1.0
     */
    public Optional<Set<String>> keys(String key, Boolean isSingle) {
        return Boolean.TRUE.equals(isSingle) ?
                Optional.ofNullable(redisTemplate.keys(key + "?")) :
                Optional.ofNullable(redisTemplate.keys(key + "*"));
    }

    /**
     * 将当前数据库的 key 移动到给定的数据库 db 当中
     *
     * @param key     要移动的 键
     * @param dbIndex 要移动到的 db 的序号, 从 0 开始
     * @return 移动成功返回 true, 移动失败返回 false
     */
    public Optional<Boolean> move(String key, int dbIndex) {
        return Optional.ofNullable(redisTemplate.move(key, dbIndex));
    }

    /**
     * 移除 key 的过期时间，key 将持久保持
     *
     * @param key 要移除过期时间的 键
     * @return 移除成功返回 true, 并且该 key 将持久存在, 移除失败返回 false
     */
    public Optional<Boolean> persist(String key) {
        return Optional.ofNullable(redisTemplate.persist(key));
    }

    /**
     * 返回 key 的剩余的过期时间
     *
     * @param key  要查询剩余过期时间的 键
     * @param unit 时间的单位
     * @return 剩余的过期时间
     */
    public Optional<Long> getExpire(String key, TimeUnit unit) {
        return Optional.ofNullable(redisTemplate.getExpire(key, unit));
    }

    /**
     * 返回 key 的剩余的过期时间, 默认时间单位: 秒
     *
     * @param key 要查询剩余过期时间的 键
     * @return 剩余的过期时间, 单位: 秒
     */
    public Optional<Long> getExpire(String key) {
        return Optional.ofNullable(redisTemplate.getExpire(key));
    }

    /**
     * 从当前数据库中随机返回一个 key
     *
     * @return 随机获取的 键 的名称
     */
    public Optional<String> randomKey() {
        return Optional.ofNullable(redisTemplate.randomKey());
    }

    /**
     * 修改 key 的名称
     *
     * @param oldKey 修改前的 键 的名称
     * @param newKey 修改后的 键 的名称
     */
    public void rename(String oldKey, String newKey) {
        redisTemplate.rename(oldKey, newKey);
    }

    /**
     * 仅当 newKey 不存在时，将 oldKey 改名为 newKey
     *
     * @param oldKey 修改前的 键 的名称
     * @param newKey 修改后的 键 的名称
     * @return 修改成功返回 true, 修改失败返回 false
     */
    public Optional<Boolean> renameIfAbsent(String oldKey, String newKey) {
        return Optional.ofNullable(redisTemplate.renameIfAbsent(oldKey, newKey));
    }

    /**
     * 返回 key 所储存的值的类型
     *
     * @param key 要查询类型的 键
     * @return key 的数据类型
     */
    public Optional<DataType> type(String key) {
        return Optional.ofNullable(redisTemplate.type(key));
    }

    // -------------------string 相关操作---------------------

    /**
     * 设置指定 key 的值
     *
     * @param key   要设置的 键
     * @param value 要设置的 值
     */
    public void set(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * 获取指定 key 的值
     *
     * @param key 键 的名称
     * @return 键 对应的 值
     */
    public Optional<Object> get(String key) {
        return Optional.ofNullable(redisTemplate.opsForValue().get(key));
    }

    /**
     * 返回 key 的 字符串值 中指定位置的 子字符串
     *
     * @param key   要获取值的 键
     * @param start 开始位置, 最小值: 0
     * @param end   结束位置, 最大值: 字符串 - 1, 若为 -1 则是获取整个字符串值
     * @return 指定 键 的 字符串值 的 子字符串
     */
    public Optional<String> getRange(String key, long start, long end) {
        return Optional.ofNullable(redisTemplate.opsForValue().get(key, start, end));
    }

    /**
     * 将给定 key 的值设为 value ，并返回 key 的旧值( old value )
     *
     * @param key   要设置值的 键
     * @param value 新值
     * @return 旧值
     */
    public Optional<Object> getAndSet(String key, Object value) {
        return Optional.ofNullable(redisTemplate.opsForValue().getAndSet(key, value));
    }

    /**
     * 对 key 所储存的字符串值，获取指定偏移量上的位( bit )
     *
     * @param key    键
     * @param offset 偏移量
     * @return 指定偏移量上的 位( 0 / 1)
     */
    public Optional<Boolean> getBit(String key, long offset) {
        return Optional.ofNullable(redisTemplate.opsForValue().getBit(key, offset));
    }

    /**
     * 批量获取 key 的 值
     *
     * @param keys 要获取值的 键 的集合
     * @return key对应的值的集合
     */
    public Optional<List<Object>> multiGet(Collection<String> keys) {
        return Optional.ofNullable(redisTemplate.opsForValue().multiGet(keys));
    }

    /**
     * 设置ASCII码, 字符串'a'的ASCII码是97, 转为二进制是'01100001', 此方法是将二进制第 offset 位值变为 value
     *
     * @param key    要设置的 键
     * @param offset 偏移多少位
     * @param value  值, true 为 1,  false 为 0
     * @return 设置成功返回 true, 设置失败返回 false
     */
    public Optional<Boolean> setBit(String key, long offset, boolean value) {
        return Optional.ofNullable(redisTemplate.opsForValue().setBit(key, offset, value));
    }

    /**
     * 将值 value 关联到 key ，并将 key 的过期时间设为 timeout
     *
     * @param key     键
     * @param value   值
     * @param timeout 过期时间
     * @param unit    时间单位, 天: TimeUnit.DAYS 小时: TimeUnit.HOURS 分钟: TimeUnit.MINUTES
     *                秒: TimeUnit.SECONDS 毫秒: TimeUnit.MILLISECONDS
     */
    public void setEx(String key, Object value, long timeout, TimeUnit unit) {
        redisTemplate.opsForValue().set(key, value, timeout, unit);
    }

    /**
     * 只有在 key 不存在时设置 key 的值
     *
     * @param key   键
     * @param value 值
     * @return 之前已经存在返回 false, 不存在返回 true
     */
    public Optional<Boolean> setIfAbsent(String key, Object value) {
        return Optional.ofNullable(redisTemplate.opsForValue().setIfAbsent(key, value));
    }

    /**
     * 用 value 参数覆写给定 key 所储存的字符串值，从偏移量 offset 开始
     *
     * @param key    键
     * @param value  值
     * @param offset 从指定位置开始覆写
     */
    public void setRange(String key, Object value, long offset) {
        redisTemplate.opsForValue().set(key, value, offset);
    }

    /**
     * 获取字符串的长度
     *
     * @param key 键
     * @return 该 key 对应的 值的长度
     */
    public Optional<Long> size(String key) {
        return Optional.ofNullable(redisTemplate.opsForValue().size(key));
    }

    /**
     * 批量添加 key-value
     *
     * @param maps key-value 的 map 集合
     */
    public void multiSet(Map<String, Object> maps) {
        redisTemplate.opsForValue().multiSet(maps);
    }

    /**
     * 同时设置一个或多个 key-value 对，当且仅当所有给定 key 都不存在才会设置成功
     *
     * @param maps key-value 集合
     * @return 之前已经存在返回 false, 不存在返回 true
     */
    public Optional<Boolean> multiSetIfAbsent(Map<String, Object> maps) {
        return Optional.ofNullable(redisTemplate.opsForValue().multiSetIfAbsent(maps));
    }

    /**
     * 增加(自增长), 负数则为自减
     *
     * @param key       键
     * @param increment 自增量
     * @return 增加后的值
     */
    public Optional<Long> incrBy(String key, long increment) {
        return Optional.ofNullable(redisTemplate.opsForValue().increment(key, increment));
    }

    /**
     * 自增长, 增长量为浮点数
     *
     * @param key       键
     * @param increment 自增量
     * @return 增加后的值
     */
    public Optional<Double> incrByFloat(String key, double increment) {
        return Optional.ofNullable(redisTemplate.opsForValue().increment(key, increment));
    }

    /**
     * 将 value 追加到指定 key 的值的末尾
     *
     * @param key   键
     * @param value 要追加的值
     * @return 追加值后新值的长度
     */
    public Integer append(String key, String value) {
        return redisTemplate.opsForValue().append(key, value);
    }

    // -------------------hash 相关操作-------------------------

    /**
     * 获取存储在哈希表中指定字段的值
     *
     * @param key   键
     * @param field 字段名( 即 map 中的 key )
     * @return 值
     */
    public Optional<Object> hGet(String key, String field) {
        return Optional.ofNullable(redisTemplate.opsForHash().get(key, field));
    }

    /**
     * 获取给定 哈希表 中的所有键值对
     *
     * @param key 哈希表
     * @return 所有的 键值对
     */
    public Optional<Map<Object, Object>> hGetAll(String key) {
        return Optional.of(redisTemplate.opsForHash().entries(key));
    }

    /**
     * 获取指定 哈希表 中所有给定字段的值
     *
     * @param key    哈希表
     * @param fields 要获取值的字段集合
     * @return 哈希表中所有给定字段的值
     */
    public Optional<List<Object>> hMultiGet(String key, Collection<Object> fields) {
        return Optional.of(redisTemplate.opsForHash().multiGet(key, fields));
    }

    /**
     * 向指定 哈希表 中存储一个 键值对
     *
     * @param key     哈希表
     * @param hashKey 字段名
     * @param value   值
     */
    public void hPut(String key, String hashKey, Object value) {
        redisTemplate.opsForHash().put(key, hashKey, value);
    }

    /**
     * 向指定 哈希表 中存储多个 键值对
     *
     * @param key  哈希表
     * @param maps 键值对集合
     */
    public void hPutAll(String key, Map<String, Object> maps) {
        redisTemplate.opsForHash().putAll(key, maps);
    }

    /**
     * 仅当 hashKey 不存在时才设置
     *
     * @param key     哈希表
     * @param hashKey 字段名
     * @param value   值
     * @return 设置成功返回 true, 设置失败返回 false
     */
    public Optional<Boolean> hPutIfAbsent(String key, String hashKey, Object value) {
        return Optional.of(redisTemplate.opsForHash().putIfAbsent(key, hashKey, value));
    }

    /**
     * 删除哈希表中一个或多个字段
     *
     * @param key    哈希表
     * @param fields 要删除的字段集合
     * @return 删除成功的数目
     */
    public Optional<Long> hDelete(String key, Object... fields) {
        return Optional.of(redisTemplate.opsForHash().delete(key, fields));
    }

    /**
     * 查看哈希表中指定的字段是否存在
     *
     * @param key   要查看的哈希表
     * @param field 字段名
     * @return 存在返回 true, 不存在返回 false
     */
    public Optional<Boolean> hExists(String key, String field) {
        return Optional.of(redisTemplate.opsForHash().hasKey(key, field));
    }

    /**
     * 为哈希表 key 中的指定字段的整数值加上增量 increment
     *
     * @param key       指定的哈希表
     * @param field     字段名
     * @param increment 增加的量
     * @return 增加后的值
     */
    public Optional<Long> hIncrBy(String key, Object field, long increment) {
        return Optional.of(redisTemplate.opsForHash().increment(key, field, increment));
    }

    /**
     * 为哈希表 key 中的指定字段的整数值加上增量 increment( 浮点型 )
     *
     * @param key   指定的哈希表
     * @param field 字段名
     * @param delta 增加的量( 浮点型 )
     * @return 增加后的值
     */
    public Optional<Double> hIncrByFloat(String key, Object field, double delta) {
        return Optional.of(redisTemplate.opsForHash().increment(key, field, delta));
    }

    /**
     * 获取所有哈希表中的字段
     *
     * @param key 哈希表
     * @return 所有的 字段
     */
    public Optional<Set<Object>> hKeys(String key) {
        return Optional.of(redisTemplate.opsForHash().keys(key));
    }

    /**
     * 获取哈希表中字段的数量
     *
     * @param key 哈希表
     * @return 哈希表所有字段的数量
     */
    public Optional<Long> hSize(String key) {
        return Optional.of(redisTemplate.opsForHash().size(key));
    }

    /**
     * 获取哈希表中所有值
     *
     * @param key 哈希表
     * @return 哈希表中所有的值
     */
    public Optional<List<Object>> hValues(String key) {
        return Optional.of(redisTemplate.opsForHash().values(key));
    }

    /**
     * 迭代哈希表中的键值对
     *
     * @param key     哈希表
     * @param options 迭代的限制条件, 为 ScanOptions.NONE 则无限制
     * @return 下一个键值对元组的游标
     */
    public Optional<Cursor<Entry<Object, Object>>> hScan(String key, ScanOptions options) {
        return Optional.of(redisTemplate.opsForHash().scan(key, options));
    }

    // ------------------------list 相关操作----------------------------

    /**
     * 通过索引获取列表中的元素
     *
     * @param key   元素所在的列表
     * @param index 下标, 从 0 开始
     * @return 列表中指定下标的值
     */
    public Optional<Object> lIndex(String key, long index) {
        return Optional.ofNullable(redisTemplate.opsForList().index(key, index));
    }

    /**
     * 获取列表指定范围内的元素
     *
     * @param key   元素所在列表
     * @param start 开始位置, 0 是开始位置
     * @param end   结束位置, -1 返回所有
     * @return 指定索引范围内的元素
     */
    public Optional<List<Object>> lRange(String key, long start, long end) {
        return Optional.ofNullable(redisTemplate.opsForList().range(key, start, end));
    }

    /**
     * 存储在 list 头部( 左边 )
     *
     * @param key   列表
     * @param value 存储的值
     * @return 列表长度
     */
    public Optional<Long> lLeftPush(String key, Object value) {
        return Optional.ofNullable(redisTemplate.opsForList().leftPush(key, value));
    }

    /**
     * 将多个值存入列表中
     *
     * @param key   列表
     * @param value 值, 可以输入多个
     * @return 列表长度
     */
    public Optional<Long> lLeftPushAll(String key, Object... value) {
        return Optional.ofNullable(redisTemplate.opsForList().leftPushAll(key, value));
    }

    /**
     * 将多个值存入列表中
     *
     * @param key   列表
     * @param value 值的集合
     * @return 列表长度
     */
    public Optional<Long> lLeftPushAll(String key, Collection<Object> value) {
        return Optional.ofNullable(redisTemplate.opsForList().leftPushAll(key, value));
    }

    /**
     * 当 list 存在的时候才加入
     *
     * @param key   列表
     * @param value 值
     * @return 列表长度
     */
    public Optional<Long> lLeftPushIfPresent(String key, Object value) {
        return Optional.ofNullable(redisTemplate.opsForList().leftPushIfPresent(key, value));
    }

    /**
     * 如果 pivot 存在,在 pivot 前面添加
     *
     * @param key   列表
     * @param pivot 基准值
     * @param value 要添加的值
     * @return 列表长度
     */
    public Optional<Long> lLeftPush(String key, Object pivot, Object value) {
        return Optional.ofNullable(redisTemplate.opsForList().leftPush(key, pivot, value));
    }

    /**
     * 存储在 list 尾部( 右边 )
     *
     * @param key   列表
     * @param value 值
     * @return 列表长度
     */
    public Optional<Long> lRightPush(String key, Object value) {
        return Optional.ofNullable(redisTemplate.opsForList().rightPush(key, value));
    }

    /**
     * 将多个值存入列表中
     *
     * @param key   列表
     * @param value 值, 可以输入多个
     * @return 列表长度
     */
    public Optional<Long> lRightPushAll(String key, Object... value) {
        return Optional.ofNullable(redisTemplate.opsForList().rightPushAll(key, value));
    }

    /**
     * 将多个值存入列表中
     *
     * @param key   列表
     * @param value 值的集合
     * @return 列表长度
     */
    public Optional<Long> lRightPushAll(String key, Collection<Object> value) {
        return Optional.ofNullable(redisTemplate.opsForList().rightPushAll(key, value));
    }

    /**
     * 为已存在的列表添加值
     *
     * @param key   存在的列表
     * @param value 值
     * @return 列表长度
     */
    public Optional<Long> lRightPushIfPresent(String key, Object value) {
        return Optional.ofNullable(redisTemplate.opsForList().rightPushIfPresent(key, value));
    }

    /**
     * 在 pivot 元素的右边添加值
     *
     * @param key   列表
     * @param pivot 基准值
     * @param value 要添加的值
     * @return 列表长度
     */
    public Optional<Long> lRightPush(String key, Object pivot, Object value) {
        return Optional.ofNullable(redisTemplate.opsForList().rightPush(key, pivot, value));
    }

    /**
     * 通过索引设置列表元素的值
     *
     * @param key   列表
     * @param index 位置
     * @param value 值
     */
    public void lSet(String key, long index, Object value) {
        redisTemplate.opsForList().set(key, index, value);
    }

    /**
     * 移出并获取列表的第一个元素
     *
     * @param key 列表
     * @return 删除的元素
     */
    public Optional<Object> lLeftPop(String key) {
        return Optional.ofNullable(redisTemplate.opsForList().leftPop(key));
    }

    /**
     * 移出并获取列表的第一个元素, 如果列表没有元素会阻塞列表直到等待超时或发现可弹出元素为止
     *
     * @param key     列表
     * @param timeout 等待时间
     * @param unit    时间单位
     * @return 删除的元素
     */
    public Optional<Object> lrLeftPop(String key, long timeout, TimeUnit unit) {
        return Optional.ofNullable(redisTemplate.opsForList().leftPop(key, timeout, unit));
    }

    /**
     * 移除并获取列表最后一个元素
     *
     * @param key 列表
     * @return 删除的元素
     */
    public Optional<Object> lRightPop(String key) {
        return Optional.ofNullable(redisTemplate.opsForList().rightPop(key));
    }

    /**
     * 移出并获取列表的最后一个元素, 如果列表没有元素会阻塞列表直到等待超时或发现可弹出元素为止
     *
     * @param key     列表
     * @param timeout 等待时间
     * @param unit    时间单位
     * @return 删除的元素
     */
    public Optional<Object> lrRightPop(String key, long timeout, TimeUnit unit) {
        return Optional.ofNullable(redisTemplate.opsForList().rightPop(key, timeout, unit));
    }

    /**
     * 移除列表的最后一个元素，并将该元素添加到另一个列表并返回
     *
     * @param sourceKey      要移除元素的列表
     * @param destinationKey 要添加元素的列表
     * @return 移动的元素
     */
    public Optional<Object> lRightPopAndLeftPush(String sourceKey, String destinationKey) {
        return Optional.ofNullable(redisTemplate.opsForList().rightPopAndLeftPush(sourceKey, destinationKey));
    }

    /**
     * 从列表中弹出一个值，将弹出的元素插入到另外一个列表中并返回它; 如果列表没有元素会阻塞列表直到等待超时或发现可弹出元素为止
     *
     * @param sourceKey      要移除元素的列表
     * @param destinationKey 要添加元素的列表
     * @param timeout        等待时间
     * @param unit           时间单位
     * @return 移动的元素
     */
    public Optional<Object> lrRightPopAndLeftPush(String sourceKey, String destinationKey, long timeout, TimeUnit unit) {
        return Optional.ofNullable(redisTemplate.opsForList()
                .rightPopAndLeftPush(sourceKey, destinationKey, timeout, unit));
    }

    /**
     * 删除集合中值等于 value 的元素
     *
     * @param key   列表
     * @param index index = 0, 删除所有值等于value的元素;
     *              index > 0, 从头部开始删除第一个值等于 value 的元素;
     *              index < 0, 从尾部开始删除第一个值等于 value 的元素;
     * @param value 值
     * @return 列表长度
     */
    public Optional<Long> lRemove(String key, long index, Object value) {
        return Optional.ofNullable(redisTemplate.opsForList().remove(key, index, value));
    }

    /**
     * 裁剪 list
     *
     * @param key   列表
     * @param start 起始位置
     * @param end   结束位置
     * @see <a href="https://redis.io/commands/ltrim">Redis Documentation: LTRIM</a>
     */
    public void lTrim(String key, long start, long end) {
        redisTemplate.opsForList().trim(key, start, end);
    }

    /**
     * 获取列表长度
     *
     * @param key 列表
     * @return 列表长度
     */
    public Optional<Long> lLen(String key) {
        return Optional.ofNullable(redisTemplate.opsForList().size(key));
    }

    // --------------------set 相关操作--------------------------

    /**
     * set 添加元素
     *
     * @param key    集合
     * @param values 值, 可以同时添加多个
     * @return 集合长度
     */
    public Optional<Long> sAdd(String key, Object... values) {
        return Optional.ofNullable(redisTemplate.opsForSet().add(key, values));
    }

    /**
     * set 移除元素
     *
     * @param key    集合
     * @param values 要移除的元素, 可以同时移除多个
     * @return 集合长度
     */
    public Optional<Long> sRemove(String key, Object... values) {
        return Optional.ofNullable(redisTemplate.opsForSet().remove(key, values));
    }

    /**
     * 移除并返回集合的一个随机元素
     *
     * @param key 集合
     * @return 集合中随机一个元素
     */
    public Optional<Object> sPop(String key) {
        return Optional.ofNullable(redisTemplate.opsForSet().pop(key));
    }

    /**
     * 将元素 value 从一个集合移到另一个集合
     *
     * @param key     被移除的集合
     * @param value   要移除的元素
     * @param destKey 移动到的目标集合
     * @return 移动成功返回 true, 移动失败返回 false
     */
    public Optional<Boolean> sMove(String key, Object value, String destKey) {
        return Optional.ofNullable(redisTemplate.opsForSet().move(key, value, destKey));
    }

    /**
     * 获取集合的大小
     *
     * @param key 集合
     * @return 集合长度
     */
    public Optional<Long> sSize(String key) {
        return Optional.ofNullable(redisTemplate.opsForSet().size(key));
    }

    /**
     * 判断集合是否包含 value
     *
     * @param key   集合
     * @param value 元素
     * @return 包含返回 true, 不包含返回 false
     */
    public Optional<Boolean> sIsMember(String key, Object value) {
        return Optional.ofNullable(redisTemplate.opsForSet().isMember(key, value));
    }

    /**
     * 获取两个集合的交集
     *
     * @param key      集合1
     * @param otherKey 集合2
     * @return 两个集合的交集
     */
    public Optional<Set<Object>> sIntersect(String key, String otherKey) {
        return Optional.ofNullable(redisTemplate.opsForSet().intersect(key, otherKey));
    }

    /**
     * 获取 key 集合与多个集合的交集
     *
     * @param key       集合1
     * @param otherKeys 其余多个集合
     * @return 多个集合的交集
     */
    public Optional<Set<Object>> sIntersect(String key, Collection<String> otherKeys) {
        return Optional.ofNullable(redisTemplate.opsForSet().intersect(key, otherKeys));
    }

    /**
     * key 集合与 otherKey 集合的交集存储到 destKey 集合中
     *
     * @param key      集合1
     * @param otherKey 集合2
     * @param destKey  用于保存结果的集合
     * @return 新集合的长度
     */
    public Optional<Long> sIntersectAndStore(String key, String otherKey, String destKey) {
        return Optional.ofNullable(redisTemplate.opsForSet().intersectAndStore(key, otherKey,
                destKey));
    }

    /**
     * key 集合与多个集合的交集存储到 destKey 集合中
     *
     * @param key       集合1
     * @param otherKeys 其余多个集合
     * @param destKey   用于保存结果的集合
     * @return 新集合的长度
     */
    public Optional<Long> sIntersectAndStore(String key, Collection<String> otherKeys, String destKey) {
        return Optional.ofNullable(redisTemplate.opsForSet().intersectAndStore(key, otherKeys,
                destKey));
    }

    /**
     * 获取两个集合的并集
     *
     * @param key       集合1
     * @param otherKeys 集合2
     * @return 两个集合的并集
     */
    public Optional<Set<Object>> sUnion(String key, String otherKeys) {
        return Optional.ofNullable(redisTemplate.opsForSet().union(key, otherKeys));
    }

    /**
     * 获取 key 集合与多个集合的并集
     *
     * @param key       集合1
     * @param otherKeys 其余多个集合
     * @return 多个集合的并集
     */
    public Optional<Set<Object>> sUnion(String key, Collection<String> otherKeys) {
        return Optional.ofNullable(redisTemplate.opsForSet().union(key, otherKeys));
    }

    /**
     * key 集合与 otherKey 集合的并集存储到 destKey 中
     *
     * @param key      集合1
     * @param otherKey 集合2
     * @param destKey  用于保存结果的集合
     * @return 新集合的长度
     */
    public Optional<Long> sUnionAndStore(String key, String otherKey, String destKey) {
        return Optional.ofNullable(redisTemplate.opsForSet().unionAndStore(key, otherKey, destKey));
    }

    /**
     * key 集合与多个集合的并集存储到 destKey 中
     *
     * @param key       集合1
     * @param otherKeys 其余多个集合
     * @param destKey   用于保存结果的集合
     * @return 新集合的长度
     */
    public Optional<Long> sUnionAndStore(String key, Collection<String> otherKeys, String destKey) {
        return Optional.ofNullable(redisTemplate.opsForSet().unionAndStore(key, otherKeys, destKey));
    }

    /**
     * 获取两个集合的差集
     *
     * @param key      集合1
     * @param otherKey 集合2
     * @return 集合1 - 集合2 的差集
     */
    public Optional<Set<Object>> sDifference(String key, String otherKey) {
        return Optional.ofNullable(redisTemplate.opsForSet().difference(key, otherKey));
    }

    /**
     * 获取 key 集合与多个集合的差集
     *
     * @param key       集合1
     * @param otherKeys 其余多个集合
     * @return 集合1 - 集合2 - 集合3 - ... 集合n 的差集
     */
    public Optional<Set<Object>> sDifference(String key, Collection<String> otherKeys) {
        return Optional.ofNullable(redisTemplate.opsForSet().difference(key, otherKeys));
    }

    /**
     * key 集合与 otherKey 集合的差集存储到 destKey 中
     *
     * @param key      集合1
     * @param otherKey 集合2
     * @param destKey  用于保存结果的集合
     * @return 新集合的长度
     */
    public Optional<Long> sDifference(String key, String otherKey, String destKey) {
        return Optional.ofNullable(redisTemplate.opsForSet().differenceAndStore(key, otherKey,
                destKey));
    }

    /**
     * key 集合与多个集合的差集存储到 destKey 中
     *
     * @param key       集合1
     * @param otherKeys 其余多个集合
     * @param destKey   用于保存结果的集合
     * @return 新集合的长度
     */
    public Optional<Long> sDifference(String key, Collection<String> otherKeys, String destKey) {
        return Optional.ofNullable(redisTemplate.opsForSet().differenceAndStore(key, otherKeys,
                destKey));
    }

    /**
     * 获取集合所有元素
     *
     * @param key 集合
     * @return 集合中所有元素
     */
    public Optional<Set<Object>> setMembers(String key) {
        return Optional.ofNullable(redisTemplate.opsForSet().members(key));
    }

    /**
     * 随机获取集合中的一个元素
     *
     * @param key 集合
     * @return 集合中随机一个元素
     */
    public Optional<Object> sRandomMember(String key) {
        return Optional.of(redisTemplate.opsForSet().randomMember(key));
    }

    /**
     * 随机获取集合中 count 个元素
     *
     * @param key   集合
     * @param count 要获取的元素个数
     * @return count 个随机元素组成的集合
     */
    public Optional<List<Object>> sRandomMembers(String key, long count) {
        return Optional.ofNullable(redisTemplate.opsForSet().randomMembers(key, count));
    }

    /**
     * 随机获取集合中 count 个元素并且去除重复的
     *
     * @param key   集合
     * @param count 要获取的元素个数
     * @return count 个随机元素组成的集合, 并且不包含重复元素
     */
    public Optional<Set<Object>> sDistinctRandomMembers(String key, long count) {
        return Optional.ofNullable(redisTemplate.opsForSet().distinctRandomMembers(key, count));
    }

    /**
     * 迭代集合中的元素
     *
     * @param key     集合
     * @param options 迭代的限制条件, 为 ScanOptions.NONE 则无限制
     * @return 下一个元素的游标
     */
    public Optional<Cursor<Object>> sScan(String key, ScanOptions options) {
        return Optional.of(redisTemplate.opsForSet().scan(key, options));
    }

    // ------------------zSet 相关操作--------------------------------

    /**
     * 添加元素, 有序集合是按照元素的 score 值由小到大排列
     *
     * @param key   有序集合
     * @param value 元素
     * @param score 分数
     * @return 添加成功返回 true, 添加失败返回 false
     */
    public Optional<Boolean> zAdd(String key, Object value, double score) {
        return Optional.ofNullable(redisTemplate.opsForZSet().add(key, value, score));
    }

    /**
     * 添加多个元素到有序集合中
     *
     * @param key    有序集合
     * @param values 多个元素值
     * @return 有序集合长度
     */
    public Optional<Long> zAdd(String key, Set<TypedTuple<Object>> values) {
        return Optional.ofNullable(redisTemplate.opsForZSet().add(key, values));
    }

    /**
     * 移除有序集合中的值
     *
     * @param key    有序集合
     * @param values 要移除的值, 可以同时移除多个
     * @return 有序集合长度
     */
    public Optional<Long> zRemove(String key, Object... values) {
        return Optional.ofNullable(redisTemplate.opsForZSet().remove(key, values));
    }

    /**
     * 增加元素的 score 值，并返回增加后的值
     *
     * @param key   有序集合
     * @param value 要增加的元素
     * @param delta 增加的分数是多少
     * @return 增加后的分数
     */
    public Optional<Double> zIncrementScore(String key, Object value, double delta) {
        return Optional.ofNullable(redisTemplate.opsForZSet().incrementScore(key, value, delta));
    }

    /**
     * 返回元素在集合的排名,有序集合是按照元素的 score 值由小到大排列
     *
     * @param key   有序集合
     * @param value 值
     * @return 排名, 从小到大顺序, 0 表示第一位
     */
    public Optional<Long> zRank(String key, Object value) {
        return Optional.ofNullable(redisTemplate.opsForZSet().rank(key, value));
    }

    /**
     * 返回元素在集合的排名,按元素的 score 值由大到小排列
     *
     * @param key   有序集合
     * @param value 值
     * @return 排名, 从大到小顺序
     */
    public Optional<Long> zReverseRank(String key, Object value) {
        return Optional.ofNullable(redisTemplate.opsForZSet().reverseRank(key, value));
    }

    /**
     * 获取集合的元素, 从小到大排序
     *
     * @param key   有序集合
     * @param start 开始位置
     * @param end   结束位置, -1 表示从开始位置开始后面的所有元素
     * @return 指定区间的值的集合
     */
    public Optional<Set<Object>> zRange(String key, long start, long end) {
        return Optional.ofNullable(redisTemplate.opsForZSet().range(key, start, end));
    }

    /**
     * 获取集合元素, 并且把 score 值也获取
     *
     * @param key   有序集合
     * @param start 开始位置
     * @param end   结束位置, -1 表示从开始位置开始后面的所有元素
     * @return 指定区间的元素及分数的元组的集合
     */
    public Optional<Set<TypedTuple<Object>>> zRangeWithScores(String key, long start, long end) {
        return Optional.ofNullable(redisTemplate.opsForZSet().rangeWithScores(key, start, end));
    }

    /**
     * 根据 score 值查询集合元素
     *
     * @param key 有序集合
     * @param min 最小值
     * @param max 最大值
     * @return 分数 在最小值与最大值之间的元素集合
     */
    public Optional<Set<Object>> zRangeByScore(String key, double min, double max) {
        return Optional.ofNullable(redisTemplate.opsForZSet().rangeByScore(key, min, max));
    }

    /**
     * 根据 score 值查询集合元素及其分数, 并按分数从小到大排序
     *
     * @param key 有序集合
     * @param min 最小值
     * @param max 最大值
     * @return 分数 在最小值与最大值之间的元素与分数的元组的集合
     */
    public Optional<Set<TypedTuple<Object>>> zRangeByScoreWithScores(String key, double min, double max) {
        return Optional.ofNullable(redisTemplate.opsForZSet().rangeByScoreWithScores(key, min, max));
    }

    /**
     * 根据 score 值查询集合元素及其分数, 从小到大排序, 只获取 start 到 end 位置之间的结果
     *
     * @param key   有序集合
     * @param min   最低分数
     * @param max   最高分数
     * @param start 开始位置
     * @param end   结束位置
     * @return 分数在 min 与 max 之间, 位置在 start 与 end 之间的元素与分数的元组的集合
     */
    public Optional<Set<TypedTuple<Object>>> zRangeByScoreWithScores(String key, double min, double max, long start, long end) {
        return Optional.ofNullable(redisTemplate.opsForZSet().rangeByScoreWithScores(key, min, max, start, end));
    }

    /**
     * 获取集合的元素, 从大到小排序
     *
     * @param key   有序集合
     * @param start 开始位置
     * @param end   结束位置
     * @return 按照 分数 倒序的元素集合
     */
    public Optional<Set<Object>> zReverseRange(String key, long start, long end) {
        return Optional.ofNullable(redisTemplate.opsForZSet().reverseRange(key, start, end));
    }

    /**
     * 获取集合的元素, 从大到小排序, 并返回 score 值
     *
     * @param key   有序集合
     * @param start 开始位置
     * @param end   结束位置
     * @return 指定区间的元素及其分数的元组的集合
     */
    public Optional<Set<TypedTuple<Object>>> zReverseRangeWithScores(String key, long start, long end) {
        return Optional.ofNullable(redisTemplate.opsForZSet().reverseRangeWithScores(key, start, end));
    }

    /**
     * 根据 score 值查询集合元素, 从大到小排序
     *
     * @param key 有序集合
     * @param min 分数最小值
     * @param max 分数最大值
     * @return 分数在 min 与 max 之间的元素的集合, 按分数倒序
     */
    public Optional<Set<Object>> zReverseRangeByScore(String key, double min, double max) {
        return Optional.ofNullable(redisTemplate.opsForZSet().reverseRangeByScore(key, min, max));
    }

    /**
     * 根据 score 值查询集合元素, 从大到小排序
     *
     * @param key 有序集合
     * @param min 分数最小值
     * @param max 分数最大值
     * @return 分数在 min 与 max 之间的元素与分数的元组的集合, 按分数倒序
     */
    public Optional<Set<TypedTuple<Object>>> zReverseRangeByScoreWithScores(String key, double min, double max) {
        return Optional.ofNullable(redisTemplate.opsForZSet().reverseRangeByScoreWithScores(key, min, max));
    }

    /**
     * 根据 score 值查询集合元素及其分数, 从小到大排序, 只获取 start 到 end 位置之间的结果, 按分数从小到大排序
     *
     * @param key   有序集合
     * @param min   分数最小值
     * @param max   分数最大值
     * @param start 开始位置
     * @param end   结束位置
     * @return 分数在 min 与 max 之间, 位置在 start 与 end 之间的元素与分数的元组的集合, 按分数倒序
     */
    public Optional<Set<Object>> zReverseRangeByScore(String key, double min, double max, long start, long end) {
        return Optional.ofNullable(redisTemplate.opsForZSet().reverseRangeByScore(key, min, max, start, end));
    }

    /**
     * 根据 score 值获取集合元素数量
     *
     * @param key 有序集合
     * @param min 分数最小值
     * @param max 分数最大值
     * @return 分数在最小值与最大值之间的元素数量
     */
    public Optional<Long> zCount(String key, double min, double max) {
        return Optional.ofNullable(redisTemplate.opsForZSet().count(key, min, max));
    }

    /**
     * 获取集合大小
     *
     * @param key 有序集合
     * @return 集合中的元素数量
     */
    public Optional<Long> zSize(String key) {
        return Optional.ofNullable(redisTemplate.opsForZSet().size(key));
    }

    /**
     * 获取集合大小
     *
     * @param key 有序集合
     * @return 集合中的元素数量
     */
    public Optional<Long> zCard(String key) {
        return Optional.ofNullable(redisTemplate.opsForZSet().zCard(key));
    }

    /**
     * 获取集合中 value 元素的 score 值
     *
     * @param key   有序集合
     * @param value 元素值
     * @return 该元素值的分数
     */
    public Optional<Double> zScore(String key, Object value) {
        return Optional.ofNullable(redisTemplate.opsForZSet().score(key, value));
    }

    /**
     * 移除指定索引位置的成员
     *
     * @param key   有序集合
     * @param start 开始位置
     * @param end   结束位置
     * @return 移除的元素个数
     */
    public Optional<Long> zRemoveRange(String key, long start, long end) {
        return Optional.ofNullable(redisTemplate.opsForZSet().removeRange(key, start, end));
    }

    /**
     * 根据指定的 score 值的范围来移除成员
     *
     * @param key 有序集合
     * @param min 分数最小值
     * @param max 分数最大值
     * @return 移除的元素个数
     */
    public Optional<Long> zRemoveRangeByScore(String key, double min, double max) {
        return Optional.ofNullable(redisTemplate.opsForZSet().removeRangeByScore(key, min, max));
    }

    /**
     * 获取 key 和 otherKey 的并集并存储在 destKey 中
     *
     * @param key      集合1
     * @param otherKey 集合2
     * @param destKey  用于保存结果的集合
     * @return 新集合的长度
     */
    public Optional<Long> zUnionAndStore(String key, String otherKey, String destKey) {
        return Optional.ofNullable(redisTemplate.opsForZSet().unionAndStore(key, otherKey, destKey));
    }

    /**
     * 获取 key 和 otherKeys 的并集并存储在 destKey 中
     *
     * @param key       集合1
     * @param otherKeys 其余多个集合
     * @param destKey   用于保存结果的集合
     * @return 新集合的长度
     */
    public Optional<Long> zUnionAndStore(String key, Collection<String> otherKeys, String destKey) {
        return Optional.ofNullable(redisTemplate.opsForZSet().unionAndStore(key, otherKeys, destKey));
    }

    /**
     * 获取 key 和 otherKey 的交集并存储在 destKey 中
     *
     * @param key      集合1
     * @param otherKey 集合2
     * @param destKey  用于保存结果的集合
     * @return 新集合的长度
     */
    public Optional<Long> zIntersectAndStore(String key, String otherKey, String destKey) {
        return Optional.ofNullable(redisTemplate.opsForZSet().intersectAndStore(key, otherKey, destKey));
    }

    /**
     * 获取 key 和 otherKeys 的交集并存储在 destKey 中
     *
     * @param key       集合1
     * @param otherKeys 其余多个集合
     * @param destKey   用于保存结果的集合
     * @return 新集合的长度
     */
    public Optional<Long> zIntersectAndStore(String key, Collection<String> otherKeys, String destKey) {
        return Optional.ofNullable(redisTemplate.opsForZSet().intersectAndStore(key, otherKeys, destKey));
    }

    /**
     * 迭代有序集合
     *
     * @param key     有序集合
     * @param options 迭代限制条件, 为 ScanOptions.NONE 则无限制
     * @return 下一个元素及分数元组的游标
     */
    public Optional<Cursor<TypedTuple<Object>>> zScan(String key, ScanOptions options) {
        return Optional.of(redisTemplate.opsForZSet().scan(key, options));
    }

    /**
     * @param info 连接信息回调
     * @description: 获取Redis连接信息
     * @author: baijianmin
     * @date: 2023-04-27 15:20:39
     * @return: java.lang.Object
     * @version: 1.0
     */
    public Object execute(RedisCallback<Object> info) {
        return redisTemplate.execute(info);
    }

    /**
     * @param lockKey        分布式锁的Key
     * @param requestId      锁的值
     * @param acquireTimeout 尝试获取锁的超时时间(毫秒)
     * @param expireTime     锁的过期时间(毫秒)，防止死锁
     * @description: 可重入分布式加锁
     * @author: baijianmin
     * @date: 2022-07-23 09:53:09
     * @return: boolean
     * @version: 1.0
     */
    public boolean tryReentrantLock(String lockKey, String requestId, long acquireTimeout, long expireTime) {
        // 从本地线程中取出重入锁信息
        Map<String, Integer> value = threadLocal.get();
        // 如果已经取过锁了，表示已经重入，那么重入次数加一
        if (value.containsKey(lockKey)) {
            int count = value.get(lockKey) + 1;
            log.warn("已重入，当前计数: {}", count);
            value.put(lockKey, count);
            return true;
        }
        // 指定超时时间 = 当前时间 + 超时时间
        long end = System.currentTimeMillis() + acquireTimeout;
        // 只要没过期，就进行线程休眠并重试
        while (System.currentTimeMillis() < end) {
            // 加锁
            Boolean result = this.lock(lockKey, requestId, expireTime);
            // 判断加锁情况
            if (Boolean.TRUE.equals(result)) {
                // 成功加锁就往重入锁信息map中记录并放一个重入计数初始值一
                value.put(lockKey, 1);
                log.info("进行初次加锁，当前重入计数: {}", 1);
                return true;
            }
            try {
                //尝试获取锁失败，休眠100ms再试
                TimeUnit.MILLISECONDS.sleep(1000);
            } catch (InterruptedException e) {
                log.error("redis可重入分布式锁中断异常", e);
                break;
            }
        }
        // 直到指定的超时时间都没取到锁就返回false
        return false;
    }

    /**
     * @param lockKey   分布式锁的Key
     * @param requestId 锁的值
     * @description: 尝试释放分布式可重入锁
     * @author: baijianmin
     * @date: 2022-07-23 09:55:06
     * @return: boolean
     * @version: 1.0
     */
    public boolean tryReentrantUnlock(String lockKey, String requestId) {
        // 从本地线程中取出重入锁信息
        Map<String, Integer> value = threadLocal.get();
        // 判断重入取锁情况
        try {
            if (value.containsKey(lockKey)) {
                // 取到后就将重入次数减一
                int num = value.get(lockKey) - 1;
                // 如果这个锁已经减到了第一次取锁的时候，就开始执行解锁
                if (num <= 0) {
                    Long result = this.release(lockKey, requestId);
                    // 判断脚本执行结果
                    if (result != null && result.equals(1L)) {
                        log.info("已释放初次加锁，当前重入计数: {}", num);
                        // 值为1表示成功
                        value.remove(lockKey);
                        return true;
                    } else {
                        // 否则失败
                        return false;
                    }
                } else {
                    log.warn("已释放重入，当前计数: {}", num);
                    // 还不是第一次取锁，就放回去
                    value.put(lockKey, num);
                    return false;
                }
            } else {
                log.warn("未获取到分布式锁，不能释放锁");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("redis可重入锁取锁出错: {}", e.getMessage());
        }
        // 锁只有被解开才返回true，否则都是false
        return false;
    }

    /**
     * @param lockKey        分布式锁的Key
     * @param requestId      锁的值
     * @param acquireTimeout 尝试获取锁的超时时间(毫秒)，每次重新取锁失败都会按2倍初始值(50ms)递增延时重取
     * @param expireTime     锁的过期时间(毫秒)，防止死锁
     * @description: 尝试获取分布式不可重入锁
     * @author: baijianmin
     * @date: 2022-07-23 16:02:43
     * @return: boolean
     * @version: 1.0
     */
    public boolean tryLock(String lockKey, String requestId, long acquireTimeout, long expireTime) {
        // 从本地线程中取出重入锁信息
        Map<String, Integer> value = threadLocal.get();
        // 判断是否重复加锁
        if (value.containsKey(lockKey)) {
            log.warn("线程: {} 重复加锁, 已拒绝", Thread.currentThread().getId());
            // 如果重复加锁，则返回false
            return false;
        }
        try {
            // 尝试取锁
            Boolean lockResult = this.lock(lockKey, requestId, expireTime);
            // 判断取锁情况
            if (lockResult != null && lockResult) {
                // 成功取到锁，向本地线程放入lockKey，返回true
                value.put(lockKey, 1);
                return true;
            } else {
                // 取锁失败，线程休眠并递增延时，每次递增2倍时长，逐次尝试取锁，直至达到超时时间返回false
                // 线程休眠重取初始值
                long incrementTimeout = 50;
                // 重试计数
                int count = 1;
                // 长轮询尝试重新取锁
                while (incrementTimeout <= acquireTimeout) {
                    // 加锁
                    Boolean result = this.lock(lockKey, requestId, expireTime);
                    log.warn("线程: {}, 已尝试重新取锁: {} 次", Thread.currentThread().getId(), count);
                    // 判断加锁情况
                    if (Boolean.TRUE.equals(result)) {
                        log.info("线程: {}, 取锁成功", Thread.currentThread().getId());
                        return true;
                    }
                    // 取锁失败则递增休眠时间
                    incrementTimeout = incrementTimeout * 2;
                    try {
                        //尝试获取锁失败，递增休眠重取
                        TimeUnit.MILLISECONDS.sleep(incrementTimeout);
                    } catch (InterruptedException e) {
                        log.error("redis不可重入分布式锁中断异常", e);
                        break;
                    }
                    count++;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("redis不可重入锁取锁出错: {}", e.getMessage());
        }
        // 达到指定超时时间仍未取到锁就返回false
        return false;
    }

    /**
     * @param lockKey   分布式锁的Key
     * @param requestId 锁的值
     * @description:
     * @author: baijianmin
     * @date: 2022-07-23 16:23:21
     * @return: boolean
     * @version: 1.0
     */
    public boolean tryUnlock(String lockKey, String requestId) {
        Long result = 0L;
        try {
            // 解锁
            result = this.release(lockKey, requestId);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("redis不可重入锁解锁出错: {}", e.getMessage());
        } finally {
            // 因为解的是不可重入锁，为防止threadMap中存留有脏数据，不论解锁结果如何，都要将其从threadMap中移除
            Map<String, Integer> value = threadLocal.get();
            value.remove(lockKey);
        }
        return result > 0;
    }

    /**
     * @param lockKey    分布式锁的Key
     * @param requestId  锁的值
     * @param expireTime 锁的过期时间(毫秒)，防止死锁
     * @description: 加锁
     * @author: baijianmin
     * @date: 2022-07-23 15:58:45
     * @return: java.lang.Boolean
     * @version: 1.0
     */
    private Boolean lock(String lockKey, String requestId, long expireTime) {
        return redisTemplate.boundValueOps(lockKey).setIfAbsent(requestId, expireTime, TimeUnit.MILLISECONDS);
    }

    /**
     * @param lockKey   分布式锁的Key
     * @param requestId 锁的值
     * @description: lua脚本释放锁
     * @author: baijianmin
     * @date: 2022-07-23 15:56:45
     * @return: java.lang.Long
     * @version: 1.0
     */
    private Long release(String lockKey, String requestId) {
        // 使用lua脚本解锁
        String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
        // 定义设置lua脚本返回的数据类型
        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>();
        // 设置lua脚本返回类型为Long
        redisScript.setResultType(Long.class);
        redisScript.setScriptText(script);
        // 执行释放锁脚本并返回结果
        return redisTemplate.execute(redisScript, Collections.singletonList(lockKey), requestId);
    }

    /**
     * @param channel 频道
     * @param message 消息
     * @description: 向指定频道发布消息
     * @author: baijianmin
     * @date: 2022-08-18 16:18:06
     * @return: java.util.Optional<java.lang.Boolean>
     * @version: 1.0
     */
    public Optional<Boolean> convertAndSend(String channel, String message) {
        try {
            // 向指定频道发布消息
            redisTemplate.convertAndSend(channel, message);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("redis 消息发布出错: {}", e.getMessage());
            return Optional.empty();
        }
        return Optional.of(true);
    }

    /**
     * @description: 看门狗线程
     * @author: baijianmin
     * @dateTime: 2022-08-24 23:43:24
     */
    class WatchDogThread implements Runnable {
        private final ScheduledThreadPoolExecutor poolExecutor;
        private final String key;
        private final String value;
        private final Long ttl;

        public WatchDogThread(ScheduledThreadPoolExecutor poolExecutor, String key, String value, long ttl) {
            this.poolExecutor = poolExecutor;
            this.key = key;
            this.value = value;
            this.ttl = ttl;
        }

        @Override
        public void run() {
            try {
                // 执行锁续期操作 使用Lua脚本实现原子性
                if (renew(key, value, ttl) == 0) {
                    // 续期失败 可能是业务系统发生异常并且没有进行异常捕捉，没有进行释放锁操作
                    poolExecutor.shutdown();
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * @param key   分布式锁的Key
     * @param value 锁的值
     * @param ttl   锁过期时间
     * @description: 看门狗
     * @author: baijianmin
     * @date: 2022-08-25 09:26:11
     * @return:
     * @version: 1.0
     */
    private void watchDog(String key, String value, long ttl) {
        // 获取续期速率
        long rate = getRate(ttl);
        if (scheduledExecutorService.isShutdown()) {
            scheduledExecutorService = new ScheduledThreadPoolExecutor(1);
        }
        scheduledExecutorService.scheduleAtFixedRate(new WatchDogThread(scheduledExecutorService, key, value, ttl),
                1000, rate, TimeUnit.MILLISECONDS);
    }

    /**
     * @param ttl key过期时间
     * @description: 获取续期速率
     * @author: baijianmin
     * @date: 2022-08-25 09:24:42
     * @return: @return long
     * @version: 1.0
     */
    private long getRate(long ttl) {
        if (ttl - 5000 > 0) {
            return ttl - 5000;
        } else if (ttl - 1000 > 0) {
            return ttl - 1000;
        }
        throw new RuntimeException("ttl 不允许小于1000");
    }

    /**
     * @param lockKey   分布式锁的Key
     * @param requestId 请求id
     * @param ttl       锁过期时间
     * @description: 锁续期Lua脚本
     * @author: baijianmin
     * @date: 2022-08-25 09:25:14
     * @return: @return {@code Long }
     * @version: 1.0
     */
    public Long renew(String lockKey, String requestId, long ttl) {
        // 使用lua脚本锁续期
        String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('pexpire', KEYS[1], ARGV[2]) else return 0 end";
        // 定义设置lua脚本返回的数据类型
        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>();
        // 设置lua脚本返回类型为Long
        redisScript.setResultType(Long.class);
        redisScript.setScriptText(script);
        // 执行锁续期脚本并返回结果
        return redisTemplate.execute(redisScript, Collections.singletonList(lockKey), requestId, ttl);
    }

}