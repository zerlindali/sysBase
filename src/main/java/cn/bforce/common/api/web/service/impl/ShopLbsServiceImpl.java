package cn.bforce.common.api.web.service.impl;


import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.bforce.common.api.web.repository.ShopLbsRepository;
import cn.bforce.common.api.web.service.ShopLbsService;
import cn.bforce.common.persistence.DataObject;


@Service("shopLbsService")
public class ShopLbsServiceImpl implements ShopLbsService
{

    @Autowired
    private ShopLbsRepository shopLbsRepository;

    public DataObject doLoad(Serializable rowId)
    {
        return shopLbsRepository.doLoad(rowId);
    }

}
