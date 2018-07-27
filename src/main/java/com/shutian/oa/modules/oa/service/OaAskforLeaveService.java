/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.shutian.oa.modules.oa.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.google.common.collect.Maps;
import com.shutian.oa.common.utils.StringUtils;
import com.shutian.oa.modules.act.service.ActTaskService;
import com.shutian.oa.modules.act.utils.ActUtils;
import com.shutian.oa.modules.oa.dao.OaMeApplyDao;
import com.shutian.oa.modules.oa.dao.OaOpinionDao;
import com.shutian.oa.modules.oa.entity.*;
import com.shutian.oa.modules.oa.enumeration.LeaveTaskDefKeyEnum;
import com.shutian.oa.modules.oa.enumeration.ReimTaskDefKeyEnum;
import com.shutian.oa.modules.sys.entity.User;
import com.shutian.oa.modules.sys.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shutian.oa.common.persistence.Page;
import com.shutian.oa.common.service.CrudService;
import com.shutian.oa.modules.oa.dao.OaAskforLeaveDao;

/**
 * 请假申请Service
 * @author lizitao
 * @version 2018-07-25
 */
@Service
@Transactional(readOnly = true)
public class OaAskforLeaveService extends CrudService<OaAskforLeaveDao, OaAskforLeave> {

	@Autowired
	private ActTaskService actTaskService;
	@Autowired
	private OaOpinionDao oaOpinionDao;
	@Autowired
	private OaMeApplyDao oaMeApplyDao;

	public OaAskforLeave get(String id) {
		return super.get(id);
	}
	
	public List<OaAskforLeave> findList(OaAskforLeave oaAskforLeave) {
		return super.findList(oaAskforLeave);
	}
	
	public Page<OaAskforLeave> findPage(Page<OaAskforLeave> page, OaAskforLeave oaAskforLeave) {
		return super.findPage(page, oaAskforLeave);
	}
	
	@Transactional(readOnly = false)
	public void save(OaAskforLeave oaAskforLeave) {
		//获取提交人的信息
		User user = UserUtils.get(oaAskforLeave.getToUser().getId());
		if(StringUtils.isBlank(oaAskforLeave.getId())){
			oaAskforLeave.preInsert();
			dao.insert(oaAskforLeave);

			Map<String, Object> vars = Maps.newHashMap();
			vars.put(LeaveTaskDefKeyEnum.DEPTMANAGER.getUser(),user.getLoginName());
			// 启动流程
			String procInsId = actTaskService.startProcess(ActUtils.PD_ASKFOR_LEAVE[0], ActUtils.PD_ASKFOR_LEAVE[1], oaAskforLeave.getId(), oaAskforLeave.getCreateBy().getName()+"请假",vars);
			//记录一下我的申请
			OaMeApply oaMeApply = new OaMeApply();
			oaMeApply.preInsert();
			oaMeApply.setName("请假申请");
			oaMeApply.setStatus("0");
			oaMeApply.setCreateDate(oaAskforLeave.getCreateDate());
			oaMeApply.setUser(new User(oaAskforLeave.getCreateBy().getId()));
			oaMeApply.setProcInsId(procInsId);
			oaMeApplyDao.insert(oaMeApply);
		}else{
			OaAskforLeave oaAskforLeave1 = dao.get(oaAskforLeave.getId());
			User createUser = UserUtils.get(oaAskforLeave1.getCreateBy().getId());
			oaAskforLeave.preUpdate();

			oaAskforLeave.getAct().setComment(("yes".equals(oaAskforLeave.getAct().getFlag())?"[重申] ":"[撤销] ")+createUser.getName()+"请假申请");
			// 完成流程任务
			Map<String, Object> vars = Maps.newHashMap();
			vars.put("pass", "yes".equals(oaAskforLeave.getAct().getFlag())? "0" : "1");
			if("yes".equals(oaAskforLeave.getAct().getFlag())){ //重申
				//设置提交人
				vars.put(LeaveTaskDefKeyEnum.DEPTMANAGER.getUser(),user.getLoginName());
				dao.update(oaAskforLeave);
			}else{  //撤销
				OaMeApply oaMeApply = new OaMeApply();
				oaMeApply.setProcInsId(oaAskforLeave.getAct().getProcInsId());
				oaMeApply.setStatus("2");
				oaMeApplyDao.updateStatus(oaMeApply);
			}
			actTaskService.complete(oaAskforLeave.getAct().getTaskId(), oaAskforLeave.getAct().getProcInsId(), oaAskforLeave.getAct().getComment(), createUser.getName()+"请假", vars);
		}

	}

	@Transactional(readOnly = false)
	public void saveSp(OaAskforLeave oaAskforLeave) {
		oaAskforLeave.preUpdate();
		// 设置意见
		OaOpinion oaOpinion = new OaOpinion();
		oaOpinion.setBusinessTable(ActUtils.PD_ASKFOR_LEAVE[1]);
		oaOpinion.setBussinessId(oaAskforLeave.getId());
		oaOpinion.setUserName(UserUtils.getUser().getName());
		// 对不同环节的业务逻辑进行操作
		String taskDefKey = oaAskforLeave.getAct().getTaskDefKey();
		//设置意见
		List<String> comments = oaAskforLeave.getComments().stream().filter(e->e!=null).collect(Collectors.toList());
		oaAskforLeave.getAct().setComment(("yes".equals(oaAskforLeave.getAct().getFlag())?"[同意] ":"[驳回] ")+comments.get(0));
		// 提交流程任务
		Map<String, Object> vars = Maps.newHashMap();
		vars.put("pass", "yes".equals(oaAskforLeave.getAct().getFlag())? "1" : "0");
		//获取提交给的用户登录名
		User user = new User();
		user.setLoginName("admin"); //发生用户错误时一律提交给系统管理员
		if(oaAskforLeave.getToUser()!=null){
			user = UserUtils.get(oaAskforLeave.getToUser().getId());
		}
		OaOpinion oldOpinion = new OaOpinion();
		//获取当前用户
		User currentUser = UserUtils.getUser();
		if(taskDefKey.equals(LeaveTaskDefKeyEnum.DEPTMANAGER.getName())){ //部门负责人审批环节
			oaOpinion.setContent(comments.get(0)+"["+currentUser.getName()+"]");
			oaOpinion.setLevel(1);
			oaOpinion.setPosition("部门负责人意见");
			oldOpinion.setLevel(1);
			if("yes".equals(oaAskforLeave.getAct().getFlag())){ //同意
				vars.put(LeaveTaskDefKeyEnum.HR.getUser(),user.getLoginName());
			}else{  //驳回
				OaAskforLeave oaAskforLeave1 = dao.get(oaAskforLeave.getId());
				User createUser = UserUtils.get(oaAskforLeave1.getCreateBy().getId());
				vars.put(LeaveTaskDefKeyEnum.APPLY.getUser(),createUser.getLoginName());
			}
		}else if(taskDefKey.equals(LeaveTaskDefKeyEnum.HR.getName())){ //hr审批
			oaOpinion.setContent(comments.get(0)+"["+currentUser.getName()+"]");
			oaOpinion.setLevel(2);
			oaOpinion.setPosition("人事部门意见");
			oldOpinion.setLevel(2);
			if("no".equals(oaAskforLeave.getAct().getFlag())){
				OaAskforLeave oaAskforLeave1 = dao.get(oaAskforLeave.getId());
				User createUser = UserUtils.get(oaAskforLeave1.getCreateBy().getId());
				vars.put(LeaveTaskDefKeyEnum.APPLY.getUser(),createUser.getLoginName());
			}else{
				OaMeApply oaMeApply = new OaMeApply();
				oaMeApply.setProcInsId(oaAskforLeave.getAct().getProcInsId());
				oaMeApply.setStatus("1");
				oaMeApplyDao.updateStatus(oaMeApply);
			}
		}else{
			return;
		}
		//插入或者更新意见
		oldOpinion.setBussinessId(oaAskforLeave.getId());
		OaOpinion oaOpinion1 = oaOpinionDao.getByBussIdAndLevel(oldOpinion);
		if(oaOpinion1==null){
			oaOpinion.preInsert();
			oaOpinionDao.insert(oaOpinion);
		}else{
			oaOpinion.preUpdate();
			oaOpinion.setId(oaOpinion1.getId());
			oaOpinionDao.update(oaOpinion);
		}
		actTaskService.complete(oaAskforLeave.getAct().getTaskId(), oaAskforLeave.getAct().getProcInsId(), oaAskforLeave.getAct().getComment(), vars);
	}
	
	@Transactional(readOnly = false)
	public void delete(OaAskforLeave oaAskforLeave) {
		super.delete(oaAskforLeave);
	}
	
}