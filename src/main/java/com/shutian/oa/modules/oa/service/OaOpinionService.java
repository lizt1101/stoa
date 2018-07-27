/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.shutian.oa.modules.oa.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shutian.oa.common.persistence.Page;
import com.shutian.oa.common.service.CrudService;
import com.shutian.oa.modules.oa.entity.OaOpinion;
import com.shutian.oa.modules.oa.dao.OaOpinionDao;

/**
 * oa意见表Service
 * @author lizt
 * @version 2018-07-18
 */
@Service
@Transactional(readOnly = true)
public class OaOpinionService extends CrudService<OaOpinionDao, OaOpinion> {

	public OaOpinion get(String id) {
		return super.get(id);
	}
	
	public List<OaOpinion> findList(OaOpinion oaOpinion) {
		return super.findList(oaOpinion);
	}
	
	public Page<OaOpinion> findPage(Page<OaOpinion> page, OaOpinion oaOpinion) {
		return super.findPage(page, oaOpinion);
	}
	
	@Transactional(readOnly = false)
	public void save(OaOpinion oaOpinion) {
		super.save(oaOpinion);
	}
	
	@Transactional(readOnly = false)
	public void delete(OaOpinion oaOpinion) {
		super.delete(oaOpinion);
	}

	public List<OaOpinion> getCommentListByBussinessId(OaOpinion oaOpinion){
		return dao.getCommentListByBussinessId(oaOpinion);
	}

	public OaOpinion getByBussIdAndLevel(OaOpinion oaOpinion){
		return dao.getByBussIdAndLevel(oaOpinion);
	}
	
}