<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>请假申请审批</title>
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
		<li class="active"><a href="#">请假审批</a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="oaAskforLeave" action="${ctx}/oa/oaAskforLeave/spSave" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<form:hidden path="act.taskId"/>
		<form:hidden path="act.taskName"/>
		<form:hidden path="act.taskDefKey"/>
		<form:hidden path="act.procInsId"/>
		<form:hidden path="act.procDefId"/>
		<form:hidden id="flag" path="act.flag"/>
		<sys:message content="${message}"/>
		<legend>请假审批</legend>
		<div class="control-group">
			<label class="control-label">请假类型：</label>
			<div class="controls">
				${fns:getDictLabel(oaAskforLeave.leaveType, 'oa_leave_type', '')}
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">请假理由：</label>
			<div class="controls">
				${oaAskforLeave.reason}
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">开始时间：</label>
			<div class="controls">
				<fmt:formatDate value="${oaAskforLeave.startTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">结束时间：</label>
			<div class="controls">
				<fmt:formatDate value="${oaAskforLeave.endTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">申请时长：</label>
			<div class="controls">
					${oaAskforLeave.applyTime}时
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">备注信息：</label>
			<div class="controls">
				${oaAskforLeave.remarks}
			</div>
		</div>
		<c:if test="${not empty oaAskforLeave.id}">
			<div class="control-group">
				<label class="control-label">部门负责人意见：</label>
				<div class="controls">
					<c:choose>
						<c:when test="${oaAskforLeave.act.taskDefKey=='department'}">
							<textarea name="comments[0]"  htmlEscape="false" rows="4" maxlength="255" class="input-xxlarge required" >${oaAskforLeave.oaOpinions[0].content}</textarea>
						</c:when>
						<c:otherwise>${oaAskforLeave.oaOpinions[0].content}</c:otherwise>
					</c:choose>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">人事部门意见：</label>
				<div class="controls">
					<c:choose>
						<c:when test="${oaAskforLeave.act.taskDefKey=='hr'}">
							<textarea name="comments[1]"  htmlEscape="false" rows="4" maxlength="255" class="input-xxlarge required" >${oaAskforLeave.oaOpinions[1].content}</textarea>
						</c:when>
						<c:otherwise>${oaAskforLeave.oaOpinions[1].content}</c:otherwise>
					</c:choose>
				</div>
			</div>
		</c:if>
		<c:if test="${oaAskforLeave.act.taskDefKey ne 'hr'}">
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
		</c:if>
		<div class="form-actions">
			<c:if test="${oaAskforLeave.act.taskDefKey ne 'end'}">
				<input id="btnSubmit" class="btn btn-primary" type="submit" value="同 意" onclick="$('#flag').val('yes');$('#to').removeAttr('style')"/>&nbsp;
				<input id="btnSubmit" class="btn btn-inverse" type="submit" value="驳 回" onclick="$('#flag').val('no');$('#to').attr('style','display:none')"/>&nbsp;
			</c:if>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
		<act:histoicFlow procInsId="${oaAskforLeave.act.procInsId}" />
		<legend>流程图</legend>
		<iframe style="height: 600px;width: 100%" src="${pageContext.request.contextPath}/act/diagram-viewer?processDefinitionId=${oaAskforLeave.act.histTask.processDefinitionId}&processInstanceId=${oaAskforLeave.act.histTask.processInstanceId}"></iframe>
	</form:form>
</body>
</html>