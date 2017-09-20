package cn.bforce.business.user;

public enum UserRole
{
    ADMININSTRATOR(1, "超级管理员"), OPERATE(2, "运营"), SERVICE(3, "客服");

    private int code;

    private String detail;

    UserRole(int code, String detail)
    {
        this.code = code;
        this.detail = detail;
    }

    public int getCode()
    {
        return code;
    }

    public void setCode(int code)
    {
        this.code = code;
    }

    public String getDetail()
    {
        return detail;
    }

    public void setDetail(String detail)
    {
        this.detail = detail;
    }
}
