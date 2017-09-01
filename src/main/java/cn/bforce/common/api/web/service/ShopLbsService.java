package cn.bforce.common.api.web.service;

import java.io.Serializable;

import cn.bforce.common.persistence.DataObject;

public interface ShopLbsService
{
    public DataObject doLoad(Serializable rowId);
}
