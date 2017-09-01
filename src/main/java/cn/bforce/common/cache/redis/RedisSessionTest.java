package cn.bforce.common.cache.redis;


import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
public class RedisSessionTest implements EmbeddedServletContainerCustomizer
{
    Logger logger = LogManager.getLogger(RedisSessionTest.class);

    @RequestMapping("test/getsession")
    @ResponseBody
    String get(HttpServletRequest request)
    {
        Object o = request.getSession().getAttribute("springboot");  
        if(o == null){  
            o = "spring boot 牛逼了!!!由端口"+request.getLocalPort()+"生成";  
            request.getSession().setAttribute("springboot", o);  
        }  
          
        return "端口=" + request.getLocalPort() +  " sessionId=" + request.getSession().getId() +"<br/>"+o;  
    }

    @Override
    public void customize(ConfigurableEmbeddedServletContainer configurableEmbeddedServletContainer)
    {
        configurableEmbeddedServletContainer.setPort(8004);
    }
}
