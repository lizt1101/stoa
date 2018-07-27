/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.shutian.oa.modules.oa.service;

import java.util.List;

import com.google.common.collect.Lists;
import com.shutian.oa.modules.act.entity.Act;
import com.shutian.oa.modules.act.service.ActTaskService;
import com.shutian.oa.modules.act.utils.ProcessDefCache;
import com.shutian.oa.modules.oa.dao.OaCostReimbursementDao;
import com.shutian.oa.modules.sys.entity.User;
import com.shutian.oa.modules.sys.utils.UserUtils;
import org.activiti.engine.HistoryService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shutian.oa.common.persistence.Page;
import com.shutian.oa.common.service.CrudService;
import com.shutian.oa.modules.oa.entity.OaMeApply;
import com.shutian.oa.modules.oa.dao.OaMeApplyDao;

/**
 * 我的申请Service
 * @author lizitao
 * @version 2018-07-24
 */
@Service
@Transactional(readOnly = true)
public class OaMeApplyService extends CrudService<OaMeApplyDao, OaMeApply> {

	@Autowired
	private OaCostReimbursementDao oaCostReimbursementDao;
	@Autowired
	private ActTaskService actTaskService;
	@Autowired
	private TaskService taskService;
	@Autowired
	private HistoryService historyService;

	public OaMeApply get(String id) {
		return super.get(id);
	}
	
	public List<OaMeApply> findList(OaMeApply oaMeApply) {
		return super.findList(oaMeApply);
	}
	
	public Page<OaMeApply> findPage(Page<OaMeApply> page, OaMeApply oaMeApply) {
		oaMeApply.setUser(new User(UserUtils.getUser().getId()));
		Page<OaMeApply> page1 = super.findPage(page, oaMeApply);
		List<OaMeApply> oaMeApplies = page1.getList();
		for (OaMeApply oaMe:oaMeApplies) {
			HistoricProcessInstance hisProcIns=actTaskService.getHisProcIns(oaMe.getProcInsId());
			oaMe.getAct().setHisProcIns(hisProcIns);
			List<HistoricTaskInstance> tasks = historyService.createHistoricTaskInstanceQuery()
					.orderByHistoricTaskInstanceStartTime().asc().processInstanceId(oaMe.getProcInsId()).list();
			if(tasks.size()>0){
				HistoricTaskInstance historicTaskInstance = tasks.get(tasks.size()-1);
				oaMe.getAct().setHistTask(historicTaskInstance);
				oaMe.getAct().setProcDef(ProcessDefCache.get(historicTaskInstance.getProcessDefinitionId()));
				oaMe.getAct().setVars(historicTaskInstance.getProcessVariables());
			}
		}
		return page1;
	}
	
	@Transactional(readOnly = false)
	public void save(OaMeApply oaMeApply) {
		super.save(oaMeApply);
	}
	
	@Transactional(readOnly = false)
	public void delete(OaMeApply oaMeApply) {
		super.delete(oaMeApply);
	}
	
}