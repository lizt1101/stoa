/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.shutian.oa.modules.oa.service;

import java.nio.file.OpenOption;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import com.google.common.collect.Maps;
import com.shutian.oa.common.utils.StringUtils;
import com.shutian.oa.modules.act.service.ActTaskService;
import com.shutian.oa.modules.act.utils.ActUtils;
import com.shutian.oa.modules.oa.dao.OaCostReimDetailedDao;
import com.shutian.oa.modules.oa.dao.OaMeApplyDao;
import com.shutian.oa.modules.oa.dao.OaOpinionDao;
import com.shutian.oa.modules.oa.entity.*;
import com.shutian.oa.modules.oa.enumeration.OvertimeTaskDefKeyEnum;
import com.shutian.oa.modules.oa.enumeration.ReimTaskDefKeyEnum;
import com.shutian.oa.modules.sys.entity.User;
import com.shutian.oa.modules.sys.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shutian.oa.common.persistence.Page;
import com.shutian.oa.common.service.CrudService;
import com.shutian.oa.modules.oa.dao.OaCostReimbursementDao;

/**
 * 费用报销Service
 * @author lizt
 * @version 2018-07-18
 */
@Service
@Transactional(readOnly = true)
public class OaCostReimbursementService extends CrudService<OaCostReimbursementDao, OaCostReimbursement> {
	@Autowired
	private OaCostReimDetailedDao oaCostReimDetailedDao;
	@Autowired
	private ActTaskService actTaskService;
	@Autowired
	private OaOpinionDao oaOpinionDao;
	@Autowired
	private OaMeApplyDao oaMeApplyDao;

	public OaCostReimbursement get(String id) {
		return super.get(id);
	}
	
	public List<OaCostReimbursement> findList(OaCostReimbursement oaCostReimbursement) {
		return super.findList(oaCostReimbursement);
	}
	
	public Page<OaCostReimbursement> findPage(Page<OaCostReimbursement> page, OaCostReimbursement oaCostReimbursement) {
		return super.findPage(page, oaCostReimbursement);
	}
	
	@Transactional(readOnly = false)
	public void save(OaCostReimbursement oaCostReimbursement) {
		//获取提交人的信息
		User user = UserUtils.get(oaCostReimbursement.getToUser().getId());
		if(StringUtils.isBlank(oaCostReimbursement.getId())){
			oaCostReimbursement.preInsert();
			oaCostReimbursement.setStatus(0);
			dao.insert(oaCostReimbursement);
			List<OaCostReimDetailed> list = oaCostReimbursement.getOaCostReimDetaileds();
			list.forEach(e->{
                e.preInsert();
                e.setIsDelete(0);
                e.setReimId(oaCostReimbursement.getId());
                oaCostReimDetailedDao.insert(e);
            });

			Map<String, Object> vars = Maps.newHashMap();
			vars.put(ReimTaskDefKeyEnum.DEPARTMENT.getUser(),user.getLoginName());
			// 启动流程
			String procInsId = actTaskService.startProcess(ActUtils.PD_COST_REIM[0], ActUtils.PD_COST_REIM[1], oaCostReimbursement.getId(), oaCostReimbursement.getCreateBy().getName()+"费用报销",vars);
			//记录一下我的申请
			OaMeApply oaMeApply = new OaMeApply();
			oaMeApply.preInsert();
			oaMeApply.setName("费用报销");
			oaMeApply.setStatus("0");
			oaMeApply.setCreateDate(oaCostReimbursement.getCreateDate());
			oaMeApply.setUser(new User(oaCostReimbursement.getCreateBy().getId()));
			oaMeApply.setProcInsId(procInsId);
			oaMeApplyDao.insert(oaMeApply);
		}else{
			OaCostReimbursement oaCostReimbursement1 = dao.get(oaCostReimbursement.getId());
			User createUser = UserUtils.get(oaCostReimbursement1.getCreateBy().getId());
			oaCostReimbursement.preUpdate();

			oaCostReimbursement.getAct().setComment(("yes".equals(oaCostReimbursement.getAct().getFlag())?"[重申] ":"[撤销] ")+createUser.getName()+"费用报销申请");
			// 完成流程任务
			Map<String, Object> vars = Maps.newHashMap();
			vars.put("pass", "yes".equals(oaCostReimbursement.getAct().getFlag())? "1" : "0");
			if("yes".equals(oaCostReimbursement.getAct().getFlag())){ //重申
				//更新或者添加费用明细
				OaCostReimDetailed oaCostReimDetailed = new OaCostReimDetailed();
				oaCostReimDetailed.setReimId(oaCostReimbursement.getId());
				List<OaCostReimDetailed> oldList = oaCostReimDetailedDao.getByReimId(oaCostReimDetailed);
				List<String> detailIds = oldList.stream().map(e-> {
					return e.getId();
				}).collect(Collectors.toList());
				List<OaCostReimDetailed> list = oaCostReimbursement.getOaCostReimDetaileds().stream().filter(e->e.getMoney()!=null).collect(Collectors.toList());
				for (OaCostReimDetailed detail:list) {
					if(StringUtils.isBlank(detail.getId())){  //插入
						detail.preInsert();
						detail.setIsDelete(0);
						detail.setReimId(oaCostReimbursement.getId());
						oaCostReimDetailedDao.insert(detail);
					}else{ //更新
						detail.preUpdate();
						detail.setIsDelete(0);
						detail.setReimId(oaCostReimbursement.getId());
						oaCostReimDetailedDao.update(detail);
						detailIds.remove(detail.getId());
					}
				}
				//逻辑删除原先的费用明细
				if(detailIds.size()>0){
					for (String s:detailIds){
						OaCostReimDetailed detailed = new OaCostReimDetailed();
						detailed.setId(s);
						oaCostReimDetailedDao.delete(detailed);
					}
				}
				//设置提交人
				vars.put(ReimTaskDefKeyEnum.DEPARTMENT.getUser(),user.getLoginName());
				dao.update(oaCostReimbursement);
			}else{  //撤销
				oaCostReimbursement.preUpdate();
				oaCostReimbursement.setStatus(2);
				dao.updateStatus(oaCostReimbursement);
				OaMeApply oaMeApply = new OaMeApply();
				oaMeApply.setProcInsId(oaCostReimbursement.getAct().getProcInsId());
				oaMeApply.setStatus("2");
				oaMeApplyDao.updateStatus(oaMeApply);
			}
			actTaskService.complete(oaCostReimbursement.getAct().getTaskId(), oaCostReimbursement.getAct().getProcInsId(), oaCostReimbursement.getAct().getComment(), createUser.getName()+"费用报销", vars);
		}


	}

	@Transactional(readOnly = false)
	public void saveSp(OaCostReimbursement oaCostReimbursement) {
		oaCostReimbursement.preUpdate();
		// 设置意见
		OaOpinion oaOpinion = new OaOpinion();
		oaOpinion.setBusinessTable(ActUtils.PD_COST_REIM[1]);
		oaOpinion.setBussinessId(oaCostReimbursement.getId());
		oaOpinion.setUserName(UserUtils.getUser().getName());
		// 对不同环节的业务逻辑进行操作
		String taskDefKey = oaCostReimbursement.getAct().getTaskDefKey();
		//设置意见
		List<String> comments = oaCostReimbursement.getComments().stream().filter(e->e!=null).collect(Collectors.toList());
		oaCostReimbursement.getAct().setComment(("yes".equals(oaCostReimbursement.getAct().getFlag())?"[同意] ":"[驳回] ")+comments.get(0));
		// 提交流程任务
		Map<String, Object> vars = Maps.newHashMap();
		vars.put("pass", "yes".equals(oaCostReimbursement.getAct().getFlag())? "1" : "0");
		//获取提交给的用户登录名
		User user = new User();
		user.setLoginName("admin"); //发生用户错误时一律提交给系统管理员
		if(oaCostReimbursement.getToUser()!=null){
			user = UserUtils.get(oaCostReimbursement.getToUser().getId());
		}
		OaOpinion oldOpinion = new OaOpinion();
		//获取当前用户
		User currentUser = UserUtils.getUser();
		if(taskDefKey.equals(ReimTaskDefKeyEnum.DEPARTMENT.getName())){ //部门负责人审批环节
			oaOpinion.setContent(comments.get(0)+"["+currentUser.getName()+"]");
			oaOpinion.setLevel(1);
			oaOpinion.setPosition("部门负责人意见");
			oldOpinion.setLevel(1);
			if("yes".equals(oaCostReimbursement.getAct().getFlag())){ //同意
				vars.put(ReimTaskDefKeyEnum.ACCOUNTING.getUser(),user.getLoginName());
			}else{  //驳回
				OaCostReimbursement oaCostReimbursement1 = dao.get(oaCostReimbursement.getId());
				User createUser = UserUtils.get(oaCostReimbursement1.getCreateBy().getId());
				vars.put(ReimTaskDefKeyEnum.RETURNME.getUser(),createUser.getLoginName());
			}
		}else if(taskDefKey.equals(ReimTaskDefKeyEnum.ACCOUNTING.getName())){ //会计复核
			oaOpinion.setContent(comments.get(0)+"["+currentUser.getName()+"]");
			oaOpinion.setLevel(2);
			oaOpinion.setPosition("会计复核意见");
			oldOpinion.setLevel(2);
			if("yes".equals(oaCostReimbursement.getAct().getFlag())){
				vars.put(ReimTaskDefKeyEnum.FINANCE.getUser(),user.getLoginName());
			}else{
				OaCostReimbursement oaCostReimbursement1 = dao.get(oaCostReimbursement.getId());
				User createUser = UserUtils.get(oaCostReimbursement1.getCreateBy().getId());
				vars.put(ReimTaskDefKeyEnum.RETURNME.getUser(),createUser.getLoginName());
			}
		}else if(taskDefKey.equals(ReimTaskDefKeyEnum.FINANCE.getName())){  //财务部负责人审批
			oaOpinion.setContent(comments.get(0)+"["+currentUser.getName()+"]");
			oaOpinion.setLevel(3);
			oaOpinion.setPosition("财务部负责人意见");
			oldOpinion.setLevel(3);
			if("yes".equals(oaCostReimbursement.getAct().getFlag())){
				vars.put(ReimTaskDefKeyEnum.TOPMANAGER.getUser(),user.getLoginName());
			}else{
				OaCostReimbursement oaCostReimbursement1 = dao.get(oaCostReimbursement.getId());
				User createUser = UserUtils.get(oaCostReimbursement1.getCreateBy().getId());
				vars.put(ReimTaskDefKeyEnum.RETURNME.getUser(),createUser.getLoginName());
			}
		}else if(taskDefKey.equals(ReimTaskDefKeyEnum.TOPMANAGER.getName())){  //总经理审批
			oaOpinion.setContent(comments.get(0)+"["+currentUser.getName()+"]");
			oaOpinion.setLevel(4);
			oaOpinion.setPosition("总经理意见");
			oldOpinion.setLevel(4);
			if("no".equals(oaCostReimbursement.getAct().getFlag())){
				OaCostReimbursement oaCostReimbursement1 = dao.get(oaCostReimbursement.getId());
				User createUser = UserUtils.get(oaCostReimbursement1.getCreateBy().getId());
				vars.put(ReimTaskDefKeyEnum.RETURNME.getUser(),createUser.getLoginName());
			}else{
				OaMeApply oaMeApply = new OaMeApply();
				oaMeApply.setProcInsId(oaCostReimbursement.getAct().getProcInsId());
				oaMeApply.setStatus("1");
				oaMeApplyDao.updateStatus(oaMeApply);
				oaCostReimbursement.setStatus(1);  //完成
				dao.update(oaCostReimbursement);
			}
		}else{
			return;
		}
		//插入或者更新意见
		oldOpinion.setBussinessId(oaCostReimbursement.getId());
		OaOpinion oaOpinion1 = oaOpinionDao.getByBussIdAndLevel(oldOpinion);
		if(oaOpinion1==null){
			oaOpinion.preInsert();
			oaOpinionDao.insert(oaOpinion);
		}else{
			oaOpinion.preUpdate();
			oaOpinion.setId(oaOpinion1.getId());
			oaOpinionDao.update(oaOpinion);
		}
		actTaskService.complete(oaCostReimbursement.getAct().getTaskId(), oaCostReimbursement.getAct().getProcInsId(), oaCostReimbursement.getAct().getComment(), vars);
	}
	
	@Transactional(readOnly = false)
	public void delete(OaCostReimbursement oaCostReimbursement) {
		super.delete(oaCostReimbursement);
	}
	
}