package com.redpig.common.dao;

import com.redpig.common.entity.AuditableEntity;
import com.redpig.common.entity.BaseEntity;
import com.redpig.common.utils.SqlBuilder;
import com.redpig.common.utils.Utils;
import com.redpig.common.model.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.util.*;
import java.util.Map.Entry;

/**
 * 通用DAO类
 */
@Repository
public class BaseDao extends NamedParameterJdbcDaoSupport {
	private static final Logger LOG = Logger.getLogger(BaseDao.class);
	@Autowired
	protected UIdGenerator uIdGenerator;
	
	@Resource(name = "jdbcTemplate")
	protected void inject(JdbcTemplate jdbcTemplate) {
		super.setJdbcTemplate(jdbcTemplate);
	}
	
	public <E extends BaseEntity> E findById(Class<E> type, String id) {
		if (Utils.isNull(type)) {
			return null;
		}
		List<E> es = this.listByProp(type, "id", id);
		if (Utils.isNotEmpty(es)) {
			return es.get(0);
		}
		return null;
	}
	
	public void save(BaseEntity e) {
		if (Utils.isNull(e)) {
			return;
		}
		if (Utils.isBlank(e.getId())) {
			this.insert(e);
		} else {
			this.update(e);
		}
	}
		
	public void insert(BaseEntity e) {
		if (Utils.isNull(e)) {
			return;
		}
		if (Utils.isBlank(e.getId())) {
			e.setId(this.uIdGenerator.genId());
		}
		if (e instanceof AuditableEntity) {
			AuditableEntity ae = (AuditableEntity) e;
			if (Utils.isNull(ae.getCreatedTime())) {
				ae.setCreatedTime(new Date());
			}
		}
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		SimpleJdbcInsert insertActor = new SimpleJdbcInsert(jdbcTemplate);  
	    insertActor.setTableName(Utils.toDBName(e.getClass().getSimpleName()));  
	    insertActor.execute(new BeanPropertySqlParameterSource(e));
	}
	
	public int update(BaseEntity e) {
		if (Utils.isNull(e) || Utils.isBlank(e.getId())) {
			return 0;
		}
		if (e instanceof AuditableEntity) {
	    	AuditableEntity ae = (AuditableEntity) e;
			if (Utils.isNull(ae.getUpdatedTime())) {
				ae.setUpdatedTime(new Date());
			}
	    }
		Class<? extends BaseEntity> type = e.getClass();
		String tbName = Utils.toDBName(type.getSimpleName());
		StringBuffer sb = new StringBuffer("update " + tbName + " set ");  
	    List<Field> fields = Utils.getFields(type);  
	    Field field;
	    String fieldName, dbName, getterMethodName;
	    Class<?> fieldType;
	    int size = fields.size(), j = 0;
	    Map<String, Object> paramMap = new HashMap<String, Object>();
		for (int i = 0; i < size; i++) {  
			field = fields.get(i);
			fieldType = field.getType();
	        fieldName = field.getName();
	        getterMethodName = Utils.toGetterMethodName(fieldName, fieldType);
	        dbName = Utils.toDBName(fieldName);
	        if ("createdTime".equals(fieldName) || "id".equals(fieldName) || "serialVersionUID".equals(fieldName)) {
	        	continue;
	        }
	        try {
				paramMap.put(fieldName, Utils.getMethod(type, getterMethodName).invoke(e));
			} catch (Exception ex) {
				LOG.error("调用getter方法出错，方法：" + getterMethodName, ex);
				continue;
			}
	        if (j > 0) {  
	        	sb.append(',');
	        }
	        sb.append(dbName + " = :" + fieldName);
	        j++;
	    }
	    
	    sb.append(" where id = :id");
	    paramMap.put("id", e.getId());
	    String sql = sb.toString();
	    LOG.debug("更新实体SQL为：" + sql);
		int updateCount = this.getNamedParameterJdbcTemplate().update(sql, paramMap);
		return updateCount;
	}
	
	public <E extends BaseEntity> int update(Class<E> type, String id, Map<String, Object> paramMap) {
		if (Utils.isNull(type) || Utils.isBlank(id) || Utils.isNull(paramMap) || paramMap.size() <= 0) {
			return 0;
		}
		String tbName = Utils.toDBName(type.getSimpleName());
		StringBuffer sb = new StringBuffer("update " + tbName + " set ");  
	    Set<String> keys = paramMap.keySet();  
	    int i = 0;
	    for (String key : keys) {  
	        if (i > 0) {  
	        	sb.append(',');
	        }  
	        sb.append(Utils.toDBName(key) + " = :" + key);
	        i++;
	    }
	    if (!paramMap.containsKey("updatedTime") && Utils.isSubClassOf(type, AuditableEntity.class)) {
	    	if (i > 0) {
	    		sb.append(',');
	    	}
	    	sb.append("updated_time = :updatedTime");
	    	paramMap.put("updatedTime", new Date());
	    }
	    
	    sb.append(" where id = :id");
	    String sql = sb.toString();  
	    LOG.debug("更新实体SQL为：" + sql);
	    Map<String, Object> pMap = paramMap;
	    paramMap.put("id", id);
		int updateCount = this.getNamedParameterJdbcTemplate().update(sql, pMap);
		return updateCount;
	}
	
	public <E extends BaseEntity> int update(Class<E> type, String[] ids, Map<String, Object> paramMap) {
		if (Utils.isNull(type) || Utils.isEmpty(ids) || Utils.isNull(paramMap) || paramMap.size() <= 0) {
			return 0;
		}
		String tbName = Utils.toDBName(type.getSimpleName());
		StringBuffer sb = new StringBuffer("update " + tbName + " set ");  
	    Set<String> keys = paramMap.keySet();  
	    int i = 0;
	    for (String key : keys) {
	        if (i > 0) {  
	        	sb.append(',');
	        }  
	        sb.append(Utils.toDBName(key) + " = :" + key);
	        i++;
	    }
	    if (!paramMap.containsKey("updatedTime") && Utils.isSubClassOf(type, AuditableEntity.class)) {
	    	if (i > 0) {
	    		sb.append(',');
	    	}
	    	sb.append("updated_time = :updatedTime");
	    	paramMap.put("updatedTime", new Date());
	    }
	    
	    sb.append(" where id in (");
	    int j = 0;
	    for (String id : ids) {
	    	if (j > 0) {
	    		sb.append(",");
	    	}
	    	sb.append(":id" + j);
	    	paramMap.put("id" + j, id);
	    	j++;
	    }
	    sb.append(")");
	    String sql = sb.toString();  
	    LOG.debug("更新实体SQL为：" + sql);
	    Map<String, Object> pMap = paramMap;
		int updateCount = this.getNamedParameterJdbcTemplate().update(sql, pMap);
		return updateCount;
	}

	public <E extends BaseEntity> List<E> list(Class<E> type, Order... orders) {
		if (Utils.isNull(type)) {
			return new ArrayList<E>();
		}
		String sql = "select * from " + Utils.toDBName(type.getSimpleName());
		if (Utils.isNotEmpty(orders)) {
			sql += " order by ";
			int len = orders.length;
			for (int i = 0; i < len; i++) {
				if (i > 0) {
					sql += ",";
				}
				sql += orders[i];
			}
		}
		return this.list(type, sql, new Object[0]);
	}

	public <E extends BaseEntity> Page<E> list(Class<E> type, Integer pageNo, Integer pageSize, Order... orders) {
		return this.list(type, null, null, pageNo, pageSize, orders);
	}
	
	public <E extends BaseEntity> Page<E> list(Class<E> type, String[] fieldNames, Integer pageNo, Integer pageSize, Order... orders) {
		return this.list(type, fieldNames, null, pageNo, pageSize, orders);
	}
	
	public <E extends BaseEntity> Page<E> list(Class<E> type, Map<String, Object> paramMap, Integer pageNo, Integer pageSize, Order... orders) {
		return this.list(type, null, paramMap, pageNo, pageSize, orders);
	}
	
	public <E extends BaseEntity> Page<E> list(Class<E> type, String[] fieldNames, Map<String, Object> paramMap, Integer pageNo, Integer pageSize, Order... orders) {
		if (Utils.isNull(type)) {
			return new Page<E>(0);
		}
		int pn = Utils.isNull(pageNo) ? Page.PAGE_NO : pageNo;
		int ps = Utils.isNull(pageSize) ? Page.PAGE_SIZE : pageSize;
		int total = this.count(type, paramMap);
		Page<E> page = new Page<E>(pn, ps, total);
		
		String fields = "";
		if (Utils.isEmpty(fieldNames)) {
			fields += "*";
		} else {
			int len = fieldNames.length;
			for (int i = 0; i < len; i++) {
				if (i > 0) {
					fields += ',';
				}
				fields += Utils.toDBName(fieldNames[i]);
			}
		}
		SqlBuilder sb = new SqlBuilder("select " + fields + " from " + Utils.toDBName(type.getSimpleName()) + " where 1 = 1");
		if (Utils.isNotEmpty(paramMap)) {
			Set<Entry<String, Object>> ens = paramMap.entrySet();
			for (Entry<String, Object> en : ens) {
				if (Utils.isNull(en.getValue())) {
					sb.appendStatement(" and " + Utils.toDBName(en.getKey()) + " is null");
				} else {
					sb.appendStatement(" and " + Utils.toDBName(en.getKey()) + " = ").appendParamValue(en.getValue());
				}
			}
		}
		if (Utils.isNotEmpty(orders)) {
			sb.appendStatement(" order by ");
			int len = orders.length;
			for (int i = 0; i < len; i++) {
				if (i > 0) {
					sb.appendStatement(",");
				}
				sb.appendStatement(orders[i].toString());
			}
		}
		sb.appendStatement(" limit " + page.getOffset() + ", " + ps);
		List<E> rows = this.list(type, sb.toStatement(), sb.toParams());
		page.setRows(rows);
		return page;
	}
	
	protected <E extends BaseEntity> Page<E> list(Class<E> type, String sql, Object[] params, Integer pageNo, Integer pageSize) {
		if (Utils.isNull(type) || Utils.isBlank(sql)) {
			return new Page<E>(0);
		}
		int pn = Utils.isNull(pageNo) ? Page.PAGE_NO : pageNo;
		int ps = Utils.isNull(pageSize) ? Page.PAGE_SIZE : pageSize;
		int total = this.count(sql, params);
		Page<E> page = new Page<E>(pn, ps, total);
		sql += " limit " + page.getOffset() + ", " + ps;
		List<E> rows = this.list(type, sql, params);
		page.setRows(rows);
		LOG.debug("查询SQL为：" + sql);
		return page;
	}
	
	public <E extends BaseEntity> List<E> list(Class<E> type, String sql, Object[] params) {
		if (Utils.isNull(type) || Utils.isBlank(sql)) {
			return new ArrayList<E>();
		}
		LOG.debug("查询SQL为：" + sql);
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		return jdbcTemplate.query(sql, params, new BeanPropertyRowMapper<E>(type));
	}
	
	public <E extends BaseEntity> List<Map<String, Object>> list (Class<E> type, String[] fieldNames, Map<String, Object> paramValues, Order... orders) {
		if (Utils.isNull(type)) {
			return new ArrayList<Map<String, Object>>();
		}
		String fields = "";
		if (Utils.isEmpty(fieldNames)) {
			fields += "*";
		} else {
			int len = fieldNames.length;
			for (int i = 0; i < len; i++) {
				if (i > 0) {
					fields += ',';
				}
				fields += Utils.toDBName(fieldNames[i]);
			}
		}
		
		SqlBuilder sb = new SqlBuilder("select "+ fields + " from " + Utils.toDBName(type.getSimpleName()) + " where 1 = 1");
		if (Utils.isNotEmpty(paramValues)) {
			Set<Entry<String, Object>> ens = paramValues.entrySet();
			for (Entry<String, Object> en : ens) {
				if (Utils.isNull(en.getValue())) {
					sb.appendStatement(" and " + Utils.toDBName(en.getKey()) + " is null");
				} else {
					sb.appendStatement(" and " + Utils.toDBName(en.getKey()) + " = ").appendParamValue(en.getValue());
				}
			}
		}
		String orderSql = "";
		if (Utils.isNotEmpty(orders)) {
			orderSql += " order by ";
			int len = orders.length;
			for (int i = 0; i < len; i++) {
				if (i > 0) {
					orderSql += ",";
				}
				orderSql += orders[i];
			}
		}
		sb.appendStatement(orderSql);
		return this.getJdbcTemplate().queryForList(sb.toStatement(), sb.toParams());
	}
	
	public <E extends BaseEntity> List<E> listByProp(Class<E> type, String prop, Object value, Order... orders) {
		if (Utils.isNull(type)) {
			return new ArrayList<E>();
		}
		SqlBuilder sb = new SqlBuilder("select * from " + Utils.toDBName(type.getSimpleName()) + " t where 1 = 1");
		if (Utils.isNotBlank(prop)) {
			if (Utils.isNull(value)) {
				sb.appendStatement(" and t." + Utils.toDBName(prop) + " is null");
			} else {
				sb.appendStatement(" and t." + Utils.toDBName(prop) + " = ").appendParamValue(value);
			}
		}
		String orderSql = "";
		if (Utils.isNotEmpty(orders)) {
			orderSql += " order by ";
			int len = orders.length;
			for (int i = 0; i < len; i++) {
				if (i > 0) {
					orderSql += ",";
				}
				orderSql += orders[i];
			}
		}
		sb.appendStatement(orderSql);
		return this.list(type, sb.toStatement(), sb.toParams());
	}
	
	public <E extends BaseEntity> List<E> listByProps(Class<E> type, Map<String, Object> params, Order... orders) {
		if (Utils.isNull(type)) {
			return new ArrayList<E>();
		}
		SqlBuilder sb = new SqlBuilder("select * from " + Utils.toDBName(type.getSimpleName()) + " where 1 = 1");
		if (Utils.isNotEmpty(params)) {
			Set<Entry<String, Object>> ens = params.entrySet();
			for (Entry<String, Object> en : ens) {
				if (Utils.isNull(en.getValue())) {
					sb.appendStatement(" and " + Utils.toDBName(en.getKey()) + " is null");
				} else {
					sb.appendStatement(" and " + Utils.toDBName(en.getKey()) + " = ").appendParamValue(en.getValue());
				}
			}
		}
		String orderSql = "";
		if (Utils.isNotEmpty(orders)) {
			orderSql += " order by ";
			int len = orders.length;
			for (int i = 0; i < len; i++) {
				if (i > 0) {
					orderSql += ",";
				}
				orderSql += orders[i];
			}
		}
		sb.appendStatement(orderSql);
		return this.list(type, sb.toStatement(), sb.toParams());
	}
	
	public <T extends Object, E extends BaseEntity> List<T> listProp(Class<E> type, String prop, Class<T> propType, Map<String, Object> params, Order... orders) {
		if (Utils.isNull(type) || Utils.isBlank(prop)) {
			return new ArrayList<T>();
		}
		SqlBuilder sb = new SqlBuilder("select " + Utils.toDBName(prop.trim()) + " from " + Utils.toDBName(type.getSimpleName()) + " where 1 = 1");
		if (Utils.isNotEmpty(params)) {
			Set<Entry<String, Object>> ens = params.entrySet();
			for (Entry<String, Object> en : ens) {
				if (Utils.isNull(en.getValue())) {
					sb.appendStatement(" and " + Utils.toDBName(en.getKey()) + " is null");
				} else {
					sb.appendStatement(" and " + Utils.toDBName(en.getKey()) + " = ").appendParamValue(en.getValue());
				}
			}
		}
		String orderSql = "";
		if (Utils.isNotEmpty(orders)) {
			orderSql += " order by ";
			int len = orders.length;
			for (int i = 0; i < len; i++) {
				if (i > 0) {
					orderSql += ",";
				}
				orderSql += orders[i];
			}
		}
		sb.appendStatement(orderSql);
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		return jdbcTemplate.queryForList(sb.toStatement(), propType, sb.toParams());
	}
	
	public <E extends BaseEntity> void del(Class<E> type) {
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		jdbcTemplate.execute("delete from " + Utils.toDBName(type.getSimpleName()));
	}
	
	public int executesql(String sql){
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		return jdbcTemplate.update(sql);
	}
	
//	public List<E> execList(String sql){
//		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
//		this.list(type, sb.toStatement(), sb.toParams());
//		return jdbcTemplate.update(sql);
//	}
	
	public <E extends BaseEntity> int del(Class<E> type, String id) {
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		return jdbcTemplate.update("delete from " + Utils.toDBName(type.getSimpleName()) + " where id = ?", id);
	}
	
	public <E extends BaseEntity> int del(Class<E> type, String... ids) {
		if (Utils.isNotEmpty(ids)) {
			SqlBuilder sb = new SqlBuilder("delete from " + Utils.toDBName(type.getSimpleName()) + " where id in").appendParamValues(ids);
			JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
			return jdbcTemplate.update(sb.toStatement(), sb.toParams());
		}
		return 0;
	}
	
	public <E extends BaseEntity> int del(String sql, Object[] params) {
		if (Utils.isBlank(sql)) {
			return 0;
		}
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		return jdbcTemplate.update(sql, params);
	}
	
	public <E extends BaseEntity> int del(Class<E> type, Map<String, Object> paramMap) {
		SqlBuilder sb = new SqlBuilder("delete from " + Utils.toDBName(type.getSimpleName()) + " where 1 = 1");
		if (Utils.isNotEmpty(paramMap)) {
			Set<Entry<String, Object>> ens = paramMap.entrySet();
			for (Entry<String, Object> en : ens) {
				sb.appendStatement(" and " + Utils.toDBName(en.getKey()) + " = ").appendParamValue(en.getValue());
			}
		}
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		return jdbcTemplate.update(sb.toStatement(), sb.toParams());
	}
	
	public <E extends BaseEntity> int count(Class<E> type) {
		if (Utils.isNull(type)) {
			return 0;
		}
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		Number number = jdbcTemplate.queryForObject("select count(*) from " + Utils.toDBName(type.getSimpleName()), null, Integer.class);
		return (number != null ? number.intValue() : 0);
	}
	
	public <E extends BaseEntity> int count(Class<E> type, Map<String, Object> params) {
		if (Utils.isNull(type)) {
			return 0;
		}
		SqlBuilder sb = new SqlBuilder("select count(*) from " + Utils.toDBName(type.getSimpleName()) + " where 1 = 1");
		if (Utils.isNotEmpty(params)) {
			Set<Entry<String, Object>> ens = params.entrySet();
			for (Entry<String, Object> en : ens) {
				sb.appendStatement(" and " + Utils.toDBName(en.getKey()) + " = ").appendParamValue(en.getValue());
			}
		}
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		Number number = jdbcTemplate.queryForObject(sb.toStatement(), sb.toParams(), Integer.class);
		return (number != null ? number.intValue() : 0);
	}
	
	protected int count(String sql, Object... params) {
		if (Utils.isBlank(sql)) {
			return -1;
		}
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		Number number = jdbcTemplate.queryForObject("select count(*) from (" + sql + ") t", params, Integer.class);
		return (number != null ? number.intValue() : 0);
	}
	
	public void execute(String sql) {
		if (Utils.isBlank(sql)) {
			return;
		}
		JdbcTemplate jdbcTemplate = this.getJdbcTemplate();
		jdbcTemplate.execute(sql);
	}
}
