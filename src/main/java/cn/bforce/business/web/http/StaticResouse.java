package cn.bforce.business.web.http;


import java.io.UnsupportedEncodingException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cn.bforce.business.web.beans.LoginTicket;
import cn.bforce.common.utils.config.PropUtil;
import cn.bforce.common.utils.security.EncryptUtil;
import cn.bforce.common.utils.string.StringUtil;

import com.google.gson.Gson;

public class StaticResouse
{
    static Logger logger = LogManager.getLogger(StaticResouse.class);

    public static String USER_CONTEXT = "USER_CONTEXT";

    public static String USER_COOKIES = "USER_COOKIES";

    public static String LOGIN_TO_URL = "toUrl";

    public static final int Minute = 60;

    public static final int Day = 24 * 60 * 60;
    
    public static void setUserSession(HttpServletRequest request, LoginTicket lt)
    {
        request.getSession().setAttribute(USER_CONTEXT, lt);
    }

    public static void setUserCookies(HttpServletResponse response, LoginTicket lt, int second)
        throws UnsupportedEncodingException
    {
        String tempStr = lt.toGson();
        String deStr = EncryptUtil.encryptBASE64(tempStr);

        Cookie cookie = new Cookie(USER_COOKIES, deStr);
        cookie.setPath("/");
        cookie.setMaxAge(second);
        response.addCookie(cookie);

        cookie = new Cookie(USER_COOKIES, deStr);
        cookie.setPath("/");
        cookie.setDomain(PropUtil.loadValue("application.properties", "yuntu.cookie.domain.test"));
        cookie.setMaxAge(second);
        response.addCookie(cookie);

        cookie = new Cookie(USER_COOKIES, deStr);
        cookie.setPath("/");
        cookie.setDomain(PropUtil.loadValue("application.properties", "yuntu.cookie.domain"));
        cookie.setMaxAge(second);
        response.addCookie(cookie);

    }

    public static void cleanUserSession(HttpServletRequest request)
    {
        HttpSession session = request.getSession();
        session.removeAttribute(USER_CONTEXT);
        session.invalidate();
    }

    public static LoginTicket getUserSession(HttpServletRequest request)
    {
        LoginTicket loginTicket = (LoginTicket)request.getSession().getAttribute(
            StaticResouse.USER_CONTEXT);
        if (loginTicket == null)
        {
            loginTicket = getUserCookie(request);
            if (loginTicket != null) setUserSession(request, loginTicket);
        }
        return loginTicket;
    }

    public static void delUserCookies(HttpServletRequest request, HttpServletResponse response)
    {
        Cookie[] cookies = request.getCookies();
        if (null == cookies)
        {
            logger.debug("没有cookie==============");
        }
        else
        {
            for (Cookie cookie : cookies)
            {
                if (cookie.getName().equals(USER_COOKIES))
                {
                    cookie.setValue(null);
                    cookie.setMaxAge(0);// 立即销毁cookie
                    cookie.setPath("/");
                    // logger.debug("被删除的cookie名字为:"+cookie.getName());
                    response.addCookie(cookie);
                    break;
                }
            }
        }
    }

    public static LoginTicket getUserCookie(HttpServletRequest request)
    {
        LoginTicket loginTicket = null;
        Cookie cookies[] = request.getCookies();
        if (cookies != null)
        {
            // 读取本机共存在多少COOKIE
            for (int i = 0; i < cookies.length; i++ )
            {
                // logger.debug("所有cookiesName: "+cookies[i].getName());
                if (cookies[i].getName().equals(USER_COOKIES))
                {
                    loginTicket = new LoginTicket();
                    String gsonString = cookies[i].getValue();
                    String ltStr = EncryptUtil.decryptBASE64(gsonString);
                    Gson gson = StringUtil.getGson();
                    loginTicket = gson.fromJson(ltStr, LoginTicket.class);
                    break;
                }
            }
        }
        else
        {
            logger.debug("没有Cookie");
        }
        return loginTicket;
    }

    public static String getUserCookieBASE64Str(HttpServletRequest request)
    {
        Cookie cookies[] = request.getCookies();
        if (cookies != null)
        {
            // 读取本机共存在多少COOKIE
            for (int i = 0; i < cookies.length; i++ )
            {
                // logger.debug("所有cookiesName: "+cookies[i].getName());
                if (cookies[i].getName().equals(USER_COOKIES))
                {
                    String gsonString = cookies[i].getValue();
                    return gsonString;
                }
            }
        }
        else
        {
            logger.debug("没有Cookie");
        }
        return "";
    }

}
