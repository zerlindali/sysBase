package cn.bforce.common.exception;

/**
 * <p class="detail">
 * 10开头为通用错误码
 * </p>
 * @ClassName: ErrorCode 
 * @version V1.0  
 * @date 2017年9月6日 
 * @author yuandx
 * Copyright 2017 b-force.cn, Inc. All rights reserved
 */
public class ErrorCode
{
    //错误码未配置
    public static int ERRORCODE_NOT_CONFIGED = 10000000;
    //参数为空！
    public static int ERRORCODE_PARAMETER_EMPTY = 10000001;
    //格式不正确！
    public static int ERRORCODE_PARAMETER_FORMAT_ERROR = 10000002;
}
