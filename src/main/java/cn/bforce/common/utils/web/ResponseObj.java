package cn.bforce.common.utils.web;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import cn.bforce.common.utils.string.StringUtil;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.gson.Gson;


/**
 * <p class="detail"> 功能： 客户端接口数据返回对象类 </p>
 * 
 * @ClassName: ResponseObj
 * @version V1.0
 * @date 2015-3-19
 * @author tangy Copyright 2015 b-force.cn, Inc. All rights reserved
 */
public class ResponseObj
{

    /**
     * 请求成功 1
     */
    public static final int INFO_SUCCESS = 1;

    /**
     * 请求失败 0
     */
    public static final int ERROR_FAILED = 0;

    /**
     * 服务器内部错误 500
     */
    public static final int ERROR_SERVER_ERR = 500;

    /**
     * 无效请求 400
     */
    public static final int ERROR_INVALID = 400;

    /**
     * 未登录 401
     */
    public static final int ERROR_NOLOGIN = 401;

    /**
     * 无权限 403
     */
    public static final int ERROR_NOACCESS = 403;

    /**
     * 路径不存在 404
     */
    public static final int ERROR_NOACTION = 404;

    /**
     * 请求超时 408
     */
    public static final int ERROR_TIMEOUT = 408;

    /**
     * 执行结果状态 0操作失败，业务操作未成功 1成功 500服务器内部错误 400无效的请求 401未登录 403无权限执行此操作 404请求的页面不存在 408请求超时
     */
    private int status;

    /**
     * 前台显示的提示信息
     */
    private String showMessage = "";

    /**
     * 返回app端的的json数据字符串
     */
    private Object data;

    /**
     * 数据id，用来保存新增/修改记录的id
     */
    private Object dataId;

    public ResponseObj()
    {

    }

    public ResponseObj(int status, String showMessage)
    {
        this.status = status;
        this.showMessage = showMessage;
    }

    public ResponseObj(int status, String showMessage, Object data)
    {
        this.status = status;
        this.showMessage = showMessage;
        this.data = data;
    }

    /**
     * <p class="detail"> 功能：根据传入值: true or false,直接new 出ResponseObj提示对象 构造函数 </p> @author
     * wuxw @param flag @return @throws
     */
    public ResponseObj(Boolean flag)
    {
        if (flag)
        {
            this.status = ViewShowEnums.INFO_SUCCESS.getStatus();
            this.showMessage = ViewShowEnums.INFO_SUCCESS.getDetail();
        }
        else
        {
            this.status = ViewShowEnums.INFO_ERROR.getStatus();
            this.showMessage = ViewShowEnums.INFO_ERROR.getDetail();
        }
    }

    public ResponseObj(Object data)
    {
        if (data != null)
        {
            this.status = ViewShowEnums.INFO_SUCCESS.getStatus();
            this.showMessage = ViewShowEnums.INFO_SUCCESS.getDetail();
        }
        else
        {
            this.status = ViewShowEnums.INFO_ERROR.getStatus();
            this.showMessage = ViewShowEnums.INFO_ERROR.getDetail();
        }
        this.data = data;
    }

    public int getStatus()
    {
        return status;
    }

    public void setStatus(int status)
    {
        this.status = status;
        this.showMessage = getShowMessage(status);
    }

    public String getShowMessage()
    {
        if ("".equals(showMessage))
        {
            return getShowMessage(status);
        }
        else
        {
            return showMessage;
        }
    }

    /**
     * <p> 功能： 处理成功返回 </p>
     * 
     * @author wanDong
     * @Date 2017年4月28日
     * @param data
     * @return
     */
    public static ResponseObj success(String message)
    {
        return new ResponseObj(INFO_SUCCESS, message);
    }

    /**
     * @Description: 处理成功返回
     * @author wanDong
     * @param message
     * @param data
     * @return
     */
    public static ResponseObj success(String message, Object data)
    {
        return new ResponseObj(INFO_SUCCESS, message, data);
    }

    /**
     * @Description: 失败
     * @author wanDong
     * @param message
     * @return
     */
    public static ResponseObj fail(String message)
    {
        return new ResponseObj(ERROR_FAILED, message);
    }

    /**
     * <p class="detail"> 功能：根据异常状态值返回异常描述 </p>
     * 
     * @author tangy
     * @date 2015-1-8
     * @param status
     * @return
     */
    public String getShowMessage(int status)
    {
        String msg = "";
        switch (status)
        {
            case 0:
                msg = "服务器系统内部错误";
                break;
            case 1:
                msg = "操作成功";
                break;
            case 400:
                msg = "无效的请求";
                break;
            case 401:
                msg = "用户未登录";
                break;
            case 403:
                msg = "无权限执行此操作";
                break;
            case 404:
                msg = "请求的页面不存在";
                break;
            case 408:
                msg = "请求超时";
                break;
            default:
                msg = "请求响应异常";
                break;
        }
        return msg;
    }

    public ResponseObj setShowMessage(String showMessage)
    {
        this.showMessage = showMessage;
        return this;
    }

    public ResponseObj setSuccessShowMessage(String showMessage)
    {
        this.showMessage = showMessage;
        this.status = ViewShowEnums.INFO_SUCCESS.getStatus();
        return this;
    }

    public Object getData()
    {
        return data;
    }

    public void setData(Object data)
    {
        if (data != null)
        {
            this.status = ViewShowEnums.INFO_SUCCESS.getStatus();
            this.showMessage = ViewShowEnums.INFO_SUCCESS.getDetail();
        }
        else
        {
            this.status = ViewShowEnums.INFO_ERROR.getStatus();
            this.showMessage = ViewShowEnums.INFO_ERROR.getDetail();
        }
        this.data = data;
    }

    /**
     * <p class="detail"> 功能：获得转化后的json字符串 </p>
     * 
     * @author tangy
     * @date 2015-6-30
     * @return
     */
    @JsonIgnore
    public String getJsonStr()
    {
        Gson json = StringUtil.getGson();
        return json.toJson(this);
    }

    /**
     * <p class="detail"> 功能：根据已经转化成json字符串形式的字符串生成完整的json格式 </p>
     * 
     * @author tangy
     * @date 2015-6-30
     * @return
     */
    @JsonIgnore
    public String getJsonStr(String data)
    {
        String jsonStr = "{\"status\":" + getStatus() + ",\"showMessage\":\"" + getShowMessage()
                         + "\",\"data\":" + data + "}";
        return jsonStr;
    }

    /**
     * <p class="detail"> 功能：web端返回的json字符串 </p>
     * 
     * @author panwuhai
     * @date 2016年4月15日
     * @param request
     * @param response
     * @return
     */
    public String toWebJson(HttpServletRequest request, HttpServletResponse response)
    {

        // 如果客户端传入了callBack变量说明该请求是jsonp跨域请求，则将数据包装成jsonp所需格式返回
        String callBackFunName = request.getParameter("callback");
        setAccessContrlAllowOrigin(response);
        if (StringUtils.isNotBlank(callBackFunName))
        {
            return callBackFunName + "(" + StringUtil.getGson().toJson(this) + ")";
        }

        return StringUtil.getGson().toJson(this);
    }

    /**
     * <p class="detail"> 功能：设置浏览器允许跨域，*表示允许所有域名，可以指定允许接入的域名(逗号分隔) </p>
     * 
     * @author tangy
     * @date 2015年11月6日
     * @param response
     *            请求响应
     */
    private void setAccessContrlAllowOrigin(HttpServletResponse response)
    {
        /*
         * origin参数指定一个允许向该服务器提交请求的域名(eg:http://www.tsou.cn;多个域名使用逗号分隔)，*：允许任意域名跨域访问资源，
         * 生产库域名申请完成后需配置服务器明确的域名
         */
        response.setHeader("Access-Control-Allow-Origin", "*");
        /*
         * 指明资源可以被请求的方式有哪些(一个或者多个). 这个响应头信息在客户端发出预检请求的时候会被返回
         */
        response.setHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS,PUT,DELETE");
        /*
         * 设置浏览器允许访问的服务器的头信息的白名单
         */
        response.setHeader("Access-Control-Allow-Headers",
            "Origin,X-Requested-With,Content-Type,Authorization,Accept, No-Cache, If-Modified-Since,Last-Modified, Cache-Control, Expires, X-E4M-With");
        /*
         * 默认情况下，跨源请求不提供凭据(cookie、HTTP认证及客户端SSL证明等)。
         * 通过将withCredentials属性设置为true，可以指定某个请求应该发送凭据。如果服务器接收带凭据的请求，会用下面的HTTP头部来响应
         * 如果发送的是带凭据的请求，但服务器的相应中没有包含这个头部，那么浏览器就不会把相应交给JavaScript(于是，responseText中将是空字符串，status的值为0，
         * 而且会调用onerror()事件处理程序)
         */
        response.setHeader("Access-Control-Allow-Credentials", "true");
        /*
         * 一次预请求的结果的有效时间(秒)，在该时间内不再发送预请求
         */
        response.setHeader("Access-Control-Max-Age", "2592000");
    }

    /**
     * @return dataId
     */

    public Object getDataId()
    {
        return dataId;
    }

    /**
     * @param dataId
     */
    public void setDataId(Object dataId)
    {
        this.dataId = dataId;
    }

}
