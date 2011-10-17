<%@ include file="/WEB-INF/jsp/taglib.jsp" %>

	<div>
	  <h1><fmt:message key="department.create.label" /></h1>
	  <font color="red"><wasp:message /></font>
    	<form name="f" action="<c:url value='/department/create.do'/>" method="POST" onsubmit='return validate();'>
      		<fmt:message key="department.list_department.label" />: <input type='text' name='name' value=''/><br/>
      		<input type="submit" value="<fmt:message key="department.list.data" />" />
    	</form>
	</div>

	<h1><fmt:message key="department.list.label" /></h1>
	<table cellpadding="0" cellspacing="0" border="0">
	<c:forEach items="${department}" var="d">
		<div>
		<tr>
		<td>
			<a href="/wasp/department/detail/<c:out value="${d.departmentId}" />.do"><c:out value="${d.name}" /></a>
		</td>
		</tr>
	</c:forEach>
</table>
