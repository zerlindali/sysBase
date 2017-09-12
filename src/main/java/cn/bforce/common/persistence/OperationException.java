package cn.bforce.common.persistence;


import java.lang.reflect.InvocationTargetException;
import java.text.MessageFormat;
import java.util.ResourceBundle;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * 接口服务异常
 * 
 * @author LTJ
 */
public class OperationException extends RuntimeException
{

    protected final Logger log = LogManager.getLogger(OperationException.class);

    private String baseName = "error_message";

    /**
	 * 
	 */
    private static final long serialVersionUID = -1683194329762320395L;

    private String errorCode;

    private Object[] args;

    public OperationException(String message)
    {
        super(message);
    }

    public OperationException(String message, String errorCode)
    {
        super(message);
        this.errorCode = errorCode;
    }

    public OperationException(String message, String... strings)
    {
        super(message);
        this.args = strings;
    }

    public OperationException(String message, Object[] args)
    {
        super(message);
        this.args = args;
    }

    public OperationException(String message, Exception ex)
    {
        super(message, ex);
    }

    public OperationException(Exception ex)
    {
        super(ex.getMessage(), ex);
    }

    public OperationException(InvocationTargetException ex)
    {
        super(ex.getTargetException());
    }

    public String getErrorCode()
    {
        return errorCode;
    }

    public void setErrorCode(String errorCode)
    {
        this.errorCode = errorCode;
    }

    @Override
    public String getMessage()
    {
        String msg = super.getMessage();

        if (msg != null)
        {

            try
            {
                String tmp = msg.replaceAll(" ", "_");

                msg = getMessage(baseName, tmp);

                MessageFormat messageFormat = new MessageFormat(msg);

                msg = messageFormat.format(this.args);

                if (msg != null)
                {
                    if (msg.startsWith("Field_") && msg.contains("_doesnt_have_a_default_value_"))
                    {
                        msg = getMessage(baseName, "field_value_can_not_be_not_null");
                    }
                }
            }
            catch (Exception ex)
            {
                log.error(ex.getMessage(), ex);
            }
        }

        return msg;
    }

    public Object[] getArgs()
    {
        return args;
    }

    public void setArgs(Object[] args)
    {
        this.args = args;
    }

    public String getMessage(String baseName, String key)
    {

        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        ResourceBundle errorMessageBundle = ResourceBundle.getBundle(baseName);

        if (key != null && errorMessageBundle.containsKey(key))
        {
            return errorMessageBundle.getString(key);
        }

        return key;
    }

}
