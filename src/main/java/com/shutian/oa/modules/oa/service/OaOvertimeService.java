/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.shutian.oa.modules.oa.service;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Maps;
import com.shutian.oa.common.utils.StringUtils;
import com.shutian.oa.modules.act.service.ActTaskService;
import com.shutian.oa.modules.act.utils.ActUtils;
import com.shutian.oa.modules.oa.dao.OaOpinionDao;
import com.shutian.oa.modules.oa.entity.OaOpinion;
import com.shutian.oa.modules.oa.enumeration.OvertimeTaskDefKeyEnum;
import com.shutian.oa.modules.sys.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shutian.oa.common.persistence.Page;
import com.shutian.oa.common.service.CrudService;
import com.shutian.oa.modules.oa.entity.OaOvertime;
import com.shutian.oa.modules.oa.dao.OaOvertimeDao;

/**
 * 加班申请Service
 * @author lizt
 * @version 2018-07-17
 */
@Service
@Transactional(readOnly = true)
public class OaOvertimeService extends CrudService<OaOvertimeDao, OaOvertime> {

	@Autowired
	private ActTaskService actTaskService;

	@Autowired
	private OaOpinionDao oaOpinionDao;

	public OaOvertime get(String id) {
		return super.get(id);
	}
	
	public List<OaOvertime> findList(OaOvertime oaOvertime) {
		return super.findList(oaOvertime);
	}
	
	public Page<OaOvertime> findPage(Page<OaOvertime> page, OaOvertime oaOvertime) {
		return super.findPage(page, oaOvertime);
	}
	
	@Transactional(readOnly = false)
	public void save(OaOvertime oaOvertime) {
		if (StringUtils.isBlank(oaOvertime.getId())){
			oaOvertime.preInsert();
			dao.insert(oaOvertime);
			Map<String, Object> vars = Maps.newHashMap();
			vars.put("dept","tangs");
			// 启动流程
			actTaskService.startProcess(ActUtils.PD_OVERTIME[0], ActUtils.PD_OVERTIME[1], oaOvertime.getId(), oaOvertime.getTitle(),vars);
		}
		// 重新编辑申请
		else{
			oaOvertime.preUpdate();
			dao.update(oaOvertime);

			oaOvertime.getAct().setComment(("yes".equals(oaOvertime.getAct().getFlag())?"[重申] ":"[销毁] ")+oaOvertime.getAct().getComment());
			// 完成流程任务
			Map<String, Object> vars = Maps.newHashMap();
			vars.put("pass", "yes".equals(oaOvertime.getAct().getFlag())? "1" : "0");
			if("yes".equals(oaOvertime.getAct().getFlag())){
				vars.put("dept","tangs");
			}
			actTaskService.complete(oaOvertime.getAct().getTaskId(), oaOvertime.getAct().getProcInsId(), oaOvertime.getAct().getComment(), oaOvertime.getTitle(), vars);
		}
	}

	@Transactional(readOnly = false)
	public void saveOpinion(OaOvertime oaOvertime) {
		// 设置意见
		OaOpinion oaOpinion = new OaOpinion();
		oaOpinion.setContent(oaOvertime.getAct().getComment());
		oaOvertime.getAct().setComment(("yes".equals(oaOvertime.getAct().getFlag())?"[同意] ":"[驳回] ")+oaOvertime.getAct().getComment());
		oaOvertime.preUpdate();
		oaOpinion.preInsert();
		oaOpinion.setBusinessTable(ActUtils.PD_OVERTIME[1]);
		oaOpinion.setBussinessId(oaOvertime.getId());
		oaOpinion.setUserName(UserUtils.getUser().getName());
		// 对不同环节的业务逻辑进行操作
		String taskDefKey = oaOvertime.getAct().getTaskDefKey();

		// 提交流程任务
		Map<String, Object> vars = Maps.newHashMap();
		vars.put("pass", "yes".equals(oaOvertime.getAct().getFlag())? "1" : "0");
		if(taskDefKey.equals(OvertimeTaskDefKeyEnum.DEPTMANAGER.getName())){ //部门领导审批环节
			oaOpinion.setLevel(1);
			oaOpinion.setPosition("部门领导");
			oaOpinionDao.insert(oaOpinion);
			if("yes".equals(oaOvertime.getAct().getFlag())){
				vars.put("hr","pus");
			}else{
				OaOvertime oaOvertime1 = dao.get(oaOvertime.getId());
				vars.put("apply",oaOvertime1.getCreateBy().getName());
			}
		}else if(taskDefKey.equals(OvertimeTaskDefKeyEnum.HR.getName())){ //hr审批
			oaOpinion.setLevel(2);
			oaOpinion.setPosition("人事");
			oaOpinionDao.insert(oaOpinion);
			if("no".equals(oaOvertime.getAct().getFlag())){
				OaOvertime oaOvertime1 = dao.get(oaOvertime.getId());
				vars.put("apply",oaOvertime1.getCreateBy().getName());
			}
		}else{
			return;
		}
		actTaskService.complete(oaOvertime.getAct().getTaskId(), oaOvertime.getAct().getProcInsId(), oaOvertime.getAct().getComment(), vars);
	}
	
	@Transactional(readOnly = false)
	public void delete(OaOvertime oaOvertime) {
		super.delete(oaOvertime);
	}
	
}