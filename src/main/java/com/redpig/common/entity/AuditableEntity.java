package com.redpig.common.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.redpig.common.enums.Nums;
import com.redpig.common.json.JsonDateTimeSerializer;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import java.util.Date;

/**
 * 可审计实体基类
 * @author 邓小林
 * 创建时间：2016年4月12日
 */
public class AuditableEntity extends BaseEntity {
	private static final long serialVersionUID = 4568163898243894660L;
	private String accountId; // 用户ID
	private Integer state = Nums.ONE.getNum(); // 状态：1=启用，0=停用
	@JsonSerialize(using=JsonDateTimeSerializer.class)
	@DateTimeFormat(iso=ISO.DATE_TIME, pattern="yyyy-MM-dd HH:mm:ss")
	private Date createdTime; // 创建时间
	@JsonSerialize(using=JsonDateTimeSerializer.class)
	@DateTimeFormat(iso=ISO.DATE_TIME, pattern="yyyy-MM-dd HH:mm:ss")
	private Date updatedTime; // 更新时间
	public String getAccountId() {
		return accountId;
	}
	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
	public Date getCreatedTime() {
		return createdTime;
	}
	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}
	public Date getUpdatedTime() {
		return updatedTime;
	}
	public void setUpdatedTime(Date updatedTime) {
		this.updatedTime = updatedTime;
	}
}
