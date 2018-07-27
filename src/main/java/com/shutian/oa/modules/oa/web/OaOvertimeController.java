/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.shutian.oa.modules.oa.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.shutian.oa.modules.act.entity.Act;
import com.shutian.oa.modules.oa.entity.OaOpinion;
import com.shutian.oa.modules.oa.enumeration.OvertimeTaskDefKeyEnum;
import com.shutian.oa.modules.oa.service.OaOpinionService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shutian.oa.common.config.Global;
import com.shutian.oa.common.persistence.Page;
import com.shutian.oa.common.web.BaseController;
import com.shutian.oa.common.utils.StringUtils;
import com.shutian.oa.modules.oa.entity.OaOvertime;
import com.shutian.oa.modules.oa.service.OaOvertimeService;

import java.util.List;

/**
 * 加班申请Controller
 * @author lizt
 * @version 2018-07-17
 */
@Controller
@RequestMapping(value = "${adminPath}/oa/OaOvertime")
public class OaOvertimeController extends BaseController {

	@Autowired
	private OaOvertimeService oaOvertimeService;
	@Autowired
	private OaOpinionService oaOpinionService;
	
	@ModelAttribute
	public OaOvertime get(@RequestParam(required=false) String id) {
		OaOvertime entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = oaOvertimeService.get(id);
		}
		if (entity == null){
			entity = new OaOvertime();
		}
		return entity;
	}
	
	@RequiresPermissions("oa:oaOvertime:view")
	@RequestMapping(value = {"list", ""})
	public String list(OaOvertime oaOvertime, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<OaOvertime> page = oaOvertimeService.findPage(new Page<OaOvertime>(request, response), oaOvertime); 
		model.addAttribute("page", page);
		return "modules/oa/oaOvertimeList";
	}

	@RequestMapping(value = "form")
	public String form(OaOvertime oaOvertime, Model model) {
		String view = "oaOvertimeForm";
		OaOpinion oaOpinion = new OaOpinion();
		// 查看审批申请单
		if (StringUtils.isNotBlank(oaOvertime.getId())){ //
			oaOpinion.setBussinessId(oaOvertime.getId());
			List<OaOpinion> list = oaOpinionService.getCommentListByBussinessId(oaOpinion);
			// 环节编号
			String taskDefKey = oaOvertime.getAct().getTaskDefKey();
			if(taskDefKey.equals(OvertimeTaskDefKeyEnum.DEPTMANAGER.getName())){ //部门领导审批环节
				oaOvertime.setOaOpinions(list);
				view = "oaOvertimeDeptForm";
			}else if(taskDefKey.equals(OvertimeTaskDefKeyEnum.HR.getName())){ //人事审批环节
				oaOvertime.setOaOpinions(list);
				view = "oaOvertimeDeptForm";
			}else if(taskDefKey.equals(OvertimeTaskDefKeyEnum.APPLY.getName())){ //返回给我的处理
				view = "oaOvertimeForm";
			}
		}
		model.addAttribute("oaOvertime", oaOvertime);
		return "modules/oa/"+view;
	}

	@RequestMapping(value = "save")
	public String save(OaOvertime oaOvertime, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, oaOvertime)){
			return form(oaOvertime, model);
		}
		oaOvertimeService.save(oaOvertime);
		addMessage(redirectAttributes, "保存加班申请成功");
		return "redirect:" + adminPath + "/act/task/todo/";
	}

	/**
	 * 意见审批
	 * @param oaOvertime
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "opinionSave")
	public String opinionSave(OaOvertime oaOvertime, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, oaOvertime)){
			return form(oaOvertime, model);
		}
		oaOvertimeService.saveOpinion(oaOvertime);
		addMessage(redirectAttributes, "保存加班申请成功");
		return "redirect:"+Global.getAdminPath()+"/oa/OaOvertime/?repage";
	}

	@RequestMapping(value = "delete")
	public String delete(OaOvertime oaOvertime, RedirectAttributes redirectAttributes) {
		oaOvertimeService.delete(oaOvertime);
		addMessage(redirectAttributes, "删除加班申请成功");
		return "redirect:"+Global.getAdminPath()+"/oa/oaOvertime/?repage";
	}

}