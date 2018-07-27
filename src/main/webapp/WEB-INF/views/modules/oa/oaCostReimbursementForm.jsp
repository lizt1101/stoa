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
                    total1();
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
		function addH(){
		    tab1 = document.getElementById("tab");//table1为table的id
            var count = tab1.rows.length; //总行数
            var countm = count-9; //明细行数
            var add1 = countm+2; //在哪一行后添加一行
            var index = countm+1;
            var html = "<tr>" +
                "<td class='tit' colspan='4'><input required=\"required\" name='oaCostReimDetaileds["+index+"].summary' htmlEscape='false' maxlength='50' style=\"width: 90%\"/></td>" +
                "<td class='tit' colspan='2'><input required=\"required\" name='oaCostReimDetaileds["+index+"].project' htmlEscape='false' maxlength='50' style=\"width: 90%\"/></td>" +
                "<td class='tit' colspan='3'><input required=\"required\" class='total' onchange='total1()' onkeyup='total()' name='oaCostReimDetaileds["+index+"].money' htmlEscape='false' maxlength='50' style=\"width: 90%\"/></td>" +
                "<td class='tit' colspan='1'><input required=\"required\" class='costCount' onkeyup='costCount()' name='oaCostReimDetaileds["+index+"].billCount' htmlEscape='false' maxlength='50' style=\"width: 90%\"/></td>" +
                "<td class='tit' colspan='2'><input name='oaCostReimDetaileds["+index+"].remarks' htmlEscape='false' maxlength='50' style=\"width: 90%\"/></td>" +
				"<td class=\"tit\"><a class='delH' onclick='delH(this)'>-</a></td>" +
                "</tr>";
            $("#tab tr:eq("+add1+")").after(html);
		}
    function delH(obj) {
        $(obj).parent().parent().remove();
        total();
        costCount();
    }
    function total1(){
        var total=0;
        $(".total").each(function(){
            total = addNum (total, $(this).val());
        })
        $.post("${ctx}/oa/oaCostReimbursement/getChinese",{"total":total},function(result){
            $("#chinese").text(result.chineseCost);
            $(".chinese1").val(result.chineseCost);
        },'Json');
    }
    function costCount(){
        var total= 0;
        $(".costCount").each(function(){
            total = addNum (total, $(this).val());
        })
        $("#costCount").text(total);
	}
    function total(){
		var total=0;
		$(".total").each(function(){
		    total = addNum (total, $(this).val());
		})
		$("#totalCount").text(total);
        $(".totalCount1").val(total);
	}
    function addNum (num1, num2) {
        var sq1,sq2,m;
        try {
            sq1 = num1.toString().split(".")[1].length;
        }
        catch (e) {
            sq1 = 0;
        }
        try {
            sq2 = num2.toString().split(".")[1].length;
        }
        catch (e) {
            sq2 = 0;
        }
        m = Math.pow(10,Math.max(sq1, sq2));
        return (num1 * m + num2 * m) / m;
    }
	</script>
    <style>
        .delH,.addH{
            font-size: 20px;
            color: red;
        }
        .addH:hover{
            text-decoration: none;
            cursor:pointer;
            font-size: 25px;
            color: red;
        }
        .delH:hover{
            text-decoration: none;
            cursor:pointer;
            font-size: 25px;
            color: red;
        }
    </style>
</head>
<body>
	<ul class="nav nav-tabs">
		<%--<li><a href="${ctx}/oa/oaCostReimbursement/">费用报销列表</a></li>--%>
		<li class="active"><a href="#">费用报销<c:if test="${not empty oaCostReimbursement.id}">重申</c:if><c:if test="${empty oaCostReimbursement.id}">申请</c:if></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="oaCostReimbursement" action="${ctx}/oa/oaCostReimbursement/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<form:hidden path="act.taskId"/>
		<form:hidden path="act.taskName"/>
		<form:hidden path="act.taskDefKey"/>
		<form:hidden path="act.procInsId"/>
		<form:hidden path="act.procDefId"/>
		<form:hidden id="flag" path="act.flag"/>
		<form:hidden path="totalMoney" class="totalCount1"/>
		<form:hidden path="totalBigMoney" class="chinese1"/>
		<sys:message content="${message}"/>
		<fieldset>
			<legend>费用报销申请</legend>
			<table class="table-form" id="tab">
				<tr>
					<td colspan="11" class="tit">费用报销单</td>
					<td colspan=""  class="tit">编号:<form:input path="number" htmlEscape="false" maxlength="20" class="required"/>
						<span class="help-inline"><font color="red">*</font> </span>
					</td>
					<td class="tit"></td>
				</tr>
				<tr>
					<td class="tit">报销人 :</td>
					<td colspan="2" class="tit"><sys:treeselect id="user" name="user.id" value="${oaCostReimbursement.user.id}" labelName="user.name" labelValue="${oaCostReimbursement.user.name}"
									title="用户" url="/sys/office/treeData?type=3" cssClass="required recipient" cssStyle="width:150px"
									allowClear="true" notAllowSelectParent="true" smallBtn="false"/>
						<span class="help-inline"><font color="red">*</font> </span>
					</td>
					<td class="tit">部门 :</td>
					<td colspan="2" class="tit"><sys:treeselect id="office" name="office.id" value="${oaCostReimbursement.office.id}" labelName="office.name" labelValue="${oaCostReimbursement.office.name}"
									title="用户" url="/sys/office/treeData?type=2" cssClass="required recipient" cssStyle="width:150px"
									allowClear="true" notAllowSelectParent="true" smallBtn="false"/>
						<span class="help-inline"><font color="red">*</font> </span>
					</td>
					<td class="tit">职位 :</td>
					<td colspan="2" class="tit">
					<form:input path="reimPosition" htmlEscape="false" maxlength="50" class="required"/>
						<span class="help-inline"><font color="red">*</font> </span>
					</td>
					<td class="tit">报销时间 :</td>
					<td colspan="2" class="tit">
						<fmt:formatDate value="${oaCostReimbursement.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
					</td>
					<td class="tit"></td>
				</tr>
				<tr>
					<td class="tit" colspan="4">摘要<span class="help-inline"><font color="red">*</font> </span></td>
					<td class="tit" colspan="2">项目<span class="help-inline"><font color="red">*</font> </span></td>
					<td class="tit" colspan="3">金额<span class="help-inline"><font color="red">*</font> </span></td>
					<td class="tit" colspan="1">单据张数<span class="help-inline"><font color="red">*</font> </span></td>
					<td class="tit" colspan="2">备注
						<span class="help-inline"><font color="red">*</font> </span>
					</td>
					<td class="tit"><a class="addH" onclick="addH()">+</a></td>
				</tr>
				<c:if test="${not empty oaCostReimbursement.id}">
					<c:forEach items="${oaCostReimbursement.oaCostReimDetaileds}" var="detail" varStatus="status">
						<tr>
							<input value="${detail.id}" name="oaCostReimDetaileds[${status.index}].id" type="hidden"/>
							<td class="tit" colspan="4">
								<input required="required" name="oaCostReimDetaileds[${status.index}].summary" value="${detail.summary}" htmlEscape="false" maxlength="50" style="width: 90%"/>
							</td>
							<td class="tit" colspan="2">
								<input required="required" name="oaCostReimDetaileds[${status.index}].project" value="${detail.project}" htmlEscape="false" maxlength="50" style="width: 90%"/>
							</td>
							<td class="tit" colspan="3">
								<input required="required" onkeyup="total()" onchange='total1()' class='total' value="${detail.money}" name="oaCostReimDetaileds[${status.index}].money" htmlEscape="false" maxlength="12" style="width: 90%"/>
							</td>
							<td class="tit" colspan="1">
								<input required="required" class='costCount'  onkeyup="costCount()" value="${detail.billCount}" name="oaCostReimDetaileds[${status.index}].billCount" htmlEscape="false" maxlength="2" style="width: 90%"/>
							</td>
							<td class="tit" colspan="2">
								<input name="oaCostReimDetaileds[${status.index}].remarks" value="${detail.remarks}" htmlEscape="false" maxlength="50" style="width: 90%"/>
							</td>
							<td class="tit"><a class='delH' onclick='delH(this)'>-</a></td>
						</tr>
					</c:forEach>
				</c:if>
				<c:if test="${empty oaCostReimbursement.id}">
					<tr>
						<td class="tit" colspan="4">
							<input required="required" name="oaCostReimDetaileds[0].summary" htmlEscape="false" maxlength="50" style="width: 90%"/>
						</td>
						<td class="tit" colspan="2">
							<input required="required" name="oaCostReimDetaileds[0].project" htmlEscape="false" maxlength="50" style="width: 90%"/>
						</td>
						<td class="tit" colspan="3">
							<input required="required" type="number" step="0.01" onkeyup="total()" onchange='total1()' class='total' name="oaCostReimDetaileds[0].money" htmlEscape="false" maxlength="12" style="width: 90%"/>
						</td>
						<td class="tit" colspan="1">
							<input required="required" type="number" class='costCount' onkeyup='costCount()' name="oaCostReimDetaileds[0].billCount" htmlEscape="false" maxlength="11" style="width: 90%"/>
						</td>
						<td class="tit" colspan="2">
							<input name="oaCostReimDetaileds[0].remarks" htmlEscape="false" maxlength="50" style="width: 90%"/>
						</td>
						<td class="tit"><a class='delH' onclick='delH(this)'>-</a></td>
					</tr>
				</c:if>
				<tr>
					<td class="tit" colspan="6">合计:</td>
					<td class="tit" colspan="3" id="totalCount">${oaCostReimbursement.totalMoney}</td>
					<td class="tit" colspan="1" id="costCount">${oaCostReimbursement.costCount}</td>
					<td class="tit" colspan="2"></td>
					<td class="tit"></td>
				</tr>
				<tr>
					<td class="tit" colspan="3">合计人民币(大写) :</td>
					<td class="tit" colspan="9" id="chinese">${oaCostReimbursement.totalBigMoney}</td>
					<td class="tit"></td>
				</tr>
				<c:if test="${empty oaCostReimbursement.oaOpinions}">
					<tr>
						<td class="tit" colspan="3">部门负责人意见 :</td>
						<td class="tit" colspan="9"></td>
						<td class="tit"></td>
					</tr>
					<tr>
						<td class="tit" colspan="3">会计复核 :</td>
						<td class="tit" colspan="9">
						</td>
						<td class="tit"></td>
					</tr>
					<tr>
						<td class="tit" colspan="3">财务部负责人意见 :</td>
						<td class="tit" colspan="9">
						</td>
						<td class="tit"></td>
					</tr>
					<tr>
						<td class="tit" colspan="3">总经理意见 :</td>
						<td class="tit" colspan="9">
						</td>
						<td class="tit"></td>
					</tr>
				</c:if>
				<c:if test="${not empty oaCostReimbursement.oaOpinions}">
					<tr>
						<td class="tit" colspan="3">部门负责人意见 :</td>
						<td class="tit" colspan="9">${oaCostReimbursement.oaOpinions[0].content}</td>
						<td class="tit"></td>
					</tr>
					<tr>
						<td class="tit" colspan="3">会计复核 :</td>
						<td class="tit" colspan="9">
							<c:if test="${empty oaCostReimbursement.oaOpinions[1]}"></c:if>
							<c:if test="${not empty oaCostReimbursement.oaOpinions[1]}">${oaCostReimbursement.oaOpinions[1].content}</c:if>
						</td>
						<td class="tit"></td>
					</tr>
					<tr>
						<td class="tit" colspan="3">财务部负责人意见 :</td>
						<td class="tit" colspan="9">
							<c:if test="${empty oaCostReimbursement.oaOpinions[2]}"></c:if>
							<c:if test="${not empty oaCostReimbursement.oaOpinions[2]}">${oaCostReimbursement.oaOpinions[2].content}</c:if>
						</td>
						<td class="tit"></td>
					</tr>
					<tr>
						<td class="tit" colspan="3">总经理意见 :</td>
						<td class="tit" colspan="9">
							<c:if test="${empty oaCostReimbursement.oaOpinions[3]}"></c:if>
							<c:if test="${not empty oaCostReimbursement.oaOpinions[3]}">${oaCostReimbursement.oaOpinions[3].content}</c:if>
						</td>
						<td class="tit"></td>
					</tr>
				</c:if>
			</table>
		</fieldset>
		<div style="margin-top: 20px;" id="to">
			<label>提交给：</label>	<c:if test="${not empty oaCostReimbursement.id}"><span style="color: red">注:撤销申请不必选择提交人</span></c:if>
			<div>
				<sys:treeselect id="toUser" name="toUser.id" value="${oaCostReimbursement.toUser.name}" labelName="toUser.name" labelValue="${oaCostReimbursement.toUser.name}"
								title="用户" url="/sys/office/officeTreeData?type=3" cssClass="required recipient" cssStyle="width:150px"
								allowClear="true" notAllowSelectParent="true" smallBtn="false"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
		</div>
		<div class="form-actions">
			<input id="btnSubmit" class="btn btn-primary" type="submit" value="提交申请" onclick="$('#flag').val('yes');$('#to').attr('style','')"/>&nbsp;
			<c:if test="${not empty oaCostReimbursement.id}">
				<input id="btnSubmit2" class="btn btn-inverse" type="submit" value="撤销申请" onclick="$('#flag').val('no');$('#to').attr('style','display:none')"/>&nbsp;
			</c:if>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
		<c:if test="${not empty oaCostReimbursement.id}">
			<act:histoicFlow procInsId="${oaCostReimbursement.act.procInsId}" />
			<legend>流程图</legend>
			<iframe style="height: 600px;width: 100%" src="${pageContext.request.contextPath}/act/diagram-viewer?processDefinitionId=${oaCostReimbursement.act.histTask.processDefinitionId}&processInstanceId=${oaCostReimbursement.act.histTask.processInstanceId}"></iframe>
		</c:if>
		</div>
	</form:form>
</body>
</html>