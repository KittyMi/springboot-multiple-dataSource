package com.andy.mybatis.config.db;

/**
 * @author andy_lai
 * @version 1.0
 * @date 2019/11/19 16:20
 */
public enum DBTypeEnum {
    master("master"), slave("slave");
    private String value;

    DBTypeEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
