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
import com.shutian.oa.modules.oa.entity.OaOpinion;
import com.shutian.oa.modules.oa.service.OaOpinionService;

/**
 * oa意见表Controller
 * @author lizt
 * @version 2018-07-18
 */
@Controller
@RequestMapping(value = "${adminPath}/oa/oaOpinion")
public class OaOpinionController extends BaseController {

	@Autowired
	private OaOpinionService oaOpinionService;
	
	@ModelAttribute
	public OaOpinion get(@RequestParam(required=false) String id) {
		OaOpinion entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = oaOpinionService.get(id);
		}
		if (entity == null){
			entity = new OaOpinion();
		}
		return entity;
	}
	
	@RequiresPermissions("oa:oaOpinion:view")
	@RequestMapping(value = {"list", ""})
	public String list(OaOpinion oaOpinion, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<OaOpinion> page = oaOpinionService.findPage(new Page<OaOpinion>(request, response), oaOpinion); 
		model.addAttribute("page", page);
		return "modules/oa/oaOpinionList";
	}

	@RequiresPermissions("oa:oaOpinion:view")
	@RequestMapping(value = "form")
	public String form(OaOpinion oaOpinion, Model model) {
		model.addAttribute("oaOpinion", oaOpinion);
		return "modules/oa/oaOpinionForm";
	}

	@RequiresPermissions("oa:oaOpinion:edit")
	@RequestMapping(value = "save")
	public String save(OaOpinion oaOpinion, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, oaOpinion)){
			return form(oaOpinion, model);
		}
		oaOpinionService.save(oaOpinion);
		addMessage(redirectAttributes, "保存意见成功");
		return "redirect:"+Global.getAdminPath()+"/oa/oaOpinion/?repage";
	}
	
	@RequiresPermissions("oa:oaOpinion:edit")
	@RequestMapping(value = "delete")
	public String delete(OaOpinion oaOpinion, RedirectAttributes redirectAttributes) {
		oaOpinionService.delete(oaOpinion);
		addMessage(redirectAttributes, "删除意见成功");
		return "redirect:"+Global.getAdminPath()+"/oa/oaOpinion/?repage";
	}

}