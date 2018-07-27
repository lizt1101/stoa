<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>费用报销管理</title>
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
		<li class="active"><a href="${ctx}/oa/oaCostReimbursement/">费用报销列表</a></li>
		<li><a href="${ctx}/oa/oaCostReimbursement/form">费用报销添加</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="oaCostReimbursement" action="${ctx}/oa/oaCostReimbursement/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>报销人</th>
				<th>部门</th>
				<th>报销人职位</th>
				<th>合计金额(数字)</th>
				<th>合计金额（汉字大写）</th>
				<th>创建人</th>
				<th>更新时间</th>
				<shiro:hasPermission name="oa:oaCostReimbursement:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="oaCostReimbursement">
			<tr>
				<td><a href="${ctx}/oa/oaCostReimbursement/form?id=${oaCostReimbursement.id}">
					${oaCostReimbursement.user.name}
				</a></td>
				<td>
					${oaCostReimbursement.office.name}
				</td>
				<td>
					${oaCostReimbursement.reimPosition}
				</td>
				<td>
					${oaCostReimbursement.totalMoney}
				</td>
				<td>
					${oaCostReimbursement.totalBigMoney}
				</td>
				<td>
					${oaCostReimbursement.createBy.id}
				</td>
				<td>
					<fmt:formatDate value="${oaCostReimbursement.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
    				<a href="${ctx}/oa/oaCostReimbursement/form?id=${oaCostReimbursement.id}">修改</a>
					<a href="${ctx}/oa/oaCostReimbursement/delete?id=${oaCostReimbursement.id}" onclick="return confirmx('确认要删除该费用报销吗？', this.href)">删除</a>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>