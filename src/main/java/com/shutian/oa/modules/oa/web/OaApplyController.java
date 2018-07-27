/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.shutian.oa.modules.oa.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.shutian.oa.modules.oa.entity.OaCostReimbursement;
import com.shutian.oa.modules.oa.service.OaCostReimbursementService;
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
import com.shutian.oa.modules.oa.entity.OaApply;
import com.shutian.oa.modules.oa.service.OaApplyService;

/**
 * 选择申请的流程Controller
 * @author lizitao
 * @version 2018-07-17
 */
@Controller
@RequestMapping(value = "${adminPath}/oa/oaApply")
public class OaApplyController extends BaseController {

	@Autowired
	private OaApplyService oaApplyService;
	@Autowired
	private OaCostReimbursementService oaCostReimbursementService;

	/**
	 *
	 * @param oaCostReimbursement
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = {"myApply"})
	public String myApply(OaCostReimbursement oaCostReimbursement, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<OaCostReimbursement> page = oaCostReimbursementService.findPage(new Page<OaCostReimbursement>(request, response), oaCostReimbursement);
		model.addAttribute("page", page);
		return "modules/oa/oaMeApplyList";
	}

	
	@ModelAttribute
	public OaApply get(@RequestParam(required=false) String id) {
		OaApply entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = oaApplyService.get(id);
		}
		if (entity == null){
			entity = new OaApply();
		}
		return entity;
	}
	
	@RequiresPermissions("oa:oaApply:view")
	@RequestMapping(value = {"list", ""})
	public String list(OaApply oaApply, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<OaApply> page = oaApplyService.findPage(new Page<OaApply>(request, response), oaApply); 
		model.addAttribute("page", page);
		return "modules/oa/oaApplyList";
	}

	@RequiresPermissions("oa:oaApply:view")
	@RequestMapping(value = "form")
	public String form(OaApply oaApply, Model model) {
		model.addAttribute("oaApply", oaApply);
		return "modules/oa/oaApplyForm";
	}

	@RequiresPermissions("oa:oaApply:edit")
	@RequestMapping(value = "save")
	public String save(OaApply oaApply, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, oaApply)){
			return form(oaApply, model);
		}
		oaApplyService.save(oaApply);
		addMessage(redirectAttributes, "保存申请成功");
		return "redirect:"+Global.getAdminPath()+"/oa/oaApply/?repage";
	}
	
	@RequiresPermissions("oa:oaApply:edit")
	@RequestMapping(value = "delete")
	public String delete(OaApply oaApply, RedirectAttributes redirectAttributes) {
		oaApplyService.delete(oaApply);
		addMessage(redirectAttributes, "删除申请成功");
		return "redirect:"+Global.getAdminPath()+"/oa/oaApply/?repage";
	}

}