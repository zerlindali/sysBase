package cn.bforce.business.web.service;


import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.bforce.business.web.repository.UserLogRepository;

@Service
public class UserLogsService
{
    @Autowired
    UserLogRepository userLogRepository;

    public boolean addUserLogs(Map dataMap)
    {
        userLogRepository.doInsert(dataMap);
        return true;
    }
}
