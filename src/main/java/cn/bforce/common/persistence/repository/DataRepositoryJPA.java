package cn.bforce.common.persistence.repository;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.collections.map.CaseInsensitiveMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import cn.bforce.common.persistence.NamedQuery;
import cn.bforce.common.persistence.OperationException;
import cn.bforce.common.utils.ExArrayList;
import cn.bforce.common.utils.web.ExSQLUtils;

public class DataRepositoryJPA<T> implements DataRepository{

	protected  final Logger log = LogManager.getLogger(getClass());
		
	protected String masterTable;
	
	protected String masterTablePK;
	
	protected String loginUserId;
	
	protected String orderBy;
	
	protected String databaseType;
	
	private JpaRepository jpaRepository;
	private JpaSpecificationExecutor<T> jpaSpecificationExecutor;
	
	public DataRepositoryJPA(){
		
	}
	
	
	public String reName(String tableName){
		return tableName;
	}
	

	public String getMasterTablePK() {
		return masterTablePK;
	}


	public void setMasterTablePK(String masterTablePK) {
		this.masterTablePK = masterTablePK;
	}

	public String getMasterTable() {
		return masterTable;
	}

	public void setMasterTable(String masterTable) {
		this.masterTable = masterTable;
	}

	@Override
	public String getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

	public String getLoginUserId() {
		return loginUserId;
	}

	@Override
	public void setLoginUserId(String loginUserId) {
		this.loginUserId = loginUserId;
	}
	
	@Override
	public String getSelectSQL() {
		String sql = "select * from " + this.masterTable +" where 1=1 ";
		return sql;
	}
	
	protected Specification getWhereClause(final Map paramMap){
		
		return new Specification<T>(){
			@Override  
            public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> list = new ArrayList<Predicate>();  
				
				for(Iterator iter = paramMap.keySet().iterator(); iter.hasNext();){
					String key = (String)iter.next();
					String value =  String.valueOf(paramMap.get(key));
					Path<String> nameExp = root.get(key); 
					list.add(cb.equal(nameExp, value)); 
				}
				
			    Predicate[] p = new Predicate[list.size()];  
			    return cb.and(list.toArray(p));
			}
		};
	}
	@Override
	public List queryData(String filter, final Map paramMap, int pageIndex, int pageSize){
		List resultList = new ExArrayList();
		
		Pageable pageRequest = new PageRequest(pageIndex, pageSize);
		Specification specification = getWhereClause(paramMap);
		
		Page pageResult = this.jpaSpecificationExecutor.findAll(specification, pageRequest);
		resultList = pageResult.getContent();
		return resultList;
	}
	
	@Override
	public List queryData(String whereClause, String aOrderBy, Object[] parameters){
		return this.queryDataByPage(whereClause, aOrderBy, 0, 0, parameters);
	}
	
	@Override
	public List queryDataBySQL(String sql, Object[] parameters){
		List resultList = new ExArrayList();
		return resultList;
	}
	
	@Override
	public List queryDataByPage(String whereClause, String aOrderBy, int pageIndex, int pageSize, Object[] parameters) {
		List resultList = new ExArrayList();
		return resultList;
	}
	
	@Override
	public List queryDataByPage(String sql, int pageIndex, int pageSize, Object[] parameters) {
		// TODO Auto-generated method stub
		return null;
	}
	

	@Override
	public Map findOneBySQL(String sql, Object[] parameters) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public Map findOne(String whereClause, Object[] parameters) {
		Map dataMap = new CaseInsensitiveMap();
		return new CaseInsensitiveMap(dataMap);
	}
	
	@Override
	public Map findOne(Map paramMap) {
		Map dataMap = new CaseInsensitiveMap();
		return new CaseInsensitiveMap(dataMap);
	}


	@Override
	public NamedQuery findNamedQuery(String name) {
		return null;
	}
	

	@Override
	public int doSave(Map dataMap) {
		// TODO Auto-generated method stub
		return 0;
	}


	@Override
	public String doAction(String action, Map parameters) throws OperationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getValueListName() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void setCaller(String caller) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void clearRowMetaData(Map dataMap) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Map createNewRow() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public Map doLoad(Serializable rowId) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public int doDelete(Serializable rowId) {
		// TODO Auto-generated method stub
		return 0;
	}


	@Override
	public Serializable doInsert(Map dataMap) {
		// TODO Auto-generated method stub
		return null;
	}

	public  int[] doBatchInsert(List<Map> dataList){
		return null;
	}

	@Override
	public int doUpdate(Serializable rowId, Map dataMap) {
		// TODO Auto-generated method stub
		return 0;
	}


	@Override
	public int doUpdate(String sql, Object... args) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Map getColumnValueListMap() {
		// TODO Auto-generated method stub
		return null;
	}


    public int queryRowCount(StringBuilder whereClause, Map whereParams)
    {
        return 0;

    }
	
	/*@Override
	public void linkColumnToValueList(){
		
	}*/
}
