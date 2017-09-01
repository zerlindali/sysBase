package cn.bforce.common.cache.dictionary;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import cn.bforce.common.cache.redis.RedisUtil;

@Component
public class SysDictionary implements CommandLineRunner
{
    static final Logger logger = LogManager.getLogger(SysDictionary.class);

    @Autowired
    private RedisUtil redisClinet;

    @Override
    public void run(String... args)
        throws Exception
    {
        /*Map<String, Map<String, String>> sysDicMap = new HashMap<String, Map<String, String>>();
        
        StringBuilder sb = new StringBuilder(
            "SELECT id,dflag,dkey,dvalue FROM scrm_dictionary order by dflag asc,dkey asc ");
        List<Map<String, Object>> dictionary = jdbcTemplate.queryForList(sb.toString());
        for (int i = 0; dictionary != null && i < dictionary.size();)
        {
            Map<String, Object> dic = dictionary.get(i);
            Map<String, String> mapString = new HashMap<String, String>();
            mapString.put(dic.get("dkey").toString(), String.valueOf(dic.get("dvalue")));
            do
            {
                ++i;
                if (i >= dictionary.size())
                {
                    break;
                }
                Map<String, Object> dic2 = dictionary.get(i);
                if (dic.get("dflag").equals(dic2.get("dflag")))
                {
                    mapString.put(dic2.get("dkey").toString(), String.valueOf(dic2.get("dvalue")));
                }
                else
                {
                    break;
                }
            }
            while (i < dictionary.size());
            
            sysDicMap.put(dic.get("dflag").toString(),mapString);
        }*/
        
        //redisClinet.setMap("SYS_DIC_MAP", sysDicMap, redisClinet.DAY * 7);
    }

    /**
     * 获取词典接口
     * 
     * @param
     * @return String
     */
    /*public Map<String, String> getDvalueByMap(String dflag)
    {
        Map<String, String> map = new HashMap<String, String>();
        map = redisClinet.getMap(dflag);
        if (map == null)
        {
            map = queryDvalueByDflag(dflag, "").get(dflag);
        }
        return map;
    }*/
    
    /*private Map<String, Map<String, String>> queryDvalueByDflag(String dflag, String dkey){
        StringBuilder sb = new StringBuilder("SELECT id,dflag,dkey,dvalue FROM scrm_dictionary where 1 = 1 ");
        if(!StringUtil.isNullOrEmpty(dflag)){
            sb.append(" and dflag = '"+dflag+"'");
        }
        if(!StringUtil.isNullOrEmpty(dkey)){
            sb.append(" and dkey = '"+dkey+"'");
        }
        sb.append(" order by dflag asc,dkey asc ");
        List<Map<String,Object>> dictionary = jdbcTemplate.queryForList(sb.toString());
        for(int i=0; dictionary!=null && i<dictionary.size(); ){
            Map<String,Object> dic=dictionary.get(i);
            Map<String, String> mapString = new HashMap<String,String>();
            mapString.put(dic.get("dkey").toString(), String.valueOf(dic.get("dvalue")));
            do{
                ++i;
                if(i>=dictionary.size()){
                    break;
                }
                Map<String,Object> dic2=dictionary.get(i);
                if(dic.get("dflag").equals(dic2.get("dflag"))){
                    mapString.put(dic2.get("dkey").toString(), String.valueOf(dic2.get("dvalue")));
                }else{
                    break;
                }
            }while(i<dictionary.size());
            //SYS_DIC_MAP.put(dic.get("dflag").toString(),mapString);
        }
        //return SYS_DIC_MAP;
    }*/
}
