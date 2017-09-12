package cn.bforce.business.web.beans;


import java.util.Date;
import java.util.Map;

import cn.bforce.common.utils.string.StringUtil;

import com.google.gson.Gson;


/**
 * <p class="detail"> 功能：登录保存cookie po值 </p>
 * 
 * @ClassName: LoginTicket
 * @version V1.0
 * @date 2017-1-4
 * @author wuxw Copyright 2015 b-force.cn, Inc. All rights reserved
 */
public class LoginTicket
{

    private String userId;

    private String headImg;

    /**
     * 手机号码
     */
    private String userName;

    /**
     * 真实昵称
     */
    private String realName;

    private int level;

    private Date loginDate;

    /**
     * 所属商城Id
     */
    private String mallId;

    private Map<String, String> Menus;

    private Map<String, String> buttons;

    private Map<String, String> role;

    private String hideMobile;

    public Date getLoginDate()
    {
        return loginDate;
    }

    public void setLoginDate(Date loginDate)
    {
        this.loginDate = loginDate;
    }

    public Map<String, String> getMenus()
    {
        return Menus;
    }

    public void setMenus(Map<String, String> menus)
    {
        Menus = menus;
    }

    public Map<String, String> getButtons()
    {
        return buttons;
    }

    public void setButtons(Map<String, String> buttons)
    {
        this.buttons = buttons;
    }

    public Map<String, String> getRole()
    {
        return role;
    }

    public void setRole(Map<String, String> role)
    {
        this.role = role;
    }

    public final String getUserId()
    {
        return userId;
    }

    public final void setUserId(String userId)
    {
        this.userId = userId;
    }

    public final String getUserName()
    {
        return userName;
    }

    public final void setUserName(String userName)
    {
        this.userName = userName;
    }

    public final String getRealName()
    {
        return realName;
    }

    public final void setRealName(String realName)
    {
        this.realName = realName;
    }

    public final int getLevel()
    {
        return level;
    }

    public final void setLevel(int level)
    {
        this.level = level;
    }

    public String getHideMobile()
    {
        if (userName != null && userName.length() >= 8)
        {
            hideMobile = userName.substring(0, 3) + "****" + userName.substring(7);
        }
        else
        {
            hideMobile = userName;
        }
        return hideMobile;
    }

    @Override
    public String toString()
    {
        return "LoginTicket [userId=" + userId + ", headImg=" + headImg + ", userName=" + userName
               + ", realName=" + realName + ", level=" + level + ", loginDate=" + loginDate
               + ", mallId=" + mallId + ", Menus=" + Menus + ", buttons=" + buttons + ", role="
               + role + ", hideMobile=" + hideMobile + "]";
    }

    public String toGson()
    {
        Gson json = StringUtil.getGson();
        return json.toJson(this);
    }

    public final String getHeadImg()
    {
        return headImg;
    }

    public final void setHeadImg(String headImg)
    {
        this.headImg = headImg;
    }

    public final String getMallId()
    {
        return mallId;
    }

    public final void setMallId(String mallId)
    {
        this.mallId = mallId;
    }

}
