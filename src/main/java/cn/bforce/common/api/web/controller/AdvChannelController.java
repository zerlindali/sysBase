package cn.bforce.common.api.web.controller;


import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.bforce.common.api.web.repository.AdvChannelRepository;
import cn.bforce.common.utils.web.ExControllerUtils;


/**
 * <p class="detail"> 功能：对Controller的使用进行测试 </p>
 * 
 * @ClassName: ControllerTest
 * @version V1.0
 * @date 2017年8月30日
 * @author Zerlinda Copyright 2015 tsou.com, Inc. All rights reserved
 */
@RestController
@RequestMapping("/api/test")
public class AdvChannelController
{

    static final Logger logger = LogManager.getLogger(AdvChannelController.class);

    @Autowired
    private AdvChannelRepository advChannelRepository;

    /*
     * // 不指定数据源
     * @RequestMapping(value = "/getBaseList", produces = "application/json") public Object
     * getBaseList(HttpServletRequest request) throws IOException { Map<String, Object> alarmInfo =
     * new HashMap(); Map paramMap =
     * cn.bforce.common.utils.web.ExControllerUtils.buildParametersMap(request); Map<String,
     * Object> resultMap = new HashMap(); // List<Map<String,Object>> baseList =
     * this.baseTestRepository.queryTopVehPassByKakou(kakouNum); return resultMap; }
     *//**
       * <p class="detail"> 功能：指定数据源 </p>
       * 
       * @author Zerlinda
       * @date 2017年9月1日
       * @param request
       * @return
       * @throws IOException
       *//*
         * @TargetDataSource("bfscrm")
         * @RequestMapping(value = "/getBaseList", produces = "application/json") public Object
         * getUser(HttpServletRequest request) throws IOException { Map<String, Object> alarmInfo =
         * new HashMap(); Map paramMap = ExControllerUtils.buildParametersMap(request); Map<String,
         * Object> resultMap = new HashMap(); // List<Map<String,Object>> baseList =
         * this.baseTestRepository.queryTopVehPassByKakou(kakouNum); return resultMap; }
         */

    // 不指定数据源
    @RequestMapping(value = "/advChannel/add", produces = "application/json")
    public Object saveUser(HttpServletRequest request)
    {
        Map<String, Object> data = new HashMap();
        Object result = null;
        try
        {
            Map paramMap = ExControllerUtils.buildParametersMap(request);
            result = advChannelRepository.doInsert(paramMap);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        data.put("success", result);
        return data;
    }
}
