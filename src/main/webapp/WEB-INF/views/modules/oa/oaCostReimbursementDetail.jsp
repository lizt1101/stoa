<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>费用报销详情</title>
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
    function doPrint($obj) {
        var html = $('body').html()
        var printHtml = $obj.prop('outerHTML')
        $('body').html(printHtml)
        window.print(); //调用浏览器的打印功能打印指定区域
        $('body').html(html); // 最后还原页面
    }
	</script>
	<style>
		#dy tr,td{
			border: 1px solid #000;
			text-align: center;
		}
		#dy td{
			width : 7% ;
			word-break : break-all;
		}
		#dy tr{
			height: 35px;
		}
	</style>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="#">费用报销详情</a></li>
	</ul><br/>
	<c:if test="${oaCostReimbursement.act.status=='1'}">
	<div style="width: 100%;height: 40px">
		<input id="btnDy" class="btn" type="button" value="打 印" onclick="doPrint($('#dy'))"/>
	</div>
	</c:if>
	<legend>报销费用详情</legend>
	<div id="dy" style="width: 90%;margin-left: 5%">
	<form:form id="inputForm" modelAttribute="oaCostReimbursement" class="form-horizontal">
		<table id="tab" style="font-family: 新宋体;font-size:13px;width: 100%;word-wrap:break-word; word-break:break-all;">
				<tr style="border: 1px solid #fff">
					<td colspan="6" style="border: 0;text-align: left">
						<img style="width: 28px;height: 28px;" src="${pageContext.request.contextPath}/static/stImage/st.png">
					</td>
					<td colspan="2" style="text-align: right;font-weight: bold;font-size: 15px;border: 0;">云南术天科技有限公司</td>
				</tr>
				<tr>
					<td colspan="7" class="tit" style="font-weight: bold;">费用报销单</td>
					<td colspan="1" style="text-align: left;width: 13%"><span style="font-weight: bold;">编号:</span>
							${oaCostReimbursement.number}
					</td>
				</tr>
				<tr>
					<td class="tit" colspan="1" style="font-weight:bold;width: 6%">报销人</td>
					<td colspan="1" class="tit">
						${oaCostReimbursement.user.name}
					</td>
					<td class="tit" colspan="1" style="font-weight:bold;width: 5%">部&nbsp门</td>
					<td colspan="1" class="tit">
						${oaCostReimbursement.office.name}
					</td>
					<td class="tit" colspan="1" style="font-weight:bold;width: 5%">职&nbsp位</td>
					<td colspan="1" class="tit" style="width: 12%">
						${oaCostReimbursement.reimPosition}
					</td>
					<td class="tit" colspan="1" style="font-weight:bold;width: 5%">报销时间</td>
					<td colspan="1" class="tit">
						<fmt:formatDate value="${oaCostReimbursement.createDate}" pattern="yyyy年MM月dd日"/>
					</td>
				</tr>
				<tr>
					<td class="tit" colspan="3" style="font-weight:bold">摘&nbsp&nbsp要</td>
					<td class="tit" colspan="1" style="font-weight:bold">项&nbsp&nbsp目</td>
					<td class="tit" colspan="2" style="font-weight:bold">金额</td>
					<td class="tit" colspan="1" style="font-weight:bold">单据张数</td>
					<td class="tit" colspan="1" style="font-weight:bold">备 注</td>
				</tr>
				<c:set var="mxSize" value="${fn:length(oaCostReimbursement.oaCostReimDetaileds)}"/>
				<c:forEach items="${oaCostReimbursement.oaCostReimDetaileds}" var="detail" varStatus="status">
					<tr>
						<td colspan="3">
							${detail.summary}
						</td>
						<td class="tit" colspan="1">
							${detail.project}
						</td>
						<td class="tit" colspan="2">
							${detail.money}
						</td>
						<td class="tit" colspan="1">
							${detail.billCount}
						</td>
						<td colspan="1">
							${detail.remarks}
						</td>
					</tr>
				</c:forEach>
				<c:if test="${mxSize<4}">
					<c:forEach begin="0" end="${3-mxSize}" var="i">
						<tr>
							<td colspan="3">
								&nbsp
							</td>
							<td class="tit" colspan="1">
							</td>
							<td class="tit" colspan="2">
							</td>
							<td class="tit" colspan="1">
							</td>
							<td colspan="1">
							</td>
						</tr>
					</c:forEach>
				</c:if>
				<tr>
					<td class="tit" colspan="4" style="font-weight:bold">合&nbsp&nbsp&nbsp&nbsp计</td>
					<td class="tit" colspan="2" id="totalCount">${oaCostReimbursement.totalMoney}</td>
					<td class="tit" colspan="1" id="costCount">${oaCostReimbursement.costCount}</td>
					<td class="tit" colspan="1"></td>
				</tr>
				<tr>
					<td class="tit" colspan="2" style="font-weight:bold">合计人民币(大写)</td>
					<td class="tit" colspan="6" id="chinese">${oaCostReimbursement.totalBigMoney}</td>
				</tr>
				<tr>
					<td class="tit" colspan="2" style="font-weight:bold">部门负责人意见</td>
					<td class="tit" colspan="6">
						<c:if test="${empty oaCostReimbursement.oaOpinions[0]}"></c:if>
						<c:if test="${not empty oaCostReimbursement.oaOpinions[0]}">${oaCostReimbursement.oaOpinions[0].content}</c:if>
					</td>
				</tr>
				<tr>
					<td class="tit" colspan="2" style="font-weight:bold">会计复核</td>
					<td class="tit" colspan="6">
								<c:if test="${empty oaCostReimbursement.oaOpinions[1]}"></c:if>
								<c:if test="${not empty oaCostReimbursement.oaOpinions[1]}">${oaCostReimbursement.oaOpinions[1].content}</c:if>
					</td>
				</tr>
				<tr>
					<td class="tit" colspan="2" style="font-weight:bold">财务部负责人意见</td>
					<td class="tit" colspan="6">
						<c:if test="${empty oaCostReimbursement.oaOpinions[2]}"></c:if>
						<c:if test="${not empty oaCostReimbursement.oaOpinions[2]}">${oaCostReimbursement.oaOpinions[2].content}</c:if>
					</td>
				</tr>
				<tr>
					<td class="tit" colspan="2" style="font-weight:bold">总经理意见</td>
					<td class="tit" colspan="6">
								<c:if test="${empty oaCostReimbursement.oaOpinions[3]}"></c:if>
								<c:if test="${not empty oaCostReimbursement.oaOpinions[3]}">${oaCostReimbursement.oaOpinions[3].content}</c:if>
					</td>
				</tr>
				<tr style="border: 1px solid #fff">
					<td class="tit" colspan="2" style="font-weight:bold;border: 0"></td>
					<td class="tit" colspan="2" style="font-weight:bold;border: 0"></td>
					<td class="tit" colspan="2" style="font-weight:bold;border: 0;text-align: left">报销人签字:</td>
					<td colspan="2" style="font-weight:bold;border: 0;text-align: left">日&nbsp&nbsp&nbsp期:
					</td>
				</tr>
			</table>
	</form:form>
	</div>
	<input id="btnCancel" style="margin-top: 20px" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
	<act:histoicFlow procInsId="${oaCostReimbursement.act.procInsId}" />
	<c:if test="${oaCostReimbursement.act.status=='0'}">
	<legend>流程图</legend>
	<iframe style="height: 600px;width: 100%" src="${pageContext.request.contextPath}/act/diagram-viewer?processDefinitionId=${oaCostReimbursement.act.histTask.processDefinitionId}&processInstanceId=${oaCostReimbursement.act.histTask.processInstanceId}"></iframe>
	</c:if>
</body>
</html>