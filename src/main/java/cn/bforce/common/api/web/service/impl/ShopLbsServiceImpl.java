package cn.bforce.common.api.web.service.impl;


import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.bforce.common.api.web.repository.ShopLbsRepository;
import cn.bforce.common.api.web.service.ShopLbsService;
import cn.bforce.common.persistence.DataObject;


@Service("shopLbsService")
public class ShopLbsServiceImpl implements ShopLbsService
{

    @Autowired
    private ShopLbsRepository shopLbsRepository;
    
    @Transactional(value = "bfscrmTransactionManager",readOnly=true)
    public DataObject doLoad(Serializable rowId)
    {
        return shopLbsRepository.doLoad(rowId);
    }
    
    @Transactional(value = "bfscrmTransactionManager")
    public int doUpdate(String v) {
         shopLbsRepository.doUpdate(v);
         int a  = 8/0;
         return 1;
    }

}
