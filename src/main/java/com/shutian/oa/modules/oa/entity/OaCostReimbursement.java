/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.shutian.oa.modules.oa.entity;

import com.shutian.oa.common.persistence.ActEntity;
import com.shutian.oa.modules.sys.entity.Office;
import com.shutian.oa.modules.sys.entity.User;
import org.hibernate.validator.constraints.Length;

import com.shutian.oa.common.persistence.DataEntity;

import java.util.List;

/**
 * 费用报销Entity
 * @author lizt
 * @version 2018-07-18
 */
public class OaCostReimbursement extends ActEntity<OaCostReimbursement> {
	
	private static final long serialVersionUID = 1L;
	private User user;	//	报销人
	private Office office;	//	归属部门
	/*private String reimName;		// 报销人
	private String reimDepartment;		// 部门
	private String reimId;		// 报销人id*/
	private String reimPosition;		// 报销人职位
	private String totalMoney;		// 合计金额(数字)
	private String totalBigMoney;		// 合计金额（汉字大写）
	private String number; //编号
	private String procInsId;  //流程id
	private Integer status; //0-进行中1-已完成2-已撤销

	private List<OaCostReimDetailed> oaCostReimDetaileds; //报销费用明细
    private User toUser; //被提交人
	private List<OaOpinion> OaOpinions;  //意见
	private Integer CostCount; //该费用明细总数量
	private List<String> comments; //审批意见


	public List<String> getComments() {
		return comments;
	}

	public void setComments(List<String> comments) {
		this.comments = comments;
	}

	public Integer getCostCount() {
		return CostCount;
	}

	public void setCostCount(Integer costCount) {
		CostCount = costCount;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public User getToUser() {
        return toUser;
    }

    public void setToUser(User toUser) {
        this.toUser = toUser;
    }

    public OaCostReimbursement() {
		super();
	}

	public List<OaCostReimDetailed> getOaCostReimDetaileds() {
		return oaCostReimDetaileds;
	}

	public void setOaCostReimDetaileds(List<OaCostReimDetailed> oaCostReimDetaileds) {
		this.oaCostReimDetaileds = oaCostReimDetaileds;
	}

	public OaCostReimbursement(String id){
		super(id);
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getProcInsId() {
		return procInsId;
	}

	public void setProcInsId(String procInsId) {
		this.procInsId = procInsId;
	}


	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Office getOffice() {
		return office;
	}

	public void setOffice(Office office) {
		this.office = office;
	}

	@Length(min=1, max=24, message="报销人职位长度必须介于 1 和 24 之间")
	public String getReimPosition() {
		return reimPosition;
	}

	public void setReimPosition(String reimPosition) {
		this.reimPosition = reimPosition;
	}
	
	@Length(min=1, max=24, message="合计金额(数字)长度必须介于 1 和 24 之间")
	public String getTotalMoney() {
		return totalMoney;
	}

	public void setTotalMoney(String totalMoney) {
		this.totalMoney = totalMoney;
	}
	
	@Length(min=1, max=64, message="合计金额（汉字大写）长度必须介于 1 和 64 之间")
	public String getTotalBigMoney() {
		return totalBigMoney;
	}

	public void setTotalBigMoney(String totalBigMoney) {
		this.totalBigMoney = totalBigMoney;
	}

	public List<OaOpinion> getOaOpinions() {
		return OaOpinions;
	}

	public void setOaOpinions(List<OaOpinion> oaOpinions) {
		OaOpinions = oaOpinions;
	}
}