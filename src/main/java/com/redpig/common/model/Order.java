package com.redpig.common.model;


import com.redpig.common.utils.Utils;

/**
 * 排序工具类
 * @author 邓小林
 * 创建时间：2016年4月12日
 */
public class Order {
	private static final String asc = "asc";
	private static final String desc = "desc";
	private String order;
	
	public Order(String order) {
		this.order = order;
	}
	
	public static Order desc(String field) {
		return new Order(Utils.toDBName(field) + " " + desc);
	}
	
	public static Order asc(String field) {
		return new Order(Utils.toDBName(field) + " " + asc);
	}

	public String getOrder() {
		return order;
	}

	@Override
	public String toString() {
		return this.getOrder();
	}
}
