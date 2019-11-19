package com.andy.mybatis.config.db;

/**
 * @author andy_lai
 * @version 1.0
 * @date 2019/11/19 16:23
 * @desc 动态数据源决策
 */
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class DynamicDataSource extends AbstractRoutingDataSource {

    @Override
    protected Object determineCurrentLookupKey() {
        return  DbContextHolder.getDbType();
    }
}
