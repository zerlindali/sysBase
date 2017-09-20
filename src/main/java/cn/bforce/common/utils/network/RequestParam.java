package cn.bforce.common.utils.network;


import javax.servlet.http.HttpServletRequest;


/**
 * <p class="detail"> 功能：request请求的参数获取 </p>
 * 
 * @ClassName: RequestParam
 * @version V1.0
 * @date 2014-9-28
 * @author tangy
 */
public class RequestParam
{

    /**
     * <p class="detail"> 功能：客户端真实IP获取 在很多应用下都可能有需要将用户的真实IP记录下来，这时就要获得用户的真实IP地址，在JSP里，获取客户端的IP地
     * 址的方法是：request.getRemoteAddr()，这种方法在大部分情况下都是有效的。但是在通过了Apache,Squid等 反向代理软件就不能获取到客户端的真实IP地址了。
     * 但是在转发请求的HTTP头信息中，增加了X－FORWARDED－FOR信息。用以跟踪原有的客户端IP地址和原来客户端请求的服务器地址。 </p>
     * 
     * @author tangy
     * @date 2014-9-28
     * @param request
     * @return
     */
    public static String getRealAddress(HttpServletRequest request)
    {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
        {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
        {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip))
        {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    /**
     * <p class="detail">
     * 功能：返回IP加端口
     * </p>
     * @author yuandx
     * @param request
     * @return
     * @throws
     */
    public static String getRealAddressAndPort(HttpServletRequest request)
    {
        return getRealAddress(request) + ":" + request.getRemotePort();
    }
}
