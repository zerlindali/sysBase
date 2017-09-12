package cn.bforce.common;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@SpringBootApplication
@ComponentScan(basePackages = {"cn.bforce.common"})
@EnableCaching
@EnableTransactionManagement
public class YuntuSysBaseApplication
{

    public static void main(String[] args)
    {
        SpringApplication.run(YuntuSysBaseApplication.class, args);
    }
}
