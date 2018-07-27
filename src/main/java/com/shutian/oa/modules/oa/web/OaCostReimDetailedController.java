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
import com.shutian.oa.modules.oa.entity.OaCostReimDetailed;
import com.shutian.oa.modules.oa.service.OaCostReimDetailedService;

/**
 * 费用报销明细Controller
 * @author lizt
 * @version 2018-07-18
 */
@Controller
@RequestMapping(value = "${adminPath}/oa/oaCostReimDetailed")
public class OaCostReimDetailedController extends BaseController {

	@Autowired
	private OaCostReimDetailedService oaCostReimDetailedService;
	
	@ModelAttribute
	public OaCostReimDetailed get(@RequestParam(required=false) String id) {
		OaCostReimDetailed entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = oaCostReimDetailedService.get(id);
		}
		if (entity == null){
			entity = new OaCostReimDetailed();
		}
		return entity;
	}
	
	@RequiresPermissions("oa:oaCostReimDetailed:view")
	@RequestMapping(value = {"list", ""})
	public String list(OaCostReimDetailed oaCostReimDetailed, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<OaCostReimDetailed> page = oaCostReimDetailedService.findPage(new Page<OaCostReimDetailed>(request, response), oaCostReimDetailed); 
		model.addAttribute("page", page);
		return "modules/oa/oaCostReimDetailedList";
	}

	@RequiresPermissions("oa:oaCostReimDetailed:view")
	@RequestMapping(value = "form")
	public String form(OaCostReimDetailed oaCostReimDetailed, Model model) {
		model.addAttribute("oaCostReimDetailed", oaCostReimDetailed);
		return "modules/oa/oaCostReimDetailedForm";
	}

	@RequiresPermissions("oa:oaCostReimDetailed:edit")
	@RequestMapping(value = "save")
	public String save(OaCostReimDetailed oaCostReimDetailed, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, oaCostReimDetailed)){
			return form(oaCostReimDetailed, model);
		}
		oaCostReimDetailedService.save(oaCostReimDetailed);
		addMessage(redirectAttributes, "保存费用报销明细表成功");
		return "redirect:"+Global.getAdminPath()+"/oa/oaCostReimDetailed/?repage";
	}
	
	@RequiresPermissions("oa:oaCostReimDetailed:edit")
	@RequestMapping(value = "delete")
	public String delete(OaCostReimDetailed oaCostReimDetailed, RedirectAttributes redirectAttributes) {
		oaCostReimDetailedService.delete(oaCostReimDetailed);
		addMessage(redirectAttributes, "删除费用报销明细表成功");
		return "redirect:"+Global.getAdminPath()+"/oa/oaCostReimDetailed/?repage";
	}

}