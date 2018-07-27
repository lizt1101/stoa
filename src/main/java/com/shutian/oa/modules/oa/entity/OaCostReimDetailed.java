/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.shutian.oa.modules.oa.entity;

import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.NotNull;

import com.shutian.oa.common.persistence.DataEntity;

/**
 * 费用报销明细Entity
 * @author lizt
 * @version 2018-07-18
 */
public class OaCostReimDetailed extends DataEntity<OaCostReimDetailed> {
	
	private static final long serialVersionUID = 1L;
	private String reimId;		// 费用报销表id
	private String summary;		// 摘要
	private String project;		// 项目
	private String money;		// 金额
	private Integer billCount;		// 单据数量
	private Integer isDelete; //是否删除 0-未删除1-删除
	
	public OaCostReimDetailed() {
		super();
	}

	public OaCostReimDetailed(String id){
		super(id);
	}

	@Length(min=1, max=64, message="费用报销表id长度必须介于 1 和 64 之间")
	public String getReimId() {
		return reimId;
	}

	public void setReimId(String reimId) {
		this.reimId = reimId;
	}
	
	@Length(min=1, max=255, message="摘要长度必须介于 1 和 255 之间")
	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}
	
	@Length(min=1, max=34, message="项目长度必须介于 1 和 34 之间")
	public String getProject() {
		return project;
	}

	public void setProject(String project) {
		this.project = project;
	}
	
	@Length(min=1, max=24, message="金额长度必须介于 1 和 24 之间")
	public String getMoney() {
		return money;
	}

	public void setMoney(String money) {
		this.money = money;
	}
	
	@NotNull(message="单据数量不能为空")
	public Integer getBillCount() {
		return billCount;
	}

	public void setBillCount(Integer billCount) {
		this.billCount = billCount;
	}

	public static boolean isNUll(OaCostReimDetailed oaCostReimDetailed){
		if(oaCostReimDetailed.getSummary()==null || "".equals(oaCostReimDetailed.getSummary().trim())){
			return false;
		}
		if(oaCostReimDetailed.getProject()==null || "".equals(oaCostReimDetailed.getProject().trim())){
			return false;
		}
		if(oaCostReimDetailed.getMoney()==null || "".equals(oaCostReimDetailed.getMoney().trim())){
			return false;
		}
		if(oaCostReimDetailed.getBillCount()==null || "".equals(oaCostReimDetailed.getBillCount().toString().trim()) || oaCostReimDetailed.getBillCount()==0){
			return false;
		}
		return true;
	}

	public Integer getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(Integer isDelete) {
		this.isDelete = isDelete;
	}
}