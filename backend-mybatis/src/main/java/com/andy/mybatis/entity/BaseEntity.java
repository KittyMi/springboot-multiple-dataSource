package com.andy.mybatis.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @author andy_lai
 * @version 1.0
 * @date 2019/11/19 16:10
 * 默认字段:
 * id: 主键
 * gmt_create：创建时间datetime
 * gmt_updated：更新时间datetime
 */
@Data
@Accessors(chain = true)
public class BaseEntity implements Serializable {
    /**
     * 主键，默认自增
     */
    private Long id;

    /**
     * Insert时生成，MyMetaObjectHandler自动填充
     */
    @TableField(value = "gmt_create", fill = FieldFill.INSERT)
    private Timestamp gmtCreate;
    /**
     * Insert, Update时生成，MyMetaObjectHandler自动填充
     */
    @TableField(value = "gmt_updated", fill = FieldFill.INSERT_UPDATE)
    private Timestamp gmtUpdated;
}
