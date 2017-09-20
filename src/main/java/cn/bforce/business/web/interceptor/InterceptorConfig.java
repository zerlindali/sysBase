package cn.bforce.business.web.interceptor;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;


@Configuration
public class InterceptorConfig extends WebMvcConfigurerAdapter
{
    /**
     * <p class="detail">
     * 功能：解决WebInterceptor拦截器中不能使用注解的问题
     * </p>
     * @author yuandx
     * @return
     * @throws
     */
    @Bean
    SecurityInterceptor webInterceptor()
    {
        return new SecurityInterceptor();
    }
    
    @Override
    public void addInterceptors(InterceptorRegistry registry)
    {
        registry.addInterceptor(webInterceptor()).addPathPatterns("/**");
        super.addInterceptors(registry);
    }
}
