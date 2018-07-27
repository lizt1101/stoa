<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>费用报销管理</title>
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
    function returnMe(){

	}
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
	<%--	<li><a href="${ctx}/oa/oaCostReimbursement/">费用报销列表</a></li>--%>
		<li class="active"><a href="#">费用报销审批</a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="oaCostReimbursement" action="${ctx}/oa/oaCostReimbursement/spSave" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<form:hidden path="act.taskId"/>
		<form:hidden path="act.taskName"/>
		<form:hidden path="act.taskDefKey"/>
		<form:hidden path="act.procInsId"/>
		<form:hidden path="act.procDefId"/>
		<form:hidden id="flag" path="act.flag"/>
		<sys:message content="${message}"/>
		<fieldset>
			<legend>${oaCostReimbursement.act.taskName}</legend>
			<table class="table-form" id="tab">
				<tr>
					<td colspan="11" class="tit">费用报销单</td>
					<td colspan="1"  class="tit">编号:${oaCostReimbursement.number}</td>
				</tr>
				<tr>
					<td class="tit">报销人 :</td>
					<td colspan="2" class="tit">
						${oaCostReimbursement.user.name}
					</td>
					<td class="tit">部门 :</td>
					<td colspan="2" class="tit">
						${oaCostReimbursement.office.name}
					</td>
					<td class="tit">职位 :</td>
					<td colspan="2" class="tit">
						${oaCostReimbursement.reimPosition}
					</td>
					<td class="tit">报销时间 :</td>
					<td colspan="2" class="tit">
						<fmt:formatDate value="${oaCostReimbursement.createDate}" pattern="yyyy年MM月dd日"/>
					</td>
				</tr>
				<tr>
					<td class="tit" colspan="4">摘要</td>
					<td class="tit" colspan="2">项目</td>
					<td class="tit" colspan="3">金额</td>
					<td class="tit" colspan="1">单据张数</td>
					<td class="tit" colspan="2">备注</td>
				</tr>
				<c:forEach items="${oaCostReimbursement.oaCostReimDetaileds}" var="detail" varStatus="status">
					<tr>
						<td class="tit" colspan="4">
							${detail.summary}
						</td>
						<td class="tit" colspan="2">
							${detail.project}
						</td>
						<td class="tit" colspan="3">
							${detail.money}
						</td>
						<td class="tit" colspan="1">
							${detail.billCount}
						</td>
						<td class="tit" colspan="2">
							${detail.remarks}
						</td>
					</tr>
				</c:forEach>
				<tr>
					<td class="tit" colspan="6">合计:</td>
					<td class="tit" colspan="3" id="totalCount">${oaCostReimbursement.totalMoney}</td>
					<td class="tit" colspan="1" id="costCount">${oaCostReimbursement.costCount}</td>
					<td class="tit" colspan="2"></td>
				</tr>
				<tr>
					<td class="tit" colspan="3">合计人民币(大写) :</td>
					<td class="tit" colspan="9" id="chinese">${oaCostReimbursement.totalBigMoney}</td>
				</tr>
				<tr>
					<td class="tit" colspan="3">部门负责人意见 :</td>
					<td class="tit" colspan="9">
						<c:choose>
							<c:when test="${oaCostReimbursement.act.taskDefKey=='department'}">
								<textarea name="comments[0]"  htmlEscape="false" rows="4" maxlength="255" class="input-xxlarge required" >${oaCostReimbursement.oaOpinions[0].content}</textarea>
							</c:when>
							<c:otherwise>${oaCostReimbursement.oaOpinions[0].content}</c:otherwise>
						</c:choose>
					</td>
				</tr>
				<tr>
					<td class="tit" colspan="3">会计复核 :</td>
					<td class="tit" colspan="9">
						<c:choose>
							<c:when test="${oaCostReimbursement.act.taskDefKey=='accounting'}">
								<textarea name="comments[1]" htmlEscape="false" rows="4" maxlength="255" class="input-xxlarge required">${oaCostReimbursement.oaOpinions[1].content}</textarea>
							</c:when>
							<c:otherwise>
								<c:if test="${empty oaCostReimbursement.oaOpinions[1]}"></c:if>
								<c:if test="${not empty oaCostReimbursement.oaOpinions[1]}">${oaCostReimbursement.oaOpinions[1].content}</c:if>
							</c:otherwise>
						</c:choose>
					</td>
				</tr>
				<tr>
					<td class="tit" colspan="3">财务部负责人意见 :</td>
					<td class="tit" colspan="9">
						<c:choose>
							<c:when test="${oaCostReimbursement.act.taskDefKey=='finance'}">
								<textarea name="comments[2]" htmlEscape="false" rows="4" maxlength="255" class="input-xxlarge required">${oaCostReimbursement.oaOpinions[2].content}</textarea>
							</c:when>
							<c:otherwise>
								<c:if test="${empty oaCostReimbursement.oaOpinions[2]}"></c:if>
								<c:if test="${not empty oaCostReimbursement.oaOpinions[2]}">${oaCostReimbursement.oaOpinions[2].content}</c:if>
							</c:otherwise>
						</c:choose>
					</td>
				</tr>
				<tr>
					<td class="tit" colspan="3">总经理意见 :</td>
					<td class="tit" colspan="9">
						<c:choose>
							<c:when test="${oaCostReimbursement.act.taskDefKey=='topManager'}">
								<textarea name="comments[3]" htmlEscape="false" rows="4" maxlength="255" class="input-xxlarge required">${oaCostReimbursement.oaOpinions[3].content}</textarea>
							</c:when>
							<c:otherwise>
								<c:if test="${empty oaCostReimbursement.oaOpinions[3]}"></c:if>
								<c:if test="${not empty oaCostReimbursement.oaOpinions[3]}">${oaCostReimbursement.oaOpinions[3].content}</c:if>
							</c:otherwise>
						</c:choose>
					</td>
				</tr>
			</table>
		</fieldset>
		<c:if test="${oaCostReimbursement.act.taskDefKey ne 'topManager'}">
		<div id="to" style="margin-top: 20px;">
			<label>提交给：</label><span style="color: red">注:驳回时不用选择提交人</span>
			<div>
				<sys:treeselect id="toUser" name="toUser.id" value="${oaCostReimbursement.toUser.name}" labelName="toUser.name" labelValue="${oaCostReimbursement.toUser.name}"
								title="用户" url="/sys/office/officeTreeData?type=3" cssClass="required recipient" cssStyle="width:150px"
								allowClear="true" notAllowSelectParent="true" smallBtn="false"/>
			</div>
		</div>
		</c:if>
		<div class="form-actions">
			<c:if test="${oaCostReimbursement.act.taskDefKey ne 'end'}">
				<input id="btnSubmit" class="btn btn-primary" type="submit" value="同 意" onclick="$('#flag').val('yes');$('#to').removeAttr('style')"/>&nbsp;
				<input id="btnSubmit" class="btn btn-inverse" type="submit" value="驳 回" onclick="$('#flag').val('no');$('#to').attr('style','display:none')"/>&nbsp;
			</c:if>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
		<act:histoicFlow procInsId="${oaCostReimbursement.act.procInsId}" />
		<legend>流程图</legend>
		<iframe style="height: 600px;width: 100%" src="${pageContext.request.contextPath}/act/diagram-viewer?processDefinitionId=${oaCostReimbursement.act.histTask.processDefinitionId}&processInstanceId=${oaCostReimbursement.act.histTask.processInstanceId}"></iframe>
	</form:form>
</body>
</html>