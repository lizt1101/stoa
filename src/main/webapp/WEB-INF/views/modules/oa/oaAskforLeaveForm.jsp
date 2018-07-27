<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>请假申请管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			//$("#name").focus();
			$("#inputForm").validate({
				submitHandler: function(form){
					loading('正在提交，请稍等...');
					form.submit();
				},
				errorContainer: "#messageBox",
				errorPlacement: function(error, element) {
					$("#messageBox").text("输入有误，请先更正。");
					if (element.is(":checkbox")||element.is(":radio")||element.parent().is(".input-append")){
						error.appendTo(element.parent().parent());
					} else {
						error.insertAfter(element);
					}
				}
			});
		});
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<%--<li><a href="${ctx}/oa/oaAskforLeave/">请假申请列表</a></li>--%>
		<li class="active"><a href="#">请假申请</a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="oaAskforLeave" action="${ctx}/oa/oaAskforLeave/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<form:hidden path="act.taskId"/>
		<form:hidden path="act.taskName"/>
		<form:hidden path="act.taskDefKey"/>
		<form:hidden path="act.procInsId"/>
		<form:hidden path="act.procDefId"/>
		<form:hidden id="flag" path="act.flag"/>
		<sys:message content="${message}"/>
		<legend>请假申请</legend>
		<div class="control-group">
			<label class="control-label">请假类型：</label>
			<div class="controls">
				<form:select path="leaveType" class="input-xlarge required">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('oa_leave_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">开始时间：</label>
			<div class="controls">
				<input name="startTime" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate required"
					value="<fmt:formatDate value="${oaAskforLeave.startTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">结束时间：</label>
			<div class="controls">
				<input name="endTime" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate required"
					value="<fmt:formatDate value="${oaAskforLeave.endTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>

		<div class="control-group">
			<label class="control-label">请假理由：</label>
			<div class="controls">
				<form:textarea path="reason" htmlEscape="false" rows="4" maxlength="1024" class="input-xxlarge required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">申请时长：</label>
			<div class="controls">
				<input id="applyTime" name="applyTime" value="${oaAskforLeave.applyTime}" type="number" step="0.1" htmlEscape="false" maxlength="255" class="input-small required"/> 时
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">备注信息：</label>
			<div class="controls">
				<form:textarea path="remarks" htmlEscape="false" rows="4" maxlength="255" class="input-xxlarge "/>
			</div>
		</div>
		<c:if test="${not empty oaAskforLeave.id}">
			<div class="control-group">
				<label class="control-label">部门负责人意见：</label>
				<div class="controls">
						${oaAskforLeave.oaOpinions[0].content}
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">人事部门意见：</label>
				<div class="controls">
					<c:if test="${empty oaAskforLeave.oaOpinions[1]}"></c:if>
					<c:if test="${not empty oaAskforLeave.oaOpinions[1]}">${oaAskforLeave.oaOpinions[1].content}</c:if>
				</div>
			</div>
		</c:if>
		<div class="control-group" id="to">
			<label class="control-label">提交给：</label>
			<div class="controls">
				<sys:treeselect id="toUser" name="toUser.id" value="${oaAskforLeave.toUser.name}" labelName="toUser.name" labelValue="${oaAskforLeave.toUser.name}"
								title="用户" url="/sys/office/officeTreeData?type=3" cssClass="required recipient" cssStyle="width:150px"
								allowClear="true" notAllowSelectParent="true" smallBtn="false"/>
				<span class="help-inline"><font color="red">*</font> </span>
				<c:if test="${not empty oaAskforLeave.id}"><span style="color: red">注:撤销申请不必选择提交人</span></c:if>
			</div>
		</div>
		<div class="form-actions">
			<input id="btnSubmit" class="btn btn-primary" type="submit" value="提交申请" onclick="$('#flag').val('yes');$('#to').attr('style','')"/>&nbsp;
			<c:if test="${not empty oaAskforLeave.id}">
				<input id="btnSubmit2" class="btn btn-inverse" type="submit" value="撤销申请" onclick="$('#flag').val('no');$('#to').attr('style','display:none')"/>&nbsp;
			</c:if>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
		<c:if test="${not empty oaAskforLeave.id}">
			<act:histoicFlow procInsId="${oaAskforLeave.act.procInsId}" />
			<legend>流程图</legend>
			<iframe style="height: 600px;width: 100%" src="${pageContext.request.contextPath}/act/diagram-viewer?processDefinitionId=${oaAskforLeave.act.histTask.processDefinitionId}&processInstanceId=${oaAskforLeave.act.histTask.processInstanceId}"></iframe>
		</c:if>
	</form:form>
</body>
</html>