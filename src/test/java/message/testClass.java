package message;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import test.BaseTestService;
import cn.bforce.common.utils.web.MessageHelper;


public class testClass extends BaseTestService
{
    static final Logger logger = LogManager.getLogger(testClass.class);

    @Autowired
    private MessageHelper msgHelper;

    @Test
    public void test()
    {
        String msg1 = msgHelper.getMessage("login.relogin");
        String msg2 = msgHelper.getMessage("input.length.lessthan", new String[] {"姓名", "14"});
        logger.debug(msg1);
        logger.debug(msg2);
    }

}
