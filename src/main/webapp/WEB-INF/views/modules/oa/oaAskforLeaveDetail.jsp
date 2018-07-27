<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>请假申请详情</title>
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
		<li class="active"><a href="#">请假详情</a></li>
	</ul><br/>
	<form:form id="inputForm" class="form-horizontal">
		<legend>请假详情</legend>
		<sys:message content="${message}"/>
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
					<c:if test="${empty oaAskforLeave.oaOpinions[0]}"></c:if>
					<c:if test="${not empty oaAskforLeave.oaOpinions[0]}">${oaAskforLeave.oaOpinions[0].content}</c:if>
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
		<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		<act:histoicFlow procInsId="${oaAskforLeave.act.procInsId}" />
		<c:if test="${oaAskforLeave.act.status==0}">
			<legend>流程图</legend>
			<iframe style="height: 600px;width: 100%" src="${pageContext.request.contextPath}/act/diagram-viewer?processDefinitionId=${oaAskforLeave.act.histTask.processDefinitionId}&processInstanceId=${oaAskforLeave.act.histTask.processInstanceId}"></iframe>
		</c:if>
	</form:form>
</body>
</html>