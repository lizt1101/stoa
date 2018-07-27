/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.shutian.oa.modules.oa.entity;

import com.shutian.oa.common.persistence.ActEntity;
import com.shutian.oa.modules.sys.entity.User;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 我的申请Entity记录一下用户的流程
 * @author lizitao
 * @version 2018-07-24
 */
public class OaMeApply extends ActEntity<OaMeApply> {
	
	private static final long serialVersionUID = 1L;
	private User user;		// 用户id
	private String name;		// 申请名称
	private String procInsId;		// 流程id
	private Date beginCreateDate;		// 开始 创建时间
	private Date endCreateDate;		// 结束 创建时间
	private String status;// 0-进行中1-已完成2-流程中断

	
	public OaMeApply() {
		super();
	}

	public OaMeApply(String id){
		super(id);
	}

	@NotNull(message="用户id不能为空")
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	@Length(min=1, max=24, message="申请名称长度必须介于 1 和 24 之间")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

    @Override
    public String getProcInsId() {
        return procInsId;
    }

    @Override
    public void setProcInsId(String procInsId) {
        this.procInsId = procInsId;
    }

    public Date getBeginCreateDate() {
		return beginCreateDate;
	}

	public void setBeginCreateDate(Date beginCreateDate) {
		this.beginCreateDate = beginCreateDate;
	}
	
	public Date getEndCreateDate() {
		return endCreateDate;
	}

	public void setEndCreateDate(Date endCreateDate) {
		this.endCreateDate = endCreateDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}