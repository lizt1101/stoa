/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.shutian.oa.modules.gen.dao;

import com.shutian.oa.common.persistence.CrudDao;
import com.shutian.oa.common.persistence.annotation.MyBatisDao;
import com.shutian.oa.modules.gen.entity.GenScheme;

/**
 * 生成方案DAO接口
 * @author ThinkGem
 * @version 2013-10-15
 */
@MyBatisDao
public interface GenSchemeDao extends CrudDao<GenScheme> {
	
}
