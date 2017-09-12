package cn.bforce.common.utils.web;


import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

/**
 * <p class="detail">
 * 功能：默认不配置的情况下，读取/resource目录下messages开头的国际化资源文件
 * </p>
 * @ClassName: MessageHelper 
 * @version V1.0  
 * @date 2017年9月11日 
 * @author yuandx
 * Copyright 2017 b-force.cn, Inc. All rights reserved
 */
@Component
public class MessageHelper
{
    @Autowired
    private MessageSource messageSource;

    public String getMessage(String code, Object[] args, String defaultMessage, Locale locale)
    {
        String msg = messageSource.getMessage(code, args, defaultMessage, locale);
        return msg != null ? msg.trim() : msg;
    }

    public String getMessage(String code, Object[] args)
    {
        Locale locale = Locale.CHINA;
        String msg = messageSource.getMessage(code, args, null, locale);
        return msg != null ? msg.trim() : msg;
    }

    public String getMessage(String code)
    {
        Locale locale = Locale.CHINA;
        String msg = messageSource.getMessage(code, null, null, locale);
        return msg != null ? msg.trim() : msg;
    }
}
