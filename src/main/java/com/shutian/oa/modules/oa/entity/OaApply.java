/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.shutian.oa.modules.oa.entity;

import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.NotNull;

import com.shutian.oa.common.persistence.DataEntity;

/**
 * 选择申请的流程Entity
 * @author lizitao
 * @version 2018-07-17
 */
public class OaApply extends DataEntity<OaApply> {
	
	private static final long serialVersionUID = 1L;
	private String name;		// 申请名称
	private String url;		// 申请的跳转链接
	private Integer type;		// 申请所属类别1-人事管理2-费用相关3-日常工作4-服务管理5-合同管理6-资产管理7-销售管理8-公文管理9-行政管理10项目管理
	
	public OaApply() {
		super();
	}

	public OaApply(String id){
		super(id);
	}

	public OaApply(String id, Integer type) {
		super(id);
		this.type = type;
	}

	@Length(min=1, max=24, message="申请名称长度必须介于 1 和 24 之间")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Length(min=1, max=64, message="申请的跳转链接长度必须介于 1 和 64 之间")
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	@NotNull(message="申请所属类别1-人事管理2-费用相关3-日常工作4-服务管理5-合同管理6-资产管理7-销售管理8-公文管理9-行政管理10项目管理不能为空")
	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}
	
}