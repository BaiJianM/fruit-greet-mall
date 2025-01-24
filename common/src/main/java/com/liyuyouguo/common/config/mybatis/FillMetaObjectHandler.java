package com.liyuyouguo.common.config.mybatis;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;

/**
 * 自动填充数据库字段
 *
 * @author baijianmin
 */
@Slf4j
public class FillMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
//        // 获取当前用户信息
//        UserInfo userInfo = UserInfoUtils.getUserInfo();
//        Long userId = userInfo.getUserId() == null ? 0L : userInfo.getUserId();
//        String realName = userInfo.getRealName() == null ? "system" : userInfo.getRealName();
//        // 对新增数据时需要填充的字段进行赋值
//        this.strictInsertFill(metaObject, "createUserId", Long.class, userId);
//        this.strictInsertFill(metaObject, "createUserName", String.class, realName);
//        this.strictInsertFill(metaObject, "createTime", LocalDateTime.class, LocalDateTime.now());
//        this.strictInsertFill(metaObject, "updateUserId", Long.class, userId);
//        this.strictInsertFill(metaObject, "updateUserName", String.class, realName);
//        this.strictInsertFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());
    }

    @Override
    public void updateFill(MetaObject metaObject) {
//        // 获取当前用户信息
//        UserInfo userInfo = UserInfoUtils.getUserInfo();
//        Long userId = userInfo.getUserId() == null ? 0L : userInfo.getUserId();
//        String realName = userInfo.getRealName() == null ? "system" : userInfo.getRealName();
//        // 对修改数据时需要填充的字段进行赋值
//        this.strictInsertFill(metaObject, "updateUserId", Long.class, userId);
//        this.strictInsertFill(metaObject, "updateUserName", String.class, realName);
//        this.strictInsertFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());
    }
}