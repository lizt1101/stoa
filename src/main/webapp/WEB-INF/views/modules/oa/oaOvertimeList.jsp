<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>加班申请管理</title>
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
		<li class="active"><a href="${ctx}/oa/oaOvertime/">加班申请列表</a></li>
		<shiro:hasPermission name="oa:oaOvertime:edit"><li><a href="${ctx}/oa/oaOvertime/form">加班申请添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="oaOvertime" action="${ctx}/oa/oaOvertime/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>标题：</label>
				<form:input path="title" htmlEscape="false" maxlength="64" class="input-medium"/>
			</li>
			<li><label>紧急程度1-正常2-重要3-紧急：</label>
				<form:radiobuttons path="degree" items="${fns:getDictList('degree')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
			</li>
			<li><label>加班类型1-工作日2-休息日3-节假日4-其他：</label>
				<form:select path="type" class="input-medium">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('overtime')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>标题</th>
				<th>紧急程度1-正常2-重要3-紧急</th>
				<th>加班类型1-工作日2-休息日3-节假日4-其他</th>
				<th>开始时间</th>
				<th>结束时间</th>
				<th>加班时长</th>
				<th>更新时间</th>
				<th>备注</th>
				<shiro:hasPermission name="oa:oaOvertime:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="oaOvertime">
			<tr>
				<td><a href="${ctx}/oa/OaOvertime/form?id=${oaOvertime.id}">
					${oaOvertime.title}
				</a></td>
				<td>
					${fns:getDictLabel(oaOvertime.degree, 'degree', '')}
				</td>
				<td>
					${fns:getDictLabel(oaOvertime.type, 'overtime', '')}
				</td>
				<td>
					<fmt:formatDate value="${oaOvertime.startTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					<fmt:formatDate value="${oaOvertime.endTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${oaOvertime.timeLong}
				</td>
				<td>
					<fmt:formatDate value="${oaOvertime.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${oaOvertime.remarks}
				</td>
				<shiro:hasPermission name="oa:oaOvertime:edit"><td>
    				<a href="${ctx}/oa/oaOvertime/form?id=${oaOvertime.id}">修改</a>
					<a href="${ctx}/oa/oaOvertime/delete?id=${oaOvertime.id}" onclick="return confirmx('确认要删除该加班申请吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>