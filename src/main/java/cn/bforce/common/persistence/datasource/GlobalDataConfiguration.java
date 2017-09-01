package cn.bforce.common.persistence.datasource;


import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;


@Configuration
public class GlobalDataConfiguration
{
    @Bean(name = "bfDataSource")
    @Qualifier("bfDataSource")
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource.bf")
    public DataSource primaryDataSource()
    {
        System.out.println("-------------------- bfDataSource init ---------------------");
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "bfscrmDataSource")
    @Qualifier("bfscrmDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.bfscrm")
    public DataSource secondaryDataSource()
    {
        System.out.println("-------------------- bfscrmDataSource init ---------------------");
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

}
