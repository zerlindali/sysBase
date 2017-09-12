package cn.bforce.common.utils.web;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.alibaba.fastjson.JSONObject;

import cn.bforce.common.persistence.DataObject;
import cn.bforce.common.utils.ExStringUtils;

public class ExRequestUtils {	

	protected final static Logger log = LogManager.getLogger(ExRequestUtils.class);
		
	public static String decodeURL(Object value) {
        if (value == null) {
            throw new IllegalArgumentException("Null object parameter");
        }

        try {
            return URLDecoder.decode(value.toString(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
    
	public static boolean hasSession(HttpServletRequest request) {
        return (request.getSession(false) != null);
    }
    
	public static Cookie getCookie(HttpServletRequest request, String name) {
        Cookie cookies[] = request.getCookies();

        if (cookies == null || name == null || name.length() == 0) {
            return null;
        }

        //Otherwise, we have to do a linear scan for the cookie.
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(name)) {
                return cookie;
            }
        }

        return null;
    }
    
	public static String getCookieValue(HttpServletRequest request, String name) {
        Cookie cookie = getCookie(request, name);
        if (cookie != null) {
            return cookie.getValue();
        }
        return null;
    }
	
	public static String removeCookie(HttpServletRequest request, HttpServletResponse response, String name) {
        return removeCookie(request, response, name, "/");
    }
	
	public static String removeCookie(HttpServletRequest request, HttpServletResponse response, String name, String path) {
        Cookie cookie = getCookie(request, name);
        if (cookie != null) {
           cookie.setValue(null);
           cookie.setMaxAge(0);
           cookie.setPath(path);
        }
        setCookie(request, response, cookie);
        return null;
    }
	
	public static void setCookie(HttpServletRequest request, HttpServletResponse response, Cookie cookie) {
		if (cookie != null){
			cookie.setSecure(request.isSecure());
			response.addCookie(cookie);
		}
	}

	public static void setCookieValue(HttpServletRequest request,
			HttpServletResponse response, String name, String value,
			int maxAge, String path) {

		Cookie cookie = new Cookie(name, value);
		cookie.setMaxAge(maxAge);
		cookie.setPath(path);
		cookie.setSecure(request.isSecure());
		response.addCookie(cookie);
	}
	
	public static String getLoginToken(HttpServletRequest request){
		/*String name = GlobalConfig.getConfigValue("filter.cookie.loginToken");
		if (ExStringUtils.isBlank(name)){
			name = GlobalConfig.KEY_LOGIN_TOKEN;
		}
		String loginToken = ExRequestUtils.getCookieValue(request, name);		
		if (loginToken == null){
			loginToken = request.getParameter("token");
		}
		return loginToken;*/
	    return "";
	}	
	
	public static String getLoginId(HttpServletRequest request){
		
		/*String value = getCookieValue(request, GlobalConfig.KEY_LOGIN_ID);
		if (value != null){				
			return decodeURL(value);
		}
		
		value = getCookieValue(request, GlobalConfig.getConfigValue("filter.cookie.loginId"));
		if (value != null){				
			return decodeURL(value);
		}		
		
		value = getCookieValue(request, GlobalConfig.KEY_MEMBER_ID);
		if (value != null){				
			return decodeURL(value);
		}								
		value = getCookieValue(request, "loginId");
		if (value != null){				
			return decodeURL(value);
		}	
		
		String token = request.getParameter("token");
		if (token == null){
			token = request.getParameter("userToken");
		}
	
		if (token != null){
			*//**
			 * TODO 从缓存中获取usertoken的信息
			 *//*
			Map tokenMap = (Map)ExCacheUtils.getData("UserTokenCache", token);
			Map tokenMap = new HashMap();
			if (tokenMap != null){
				return (String)tokenMap.get("loginUserId");
			}else{
			
			}
		}*/
		
		return null;
	}
	
	/**
	 * 组装参数
	 * 
	 * @param request
	 * @return
	 * @throws IOException
	 */
	public static Map buildParametersMap(HttpServletRequest request) throws IOException {

		request.setCharacterEncoding("UTF-8");
		String contentType = request.getContentType();
		String method = request.getMethod();
		DataObject paramMap = new DataObject();
	
		if ("application/json".equalsIgnoreCase(contentType) && "Post".equalsIgnoreCase(method)) {
			// 处理JSON数据流方式提交的参数
			String queryParams = IOUtils.toString(request.getInputStream(), "UTF-8");
			if (queryParams == null || queryParams.length() == 0) {
				JSONObject jsonParams = new JSONObject();
				Enumeration<String> names = request.getParameterNames();
				while (names.hasMoreElements()) {
					String paramName = names.nextElement();
					if (!"v,time,nonce,token".contains(paramName)) {
						if (request.getParameter(paramName) != null) {
							jsonParams.put(paramName, request.getParameter(paramName));
						} else {
							jsonParams.put(paramName, ExStringUtils.concat(request.getParameterValues(paramName)));
						}
					}
				}
				queryParams = jsonParams.toJSONString();
			}

			Map tmpMap = ExStringUtils.parseJsonParameters(queryParams);
			if (tmpMap == null) {
				tmpMap = ExStringUtils.parseUrlParameters(queryParams);
			}
			paramMap.putAll(tmpMap);

		} else {
			// 处理以FORM方式提交参数
			Enumeration names = request.getParameterNames();
			while (names.hasMoreElements()) {
				String name = (String) names.nextElement();
				String value = request.getParameter(name);
				paramMap.put(name, value);
			}
		}

		// 获取当前登录用户ID TODO
		//paramMap.put("loginUserId", getLoginId(request));

		return paramMap;
	}
	
	/**
	 * 组装参数
	 * @param request
	 * @return
	 * @throws IOException
	 */
	public static Map buildMultipartContentMap(HttpServletRequest request) throws IOException{
		
		Map paramMap = new HashMap();
		
		if (ServletFileUpload.isMultipartContent(request)){
			
			FileItemFactory factory = new DiskFileItemFactory();
			ServletFileUpload servletFileUpload = new ServletFileUpload(factory);		
			servletFileUpload.setHeaderEncoding("UTF-8");
			
			try {
				
				FileItemIterator items = servletFileUpload.getItemIterator(request);
				while (items.hasNext()) {
					FileItemStream item = (FileItemStream) items.next();					
					String fieldName = item.getFieldName();
					byte[] content = toByteArray(item.openStream());
					if (item.isFormField()) {
						paramMap.put(fieldName, new String(content, "UTF-8"));
					} else {
						String fileName = item.getName();
						paramMap.put(fieldName + "-fileName", fileName);
						paramMap.put(fieldName + "-fileContent", content);
						paramMap.put("isMultipartRequest", true);
					}												
				}			
				Enumeration names = request.getParameterNames();
				while(names.hasMoreElements()){
					String name = (String)names.nextElement();
					paramMap.put(name, request.getParameter(name));
				}		
								
			} catch (FileUploadException e) {
				log.error(e);
			} catch (IOException e) {
				log.error(e);
			}			

	    }else{
	    	
	    }
		
		
		return paramMap;
	}
	
	public static String getUserIP(HttpServletRequest request){
		String remoteHost = request.getRemoteHost();
		return remoteHost;
	}
	
	public  static byte[] toByteArray(InputStream input) throws IOException {
		int BUFFER_SIZE = 4096;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] b = new byte[BUFFER_SIZE];
		int len = 0;
		while ((len = input.read(b, 0, BUFFER_SIZE)) != -1) {
			baos.write(b, 0, len);
		}
		baos.flush();

		byte[] bytes = baos.toByteArray();
		return bytes;
	}
	
	public static int getPageIndex(Map paramMap){
		int pageIndex = 1;
		if (paramMap.get("pageIndex") != null){
			pageIndex = ExStringUtils.parseInt((String)paramMap.get("pageIndex"),1);
		}else if (paramMap.get("page") != null){
			pageIndex = ExStringUtils.parseInt((String)paramMap.get("page"),1);
		}
		return pageIndex;
	}
	
	public static int getPageSize(Map paramMap){
		int pageSize = 100;
		if (paramMap.get("pageSize") != null){
			pageSize = ExStringUtils.parseInt((String)paramMap.get("pageSize"),20);
		}else if (paramMap.get("rows") != null){
			pageSize = ExStringUtils.parseInt((String)paramMap.get("rows"),20);
		}
		return pageSize;
	}
	
	public static Map getLoginUserData(String loginId){
		
		Map userData = null;
		
		/**
		 * TODO userdemo user的信息先从缓存中去
		 */
/*		String token = (String)ExCacheUtils.getData("UserTokenCache",  "Session-" + loginId);	
		Map tokenData = (Map)ExCacheUtils.getData("UserTokenCache",  token);	*/	
		String token = "";	
		Map tokenData = new HashMap();	
		if (tokenData != null){
			userData =(Map)tokenData.get("extraData");
		}
		if (userData == null){
			userData = new HashMap();
		}
		return userData;
	}
	
	// TODO
	/*public static Map getLoginUserData(HttpServletRequest request){
		return  getLoginUserData(getLoginId(request));
	}*/

}
