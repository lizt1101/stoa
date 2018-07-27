<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>加班申请管理</title>
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
		<%--<li><a href="${ctx}/oa/oaOvertime/">加班申请列表</a></li>--%>
		<li class="active"><a href="${ctx}/oa/oaOvertime/form?id=${oaOvertime.id}">加班申请审批</a></li>
	</ul><br/>
	<%--<div class="control-group">
		<label class="control-label"></label>
		<div class="controls">
			<span style="font-weight: bold;font-size: 18px">基本信息</span>
		</div>
	</div>

	&lt;%&ndash;<span>基本信息</span>&ndash;%&gt;
	<div class="control-group">
		<label class="control-label"></label>
		<div class="controls">
			<span style="font-weight: bold;font-size: 18px">加班信息</span>
		</div>
	</div>--%>
	<form:form id="inputForm" modelAttribute="oaOvertime" action="${ctx}/oa/OaOvertime/opinionSave" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<form:hidden path="act.taskId"/>
		<form:hidden path="act.taskName"/>
		<form:hidden path="act.taskDefKey"/>
		<form:hidden path="act.procInsId"/>
		<form:hidden path="act.procDefId"/>
		<form:hidden id="flag" path="act.flag"/>
		<sys:message content="${message}"/>
		<div class="control-group">
			<label class="control-label">标题：</label>
			<div class="controls">
				${oaOvertime.title}
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">紧急程度</label>
			<div class="controls">
				<label class="lbl">${fns:getDictLabel(oaOvertime.degree, 'degree', '无')}</label>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">加班类型</label>
			<div class="controls">
				<label class="lbl">${fns:getDictLabel(oaOvertime.type, 'overtime', '无')}</label>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">加班事由：</label>
			<div class="controls">
				<%--<form:textarea path="reason" htmlEscape="false" rows="4" maxlength="1024" class="input-xxlarge "/>
				<span class="help-inline"><font color="red">*</font> </span>--%>
				${oaOvertime.reason}
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">开始时间：</label>
			<div class="controls">
				<fmt:formatDate value="${oaOvertime.startTime}" type="both" dateStyle="full"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">结束时间：</label>
			<div class="controls">
				<fmt:formatDate value="${oaOvertime.endTime}" type="both" dateStyle="full"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">加班时长：</label>
			<div class="controls">
				${oaOvertime.timeLong}时
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">备注：</label>
			<div class="controls">
					${oaOvertime.remarks}
			</div>
		</div>
		<c:if test="${empty oaOvertime.oaOpinions}">
			<div class="control-group">
				<label class="control-label">部门领导审批：</label>
				<div class="controls">
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">人事审批：</label>
				<div class="controls">
				</div>
			</div>
		</c:if>
		<c:if test="${not empty oaOvertime.oaOpinions}">
			<c:forEach items="${oaOvertime.oaOpinions}" var="opinion">
				<div class="control-group">
					<label class="control-label">${opinion.position}审批：</label>
					<div class="controls">
							${opinion.content}
					</div>
				</div>
			</c:forEach>
		</c:if>
		<div class="control-group">
			<label class="control-label">您的意见：</label>
			<div class="controls">
				<form:textarea path="act.comment" class="required" rows="5" maxlength="20" cssStyle="width:500px"/>
			</div>
		</div>
		<div class="form-actions">
		<%--	<c:if test="${oaOvertime.act.taskDefKey eq 'apply_end'}">
				<input id="btnSubmit" class="btn btn-primary" type="submit" value="兑 现" onclick="$('#flag').val('yes')"/>&nbsp;
			</c:if>--%>
			<c:if test="${oaOvertime.act.taskDefKey ne 'end'}">
				<input id="btnSubmit" class="btn btn-primary" type="submit" value="同 意" onclick="$('#flag').val('yes')"/>&nbsp;
				<input id="btnSubmit" class="btn btn-inverse" type="submit" value="驳 回" onclick="$('#flag').val('no')"/>&nbsp;
			</c:if>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
		<act:histoicFlow procInsId="${oaOvertime.act.procInsId}"/>
	</form:form>
</body>
</html>