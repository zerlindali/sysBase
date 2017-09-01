package redis;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

import test.BaseTestService;

@Cacheable
public class SysDictionary extends BaseTestService
{
    static final Logger logger = LogManager.getLogger(SysDictionary.class);
    
    @Autowired
    private RedisTemplate redisTemplate;
    
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    
    @Test
    public void test() throws Exception {
        redisTemplate.opsForValue().set("aaa", "111");
        Assert.assertEquals("111", redisTemplate.opsForValue().get("aaa"));
        
        //stringRedisTemplate.opsForValue().set("bbb", "222");
        //Assert.assertEquals("222", redisTemplate.opsForValue().get("bbb"));
    }
}
