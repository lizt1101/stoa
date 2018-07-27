/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.shutian.oa.modules.oa.entity;

import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.NotNull;

import com.shutian.oa.common.persistence.DataEntity;

/**
 * oa意见表Entity
 * @author lizt
 * @version 2018-07-18
 */
public class OaOpinion extends DataEntity<OaOpinion> {
	
	private static final long serialVersionUID = 1L;
	private String businessTable;		// 关联数据库表
	private String bussinessId;		// 关联数据库表id
	private String userName;		// 审批意见的用户名
	private String content;		// 审批意见
	private String position;		// 审批流程中的职位
	private Integer level;		// 该执行流程的次序
	
	public OaOpinion() {
		super();
	}

	public OaOpinion(String id){
		super(id);
	}

	@Length(min=1, max=24, message="关联数据库表长度必须介于 1 和 24 之间")
	public String getBusinessTable() {
		return businessTable;
	}

	public void setBusinessTable(String businessTable) {
		this.businessTable = businessTable;
	}
	
	@Length(min=1, max=24, message="关联数据库表id长度必须介于 1 和 24 之间")
	public String getBussinessId() {
		return bussinessId;
	}

	public void setBussinessId(String bussinessId) {
		this.bussinessId = bussinessId;
	}
	
	@Length(min=1, max=24, message="审批意见的用户名长度必须介于 1 和 24 之间")
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	@Length(min=1, max=2048, message="审批意见长度必须介于 1 和 2048 之间")
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	@Length(min=1, max=24, message="审批流程中的职位长度必须介于 1 和 24 之间")
	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}
	
	@NotNull(message="该执行流程的次序不能为空")
	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}
	
}