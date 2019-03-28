package com.redpig.common.Service;

import com.redpig.common.dao.BaseDao;
import com.redpig.common.dao.UIdGenerator;
import com.redpig.common.entity.BaseEntity;
import com.redpig.common.model.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 通用服务类
 */
@Service
@Transactional
public class BaseService {
	private static final Logger LOG = Logger.getLogger(BaseService.class);
	@Qualifier("baseDao")
	@Autowired
	protected BaseDao baseDao;
	@Autowired
	protected UIdGenerator uIdGenerator;
	
	public <E extends BaseEntity> E findById(Class<E> type, String id) {
		return this.baseDao.findById(type, id);
	}
	
	public void save(BaseEntity e) {
		this.baseDao.save(e);
	}
		
	public void insert(BaseEntity e) {
	    this.baseDao.insert(e);
	}
	
	public int update(BaseEntity e) {
	    return this.baseDao.update(e);
	}
	
	public <E extends BaseEntity> int update(Class<E> type, String id, Map<String, Object> paramMap) {
		return this.baseDao.update(type, id, paramMap);
	}
	
	public <E extends BaseEntity> int update(Class<E> type, String[] ids, Map<String, Object> paramMap) {
		return this.baseDao.update(type, ids, paramMap);
	}

	public <E extends BaseEntity> List<E> list(Class<E> type, Order... orders) {
		return this.baseDao.list(type, orders);
	}
	
	public <E extends BaseEntity> Page<E> list(Class<E> type, Integer pageNo, Integer pageSize, Order... orders) {
		return this.baseDao.list(type, pageNo, pageSize, orders);
	}
	
	public <E extends BaseEntity> Page<E> list(Class<E> type, String[] fieldNames, Integer pageNo, Integer pageSize, Order... orders) {
		return this.baseDao.list(type, fieldNames, null, pageNo, pageSize, orders);
	}
	
	public <E extends BaseEntity> Page<E> list(Class<E> type, Map<String, Object> paramMap, Integer pageNo, Integer pageSize, Order... orders) {
		return this.baseDao.list(type, null, paramMap, pageNo, pageSize, orders);
	}
	
	public <E extends BaseEntity> Page<E> list(Class<E> type, String[] fieldNames, Map<String, Object> paramMap, Integer pageNo, Integer pageSize, Order... orders) {
		return this.baseDao.list(type, fieldNames, paramMap, pageNo, pageSize, orders);
	}
	
	public <E extends BaseEntity> List<Map<String, Object>> list (Class<E> type, String[] fieldNames, Map<String, Object> paramValues, Order... orders) {
		return this.baseDao.list(type, fieldNames, paramValues, orders);
	}
	
	public <E extends BaseEntity> List<E> listByProp(Class<E> type, String prop, Object value, Order... orders) {
		return this.baseDao.listByProp(type, prop, value, orders);
	}
	
	public <E extends BaseEntity> List<E> listByProps(Class<E> type, Map<String, Object> params, Order... orders) {
		return this.baseDao.listByProps(type, params, orders);
	}
	
	public <T extends Object, E extends BaseEntity> List<T> listProp(Class<E> type, String prop, Class<T> propType, Map<String, Object> params, Order... orders) {
		return this.baseDao.listProp(type, prop, propType, params, orders);
	}
	
	public <E extends BaseEntity> void del(Class<E> type) {
		this.baseDao.del(type);
	}
	
	public <E extends BaseEntity> int del(Class<E> type, String id) {
		return this.baseDao.del(type, id);
	}
	
	public <E extends BaseEntity> int del(Class<E> type, String... ids) {
		return this.baseDao.del(type, ids);
	}
	
	public <E extends BaseEntity> int del(Class<E> type, Map<String, Object> paramMap) {
		return this.baseDao.del(type, paramMap);
	}
	
	public <E extends BaseEntity> int count(Class<E> type, Map<String, Object> params) {
		return this.baseDao.count(type, params);
	}
	
	public <E extends BaseEntity> int count(Class<E> type) {
		return this.baseDao.count(type);
	}
}
