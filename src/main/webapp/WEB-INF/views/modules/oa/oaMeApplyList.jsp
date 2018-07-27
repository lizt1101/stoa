<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>我的申请管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			
		});
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
        	return false;
        }
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/oa/oaMeApply/">我的申请列表</a></li>
		<%--<shiro:hasPermission name="oa:oaMeApply:edit"><li><a href="${ctx}/oa/oaMeApply/form">我的申请添加</a></li></shiro:hasPermission>--%>
	</ul>
	<form:form id="searchForm" modelAttribute="oaMeApply" action="${ctx}/oa/oaMeApply/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>申请名称：</label>
				<form:input path="name" htmlEscape="false" maxlength="24" class="input-medium"/>
			</li>
			<li><label>创建时间：</label>
				<input name="beginCreateDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${oaMeApply.beginCreateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/> - 
				<input name="endCreateDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${oaMeApply.endCreateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
			</li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>申请名称</th>
				<th>创建时间</th>
				<th>当前环节</th>
				<th>流程更新时间</th>
				<th>状态</th>
				<shiro:hasPermission name="oa:oaMeApply:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="oaMeApply">
			<c:set var="task" value="${oaMeApply.act.histTask}" />
			<c:set var="vars" value="${oaMeApply.act.vars}" />
			<c:set var="procDef" value="${oaMeApply.act.procDef}" />
			<c:set var="status" value="${oaMeApply.status}" />
			<tr>
				<td>
					${oaMeApply.name}
				</td>
				<td>
					<fmt:formatDate value="${oaMeApply.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
						<c:if test="${oaMeApply.status==1}">
							结束
						</c:if>
						<c:if test="${oaMeApply.status==0}">
							${task.name}
						</c:if>
						<c:if test="${oaMeApply.status==2}">
							已撤销
						</c:if>
						<c:if test="${oaMeApply.status==3}">
							流程中断
						</c:if>
				</td>
				<td>
					<c:choose>
						<c:when test="${task.endTime==null}">
							<fmt:formatDate value="${task.startTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
						</c:when>
						<c:otherwise>
							<fmt:formatDate value="${task.endTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
						</c:otherwise>
					</c:choose>

				</td>
				<td>
					${fns:getDictLabel(oaMeApply.status, 'apply_status', '')}
				</td>
				<shiro:hasPermission name="oa:oaMeApply:edit"><td>
					<a href="${ctx}/act/task/form?taskId=${task.id}&taskName=${fns:urlEncode(task.name)}&taskDefKey=${task.taskDefinitionKey}&procInsId=${task.processInstanceId}&procDefId=${task.processDefinitionId}&status=${status}&detailFlag=1">查看</a>
					<c:if test="${oaMeApply.status==0}">
						<a target="_blank" href="${pageContext.request.contextPath}/act/diagram-viewer?processDefinitionId=${task.processDefinitionId}&processInstanceId=${task.processInstanceId}">跟踪</a>
					</c:if>
					<%--<a href="${ctx}/oa/oaMeApply/delete?id=${oaMeApply.id}" onclick="return confirmx('确认要删除该我的申请吗？', this.href)">删除</a>--%>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>