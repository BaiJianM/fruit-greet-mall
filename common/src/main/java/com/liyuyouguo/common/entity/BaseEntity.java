package com.liyuyouguo.common.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 所有实体类的基类
 *
 * @author baijianmin
 */
@Data
@Schema(description = "通用参数")
public class BaseEntity {

    /**
     * 主键
     */
    @Schema(description = "主键", example = "1")
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 是否删除（逻辑），1，是；0，否。默认0（false）
     */
    @Schema(description = "是否删除（逻辑），1，是；0，否。默认0（false）", example = "0")
    @TableLogic
    private Boolean isDelete;

    /**
     * 创建人id
     */
    @Schema(description = "创建人id", example = "1")
    @TableField(fill = FieldFill.INSERT)
    private Long createUserId;

    /**
     * 创建人姓名
     */
    @Schema(description = "创建人姓名", example = "姓名")
    @TableField(fill = FieldFill.INSERT)
    private String createUserName;

    /**
     * 创建时间
     */
    @Schema(description = "创建时间", example = "2023-12-15 12:00:00")
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    /**
     * 修改人id
     */
    @Schema(description = "修改人id", example = "1")
    @TableField(fill = FieldFill.UPDATE)
    private Long updateUserId;

    /**
     * 修改人姓名
     */
    @Schema(description = "修改人姓名", example = "姓名")
    @TableField(fill = FieldFill.UPDATE)
    private String updateUserName;

    /**
     * 修改时间
     */
    @Schema(description = "修改时间", example = "2023-12-15 12:00:00")
    @TableField(fill = FieldFill.UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    /**
     * 乐观锁实现
     */
    @Version
    @JsonIgnore
    private Long version;
}
