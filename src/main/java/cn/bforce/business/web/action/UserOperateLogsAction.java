/*
 * 版权所有(C) 商塑（杭州）科技有限公司2017-2027 This software is the confidential and proprietary information of
 * ShangSu Corporation ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the license agreement you
 * entered into with Zhejiang Tsou
 */
package cn.bforce.business.web.action;


import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import cn.bforce.business.tools.IdUtil;
import cn.bforce.business.web.beans.LoginTicket;
import cn.bforce.business.web.beans.annotation.UserLogs;
import cn.bforce.business.web.http.StaticResouse;
import cn.bforce.business.web.service.UserLogsService;
import cn.bforce.common.utils.network.RequestParam;
import cn.bforce.common.utils.string.StringUtil;
import cn.bforce.common.utils.web.ResponseObj;

import com.google.gson.Gson;


/**
 * <p class="detail">
 * 功能：操作日志
 * </p>
 * @ClassName: UserOperateLogsAction 
 * @version V1.0  
 * @date 2017年9月15日 
 * @author yuandx
 * Copyright 2017 b-force.cn, Inc. All rights reserved
 */
@Aspect
@Component
public class UserOperateLogsAction
{
    static Logger logger = LogManager.getLogger(UserOperateLogsAction.class);

    @Autowired
    private UserLogsService userLogsService;

    @Pointcut("@annotation(cn.bforce.business.web.beans.annotation.UserLogs)")
    public void controllerAspect()
    {}

    @Around("controllerAspect()")
    public Object doController(ProceedingJoinPoint point)
    {
        String methodName = point.getSignature().getName();
        String className = point.getTarget().getClass().getSimpleName();
        Object result = null;
        Map<String, String> map = null;
        Long start = 0L, end = 0L, time = 0L;

        try
        {
            map = getControllerMethodDescription(point);
            start = System.currentTimeMillis();
            result = point.proceed();
            end = System.currentTimeMillis();
            time = end - start;
        }
        catch (Throwable e)
        {
            throw new RuntimeException(e);
        }

        ResponseObj responseObj = new Gson().fromJson(result.toString(), ResponseObj.class);

        // 成功处理
        if (responseObj.getStatus() == 1)
        {
            addOperateLog(map, start, responseObj);
        }

        if (logger.isDebugEnabled())
        {
            logger.debug("请求方法: " + className + "." + methodName + "()");
            logger.debug("执行时间: " + time + "ms");
        }

        return result;
    }

    /**
     * <p class="detail">
     * 功能：日志入库
     * </p>
     * @author yuandx
     * @param map
     * @param start
     * @param responseObj
     * @throws
     */
    private void addOperateLog(Map<String, String> map, Long start, ResponseObj responseObj)
    {
        Map dataMap = new HashMap<String, String>();
        String logId = IdUtil.generatePrimaryKey("LOG", 2);
        dataMap.put("log_id", logId);
        dataMap.put("user_id", map.get("userId"));
        dataMap.put("action", map.get("methods"));
        dataMap.put("relative_rec_id", responseObj.getDataId());
        dataMap.put("relative_rec_id2", responseObj.getDataId2());
        dataMap.put("user_header", map.get("agent"));
        dataMap.put("reg_time", new Date(start));
        dataMap.put("user_ip", map.get("ip"));

        // 添加日志详情
        dataMap.put("detail", responseObj.getLogDetail());
        userLogsService.addUserLogs(dataMap);
    }

    /**
     * <p class="detail">
     * 功能：获取controller信息
     * </p>
     * @author yuandx
     * @param joinPoint
     * @return
     * @throws Exception
     * @throws
     */
    public Map<String, String> getControllerMethodDescription(JoinPoint joinPoint)
        throws Exception
    {
        MethodSignature methodSignature = (MethodSignature)joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        UserLogs userLogs = method.getAnnotation(UserLogs.class);
        if (userLogs == null) return null;
        Map<String, String> map = new HashMap<String, String>();

        HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
        LoginTicket loginUser = (LoginTicket)request.getSession().getAttribute(
            StaticResouse.USER_CONTEXT);

        map.put("userId", loginUser == null ? null : loginUser.getUserId());
        map.put("module", userLogs.module());
        map.put("methods", userLogs.action());
        String des = userLogs.description();
        if (StringUtil.isNullOrEmpty(userLogs.description()))
        {
            map.put("description", "操作成功");
        }
        map.put("description", des);
        map.put("agent", request.getHeader("User-Agent"));
        map.put("ip", RequestParam.getRealAddressAndPort(request));

        return map;
    }
}