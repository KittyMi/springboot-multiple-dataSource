package com.andy.mybatis.config;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import com.andy.mybatis.config.db.DBTypeEnum;
import com.andy.mybatis.config.db.DynamicDataSource;
import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PerformanceInterceptor;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.JdbcType;
import org.mybatis.spring.annotation.MapperScan;
import org.apache.ibatis.plugin.Interceptor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.*;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author andy_lai
 * @version 1.0
 * @date 2019/11/19 15:58
 */
@ConditionalOnMissingBean(
        name = "mybatisPlusConfig"
)
@EnableTransactionManagement
@Configuration
@MapperScan(basePackages = "com.andy.mybatis.**.mapper*")
@DependsOn( value = {"redisTemplate", "redisCacheTransfer"} )
public class DefaultMybatisPlusConfig {
    /**
     * 超过15秒 定义为慢SQL
     */
    private static Long MAXTIME=1000*15L;

    /**
     * @return 分页插件
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
       return new PaginationInterceptor();
    }

    @Bean(name = "master")
    @ConfigurationProperties(prefix = "spring.datasource.druid.master")
    public DataSource master() {
        return DruidDataSourceBuilder.create().build();
    }

    @Bean(name = "slave")
    @ConfigurationProperties(prefix = "spring.datasource.druid.slave")
    public DataSource slave() {
        return DruidDataSourceBuilder.create().build();
    }


    /**
     * 动态数据源配置
     *
     * @return
     */
    @Bean
    @Primary
    public DataSource multipleDataSource(@Qualifier("master") DataSource master,
                                         @Qualifier("slave") DataSource slave) {
        DynamicDataSource dynamicDataSource = new DynamicDataSource();
        Map<Object, Object> targetDataSources = new HashMap<>();
        targetDataSources.put(DBTypeEnum.master.getValue(), master);
        targetDataSources.put(DBTypeEnum.slave.getValue(), slave);
        //todo 可以增加多个
        dynamicDataSource.setTargetDataSources(targetDataSources);
        dynamicDataSource.setDefaultTargetDataSource(master);
        return dynamicDataSource;
    }

    @Bean("sqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory() throws Exception {
        MybatisSqlSessionFactoryBean sqlSessionFactory = new MybatisSqlSessionFactoryBean();
        sqlSessionFactory.setDataSource(multipleDataSource(master(), slave()));

        MybatisConfiguration configuration = new MybatisConfiguration();
        configuration.setJdbcTypeForNull(JdbcType.NULL);
        configuration.setMapUnderscoreToCamelCase(true);
        configuration.setCacheEnabled(false);
        sqlSessionFactory.setConfiguration(configuration);
        //添加分页功能
        sqlSessionFactory.setPlugins(new Interceptor[]{
                paginationInterceptor()
        });

        return sqlSessionFactory.getObject();
    }

    /**
     * 性能分析插件仅在开发环境中使用
     * @return 性能分析插件
     */
    @Bean
    @Profile({"dev", "default"})
    public PerformanceInterceptor performanceInterceptor() {
        return new PerformanceInterceptor().setMaxTime(MAXTIME);
    }
}
