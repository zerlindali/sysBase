package cn.bforce.common.cache.dictionary;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import cn.bforce.common.cache.redis.RedisUtil;
import cn.bforce.common.utils.string.GsonUtil;
import cn.bforce.common.utils.string.StringUtil;


@Component
public class SysDictionaryUtil implements CommandLineRunner
{
    static final Logger logger = LogManager.getLogger(SysDictionaryUtil.class);

    private static String SYS_DIC_MAP = "SYS_DIC_MAP";

    @Autowired
    private RedisUtil redisClient;

    @Autowired
    @Qualifier("bfscrmJdbcTemplate")
    protected JdbcTemplate jdbcTemplate;

    @Override
    public void run(String... args)
        throws Exception
    {
        logger.info("init SYS_DIC_MAP start...");
        
        if (redisClient.getMap(SYS_DIC_MAP) != null && !redisClient.getMap(SYS_DIC_MAP).isEmpty())
        {
            logger.info("SYS_DIC_MAP already exists...");
            logger.info(redisClient.getMap(SYS_DIC_MAP));
            return;
        }
        
        initSysDicMap();
        
        logger.info("init SYS_DIC_MAP end...");
    }

    private Map<String, String> initSysDicMap()
    {
        Map<String, String> sysDicMap = new HashMap<String, String>();

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

            sysDicMap.put(dic.get("dflag").toString(), mapString.toString());
        }

        setSysDicMap(sysDicMap);
        
        return sysDicMap;
    }

    public Map<String, String> getDvalueByMap(String dflag)
    {
        Map<String, String> sysDicMap = redisClient.getMap(SYS_DIC_MAP);
        
        if(sysDicMap == null || sysDicMap.isEmpty())
        {
            sysDicMap = initSysDicMap();
        }
        
        Map<String, String> map = GsonUtil.stringToMap(sysDicMap.get(dflag));
        if (map == null)
        {
            map = GsonUtil.stringToMap(queryDvalueByDflag(dflag, "").get(dflag));
        }

        return map;
    }

    private Map<String, String> queryDvalueByDflag(String dflag, String dkey)
    {

        Map<String, String> sysDicMap = redisClient.getMap(SYS_DIC_MAP);

        StringBuilder sb = new StringBuilder(
            "SELECT id,dflag,dkey,dvalue FROM scrm_dictionary where 1 = 1 ");
        if (!StringUtil.isNullOrEmpty(dflag))
        {
            sb.append(" and dflag = '" + dflag + "'");
        }
        if (!StringUtil.isNullOrEmpty(dkey))
        {
            sb.append(" and dkey = '" + dkey + "'");
        }
        sb.append(" order by dflag asc,dkey asc ");

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

            sysDicMap.put(dic.get("dflag").toString(), mapString.toString());
        }
        
        setSysDicMap(sysDicMap);
        
        return sysDicMap;
    }

    public String getValueByFlagAndKey(String dflag, String dkey)
    {
        Map<String, String> sysDicMap = redisClient.getMap(SYS_DIC_MAP);
        
        if(sysDicMap == null || sysDicMap.isEmpty())
        {
            sysDicMap = initSysDicMap();
        }

        String value = "";
        value = GsonUtil.stringToMap(sysDicMap.get(dflag)).get(dkey);
        if (StringUtil.isNullOrEmpty(value) && !StringUtil.isNullOrEmpty(dflag)
            && !StringUtil.isNullOrEmpty(dkey))
        {
            value = GsonUtil.stringToMap(queryDvalueByDflag(dflag, dkey).get(dflag)).get(dkey);
        }
        return value;
    }

    public void addDvalue(String dflag, Map<String, String> mapString)
    {
        Map<String, String> sysDicMap = redisClient.getMap(SYS_DIC_MAP);
        sysDicMap.put(dflag, mapString.toString());
        setSysDicMap(sysDicMap);
    }

    public void addSingleDvalue(String dflag, String dkey, String dvalue)
    {
        Map<String, String> sysDicMap = redisClient.getMap(SYS_DIC_MAP);
        Map<String, String> map = GsonUtil.stringToMap(sysDicMap.get(dflag));
        if (map != null)
        {
            map.put(dkey, dvalue);
            sysDicMap.put(dflag, map.toString());
            setSysDicMap(map);
        }
        else
        {
            Map<String, String> map2 = new HashMap<String, String>();
            map2.put(dkey, dvalue);
            addDvalue(dflag, map2);
        }
    }

    public void updateValue(String dflag, Map<String, String> mapString)
    {
        Map<String, String> sysDicMap = redisClient.getMap(SYS_DIC_MAP);
        sysDicMap.put(dflag, mapString.toString());
        setSysDicMap(sysDicMap);
    }

    public void updateSingleValue(String dflag, String dkey, String dvalue)
    {
        Map<String, String> sysDicMap = redisClient.getMap(SYS_DIC_MAP);
        Map<String, String> map = GsonUtil.stringToMap(sysDicMap.get(dflag));
        if (map != null)
        {
            map.put(dkey, dvalue);
        }
        else
        {
            Map<String, String> map2 = new HashMap<String, String>();
            map2.put(dkey, dvalue);
            addDvalue(dflag, map2);
        }
    }

    public void deleteValue(String dflag)
    {
        Map<String, String> sysDicMap = redisClient.getMap(SYS_DIC_MAP);
        Map<String, String> map = GsonUtil.stringToMap(sysDicMap.get(dflag));
        map.remove(dflag);
        sysDicMap.put(dflag, map.toString());
        setSysDicMap(map);
    }

    public void deleteSingleValue(String dflag, String dkey)
    {
        Map<String, String> sysDicMap = getSysDicMap();
        Map<String, String> map = GsonUtil.stringToMap(sysDicMap.get(dflag));

        if (map != null)
        {
            map.remove(dkey);

            sysDicMap.put(dflag, map.toString());
            setSysDicMap(map);
        }
    }

    private Map<String, String> getSysDicMap()
    {
        return redisClient.getMap(SYS_DIC_MAP);
    }

    private void setSysDicMap(Map<String, String> map)
    {
        redisClient.setMap("SYS_DIC_MAP", map, redisClient.DAY * 7);
    }
}
