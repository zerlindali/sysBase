package redis;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;

import test.BaseTestService;
import cn.bforce.common.cache.redis.RedisUtil;

public class RedisTest extends BaseTestService
{
    static final Logger logger = LogManager.getLogger(RedisTest.class);
    
    @Autowired  
    private RedisUtil redisClinet; 
    
    @Autowired  
    private StringRedisTemplate redisTemplate; 
    
    @Test
    public void test()
    {
        String a = redisClinet.getVal("category_1");
        
        logger.debug(a);
        
        redisTemplate.opsForValue().set("b", "abcdefg");
        Object b = redisTemplate.opsForValue().get("b");
        
        logger.debug(b);
    }
}
