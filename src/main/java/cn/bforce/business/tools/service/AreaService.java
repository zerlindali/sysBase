package cn.bforce.business.tools.service;

import java.util.List;
import java.util.Map;

public interface AreaService
{
    public List<Map<String, Object>> getAreaList(String areaCode, int step, String keyWords);
    
    /**
     * <p class="detail">
     * 功能：通过区域代码获取省市区的值
     * </p>
     * @author Zerlinda
     * @date 2017年9月14日 
     * @param area_code
     * @return
     */
    public String getAreaDetail(String area_code);
}
