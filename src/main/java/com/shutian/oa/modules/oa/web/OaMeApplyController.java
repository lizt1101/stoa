/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.shutian.oa.modules.oa.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import com.shutian.oa.modules.oa.entity.OaMeApply;
import com.shutian.oa.modules.oa.service.OaMeApplyService;

/**
 * 我的申请Controller
 * @author lizitao
 * @version 2018-07-24
 */
@Controller
@RequestMapping(value = "${adminPath}/oa/oaMeApply")
public class OaMeApplyController extends BaseController {

	@Autowired
	private OaMeApplyService oaMeApplyService;
	
	@ModelAttribute
	public OaMeApply get(@RequestParam(required=false) String id) {
		OaMeApply entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = oaMeApplyService.get(id);
		}
		if (entity == null){
			entity = new OaMeApply();
		}
		return entity;
	}
	
	@RequiresPermissions("oa:oaMeApply:view")
	@RequestMapping(value = {"list", ""})
	public String list(OaMeApply oaMeApply, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<OaMeApply> page = oaMeApplyService.findPage(new Page<OaMeApply>(request, response), oaMeApply);
		model.addAttribute("page", page);
		return "modules/oa/oaMeApplyList";
	}

	@RequiresPermissions("oa:oaMeApply:view")
	@RequestMapping(value = "form")
	public String form(OaMeApply oaMeApply, Model model) {
		model.addAttribute("oaMeApply", oaMeApply);
		return "modules/oa/oaMeApplyForm";
	}

	@RequiresPermissions("oa:oaMeApply:edit")
	@RequestMapping(value = "save")
	public String save(OaMeApply oaMeApply, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, oaMeApply)){
			return form(oaMeApply, model);
		}
		oaMeApplyService.save(oaMeApply);
		addMessage(redirectAttributes, "保存我的申请成功");
		return "redirect:"+Global.getAdminPath()+"/oa/oaMeApply/?repage";
	}
	
	@RequiresPermissions("oa:oaMeApply:edit")
	@RequestMapping(value = "delete")
	public String delete(OaMeApply oaMeApply, RedirectAttributes redirectAttributes) {
		oaMeApplyService.delete(oaMeApply);
		addMessage(redirectAttributes, "删除我的申请成功");
		return "redirect:"+Global.getAdminPath()+"/oa/oaMeApply/?repage";
	}

}