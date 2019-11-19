package com.andy.mybatis.config.db;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @author andy_lai
 * @version 1.0
 * @date 2019/11/19 16:30
 * @desc AOP实现的数据源切换 @Order设置的足够小是为了让他先执行
 */
@Component
@Order(value = -520)
@Slf4j
@Aspect
public class DataSourceSwitchAspect {
    @Pointcut("execution(* com.andy.mybatis.mapper.master..*.*(..))")
    private void masterAspect() {
    }

    @Pointcut("execution(* com.andy.mybatis.mapper.slave..*.*(..))")
    private void slaveAspect() {
    }



    @Before("masterAspect()")
    public void masterDb() {
        log.info("切换到master 数据源...");
        DbContextHolder.setDbType(DBTypeEnum.master);
    }

    @Before("slaveAspect()")
    public void slaveDb() {
        log.info("切换到slave 数据源...");
        DbContextHolder.setDbType(DBTypeEnum.slave);
    }

}
