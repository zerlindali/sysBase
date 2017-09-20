package cn.bforce.business.web.repository;

import org.springframework.stereotype.Repository;

import cn.bforce.common.persistence.repository.DataRepositoryJDBCBf;

@Repository("userLogRepository")
public class UserLogRepository extends DataRepositoryJDBCBf
{
    public UserLogRepository()
    {
        this.masterTable = "user_logs";
        this.masterTablePK = "log_id";
    }
}
