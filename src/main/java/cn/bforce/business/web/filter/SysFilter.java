package cn.bforce.business.web.filter;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.bforce.common.utils.config.PropUtil;

@WebFilter(filterName="myFilter",urlPatterns="/*")
public class SysFilter implements Filter {

	@Override
	public void destroy() {

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;

		res.setDateHeader("Expires", 0); // Expires:过时期限值
		res.setHeader("Cache-Control", "no-cache"); // Cache-Control来控制页面的缓存与否,public:浏览器和缓存服务器都可以缓存页面信息；
		res.setHeader("Pragma", "no-cache"); // Pragma:设置页面是否缓存，为Pragma则缓存，no-cache则不缓存

		String Origin = httpRequest.getHeader("Origin");
		if (Origin != null
				&& (Origin.indexOf("bf.cn") >= 0 || Origin
						.indexOf("b-force.cn") >= 0)) {
			res.setHeader("Access-Control-Allow-Origin",
					httpRequest.getHeader("Origin"));
			res.setHeader("Access-Control-Allow-Credentials", "true");
		} else {
			res.setHeader("Access-Control-Allow-Origin", "*");
		}
		
		String uri = httpRequest.getRequestURI();
		if (allowAddr(httpRequest)
				&& uri.endsWith("web/customer/register.do")) {
			chain.doFilter(request, response);
			return;
		}
		chain.doFilter(request, response);
	}

	protected static final boolean allowAddr(HttpServletRequest req) {
		String[] ips = PropUtil.loadValue("application.properties", "allow.ip")
				.split(",");
		String ip = req.getRemoteAddr();
		for (String aip : ips) {
			if (ip.equals(aip)) {
				return true;
			}
		}
		return false;
	}


	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// filter中注入service
		// ServletContext context = arg0.getServletContext();
		// ApplicationContext ctx =
		// WebApplicationContextUtils.getWebApplicationContext(context);
		// sysUserService = (SysUserService) ctx.getBean("sysUserService");
	}

	

	protected static final void outJson(HttpServletRequest req, HttpServletResponse res, String body)
	{
		outPrint(req, res, "text/json;charset=utf-8", body);
	}
	
	protected static final void outXml(HttpServletRequest req, HttpServletResponse res, String body)
	{
		outPrint(req, res, "text/xml;charset=utf-8", body);
	}
	
	private static final void outPrint(HttpServletRequest req, HttpServletResponse res,String contentType, String body)
	{
		res.setContentType(contentType);
		//res.setCharacterEncoding("utf-8");
		try{
			PrintWriter out = res.getWriter();
			out.println( body );
			out.flush();
			out.close();
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
}
