package cn.bforce.common.api.web.repository;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import cn.bforce.common.persistence.repository.DataRepositoryJDBCBfscrm;


@Repository("shopLbsRepository")
public class ShopLbsRepository extends DataRepositoryJDBCBfscrm
{
    static final Logger logger = LogManager.getLogger(ShopLbsRepository.class);

    public ShopLbsRepository()
    {
        this.masterTable = "scrm_shop_lbs";
        this.masterTablePK = "shop_id";
    }
    
    
    public int doUpdate(String v) {
       String sql  = "UPDATE `scrm_shop_lbs` SET `logo`=? WHERE  `shop_id`='SHOP149878725790646';";
       return this.doUpdate(sql, v);
    }
    
}
