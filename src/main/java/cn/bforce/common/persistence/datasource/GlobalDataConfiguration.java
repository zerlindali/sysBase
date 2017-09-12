package cn.bforce.common.persistence.datasource;


import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;


@Configuration
public class GlobalDataConfiguration
{
    protected  final Logger log = LogManager.getLogger(GlobalDataConfiguration.class);
    
    @Bean(name = "bfDataSource")
    @Qualifier("bfDataSource")
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource.bf")
    public DataSource primaryDataSource()
    {
        log.info("-------------------- bfDataSource init ---------------------");
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "bfscrmDataSource")
    @Qualifier("bfscrmDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.bfscrm")
    public DataSource secondaryDataSource()
    {
        log.info("-------------------- bfscrmDataSource init ---------------------");
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "bfJdbcTemplate")
    public JdbcTemplate bfJdbcTemplate(@Qualifier("bfDataSource") DataSource dataSource)
    {
        return new JdbcTemplate(dataSource);
    }

    @Bean(name = "bfscrmJdbcTemplate")
    public JdbcTemplate bfscrmscrmJdbcTemplate(@Qualifier("bfscrmDataSource") DataSource dataSource)
    {
        return new JdbcTemplate(dataSource);
    }
    
    /******配置事务管理********/
    
    @Bean
    public PlatformTransactionManager bfTransactionManager(@Qualifier("bfDataSource")DataSource prodDataSource) {
     return new DataSourceTransactionManager(prodDataSource);
    }
     
    @Bean
    public PlatformTransactionManager bfscrmTransactionManager(@Qualifier("bfscrmDataSource")DataSource sitDataSource) {
     return new DataSourceTransactionManager(sitDataSource);
    }

}
