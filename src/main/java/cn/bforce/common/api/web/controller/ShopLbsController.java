package cn.bforce.common.api.web.controller;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.bforce.common.api.web.service.ShopLbsService;
import cn.bforce.common.utils.web.ExControllerUtils;

@RestController
@RequestMapping("/api/test")
public class ShopLbsController
{
    static final Logger logger = LogManager.getLogger(AdvChannelController.class);

    @Autowired
    private ShopLbsService shopLbsService;
    
    // 指定数据源
    @RequestMapping(value = "/shopLbs/get", produces = "application/json")
    public Object getUser(HttpServletRequest request)
    {
        Map<String, Object> data = new HashMap();
        Object result = null;
        try
        {
            Map paramMap = ExControllerUtils.buildParametersMap(request);
            result = shopLbsService.doLoad((Serializable)paramMap.get("shop_id"));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        data.put("success", result);
        return data;
    }
}
