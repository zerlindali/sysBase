/**
 * winchance Inc.
 * Copyright (c) 2010-2014 All Rights Reserved.
 */
package cn.bforce.common.utils.web;

/**
 * 非业务类型枚举
 * 不需要显示下拉菜单
 * 
 * @author 卡内齐 (kaneiqi@dajiaok.com)
 * @version $Id: ConditionStatus.java, v 0.1 2014-7-29 下午12:45:46 kaneiqi Exp $
 */
public enum ViewShowEnums {

    ERROR_FAILED(0, "系统内部错误"),PARM_BLANK(2, "指定参数为空"), INFO_SUCCESS(1, "操作成功"),INFO_ERROR(3, "操作失败");

    private int    status;

    private String detail;

    ViewShowEnums(int status, String detail) {
        this.status = status;
        this.detail = detail;
    }

    /**
     * 获得枚举
     * 
     * @param code
     * @return
     */
    public static ViewShowEnums getEnumByStatus(int status) {

        for (ViewShowEnums activitie : ViewShowEnums.values()) {
            if (status == activitie.getStatus()) {
                return activitie;
            }
        }
        return null;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    /**
     * @return the detail
     */
    public String getDetail() {
        return detail;
    }

    /**
     * @param detail
     *            the detail to set
     */
    public void setDetail(String detail) {
        this.detail = detail;
    }

}
