package redis;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import test.BaseTestService;
import cn.bforce.common.cache.redis.RedisUtil;

public class RedisTest extends BaseTestService
{
    static final Logger logger = LogManager.getLogger(RedisTest.class);
    
    @Autowired  
    private RedisUtil redisClinet; 
    
    @Test
    public void test()
    {
        String a = redisClinet.getVal("category_1");
        redisClinet.setVal("token", "aaaaa", redisClinet.HOUR);
        logger.debug(a);
    }
}
