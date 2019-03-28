package com.redpig.common.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 分页模型
 * @author 邓小林
 * @param <T>
 */
public class Page<T> implements Serializable {
	private static final long serialVersionUID = 1856344655202334879L;
	public static final int PAGE_NO = 1;
	public static final int PAGE_SIZE = 20;
	
	private int pageNo;
	private int pageSize;
	private long total;
	private List<T> rows;
	
	public Page(int total) {
		this(PAGE_NO, PAGE_SIZE, total);
	}
	
	public Page(int pageSize, long total) {
		this(PAGE_NO, pageSize, total, null);
	}
	
	public Page(int pageNo, int pageSize, long total) {
		this(pageNo, pageSize, total, null);
	}
	
	public Page(int pageNo, int pageSize, long total, List<T> page) {
		this.pageNo = pageNo;
		this.pageSize = pageSize;
		this.total = total;
		this.rows = page;
	}
	
	public long getPageCount() {
		return this.total == 0 ? 0 : this.total / this.pageSize + (this.total % this.pageSize == 0 ? 0 : 1);
	}
	
	public int getOffset() {
		return (this.pageNo - 1) * this.pageSize;
	}
	
	public int getPageNo() {
		return this.pageNo;
	}
	
	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}
	
	public int getPageSize() {
		return this.pageSize;
	}
	
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	
	public long getTotal() {
		return this.total;
	}
	
	public void setTotal(long total) {
		this.total = total;
	}
	
	public List<T> getRows() {
		return this.rows == null ? new ArrayList<T>() : this.rows;
	}
	
	public void setRows(List<T> rows) {
		this.rows = rows;
	}
	
	public boolean hasPre() {
		return this.pageNo > PAGE_NO;
	}
	
	public boolean hasNext() {
		return this.pageNo < this.getPageCount();
	}
}
