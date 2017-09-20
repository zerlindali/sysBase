package cn.bforce.business.web.filter;

import javax.servlet.Filter;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
@Configuration
public class StarsFilterConfiguration
{
    /*FilterRegistrationBean 用来配置urlpattern 来确定哪些路径触发filter */
    @Bean
    public FilterRegistrationBean someFilterRegistration() {

        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(AuthFilter());
        registration.addUrlPatterns("/api/*");
        registration.setOrder(1);
        return registration;
    } 
    
    /*使用annotation tag来取代<bean></bean>*/    
    @Bean()
      public Filter AuthFilter() {
        return new SysFilter();
      }
}
