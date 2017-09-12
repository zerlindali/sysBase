package cn.bforce.business.user;

/***
 * <p class="detail"> 功能：身份等级，10：系统，8：合作商，4：子账户，2：广告主 </p>
 * 
 * @ClassName: UserLevel
 * @version V1.0
 * @date 2017-1-4
 * @author wuxw Copyright 2015 b-force.cn, Inc. All rights reserved
 */
public enum UserLevel
{

    SYSTEM(10, "系统"), PARTNER(8, "合作商"), ACCOUNT(4, "子账户"), ADVERT(2, "广告主"), SHOPKEEPER(1,
        "店主,店长"), GUIDE(0, "导购(店员)");

    private int code;

    private String detail;

    UserLevel(int code, String detail)
    {
        this.code = code;
        this.detail = detail;
    }

    /**
     * <p class="detail"> 功能：快捷迭代验证是否code存在 </p>
     * 
     * @author damowang
     * @param type
     * @return
     * @throws
     */
    public static UserLevel contains(String type)
    {
        for (UserLevel typeEnum : UserLevel.values())
        {
            if (typeEnum.name().equals(type))
            {
                return typeEnum;
            }
        }
        return null;
    }

    /**
     * <p class="detail"> 功能：遍历查找枚举 </p>
     * 
     * @author wan.Dong
     * @date 2017年2月22日
     * @param code
     * @return
     */
    public static UserLevel contains(int code)
    {
        for (UserLevel typeEnum : UserLevel.values())
        {
            if (code == typeEnum.getCode())
            {
                return typeEnum;
            }
        }
        return null;
    }

    /**
     * @return code
     */

    public int getCode()
    {
        return code;
    }

    /**
     * @param code
     */
    public void setCode(int code)
    {
        this.code = code;
    }

    /**
     * @return detail
     */

    public String getDetail()
    {
        return detail;
    }

    /**
     * @param detail
     */
    public void setDetail(String detail)
    {
        this.detail = detail;
    }
}
