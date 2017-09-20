package sms;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import test.BaseTestService;
import cn.bforce.common.utils.sms.UserSmsManager;

public class SmsTest extends BaseTestService
{
    @Autowired
    UserSmsManager userSmsManager;
    
    @Test
    public void test()
    {
        userSmsManager.sendPassWordSms(10, 8, "123456", "13508489402");
    }
}
