package cn.bforce.common.cache.authority;


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

import cn.bforce.business.user.UserLevel;
import cn.bforce.business.user.UserRole;
import cn.bforce.common.cache.redis.RedisUtil;


/**
 * <p class="detail">
 * 功能：功能：系统接口权限缓存
 * </p>
 * @ClassName: AuthorityUtil 
 * @version V1.0  
 * @date 2017年9月20日 
 * @author yuandx
 * Copyright 2017 b-force.cn, Inc. All rights reserved
 */
@Component
public class AuthorityUtil implements CommandLineRunner
{

    static final Logger logger = LogManager.getLogger(AuthorityUtil.class);

    private static String AUTHORITY_MAP = "AUTHORITY_MAP";
    
    private static String LEVEL = "LEVEL";
    
    private static String ROLE = "ROLE";

    @Autowired
    private RedisUtil redisClient;

    @Autowired
    @Qualifier("bfJdbcTemplate")
    protected JdbcTemplate jdbcTemplate;

    @Override
    public void run(String... args)
        throws Exception
    {

        logger.info("init AUTHORITY_MAP start...");

        if (redisClient.getMap(AUTHORITY_MAP) != null
            && !redisClient.getMap(AUTHORITY_MAP).isEmpty())
        {
            logger.info("AUTHORITY_MAP already exists...");
            logger.info(redisClient.getMap(AUTHORITY_MAP));
            return;
        }

        initAuthorityMap();

        logger.info("init AUTHORITY_MAP end...");
    }

    /**
     * <p class="detail"> 功能：初始化权限缓存 </p>
     * 
     * @author yuandx
     * @return
     * @throws
     */
    private Map<String, List> initAuthorityMap()
    {
        Map<String, List> authMap = new HashMap<String, List>();

        String levelSql = " select cin.interface_url from config_interface_use cinu left join config_interface cin  on cinu.interface_id=cin.id "
                           + " where  cinu.user_level=?";
        
        String roleSql = " select cin.interface_url from config_interface_use cinu left join config_interface cin  on cinu.interface_id=cin.id "
            + " where  cinu.role_id=?";

        // level权限数据
        authMap.put(LEVEL + UserLevel.SYSTEM.getCode(),
            jdbcTemplate.queryForList(levelSql, String.class, UserLevel.SYSTEM.getCode()));

        authMap.put(LEVEL + UserLevel.PARTNER.getCode(),
            jdbcTemplate.queryForList(levelSql, String.class, UserLevel.PARTNER.getCode()));

        authMap.put(LEVEL + UserLevel.ACCOUNT.getCode(),
            jdbcTemplate.queryForList(levelSql, String.class, UserLevel.ACCOUNT.getCode()));

        authMap.put(LEVEL + UserLevel.ADVERT.getCode(),
            jdbcTemplate.queryForList(levelSql, String.class, UserLevel.ADVERT.getCode()));
        
        //role权限数据
        authMap.put(ROLE + UserRole.ADMININSTRATOR.getCode(),
            jdbcTemplate.queryForList(roleSql, String.class, UserRole.ADMININSTRATOR.getCode()));
        
        authMap.put(ROLE + UserRole.OPERATE.getCode(),
            jdbcTemplate.queryForList(roleSql, String.class, UserRole.OPERATE.getCode()));
        
        authMap.put(ROLE + UserRole.SERVICE.getCode(),
            jdbcTemplate.queryForList(roleSql, String.class, UserRole.SERVICE.getCode()));

        redisClient.setMapWithList(AUTHORITY_MAP, authMap, redisClient.DAY * 7);

        return authMap;
    }

    /**
     * <p class="detail">
     * 功能：获取权限缓存map
     * </p>
     * @author yuandx
     * @return
     * @throws
     */
    public Map<String, List> getAuthMap()
    {
        Map<String, List> authMap = redisClient.getMapWithList(AUTHORITY_MAP);
        
        if (authMap == null)
        {
            authMap = initAuthorityMap();
        }
        
        return authMap;
    }
    
    /**
     * <p class="detail"> 功能：根据权限标识获取权限配置list </p>
     * 
     * @author yuandx
     * @param key
     * @return
     * @throws
     */
    public List getValue(String key)
    {
        Map<String, List> authMap = redisClient.getMapWithList(key);

        if (authMap == null || authMap.isEmpty())
        {
            authMap = initAuthorityMap();
        }

        return authMap.get(key);
    }
}
