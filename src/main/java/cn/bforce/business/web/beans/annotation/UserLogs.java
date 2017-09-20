/*
 * 版权所有(C) 商塑（杭州）科技有限公司2017-2027
 * 
 *  
 * This software is the confidential and proprietary information of
 * ShangSu Corporation ("Confidential Information").  You
 * shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with Zhejiang Tsou
 */
package cn.bforce.business.web.beans.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p class="detail">
 * 功能：业务日志输出注解
 * </p>
 * @ClassName: UserLogs 
 * @version V1.0  
 * @date 2017年2月28日 
 * @author wanDong
 * Copyright 2016 b-force.cn, Inc. All rights reserved
 */
@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface UserLogs {
	
	String module()  default ""; 
	/**
	 * 操作事件名称
	 */
	String action()  default ""; 
	
    String description()  default ""; 
	
}
