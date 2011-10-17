<%@ include file="/WEB-INF/jsp/taglib.jsp" %>

<h1><fmt:message key="pageTitle.auth/newpi/institute.label" /></h1>
<font color="red"><wasp:message /></font> 
<p><fmt:message key="piPending.select_institute_message.label" /></p>
<div>
	<form name ="f" method="POST" action="<c:url value="/auth/newpi/institute.do"/>">
	<table>
	<tr><td><fmt:message key='piPending.select_institute.label'/>: </td>
	<td><select name="instituteSelect" onchange=" return selectChange();">
		<option value=''><fmt:message key="wasp.default_select.label"/></option>
		<c:forEach items="${instituteList}" var="institute" varStatus="status">
			<option value='${institute}'>${institute}</option>
		</c:forEach>
		<option value='other'>other</option>
	</select></td>
	</tr>
	<tr>
	<td><fmt:message key='piPending.other_institute.label'/>: </td>
	<td><input type="text" name="instituteOther" id="instituteOther" readonly="readonly" value=""/></td>
	</tr>
	<tr><td colspan="2">
	<input type="submit" value="<fmt:message key='piPending.select_institute_submit.label'/>" /> 
	</td></tr>
	</table>
</div>
