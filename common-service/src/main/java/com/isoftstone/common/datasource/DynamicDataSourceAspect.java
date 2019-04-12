package com.isoftstone.common.datasource;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;


@Component
public class DynamicDataSourceAspect {
    /** * Switch DataSource * * @param point * @param targetDataSource */
    @Before("@annotation(targetDataSource))")
    public void switchDataSource(JoinPoint point, TargetDataSource targetDataSource) {
        if (!DynamicDataSourceContextHolder.containDataSourceKey(targetDataSource.value())) {
            /*DriverManagerDataSource dataSources = new DriverManagerDataSource();
            dataSources.setDriverClassName("com.mysql.cj.jdbc.Driver");
            dataSources.setUrl("jdbc:mysql://114.116.14.172:3306/mysql?useUnicode=true&useSSL=false&characterEncoding=utf8&autoReconnect=true&rewriteBatchedStatements=TRUE");
            dataSources.setUsername("root");
            dataSources.setPassword("123456");
            Map<Object, Object> dataSourceMap = new HashMap<>();
            dataSourceMap.put(targetDataSource.value(), dataSources);
            dynamicDataSource.setTargetDataSources(dataSourceMap);
            dynamicDataSource.afterPropertiesSet();
            DynamicDataSourceContextHolder.dataSourceKeys.clear();
            DynamicDataSourceContextHolder.dataSourceKeys.addAll(dynamicDataSource.getTargetDataSources().keySet());
            DynamicDataSourceContextHolder.setDataSourceKey(targetDataSource.value());*/
        } else {
            // 切换数据源
            DynamicDataSourceContextHolder.setDataSourceKey(targetDataSource.value());
            System.out.println("Switch DataSource to [{}] in Method [{}] " +
                    DynamicDataSourceContextHolder.getDataSourceKey() + point.getSignature());
        }
    }

    /** * Restore DataSource * * @param point * @param targetDataSource */
    @After("@annotation(targetDataSource))")
    public void restoreDataSource(JoinPoint point, TargetDataSource targetDataSource) {
        // 将数据源置为默认数据源
        DynamicDataSourceContextHolder.clearDataSourceKey();
        System.out.println("Restore DataSource to [{}] in Method [{}] " +
                DynamicDataSourceContextHolder.getDataSourceKey() + point.getSignature());
    }
}
