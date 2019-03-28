package com.redpig.common.entity;

import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;

/**
 * 实体基类
 */
public class BaseEntity implements Serializable {
	private static final long serialVersionUID = -3591130134414302785L;

	/**
	 * 主键ID
	 */
	private String id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public String toString() {
		return JSONObject.toJSONString(this);
	}
}
