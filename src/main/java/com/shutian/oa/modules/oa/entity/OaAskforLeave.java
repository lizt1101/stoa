/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.shutian.oa.modules.oa.entity;

import com.shutian.oa.common.persistence.ActEntity;
import com.shutian.oa.modules.sys.entity.User;
import org.hibernate.validator.constraints.Length;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import com.shutian.oa.common.persistence.DataEntity;

/**
 * 请假申请Entity
 * @author lizitao
 * @version 2018-07-25
 */
public class OaAskforLeave extends ActEntity<OaAskforLeave> {
	
	private static final long serialVersionUID = 1L;
	private String procInsId;		// 流程实例编号
	private Date startTime;		// 开始时间
	private Date endTime;		// 结束时间
	private String leaveType;		// 请假类型
	private String reason;		// 请假理由
	private String applyTime;		// 申请时长
	private Date beginApplyTime;		// 开始 申请时间
	private Date endApplyTime;		// 结束 申请时间

	private List<OaOpinion> oaOpinions;  //意见
    private User toUser;  //提交人
    private List<String> comments; //意见
	
	public OaAskforLeave() {
		super();
	}

	public OaAskforLeave(String id){
		super(id);
	}

	@Length(min=0, max=64, message="流程实例编号长度必须介于 0 和 64 之间")
	public String getProcInsId() {
		return procInsId;
	}

	public void setProcInsId(String procInsId) {
		this.procInsId = procInsId;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	
	@Length(min=0, max=20, message="请假类型长度必须介于 0 和 20 之间")
	public String getLeaveType() {
		return leaveType;
	}

	public void setLeaveType(String leaveType) {
		this.leaveType = leaveType;
	}
	
	@Length(min=0, max=255, message="请假理由长度必须介于 0 和 255 之间")
	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

    public String getApplyTime() {
        return applyTime;
    }

    public void setApplyTime(String applyTime) {
        this.applyTime = applyTime;
    }

    public Date getBeginApplyTime() {
		return beginApplyTime;
	}

	public void setBeginApplyTime(Date beginApplyTime) {
		this.beginApplyTime = beginApplyTime;
	}
	
	public Date getEndApplyTime() {
		return endApplyTime;
	}

	public void setEndApplyTime(Date endApplyTime) {
		this.endApplyTime = endApplyTime;
	}

	public List<OaOpinion> getOaOpinions() {
		return oaOpinions;
	}

	public void setOaOpinions(List<OaOpinion> oaOpinions) {
		this.oaOpinions = oaOpinions;
	}

    public User getToUser() {
        return toUser;
    }

    public void setToUser(User toUser) {
        this.toUser = toUser;
    }

    public List<String> getComments() {
        return comments;
    }

    public void setComments(List<String> comments) {
        this.comments = comments;
    }
}