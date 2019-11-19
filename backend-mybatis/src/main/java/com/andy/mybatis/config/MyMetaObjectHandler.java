package com.andy.mybatis.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;

import java.sql.Timestamp;

/**
 * @author andy_lai
 * @version 1.0
 * @date 2019/11/19 16:01
 * @desc 自定义字段填充，gmt_updated和gmt_create自动填充
 */
public class MyMetaObjectHandler implements MetaObjectHandler {
    /**
     * 插入元对象字段填充（用于插入时对公共字段的填充）
     *
     * @param metaObject 元对象
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        this.setInsertFieldValByName("gmtCreate", ts, metaObject);
        this.setInsertFieldValByName("gmtUpdated", ts, metaObject);
    }

    /**
     * 更新元对象字段填充（用于更新时对公共字段的填充）
     *
     * @param metaObject 元对象
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        this.setUpdateFieldValByName("gmtUpdated", ts, metaObject);
    }
}
