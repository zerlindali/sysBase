package sms;

import org.junit.Test;

import cn.bforce.business.user.UserSmsManager;

public class SmsTest
{
    @Test
    public void test()
    {
        UserSmsManager.sendPassWordSms(10, 8, "123456", "13508489402");
    }
}
