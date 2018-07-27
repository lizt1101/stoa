/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.shutian.oa.modules.oa.dao;

import com.shutian.oa.common.persistence.CrudDao;
import com.shutian.oa.common.persistence.annotation.MyBatisDao;
import com.shutian.oa.modules.oa.entity.OaOvertime;

/**
 * 加班申请DAO接口
 * @author lizt
 * @version 2018-07-17
 */
@MyBatisDao
public interface OaOvertimeDao extends CrudDao<OaOvertime> {
	
}