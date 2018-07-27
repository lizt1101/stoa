/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.shutian.oa.modules.oa.dao;

import com.shutian.oa.common.persistence.CrudDao;
import com.shutian.oa.common.persistence.annotation.MyBatisDao;
import com.shutian.oa.modules.oa.entity.OaAskforLeave;

/**
 * 请假申请DAO接口
 * @author lizitao
 * @version 2018-07-25
 */
@MyBatisDao
public interface OaAskforLeaveDao extends CrudDao<OaAskforLeave> {
	
}