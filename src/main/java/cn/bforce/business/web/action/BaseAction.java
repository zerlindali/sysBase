package cn.bforce.business.web.action;


import javax.servlet.http.HttpServletRequest;

import cn.bforce.business.web.beans.LoginTicket;
import cn.bforce.business.web.http.StaticResouse;


/**
 * <p class="detail"> 功能：action基类，承载通用action方法 </p>
 * 
 * @ClassName: BaseAction
 * @version V1.0
 * @date 2015-5-28
 * @author tangy Copyright 2017 b-force.cn, Inc. All rights reserved
 */
public class BaseAction
{

    /**
     * <p class="detail"> 功能：获得用户登录凭证 </p>
     * 
     * @author tangy
     * @date 2015-8-11
     * @return
     */
    protected LoginTicket getLoginTicket(HttpServletRequest request)
    {
        return (LoginTicket)request.getSession().getAttribute(StaticResouse.USER_CONTEXT);
    }

    /**
     * <p class="detail"> 功能：获取当前登录用户Id </p>
     * 
     * @author wuxw
     * @param request
     * @return
     * @throws
     */
    protected String getLoginUserId(HttpServletRequest request)
    {
        LoginTicket loginUser = (LoginTicket)request.getSession().getAttribute(
            StaticResouse.USER_CONTEXT);
        return loginUser == null ? null : loginUser.getUserId();
    }

    /**
     * <p class="detail"> 功能：获取当前登录用户Id </p>
     * 
     * @author wuxw
     * @param request
     * @return
     * @throws
     */
    protected Integer getLoginUserIdByInteger(HttpServletRequest request)
    {
        LoginTicket loginUser = (LoginTicket)request.getSession().getAttribute(
            StaticResouse.USER_CONTEXT);
        if (loginUser == null)
        {
            return null;
        }

        String userIdStr = loginUser.getUserId().toString();

        return Integer.parseInt(userIdStr);
    }

}
