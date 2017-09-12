package cn.bforce.business.user;


import cn.bforce.common.utils.config.PropUtil;
import cn.bforce.common.utils.network.HttpUtil;

import com.alibaba.fastjson.JSONObject;


public class UserSmsManager
{
    /**
     * <p class="detail"> 发送密码通知消息 功能：平台身份： 创建服务商：随机生成6位数字密码，直接发送短信到服务商手机号
     * 创建商户：随机生成6位数字密码，直接发送短信到商户手机号 审核商户-通过：随机生成6位数字密码，直接发送短信到商户手机号 服务商身份：
     * 创建子账号：随机生成6位数字密码，直接发送短信到子账号手机号 创建商户：随机生成6位数字密码，直接发送短信到商户手机号 审核商户-通过：随机生成6位数字密码，直接发送短信到商户手机号
     * 子账号身份: 创建商户：不发送短信 商户身份：无 </p>
     * 
     * @author wuxw
     * @param loginLevel
     * @param regUserLevel
     * @param phone
     * @return
     * @throws
     */
    public static boolean sendPassWordSms(int loginLevel, int regUserLevel, String passWord,
                                          String phone)
    {
        JSONObject json = new JSONObject();
        json.put("product", "云图");
        json.put("pass", passWord);

        boolean flag = false;
        // 管理员创建
        if (loginLevel == UserLevel.SYSTEM.getCode())
        {
            if (regUserLevel == UserLevel.PARTNER.getCode()
                || regUserLevel == UserLevel.ADVERT.getCode()
                || regUserLevel == UserLevel.ACCOUNT.getCode())
            {
                return sendSms(phone, "pass", json.toString());
            }
            // 服务商创建
        }
        else if (loginLevel == UserLevel.PARTNER.getCode())
        {
            if (regUserLevel == UserLevel.ADVERT.getCode()
                || regUserLevel == UserLevel.ACCOUNT.getCode())
            {
                return sendSms(phone, "pass", json.toString());
            }
        }

        return flag;
    }

    private static boolean sendSms(String mobile, String type, String params)
    {
        HttpUtil.post(PropUtil.loadValue("application.properties", "api.tools.sms"), "mobile="
                                                                                     + mobile
                                                                                     + "&type="
                                                                                     + type
                                                                                     + "&body="
                                                                                     + params);

        return true;
    }
}
