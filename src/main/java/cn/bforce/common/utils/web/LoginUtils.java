package cn.bforce.common.utils.web;


import javax.servlet.http.HttpServletRequest;

import cn.bforce.business.web.beans.LoginTicket;
import cn.bforce.business.web.http.StaticResouse;


public class LoginUtils
{

    /**
     * <p class="detail"> 功能：获得用户登录凭证 </p>
     * 
     * @author tangy
     * @date 2015-8-11
     * @return
     */
    public static LoginTicket getLoginTicket(HttpServletRequest request)
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
    public static String getLoginUserId(HttpServletRequest request)
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
    public static Integer getLoginUserIdByInteger(HttpServletRequest request)
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
