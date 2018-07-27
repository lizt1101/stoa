/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.shutian.oa.modules.oa.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shutian.oa.common.persistence.Page;
import com.shutian.oa.common.service.CrudService;
import com.shutian.oa.modules.oa.entity.OaCostReimDetailed;
import com.shutian.oa.modules.oa.dao.OaCostReimDetailedDao;

/**
 * 费用报销明细Service
 * @author lizt
 * @version 2018-07-18
 */
@Service
@Transactional(readOnly = true)
public class OaCostReimDetailedService extends CrudService<OaCostReimDetailedDao, OaCostReimDetailed> {

	public OaCostReimDetailed get(String id) {
		return super.get(id);
	}
	
	public List<OaCostReimDetailed> findList(OaCostReimDetailed oaCostReimDetailed) {
		return super.findList(oaCostReimDetailed);
	}
	
	public Page<OaCostReimDetailed> findPage(Page<OaCostReimDetailed> page, OaCostReimDetailed oaCostReimDetailed) {
		return super.findPage(page, oaCostReimDetailed);
	}
	
	@Transactional(readOnly = false)
	public void save(OaCostReimDetailed oaCostReimDetailed) {
		super.save(oaCostReimDetailed);
	}
	
	@Transactional(readOnly = false)
	public void delete(OaCostReimDetailed oaCostReimDetailed) {
		super.delete(oaCostReimDetailed);
	}

	public List<OaCostReimDetailed> getByReimId(OaCostReimDetailed oaCostReimDetailed){
		return dao.getByReimId(oaCostReimDetailed);
	}
	
}