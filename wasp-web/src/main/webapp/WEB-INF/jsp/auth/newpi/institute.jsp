<%@ include file="/WEB-INF/jsp/taglib.jsp" %>

<font color="red"><wasp:message /></font> 
<h1><fmt:message key="pageTitle.auth/newpi/institute.label" /></h1>

<div class="instructions"><fmt:message key="piPending.select_institute_message.label" /></div>

<div>
	<form name ="f" method="POST" action="<c:url value="/auth/newpi/institute.do"/>">
	<table class="data">
	<tr>
	<td class="label"><fmt:message key='piPending.select_institute.label'/></td>
	<td class="input"><select name="instituteSelect" onchange=" return selectChange();">
		<option value=''><fmt:message key="wasp.default_select.label"/></option>
		<c:forEach items="${instituteList}" var="institute" varStatus="status">
			<option value='${institute}'>${institute}</option>
		</c:forEach>
		<option value='other'>other</option>
	</select></td>
	</tr>
	<tr>
	<td class="label"><fmt:message key='piPending.other_institute.label'/>: </td>
	<td class="input"><input type="text" name="instituteOther" id="instituteOther" readonly="readonly" value=""/></td>
	</tr>
	</table>
	<div>
	<input type="submit" value="<fmt:message key='piPending.select_institute_submit.label'/>" /> 
	</div>
</div>
