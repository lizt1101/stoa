/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.shutian.oa.modules.oa.dao;

import com.shutian.oa.common.persistence.CrudDao;
import com.shutian.oa.common.persistence.annotation.MyBatisDao;
import com.shutian.oa.modules.oa.entity.OaCostReimDetailed;
import com.shutian.oa.modules.oa.entity.OaCostReimbursement;

/**
 * 费用报销DAO接口
 * @author lizt
 * @version 2018-07-18
 */
@MyBatisDao
public interface OaCostReimbursementDao extends CrudDao<OaCostReimbursement> {

    void updateStatus(OaCostReimbursement oaCostReimbursement);
	
}