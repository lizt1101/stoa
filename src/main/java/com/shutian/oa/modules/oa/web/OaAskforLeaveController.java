/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.shutian.oa.modules.oa.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.shutian.oa.modules.act.utils.ActUtils;
import com.shutian.oa.modules.oa.entity.OaCostReimDetailed;
import com.shutian.oa.modules.oa.entity.OaOpinion;
import com.shutian.oa.modules.oa.enumeration.LeaveTaskDefKeyEnum;
import com.shutian.oa.modules.oa.enumeration.ReimTaskDefKeyEnum;
import com.shutian.oa.modules.oa.service.OaOpinionService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.history.HistoricTaskInstance;
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
import com.shutian.oa.modules.oa.entity.OaAskforLeave;
import com.shutian.oa.modules.oa.service.OaAskforLeaveService;

import java.util.List;

/**
 * 请假申请Controller
 * @author lizitao
 * @version 2018-07-25
 */
@Controller
@RequestMapping(value = "${adminPath}/oa/oaAskforLeave")
public class OaAskforLeaveController extends BaseController {

	@Autowired
	private OaAskforLeaveService oaAskforLeaveService;
	@Autowired
	private OaOpinionService oaOpinionService;
    @Autowired
    private HistoryService historyService;
	
	@ModelAttribute
	public OaAskforLeave get(@RequestParam(required=false) String id) {
		OaAskforLeave entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = oaAskforLeaveService.get(id);
		}
		if (entity == null){
			entity = new OaAskforLeave();
		}
		return entity;
	}

	@RequestMapping(value = {"list", ""})
	public String list(OaAskforLeave oaAskforLeave, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<OaAskforLeave> page = oaAskforLeaveService.findPage(new Page<OaAskforLeave>(request, response), oaAskforLeave); 
		model.addAttribute("page", page);
		return "modules/oa/oaAskforLeaveList";
	}

	@RequestMapping(value = "form")
	public String form(OaAskforLeave oaAskforLeave, Model model) {
		String view = "oaAskforLeaveForm";
		if (StringUtils.isNotBlank(oaAskforLeave.getId())){
            List<HistoricTaskInstance> tasks = historyService.createHistoricTaskInstanceQuery()
                    .orderByHistoricTaskInstanceStartTime().asc().processInstanceId(oaAskforLeave.getProcInsId()).list();
            HistoricTaskInstance historicTaskInstance = tasks.get(tasks.size()-1);
            oaAskforLeave.getAct().setHistTask(historicTaskInstance);

            OaOpinion oaOpinion = new OaOpinion();
			oaOpinion.setBussinessId(oaAskforLeave.getId());
			List<OaOpinion> list = oaOpinionService.getCommentListByBussinessId(oaOpinion);  //意见列表
            if(oaAskforLeave.getAct().getDetailFlag()!=null && oaAskforLeave.getAct().getDetailFlag().equals("1")){  //详情
                oaAskforLeave.setOaOpinions(list);
                view = "oaAskforLeaveDetail";
                return "modules/oa/"+view;
            }
            // 环节编号
            String taskDefKey = oaAskforLeave.getAct().getTaskDefKey();
			if(taskDefKey.equals(LeaveTaskDefKeyEnum.DEPTMANAGER.getName())){ //部门领导审批环节
				oaAskforLeave.setOaOpinions(list);
				view = "oaAskforLeaveSPForm";
			}else if(taskDefKey.equals(LeaveTaskDefKeyEnum.HR.getName())){ //人事
				oaAskforLeave.setOaOpinions(list);
				view = "oaAskforLeaveSPForm";
			}else if(taskDefKey.equals(LeaveTaskDefKeyEnum.APPLY.getName())){ //返回给我的处理
				oaAskforLeave.setOaOpinions(list);
				view = "oaAskforLeaveForm";
			}
		}
		model.addAttribute("oaAskforLeave", oaAskforLeave);
		return "modules/oa/"+view;
	}

	@RequestMapping(value = "save")
	public String save(OaAskforLeave oaAskforLeave, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, oaAskforLeave)){
			return form(oaAskforLeave, model);
		}
		oaAskforLeaveService.save(oaAskforLeave);
		addMessage(redirectAttributes, "提交请假申请成功");
		return "redirect:"+Global.getAdminPath()+"/oa/oaMeApply/list?repage";
	}

    @RequestMapping(value = "spSave")
    public String spSave(OaAskforLeave oaAskforLeave, Model model, RedirectAttributes redirectAttributes) {
        if (!beanValidator(model, oaAskforLeave)){
            return form(oaAskforLeave, model);
        }
        oaAskforLeaveService.saveSp(oaAskforLeave);
        addMessage(redirectAttributes, "审批请假申请成功");
		return "redirect:"+Global.getAdminPath()+"/act/task/historic?repage";
    }

	@RequestMapping(value = "delete")
	public String delete(OaAskforLeave oaAskforLeave, RedirectAttributes redirectAttributes) {
		oaAskforLeaveService.delete(oaAskforLeave);
		addMessage(redirectAttributes, "删除请假申请成功");
		return "redirect:"+Global.getAdminPath()+"/oa/oaAskforLeave/?repage";
	}

}