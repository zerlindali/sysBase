package cn.bforce.common;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages={"cn.bforce.common"})  
@EnableCaching
public class YuntuSysBaseApplication {

	public static void main(String[] args) {
		SpringApplication.run(YuntuSysBaseApplication.class, args);
	}
}
