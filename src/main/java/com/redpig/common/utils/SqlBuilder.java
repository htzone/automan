package com.redpig.common.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * SQL拼接工具类
 * SqlBuilder sb = new SqlBuilder("select * from test where 1 = 1");
 * sb.appendStatement(" and id = ").appendParamValue(1);
 * sb.appendStatement(" and name like ").appendParamValue('%name');
 * sb.appendStatement(" and state in ").appendParamValues(new int[]{1, 2, 3});
 */
public class SqlBuilder {
	protected final StringBuilder statement = new StringBuilder(512);
	protected final List<Object> params = new ArrayList<Object>();
	
	public SqlBuilder() {
		this.statement.setLength(0);
	}
	
	public SqlBuilder(String state) {
		this.statement.setLength(0);
		this.statement.append(state);
	}
	
	/**
	 * 追加SQL片段
	 */
	public SqlBuilder appendStatement(String statement) {
		this.statement.append(statement);
		return this;
	}
	
	/**
	 * 追加参数值
	 */
	public SqlBuilder appendParamValue(Object paramValue) {
		this.statement.append('?');
		this.params.add(paramValue);
		return this;
	}
	
	/**
	 * 追加参数值数组
	 */
	public SqlBuilder appendParamValues(Object... paramValues) {
		if (Utils.isNotEmpty(paramValues)) {
			this.statement.append('(');
			for (int i = 0; i < paramValues.length; i++) {
				if (i > 0) {
					this.statement.append(", ");
				}
				this.statement.append('?');
				this.params.add(paramValues[i]);
			}
			this.statement.append(')');
		}
		return this;
	}
	
	/**
	 * 返回拼接的SQL
	 */
	public String toStatement() {
		return this.statement.toString();
	}
	
	/**
	 * 返回参数数组
	 */
	public Object[] toParams() {
		return this.params.toArray(new Object[0]);
	}

	@Override
	public String toString() {
		return this.statement.toString();
	}
}
