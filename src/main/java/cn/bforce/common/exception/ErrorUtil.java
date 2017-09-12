package cn.bforce.common.exception;

import cn.bforce.common.utils.config.PropUtil;
import cn.bforce.common.utils.string.StringUtil;

public class ErrorUtil
{
    private static final String ERROR_FILE_NAME = "error_message.properties";
    
    public static String getErrorMsg(int errorCode)
    {
        String errorMsg = PropUtil.loadValue(ERROR_FILE_NAME, errorCode+"");
        
        if (!StringUtil.isNullOrEmpty(errorMsg))
        {
            return errorMsg; 
        }
        else
        {
            return PropUtil.loadValue(ERROR_FILE_NAME, ErrorCode.ERRORCODE_NOT_CONFIGED+"");
        }
    }
}
