package cn.bforce.business.tools;


import java.util.Random;


public class IdUtil
{

    private static final Random _Rand = new Random();

    private static final char[] _number = ("0123456789").toCharArray();

    // 表的主键前缀（考虑是否可以做成一个枚举类型）
    public static final String WXMSG_PREFIX = "WXMSG"; // Chat_Message

    public static final String ADV_PREFIX = "ADV"; // 广告

    public static final String ML_PREFIX = "ML"; // 金额变动历史记录

    public static final String MSG_PREFIX = "MSG"; // 消息

    public static final String RCH_PREFIX = "RCH"; // 充值转账订单

    public static final String SHOP_PREFIX = "SHOP"; // 店铺

    public static final String MALL_PREFIX = "MALL"; // MALL

    public static final String SCARD_PREFIX = "SCARD"; // 内置卡券

    public static final String EXP_PREFIX = "EXP"; // 运费模板

    public static final String GROUP_PREFIX = "GROUP"; // 商品分组

    public static final String TAG_PREFIX = "TAG"; // 标签

    public static final String NID_PREFIX = "NID"; // 公告

    public static final String SGOODS_PREFIX = "SGOODS"; // 内置商品

    public static final String SSHELF_PREFIX = "SSHELF"; // 内置货架

    public static final String SORDER_PREFIX = "SORDER"; // 内置订单

    public static final String UID_PREFIX = "UID"; // 用户ID

    public static final String CH_PREFIX = "CH"; // 渠道ID

    // 生成id的随机数位数
    public static final Integer DIGIT_0 = 0;

    public static final Integer DIGIT_1 = 1;

    public static final Integer DIGIT_2 = 2;

    public static final Integer DIGIT_3 = 3;

    public static final String generatePrimaryKey(String tablePrefix, Integer digit)
    {
        long time = System.currentTimeMillis();
        return tablePrefix + time + RandNum(digit);
    }

    /**
     * 获取指定位数数字
     * 
     * @author kezhiqiang
     * @date 2017-2-10
     * @param length
     * @return
     */
    public static final String RandNum(int length)
    {
        String rand = "";
        for (int i = 0, s = _number.length; i < length; i++ )
        {
            rand = rand + _number[_Rand.nextInt(s)];
        }
        return rand;
    }

    /**
     * 生成user_id
     * 
     * @author kezhiqiang
     * @date 2017-2-10
     * @return
     */
    public static final String getUid(int levelCode)
    {
        // UID + 00 + 13 = 18位
        long time = System.currentTimeMillis();
        return "UID" + (levelCode < 10 ? "0" : "") + levelCode + time;
    }
}
