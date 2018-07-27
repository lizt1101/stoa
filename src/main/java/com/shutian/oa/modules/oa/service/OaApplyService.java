/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.shutian.oa.modules.oa.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shutian.oa.common.persistence.Page;
import com.shutian.oa.common.service.CrudService;
import com.shutian.oa.modules.oa.entity.OaApply;
import com.shutian.oa.modules.oa.dao.OaApplyDao;

/**
 * 选择申请的流程Service
 * @author lizitao
 * @version 2018-07-17
 */
@Service
@Transactional(readOnly = true)
public class OaApplyService extends CrudService<OaApplyDao, OaApply> {

	public OaApply get(String id) {
		return super.get(id);
	}
	
	public List<OaApply> findList(OaApply oaApply) {
		return super.findList(oaApply);
	}
	
	public Page<OaApply> findPage(Page<OaApply> page, OaApply oaApply) {
		return super.findPage(page, oaApply);
	}
	
	@Transactional(readOnly = false)
	public void save(OaApply oaApply) {
		super.save(oaApply);
	}
	
	@Transactional(readOnly = false)
	public void delete(OaApply oaApply) {
		super.delete(oaApply);
	}
	
}