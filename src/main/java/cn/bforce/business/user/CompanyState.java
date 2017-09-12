package cn.bforce.business.user;

public enum CompanyState
{
    // 商户
    NEXTSTATE(0, "待审核"), ISSTATE(1, "通过"), UNSTATE(9, "未通过"),

    // 子账户
    OPEN(10, "开启"), CLOSE(11, "禁用");

    // 服务商状态 无

    private int code;

    private String detail;

    CompanyState(int code, String detail)
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
    public static CompanyState contains(String type)
    {
        for (CompanyState typeEnum : CompanyState.values())
        {
            if (typeEnum.name().equals(type))
            {
                return typeEnum;
            }
        }
        return null;
    }

    public static CompanyState contains(int code)
    {
        for (CompanyState typeEnum : CompanyState.values())
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
