package cn.bforce.business.tools.service.impl;


import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.bforce.business.tools.repository.AreaRepository;
import cn.bforce.business.tools.service.AreaService;


@Service("areaService")
public class AreaServiceImpl implements AreaService
{

    @Autowired
    AreaRepository areaRepository;

    public List<Map<String, Object>> getAreaList(String areaCode, int step, String keyWords)
    {
        areaCode = areaCode == null ? "" : areaCode;
        return areaRepository.getAreaList(areaCode, step, keyWords);
    }

    @Override
    public String getAreaDetail(String area_code)
    {
        String areaDetail = "";
        List<Map<String, Object>> list = areaRepository.getAreaDetail(area_code);
        if (list != null && list.size() > 0)
        {
            Map<String, Object> tempMap = list.get(0);
            if (tempMap != null)
            {
                areaDetail = tempMap.get("address") == null ? "" : tempMap.get(
                    "address").toString();
            }
        }
        return areaDetail;
    }

}
