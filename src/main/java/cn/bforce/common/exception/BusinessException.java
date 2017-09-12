package cn.bforce.common.exception;

public class BusinessException extends RuntimeException
{
    /**
     * <p class="detail"> 功能：异常处理 </p>
     * 
     * @Fields serialVersionUID
     * @author yuandx
     * @date 2017年9月6日
     */
    private static final long serialVersionUID = -5046075988519241251L;

    public BusinessException()
    {
        super();
    }

    public BusinessException(String message)
    {
        super(message);
    }

    public BusinessException(int errorCode)
    {
        super(ErrorUtil.getErrorMsg(errorCode));
    }

    public BusinessException(String message, Exception ex)
    {
        super(message, ex);
    }

    public BusinessException(Exception ex)
    {
        super(ex.getMessage(), ex);
    }
}
