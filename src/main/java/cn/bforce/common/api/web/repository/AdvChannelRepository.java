package cn.bforce.common.api.web.repository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import cn.bforce.common.persistence.repository.DataRepositoryJDBCBf;

@Repository("advChannelRepository")
public class AdvChannelRepository extends DataRepositoryJDBCBf{

	static final Logger logger = LogManager.getLogger(AdvChannelRepository.class);
	
	public AdvChannelRepository(){
		this.masterTable = "adv_channel";
		this.masterTablePK = "adv_id";
	}
	
}
