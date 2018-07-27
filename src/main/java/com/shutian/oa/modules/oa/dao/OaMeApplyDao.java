/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.shutian.oa.modules.oa.dao;

import com.shutian.oa.common.persistence.CrudDao;
import com.shutian.oa.common.persistence.annotation.MyBatisDao;
import com.shutian.oa.modules.oa.entity.OaMeApply;

import java.util.List;

/**
 * 我的申请DAO接口
 * @author lizitao
 * @version 2018-07-24
 */
@MyBatisDao
public interface OaMeApplyDao extends CrudDao<OaMeApply> {

    List<OaMeApply> getByTableInfo(OaMeApply oaMeApply);

    void updateStatus(OaMeApply oaMeApply);
	
}