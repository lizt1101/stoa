/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.shutian.oa.modules.oa.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.shutian.oa.common.utils.ChineseConvertUtil;
import com.shutian.oa.modules.act.utils.ActUtils;
import com.shutian.oa.modules.oa.entity.OaCostReimDetailed;
import com.shutian.oa.modules.oa.entity.OaOpinion;
import com.shutian.oa.modules.oa.enumeration.OvertimeTaskDefKeyEnum;
import com.shutian.oa.modules.oa.enumeration.ReimTaskDefKeyEnum;
import com.shutian.oa.modules.oa.service.OaCostReimDetailedService;
import com.shutian.oa.modules.oa.service.OaOpinionService;
import com.shutian.oa.modules.sys.entity.Office;
import com.shutian.oa.modules.sys.entity.User;
import com.shutian.oa.modules.sys.service.OfficeService;
import com.shutian.oa.modules.sys.utils.UserUtils;
import org.activiti.engine.HistoryService;
import org.activiti.engine.history.HistoricTaskInstance;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shutian.oa.common.config.Global;
import com.shutian.oa.common.persistence.Page;
import com.shutian.oa.common.web.BaseController;
import com.shutian.oa.common.utils.StringUtils;
import com.shutian.oa.modules.oa.entity.OaCostReimbursement;
import com.shutian.oa.modules.oa.service.OaCostReimbursementService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 费用报销Controller
 * @author lizt
 * @version 2018-07-18
 */
@Controller
@RequestMapping(value = "${adminPath}/oa/oaCostReimbursement")
public class OaCostReimbursementController extends BaseController {

	@Autowired
	private OaCostReimbursementService oaCostReimbursementService;
	@Autowired
	private OaOpinionService oaOpinionService;
	@Autowired
	private OaCostReimDetailedService oaCostReimDetailedService;
	@Autowired
	private HistoryService historyService;
	@Autowired
	private OfficeService officeService;
	
	@ModelAttribute
	public OaCostReimbursement get(@RequestParam(required=false) String id) {
		OaCostReimbursement entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = oaCostReimbursementService.get(id);
		}
		if (entity == null){
			entity = new OaCostReimbursement();
		}
		return entity;
	}

	@RequestMapping(value = {"list", ""})
	public String list(OaCostReimbursement oaCostReimbursement, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<OaCostReimbursement> page = oaCostReimbursementService.findPage(new Page<OaCostReimbursement>(request, response), oaCostReimbursement); 
		model.addAttribute("page", page);
		return "modules/oa/oaCostReimbursementList";
	}

	@RequestMapping(value = "form")
	public String form(OaCostReimbursement oaCostReimbursement, Model model) {
		String view = "oaCostReimbursementForm";
		OaOpinion oaOpinion = new OaOpinion();
		if (StringUtils.isNotBlank(oaCostReimbursement.getId())){
			List<HistoricTaskInstance> tasks = historyService.createHistoricTaskInstanceQuery()
					.orderByHistoricTaskInstanceStartTime().asc().processInstanceId(oaCostReimbursement.getProcInsId()).list();
			HistoricTaskInstance historicTaskInstance = tasks.get(tasks.size()-1);
			oaCostReimbursement.getAct().setHistTask(historicTaskInstance);
			oaOpinion.setBussinessId(oaCostReimbursement.getId());
			// 环节编号
			String taskDefKey = oaCostReimbursement.getAct().getTaskDefKey();
			OaCostReimDetailed oaCostReimDetailed = new OaCostReimDetailed();
			oaCostReimDetailed.setReimId(oaCostReimbursement.getId());
			List<OaCostReimDetailed> detils = oaCostReimDetailedService.getByReimId(oaCostReimDetailed); //费用明细
			List<OaOpinion> list = oaOpinionService.getCommentListByBussinessId(oaOpinion);  //意见列表
			Integer costCount = 0;
			for (OaCostReimDetailed oa:detils){
				costCount += oa.getBillCount();
			}
			oaCostReimbursement.setCostCount(costCount);
			oaCostReimbursement.setOaCostReimDetaileds(detils);
			if(oaCostReimbursement.getAct().getDetailFlag()!=null && oaCostReimbursement.getAct().getDetailFlag().equals("1")){ //跳转详情接口
				oaCostReimbursement.setOaOpinions(list);
				view = "oaCostReimbursementDetail";
				model.addAttribute("oaCostReimbursement", oaCostReimbursement);
				return "modules/oa/"+view;
			}
			if(taskDefKey.equals(ReimTaskDefKeyEnum.DEPARTMENT.getName())){ //部门领导审批环节
				oaCostReimbursement.setOaOpinions(list);
				view = "oaCostReimbursementSPForm";
			}else if(taskDefKey.equals(ReimTaskDefKeyEnum.ACCOUNTING.getName())){ //会计复核环节
				oaCostReimbursement.setOaOpinions(list);
				view = "oaCostReimbursementSPForm";
			}else if(taskDefKey.equals(ReimTaskDefKeyEnum.FINANCE.getName())){ //财务部负责人环节
				oaCostReimbursement.setOaOpinions(list);
				view = "oaCostReimbursementSPForm";
			}else if(taskDefKey.equals(ReimTaskDefKeyEnum.TOPMANAGER.getName())){ //总经理复核环节
				oaCostReimbursement.setOaOpinions(list);
				view = "oaCostReimbursementSPForm";
			}else if(taskDefKey.equals(ReimTaskDefKeyEnum.RETURNME.getName())){ //返回给我的处理
				oaCostReimbursement.setOaOpinions(list);
				view = "oaCostReimbursementForm";
			}
		}else{
			User currentUser = UserUtils.getUser();
			Office office = officeService.get(currentUser.getOffice());
			oaCostReimbursement.setUser(currentUser);
			oaCostReimbursement.setOffice(office);
		}
		model.addAttribute("oaCostReimbursement", oaCostReimbursement);
		return "modules/oa/"+view;
	}


	@RequestMapping(value = "save")
	public String save(OaCostReimbursement oaCostReimbursement, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, oaCostReimbursement)){
			return form(oaCostReimbursement, model);
		}
		oaCostReimbursementService.save(oaCostReimbursement);
		addMessage(redirectAttributes, "保存费用报销成功");
		return "redirect:"+Global.getAdminPath()+"/oa/oaMeApply/list?repage";
	}

	/**
	 * 审批保存
	 * @param oaCostReimbursement
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "spSave")
	public String spSave(OaCostReimbursement oaCostReimbursement, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, oaCostReimbursement)){
			return form(oaCostReimbursement, model);
		}
		oaCostReimbursementService.saveSp(oaCostReimbursement);
		addMessage(redirectAttributes, "审批费用报销成功");
		return "redirect:"+Global.getAdminPath()+"/act/task/historic?repage";
	}

	@RequestMapping(value = "delete")
	public String delete(OaCostReimbursement oaCostReimbursement, RedirectAttributes redirectAttributes) {
		oaCostReimbursementService.delete(oaCostReimbursement);
		addMessage(redirectAttributes, "删除费用报销成功");
		return "redirect:"+Global.getAdminPath()+"/oa/oaCostReimbursement/?repage";
	}

	@RequestMapping(value = "getChinese")
	@ResponseBody
	public String getChinese(String total, RedirectAttributes redirectAttributes) {
		if(total.indexOf(".")!=-1){// 保留两位小数
			total = String.format("%.2f",Double.parseDouble(total));
		}
		String chinese = ChineseConvertUtil.chinese(total);
		Map<String,Object> resultMap = new HashMap<String, Object>();
		resultMap.put("chineseCost",chinese);
		return JSON.toJSONString(resultMap);
	}

}