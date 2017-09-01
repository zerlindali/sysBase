package springjdbc;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import test.BaseTestService;

public class TestSpringJdbc extends BaseTestService
{
    static final Logger logger = LogManager.getLogger(TestSpringJdbc.class);
    
    @Autowired
    private JdbcTemplate jdbcTempalte;
    
    @Test
    public void test()
    {
        String sql = "select count(1) from weixin_goods_info";
        int n = jdbcTempalte.queryForObject(sql, Integer.class, new Object[]{});
        logger.debug("goods count:" + n);
    }
}
