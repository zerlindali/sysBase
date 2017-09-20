package message;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import test.BaseTestService;
import cn.bforce.common.cache.authority.AuthorityUtil;
import cn.bforce.common.cache.redis.RedisUtil;
import cn.bforce.common.utils.web.MessageHelper;


public class testClass extends BaseTestService
{
    static final Logger logger = LogManager.getLogger(testClass.class);

    @Autowired
    private MessageHelper msgHelper;
    
    @Autowired
    private RedisUtil redisClient;
    
    @Autowired
    private AuthorityUtil authorityUtil;

    @Test
    public void test()
    {
        /*String msg1 = msgHelper.getMessage("login.relogin");
        String msg2 = msgHelper.getMessage("input.length.lessthan", new String[] {"姓名", "14"});
        logger.debug(msg1);
        logger.debug(msg2);*/
        
        /*Map<String, List> map = new HashMap<String, List>();
        
        List<String> list = new ArrayList<String>();
        list.add("abc");
        
        map.put("1", list);
        
        redisClient.setMapWithList("maplist", map, 60);
        
        logger.debug(redisClient.getMapWithList("maplist"));*/
        
        logger.debug(authorityUtil.getValue("LEVEL2"));
    }

}
