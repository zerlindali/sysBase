package cn.bforce.common.utils.sms;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.bforce.business.user.UserLevel;
import cn.bforce.common.cache.redis.RedisUtil;
import cn.bforce.common.utils.config.PropUtil;
import cn.bforce.common.utils.network.HttpUtil;
import cn.bforce.common.utils.string.StringUtil;

import com.alibaba.fastjson.JSONObject;


@Component
public class UserSmsManager
{
    @Autowired
    private RedisUtil redis;

    /**
     * 产品名称
     */
    static final String PRODUCT_NAME = "云图";

    /**
     * <p class="detail"> 发送密码通知消息 功能：平台身份： 创建服务商：随机生成6位数字密码，直接发送短信到服务商手机号
     * 创建商户：随机生成6位数字密码，直接发送短信到商户手机号 审核商户-通过：随机生成6位数字密码，直接发送短信到商户手机号 服务商身份：
     * 创建子账号：随机生成6位数字密码，直接发送短信到子账号手机号 创建商户：随机生成6位数字密码，直接发送短信到商户手机号 审核商户-通过：随机生成6位数字密码，直接发送短信到商户手机号
     * 子账号身份: 创建商户：不发送短信 商户身份：无 </p>
     * 
     * @author yuandx
     * @param loginLevel
     * @param regUserLevel
     * @param phone
     * @param passWord
     * @return
     * @throws
     */
    public boolean sendPassWordSms(int loginLevel, int regUserLevel, String passWord, String phone)
    {
        // 初始密码模板：{product,pass}
        JSONObject json = new JSONObject();
        json.put("product", PRODUCT_NAME);
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

    /**
     * <p class="detail"> 功能：发送短信 </p>
     * 
     * @author yuandx
     * @param mobile
     * @param type
     * @param params
     * @return
     * @throws
     */
    private boolean sendSms(String mobile, String type, String params)
    {
        HttpUtil.post(PropUtil.loadValue("application.properties", "api.tools.sms"), "mobile="
                                                                                     + mobile
                                                                                     + "&type="
                                                                                     + type
                                                                                     + "&body="
                                                                                     + params);

        return true;
    }

    /**
     * <p class="detail"> 功能：发送验证码 </p>
     * 
     * @author yuandx
     * @param phoneNumber
     * @return
     * @throws
     */
    public boolean sendVerifyCode(String phoneNumber)
    {
        // 验证短信模板:{product,code}
        JSONObject json = new JSONObject();
        String verifyCode = StringUtil.RandNum(5);
        json.put("product", PRODUCT_NAME);
        json.put("code", verifyCode);

        // 验证码三十分钟有效
        redis.setVal("verifycode_" + phoneNumber, verifyCode, redis.MINUTE * 30);

        return sendSms(phoneNumber, "verify", json.toString());
    }
}
