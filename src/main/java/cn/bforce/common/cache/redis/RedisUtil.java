package cn.bforce.common.cache.redis;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import cn.bforce.common.utils.string.StringUtil;


@Component
public class RedisUtil
{

    static final Logger logger = LogManager.getLogger(RedisUtil.class);
    
    @Autowired
    private JedisPool jedisPool;

    public static final int MINUTE = 60; // 一分钟

    public static final int HOUR = 60 * 60; // 一小时

    public static final int DAY = 24 * 60 * 60; // 一天，24小时 * 60分钟 * 60秒

    public static final int WEEK = DAY * 7; // 一周，7天

    public static final int MONTH = DAY * 30; // 一月，30天

    /**
     * 缓存单个值
     * 
     * @author kezhiqiang
     * @date 2016-11-8
     * @param key
     * @param value
     */
    public void setVal(String key, String value, int expireTime)
    {
        Jedis jedis = null;
        try
        {
            jedis = jedisPool.getResource();
            jedis.set(key, value);
            jedis.expire(key, expireTime);
        }
        finally
        {
            jedis.close();
        }
    }

    /**
     * 获取单个值
     * 
     * @author kezhiqiang
     * @date 2016-11-8
     * @param key
     * @return
     */
    public String getVal(String key)
    {
        Jedis jedis = null;
        try
        {
            jedis = jedisPool.getResource();
            return jedis.get(key);
        }
        finally
        {
            jedis.close();
        }
    }

    /**
     * 缓存map
     * 
     * @author kezhiqiang
     * @date 2016-11-8
     * @param key
     * @param map
     */
    public void setMap(String key, Map<String, String> map, int expireTime)
    {
        if (map != null && map.size() > 0)
        {
            Jedis jedis = null;
            try
            {
                jedis = jedisPool.getResource();
                jedis.hmset(key, map);
                jedis.expire(key, expireTime);
            }
            finally
            {
                jedis.close();
            }
        }
    }

    /**
     * 获取整个map
     * 
     * @author kezhiqiang
     * @date 2016-11-9
     * @param key
     * @return
     */
    public Map<String, String> getMap(String key)
    {
        Jedis jedis = null;
        try
        {
            jedis = jedisPool.getResource();
            Map<String, String> map = jedis.hgetAll(key);
            return map;
        }
        finally
        {
            jedis.close();
        }
    }

    /**
     * 获取map 指定key列表的值
     * 
     * @author kezhiqiang
     * @date 2016-11-8
     * @param key
     * @param valKey
     * @return
     */
    public List<String> getMap(String key, String... valKey)
    {

        Jedis jedis = null;
        try
        {
            jedis = jedisPool.getResource();
            List<String> value = null;
            if (jedis.exists(key))
            {
                value = jedis.hmget(key, valKey);
            }
            return value;
        }
        finally
        {
            jedis.close();
        }
    }

    /**
     * 获取map指定key的值
     * 
     * @author kezhiqiang
     * @date 2016-11-8
     * @param key
     * @param valKey
     * @return
     */
    public String getMapVal(String key, String valKey)
    {
        Jedis jedis = null;
        try
        {
            jedis = jedisPool.getResource();
            String value = null;
            if (jedis.exists(key))
            {
                List<String> list = jedis.hmget(key, valKey);
                if (list != null && list.size() > 0)
                {
                    value = list.get(0);
                }
            }

            return value;
        }
        finally
        {
            jedis.close();
        }
    }

    /**
     * 缓存List
     * 
     * @author kezhiqiang
     * @date 2016-11-8
     * @param key
     * @param list
     */
    public void setList(String key, List<String> list, int expireTime)
    {
        if (list != null && list.size() > 0)
        {
            String[] listgroup = new String[list.size()];
            Jedis jedis = null;
            try
            {
                jedis = jedisPool.getResource();
                jedis.lpush(key, list.toArray(listgroup));
                jedis.expire(key, expireTime); // 设置过期时间，单位秒
            }
            finally
            {
                jedis.close();
            }
        }
    }

    /**
     * 获取List
     * 
     * @author kezhiqiang
     * @date 2016-11-8
     * @param key
     * @return
     */
    public List<String> getList(String key)
    {
        Jedis jedis = null;
        try
        {
            jedis = jedisPool.getResource();
            List<String> value = null;
            if (jedis.exists(key))
            {
                value = jedis.lrange(key, 0, -1);
            }
            return value;
        }
        finally
        {
            jedis.close();
        }
    }

    /**
     * 升序排序后输出List
     * 
     * @author kezhiqiang
     * @date 2016-11-8
     * @param key
     * @return
     */
    public List<String> sortList(String key)
    {
        Jedis jedis = null;
        try
        {
            jedis = jedisPool.getResource();
            List<String> value = null;
            if (jedis.exists(key))
            {
                value = jedis.sort(key);
            }
            return value;
        }
        finally
        {
            jedis.close();
        }
    }

    /**
     * 删除指定缓存对象
     * 
     * @author kezhiqiang
     * @date 2016-11-8
     * @param key
     */
    public void del(String... key)
    {
        Jedis jedis = null;
        try
        {
            jedis = jedisPool.getResource();
            jedis.del(key);
        }
        finally
        {
            jedis.close();
        }
    }

    /**
     * <p class="detail">
     * 功能：设置Map<String, List>缓存
     * </p>
     * @author yuandx
     * @param key
     * @param map
     * @param expireTime
     * @throws
     */
    public void setMapWithList(String key, Map<String, List> map, int expireTime)
    {
        Map<byte[], byte[]> newMap = new HashMap<byte[], byte[]>();

        try
        {
            Set<String> keys = map.keySet();
            if (keys != null && keys.size() > 0)
            {
                for (Object _key : keys)
                {
                    newMap.put(StringUtil.serialize(_key), StringUtil.serialize(map.get(_key)));
                }
            }
        }
        catch (Exception e)
        {
            logger.error("setMapWithList error.");
        }

        Jedis jedis = null;
        
        try
        {
            jedis = jedisPool.getResource();
            jedis.hmset(StringUtil.serialize(key), newMap);
            jedis.expire(StringUtil.serialize(key), expireTime);
        }
        finally
        {
            jedis.close();
        }
    }
    
    /**
     * <p class="detail">
     * 功能:获取Map<String, List>缓存
     * </p>
     * @author yuandx
     * @param key
     * @param map
     * @param expireTime
     * @throws
     */
    public Map<String, List> getMapWithList(String key)
    {
        Map<byte[], byte[]> cachedMap = new HashMap<byte[], byte[]>();
        Map<String, List> result = new HashMap<String, List>();
        Jedis jedis = null;
        
        try
        {
            jedis = jedisPool.getResource();
            cachedMap = jedis.hgetAll(StringUtil.serialize(key));
            
            Set<byte[]> keys = cachedMap.keySet();
            if (keys != null && keys.size() > 0)
            {
                for (byte[] _key : keys)
                {
                    result.put(StringUtil.unserialize(_key).toString(),
                        (List)StringUtil.unserialize(cachedMap.get(_key)));
                }
            }
            
            return result;
        }
        catch (Exception e)
        {
            logger.error("getMapWithList error.");
            return null;
        }
        finally
        {
            jedis.close();
        }
    }
}