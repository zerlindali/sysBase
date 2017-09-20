package cn.bforce.business.web.interceptor;


import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import cn.bforce.business.web.beans.LoginTicket;
import cn.bforce.business.web.http.StaticResouse;
import cn.bforce.common.cache.authority.AuthorityUtil;
import cn.bforce.common.utils.network.RequestParam;
import cn.bforce.common.utils.web.ResponseObj;


/**
 * <p class="detail">
 * 功能：功能：登录日志记录拦截器及权限拦截 web / 命名接口路径拦截 
 * </p>
 * @ClassName: WebInterceptor 
 * @version V1.0  
 * @date 2017年9月15日 
 * @author yuandx
 * Copyright 2017 b-force.cn, Inc. All rights reserved
 */
@Component
public class SecurityInterceptor extends HandlerInterceptorAdapter
{

    static Logger logger = LogManager.getLogger(SecurityInterceptor.class);
    
    @Autowired
    AuthorityUtil authorityUtil;

    /**
     * 最先执行 : 在业务处理器处理请求之前被调用 如果返回false 从当前的拦截器往回执行所有拦截器的afterCompletion(),再退出拦截器链 如果返回true
     * 执行下一个拦截器,直到所有的拦截器都执行完毕 再执行被拦截的Controller 然后进入拦截器链, 从最后一个拦截器往回执行所有的postHandle()
     * 接着再从最后一个拦截器往回执行所有的afterCompletion()
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object handler)
        throws Exception
    {

        String ip = RequestParam.getRealAddress(request);// ip
        LoginTicket loginUser = StaticResouse.getUserSession(request);

        // 判断操作权限 : 拿当前url,比对配置中的url,如果存在>=0,则说明可以访问,
        String currUrl = request.getServletPath();

        logger.info("当前被拦截url:" + currUrl);
        
        boolean canLogin = true;
        
        if (loginUser == null)
        {
            //todo:不能登录的地址配置
            if ("/web/user/doLogin".equals(currUrl)
                || "/web/user/doLogin.do".equals(currUrl)
                || currUrl.endsWith("/error")
                || currUrl.contains("/web/notice/")
                || currUrl.endsWith("web/user/resetPassWord.do"))
            {
                canLogin = false;
            }
            else
            {
                // 如果请求中带token，拿到token到sso-server做校验
                ResponseObj obj = new ResponseObj();
                obj.setStatus(0);
                obj.setShowMessage("请先登录");
                response.setHeader("Content-type", "application/json;charset=UTF-8");
                response.getWriter().write(obj.getJsonStr());
                return false;
            }
        }

        if (canLogin)
        {
            Map<String, List> authMap = authorityUtil.getAuthMap();
            
            //Map<String, String> roles = loginUser.getRole();
            //int level = loginUser.getLevel();
            
            logger.info("currUrl======:" + currUrl);
        }

        // TODO 权限划分
        /**
         * if(UserLevel.SYSTEM.getCode()== lt.getLevel()){
         * if(isUseUrl(SysConfigInterface.SYS_INTERFACE_MAP
         * .get(UserLevel.SYSTEM.getDetail()),currUrl)==false){ ResponseObj obj = new
         * ResponseObj(); obj.setStatus(0); obj.setShowMessage("权限不足");
         * response.setHeader("Content-type", "application/json;charset=UTF-8");
         * response.getWriter().write(obj.getJsonStr()); return false; } }else
         * if(UserLevel.PARTNER.getCode()== lt.getLevel()){
         * if(isUseUrl(SysConfigInterface.SYS_INTERFACE_MAP
         * .get(UserLevel.PARTNER.getDetail()),currUrl)==false){ ResponseObj obj = new
         * ResponseObj(); obj.setStatus(0); obj.setShowMessage("权限不足");
         * response.setHeader("Content-type", "application/json;charset=UTF-8");
         * response.getWriter().write(obj.getJsonStr()); return false; } }else
         * if(UserLevel.ACCOUNT.getCode()== lt.getLevel()){
         * if(isUseUrl(SysConfigInterface.SYS_INTERFACE_MAP
         * .get(UserLevel.ACCOUNT.getDetail()),currUrl)==false){ ResponseObj obj = new
         * ResponseObj(); obj.setStatus(0); obj.setShowMessage("权限不足");
         * response.setHeader("Content-type", "application/json;charset=UTF-8");
         * response.getWriter().write(obj.getJsonStr()); return false; } }else
         * if(UserLevel.ADVERT.getCode()== lt.getLevel()){
         * if(isUseUrl(SysConfigInterface.SYS_INTERFACE_MAP
         * .get(UserLevel.ADVERT.getDetail()),currUrl)==false){ ResponseObj obj = new
         * ResponseObj(); obj.setStatus(0); obj.setShowMessage("权限不足");
         * response.setHeader("Content-type", "application/json;charset=UTF-8");
         * response.getWriter().write(obj.getJsonStr()); return false; } }
         **/

        return true;
    }

    /**
     * 第二步: 在业务处理器处理请求执行完成后,生成视图之前执行的动作 可在modelAndView中加入数据，比如当前时间
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response,
                           Object handler, ModelAndView modelAndView)
        throws Exception
    {
        if (logger.isDebugEnabled())
        {
            logger.debug("");
        }
    }

    /**
     * 第三步: 在DispatcherServlet完全处理完请求后被调用,可用于清理资源等 当有拦截器抛出异常时,会从当前拦截器往回执行所有的拦截器的afterCompletion()
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                                Object handler, Exception ex)
        throws Exception
    {
        if (logger.isDebugEnabled())
        {
            logger.debug("");
        }
        
        super.afterCompletion(request, response, handler, ex);
    }

    /**
     * <p class="detail"> 功能：如果存在,则返回ture,说明可访问 </p>
     * 
     * @author wuxw
     * @param urls
     *            接口数组
     * @param currUrl
     *            当前url
     * @return
     * @throws
     */
    public boolean isUseUrl(List tempList, String currUrl)
    {
        if (currUrl.indexOf(".do") > 0)
        {
            currUrl = currUrl.replaceAll(".do", "");
        }
        return tempList.contains(currUrl);
    }

}
