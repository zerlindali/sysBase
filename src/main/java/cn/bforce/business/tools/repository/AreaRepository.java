package cn.bforce.business.tools.repository;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import cn.bforce.common.persistence.repository.DataRepositoryJDBCBf;

@Repository("areaRepository")
public class AreaRepository extends DataRepositoryJDBCBf
{
    static final Logger logger = LogManager.getLogger(AreaRepository.class);
    
    public AreaRepository(){
        this.masterTable = "config_area";
        this.masterTablePK = "area_code";
    }
    
    /**
     * <p class="detail">
     * 功能：获取地址区域列表
     * </p>
     * @author wuxw
     * @param areaCode
     * @param step
     * @param keyWords
     * @return 
     * @throws
     */
    public List<Map<String, Object>> getAreaList(String areaCode, int step,
            String keyWords) {

        String sqlString = "select area_code ,area_name  from config_area where hidden=0";
        if( StringUtils.isNotBlank(keyWords) ){
            sqlString += " and area_name like '%" + keyWords + "%' ";
        }else{
            if( StringUtils.isBlank(areaCode) || areaCode.length()!=6 ){
                sqlString += " and step='"+step+"' ";
            }else if( areaCode.endsWith("0000") ){
                sqlString += " and step=2 and area_code like '" + areaCode.substring(0,2) + "%' and area_code!='"+areaCode+"' ";
            }else if( areaCode.endsWith("00") ){
                sqlString += " and step=3 and area_code like '" + areaCode.substring(0,4) + "%' and area_code!='"+areaCode+"' and area_name!='市辖区' and area_name!='县'";
            }
        }
        sqlString += "  order by area_code asc ";
        List<Map<String, Object>> list = this.jdbcTemp.queryForList(sqlString);
        return list;

    }
    
    public List<Map<String, Object>> getAreaDetail(String areaCode){
        String sql = "select getAreaCodeName_func(area_code) as address from config_area where area_code = '"+areaCode+"'";
        return this.jdbcTemp.queryForList(sql);
    }
}